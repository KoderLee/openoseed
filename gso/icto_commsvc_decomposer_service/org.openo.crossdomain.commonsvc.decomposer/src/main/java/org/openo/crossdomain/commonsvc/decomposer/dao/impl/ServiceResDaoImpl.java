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
import org.openo.crossdomain.commonsvc.decomposer.dao.inf.IServiceResDao;
import org.openo.crossdomain.commonsvc.decomposer.dao.mapper.ResMappingMapper;
import org.openo.crossdomain.commonsvc.decomposer.model.ResMapping;
import org.openo.crossdomain.commonsvc.decomposer.model.Result;
import org.springframework.stereotype.Component;

import org.openo.commonservice.log.OssLog;
import org.openo.commonservice.log.OssLogFactory;
import org.openo.commonservice.mybatis.session.MapperManager;
import org.openo.commonservice.remoteservice.exception.ServiceException;

/**
 * The implement of dao layer for service resource.
 * 
 * @since crossdomain 0.5
 */
@Component(value = "srvResDao")
public class ServiceResDaoImpl implements IServiceResDao {

	/**
	 * Log
	 */
	private static final OssLog logger = OssLogFactory
			.getLogger(ServiceResDaoImpl.class);

	private int batSizeData = 300;

	private int batSizeID = 500;

	/**
	 * Mapper Manager
	 */
	@Resource
	private MapperManager mapperMgrRef;

	public <T> T getMapperManager(final Class<T> type) {
		return mapperMgrRef.getMapper(type, Constant.SERVICEDECOMPOSER_DB);
	}

	public void setMapperMgrRef(final MapperManager mapperMgrRef) {
		this.mapperMgrRef = mapperMgrRef;
	}

	/**
	 * @see IServiceResDao#getResMappingByID(String, String)
	 */
	@Override
	public Result<ResMapping> getResMappingByID(final String resID,
			final String tenantID) throws ServiceException {
		ResMappingMapper resMapper = getMapperManager(ResMappingMapper.class);

		try {
			ResMapping resMapping = resMapper.getResMapping(resID, tenantID);
			if (resMapping != null) {
				logger.info("service id is:[" + resMapping.getServiceID()
						+ "] and resource id is: ["
						+ resMapping.getResourceID() + "]");
			} else {
				logger.info("can not get res, id=" + resID);
			}
			Result<ResMapping> result = new Result<ResMapping>(
					ErrorCode.SUCCESS);
			result.setData(resMapping);
			return result;
		} catch (PersistenceException e) {
			logger.error(e.toString());
			throw new ServiceException(ErrorCode.SD_OPER_DB_ERROR, e.toString());
		}
	}

	/**
	 * @see IServiceResDao#getResMappingBySvcID(String, String)
	 */
	@Override
	public Result<List<ResMapping>> getResMappingBySvcID(
			final String serviceID, final String tenantID)
			throws ServiceException {
		try {
			ResMappingMapper resMapper = getMapperManager(ResMappingMapper.class);
			return new Result<List<ResMapping>>(ErrorCode.SUCCESS, "",
					resMapper.getResMappings(serviceID, tenantID));
		} catch (PersistenceException e) {
			logger.error(e.toString());
			throw new ServiceException(ErrorCode.SD_OPER_DB_ERROR, e.toString());
		}
	}

	/**
	 * @see IServiceResDao#insert(ResMapping)
	 */
	@Override
	public Result<Object> insert(final ResMapping resMapping)
			throws ServiceException {
		ResMappingMapper resMapper = getMapperManager(ResMappingMapper.class);

		try {
			resMapper.insertResMapping(resMapping);
		} catch (PersistenceException e) {
			logger.error(e.toString());
			throw new ServiceException(ErrorCode.SD_OPER_DB_ERROR, e.toString());
		}

		logger.info("Insert resource mapping ok! service id is:["
				+ resMapping.getServiceID() + "] and resource id is: ["
				+ resMapping.getResourceID() + "]");

		return new Result<Object>(ErrorCode.SUCCESS);
	}

	/**
	 * @see IServiceResDao#insert(List)
	 */
	@Override
	public Result<Object> insert(final List<ResMapping> resMappings)
			throws ServiceException {
		try {
			ResMappingMapper resMapper = getMapperManager(ResMappingMapper.class);
			for (int i = 0, size = resMappings.size(); i < size; i = i
					+ batSizeData) {
				int end = i + batSizeData > size ? size : i + batSizeData;
				resMapper.batInsertResMapping(resMappings.subList(i, end));
				logger.info("BatInsertResMapping ok! Res count: " + (end - i));
			}
			return new Result<Object>(ErrorCode.SUCCESS);
		} catch (PersistenceException e) {
			logger.error(e.toString());
			throw new ServiceException(ErrorCode.SD_OPER_DB_ERROR, e.toString());
		}
	}

	/**
	 * @see IServiceResDao#delete(ResMapping)
	 */
	@Override
	public Result<Object> delete(final ResMapping resMapping)
			throws ServiceException {
		ResMappingMapper resMapper = getMapperManager(ResMappingMapper.class);

		try {
			resMapper.deleteResMapping(resMapping);
		} catch (PersistenceException e) {
			logger.error(e.toString());
			throw new ServiceException(ErrorCode.SD_OPER_DB_ERROR, e.toString());
		}

		logger.info("delete resource mapping ok! service id is:["
				+ resMapping.getServiceID() + "] and resource id is: ["
				+ resMapping.getResourceID() + "]");

		return new Result<Object>(ErrorCode.SUCCESS);
	}

	/**
	 * @see IServiceResDao#delete(List)
	 */
	@Override
	public Result<Object> delete(final List<ResMapping> resMappings)
			throws ServiceException {
		try {
			ResMappingMapper resMapper = getMapperManager(ResMappingMapper.class);
			for (int i = 0, size = resMappings.size(); i < size; i = i
					+ batSizeID) {
				int end = i + batSizeID > size ? size : i + batSizeID;
				resMapper.batDeleteResMapping(resMappings.subList(i, end));
				logger.info("BatDeleteResMapping ok! Res count: " + (end - i));
			}
			return new Result<Object>(ErrorCode.SUCCESS);
		} catch (PersistenceException e) {
			logger.error(e.toString());
			throw new ServiceException(ErrorCode.SD_OPER_DB_ERROR, e.toString());
		}
	}

	/**
	 * @see IServiceResDao#update(ResMapping)
	 */
	@Override
	public Result<Object> update(final ResMapping resMapping)
			throws ServiceException {
		ResMappingMapper resMapper = getMapperManager(ResMappingMapper.class);

		try {
			resMapper.updateResMapping(resMapping);
		} catch (PersistenceException e) {
			logger.error(e.toString());
			throw new ServiceException(ErrorCode.SD_OPER_DB_ERROR, e.toString());
		}

		logger.info("UpdateResMapping ok! ServiceID is:["
				+ resMapping.getServiceID() + "] and ResourceID is: ["
				+ resMapping.getResourceID() + "]");

		return new Result<Object>(ErrorCode.SUCCESS);
	}
}
