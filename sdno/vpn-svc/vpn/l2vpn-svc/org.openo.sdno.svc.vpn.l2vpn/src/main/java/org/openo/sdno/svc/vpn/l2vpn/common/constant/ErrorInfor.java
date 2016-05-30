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
package org.openo.sdno.svc.vpn.l2vpn.common.constant;

/**
 * Define the value of associated error key.<br/>
 * 
 * @author
 * @version SDNO 0.5 2016-3-17
 */
public class ErrorInfor {

    public static final String VPNOBJECT_NULL = "L2VPN object is null.";

    public static final String VPNUUID_NULL = "L2VPN UUID is null.";

    public static final String VPNTP_NULL = "L2VPN TP is null.";

    public static final String VPNTYPE_UNKNOWN = "L2VPN type is unknow.";

    public static final String NORELATIVE_CONTROLLER = "There is no relative controller.";

    public static final String VPNSTATUS_UNKNOWN = "L2VPN status is unknow.";

    public static final String VPNTPSTATUS_UNKNOWN = "L2VPN TP status is unknow.";

    public static final String VPNPOLICYINFO_INVALID = "VPN policy information is invalid.";

    public static final String LESS_VPNBWINFO = "It is less of VPN bandwith information.";

    public static final String LESS_TPBWINFO = "It is less of TP bandwith information.";

    private ErrorInfor() {
        /*
         * Default constructor. Make it private. Utility class constructor need
         * to be private
         */
    }
}
