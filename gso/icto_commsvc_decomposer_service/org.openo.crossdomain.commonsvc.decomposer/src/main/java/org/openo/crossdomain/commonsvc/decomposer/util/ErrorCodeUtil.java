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
package org.openo.crossdomain.commonsvc.decomposer.util;

import org.apache.commons.lang.StringUtils;
import org.openo.crossdomain.commonsvc.decomposer.model.Result;

import org.openo.commonservice.log.OssLog;
import org.openo.commonservice.remoteservice.exception.ServiceException;

/**
 * error check util class
 * 
 * @since crossdomain 0.5
 */
public final class ErrorCodeUtil {

	/**
	 * check result
	 * 
	 * @param logger logger
	 * @param result result
	 * @param errorInfo the error information
	 * @param errorCode error code
	 * @throws ServiceException if result is not success
	 * @since crossdomain 0.5
	 */
	public static void checkResult(OssLog logger, Result<?> result,
			String errorInfo, String errorCode) throws ServiceException {
		if (!result.checkSuccess()) {
			logger.error(errorInfo + " RetCode: " + result.getRetCode());
			throw new ServiceException(errorCode, errorInfo);
		}
	}

	/**
	 * check result
	 * 
	 * @param logger logger
	 * @param result result
	 * @param errorInfo the error information
	 * @param errorCode error code
	 * @throws ServiceException if result is empty
	 * @since crossdomain 0.5
	 */
	public static void hasLength(OssLog logger, String result,
			String errorInfo, String errorCode) throws ServiceException {
		if (StringUtils.isEmpty(result)) {
			logger.error(errorInfo);
			throw new ServiceException(errorCode, errorInfo);
		}
	}

	/**
	 * check result <br>
	 * if result object is null , throw an ServiceException.
	 * 
	 * @param logger logger
	 * @param result result object
	 * @param errorInfo the error information
	 * @param errorCode error code
	 * @throws ServiceException if result is null.
	 * @since crossdomain 0.5
	 */
	public static void checkObject(OssLog logger, Object result,
			String errorInfo, String errorCode) throws ServiceException {
		if (result == null) {
			logger.error(errorInfo);
			throw new ServiceException(errorCode, errorInfo);
		}
	}

	/**
	 * check result <br>
	 * if result object is not null , throw an ServiceException.
	 * 
	 * @param logger logger
	 * @param result result object
	 * @param errorInfo the error information
	 * @param errorCode error code
	 * @throws ServiceException if result is not null.
	 * @since crossdomain 0.5
	 */
	public static void checkObjectForNull(OssLog logger, Object result,
			String errorInfo, String errorCode) throws ServiceException {
		if (result != null) {
			logger.error(errorInfo);
			throw new ServiceException(errorCode, errorInfo);
		}
	}
}
