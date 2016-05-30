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
 * Define normal L2VPN SBI constant.<br/>
 * 
 * @author
 * @version SDNO 0.5 2016-3-17
 */
public class L2vpnSbiConstant {

    public static final String CONTENT_TYPE_NAME = "Content-Type";

    public static final String CONTENT_TYPE_VALUE = "application/json;charset=UTF-8";

    public static final String MODULE_L2VPN = "l2vpn";

    public static final String SCENE_DEFULT = "";

    public static final String URL = "/rest/svc/sbiadp/controller/";

    private L2vpnSbiConstant() {
        /*
         * Default constructor. Make it private. Utility class constructor need
         * to be private
         */
    }

}
