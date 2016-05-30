/*
 * Copyright (c) 2016, Huawei Technologies Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *  
 */
package org.openo.sdno.cbb.sdnwan.util.error.code;

import org.openo.sdno.cbb.sdnwan.util.error.ErrorCodeUtils;

/**
 * ERROR CODES RANGE 192001-193000 FOR L2VPN<br/>
 * 
 * @author
 * @version SDNO 0.5 18-Mar-2016
 */
public class L2VpnSvcErrorCode {

    public static final String APP_NAME = "l2vpnsvc";

    public static final String DEPLOY_CREATE_CONTROLLER_FAIL = ErrorCodeUtils.getErrorCode(APP_NAME, "create_vpn",
            "controller_return_fail");

    public static final String DEPLOY_DELETE_CONTROLLER_FAIL = ErrorCodeUtils.getErrorCode(APP_NAME, "delete_vpn",
            "controller_return_fail");

    public static final String DEPLOY_UPDATE_CONTROLLER_FAIL = ErrorCodeUtils.getErrorCode(APP_NAME, "update_vpn",
            "controller_return_fail");

    public static final String DEPLOY_UPDATE_IFCAR_CONTROLLER_FAIL = ErrorCodeUtils.getErrorCode(APP_NAME,
            "update_ifcar_vpn", "controller_return_fail");

    public static final String DEPLOY_UPDATE_STATUS_CONTROLLER_FAIL = ErrorCodeUtils.getErrorCode(APP_NAME,
            "update_status", "controller_return_fail");

    public static final String DEPLOY_GET_STATUS_CONTROLLER_FAIL = ErrorCodeUtils.getErrorCode(APP_NAME, "get_status",
            "controller_return_fail");

    public static final String DEPLOY_GET_TE_CONTROLLER_FAIL = ErrorCodeUtils.getErrorCode(APP_NAME, "get_te",
            "controller_return_fail");

    public static final String ONLY_ONE_SITE = ErrorCodeUtils.getErrorCode(APP_NAME, "l2vpncreate", "only_one_site");

    private L2VpnSvcErrorCode() {
    }
}
