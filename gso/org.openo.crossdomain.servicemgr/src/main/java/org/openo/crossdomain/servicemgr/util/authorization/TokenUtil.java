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

import org.apache.commons.lang.StringUtils;

import org.openo.baseservice.roa.common.HttpContext;

/**
 * Tools for token.<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
public class TokenUtil {

    private static final String DEFAULT_TENANT_ID = "TenantID";

    private static final String TENANT_HEAD_NAME = "X-Tenant-Id";

    /**
     * Get ProjectID<br/>
     *
     * @param token
     * @return ProjectID
     * @since crossdomain 0.5
     */
    public static String getProjectIDByToken(String token) {
        return "ProjectID";
    }

    /**
     * Get tenant ID from http context.<br/>
     *
     * @param context http context
     * @return tenant ID
     * @since crossdomain 0.5
     */
    public static String getTenantIDByRequest(HttpContext context) {
        if(context == null || context.getHttpServletRequest() == null) {
            return DEFAULT_TENANT_ID;
        }

        String tenantID = context.getHttpServletRequest().getHeader(TENANT_HEAD_NAME);

        if(StringUtils.isEmpty(tenantID)) {
            return DEFAULT_TENANT_ID;
        }

        return tenantID;
    }
}
