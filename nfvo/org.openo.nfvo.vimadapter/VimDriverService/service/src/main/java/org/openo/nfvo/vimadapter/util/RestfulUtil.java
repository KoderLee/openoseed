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

package org.openo.nfvo.vimadapter.util;

import java.util.HashMap;
import java.util.Map;

import org.openo.nfvo.vimadapter.util.constant.Constant;

import org.openo.baseservice.log.OssLog;
import org.openo.baseservice.log.OssLogFactory;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.common.HttpContext;
import org.openo.baseservice.roa.util.restclient.Restful;
import org.openo.baseservice.roa.util.restclient.RestfulFactory;
import org.openo.baseservice.roa.util.restclient.RestfulOptions;
import org.openo.baseservice.roa.util.restclient.RestfulParametes;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.nfvo.common.servicetoken.ServiceTokenHelp;

/**
 * 
* used get/config restful response<br/>
* <p>
* </p>
* 
* @author
* @version NFVO 0.5 May 15, 2016
 */
public final class RestfulUtil {
    public static final String TYPE_GET = "get";

    public static final String TYPE_ADD = "add";

    public static final String TYPE_PUT = "put";

    public static final String TYPE_DEL = "delete";

    public static final int ERROR_STATUS_CODE = -1;

    private final static OssLog LOG = OssLogFactory
            .getLogger(RestfulUtil.class);

    private RestfulUtil() {

    }

    public static String getResponseContent(String url,
            RestfulParametes restParametes, String type) {
        Map<String, Object> resMap = getResponseContent(url, restParametes,
                null, type);

        return resMap.get("responseContent").toString();

    }

    public static Map<String, Object> getResponseContent(String url,
            RestfulParametes restParametes, RestfulOptions opt, String type) {
        Map<String, Object> resMap = new HashMap<String, Object>(2);

        try {
            Restful rest = RestfulFactory
                    .getRestInstance(RestfulFactory.PROTO_HTTPS);
            RestfulResponse rsp = null;
            if (rest != null) {
                if (TYPE_GET.equals(type)) {
                    rsp = rest.get(url, restParametes, opt);
                } else if (TYPE_ADD.equals(type)) {
                    rsp = rest.post(url, restParametes, opt);
                } else if (TYPE_PUT.equals(type)) {
                    rsp = rest.put(url, restParametes, opt);
                } else if (TYPE_DEL.equals(type)) {
                    rsp = rest.delete(url, restParametes, opt);
                }
                if (null != rsp) {
                    resMap.put("responseContent", rsp.getResponseContent());
                    resMap.put("statusCode", rsp.getStatus());
                }
            }
            LOG.info("get response data success!");
        } catch (ServiceException e) {
            LOG.error("get response data catch exception {}.", e);
        }

        return resMap;
    }

    public static RestfulResponse getResponseResult(String url, String type,
            HttpContext context) {
        RestfulParametes restParametes = new RestfulParametes();
        return getResponseResult(url, restParametes, type, context);
    }

    public static RestfulResponse getResponseResult(String url,
            RestfulParametes restParametes, String type, HttpContext context) {
        return restfulResponse(url, restParametes, null, type, context);
    }

    public static RestfulResponse restfulResponse(String url,
            RestfulParametes restParametes, RestfulOptions opt, String type,
            HttpContext context) {
        RestfulResponse rsp = null;
        try {
            Map<String, String> headerMap = new HashMap<String, String>(2);
            headerMap.put(Constant.CONTENT_TYPE, Constant.APPLICATION);
            headerMap.put(Constant.HEADER_AUTH_TOKEN,
                    getTokenFromContext(context));
            restParametes.setHeaderMap(headerMap);
            Restful rest = RestfulFactory
                    .getRestInstance(RestfulFactory.PROTO_HTTPS);

            if (rest != null) {
                if (TYPE_GET.equals(type)) {
                    rsp = rest.get(url, restParametes, opt);
                } else if (TYPE_ADD.equals(type)) {
                    rsp = rest.post(url, restParametes, opt);
                } else if (TYPE_PUT.equals(type)) {
                    rsp = rest.put(url, restParametes, opt);
                } else if (TYPE_DEL.equals(type)) {
                    rsp = rest.delete(url, restParametes, opt);
                }
            }
            LOG.info("get response data success!");
        } catch (ServiceException e) {
            LOG.error("get response data catch ServiceException {}.", e);
        }
        return rsp;
    }

    private static String getTokenFromContext(HttpContext context) {
        if (context != null && context.getHttpServletRequest() != null) {
            return context.getHttpServletRequest().getHeader(
                    Constant.HEADER_AUTH_TOKEN);
        }
        return ServiceTokenHelp.createServiceToken();
    }

}
