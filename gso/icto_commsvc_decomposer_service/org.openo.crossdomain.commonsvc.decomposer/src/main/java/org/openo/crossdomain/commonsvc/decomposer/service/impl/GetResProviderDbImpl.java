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

import org.openo.crossdomain.commonsvc.decomposer.constant.ErrorCode;
import org.openo.crossdomain.commonsvc.decomposer.dao.inf.IServiceResDao;
import org.openo.crossdomain.commonsvc.decomposer.model.ResMapping;
import org.openo.crossdomain.commonsvc.decomposer.model.Result;
import org.openo.crossdomain.commonsvc.decomposer.service.inf.IGetResProvider;
import org.openo.crossdomain.commonsvc.decomposer.util.DecomposerContextHelper;
import org.openo.crossdomain.commonsvc.decomposer.util.JsonConvertUtils;

import net.sf.json.JSONObject;

import org.openo.commonservice.remoteservice.exception.ServiceException;

/**
 * provide resource data from database
 * 
 * @since crossdomain 0.5
 */
public class GetResProviderDbImpl implements IGetResProvider {

	/**
	 * service ID
	 */
	private final String serviceID;
	/**
	 * tenant ID
	 */
	private final String tenantID;
	/**
	 * task process
	 */
	private final String taskProcess;

	/**
	 * Constructor
	 * 
	 * @param serviceID service ID
	 * @param tenantID tenant ID
	 * @param taskProcess task process
	 * @since crossdomain 0.5
	 */
	public GetResProviderDbImpl(final String serviceID, final String tenantID,
			final String taskProcess) {
		this.serviceID = serviceID;
		this.tenantID = tenantID;
		this.taskProcess = taskProcess;
	}

	/**
	 * @see IGetResProvider#getResInfo()
	 */
	@Override
	public JSONObject getResInfo() throws ServiceException {
		IServiceResDao srvResDao = (IServiceResDao) DecomposerContextHelper
				.getInstance().getBean("srvResDao");
		final Result<List<ResMapping>> resResult = srvResDao
				.getResMappingBySvcID(serviceID, tenantID);
		if (!resResult.checkSuccess()) {
			logger.error("GetResProviderDbImpl.getResInfo() failed .Service id is  "
					+ serviceID
					+ " return error,errorCode is "
					+ resResult.getRetCode());
			throw new ServiceException(ErrorCode.SD_OPER_DB_ERROR,
					"failed to get res mapping by serviceID. service id is "
							+ serviceID);
		}

		return JsonConvertUtils.convertResFromDb(resResult.getData(),
				taskProcess);
	}
}
