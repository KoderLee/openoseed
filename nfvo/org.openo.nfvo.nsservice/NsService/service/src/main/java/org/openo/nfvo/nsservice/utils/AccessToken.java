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

import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.openo.nfvo.common.utils.JsonUtil;

/**
 * 
* The token manager, keep the token status for every http rest call<br/>
* <p>
* </p>
* 
* @author
* @version NFVO 0.5 May 15, 2016
 */
public class AccessToken {

    private String accessToken;

    private int expire;

    private long createTime;

    /**
    * Constructor<br/>
    * <p>
    * </p>
    *
    * @since NFVO 0.5
    */
    public AccessToken() {
        // Do nothing
    }

    public AccessToken(String token, int ttl) {
        this.accessToken = token;
        this.expire = ttl;
        this.createTime = System.currentTimeMillis();
    }

    public AccessToken(String accessToken, Integer expire, Long createTime) {
        this.accessToken = accessToken;
        this.expire = expire == null ? 0 : expire;
        this.createTime = createTime == null ? 0 : createTime;
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public int getExpire() {
        return this.expire;
    }

    public long getCreateTime() {
        return this.createTime;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((accessToken == null) ? 0 : accessToken.hashCode());
        result = prime * result + (int)(createTime ^ (createTime >>> 32));
        result = prime * result + expire;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }
        if(obj == null) {
            return false;
        }
        if(getClass() != obj.getClass()) {
            return false;
        }
        AccessToken other = (AccessToken)obj;
        if(accessToken == null) {
            if(other.accessToken != null) {
                return false;
            }
        } else if(!accessToken.equals(other.accessToken)) {
            return false;
        }
        if(createTime != other.createTime) {
            return false;
        }
        if(expire != other.expire) {
            return false;
        }
        return true;
    }

    public boolean valid() {
        if(this.expire == 0) {
            return true;
        }
        return System.currentTimeMillis() - this.createTime <= 1000L * this.expire;
    }

    @Override
    public String toString() {
        JSONObject tokenObj = new JSONObject();
        tokenObj.put("accessToken", StringUtils.trimToEmpty(this.getAccessToken()));
        tokenObj.put("expire", this.getExpire());
        tokenObj.put("createTime", this.getCreateTime());
        return tokenObj.toString();
    }

    public static AccessToken toEntity(JSONObject jsonObject) {
        String token = JsonUtil.getJsonFieldStr(jsonObject, "accessToken");
        Integer expire = JsonUtil.getJsonFieldInt(jsonObject, "expire");
        Long createTime = JsonUtil.getJsonFieldLong(jsonObject, "createTime");
        AccessToken accessToken = new AccessToken(token, expire, createTime);
        return accessToken;
    }
}
