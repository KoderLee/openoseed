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
package org.openo.crossdomain.commonsvc.executor.common.util;

import org.openo.commonservice.remoteservice.exception.ExceptionArgs;
import org.openo.commonservice.remoteservice.exception.ServiceException;
import org.apache.commons.httpclient.HttpStatus;

public final class ServiceExceptionUtil {

    /**
	 *throw Not Found Exception
	 *@throws ServiceException nof found
	 *@since crossdomain 0.5 2016-3-18
	 */	
    public static void throwNotFoundException() throws ServiceException {
        throw new ServiceException(ServiceException.DEFAULT_ID, HttpStatus.SC_NOT_FOUND);
    }

	/**
	 *throw Bad Request Exception
	 *@param reason exception reason
	 *@throws ServiceException : bad request
	 *@since crossdomain 0.5 2016-3-18
	 */	
    public static void throwBadRequestException(String reason) throws ServiceException {
        ServiceException e = new ServiceException(ServiceException.DEFAULT_ID, reason);
        e.setHttpCode(HttpStatus.SC_BAD_REQUEST);

        ExceptionArgs exceptionArgs = new ExceptionArgs();
        exceptionArgs.setReasonArgs(new String[] {reason});
        e.setExceptionArgs(exceptionArgs);

        throw e;
    }

	/**
	 *throw Error Exception
	 *@param reason exception reason
	 *@throws ServiceException : error
	 *@since crossdomain 0.5 2016-3-18
	 */	
    public static void throwErrorException(String reason) throws ServiceException {
        ServiceException e = new ServiceException(ServiceException.DEFAULT_ID, reason);
        e.setHttpCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);

        ExceptionArgs exceptionArgs = new ExceptionArgs();
        exceptionArgs.setReasonArgs(new String[] {reason});
        e.setExceptionArgs(exceptionArgs);

        throw e;
    }
}
