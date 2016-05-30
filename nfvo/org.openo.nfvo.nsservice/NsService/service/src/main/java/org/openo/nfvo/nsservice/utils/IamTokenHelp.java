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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.openo.baseservice.deploy.util.DefaultEnvUtil;
import org.openo.baseservice.encrypt.cbb.CipherException;
import org.openo.baseservice.encrypt.cbb.CipherManager;
import org.openo.baseservice.encrypt.cbb.KeyType;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.util.restclient.RestfulFactory;
import org.openo.baseservice.roa.util.restclient.RestfulParametes;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;

/**
 * 
* Supply the foundational token manager for rest interface call<br/>
* <p>
* </p>
* 
* @author
* @version NFVO 0.5 May 15, 2016
 */
public final class IamTokenHelp {

    private static final Logger LOGGER = LoggerFactory.getLogger(IamTokenHelp.class);

    private static volatile boolean isCreating = false;

    private static final ConcurrentHashMap<String, AccessToken> TOKEN_CACHE =
            new ConcurrentHashMap<String, AccessToken>();

    private IamTokenHelp() {

    }

    public static String createServiceToken() {
        String processInstanceId = DefaultEnvUtil.getProcessName();
        AccessToken token = (AccessToken)TOKEN_CACHE.get(processInstanceId);
        if(token == null || !token.valid()) {
            return getNewServiceToken(processInstanceId);
        }
        return token.getAccessToken();
    }

    private static synchronized AccessToken createServiceToken(String processInstanceId) {
        if((processInstanceId == null) || (processInstanceId.length() <= 0)) {
            LOGGER.error("get app param failed.");
            return null;
        }
        LOGGER.warn("begin to create service token" + processInstanceId);
        isCreating = true;

        try {

            Map<String, String> header = new HashMap<String, String>(10);
            header.put("Content-Type", "application/json;charset=UTF-8");
            header.put("Accept", "application/json");

            RestfulParametes secondReqParams = new RestfulParametes();
            secondReqParams.setHeaderMap(header);

            AuthConfigInfo configInfo = AuthConfigInfo.getInstance();
            String userName = configInfo.gotUserName();
            String userPsw = configInfo.gotEncryptedPW();
            char[] pd = CipherManager.getInstance().decrypt(userPsw.toCharArray(), KeyType.SHARE, "common_shared");
            String domain = configInfo.gotDomain();
            secondReqParams.setRawData(buildJson(userName, new String(pd), domain));

            RestfulResponse secondRsp = RestfulFactory.getRestInstance().post("/v3/auth/tokens", secondReqParams);

            clearSensitivityMem(pd);
            if(secondRsp == null) {
                LOGGER.error("error s_rand, authorization token return null");
                return null;
            }
            String tokenStr = secondRsp.getRespHeaderStr("X-Subject-Token");
            AccessToken localAccessToken = new AccessToken();
            localAccessToken.setAccessToken(tokenStr);
            return localAccessToken;
        } catch(ServiceException e) {
            LOGGER.error("Get ServiceException.", e);
        } catch(CipherException e) {
            LOGGER.error("Get ServiceException.", e);
        } finally {
            LOGGER.error("after create service token");
            isCreating = false;
        }
        return null;
    }

    private static synchronized String getNewServiceToken(String processInstanceId) {
        if(isCreating) {
            LOGGER.warn("service token is creating");
            waitSomeTime(50);
            return getServiceTokenFromCache(processInstanceId);
        }
        return createServiceTokenAndPut(processInstanceId);
    }

    private static void waitSomeTime(int i) {
        final Object lockObject = new Object();
        try {
            synchronized(lockObject) {
                TimeUnit.MILLISECONDS.sleep(i);
            }
        } catch(InterruptedException localInterruptedException) {
            LOGGER.error("SLEEP WRONG");
        }
    }

    private static String getServiceTokenFromCache(String processInstanceId) {
        AccessToken accToken = (AccessToken)TOKEN_CACHE.get(processInstanceId);
        if(accToken != null) {
            return accToken.getAccessToken();
        }
        return null;
    }

    private static String createServiceTokenAndPut(String processInstanceId) {
        AccessToken accesstoken = createServiceToken(processInstanceId);
        if(accesstoken != null) {
            TOKEN_CACHE.put(processInstanceId, accesstoken);
        }
        return accesstoken == null ? null : accesstoken.getAccessToken();
    }

    public static void setTokenInfo(RestfulParametes parameters) {
        parameters.putHttpContextHeader("X-Auth-Token", createServiceToken());
    }

    public static void clearToken() {
        String processInstanceId = org.openo.baseservice.deploy.util.DefaultEnvUtil.getProcessName();
        TOKEN_CACHE.remove(processInstanceId);
    }

    private static String buildJson(String name, String psw, String doMain) {

        String mode =
                "{'auth':{'identity':{'methods':['password'],'password':{'user':{'name':'','password':'',"
                        + "'domain':{'name':''}}}},'scope':{'domain':{'name':''}}}}";
        JSONObject json = JSONObject.fromObject(mode);
        // add Name
        json.getJSONObject("auth").getJSONObject("identity").getJSONObject("password").getJSONObject("user")
                .put("name", name);
        // add psw
        json.getJSONObject("auth").getJSONObject("identity").getJSONObject("password").getJSONObject("user")
                .put("password", psw);
        // add domain
        json.getJSONObject("auth").getJSONObject("identity").getJSONObject("password").getJSONObject("user")
                .getJSONObject("domain").put("name", doMain);
        json.getJSONObject("auth").getJSONObject("scope").getJSONObject("domain").put("name", doMain);
        return json.toString();
    }

    private static void clearSensitivityMem(char[] sensityContent) {
        if(sensityContent != null && sensityContent.length > 0) {
            for(int i = 0; i < sensityContent.length; i++) {
                sensityContent[i] = '\000';
            }
        }
    }

}
