/*******************************************************************************
 * Copyright (c) 2016, Huawei Technologies Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package org.openo.crossdomain.commonsvc.decomposer.rest.util;

import java.util.List;

import javax.xml.ws.spi.http.HttpContext;

import org.openo.crossdomain.commonsvc.decomposer.constant.Constant;
import org.openo.crossdomain.commonsvc.decomposer.constant.ErrorCode;
import org.openo.crossdomain.commonsvc.decomposer.dao.inf.IServiceOperateDao;
import org.openo.crossdomain.commonsvc.decomposer.dao.inf.IServiceResDao;
import org.openo.crossdomain.commonsvc.decomposer.model.ResMapping;
import org.openo.crossdomain.commonsvc.decomposer.model.Result;
import org.openo.crossdomain.commonsvc.decomposer.model.ServiceDecomposerTask;
import org.openo.crossdomain.commonsvc.decomposer.util.DecomposerContextHelper;

/**
 * Token util class <br/>
 * 
 * @author
 * @version crossdomain 0.5
 */
public class TokenUtil {

	/**
	 * Verification tasks access conditions is ok <br/>
	 * 
	 * @param logger logger
	 * @param task Decompose task
	 * @throws throw ServiceException if check failed.
	 * @since crossdomain 0.5
	 */
	public static void checkAccessible(final OssLog logger,
			final ServiceDecomposerTask task) throws ServiceException {

		String tenantID = task.getTenantID();
		if (StringUtils.isBlank(tenantID)) {
			logger.error("CheckAccessible failed for null tenantID!");
			throw new ServiceException(ErrorCode.SD_BAD_PARAM,
					HttpStatus.SC_UNAUTHORIZED);
		}

		if (Constant.OperType.CREATE_DEFINE
				.equalsIgnoreCase(task.getOperType())) {
			return;
		}

		String serviceID = task.getServiceID();
		if (StringUtils.isBlank(serviceID)) {
			logger.error("CheckAccessible failed for null serviceID!");
			throw new ServiceException(ErrorCode.SD_BAD_PARAM,
					HttpStatus.SC_NOT_FOUND);
		}

		IServiceResDao srvResDao = (IServiceResDao) DecomposerContextHelper
				.getInstance().getBean("srvResDao");
		IServiceOperateDao srvOperateDao = (IServiceOperateDao) DecomposerContextHelper
				.getInstance().getBean("srvOperateDao");
		Result<List<ResMapping>> resMappingsResult = srvResDao
				.getResMappingBySvcID(serviceID, tenantID);
		Result<List<ServiceDecomposerTask>> sdTaskLstResult = srvOperateDao
				.getSDTaskBySvcID(serviceID, tenantID);
		if (CollectionUtils.isEmpty(resMappingsResult.getData())
				&& CollectionUtils.isEmpty(sdTaskLstResult.getData())) {
			logger.error("CheckAccessible failed for null record!");

			throw new ServiceException(ErrorCode.SD_SERVICE_NOT_EXIST,
					HttpStatus.SC_NOT_FOUND);
		}
	}

	/**
	 * Gets a specified item from the header <br/>
	 * 
	 * @param context Http Context
	 * @param key The keyword
	 * @return value
	 * @throws throw ServiceException if obtain failed.
	 * @since crossdomain 0.5
	 */
	public static String getStringFromHeader(final HttpContext context,
			final String key) throws ServiceException {
		if (context == null || context.getHttpServletRequest() == null) {
			throw new ServiceException(ErrorCode.SD_BAD_PARAM,
					HttpStatus.SC_UNAUTHORIZED);
		}

		if (StringUtils.isEmpty(key)) {
			throw new ServiceException(ErrorCode.SD_BAD_PARAM,
					HttpStatus.SC_BAD_REQUEST);
		}

		String vlaue = context.getHttpServletRequest().getHeader(key);

		if (StringUtils.isEmpty(vlaue)) {
			throw new ServiceException(ErrorCode.SD_BAD_PARAM,
					HttpStatus.SC_BAD_REQUEST);
		}

		return vlaue;
	}
}
