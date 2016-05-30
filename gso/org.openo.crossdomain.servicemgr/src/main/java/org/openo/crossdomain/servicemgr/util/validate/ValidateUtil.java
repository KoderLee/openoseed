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
package org.openo.crossdomain.servicemgr.util.validate;

import org.openo.crossdomain.servicemgr.exception.ErrorCode;
import org.openo.crossdomain.servicemgr.exception.HttpCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import org.openo.baseservice.remoteservice.exception.ServiceException;

public class ValidateUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValidateUtil.class);

    public static void assertNotNull(Object object, String name) throws ServiceException {
        assertNotNull(object, ErrorCode.SVCMGR_SERVICEMGR_BAD_PARAM, name);
    }

    public static void assertNotNull(Object object, String errorCode, String name) throws ServiceException {
        if(object == null) {
            LOGGER.error("{} can't be null", name);
            throw new ServiceException(errorCode, HttpCode.BAD_REQUEST);
        }
    }

    public static void assertNotEmpty(String param, String name) throws ServiceException {
        assertNotEmpty(param, ErrorCode.SVCMGR_SERVICEMGR_BAD_PARAM, name);
    }

    public static void assertNotEmpty(String param, String errorCode, String name) throws ServiceException {
        if(!StringUtils.hasLength(param)) {
            LOGGER.error("{} can't be null or empty", name);
            throw new ServiceException(errorCode, HttpCode.BAD_REQUEST);
        }
    }

    public static void assertNull(Object object, String errorCode, String loggerInfo) throws ServiceException {
        if(null != object) {
            LOGGER.error(loggerInfo);
            throw new ServiceException(errorCode, HttpCode.BAD_REQUEST);
        }
    }

    public static void assertNull(Object object, String name) throws ServiceException {
        String loggerInfo = String.format("%s must be null or empty", name);
        assertNull(object, ErrorCode.SVCMGR_SERVICEMGR_BAD_PARAM, loggerInfo);
    }
}
