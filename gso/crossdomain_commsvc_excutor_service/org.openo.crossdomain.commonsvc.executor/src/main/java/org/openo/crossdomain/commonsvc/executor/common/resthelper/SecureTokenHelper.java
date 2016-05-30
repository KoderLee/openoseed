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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.openo.crossdomain.commonsvc.executor.common.constant.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.openo.commonservice.deploy.util.DefaultEnvUtil;
import org.openo.commonservice.encrypt.cbb.CipherException;
import org.openo.commonservice.encrypt.cbb.CipherManager;
import org.openo.commonservice.encrypt.cbb.KeyType;
import org.openo.commonservice.remoteservice.exception.ServiceException;
import org.openo.crossdomain.commsvc.common.util.erhttpclientutil.AuthSoConfigInfo;
import org.openo.crossdomain.commsvc.common.util.erhttpclientutil.Restful;
import org.openo.crossdomain.commsvc.common.util.erhttpclientutil.RestfulFactory;
import org.openo.crossdomain.commsvc.common.util.erhttpclientutil.RestfulOptions;
import org.openo.crossdomain.commsvc.common.util.erhttpclientutil.RestfulParametes;
import org.openo.crossdomain.commsvc.common.util.erhttpclientutil.RestfulResponse;

public class SecureTokenHelper {

    public static final long MILLSECOND = 1000000000;

    public static final int REST_TIMEOUT = 300000;

    public static final String URL_TOKEN = "/v3/auth/tokens";

    private static Logger logger = LoggerFactory.getLogger(SecureTokenHelper.class);

    private static Map<String, Object> getRequestBodyMap(String accessName, char pd[]) {
        Map<String, String> domainMap = new HashMap<>();
        domainMap.put("name", "op_service");

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("name", accessName);
        userMap.put("domain", domainMap);
        userMap.put("password", new String(pd));

        Map<String, Object> passwdMap = new HashMap<>();
        passwdMap.put("user", userMap);

        List<String> methodList = new ArrayList<>();
        methodList.add("password");

        Map<String, Object> identityMap = new HashMap<>();
        identityMap.put("password", passwdMap);
        identityMap.put("methods", methodList);

        Map<String, Object> scopeMap = new HashMap<>();
        scopeMap.put("domain", domainMap);

        Map<String, Object> authMap = new HashMap<>();
        authMap.put("identity", identityMap);
        authMap.put("scope", scopeMap);

        Map<String, Object> requestBodyMap = new HashMap<>();
        requestBodyMap.put("auth", authMap);
        return requestBodyMap;
    }

    private static Map<String, Object> getTenantTokenRequestBodyMap(String tenantId) {
        Map<String, String> domainMap = new HashMap<>();
        domainMap.put("id", tenantId);

        Map<String, Object> assumeRoleMap = new HashMap<>();
        assumeRoleMap.put("domain_id", tenantId);
        assumeRoleMap.put("xrole_name", "op_service");

        List<String> methodList = new ArrayList<>();
        methodList.add("hw_assume_role");

        Map<String, Object> identityMap = new HashMap<>();
        identityMap.put("hw_assume_role", assumeRoleMap);
        identityMap.put("methods", methodList);

        Map<String, Object> scopeMap = new HashMap<>();
        scopeMap.put("domain", domainMap);

        Map<String, Object> authMap = new HashMap<>();
        authMap.put("identity", identityMap);
        authMap.put("scope", scopeMap);

        Map<String, Object> requestBodyMap = new HashMap<>();
        requestBodyMap.put("auth", authMap);
        return requestBodyMap;
    }

    /**
	 *Get Auth Token(inner User:ServiceExecutor)
	 *@param userName User Name for request
	 *@param passwd password for request
	 *@return token required
	 *@since crossdomain 0.5 2016-3-18
	 */
    public static String getAuthToken(String userName, String passwd) {
        StringBuffer sb = new StringBuffer();
        try {
            char pd[] = CipherManager.getInstance().decrypt(passwd.toCharArray(), KeyType.SHARE, "common_shared");

            if(pd == null) {
                logger.error("SecureTokenHelper.getAuthToken>>pd is null");
                return Constants.NULL_STR;
            }

            Map<String, Object> requstMap = getRequestBodyMap(userName, pd);

            ObjectMapper mapper = new ObjectMapper();
            sb.append(mapper.writeValueAsString(requstMap));
            requstMap.clear();
            clearSensitivityMem(pd);

            if(isSoDoDeployment()) {
                return getTokenBySoClient(null, sb);
            } else {
                return getTokenByProxy(null, sb);
            }

        } catch(IOException e) {
            logger.error("mapper.writeValueAsString fail", e);
            return Constants.NULL_STR;
        } catch(CipherException e) {
            logger.error(String.format("SecureTokenHelper.getAuthToken>>dcrpt throw an exception"), e);
            return Constants.NULL_STR;
        }
    }

	/**
	 *Get Tenant Token By TenandId
	 *@param tenantId tenantId for request
	 *@return tennant token required
	 *@since crossdomain 0.5 2016-3-18
	 */
    public static String getTenantIdToken(String tenantId) {
        StringBuffer strBuf = new StringBuffer();

        String token =
                SecureTokenHelper.getAuthToken(AuthConfigInfo.getInstance().getUserName(), AuthConfigInfo.getInstance()
                        .getEncryptedPW());
        if(StringUtils.isEmpty(token)) {
            return Constants.NULL_STR;
        }

        try {
            ObjectMapper mapper = new ObjectMapper();

            Map<String, Object> requstMap = getTenantTokenRequestBodyMap(tenantId);
            strBuf.append(mapper.writeValueAsString(requstMap));
            requstMap.clear();

            if(isSoDoDeployment()) {
                return getTokenBySoClient(token, strBuf);
            } else {
                return getTokenByProxy(token, strBuf);
            }
        } catch(IOException e) {
            logger.error("mapper.writeValueAsString fail", e);
            return Constants.NULL_STR;
        }
    }

    private static String getTokenBySoClient(String token, StringBuffer strBuf) {
        String iamPort = null;
        String iamIp = null;
        String authUrl = AuthSoConfigInfo.getInstance().getAuthUrl();
        if(StringUtils.isEmpty(authUrl) || authUrl.split(":").length != 3) {
            logger.error("SecureTokenHelper.getAuthTokenBySoClient >> bad parameter authUrl Formation");
            return Constants.NULL_STR;
        }

        String[] splits = authUrl.split(":");

        iamIp = splits[1].substring(2, splits[1].length());
        iamPort = splits[2];
        final RestfulParametes restfulParametes = new RestfulParametes();
        restfulParametes.putHttpContextHeader(Constants.HttpContext.CONTENT_TYPE_HEADER,
                Constants.HttpContext.MEDIA_TYPE_JSON);

        if(StringUtils.isNotEmpty(token)) {
            restfulParametes.putHttpContextHeader(Constants.HttpContext.X_AUTH_TOKEN, token);
        }
        restfulParametes.setRawData(strBuf.toString());

        strBuf.setLength(0);

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
            logger.error("SecureTokenHelper.getAuthTokenBySoClient >> send post request throw an exception", e);
            return Constants.NULL_STR;
        } catch(NumberFormatException e) {
            logger.error("SecureTokenHelper.getAuthTokenBySoClient >> NumberFormatException iamPort formation");
            return Constants.NULL_STR;
        }

        if(rsp != null && !httpStatusOk(rsp.getStatus())) {
            logger.error("SecureTokenHelper.getAuthTokenBySoClient >> http post error, errorCode: ", rsp.getStatus());
            return Constants.NULL_STR;
        }

        return (rsp != null) ? rsp.getRespHeaderStr(Constants.HttpContext.X_SUBJECT_TOKEN) : Constants.NULL_STR;
    }

    private static String getTokenByProxy(String token, StringBuffer sb) {
        final RestfulParametes restfulParametes = new RestfulParametes();
        restfulParametes.putHttpContextHeader(Constants.HttpContext.CONTENT_TYPE_HEADER,
                Constants.HttpContext.MEDIA_TYPE_JSON);

        if(StringUtils.isNotEmpty(token)) {
            restfulParametes.putHttpContextHeader(Constants.HttpContext.X_AUTH_TOKEN, token);
        }

        restfulParametes.setRawData(sb.toString());

        sb.setLength(0);
        long timeStart = System.nanoTime();
        RestfulOptions options = new RestfulOptions();
        options.setRestTimeout(REST_TIMEOUT);

        RestfulResponse rsp;
        try {
            rsp = RestfulFactory.getRestInstance().post(URL_TOKEN, restfulParametes, options);
        } catch(ServiceException e) {
            logger.error("SecureTokenHelper.getAuthTokenByProxy >> send post request throw an exception", e);
            return Constants.NULL_STR;
        }

        if((rsp != null) && (!httpStatusOk(rsp.getStatus()))) {
            long timeSpanPost = System.nanoTime() - timeStart;
            logger.error("SecureTokenHelper.getAuthToken >> http post error, errorCode: %s, timeSpan: %s ",
                    rsp.getStatus(), timeSpanPost / MILLSECOND);

            return Constants.NULL_STR;
        }

        return (rsp != null) ? rsp.getRespHeaderStr(Constants.HttpContext.X_SUBJECT_TOKEN) : Constants.NULL_STR;
    }

    /**
     * clearSensitivityMem
     *
     * @param content char[]
     */
    public static void clearSensitivityMem(char[] content) {
        if(content != null && content.length > Constants.NULL_ID) {
            for(int i = Constants.NULL_ID; i < content.length; i++) {
                content[i] = '\000';
            }
        }
    }

    private static boolean httpStatusOk(int status) {

        return (status / 100 == 2);
    }

    private static boolean isSoDoDeployment() {
        return StringUtils.isNotEmpty(AuthSoConfigInfo.getInstance().getAuthUrl());
    }
}
