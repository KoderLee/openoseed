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

import java.util.List;

import org.openo.sdno.vpn.wan.servicemodel.policy.VpnCreatePolicy;
import org.openo.sdno.vpn.wan.servicemodel.tp.Tp;
import org.openo.sdno.vpn.wan.servicemodel.vpn.Vpn;

public class L3VpnMergePolicyService {

    /**
     * Merge admin status.
     * 
     * @since SDNO 0.5
     */
    public void mergeadminStatus(Vpn vpn, VpnCreatePolicy vpnCreatePolicy) {
        if(null != vpn.getVpnBasicInfo() && vpnCreatePolicy.getBasicVpnInfo() != null) {
            vpn.getVpnBasicInfo().setAdminStatus(vpnCreatePolicy.getBasicVpnInfo().getAdminStatus());
            setTpAdminStatus(vpn);
        }
    }

    /**
     * Merge scene.
     * 
     * @since SDNO 0.5
     */
    public void mergeScene(final Vpn vpn, final VpnCreatePolicy vpnCreatePolicy) {
        if(vpn.getVpnBasicInfo() != null && vpnCreatePolicy.getBasicVpnInfo() != null) {
            vpn.getVpnBasicInfo().setCn3DciScene(vpnCreatePolicy.getBasicVpnInfo().isCn3DciScene());
        }
    }
}
