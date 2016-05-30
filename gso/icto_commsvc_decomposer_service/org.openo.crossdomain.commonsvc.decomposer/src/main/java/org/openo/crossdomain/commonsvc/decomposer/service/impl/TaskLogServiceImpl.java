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

import java.util.List;

import javax.annotation.Resource;

import org.openo.crossdomain.commonsvc.decomposer.constant.ErrorCode;
import org.openo.crossdomain.commonsvc.decomposer.dao.inf.ITaskLogDao;
import org.openo.crossdomain.commonsvc.decomposer.model.Result;
import org.openo.crossdomain.commonsvc.decomposer.model.TaskLog;
import org.openo.crossdomain.commonsvc.decomposer.service.inf.ITaskLogService;
import org.openo.crossdomain.commonsvc.decomposer.util.TaskLogUtil;
import org.springframework.stereotype.Service;

import org.openo.commonservice.log.OssLog;
import org.openo.commonservice.log.OssLogFactory;
import org.openo.commonservice.remoteservice.exception.ServiceException;
import org.openo.commonservice.roa.common.HttpContext;

/**
 * The implement for service decomposer task log.<br>
 * 
 * @since crossdomain 0.5
 */
@Service(value = "taskLogService")
public class TaskLogServiceImpl implements ITaskLogService {

	/**
	 * logger
	 */
	private final OssLog logger = OssLogFactory
			.getLogger(ServiceOperateServiceImpl.class);

	/**
	 * dao for task log
	 */
	@Resource
	private ITaskLogDao taskLogDao;

	/**
	 * @see ITaskLogService#getTaskLogCount(String)
	 */
	@Override
	public Result<List<TaskLog>> getTaskLog(HttpContext context, String taskID)
			throws ServiceException {
		return taskLogDao.getTaskLogByTaskID(taskID);
	}

	/**
	 * @see ITaskLogService#insertLog(TaskLog)
	 */
	@Override
	public Result<String> insertLog(TaskLog taskLog) throws ServiceException {
		taskLogDao.insert(taskLog);
		return new Result<String>(ErrorCode.SUCCESS);
	}

	/**
	 * @see ITaskLogService#insertLog(String, String, String, String)
	 */
	@Override
	public Result<String> insertLog(String taskID, String progress,
			String result, String resultReason) {
		try {

			TaskLog taskLog = new TaskLog(taskID);
			taskLog.setSequenceID(TaskLogUtil.distributeID(taskID));
			taskLog.setDescription(progress);
			taskLog.setResult(result);
			taskLog.setResultReason(resultReason);

			return insertLog(taskLog);
		} catch (ServiceException e) {
			logger.error(e.toString());
			return new Result<String>(ErrorCode.FAIL);
		}
	}

	public void setTaskLogDao(ITaskLogDao taskLogDao) {
		this.taskLogDao = taskLogDao;
	}

	/**
	 * @see ITaskLogService#getTaskLogCount(String)
	 */
	@Override
	public int getTaskLogCount(String taskID) {
		return taskLogDao.getTaskLogCountByTaskID(taskID);
	}

}
