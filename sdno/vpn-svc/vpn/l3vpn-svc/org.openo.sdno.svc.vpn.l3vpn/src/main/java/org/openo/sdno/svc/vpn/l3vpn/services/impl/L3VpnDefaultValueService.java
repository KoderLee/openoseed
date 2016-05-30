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
package org.openo.sdno.svc.vpn.l3vpn.services.impl;

import org.openo.sdno.vpn.wan.networkmodel.servicel3vpn.L3vpn;
import org.openo.sdno.vpn.wan.servicemodel.vpn.Vpn;

public class L3VpnDefaultValueService {

    /**
     * Set tunnel model.
     * 
     * @since SDNO 0.5
     */
    public void setTunnelModel(Vpn vpn, L3vpn l3vpn) {
        if(l3vpn.getTunnelService() != null) {
            l3vpn.getTunnelService().setTunnelMode("1to1");
        }

    }

}
