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
import org.apache.commons.lang.StringUtils;
import org.openo.crossdomain.commonsvc.decomposer.constant.Constant;
import org.openo.crossdomain.commonsvc.decomposer.constant.ConstantURL;
import org.openo.crossdomain.commonsvc.decomposer.constant.ErrorCode;
import org.openo.crossdomain.commonsvc.decomposer.logutil.TaskHeader;
import org.openo.crossdomain.commonsvc.decomposer.model.EnumJobType;
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
import com.huawei.icto.commsvc.jobscheduler.job.StepPolicy;

/**
 * execute job , This job is to calling the executor and processing the results
 * returned by the executor.
 * 
 * @since crossdomain 0.5
 */
public class ExecuteJob extends AbsPersistentJob {

	/**
	 * logger
	 */
	private final OssLog logger = OssLogFactory.getLogger(ExecuteJob.class);

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
		JSONObject attributeJson = JSONObject
				.fromObject(jobBean.getAttribute());
		String taskID = attributeJson.getString(Constant.TASK_ID);
		String tenantID = attributeJson.getString(Constant.TENANT_ID);
		TaskHeader taskHeader = new TaskHeader(attributeJson);
		Map<String, String> mapHeader = new HashMap<String, String>();
		mapHeader.put(Constant.Security.X_AUTH_TOKEN,
				attributeJson.getString(Constant.TOKEN));
		mapHeader.put(Constant.Security.X_CLIENT_ADDR,
				attributeJson.getString(Constant.TERMINAL));

		try {

			String context = DecomposerUtil.decrypt(jobBean.getContext());
			Result<String> result = RestfulProxy.restSend(RestfulMethod.POST,
					ConstantURL.CREATE_SE_JOB, context, mapHeader);

			if (result.getStatusCode() == HttpStatus.SC_ACCEPTED) {
				JSONObject resultData = JSONObject.fromObject(result.getData());
				String location = resultData.getString(Constant.LOCATION);

				String jobID = getJobIDByLocation(location);
				IServiceOperateTask srvOperateTask = (IServiceOperateTask) DecomposerContextHelper
						.getInstance().getBean(
								Constant.SpringDefine.SRV_OPERATE_TASK);
				srvOperateTask.updateTaskJobID(taskID, tenantID, jobID);

				jobBean.setType(EnumJobType.EXECUTE_WAITREPLAY.getName());
				attributeJson.put(Constant.LOCATION, location);
				jobBean.setAttribute(attributeJson.toString());

				return StepPolicy.createToUpdateAndSchedulePolicy(5, jobBean);
			}

			else if (result.getStatusCode() == HttpStatus.SC_OK) {
				// check result
				ErrorCodeUtil.checkResult(logger, result, "execute fail",
						ErrorCode.SD_TASK_EXECUTE_FAIL);
				ErrorCodeUtil.hasLength(logger, result.getData(),
						"result.getData() is empty!",
						ErrorCode.SD_TASK_EXECUTE_FAIL);

				service.processExecuteResult(result, context, attributeJson);
				IServiceOperateTask srvOperateTask = (IServiceOperateTask) DecomposerContextHelper
						.getInstance().getBean(
								Constant.SpringDefine.SRV_OPERATE_TASK);

				srvOperateTask
						.updateTaskStatusAndWriteLog(taskHeader,
								EnumProgress.COMPLETE.getName(),
								"execute complete", "");
				getScheduler().deleteJob(jobBean.getId());

				return StepPolicy.createDeletedPolicy();

			} else {
				writeErrorLogAndUpdateTask(
						taskHeader,
						EnumProgress.EXECUTE.getName(),
						"execute failed",
						"Can not recognise the HttpStatus :"
								+ result.getStatusCode());
			}
		} catch (Exception e) {
			writeErrorLogAndUpdateTask(taskHeader,
					EnumProgress.EXECUTE.getName(), "execute failed",
					e.toString());
		}

		getScheduler().deleteJob(jobBean.getId());

		return StepPolicy.createDeletedPolicy();
	}

	private static String getJobIDByLocation(String location) {
		if (!StringUtils.isEmpty(location)) {
			int lastIndexOf = location.lastIndexOf("/");
			if (lastIndexOf > 0 && lastIndexOf < location.length() - 1) {
				return location.substring(lastIndexOf + 1);
			}
		}

		return "";
	}
}
