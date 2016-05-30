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

package org.openo.nfvo.nsservice.utils;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.openo.nfvo.nsservice.simpleservice.common.exception.HwDoException;
import org.openo.nfvo.nsservice.simpleservice.constant.Constant;

import net.sf.json.JSONObject;

import org.openo.baseservice.log.OssLog;
import org.openo.baseservice.log.OssLogFactory;
import org.openo.baseservice.roa.common.HttpContext;
import org.openo.baseservice.roa.util.restclient.Restful;
import org.openo.baseservice.roa.util.restclient.RestfulAsyncCallback;
import org.openo.baseservice.roa.util.restclient.RestfulFactory;
import org.openo.baseservice.roa.util.restclient.RestfulParametes;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.nfvo.common.servicetoken.ServiceTokenHelp;

/**
 * 
* The utilty of basic restful interface operation<br/>
* <p>
* </p>
* 
* @author
* @version NFVO 0.5 May 15, 2016
 */
public abstract class RestfulUtil {

    private static final OssLog LOG = OssLogFactory.getLogger(RestfulUtil.class);

    private RestfulUtil() {
    }

    /**
     * Handle RestfulResponse from other app.
     *
     * @param path : The url of the other app
     * @param methodName : The method name of the other app
     * @param paraJson : The parameter to the other app
     * @param context : The context to the other app
     * @return : The answer
     */
    public static JSONObject sendReqToApp(String path, String methodName, JSONObject paraJson, HttpContext context) {
        JSONObject retJson = new JSONObject();
        retJson.put("retCode", Constant.REST_FAIL);

        RestfulResponse restfulResponse = RestfulUtil.getRestResByDefault(path, methodName, paraJson, context);

        if(restfulResponse == null) {
            LOG.error("function=sendReqToApp, msg=data from other app is null");
            retJson.put("data", "get null result");
        } else if(restfulResponse.getStatus() == Constant.HTTP_OK) {
            JSONObject object = JSONObject.fromObject(restfulResponse.getResponseContent());
            if(!object.containsKey("retCode") && object.containsKey("result")) {
                return object;
            }
            if(object.getInt("retCode") == Constant.REST_SUCCESS) {
                retJson.put("retCode", Constant.REST_SUCCESS);
                retJson.put("data", object.get("data"));
                return retJson;
            }
            return object;
        } else {
            retJson.put("data", "send to other app got error status: " + restfulResponse.getStatus());
        }
        return retJson;
    }

    /**
     * Within apps, we support a default method to invoke.
     *
     * @param path : The url of the other app
     * @param methodName : The method name of the other app
     * @param bodyParam : The parameter to the other app
     * @param context : The context to the other app
     * @return : The answer
     */
    @SuppressWarnings("unchecked")
    public static RestfulResponse getRestResByDefault(String path, String methodName, JSONObject bodyParam,
            HttpContext context) {
        RestfulParametes restParametes = new RestfulParametes();
        Map<String, String> headerMap = new HashMap<String, String>(3);
        headerMap.put(Constant.CONTENT_TYPE, Constant.APPLICATION);
        headerMap.put(Constant.X_AUTH_TOKEN, getTokenFromContext(context));
        restParametes.setHeaderMap(headerMap);
        restParametes.putHttpContextHeader("Content-Type", "application/json;charset=UTF-8");

        if(Constant.GET.equals(methodName) || Constant.DELETE.equals(methodName)) {
            if(null != bodyParam) {
                Map<String, String> paramsMap = new HashMap<String, String>(Constant.DEFAULT_COLLECTION_SIZE);
                if(path.contains("?")) {
                    String[] utlList = path.split("\\?");
                    String[] params = utlList[1].split("&");
                    int paramsSize = params.length;

                    for(int i = 0; i < paramsSize; i++) {
                        paramsMap.put(params[i].split("=")[0], params[i].split("=")[1]);
                    }
                }

                String paramKey = null;
                Iterator<String> nameItr = bodyParam.keys();
                while(nameItr.hasNext()) {
                    paramKey = nameItr.next();
                    paramsMap.put(paramKey, bodyParam.get(paramKey).toString());

                }

                restParametes.setParamMap(paramsMap);
            }
        } else {
            restParametes.setRawData(bodyParam == null ? null : bodyParam.toString());
        }

        return getRestRes(methodName, path, restParametes);
    }


    /**
     * Send restful notification to other module.
     *
     * @param path : The url of the other app
     * @param methodName : The method name of the other app
     * @param bodyParam : The parameter to the other app
     * @param context : The context to the other app
     */
    public static void sentEvtByRest(String path, String methodName, JSONObject bodyParam, HttpContext context) {
        RestfulParametes restParametes = new RestfulParametes();
        Map<String, String> headerMap = new HashMap<String, String>(3);
        headerMap.put(Constant.CONTENT_TYPE, Constant.APPLICATION);
        headerMap.put(Constant.X_AUTH_TOKEN, getTokenFromContext(context));
        restParametes.setHeaderMap(headerMap);
        restParametes.setRawData(bodyParam == null ? null : bodyParam.toString());

        getRestRes(methodName, path, restParametes, new AsyncCallback());
    }

    /**
     * Encapsulate the java reflect exception.
     *
     * @param methodName : The method name of the other app
     * @param objects : All the parameter of the other app
     * @return : The answer
     */
    public static RestfulResponse getRestRes(String methodName, Object... objects) {
        Restful rest = RestfulFactory.getRestInstance();
        try {
            if(objects == null || rest == null) {
                return null;
            }

            Class<?>[] classes = new Class[objects.length];
            for(int i = 0; i < objects.length; i++) {
                classes[i] = objects[i].getClass();
            }
            if(methodName.startsWith("async")) {
                classes[classes.length - 1] = RestfulAsyncCallback.class;
            }

            Class<?> rtType = methodName.startsWith("async") ? void.class : RestfulResponse.class;
            MethodType mt = MethodType.methodType(rtType, classes);
            Object reuslt =
                    MethodHandles.lookup().findVirtual(rest.getClass(), methodName, mt).bindTo(rest)
                            .invokeWithArguments(objects);
            if(reuslt != null) {
                return (RestfulResponse)reuslt;
            }
            LOG.warn("function=getRestRes, msg: invoke Restful async {} method which return type is Void.", methodName);
            return null;
        } catch(ReflectiveOperationException e) {
            LOG.error("function=getRestRes, msg=error occurs, e={}.", e);
        } catch(Exception e) {
            LOG.error("function=getRestRes, msg=Exception occurs, e={}.", e);
            try {
                throw (HwDoException)new HwDoException().initCause(e.getCause());
            } catch(HwDoException e1) {
                LOG.error("function=getRestRes, msg=HwDoException occurs, e={}.", e1);
            }
        }
        return null;
    }

    public static long sendAuditLogByDefault(String url, RestfulParametes restParametes) {
        RestfulResponse response = RestfulUtil.getRestRes(Constant.POST, url, restParametes);
        if(response == null) {
            LOG.error("function=sendAuditLogByDefault, msg=response is null, please check.");
            return Constant.ERROR_CODE;
        }
        return Long.parseLong(response.getResponseContent());
    }

    private static String getTokenFromContext(HttpContext context) {
        if(context != null && context.getHttpServletRequest() != null) {
            return context.getHttpServletRequest().getHeader(Constant.X_AUTH_TOKEN);
        }
        return ServiceTokenHelp.createServiceToken();
    }
}
