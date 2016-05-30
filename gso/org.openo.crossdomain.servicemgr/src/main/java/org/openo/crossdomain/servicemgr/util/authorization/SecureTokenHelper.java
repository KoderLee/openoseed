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
package org.openo.crossdomain.servicemgr.util.authorization;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.openo.crossdomain.commsvc.formation.util.jsonutil.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import org.openo.baseservice.deploy.util.DefaultEnvUtil;
import org.openo.baseservice.encrypt.cbb.CipherException;
import org.openo.baseservice.encrypt.cbb.CipherManager;
import org.openo.baseservice.encrypt.cbb.KeyType;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.crossdomain.commsvc.common.util.erhttpclientutil.AuthSoConfigInfo;
import org.openo.crossdomain.commsvc.common.util.erhttpclientutil.Restful;
import org.openo.crossdomain.commsvc.common.util.erhttpclientutil.RestfulFactory;
import org.openo.crossdomain.commsvc.common.util.erhttpclientutil.RestfulOptions;
import org.openo.crossdomain.commsvc.common.util.erhttpclientutil.RestfulParametes;
import org.openo.crossdomain.commsvc.common.util.erhttpclientutil.RestfulResponse;

/**
 * Help to get secure token.<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
public class SecureTokenHelper {

    private static final String URL_TOKEN = "/v3/auth/tokens";

    private static Logger logger = LoggerFactory.getLogger(SecureTokenHelper.class);

    private static Map<String, Object> getAssumeRequestBodyMap(String domainId) {
        Map<String, String> assumeMap = new HashMap<String, String>();
        assumeMap.put("domain_id", domainId);
        assumeMap.put("xrole_name", "op_service");

        List<String> methodList = new ArrayList<String>();
        methodList.add("hw_assume_role");

        Map<String, Object> identityMap = new HashMap<String, Object>();
        identityMap.put("hw_assume_role", assumeMap);
        identityMap.put("methods", methodList);

        Map<String, Object> projectMap = new HashMap<String, Object>();
        projectMap.put("name", "cn-north-1");

        Map<String, Object> scopeMap = new HashMap<String, Object>();
        scopeMap.put("project", projectMap);

        Map<String, Object> authMap = new HashMap<String, Object>();
        authMap.put("identity", identityMap);
        authMap.put("scope", scopeMap);

        Map<String, Object> requestBodyMap = new HashMap<String, Object>();
        requestBodyMap.put("auth", authMap);
        return requestBodyMap;
    }

    /**
     * Get token.<br/>
     *
     * @param accessName user
     * @param accessValue value
     * @param tenantId tenant ID
     * @param projectName project name
     * @return token
     * @since crossdomain 0.5
     */
    public static String getAssumeToken(String accessName, String accessValue, String tenantId, String projectName) {
        try {
            String assumeRequestBody = JsonUtil.marshal(getAssumeRequestBodyMap(tenantId));
            String serviceXToken = SecureInnerTokenHolder.getInnerSecureToken();

            if(StringUtils.isEmpty(AuthSoConfigInfo.getInstance().getAuthUrl())) {
                Map<String, String> httpHeaders = new HashMap<String, String>();
                httpHeaders.put("X-Auth-Token", serviceXToken);
                return postAussmeTokenByProxy(URL_TOKEN, assumeRequestBody, httpHeaders);
            } else {
                Map<String, String> httpHeaders = new HashMap<String, String>();
                httpHeaders.put("X-Auth-Token", serviceXToken);
                return postAussmeTokenBySoClient(URL_TOKEN, assumeRequestBody, httpHeaders);
            }
        } catch(ServiceException e) {
            logger.error("SecureTokenHelper.getAuthToken>>marshal map exception: ", e.getMessage());
            return "";
        }

    }

    private static Map<String, Object> getRequestBodyMap(String accessName, char pd[]) {
        Map<String, String> domainMap = new HashMap<String, String>();
        domainMap.put("name", "op_service");

        Map<String, Object> userMap = new HashMap<String, Object>();
        userMap.put("name", accessName);
        userMap.put("domain", domainMap);
        userMap.put("password", new String(pd));

        Map<String, Object> passwdMap = new HashMap<String, Object>();
        passwdMap.put("user", userMap);

        List<String> methodList = new ArrayList<String>();
        methodList.add("password");

        Map<String, Object> identityMap = new HashMap<String, Object>();
        identityMap.put("password", passwdMap);
        identityMap.put("methods", methodList);

        Map<String, Object> scopeMap = new HashMap<String, Object>();
        scopeMap.put("domain", domainMap);

        Map<String, Object> authMap = new HashMap<String, Object>();
        authMap.put("identity", identityMap);
        authMap.put("scope", scopeMap);

        Map<String, Object> requestBodyMap = new HashMap<String, Object>();
        requestBodyMap.put("auth", authMap);
        return requestBodyMap;
    }

    /**
     * Get authentication token.<br/>
     *
     * @param accessName user
     * @param identity access indentify
     * @return token
     * @since crossdomain 0.5
     */
    public static String getAuthToken(String accessName, String identity) {
        if(StringUtils.isEmpty(accessName) || StringUtils.isEmpty(identity)) {
            logger.error("SecureTokenHelper.getAuthToken>>bad parameters accessName or identity is null");
            return "";
        }

        StringBuffer sb = new StringBuffer();
        try {
            char pd[] = CipherManager.getInstance().decrypt(identity.toCharArray(), KeyType.SHARE, "common_shared");

            if(pd == null) {
                logger.error("SecureTokenHelper.getAuthToken>>pd or us is null");
                return "";
            }

            Map<String, Object> requestMap = getRequestBodyMap(accessName, pd);
            sb.append(JsonUtil.marshal(requestMap));
            requestMap.clear();
            clearSensitivityMem(pd);

            if(StringUtils.isEmpty(AuthSoConfigInfo.getInstance().getAuthUrl())) {
                return getAuthTokenByProxy(sb);
            } else {
                return getAuthTokenBySoClient(sb);
            }
        } catch(CipherException e) {
            logger.error("SecureTokenHelper.getAuthToken>>dcrpt throw an exception: ", e.getMessage());
            return "";
        } catch(ServiceException e) {
            logger.error("SecureTokenHelper.getAuthToken>>marshal map throw an exception: ", e.getMessage());
            return "";
        }

    }

    private static String getAuthTokenByProxy(StringBuffer rawData) {
        final RestfulParametes restfulParametes = new RestfulParametes();
        restfulParametes.putHttpContextHeader("Content-Type", "application/json;charset=UTF-8");
        restfulParametes.setRawData(rawData.toString());

        rawData.setLength(0);
        RestfulOptions options = new RestfulOptions();
        options.setRestTimeout(300000);
        RestfulResponse rsp;
        try {
            rsp = RestfulFactory.getRestInstance().post(URL_TOKEN, restfulParametes, options);
        } catch(ServiceException e) {
            logger.error("SecureTokenHelper.getAuthTokenByProxy >> send post request throw an exception: ",
                    e.getMessage());
            return "";
        }

        if(rsp != null && rsp.getStatus() / 100 != 2) {
            logger.error("SecureTokenHelper.getAuthToken >> http post error, errorCode: ", rsp.getStatus());
            return "";
        }

        return rsp != null ? rsp.getRespHeaderStr("X-Subject-Token") : "";
    }

    private static String getAuthTokenBySoClient(StringBuffer rawData) {
        String iamPort = null;
        String iamIp = null;
        String authUrl = AuthSoConfigInfo.getInstance().getAuthUrl();
        if(StringUtils.isEmpty(authUrl) || authUrl.split(":").length != 3) {
            logger.error("SecureTokenHelper.getAuthTokenBySoClient >> bad parameter authUrl format");
            return "";
        }

        String[] splits = authUrl.split(":");
        iamIp = splits[1].substring(2, splits[1].length());
        iamPort = splits[2];
        final RestfulParametes restfulParametes = new RestfulParametes();
        restfulParametes.putHttpContextHeader("Content-Type", "application/json;charset=UTF-8");
        restfulParametes.setRawData(rawData.toString());

        rawData.setLength(0);

        String ssloptionfile =
                DefaultEnvUtil.getAppRoot() + File.separator + "etc" + File.separator + "sslconf" + File.separator
                        + "ssl.nfvo.properties";
        Restful rest = RestfulFactory.createHttpsRest(ssloptionfile, null);

        RestfulResponse rsp = null;
        try {
            RestfulOptions opt = new RestfulOptions();
            opt.setHost(iamIp);
            opt.setPort(Integer.parseInt(iamPort));

            rsp = rest.post(URL_TOKEN, restfulParametes, opt);
        } catch(ServiceException e) {
            logger.error("SecureTokenHelper.getAuthTokenBySoClient >> send post request throw an exception: ", e);
            return "";
        } catch(NumberFormatException e) {
            logger.error("SecureTokenHelper.getAuthTokenBySoClient >> NumberFormatException iamPort format");
            return "";
        }

        if(rsp != null && rsp.getStatus() / 100 != 2) {
            logger.error("SecureTokenHelper.getAuthTokenBySoClient >> http post error, errorCode: ", rsp.getStatus());
            return "";
        }

        return rsp != null ? rsp.getRespHeaderStr("X-Subject-Token") : "";
    }

    /**
     * clearSensitivityMem
     * 
     * @param content char[]
     */
    private static void clearSensitivityMem(char[] content) {
        if(content != null && content.length > 0) {
            for(int i = 0; i < content.length; i++) {
                content[i] = '\000';
            }
        }
    }

    private static String
            postAussmeTokenByProxy(final String url, String rawData, final Map<String, String> httpHeaders) {
        final RestfulParametes restfulParametes = new RestfulParametes();

        if(!CollectionUtils.isEmpty(httpHeaders)) {
            for(Entry<String, String> entry : httpHeaders.entrySet()) {
                restfulParametes.putHttpContextHeader(entry.getKey(), entry.getValue());
            }
        }

        restfulParametes.putHttpContextHeader("Content-Type", "application/json;charset=UTF-8");
        restfulParametes.setRawData(rawData);

        RestfulOptions options = new RestfulOptions();
        options.setRestTimeout(300000);

        RestfulResponse rsp = null;
        try {
            rsp = RestfulFactory.getRestInstance(RestfulFactory.PROTO_HTTPS).post(url, restfulParametes, options);
        } catch(ServiceException e) {
            logger.error("SecureTokenHelper.post>>send post request throw an exception: ");
            return "";
        }

        return rsp != null ? rsp.getRespHeaderStr("X-Subject-Token") : "";

    }

    private static String postAussmeTokenBySoClient(final String url, String rawData,
            final Map<String, String> httpHeaders) {
        final RestfulParametes restfulParametes = new RestfulParametes();

        if(!CollectionUtils.isEmpty(httpHeaders)) {
            for(Entry<String, String> entry : httpHeaders.entrySet()) {
                restfulParametes.putHttpContextHeader(entry.getKey(), entry.getValue());
            }
        }

        String iamPort = null;
        String iamIp = null;
        String authUrl = AuthSoConfigInfo.getInstance().getAuthUrl();
        if(StringUtils.isEmpty(authUrl) || authUrl.split(":").length != 3) {
            logger.error("SecureTokenHelper.getAuthTokenBySoClient >> bad parameter authUrl format");
            return "";
        }

        String[] splits = authUrl.split(":");
        iamIp = splits[1].substring(2, splits[1].length());
        iamPort = splits[2];

        restfulParametes.putHttpContextHeader("Content-Type", "application/json;charset=UTF-8");
        restfulParametes.setRawData(rawData);

        RestfulOptions options = new RestfulOptions();
        options.setRestTimeout(300000);

        String ssloptionfile =
                DefaultEnvUtil.getAppRoot() + File.separator + "etc" + File.separator + "sslconf" + File.separator
                        + "ssl.nfvo.properties";
        Restful rest = RestfulFactory.createHttpsRest(ssloptionfile, null);

        RestfulResponse rsp = null;
        try {
            RestfulOptions opt = new RestfulOptions();
            opt.setHost(iamIp);
            opt.setPort(Integer.parseInt(iamPort));

            rsp = rest.post(URL_TOKEN, restfulParametes, opt);
        } catch(ServiceException e) {
            logger.error("SecureTokenHelper.getAuthTokenBySoClient >> send post request throw an exception: ", e);
            return "";
        } catch(NumberFormatException e) {
            logger.error("SecureTokenHelper.getAuthTokenBySoClient >> NumberFormatException iamPort format");
            return "";
        }

        if(rsp != null && rsp.getStatus() / 100 != 2) {
            logger.error("SecureTokenHelper.getAuthTokenBySoClient >> http post error, errorCode: ", rsp.getStatus());
            return "";
        }

        return rsp != null ? rsp.getRespHeaderStr("X-Subject-Token") : "";

    }

}
