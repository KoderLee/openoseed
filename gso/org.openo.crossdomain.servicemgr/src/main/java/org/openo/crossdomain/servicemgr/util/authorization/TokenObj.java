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

import java.util.Calendar;

/**
 * Token information.<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
public class TokenObj {

    private static final long TIME_OUT_SLOT = 3600000L;

    private long createTime;

    private String tokenStr;

    /**
     * Constructor.<br/>
     * @param token
     */
    public TokenObj(String token) {
        createTime = Calendar.getInstance().getTimeInMillis();
        this.tokenStr = token;
    }

    /**
     * Token is out time.<br/>
     *
     * @return true when token is out time.
     * @since crossdomain 0.5
     */
    boolean isTokenTimeout() {
        long now = Calendar.getInstance().getTimeInMillis();
        return now - this.createTime >= TIME_OUT_SLOT;
    }

    public String getToken() {
        return tokenStr;
    }
}
