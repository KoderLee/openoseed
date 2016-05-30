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
package org.openo.crossdomain.servicemgr.util.http;

import org.apache.commons.httpclient.HttpStatus;
import org.openo.crossdomain.servicemgr.exception.HttpCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.openo.baseservice.remoteservice.exception.ExceptionArgs;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.exceptionFrame.RoaExceptionInfo;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.baseservice.rpc.util.RestTransferUtil;

/**
 * Tools to deal with response.<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
public class ResponseUtils {

    private static final Logger logger = LoggerFactory.getLogger(ResponseUtils.class);

    /**
     * Get object data frlm response.<br/>
     *
     * @param response reponse
     * @param clazz class type
     * @return class object
     * @throws ServiceException
     * @since crossdomain 0.5
     */
    public static <T> T transferResponse(RestfulResponse response, Class<T> clazz) throws ServiceException {
        int httpStatus = response.getStatus();
        if(httpStatus >= HttpStatus.SC_OK && httpStatus < HttpStatus.SC_MULTIPLE_CHOICES) {
            return RestTransferUtil.transferResponse(response.getResponseContent(), clazz);
        } else {
            logger.error("response failed, response status: " + response.getStatus());
            checkResonseAndThrowException(response);
            return null;
        }
    }

    /**
     * Check response data.<br/>
     *
     * @param response
     * @return null
     * @throws ServiceException
     * @since crossdomain 0.5
     */
    public static String checkResonseAndThrowException(RestfulResponse response) throws ServiceException {
        if(!HttpCode.isSucess(response.getStatus())) {
            RoaExceptionInfo roaExceptionInfo =
                    RestTransferUtil.transferResponse(response.getResponseContent(), RoaExceptionInfo.class);
            if(null == roaExceptionInfo) {
                return null;
            }
            ServiceException serviceException = new ServiceException();
            serviceException.setHttpCode(response.getStatus());
            serviceException.setId(roaExceptionInfo.getExceptionId());
            serviceException.setExceptionArgs(new ExceptionArgs(roaExceptionInfo.getDescArgs(), roaExceptionInfo
                    .getReasonArgs(), roaExceptionInfo.getDetailArgs(), roaExceptionInfo.getAdviceArgs()));

            throw serviceException;
        }
        return null;
    }

    /**
     * Get response string.<br/>
     * If failing to operate, throw exception.
     *
     * @param response
     * @return content string
     * @throws ServiceException
     * @since crossdomain 0.5
     */
    public static String transferResponse(RestfulResponse response) throws ServiceException {
        int httpStatus = response.getStatus();
        String result = null;
        if(httpStatus >= HttpStatus.SC_OK && httpStatus < HttpStatus.SC_MULTIPLE_CHOICES) {
            result = response.getResponseContent();
        } else {
            checkResonseAndThrowException(response);
        }

        return result;
    }
}
