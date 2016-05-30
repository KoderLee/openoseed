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

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.openo.crossdomain.commonsvc.decomposer.constant.Constant;
import org.openo.crossdomain.commonsvc.decomposer.constant.ErrorCode;
import org.openo.crossdomain.commonsvc.decomposer.dao.inf.IServiceDecomposerDao;
import org.openo.crossdomain.commonsvc.decomposer.model.Result;
import org.openo.crossdomain.commonsvc.decomposer.model.ServiceDecomposerMapping;
import org.openo.crossdomain.commonsvc.decomposer.service.inf.IServiceDecomposerService;
import org.springframework.stereotype.Service;

import org.openo.commonservice.remoteservice.exception.ServiceException;
import org.openo.commonservice.roa.common.HttpContext;

/**
 * The implement of logic layer for decomposer rules register flow.
 * 
 * @since crossdomain 0.5
 */
@Service(value = Constant.SpringDefine.SRV_DECOMPOSER_SERVICE)
public class ServiceDecomposerServiceImpl implements IServiceDecomposerService {

	@Resource
	private IServiceDecomposerDao srvDecomposerDao;

	/**
	 * @see IServiceDecomposerService#regService(org.openo.crossdomain.commonsvc.decomposer.service.inf.HttpContext,
	 *      List)
	 */
	@Override
	public Result<String> regService(final HttpContext context,
			final List<ServiceDecomposerMapping> sdMappingLst)
			throws ServiceException {
		return srvDecomposerDao.regService(sdMappingLst);
	}

	/**
	 * @see IServiceDecomposerService#queryService(org.openo.crossdomain.commonsvc.decomposer.service.inf.HttpContext,
	 *      String, String)
	 */
	@Override
	public Result<String> queryService(final HttpContext context,
			final String typename, final String regtype)
			throws ServiceException {

		Result<List<ServiceDecomposerMapping>> sdMapLstResult = srvDecomposerDao
				.getServices(typename, regtype);

		JSONArray jsonSDArray = JSONArray.fromObject(sdMapLstResult.getData());
		JSONObject jsonRules = new JSONObject();
		jsonRules.put(Constant.REGISTER_KEY, jsonSDArray);

		return new Result<String>(ErrorCode.SUCCESS, "", jsonRules.toString());
	}

	/**
	 * @see IServiceDecomposerService#deleteService(org.openo.crossdomain.commonsvc.decomposer.service.inf.HttpContext,
	 *      List)
	 */
	@Override
	public Result<String> deleteService(final HttpContext context,
			final List<ServiceDecomposerMapping> sdMappingLst)
			throws ServiceException {
		return srvDecomposerDao.deleteService(sdMappingLst);
	}

	public void setSrvDecomposerDao(final IServiceDecomposerDao srvDecomposerDao) {
		this.srvDecomposerDao = srvDecomposerDao;
	}

	/**
	 * @see IServiceDecomposerService#getServiceByType(String, String, String)
	 */
	@Override
	public Result<ServiceDecomposerMapping> getServiceByType(String svcType,
			String regType, String version) throws ServiceException {
		return srvDecomposerDao.getServiceByType(svcType, regType, version);
	}
}
