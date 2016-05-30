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
package org.openo.crossdomain.commonsvc.executor.common.resthelper;

import org.openo.commonservice.remoteservice.exception.ServiceException;
import org.openo.commonservice.roa.util.restclient.RestfulOptions;
import org.openo.commonservice.roa.util.restclient.RestfulParametes;
import org.openo.commonservice.roa.util.restclient.RestfulResponse;

import javax.servlet.http.HttpServletResponse;

import org.openo.crossdomain.commonsvc.executor.common.constant.Constants;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class RestfulProxy {

    private static final int RETRYLIMIT = 1;

    private static final ReentrantReadWriteLock cacheLock = new ReentrantReadWriteLock();

    public static RestfulResponse get(String uri, RestfulParametes restParametes) throws ServiceException {
        return doAction(RestfulMethod.GET, uri, restParametes, null);
    }

    public static RestfulResponse get(String uri, RestfulParametes restParametes, RestfulOptions restOptions)
            throws ServiceException {
        return doAction(RestfulMethod.GET, uri, restParametes, restOptions);
    }

    public static RestfulResponse put(String uri, RestfulParametes restParametes) throws ServiceException {
        return doAction(RestfulMethod.PUT, uri, restParametes, null);
    }

    public static RestfulResponse put(String uri, RestfulParametes restParametes, RestfulOptions restOptions)
            throws ServiceException {
        return doAction(RestfulMethod.PUT, uri, restParametes, restOptions);
    }

    public static RestfulResponse post(String uri, RestfulParametes restParametes) throws ServiceException {
        return doAction(RestfulMethod.POST, uri, restParametes, null);
    }

    public static RestfulResponse post(String uri, RestfulParametes restParametes, RestfulOptions restOptions)
            throws ServiceException {
        return doAction(RestfulMethod.POST, uri, restParametes, restOptions);
    }

    public static RestfulResponse delete(String uri, RestfulParametes restParametes) throws ServiceException {
        return doAction(RestfulMethod.DELETE, uri, restParametes, null);
    }

    public static RestfulResponse delete(String uri, RestfulParametes restParametes, RestfulOptions restOptions)
            throws ServiceException {
        return doAction(RestfulMethod.DELETE, uri, restParametes, restOptions);
    }

    private static RestfulResponse doAction(RestfulMethod action, String uri, RestfulParametes restParametes,
            RestfulOptions restOptions) throws ServiceException {
        RestfulResponse response;
        int retry = RETRYLIMIT;

        restParametes.putHttpContextHeader(Constants.HttpContext.CONTENT_TYPE_HEADER,
                Constants.HttpContext.MEDIA_TYPE_JSON);

        do {
            if(restOptions == null) {
                response = action.method(uri, restParametes);
            } else {
                response = action.method(uri, restParametes, restOptions);
            }

            if(HttpServletResponse.SC_BAD_REQUEST != response.getStatus()) {
                retry = 0;
            }

        } while((retry--) > 0);

        return response;
    }
}
