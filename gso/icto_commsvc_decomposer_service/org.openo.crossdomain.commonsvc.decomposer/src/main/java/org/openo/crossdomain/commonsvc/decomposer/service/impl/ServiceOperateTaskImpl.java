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
package org.openo.crossdomain.commonsvc.decomposer.service.impl;

import org.apache.commons.lang.StringUtils;
import org.openo.crossdomain.commonsvc.decomposer.constant.Constant;
import org.openo.crossdomain.commonsvc.decomposer.constant.ErrorCode;
import org.openo.crossdomain.commonsvc.decomposer.dao.inf.IServiceOperateDao;
import org.openo.crossdomain.commonsvc.decomposer.logutil.ServicedecomposerAuditLog;
import org.openo.crossdomain.commonsvc.decomposer.logutil.TaskHeader;
import org.openo.crossdomain.commonsvc.decomposer.model.EnumProgress;
import org.openo.crossdomain.commonsvc.decomposer.model.Result;
import org.openo.crossdomain.commonsvc.decomposer.model.ServiceDecomposerTask;
import org.openo.crossdomain.commonsvc.decomposer.service.inf.IServiceOperateTask;
import org.openo.crossdomain.commonsvc.decomposer.service.inf.ITaskLogService;
import org.openo.crossdomain.commonsvc.decomposer.util.DecomposerContextHelper;
import org.openo.crossdomain.commonsvc.decomposer.util.ErrorCodeUtil;
import org.springframework.stereotype.Service;

import org.openo.commonservice.log.OssLog;
import org.openo.commonservice.log.OssLogFactory;
import org.openo.commonservice.remoteservice.exception.ServiceException;

/**
 * The implement for service decomposer task update.
 * 
 * @since crossdomain 0.5
 */
@Service(value = Constant.SpringDefine.SRV_OPERATE_TASK)
public class ServiceOperateTaskImpl implements IServiceOperateTask {

	/**
	 * logger
	 */
	private final OssLog logger = OssLogFactory
			.getLogger(ServiceOperateTaskImpl.class);

	/**
	 * @see IServiceOperateTask#updateTaskStatus(TaskHeader, String, String,
	 *      String)
	 */
	@Override
	public void updateTaskStatusAndWriteLog(final TaskHeader taskHeader,
			final String progress, final String result,
			final String resultReason) throws ServiceException {
		updateTaskStatus(taskHeader, progress, result, resultReason);
		logTaskLog(taskHeader.getTaskId(), progress, result, resultReason);
	}

	/**
	 * @see IServiceOperateTask#updateTaskStatusAndWriteLog(ServiceDecomposerTask,
	 *      String, String, String)
	 */
	@Override
	public ServiceDecomposerTask updateTaskStatusAndWriteLog(
			final ServiceDecomposerTask task, final String progress,
			final String result, final String resultReason)
			throws ServiceException {

		updateTaskStatus(task, progress, result, resultReason);

		logTaskLog(task.getTaskID(), progress, result, resultReason);

		return task;
	}

	/**
	 * @see IServiceOperateTask#updateTaskStatus(TaskHeader, String, String,
	 *      String)
	 */
	@Override
	public ServiceDecomposerTask updateTaskStatus(final TaskHeader taskHeader,
			final String progress, final String result,
			final String resultReason) throws ServiceException {
		final ServiceDecomposerTask task = getTask(taskHeader.getTaskId(),
				taskHeader.getTenantId());
		updateTaskStatus(task, progress, result, resultReason);

		if ((!StringUtils.isEmpty(progress))
				&& progress.equals(EnumProgress.COMPLETE.getName())) {
			task.setUser(taskHeader.getUser());
			ServicedecomposerAuditLog.addAuditLogEnd(task);
		}

		return task;
	}

	private void updateTaskStatus(final ServiceDecomposerTask task,
			final String progress, final String result,
			final String resultReason) throws ServiceException {
		task.setProgress(progress);
		task.setResult(result);
		task.setResultReason(resultReason);

		IServiceOperateDao srvOperateDao = (IServiceOperateDao) DecomposerContextHelper
				.getInstance().getBean(Constant.SpringDefine.SRV_OPERATE_DAO);
		final Result<Object> updateResult = srvOperateDao.update(task);
		ErrorCodeUtil.checkResult(logger, updateResult,
				"Update Task failed! TaskID: " + task.getTaskID(),
				ErrorCode.SD_OPER_DB_ERROR);
	}

	private void logTaskLog(String taskID, final String progress,
			final String result, final String resultReason) {
		ITaskLogService taskLogService = (ITaskLogService) DecomposerContextHelper
				.getInstance().getBean("taskLogService");

		taskLogService.insertLog(taskID, progress, result, resultReason);
	}

	/**
	 * @see IServiceOperateTask#getTask(String, String)
	 */
	@Override
	public ServiceDecomposerTask getTask(final String taskID,
			final String tenantID) throws ServiceException {
		IServiceOperateDao srvOperateDao = (IServiceOperateDao) DecomposerContextHelper
				.getInstance().getBean(Constant.SpringDefine.SRV_OPERATE_DAO);
		final Result<ServiceDecomposerTask> sdTaskResult = srvOperateDao
				.getTaskByTaskID(taskID, tenantID);
		ErrorCodeUtil.checkResult(logger, sdTaskResult,
				"Query Task failed! taskID: " + taskID,
				ErrorCode.SD_OPER_DB_ERROR);

		final ServiceDecomposerTask task = sdTaskResult.getData();

		return task;
	}

	/**
	 * @see IServiceOperateTask#updateTaskJobID(String, String, String)
	 */
	@Override
	public void updateTaskJobID(final String taskID, final String tenantID,
			final String jobID) throws ServiceException {
		final ServiceDecomposerTask task = getTask(taskID, tenantID);
		task.setSeJobID(jobID);

		IServiceOperateDao srvOperateDao = (IServiceOperateDao) DecomposerContextHelper
				.getInstance().getBean(Constant.SpringDefine.SRV_OPERATE_DAO);
		final Result<Object> updateResult = srvOperateDao.update(task);
		ErrorCodeUtil.checkResult(logger, updateResult,
				"Update JobID failed! jobID: " + jobID,
				ErrorCode.SD_OPER_DB_ERROR);
	}
}
