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
package org.openo.sdno.svc.vpn.l2vpn.service.inf;

import org.openo.sdno.vpn.wan.networkmodel.serviceeline.L2vpnEline;
import org.openo.sdno.vpn.wan.networkmodel.servicetypes.Ac;
import org.openo.sdno.vpn.wan.servicemodel.composedvpn.VpnAndCreatePolicy;
import org.openo.sdno.vpn.wan.servicemodel.tp.Tp;
import org.openo.sdno.vpn.wan.servicemodel.vpn.Vpn;

/**
 * The abstract class of L2VPN southbound Service.<br/>
 * 
 * @author
 * @version SDNO 0.5 2016-3-16
 */
public interface AbstractL2vpnSbiService extends L2vpnSbiService {

    /**
     * The business model is converted into the south model and used to create
     * the L2VPN.<br>
     */
    L2vpnEline bussi2South4Create(VpnAndCreatePolicy vpnAndCreatePolicy);

    /**
     * The business model is converted into the south model and used to delete
     * the L2VPN.<br>
     */
    L2vpnEline bussi2South4Del(Vpn vpn);

    /**
     * The business model is converted into the south model and used to get the
     * L2VPN status.<br>
     */
    L2vpnEline bussi2South4Status(Vpn vpn);

    /**
     * The business model is converted into the south AC model and used to get
     * the TP status.<br>
     */
    Ac bussi2SouthAc4TpStatus(Tp tp);

    /**
     * Get content type.<br>
     */
    String getCtrlContentType();

    /**
     * The return value of default scene.<br>
     */
    String getScene();

}
