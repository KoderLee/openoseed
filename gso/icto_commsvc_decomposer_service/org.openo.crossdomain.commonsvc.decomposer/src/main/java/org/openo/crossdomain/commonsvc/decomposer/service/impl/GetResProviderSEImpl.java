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

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.openo.crossdomain.commonsvc.decomposer.constant.Constant;
import org.openo.crossdomain.commonsvc.decomposer.constant.ConstantURL;
import org.openo.crossdomain.commonsvc.decomposer.constant.ErrorCode;
import org.openo.crossdomain.commonsvc.decomposer.dao.inf.IServiceResDao;
import org.openo.crossdomain.commonsvc.decomposer.model.ResMapping;
import org.openo.crossdomain.commonsvc.decomposer.model.Result;
import org.openo.crossdomain.commonsvc.decomposer.rest.util.RestfulMethod;
import org.openo.crossdomain.commonsvc.decomposer.rest.util.RestfulProxy;
import org.openo.crossdomain.commonsvc.decomposer.service.inf.IGetResProvider;
import org.openo.crossdomain.commonsvc.decomposer.util.DecomposerContextHelper;
import org.openo.crossdomain.commonsvc.decomposer.util.DecomposerUtil;
import org.openo.crossdomain.commonsvc.decomposer.util.JsonConvertUtils;
import org.springframework.util.StringUtils;

import org.openo.commonservice.remoteservice.exception.ServiceException;

/**
 * provide resource data from Executor
 * 
 * @since crossdomain 0.5
 */
public class GetResProviderSEImpl implements IGetResProvider {

	/**
	 * job id
	 */
	private final String seJobId;

	/**
	 * token
	 */
	private final String token;

	/**
	 * service ID
	 */
	private final String serviceID;

	/**
	 * tenant ID
	 */
	private final String tenantID;

	/**
	 * Constructor
	 * 
	 * @param seJobId job ID
	 * @param token token
	 * @param serviceID service ID
	 * @param tenantID tenant ID
	 * @since crossdomain 0.5
	 */
	public GetResProviderSEImpl(final String seJobId, final String token,
			final String serviceID, final String tenantID) {
		this.seJobId = seJobId;
		this.token = token;
		this.serviceID = serviceID;
		this.tenantID = tenantID;
	}

	/**
	 * @see IGetResProvider#getResInfo()
	 */
	@Override
	public JSONObject getResInfo() throws ServiceException {
		Map<String, String> mapHeader = new HashMap<String, String>();
		mapHeader.put(Constant.Security.X_AUTH_TOKEN, token);

		final Result<String> rst = RestfulProxy.restSend(RestfulMethod.GET,
				MessageFormat.format(ConstantURL.QUERY_SE_JOB_DETAIL, seJobId),
				"", mapHeader);
		if (!rst.checkSuccess()) {
			logger.error("GetResProviderSEImpl.getResInfo(): get job error,job id is : "
					+ seJobId + " errorCode is " + rst.getRetCode());
			throw new ServiceException(ErrorCode.SD_TASK_EXECUTE_FAIL,
					"fail to get job detail. job id is " + seJobId);
		}

		if (!StringUtils.hasLength(rst.getData())) {
			logger.error("GetResProviderSEImpl.getResInfo(): get job but no body ,job id is : "
					+ seJobId + " errorCode is " + rst.getRetCode());
			throw new ServiceException(ErrorCode.SD_TASK_EXECUTE_FAIL,
					"get data is empty. job id is " + seJobId);
		}

		IServiceResDao srvResDao = (IServiceResDao) DecomposerContextHelper
				.getInstance().getBean("srvResDao");
		final Result<List<ResMapping>> resResult = srvResDao
				.getResMappingBySvcID(serviceID, tenantID);
		if (!resResult.checkSuccess()) {
			logger.error("GetResProviderSEImpl.getResInfo() failed .Service id is  "
					+ serviceID
					+ " return error,errorCode is "
					+ resResult.getRetCode());
			throw new ServiceException(ErrorCode.SD_OPER_DB_ERROR,
					"failed to get res mapping by serviceID. service id is "
							+ serviceID);
		}

		final JSONObject resFromSeJson = JsonConvertUtils.convertResFromSE(rst
				.getData());

		final Map<String, ResMapping> resMappingMap = DecomposerUtil
				.resMapping2Map(resResult.getData());

		Iterator<?> keys = resFromSeJson.keys();
		while (keys.hasNext()) {
			String resourceLabel = keys.next().toString();
			JSONObject jsonRes = resFromSeJson.getJSONObject(resourceLabel);
			ResMapping resMapping = resMappingMap.get(resourceLabel);
			if (resMapping != null) {
				jsonRes.put(Constant.DEPENDS, resMapping.getDepends());
			}
		}

		return resFromSeJson;
	}
}
