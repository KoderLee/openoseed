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
import org.openo.crossdomain.commonsvc.decomposer.service.inf.IServiceOperateTask;
import org.openo.crossdomain.commonsvc.decomposer.util.DecomposerContextHelper;
import org.openo.crossdomain.commonsvc.decomposer.util.DecomposerUtil;
import org.openo.crossdomain.commonsvc.decomposer.util.ErrorCodeUtil;

import org.openo.commonservice.log.OssLog;
import org.openo.commonservice.log.OssLogFactory;
import org.openo.commonservice.remoteservice.exception.ServiceException;
import com.huawei.icto.commsvc.jobscheduler.job.StepPolicy;

/**
 * wait executor result job , this job to poll the executor, and to process the
 * results of the final return.<br>
 * 
 * @since crossdomain 0.5
 */
public class ExecuteWaitReplayJob extends AbsPersistentJob {

	/**
	 * logger
	 */
	private final OssLog logger = OssLogFactory
			.getLogger(ExecuteWaitReplayJob.class);

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
		TaskHeader taskHeader = new TaskHeader(attributeJson);
		Map<String, String> mapHeader = new HashMap<String, String>();
		mapHeader.put(Constant.Security.X_AUTH_TOKEN,
				attributeJson.getString(Constant.TOKEN));
		mapHeader.put(Constant.Security.X_CLIENT_ADDR,
				attributeJson.getString(Constant.TERMINAL));

		Result<String> result = RestfulProxy.restSend(RestfulMethod.GET, url,
				"", mapHeader);

		try {
			String progress = "";

			if (result.getStatusCode() == HttpStatus.SC_OK) {
				// check result
				ErrorCodeUtil.checkResult(logger, result, "Get " + url
						+ " error!", ErrorCode.SD_TASK_EXECUTE_FAIL);
				ErrorCodeUtil.hasLength(logger, result.getData(),
						"result.getData() is empty!",
						ErrorCode.SD_TASK_EXECUTE_FAIL);

				JSONObject srvDataJsonObject = JSONObject.fromObject(result
						.getData());
				JSONObject srvJobJsonObject = srvDataJsonObject
						.getJSONObject(Constant.SERVICE_JOB);
				progress = srvJobJsonObject.getString(Constant.PROGRESS);
				logger.warn("Get " + url + " Progress:" + progress);

				if (EnumProgress.COMPLETE.getName().equals(progress)) {

					String context = DecomposerUtil.decrypt(jobBean
							.getContext());
					service.processExecuteResult(result, context, attributeJson);

					String resultStr = srvJobJsonObject
							.getString(Constant.RESULT);
					String resultReason = srvJobJsonObject
							.getString(Constant.RESULT_REASON);

					IServiceOperateTask srvOperateTask = (IServiceOperateTask) DecomposerContextHelper
							.getInstance().getBean(
									Constant.SpringDefine.SRV_OPERATE_TASK);

					srvOperateTask.updateTaskStatus(taskHeader,
							EnumProgress.COMPLETE.getName(), resultStr,
							resultReason);
					getScheduler().deleteJob(jobBean.getId());

					return StepPolicy.createDeletedPolicy();
				} else {

					return StepPolicy
							.createRescheduleLaterPolicy(AbsPersistentJob.RESCHEDULE_INTERVAL);
				}
			}

			writeErrorLogAndUpdateTask(taskHeader,
					EnumProgress.EXECUTE.getName(),
					"Failed to waiting execution Response",
					"Exception handling. HttpStatus :" + result.getStatusCode()
							+ " progress:" + progress);
		} catch (ServiceException e) {
			writeErrorLogAndUpdateTask(taskHeader,
					EnumProgress.EXECUTE.getName(),
					"Failed to waiting execution Response", e.toString());
		}

		getScheduler().deleteJob(jobBean.getId());

		return StepPolicy.createDeletedPolicy();
	}

}
