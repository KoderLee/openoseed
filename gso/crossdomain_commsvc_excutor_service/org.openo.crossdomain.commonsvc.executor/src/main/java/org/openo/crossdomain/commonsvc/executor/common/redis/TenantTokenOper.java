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
package org.openo.crossdomain.commonsvc.executor.common.redis;

import org.openo.commonservice.log.OssLog;
import org.openo.commonservice.log.OssLogFactory;
import org.openo.commonservice.redis.oper.MapOper;
import org.openo.commonservice.roa.util.restclient.RestfulParametes;

import org.openo.crossdomain.commonsvc.executor.common.constant.Constants;
import org.openo.crossdomain.commonsvc.executor.common.resthelper.SecureTokenHelper;
import org.openo.crossdomain.commonsvc.executor.common.util.CommonUtil;
import org.springframework.util.StringUtils;

import java.util.List;

public class TenantTokenOper {

    private static final OssLog log = OssLogFactory.getLogger(TenantTokenOper.class);

    private static TenantTokenOper instance;

    private MapOper<TokenEntry> mapOper;

    private String name = Constants.TENANT_TOKEN_RD;

    private TenantTokenOper() {
        mapOper = new MapOper<>(Constants.BUNDLE_NAME, Constants.RDB_NAME);
    }

    public synchronized static TenantTokenOper getInstance() {
        if(instance == null) {
            instance = new TenantTokenOper();
        }
        return instance;
    }
	/**
	 *get Tenant Token By TenantId
	 *@param tenantId tenantId
	 *@return token
	 *@since crossdomain 0.5 2016-3-18
	 */
    public String getTokenByTenantId(String tenantId) {

        List<TokenEntry> tokenList = mapOper.get(name, TokenEntry.class, tenantId);

        if(tokenList != null && tokenList.size() > Constants.NULL_ID) {
            TokenEntry entry = tokenList.get(Constants.NULL_ID);
            if(entry != null && entry.getExpireAt() > CommonUtil.getTimeInMillis()) {
                return entry.getToken();
            }
        }

        String token = SecureTokenHelper.getTenantIdToken(tenantId);
        if(StringUtils.hasLength(token)) {
            TokenEntry entry = new TokenEntry();
            entry.setToken(token);
            entry.setExpireAt(CommonUtil.getTimeInMillis() + Constants.ALMOST_ONE_DAY_AS_MS);
            mapOper.put(name, tenantId, entry);
        }

        return token;
    }

	
	/**
	 *set Tenant Token to resfulParameters
	 *@param tenantId tenantId
	 *@param restParametes restParametes
	 *@since crossdomain 0.5 2016-3-18
	 */
    public void setSecureToken(String tenantid, RestfulParametes restParametes) {

        String token = getTokenByTenantId(tenantid);

        if(StringUtils.hasLength(token)) {
            restParametes.putHttpContextHeader(Constants.HttpContext.X_AUTH_TOKEN, token);
        }
    }

    static private class TokenEntry {

        String token;

        Long expireAt;

        public Long getExpireAt() {
            return expireAt;
        }

        public void setExpireAt(Long expireAt) {
            this.expireAt = expireAt;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
