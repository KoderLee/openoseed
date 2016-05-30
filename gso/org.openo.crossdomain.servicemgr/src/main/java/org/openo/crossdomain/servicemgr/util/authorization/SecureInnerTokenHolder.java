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

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Holder that has inner secure token.<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
public class SecureInnerTokenHolder {

    private static Map<String, TokenObj> SECURE_TOKEN_MAP = new HashMap<String, TokenObj>();

    private static final String INNER_TOKEN_KEY = "inner_token";

    /**
     * logger
     */
    private static final Logger logger = LoggerFactory.getLogger(SecureInnerTokenHolder.class);

    /**
     * Get inner secure token.<br/>
     *
     * @return token
     * @since crossdomain 0.5
     */
    public static String getInnerSecureToken() {

        if(isInnerTokenMapEmpty()) {
            if(!initSecureTokenHolder()) {
                logger.error("getInnerSecureToken >> return empty string");
                return "";
            }
        } else {
            TokenObj tokenObj = SECURE_TOKEN_MAP.get(INNER_TOKEN_KEY);
            if(tokenObj.isTokenTimeout()) {
                if(!updateSecureTokenHolder()) {
                    logger.error("getInnerSecureToken >> return empty string");
                    return "";
                }
            }

        }

        return SECURE_TOKEN_MAP.get(INNER_TOKEN_KEY).getToken();
    }

    private static boolean initSecureTokenHolder() {
        synchronized(SecureInnerTokenHolder.class) {

            if(isInnerTokenMapEmpty()) {
                String token =
                        SecureTokenHelper.getAuthToken(AuthConfigInfo.getInstance().getAccessName(), AuthConfigInfo
                                .getInstance().getIdentity());
                TokenObj tokenObj = new TokenObj(token);
                if(StringUtils.isNotEmpty(token)) {
                    SECURE_TOKEN_MAP.put(INNER_TOKEN_KEY, tokenObj);
                } else {

                    logger.error("SecureTokenHolder >> initSecureTokenHolder failed >> getAuthToken return empty");
                    return false;
                }
            }
        }

        return true;
    }

    private static boolean updateSecureTokenHolder() {
        synchronized(SecureInnerTokenHolder.class) {

            TokenObj tokenObj = SECURE_TOKEN_MAP.get(INNER_TOKEN_KEY);
            if(tokenObj.isTokenTimeout()) {
                String newToken =
                        SecureTokenHelper.getAuthToken(AuthConfigInfo.getInstance().getAccessName(), AuthConfigInfo
                                .getInstance().getIdentity());
                TokenObj newTokenObj = new TokenObj(newToken);
                if(StringUtils.isNotEmpty(newToken)) {
                    SECURE_TOKEN_MAP.put(INNER_TOKEN_KEY, newTokenObj);
                } else {

                    logger.error("SecureTokenHolder >> updateSecureTokenHolder failed >> retry getAuthToken return empty");
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Check if inner token is not valid.<br/>
     *
     * @return true when there is no inner token.
     * @since crossdomain 0.5
     */
    public static boolean isInnerTokenMapEmpty() {
        return MapUtils.isEmpty(SECURE_TOKEN_MAP) || (SECURE_TOKEN_MAP.get(INNER_TOKEN_KEY) == null);
    }

}
