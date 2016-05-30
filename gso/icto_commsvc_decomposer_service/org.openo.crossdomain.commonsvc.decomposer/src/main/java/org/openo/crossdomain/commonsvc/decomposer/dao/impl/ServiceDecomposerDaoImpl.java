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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.exceptions.PersistenceException;
import org.openo.crossdomain.commonsvc.decomposer.constant.Constant;
import org.openo.crossdomain.commonsvc.decomposer.constant.ErrorCode;
import org.openo.crossdomain.commonsvc.decomposer.dao.inf.IServiceDecomposerDao;
import org.openo.crossdomain.commonsvc.decomposer.dao.mapper.ServiceDecomposerMappingMapper;
import org.openo.crossdomain.commonsvc.decomposer.model.Result;
import org.openo.crossdomain.commonsvc.decomposer.model.ServiceDecomposerMapping;
import org.openo.crossdomain.commonsvc.decomposer.util.DecomposerUtil;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import org.openo.commonservice.log.OssLog;
import org.openo.commonservice.log.OssLogFactory;
import org.openo.commonservice.mybatis.session.MapperManager;
import org.openo.commonservice.redis.oper.MapOper;
import org.openo.commonservice.remoteservice.exception.ServiceException;

/**
 * The implement of dao layer for decomposer rules register flow.
 * 
 * @since crossdomain 0.5
 */
@Repository(value = "srvDecomposerDao")
public class ServiceDecomposerDaoImpl implements IServiceDecomposerDao {

	private static final OssLog logger = OssLogFactory
			.getLogger(ServiceDecomposerDaoImpl.class);

	@Resource
	private MapperManager mapperMgrRef;

	private MapOper<ServiceDecomposerMapping> sdMapOper;

	private void compareMapping(final List<ServiceDecomposerMapping> values) {
		if (!DecomposerUtil.isListHasRealLength(values)) {
			return;
		}

		Collections.sort(values, new Comparator<ServiceDecomposerMapping>() {

			private int versionA = Constant.NUM_INIT_ZERO;

			private int versionB = Constant.NUM_INIT_ZERO;

			private String versionStringA = Constant.BLANK_STRING;

			private String versionStringB = Constant.BLANK_STRING;

			@Override
			public int compare(final ServiceDecomposerMapping sdMapA,
					final ServiceDecomposerMapping sdMapB) {

				versionA = Constant.NUM_INIT_ZERO;
				versionB = Constant.NUM_INIT_ZERO;

				versionStringA = sdMapA.getVersion();
				versionStringB = sdMapB.getVersion();

				if ((StringUtils.hasLength(versionStringA))
						&& (versionStringA.matches(Constant.VERSION_RULS))) {

					versionA = Integer.valueOf(versionStringA.substring(1));
				}

				if ((StringUtils.hasLength(versionStringB))
						&& (versionStringB.matches(Constant.VERSION_RULS))) {

					versionB = Integer.valueOf(versionStringB.substring(1));
				}

				return versionA - versionB;
			}
		});

	}

	/**
	 * @see IServiceDecomposerDao#deleteService(List)
	 */
	@Override
	public Result<String> deleteService(
			final List<ServiceDecomposerMapping> sdMappingLst)
			throws ServiceException {
		final ServiceDecomposerMappingMapper sdMapper = getMapperManager(ServiceDecomposerMappingMapper.class);
		for (final ServiceDecomposerMapping sdMapping : sdMappingLst) {

			try {
				sdMapper.deleteSrvDecomposer(sdMapping);
			} catch (final PersistenceException e) {
				logger.error(e.toString());
				throw new ServiceException(ErrorCode.SD_OPER_DB_ERROR,
						e.toString());
			}
			final String key = sdMapping.getTypeName() + ":"
					+ sdMapping.getRegType();
			final String field = sdMapping.getVersion();
			this.getRedisOper().remove(key, field);

			logger.warn("Delete Service Decomposer  type is:["
					+ sdMapping.getRegType() + "] and version is: ["
					+ sdMapping.getVersion() + "] name is : ["
					+ sdMapping.getTypeName() + "]");
		}
		return new Result<String>(ErrorCode.SUCCESS);
	}

	private void filteDataRow(final List<ServiceDecomposerMapping> values,
			final String version) {
		if ((!DecomposerUtil.isListHasRealLength(values))
				|| (!StringUtils.hasLength(version))) {

			return;
		}

		final Iterator<ServiceDecomposerMapping> mappingIt = values.iterator();

		ServiceDecomposerMapping oneMapping;

		while (mappingIt.hasNext()) {
			oneMapping = mappingIt.next();
			if ((oneMapping.getVersion() == null)
					|| (!oneMapping.getVersion().equals(version))) {
				mappingIt.remove();
			}

		}
	}

	public <T> T getMapperManager(final Class<T> type) {
		return mapperMgrRef.getMapper(type, Constant.SERVICEDECOMPOSER_DB);
	}

	public MapOper<ServiceDecomposerMapping> getRedisOper() {
		if (sdMapOper == null) {
			sdMapOper = new MapOper<ServiceDecomposerMapping>(Constant.SD_MAP,
					Constant.REDIS_DB);
		}
		return sdMapOper;
	}

	/**
	 * @see IServiceDecomposerDao#getServiceByType(String, String, String)
	 */
	@Override
	public Result<ServiceDecomposerMapping> getServiceByType(
			final String svcType, final String regType, final String version)
			throws ServiceException {
		final Result<List<ServiceDecomposerMapping>> rst = getServicesByType(
				svcType, regType, version);
		final List<ServiceDecomposerMapping> values = rst.getData();

		return new Result<ServiceDecomposerMapping>(ErrorCode.SUCCESS, "",
				(DecomposerUtil.isListHasRealLength(values) ? values.get(values
						.size() - 1) : null));
	}

	/**
	 * @see IServiceDecomposerDao#getServicesByType(String, String, String)
	 */
	@Override
	public Result<List<ServiceDecomposerMapping>> getServicesByType(
			final String svcType, final String regType, final String version)
			throws ServiceException {

		final String key = DecomposerUtil.getServiceKeyForRedis(svcType,
				regType);

		final List<ServiceDecomposerMapping> values = new ArrayList<ServiceDecomposerMapping>();

		List<ServiceDecomposerMapping> tempValuesList;
		if (StringUtils.hasLength(version)) {
			tempValuesList = getRedisOper().get(key,
					ServiceDecomposerMapping.class, version);

			if (DecomposerUtil.isListHasRealLength(tempValuesList)) {
				values.addAll(tempValuesList);
			}
		}

		if (!DecomposerUtil.isListHasRealLength(values)) {
			final ServiceDecomposerMappingMapper sdMapper = getMapperManager(ServiceDecomposerMappingMapper.class);
			try {
				tempValuesList = sdMapper.getSrvDecomposer(svcType, regType);

				if (DecomposerUtil.isListHasRealLength(tempValuesList)) {
					values.addAll(tempValuesList);
				}

			} catch (final PersistenceException e) {
				logger.error(e.toString());
				throw new ServiceException(ErrorCode.SD_OPER_DB_ERROR,
						e.toString());
			}

		}

		filteDataRow(values, version);

		compareMapping(values);

		return new Result<List<ServiceDecomposerMapping>>(ErrorCode.SUCCESS,
				"", values);
	}

	/**
	 * @see IServiceDecomposerDao#getServices(String, String)
	 */
	@Override
	public Result<List<ServiceDecomposerMapping>> getServices(
			final String svcType, final String regType) throws ServiceException {
		List<ServiceDecomposerMapping> values;

		ServiceDecomposerMappingMapper sdMapper = getMapperManager(ServiceDecomposerMappingMapper.class);
		try {
			values = sdMapper.getSrvDecomposer(svcType, regType);
		} catch (final PersistenceException e) {
			logger.error(e.toString());
			throw new ServiceException(ErrorCode.SD_OPER_DB_ERROR, e.toString());
		}

		return new Result<List<ServiceDecomposerMapping>>(ErrorCode.SUCCESS,
				"", values);
	}

	/**
	 * @see IServiceDecomposerDao#regService(List)
	 */
	@Override
	public Result<String> regService(
			final List<ServiceDecomposerMapping> sdMappingLst)
			throws ServiceException {

		Result<ServiceDecomposerMapping> aResult;

		int allSuccessData = 0;

		final ServiceDecomposerMappingMapper sdMapper = getMapperManager(ServiceDecomposerMappingMapper.class);

		for (final ServiceDecomposerMapping sdMapping : sdMappingLst) {

			aResult = getServiceByType(sdMapping.getTypeName(),
					sdMapping.getRegType(), sdMapping.getVersion());

			try {
				if (aResult.getData() == null) {

					allSuccessData += sdMapper.insertSrvDecomposer(sdMapping);
				} else {

					allSuccessData += sdMapper.updateSrvDecomposer(sdMapping);
				}
			} catch (final PersistenceException e) {
				logger.error(e.toString());
				throw new ServiceException(ErrorCode.SD_OPER_DB_ERROR,
						e.toString());
			}

			// use map from redis. need clone ServiceDecomposerMapping,
			final ServiceDecomposerMapping copySdMapping = new ServiceDecomposerMapping(
					sdMapping);
			final String key = DecomposerUtil.getServiceKeyForRedis(
					copySdMapping.getTypeName(), copySdMapping.getRegType());

			final String field = copySdMapping.getVersion();

			this.getRedisOper().put(key, field, copySdMapping);

			logger.warn("Register Service Decomposer  type is:["
					+ sdMapping.getRegType() + "] and version is: ["
					+ sdMapping.getVersion() + "] name is : ["
					+ sdMapping.getTypeName() + "];The service is new : "
					+ (aResult.getData() == null));

		}

		String resultString = ErrorCode.SUCCESS;
		if (allSuccessData != sdMappingLst.size()) {
			resultString = ErrorCode.FAIL;
			logger.warn("ServiceDecomposerDaoImpl.regService() success partly and total is "
					+ sdMappingLst.size() + " success is " + allSuccessData);

		}

		return new Result<String>(resultString);
	}

	public void setMapperMgrRef(final MapperManager mapperMgrRef) {
		this.mapperMgrRef = mapperMgrRef;
	}

}
