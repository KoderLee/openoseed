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

package org.openo.nfvo.vimadapter.service.util;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.openo.nfvo.vimadapter.util.VimAsyncCallback;
import org.openo.nfvo.vimadapter.util.constant.Constant;

import net.sf.json.JSONObject;

import org.openo.baseservice.log.OssLog;
import org.openo.baseservice.log.OssLogFactory;
import org.openo.baseservice.roa.common.HttpContext;
import org.openo.baseservice.roa.util.restclient.Restful;
import org.openo.baseservice.roa.util.restclient.RestfulAsyncCallback;
import org.openo.baseservice.roa.util.restclient.RestfulFactory;
import org.openo.baseservice.roa.util.restclient.RestfulParametes;
import org.openo.nfvo.common.servicetoken.ServiceTokenHelp;

/**
 * 
* Used for set an evnet by a rest interface call<br/>
* <p>
* </p>
* 
* @author
* @version NFVO 0.5 May 15, 2016
 */
public final class EventUtil {
    private static final OssLog LOG = OssLogFactory.getLog(EventUtil.class);

    private EventUtil() {

    }

    public static void sentEvtByRest(String servicePath, String methodName,
            JSONObject paramJson, HttpContext context) {
        Restful rest = RestfulFactory.getRestInstance();
        RestfulParametes restParametes = new RestfulParametes();

        Map<String, String> headerMap = new HashMap<String, String>(2);
        headerMap.put(Constant.CONTENT_TYPE, Constant.APPLICATION);
        headerMap.put(Constant.HEADER_AUTH_TOKEN, getTokenFromContext(context));
        restParametes.setHeaderMap(headerMap);
        restParametes.setRawData(paramJson.toString());
        try {
            Method method = rest.getClass().getMethod(
                    methodName,
                    new Class[] { String.class, RestfulParametes.class,
                            RestfulAsyncCallback.class });
            method.invoke(rest, servicePath, restParametes,
                    new VimAsyncCallback());
        } catch (ReflectiveOperationException e) {
            LOG.error(
                    "function=sentEvtByRest, msg=ReflectiveOperationException occurs, e={}.",
                    e);
        }
    }

    private static String getTokenFromContext(HttpContext context) {
        if (context != null && context.getHttpServletRequest() != null) {
            return context.getHttpServletRequest().getHeader(
                    Constant.HEADER_AUTH_TOKEN);
        }
        return ServiceTokenHelp.createServiceToken();
    }
}
