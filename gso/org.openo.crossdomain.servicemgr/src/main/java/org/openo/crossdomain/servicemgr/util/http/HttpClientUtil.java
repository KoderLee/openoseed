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

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.openo.crossdomain.servicemgr.util.json.JsonUtil;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.util.restclient.RestfulFactory;
import org.openo.baseservice.roa.util.restclient.RestfulParametes;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.baseservice.rpc.util.RestTransferUtil;

import net.sf.json.JSONObject;

/**
 * Tools for http.<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
public final class HttpClientUtil {

    private static final String X_AUTH_TOKEN = "X-Auth-Token";

    /**
     * IP
     */
    public static final String X_CLIENT_ADDR = "x-real-client-addr";

    /**
     * Get RestfulResponse instance.<br/>
     *
     * @param url url
     * @param httpHeaders header of http
     * @param httpRequest http request
     * @return RestfulResponse instance
     * @throws ServiceException
     * @since crossdomain 0.5
     */
    static public RestfulResponse get(final String url, final Map<String, String> httpHeaders,
            HttpServletRequest httpRequest) throws ServiceException {
        final RestfulParametes restfulParametes = getRestfulParametes(httpRequest);

        for(Entry<String, String> entry : httpHeaders.entrySet()) {
            restfulParametes.putHttpContextHeader(entry.getKey(), entry.getValue());
        }

        return RestfulFactory.getRestInstance(RestfulFactory.PROTO_HTTPS).get(url, restfulParametes);
    }

    /**
     * Get RestfulResponse instance.<br/>
     *
     * @param url url
     * @param httpHeaders header of http
     * @return RestfulResponse instance.
     * @throws ServiceException
     * @since crossdomain 0.5
     */
    static public RestfulResponse get(final String url, final Map<String, String> httpHeaders) throws ServiceException {
        final RestfulParametes restfulParametes = new RestfulParametes();

        for(Entry<String, String> entry : httpHeaders.entrySet()) {
            restfulParametes.putHttpContextHeader(entry.getKey(), entry.getValue());
        }
        restfulParametes.putHttpContextHeader("Content-Type", "application/json;charset=UTF-8");
        return RestfulFactory.getRestInstance(RestfulFactory.PROTO_HTTPS).get(url, restfulParametes);
    }

    /**
     * Get RestfulResponse instance for post operation.<br/>
     *
     * @param url url
     * @param sendObj data that is sent
     * @param httpRequest http request
     * @return RestfulResponse instance.
     * @throws ServiceException
     * @since crossdomain 0.5
     */
    static public RestfulResponse post(final String url, Object sendObj, HttpServletRequest httpRequest)
            throws ServiceException {
        final RestfulParametes restfulParametes = getRestfulParametes(httpRequest);

        if(sendObj != null) {
            JSONObject param = JSONObject.fromObject(sendObj);
            String strJsonReq = RestTransferUtil.tansferRequest(param);
            restfulParametes.setRawData(strJsonReq);
        }
        return RestfulFactory.getRestInstance(RestfulFactory.PROTO_HTTPS).post(url, restfulParametes);
    }

    /**
     * Get RestfulResponse instance for put operation.<br/>
     *
     * @param url url
     * @param sendObj data that is sent
     * @param httpRequest http request
     * @return RestfulResponse instance.
     * @throws ServiceException
     * @since crossdomain 0.5
     */
    static public RestfulResponse put(final String url, Object sendObj, HttpServletRequest httpRequest)
            throws ServiceException {
        final RestfulParametes restfulParametes = getRestfulParametes(httpRequest);

        if(sendObj != null) {
            JSONObject param = JSONObject.fromObject(sendObj);
            String strJsonReq = RestTransferUtil.tansferRequest(param);
            restfulParametes.setRawData(strJsonReq);
        }
        return RestfulFactory.getRestInstance(RestfulFactory.PROTO_HTTPS).put(url, restfulParametes);
    }

    /**
     * Get RestfulResponse instance for delete operation.<br/>
     *
     * @param url url
     * @param httpRequest http request
     * @return RestfulResponse instance.
     * @throws ServiceException
     * @since crossdomain 0.5
     */
    static public RestfulResponse delete(final String url, HttpServletRequest httpRequest) throws ServiceException {
        final RestfulParametes restfulParametes = getRestfulParametes(httpRequest);

        return RestfulFactory.getRestInstance(RestfulFactory.PROTO_HTTPS).delete(url, restfulParametes);
    }
    
    /**
     * Get restful parameters.<br/>
     *
     * @param httpRequest http request
     * @return restful parameters
     * @since crossdomain 0.5
     */
    public static RestfulParametes getRestfulParametes(HttpServletRequest httpRequest) {
        final RestfulParametes restfulParametes = new RestfulParametes();

        String xAuthToken = httpRequest.getHeader(X_AUTH_TOKEN);
        restfulParametes.putHttpContextHeader(X_AUTH_TOKEN, xAuthToken);

        String xClientAddr = httpRequest.getHeader(X_CLIENT_ADDR);
        restfulParametes.putHttpContextHeader(X_CLIENT_ADDR, xClientAddr);

        restfulParametes.putHttpContextHeader("Content-Type", "application/json;charset=UTF-8");

        return restfulParametes;
    }

    /**
     * Get RestfulResponse instance for delete operation.<br/>
     *
     * @param url url
     * @return tokenStr authentication token
     * @throws ServiceException
     * @since crossdomain 0.5
     */
    static public RestfulResponse delete(final String url, String tokenStr) throws ServiceException {
        final RestfulParametes restfulParametes = new RestfulParametes();

        restfulParametes.putHttpContextHeader(X_AUTH_TOKEN, tokenStr);
        restfulParametes.putHttpContextHeader("Content-Type", "application/json;charset=UTF-8");
        return RestfulFactory.getRestInstance(RestfulFactory.PROTO_HTTPS).delete(url, restfulParametes);
    }

    /**
     * Get RestfulResponse instance for post operation.<br/>
     *
     * @param url url
     * @param sendObj content that is sent
     * @return tokenStr authentication token
     * @throws ServiceException
     * @since crossdomain 0.5
     */
    static public RestfulResponse post(final String url, Object sendObj, String tokenStr) throws ServiceException {
        final RestfulParametes restfulParametes = new RestfulParametes();

        restfulParametes.putHttpContextHeader(X_AUTH_TOKEN, tokenStr);
        restfulParametes.putHttpContextHeader("Content-Type", "application/json;charset=UTF-8");

        if(sendObj != null) {
            String strJsonReq = null;
            try {
                strJsonReq = JsonUtil.marshal(sendObj);
            } catch(IOException e) {
                return null;
            }
            restfulParametes.setRawData(strJsonReq);
        }
        return RestfulFactory.getRestInstance(RestfulFactory.PROTO_HTTPS).post(url, restfulParametes);
    }

}
