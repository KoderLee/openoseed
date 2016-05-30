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
package org.openo.crossdomain.commonsvc.executor.common.resthelper.impl;

import org.openo.crossdomain.commonsvc.executor.common.resthelper.RestfulProxy;
import org.openo.crossdomain.commonsvc.executor.common.resthelper.inf.IRestClient;

import org.openo.commonservice.log.OssLog;
import org.openo.commonservice.log.OssLogFactory;
import org.openo.commonservice.remoteservice.exception.ServiceException;
import org.openo.commonservice.roa.util.restclient.RestfulOptions;
import org.openo.commonservice.roa.util.restclient.RestfulParametes;
import org.openo.commonservice.roa.util.restclient.RestfulResponse;

public class ResourceConfigClient implements IRestClient {

    private static final OssLog log = OssLogFactory.getLogger(ResourceConfigClient.class);

    public final RestfulResponse get(String id, RestfulParametes restParametes, RestfulOptions options) {
        try {
            return RestfulProxy.get(id, restParametes, options);
        } catch(ServiceException e) {
            return buildErrorResponse(e);
        }
    }

    public final RestfulResponse put(String id, RestfulParametes restParametes, RestfulOptions options) {
        try {
            return RestfulProxy.put(id, restParametes, options);
        } catch(ServiceException e) {
            return buildErrorResponse(e);
        }
    }

    public final RestfulResponse post(String id, RestfulParametes restParametes, RestfulOptions options) {
        try {
            return RestfulProxy.post(id, restParametes, options);
        } catch(ServiceException e) {
            return buildErrorResponse(e);
        }
    }

    public final RestfulResponse delete(String id, RestfulParametes restParametes, RestfulOptions options) {
        try {
            return RestfulProxy.delete(id, restParametes, options);
        } catch(ServiceException e) {
            return buildErrorResponse(e);
        }
    }

    private RestfulResponse buildErrorResponse(Exception e) {
        log.error("", e);
        RestfulResponse errorResponse = new RestfulResponse();
        errorResponse.setResponseJson("{\"message\":\"" + e.getMessage() + "\"}");
        return errorResponse;
    }
}
