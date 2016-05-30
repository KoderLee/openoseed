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

import org.openo.crossdomain.commonsvc.decomposer.constant.Constant;
import org.openo.crossdomain.commonsvc.decomposer.logutil.TaskHeader;
import org.openo.crossdomain.commonsvc.decomposer.model.EnumJobType;
import org.openo.crossdomain.commonsvc.decomposer.model.EnumProgress;
import org.openo.crossdomain.commonsvc.decomposer.service.impl.ServiceOperateServiceImpl;
import org.openo.crossdomain.commonsvc.decomposer.service.inf.IServiceOperateTask;
import org.openo.crossdomain.commonsvc.decomposer.util.DecomposerContextHelper;

import net.sf.json.JSONObject;

import org.openo.commonservice.log.OssLog;
import org.openo.commonservice.log.OssLogFactory;
import org.openo.commonservice.remoteservice.exception.ServiceException;
import com.huawei.icto.commsvc.jobscheduler.job.StepPolicy;
import com.huawei.icto.commsvc.jobscheduler.model.JobBean;

/**
 * Persistent Job Proxy,this proxy to scheduling each specific implementation by
 * job type
 * 
 * @since crossdomain 0.5
 */
public class PersistentJobProxy extends AbsPersistentJob {

	/**
	 * logger
	 */
	private final OssLog logger = OssLogFactory
			.getLog(PersistentJobProxy.class);

	// /**
	// * job bean
	// private JobBean jobBean;

	private static Map<String, Class<?>> JOB_MAP = new HashMap<String, Class<?>>();
	static {
		JOB_MAP.put(EnumJobType.DECOMPOSE.getName(), SerialDecomposeJob.class);
		JOB_MAP.put(EnumJobType.DECOMPOSE_WAITREPLAY.getName(),
				SerialDecomposeWaitReplayJob.class);
		JOB_MAP.put(EnumJobType.EXECUTE.getName(), ExecuteJob.class);
		JOB_MAP.put(EnumJobType.EXECUTE_WAITREPLAY.getName(),
				ExecuteWaitReplayJob.class);
	}

	public PersistentJobProxy(final JobBean jobBean) {
		this.jobBean = jobBean;
	}

	/**
	 * run action
	 * 
	 * @return step policy
	 * @since crossdomain 0.5
	 */
	@Override
	public StepPolicy run() {
		Class<?> persistentJobCls = JOB_MAP.get(jobBean.getType());
		if (persistentJobCls == null) {
			logger.error("can not found job processor! type:"
					+ jobBean.getType());
			return null;
		}

		AbsPersistentJob persistentJob;
		try {
			persistentJob = (AbsPersistentJob) persistentJobCls.newInstance();
			persistentJob.setJobBean(jobBean);
			return persistentJob.run();
		} catch (Exception e) {
			logger.error(e.toString());

			String attribute = jobBean.getAttribute();
			JSONObject attributeJson = JSONObject.fromObject(attribute);
			// String errorString = e.getMessage() == null ? e.toString() :
			// e.getMessage();

			TaskHeader taskHeader = new TaskHeader(attributeJson);
			writeErrorLogAndUpdateTask(taskHeader, e.getMessage());

			getScheduler().deleteJob(jobBean.getId());

			return StepPolicy.createDeletedPolicy();
		}
	}

	protected void writeErrorLogAndUpdateTask(final TaskHeader taskHeader,
			final String error) {
		ServiceOperateServiceImpl service = (ServiceOperateServiceImpl) DecomposerContextHelper
				.getInstance().getBean(
						Constant.SpringDefine.SRV_OPERATE_SERVICE);

		writeErrorLog(logger, service, taskHeader.getTaskId(),
				jobBean.getType(), error);

		try {

			IServiceOperateTask srvOperateTask = (IServiceOperateTask) DecomposerContextHelper
					.getInstance().getBean(
							Constant.SpringDefine.SRV_OPERATE_TASK);
			srvOperateTask.updateTaskStatus(taskHeader,
					EnumProgress.COMPLETE.getName(), jobBean.getType()
							+ " failed", error);
		} catch (ServiceException e) {
			logger.error(e.toString());
		}
	}
}
