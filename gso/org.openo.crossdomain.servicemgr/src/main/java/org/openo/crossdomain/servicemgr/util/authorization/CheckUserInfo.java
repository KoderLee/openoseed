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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.common.HttpContext;
import org.openo.baseservice.roa.util.restclient.RestfulFactory;
import org.openo.baseservice.roa.util.restclient.RestfulParametes;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.crossdomain.commsvc.common.util.jsonutil.JsonUtil;

/**
 * Class for checkint if user has right.<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
public final class CheckUserInfo {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckUserInfo.class);

    private static final String IAM_USER_NAME = "X-User-Name";

    private static final String IAM_USER_ID = "X-User-Id";

    /**
     * Check service token.<br/>
     *
     * @param accessName user name
     * @param accessValue request content
     * @param context http context
     * @return false if there is something wrong with token.
     * @since crossdomain 0.5
     */
    public static synchronized boolean checkServiceToken(String accessName, String accessValue, HttpContext context) {
        if(StringUtils.isEmpty(accessName)) {
            LOGGER.error("get accessName param failed.");
            return false;
        }
        if(StringUtils.isEmpty(accessValue)) {
            LOGGER.error("get accessValue param failed.");
            return false;
        }
        if(null == context) {
            LOGGER.error("CheckUserInfo context param is null.");
            return false;
        }

        try {

            Map<String, String> header = new HashMap<String, String>(10);
            header.put("Content-Type", "application/json;charset=UTF-8");
            header.put("Accept", "application/json");

            RestfulParametes secondReqParams = new RestfulParametes();
            secondReqParams.setHeaderMap(header);

            HttpServletRequest req = context.getHttpServletRequest();
            String userId = null;
            String cruentUser = null;

            if(req != null) {

                cruentUser = req.getHeader(IAM_USER_NAME);

                if(!StringUtils.equals(cruentUser, accessName)) {
                    LOGGER.error("CheckUserInfo check user fail, accessName not Match!!");
                    return false;
                }

                userId = req.getHeader(IAM_USER_ID);

                if(StringUtils.isEmpty(userId)) {
                    LOGGER.error("CheckUserInfo check user fail,userId is null");
                    return false;
                }
            }

            String identityBody = JsonUtil.marshal(getIdentityBodyMap(userId, accessValue));
            secondReqParams.setRawData(identityBody);

            RestfulResponse secondRsp = RestfulFactory.getRestInstance().post("/v3/auth/tokens", secondReqParams);

            accessValue = "";
            secondReqParams = null;
            if(secondRsp != null && !StringUtils.isEmpty(secondRsp.getRespHeaderStr("X-Subject-Token"))) {

                return true;
            }

            LOGGER.error("error s_rand, authorization token return null");
            return false;
        } catch(ServiceException e) {
            LOGGER.error("Get ServiceException.", e);
        }
        return false;
    }

    private static Map<String, Object> getIdentityBodyMap(String id, String psw) {
        Map<String, String> userMap = new HashMap<String, String>();
        userMap.put("id", id);
        userMap.put("password", psw);

        Map<String, Object> passwdMap = new HashMap<String, Object>();
        passwdMap.put("user", userMap);

        List<String> methodList = new ArrayList<String>();
        methodList.add("password");

        Map<String, Object> identityMap = new HashMap<String, Object>();
        identityMap.put("password", passwdMap);
        identityMap.put("methods", methodList);

        Map<String, Object> authMap = new HashMap<String, Object>();
        authMap.put("identity", identityMap);

        Map<String, Object> requestBodyMap = new HashMap<String, Object>();
        requestBodyMap.put("auth", authMap);
        return requestBodyMap;
    }
}
