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

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpStatus;
import org.openo.crossdomain.commonsvc.decomposer.constant.Constant;
import org.openo.crossdomain.commonsvc.decomposer.constant.ErrorCode;
import org.openo.crossdomain.commonsvc.decomposer.logutil.TaskHeader;
import org.openo.crossdomain.commonsvc.decomposer.model.EnumProgress;
import org.openo.crossdomain.commonsvc.decomposer.model.Result;
import org.openo.crossdomain.commonsvc.decomposer.rest.util.RestfulMethod;
import org.openo.crossdomain.commonsvc.decomposer.rest.util.RestfulProxy;
import org.openo.crossdomain.commonsvc.decomposer.service.impl.ServiceOperateServiceImpl;
import org.openo.crossdomain.commonsvc.decomposer.util.DecomposerContextHelper;
import org.openo.crossdomain.commonsvc.decomposer.util.DecomposerUtil;
import org.openo.crossdomain.commonsvc.decomposer.util.ErrorCodeUtil;

import org.openo.commonservice.remoteservice.exception.ServiceException;
import com.huawei.icto.commsvc.jobscheduler.job.StepPolicy;

/**
 * wait decompose designer result job , this job to poll the designer, and to
 * process the results of the final return.<br>
 * 
 * @since crossdomain 0.5
 */
public class SerialDecomposeWaitReplayJob extends AbsDecomposeJob {

	/**
	 * run action
	 * 
	 * @return step policy
	 * @since crossdomain 0.5
	 */
	@Override
	public StepPolicy run() {
		ServiceOperateServiceImpl service = (ServiceOperateServiceImpl) DecomposerContextHelper
				.getInstance().getBean(
						Constant.SpringDefine.SRV_OPERATE_SERVICE);

		String attribute = jobBean.getAttribute();
		JSONObject attributeJson = JSONObject.fromObject(attribute);
		String url = attributeJson.getString(Constant.LOCATION);
		Map<String, String> mapHeader = new HashMap<String, String>();
		mapHeader.put(Constant.Security.X_AUTH_TOKEN,
				attributeJson.getString(Constant.TOKEN));
		mapHeader.put(Constant.Security.X_CLIENT_ADDR,
				attributeJson.getString(Constant.TERMINAL));

		boolean restarted = checkJobRestart(service, attributeJson);
		if (restarted) {

			getScheduler().deleteJob(jobBean.getId());
			return StepPolicy.createDeletedPolicy();
		}

		Result<String> result = RestfulProxy.restSend(RestfulMethod.GET, url,
				"", mapHeader);

		return processRestResult(service, attributeJson, result);
	}

	private StepPolicy processRestResult(ServiceOperateServiceImpl service,
			JSONObject attributeJson, Result<String> result) {
		logger.info("status code:" + result.getStatusCode());
		TaskHeader taskHeader = new TaskHeader(attributeJson);

		if (result.getStatusCode() == HttpStatus.SC_OK) {
			try {
				// check result
				ErrorCodeUtil.checkResult(logger, result,
						"decomposerResource fail",
						ErrorCode.SD_TASK_EXECUTE_FAIL);
				ErrorCodeUtil.hasLength(logger, result.getData(),
						"result.getData() is empty!",
						ErrorCode.SD_TASK_EXECUTE_FAIL);

				logger.info("process decompose result");

				String context = DecomposerUtil.decrypt(jobBean.getContext());
				service.processSerialDecomposerResourceResult(result, context,
						attributeJson);

				getScheduler().deleteJob(jobBean.getId());

				return StepPolicy.createDeletedPolicy();
			} catch (ServiceException e) {
				writeErrorLogAndUpdateTask(taskHeader,
						EnumProgress.DECOMPOSE.getName(),
						"Failed to waiting decompose Response", e.toString());
			}

		} else if (result.getStatusCode() == HttpStatus.SC_ACCEPTED) {

			return StepPolicy
					.createRescheduleLaterPolicy(AbsPersistentJob.RESCHEDULE_INTERVAL);
		} else {

			writeErrorLogAndUpdateTask(
					taskHeader,
					EnumProgress.DECOMPOSE.getName(),
					"Failed to waiting decompose Response",
					"Can not recognise the Http Status :"
							+ result.getStatusCode());
		}

		getScheduler().deleteJob(jobBean.getId());

		return StepPolicy.createDeletedPolicy();
	}
}
