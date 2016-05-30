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
 * Error codes range 194001-195000 for ComposedVpn.<br/>
 * 
 * @author
 * @version SDNO 0.5 18-Mar-2016
 */
public class ComposedVpnSvcErrorCode {

    public static final String APP_NAME = "composedvpnsvc";

    /**
     * VPN with the same name already exists and cannot be created again. 194001
     */
    public static final String CONFLICT_VPNNAME = ErrorCodeUtils.getErrorCode(APP_NAME, "createvpn",
            "conflict_vpn_name");

    /**
     * Interface has been occupied, please use the other interface. 194002
     */
    public static final String INTERFACE_OCCUPIED = ErrorCodeUtils.getErrorCode(APP_NAME, "createvpn",
            "interface_occupied");

    /**
     * VLAN id has been occupied, please use the other VLAN id. 194003
     */
    public static final String VLAN_OCCUPIED = ErrorCodeUtils.getErrorCode(APP_NAME, "createvpn", "vlan_occupied");

    /**
     * Resource not found in the other domain of the corresponding configuration, please check the
     * configuration 194004.
     */
    public static final String CROSS_DOMAIN_ERROR = ErrorCodeUtils.getErrorCode(APP_NAME, "createvpn",
            "cross_domain_error");

    /**
     * Inter domain link not found. 194005
     */
    public static final String NO_INTER_DOMAIN_LINK = ErrorCodeUtils.getErrorCode(APP_NAME, "calculatepath",
            "no_inter_domain_link");

    /**
     * The current interface does not exist. 194006
     */
    public static final String CURRENT_INTERFACE_NOT_EXIST = ErrorCodeUtils.getErrorCode(APP_NAME, "createvpn",
            "current_interface_not_exist");

    /**
     * Interface does not exist. 194007
     */
    public static final String INTERFACE_NOT_EXIST = ErrorCodeUtils.getErrorCode(APP_NAME, "createvpn",
            "interface_not_exist");

    /**
     * NE does not exist. 194008
     */
    public static final String NE_NOT_EXIST = ErrorCodeUtils.getErrorCode(APP_NAME, "createvpn", "ne_not_exist");

    /**
     * VPN does not exist 194009.
     */
    public static final String VPN_NOT_EXIST = ErrorCodeUtils.getErrorCode(APP_NAME, "createvpn", "vpn_not_exist");

    /**
     * Bandwidth is not enough. 194010
     */
    public static final String BANDWIDTH_NOT_ENOUGH = ErrorCodeUtils.getErrorCode(APP_NAME, "createvpn",
            "bandwidth_not_enough");

    /**
     * Update VPN info to database failed.
     */
    public static final String UPDATE_TO_DB_FAILE = ErrorCodeUtils.getErrorCode(APP_NAME, "updatevpn",
            "update_to_db_faile");

    /**
     * Force delete failed.
     */
    public static final String FORCE_DELETE_FAILED = ErrorCodeUtils.getErrorCode(APP_NAME, "forceDelete",
            "force_delete_failed");

    private ComposedVpnSvcErrorCode() {
    }
}
