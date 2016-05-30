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
import org.openo.crossdomain.commonsvc.decomposer.dao.inf.IServiceOperateDao;
import org.openo.crossdomain.commonsvc.decomposer.dao.mapper.ServiceDecomposerTaskMapper;
import org.openo.crossdomain.commonsvc.decomposer.model.Result;
import org.openo.crossdomain.commonsvc.decomposer.model.ServiceDecomposerTask;
import org.openo.crossdomain.commonsvc.decomposer.util.DecomposerUtil;
import org.springframework.stereotype.Component;

import org.openo.commonservice.log.OssLog;
import org.openo.commonservice.log.OssLogFactory;
import org.openo.commonservice.mybatis.session.MapperManager;
import org.openo.commonservice.remoteservice.exception.ServiceException;

/**
 * The implement of dao layer for service operation
 * 
 * @since crossdomain 0.5
 */
@Component(value = Constant.SpringDefine.SRV_OPERATE_DAO)
public class ServiceOperateDaoImpl implements IServiceOperateDao {

	/**
	 * Logger
	 */
	private static final OssLog logger = OssLogFactory
			.getLogger(ServiceOperateDaoImpl.class);

	/**
	 * Mapper Manager
	 */
	@Resource
	private MapperManager mapperMgrRef;

	public <T> T getMapperManager(Class<T> type) {
		return mapperMgrRef.getMapper(type, Constant.SERVICEDECOMPOSER_DB);
	}

	public void setMapperMgrRef(MapperManager mapperMgrRef) {
		this.mapperMgrRef = mapperMgrRef;
	}

	/**
	 * @see IServiceOperateDao#getSDTaskBySvcID(String, String)
	 */
	@Override
	public Result<List<ServiceDecomposerTask>> getSDTaskBySvcID(
			String serviceID, String tenantID) throws ServiceException {
		try {
			ServiceDecomposerTaskMapper sdMapper = getMapperManager(ServiceDecomposerTaskMapper.class);
			List<ServiceDecomposerTask> sdTasks = sdMapper
					.getSrvDecomposerTask(serviceID, tenantID);

			if (sdTasks != null) {
				for (ServiceDecomposerTask sdTask : sdTasks) {
					sdTask.setServiceContent(DecomposerUtil.decrypt(sdTask
							.getServiceContent()));
				}
			}

			return new Result<List<ServiceDecomposerTask>>(ErrorCode.SUCCESS,
					"", sdTasks);
		} catch (PersistenceException e) {
			logger.error(e.toString());
			throw new ServiceException(ErrorCode.SD_OPER_DB_ERROR, e.toString());
		}
	}

	/**
	 * @see IServiceOperateDao#getTaskByTaskID(String, String)
	 */
	@Override
	public Result<ServiceDecomposerTask> getTaskByTaskID(String taskID,
			String tenantID) throws ServiceException {
		try {
			ServiceDecomposerTaskMapper sdMapper = getMapperManager(ServiceDecomposerTaskMapper.class);

			ServiceDecomposerTask sdTask = sdMapper.qrySrvDecomposerTask(
					taskID, tenantID);

			if (sdTask != null) {
				sdTask.setServiceContent(DecomposerUtil.decrypt(sdTask
						.getServiceContent()));
			}

			return new Result<ServiceDecomposerTask>(ErrorCode.SUCCESS, "",
					sdTask);
		} catch (PersistenceException e) {
			logger.error(e.toString());
			throw new ServiceException(ErrorCode.SD_OPER_DB_ERROR, e.toString());
		}
	}

	/**
	 * @see IServiceOperateDao#insert(ServiceDecomposerTask)
	 */
	@Override
	public Result<Object> insert(ServiceDecomposerTask task)
			throws ServiceException {
		if (task == null) {
			logger.error("Insert Task is null!");
			return new Result<Object>(ErrorCode.SUCCESS);
		}

		ServiceDecomposerTask cloneTask = task.deepCopy();

		cloneTask.setServiceContent(DecomposerUtil.encrypt(task
				.getServiceContent()));

		try {
			ServiceDecomposerTaskMapper sdMapper = getMapperManager(ServiceDecomposerTaskMapper.class);

			sdMapper.insertSrvDecomposerTask(cloneTask);
		} catch (PersistenceException e) {
			logger.error(e.toString());
			throw new ServiceException(ErrorCode.SD_OPER_DB_ERROR, e.toString());
		}

		logger.info("Insert Task ok! serviceid is: " + cloneTask.getServiceID()
				+ " and taskid is: " + cloneTask.getTaskID());

		return new Result<Object>(ErrorCode.SUCCESS);
	}

	/**
	 * @see IServiceOperateDao#delete(ServiceDecomposerTask)
	 */
	@Override
	public Result<Object> delete(ServiceDecomposerTask task)
			throws ServiceException {
		if (task == null) {
			logger.error("Delete Task is null!");
			return new Result<Object>(ErrorCode.SUCCESS);
		}

		try {
			ServiceDecomposerTaskMapper sdMapper = getMapperManager(ServiceDecomposerTaskMapper.class);
			sdMapper.deleteSrvDecomposerTask(task);
		} catch (PersistenceException e) {
			logger.error(e.toString());
			throw new ServiceException(ErrorCode.SD_OPER_DB_ERROR, e.toString());
		}

		logger.info("Delete Task ok! serviceid is: " + task.getServiceID()
				+ " and taskid is: " + task.getTaskID());

		return new Result<Object>(ErrorCode.SUCCESS);
	}

	/**
	 * @see IServiceOperateDao#update(ServiceDecomposerTask)
	 */
	@Override
	public Result<Object> update(ServiceDecomposerTask task)
			throws ServiceException {
		if (task == null) {
			logger.error("Update Task is null!");
			return new Result<Object>(ErrorCode.SUCCESS);
		}

		ServiceDecomposerTask cloneTask = task.deepCopy();

		cloneTask.setServiceContent(DecomposerUtil.encrypt(task
				.getServiceContent()));

		try {
			ServiceDecomposerTaskMapper sdMapper = getMapperManager(ServiceDecomposerTaskMapper.class);
			sdMapper.updateSrvDecomposerTask(cloneTask);
		} catch (PersistenceException e) {
			logger.error(e.toString());
			throw new ServiceException(ErrorCode.SD_OPER_DB_ERROR, e.toString());
		}

		logger.info("Update Task ok! serviceid is: " + cloneTask.getServiceID()
				+ " and taskid is: " + cloneTask.getTaskID());

		return new Result<Object>(ErrorCode.SUCCESS);
	}
}
