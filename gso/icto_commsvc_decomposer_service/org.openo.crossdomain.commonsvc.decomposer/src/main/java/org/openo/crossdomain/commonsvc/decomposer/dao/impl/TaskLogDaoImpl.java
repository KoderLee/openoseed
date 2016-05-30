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
package org.openo.crossdomain.commonsvc.decomposer.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.exceptions.PersistenceException;
import org.openo.crossdomain.commonsvc.decomposer.constant.Constant;
import org.openo.crossdomain.commonsvc.decomposer.constant.ErrorCode;
import org.openo.crossdomain.commonsvc.decomposer.dao.inf.ITaskLogDao;
import org.openo.crossdomain.commonsvc.decomposer.dao.mapper.TaskLogMapper;
import org.openo.crossdomain.commonsvc.decomposer.model.Result;
import org.openo.crossdomain.commonsvc.decomposer.model.TaskLog;
import org.springframework.stereotype.Component;

import org.openo.commonservice.log.OssLog;
import org.openo.commonservice.log.OssLogFactory;
import org.openo.commonservice.mybatis.session.MapperManager;
import org.openo.commonservice.remoteservice.exception.ServiceException;

/**
 * The implement of dao layer for task log.
 * 
 * @since crossdomain 0.5
 */
@Component(value = "taskLogDao")
public class TaskLogDaoImpl implements ITaskLogDao {

	/**
	 * Logger
	 */
	private static final OssLog logger = OssLogFactory
			.getLogger(TaskLogDaoImpl.class);

	/**
	 * MapperManager
	 */
	@Resource
	private MapperManager mapperMgrRef;

	public <T> T getMapperManager(Class<T> type) {
		return mapperMgrRef.getMapper(type, Constant.SERVICEDECOMPOSER_DB);
	}

	/**
	 * @see ITaskLogDao#getTaskLogByTaskID(String)
	 */
	@Override
	public Result<List<TaskLog>> getTaskLogByTaskID(String taskID)
			throws ServiceException {
		TaskLogMapper taskJobMapper = getMapperManager(TaskLogMapper.class);
		try {
			return new Result<List<TaskLog>>(ErrorCode.SUCCESS, "",
					taskJobMapper.getTaskLogByTaskID(taskID));
		} catch (PersistenceException e) {
			logger.error(e.toString());
			throw new ServiceException(ErrorCode.SD_OPER_DB_ERROR,
					e.getMessage());
		}
	}

	/**
	 * @see ITaskLogDao#insert(TaskLog)
	 */
	@Override
	public Result<Object> insert(TaskLog taskLog) throws ServiceException {
		TaskLogMapper taskJobMapper = getMapperManager(TaskLogMapper.class);

		try {
			taskJobMapper.insert(taskLog);
		} catch (PersistenceException e) {
			logger.error(e.toString());
			throw new ServiceException(ErrorCode.SD_OPER_DB_ERROR,
					e.getMessage());
		}

		logger.info("Insert taskLog ok! task id is:[" + taskLog.getTaskID()
				+ "] and description is: [" + taskLog.getDescription() + "]");

		return new Result<Object>(ErrorCode.SUCCESS);
	}

	public void setMapperMgrRef(MapperManager mapperMgrRef) {
		this.mapperMgrRef = mapperMgrRef;
	}

	/**
	 * @see ITaskLogDao#getTaskLogCountByTaskID(String)
	 */
	@Override
	public int getTaskLogCountByTaskID(String taskID) {
		TaskLogMapper taskJobMapper = getMapperManager(TaskLogMapper.class);
		try {
			return taskJobMapper.getTaskLogCountByTaskID(taskID);
		} catch (PersistenceException e) {
			logger.error(e.toString());
		}
		return 0;
	}

}
