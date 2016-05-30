/*
 * Copyright (c) 2016, Huawei Technologies Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *  
 */
package org.openo.sdno.cbb.sdnwan.util.error;

import org.apache.commons.lang3.StringUtils;

import org.openo.sdno.remoteservice.exception.ExceptionArgs;
import org.openo.sdno.remoteservice.exception.ServiceException;

/**
 * Service exception util.<br/>
 * 
 * @author
 * @version SDNO 0.5 17-Mar-2016
 */
public class ServiceExceptionUtil {

    public static final int NOT_FOUND = 404;

    public static final int BAD_REQUEST = 400;

    private ServiceExceptionUtil() {
    }

    /**
     * Makes a new ServiceException object from the input error code and arguments sent as input.<br/>
     * 
     * @param errCode unique string for a given exception type
     * @param commonArgs string array constituting the content of the exception message
     * @return ServiceException object
     * @since SDNO 0.5
     */
    public static final ServiceException getBadRequestServiceExceptionWithCommonArgs(final String errCode,
            final String[] commonArgs) {
        final ExceptionArgs exceptionArgs = new ExceptionArgs(commonArgs, commonArgs, commonArgs, commonArgs);

        return getBadRequestServiceException(errCode, exceptionArgs);
    }

    /**
     * Makes a new ServiceException object from the input error code.<br/>
     * 
     * @param errCode unique number for a given exception type
     * @return ServiceException object
     * @since SDNO 0.5
     */
    public static final ServiceException getServiceException(final int errCode) {
        return getServiceException(String.valueOf(errCode), null);
    }

    /**
     * Makes a new ServiceException object from the input error code.<br/>
     * 
     * @param errCode unique string for a given exception type
     * @return ServiceException object
     * @since SDNO 0.5
     */
    public static final ServiceException getServiceException(final String errCode) {
        return getServiceException(errCode, null);
    }

    /**
     * Makes a new ServiceException object from the input error code and HTTP status code.<br/>
     * 
     * @param errCode unique string for a given exception type
     * @param httpCode HTTP status code
     * @return ServiceException object
     * @since SDNO 0.5
     */
    public static final ServiceException getServiceException(final String errCode, final int httpCode) {
        return getServiceException(errCode, httpCode, null);
    }

    private static final ServiceException getServiceException(final String errCode, final int httpCode,
            final ExceptionArgs exceptionArgs) {
        final ServiceException ex = getServiceException(errCode, exceptionArgs);
        ex.setHttpCode(httpCode);

        return ex;
    }

    private static final ServiceException getServiceException(final String errCode, final ExceptionArgs exceptionArgs) {
        final ServiceException ex = new ServiceException(errCode, StringUtils.EMPTY);

        if(null != exceptionArgs) {
            ex.setExceptionArgs(exceptionArgs);
        }

        return ex;
    }

    /**
     * Makes a new ServiceException object from the input error code and HTTP status code.<br/>
     * 
     * @param errCode unique string for a given exception type
     * @param commonArgs string array constituting the content of the exception message
     * @return ServiceException object
     * @since SDNO 0.5
     */
    public static final ServiceException getServiceExceptionWithCommonArgs(final String errCode,
            final String[] commonArgs) {
        final ExceptionArgs exceptionArgs = new ExceptionArgs();
        exceptionArgs.setDescArgs(commonArgs);
        exceptionArgs.setDetailArgs(commonArgs);
        exceptionArgs.setReasonArgs(commonArgs);
        exceptionArgs.setAdviceArgs(commonArgs);

        return getServiceException(String.valueOf(errCode), exceptionArgs);
    }

    /**
     * Throws BadRequestException a Wrapper around the ServiceException with predefined exception
     * details.<br/>
     * 
     * @throws ServiceException throws ServiceException as BadRequestException with predefined
     *             exception details
     * @since SDNO 0.5
     */
    public static final void throwBadRequestException() throws ServiceException {
        throw new ServiceException(ServiceException.DEFAULT_ID, BAD_REQUEST);
    }

    private static final ServiceException getBadRequestServiceException(final String errCode,
            final ExceptionArgs exceptionArgs) {
        return getServiceException(errCode, BAD_REQUEST, exceptionArgs);
    }

}
