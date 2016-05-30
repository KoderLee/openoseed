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
package org.openo.sdno.cbb.sdnwan.util.accessor;

import java.util.List;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import org.openo.sdno.vpn.wan.servicemodel.tp.Tp;
import org.openo.sdno.vpn.wan.servicemodel.vpn.Vpn;
import org.openo.sdno.vpn.wan.servicemodel.vpn.VpnBasicInfo;

public final class VpnModelAccessor {

    private VpnModelAccessor() {
        // To hide the implicit public constructor
    }

    /**
     * Get the list of access points for a given VPN.<br/>
     * 
     * @param vpn whose access points are to be returned.
     * @return list of access points
     * @since SDNO 0.5
     */
    public static List<Tp> getAccessPointList(Vpn vpn) {
        VpnBasicInfo vpnBasicInfo = getVpnBasicInfo(vpn);

        if(vpnBasicInfo == null) {
            return null;
        }

        return vpnBasicInfo.getAccessPointList();
    }

    /**
     * Get the access point for a given VPN and TP UUID.<br/>
     * 
     * @param vpn VPN object
     * @param tpId TP UUID
     * @return access point
     * @since SDNO 0.5
     */
    public static Tp getAccessPoint(Vpn vpn, String tpId) {
        if(!StringUtils.hasText(tpId)) {
            return null;
        }

        List<Tp> tps = getAccessPointList(vpn);

        if(CollectionUtils.isEmpty(tps)) {
            return null;
        }

        for(Tp tp : tps) {
            if(tpId.equals(tp.getId())) {
                return tp;
            }
        }

        return null;
    }

    /**
     * Get the NE ID for a given VPN.<br/>
     * 
     * @param vpn VPN object
     * @return NE Id
     * @since SDNO 0.5
     */
    public static String getNeId(Vpn vpn) {
        if((vpn.getVpnBasicInfo() != null) && vpn.getVpnBasicInfo().isCn3DciScene()) {
            return getDciNeId(vpn);
        }

        List<Tp> tps = getAccessPointList(vpn);

        return TpModelAccessor.getNeId(tps);
    }

    private static String getDciNeId(Vpn vpn) {
        VpnBasicInfo vpnBasicInfo = getVpnBasicInfo(vpn);

        if((vpnBasicInfo != null) && !CollectionUtils.isEmpty(vpnBasicInfo.getNes())) {
            return vpnBasicInfo.getNes().get(0);
        }

        return null;
    }

    private static VpnBasicInfo getVpnBasicInfo(Vpn vpn) {
        if(vpn == null) {
            return null;
        }

        return vpn.getVpnBasicInfo();
    }
}
