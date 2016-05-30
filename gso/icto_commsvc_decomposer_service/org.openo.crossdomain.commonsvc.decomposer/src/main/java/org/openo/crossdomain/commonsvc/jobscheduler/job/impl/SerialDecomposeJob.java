/*******************************************************************************
 * Copyright (c) 2016, Huawei Technologies Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package org.openo.crossdomain.commonsvc.jobscheduler.job.impl;

import java.util.Iterator;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpStatus;
import org.openo.crossdomain.commonsvc.decomposer.constant.Constant;
import org.openo.crossdomain.commonsvc.decomposer.constant.ErrorCode;
import org.openo.crossdomain.commonsvc.decomposer.logutil.TaskHeader;
import org.openo.crossdomain.commonsvc.decomposer.model.EnumJobType;
import org.openo.crossdomain.commonsvc.decomposer.model.EnumProgress;
import org.openo.crossdomain.commonsvc.decomposer.model.Result;
import org.openo.crossdomain.commonsvc.decomposer.service.impl.ServiceOperateServiceImpl;
import org.openo.crossdomain.commonsvc.decomposer.util.DecomposerContextHelper;
import org.openo.crossdomain.commonsvc.decomposer.util.DecomposerUtil;
import org.openo.crossdomain.commonsvc.decomposer.util.ErrorCodeUtil;

import org.openo.commonservice.remoteservice.exception.ServiceException;
import com.huawei.icto.commsvc.jobscheduler.job.StepPolicy;

/**
 * design job , This job is to calling the designer and processing the results
 * returned by the designer.
 * 
 * @since crossdomain 0.5
 */
public class SerialDecomposeJob extends AbsDecomposeJob {

	/**
	 * run action
	 * 
	 * @return step policy
	 * @since crossdomain 0.5
	 */
	@Override
	public StepPolicy run() {
		String attribute = jobBean.getAttribute();
		JSONObject attributeJson = JSONObject.fromObject(attribute);
		// String serviceID = attributeJson.getString(Constant.SERVICEID);
		TaskHeader taskHeader = new TaskHeader(attributeJson);

		ServiceOperateServiceImpl service = (ServiceOperateServiceImpl) DecomposerContextHelper
				.getInstance().getBean(
						Constant.SpringDefine.SRV_OPERATE_SERVICE);

		boolean restarted = checkJobRestart(service, attributeJson);
		if (restarted) {

			getScheduler().deleteJob(jobBean.getId());
			return StepPolicy.createDeletedPolicy();
		}

		/*
		 * { "SDN/v1" : { resources : { "sdn_1" : { "name" : "sap1", "id" :
		 * "<sdn_1>", "type" : "SAP", "action" : "delete", "depends" : "" },
		 * "nfv_1" : { "name" : "vm1", "id" : "<nfv_1>", "type" : "VM", "action"
		 * : "create", "ipv4" : "1.1.1.1" } }, "status" :
		 * "Decomposed/Decomposing/WaitToDecompose" } }
		 */
		// @formatter:on
		try {

			JSONObject readyRes = prepareDecomposeResource(attributeJson);
			if (readyRes == null) {
				writeErrorLogAndUpdateTask(taskHeader,
						EnumProgress.DECOMPOSE.getName(), "decompose failed",
						"prepare process resource return is null");

				Result<String> result = new Result<String>();
				JSONObject dataJsonObject = new JSONObject();
				dataJsonObject.put(Constant.RESOURCES, new JSONObject());
				result.setData(dataJsonObject.toString());

				String context = DecomposerUtil.decrypt(jobBean.getContext());
				service.processSerialDecomposerResourceResult(result, context,
						attributeJson);
				getScheduler().deleteJob(jobBean.getId());
				return StepPolicy.createDeletedPolicy();
			}

			Result<String> result = service.decomposerResource(readyRes,
					attributeJson);

			return processRestResult(attributeJson, service, result);
		} catch (ServiceException e) {
			writeErrorLogAndUpdateTask(taskHeader,
					EnumProgress.DECOMPOSE.getName(), "decompose failed",
					e.toString());
			getScheduler().deleteJob(jobBean.getId());

			return StepPolicy.createDeletedPolicy();
		}
	}

	private StepPolicy processRestResult(final JSONObject attributeJson,
			final ServiceOperateServiceImpl service, final Result<String> result)
			throws ServiceException {
		logger.debug("decomposerResource result:" + result.getStatusCode());

		if (result.getStatusCode() == HttpStatus.SC_ACCEPTED) {
			JSONObject resultData = JSONObject.fromObject(result.getData());
			String location = resultData.getString(Constant.LOCATION);

			jobBean.setType(EnumJobType.DECOMPOSE_WAITREPLAY.getName());
			attributeJson.put(Constant.LOCATION, location);
			jobBean.setAttribute(attributeJson.toString());

			return StepPolicy.createToUpdateAndSchedulePolicy(5, jobBean);
		}

		else if (result.getStatusCode() == HttpStatus.SC_OK) {
			// check result
			ErrorCodeUtil.checkResult(logger, result,
					"decomposerResource fail", ErrorCode.SD_TASK_EXECUTE_FAIL);
			ErrorCodeUtil.hasLength(logger, result.getData(),
					"result.getData() is empty!",
					ErrorCode.SD_TASK_EXECUTE_FAIL);

			String context = DecomposerUtil.decrypt(jobBean.getContext());
			service.processSerialDecomposerResourceResult(result, context,
					attributeJson);

			getScheduler().deleteJob(jobBean.getId());

			return StepPolicy.createDeletedPolicy();
		} else {
			TaskHeader taskHeader = new TaskHeader(attributeJson);
			writeErrorLogAndUpdateTask(
					taskHeader,
					EnumProgress.DECOMPOSE.getName(),
					"decompose failed",
					"Can not recognise the HttpStatus :"
							+ result.getStatusCode());
		}

		getScheduler().deleteJob(jobBean.getId());

		return StepPolicy.createDeletedPolicy();
	}

	private JSONObject prepareDecomposeResource(final JSONObject attributeJson)
			throws ServiceException {

		String context = DecomposerUtil.decrypt(jobBean.getContext());
		JSONObject contextJsonObject = JSONObject.fromObject(context);
		Iterator<?> it = contextJsonObject.keys();

		while (it.hasNext()) {
			String domainKey = (String) it.next();
			JSONObject aDomainResJsonObject = contextJsonObject
					.getJSONObject(domainKey);
			String status = aDomainResJsonObject.getString(Constant.STATUS);
			if (EnumProgress.READY.getName().equals(status)) {
				attributeJson.put(Constant.VERSION_DOMAIN, domainKey);

				// contextJsonObject.getJSONObject(domainKey).put(Constant.STATUS,
				// EnumProgress.DECOMPOSE.getName());
				contextJsonObject.remove(domainKey);
				JSONObject resJsonObject = aDomainResJsonObject
						.getJSONObject(Constant.RESOURCES);
				contextJsonObject.put(Constant.DEC_RESOURCES, resJsonObject);

				jobBean.setContext(DecomposerUtil.encrypt(contextJsonObject
						.toString()));

				return resJsonObject;
			}
		}

		return null;
	}
}
