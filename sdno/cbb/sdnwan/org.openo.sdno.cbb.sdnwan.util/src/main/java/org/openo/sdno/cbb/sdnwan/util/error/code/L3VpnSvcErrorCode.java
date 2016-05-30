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
 * ERROR CODES RANGE 193001-194000 for L3VPN.<br/>
 * 
 * @author
 * @version SDNO 0.5 18-Mar-2016
 */
public class L3VpnSvcErrorCode {

    public static final String APP_NAME = "l3vpnsvc";

    public static final int INVALID_VPN_TYPE = 193501;

    public static final int EXISTED_VPN = 193502;

    public static final String VNI_BINDED = ErrorCodeUtils.getErrorCode(APP_NAME, "import_vpn", "vni_binded");

    public static final String VPN_ACTIVE = ErrorCodeUtils.getErrorCode(APP_NAME, "delete", "vpn_active");

    public static final String DEPLOY_CREATE_CONTROLLER_FAIL = ErrorCodeUtils.getErrorCode(APP_NAME, "create_vpn",
            "controller_return_fail");

    public static final String DEPLOY_UPDATEDEC_CONTROLLER_FAIL = ErrorCodeUtils.getErrorCode(APP_NAME, "update_vpn",
            "controller_return_fail");

    public static final String DEPLOY_GET_CONTROLLER_FAIL = ErrorCodeUtils.getErrorCode(APP_NAME, "get_vpn",
            "controller_return_fail");

    public static final String DEPLOY_GET_STATUS_CONTROLLER_FAIL = ErrorCodeUtils.getErrorCode(APP_NAME, "get_status",
            "controller_return_fail");

    public static final String DEPLOY_GET_TP_STATUS_CONTROLLER_FAIL = ErrorCodeUtils.getErrorCode(APP_NAME,
            "get_tp_status", "controller_return_fail");

    public static final String DEPLOY_DELETE_CONTROLLER_FAIL = ErrorCodeUtils.getErrorCode(APP_NAME, "delete_vpn",
            "controller_return_fail");

    public static final String DEPLOY_BIND_TP_CREATE_CONTROLLER_FAIL = ErrorCodeUtils.getErrorCode(APP_NAME,
            "bind_tp_create", "controller_return_fail");

    public static final String DEPLOY_BIND_TP_DELETE_CONTROLLER_FAIL = ErrorCodeUtils.getErrorCode(APP_NAME,
            "bind_tp_delete", "controller_return_fail");

    public static final String DEPLOY_TP_QOS_UPDATE_CONTROLLER_FAIL = ErrorCodeUtils.getErrorCode(APP_NAME,
            "tp_qos_update", "controller_return_fail");

    public static final String DEPLOY_TP_UPDATE_CONTROLLER_FAIL = ErrorCodeUtils.getErrorCode(APP_NAME, "tp_update",
            "controller_return_fail");

    public static final String DEPLOY_GET_TE_CONTROLLER_FAIL = ErrorCodeUtils.getErrorCode(APP_NAME, "get_te",
            "controller_return_fail");

    public static final String DEPLOY_DELETE_SITE_ONE_TP = ErrorCodeUtils.getErrorCode(APP_NAME, "delete_site",
            "one_TP");

    private L3VpnSvcErrorCode() {
    }
}
