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

import org.openo.crossdomain.commonsvc.decomposer.constant.Constant;
import org.openo.crossdomain.commonsvc.decomposer.logutil.TaskHeader;
import org.openo.crossdomain.commonsvc.decomposer.model.EnumProgress;
import org.openo.crossdomain.commonsvc.decomposer.service.impl.ServiceOperateServiceImpl;
import org.openo.crossdomain.commonsvc.decomposer.service.inf.IServiceOperateTask;
import org.openo.crossdomain.commonsvc.decomposer.service.inf.ITaskLogService;
import org.openo.crossdomain.commonsvc.decomposer.util.DecomposerContextHelper;
import org.openo.crossdomain.commonsvc.decomposer.util.DecomposerUtil;

import org.openo.commonservice.log.OssLog;
import org.openo.commonservice.log.OssLogFactory;
import org.openo.commonservice.remoteservice.exception.ServiceException;
import com.huawei.icto.commsvc.jobscheduler.core.inf.IScheduler;
import com.huawei.icto.commsvc.jobscheduler.job.IPersistentJob;
import com.huawei.icto.commsvc.jobscheduler.model.JobBean;

/**
 * abstract Persistent job,it implement the interface of IPersistentJob.<br>
 * 
 * @since crossdomain 0.5
 */
public abstract class AbsPersistentJob implements IPersistentJob {

	private final OssLog logger = OssLogFactory
			.getLogger(AbsPersistentJob.class);

	/**
	 * job bean
	 */
	protected JobBean jobBean;

	/**
	 * scheduler
	 */
	private IScheduler scheduler;

	/**
	 * interval time
	 */
	public final static int RESCHEDULE_INTERVAL = 5;

	public void setJobBean(JobBean jobBean) {
		this.jobBean = jobBean;
	}

	/**
	 * write error log
	 * 
	 * @param logger logger
	 * @param service service
	 * @param taskID task ID
	 * @param process process
	 * @param log message
	 * @since crossdomain 0.5
	 */
	public void writeErrorLog(OssLog logger, ServiceOperateServiceImpl service,
			String taskID, String process, String log) {
		logger.error(log);

		String reason = "";
		if (log != null) {
			reason = log.length() > 255 ? log.substring(0, 255) : log;
		}
		if (service != null) {
			ITaskLogService taskLogService = (ITaskLogService) DecomposerContextHelper
					.getInstance().getBean("taskLogService");
			taskLogService.insertLog(taskID, process, process + " failed",
					reason);
		}
	}

	/**
	 * get the scheduler.<br>
	 * 
	 * @return scheduler
	 * @since crossdomain 0.5
	 */
	public IScheduler getScheduler() {
		if (scheduler == null) {
			scheduler = (IScheduler) DecomposerContextHelper.getInstance()
					.getBean("scheduler");
		}
		return scheduler;
	}

	protected void writeErrorLogAndUpdateTask(final TaskHeader taskHeader,
			String process, String result, String error) {
		ServiceOperateServiceImpl service = (ServiceOperateServiceImpl) DecomposerContextHelper
				.getInstance().getBean(
						Constant.SpringDefine.SRV_OPERATE_SERVICE);

		writeErrorLog(logger, service, taskHeader.getTaskId(), process, error);

		try {

			IServiceOperateTask srvOperateTask = (IServiceOperateTask) DecomposerContextHelper
					.getInstance().getBean(
							Constant.SpringDefine.SRV_OPERATE_TASK);
			srvOperateTask.updateTaskStatus(taskHeader,
					EnumProgress.COMPLETE.getName(), result, error);
		} catch (ServiceException e) {
			logger.error(e.toString());
		}

		DecomposerUtil.clearTaskRedis(taskHeader.getTaskId());
	}

}
