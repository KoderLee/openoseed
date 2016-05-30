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
package org.openo.sdno.cbb.sdnwan.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import org.openo.sdno.vpn.wan.db.tp.TpPo;
import org.openo.sdno.vpn.wan.db.vpn.VpnBasicInfoPo;
import org.openo.sdno.vpn.wan.servicemodel.relation.VpnTpRelation;
import org.openo.sdno.vpn.wan.servicemodel.topology.Topology;
import org.openo.sdno.vpn.wan.servicemodel.tp.Tp;
import org.openo.sdno.vpn.wan.servicemodel.vpn.PathInfo;
import org.openo.sdno.vpn.wan.servicemodel.vpn.VpnBasicInfo;

public class VpnBasicInfoDaoHelper {

    private VpnBasicInfoDaoHelper() {
    }

    /**
     * Prepare path information for vpn add.
     * 
     * @since SDNO 0.5
     */
    public static void preparePathInfo4Add(VpnBasicInfo vpnBasicInfo, List<PathInfo> pathInfos) {
        List<PathInfo> tmpPathList = DaoUtil.safeList(vpnBasicInfo.getPathList());
        pathInfos.addAll(tmpPathList);
        for(final PathInfo pathInfo : tmpPathList) {
            pathInfo.setValue4Po("vpnBasicInfoId", vpnBasicInfo.getUuid());
        }
    }

    /**
     * Get VPN terminate points.
     * 
     * @since SDNO 0.5
     */
    public static Map<String, List<Tp>> getVpnTpMap(final List<VpnTpRelation> vpnTpRelations, final List<TpPo> tpPos) {
        final Map<String, List<Tp>> vpnTpMap = new HashMap<>();
        for(final VpnTpRelation vpnTpRelation : vpnTpRelations) {

            for(final TpPo tpPo : tpPos) {
                if(tpPo.getUuid().equals(vpnTpRelation.getTpId())) {
                    List<Tp> tpPoList = vpnTpMap.get(vpnTpRelation.getVpnBasicInfoId());
                    if(null == tpPoList) {
                        tpPoList = new ArrayList<>();
                        vpnTpMap.put(vpnTpRelation.getVpnBasicInfoId(), tpPoList);
                    }
                    tpPoList.add(tpPo.toSvcModel());
                }
            }
        }
        return vpnTpMap;
    }

    /**
     * Get VPN terminate points.
     * 
     * @since SDNO 0.5
     */
    public static List<Tp> getTps(final List<VpnBasicInfo> vpnBasicInfos) {
        final List<Tp> tps = new ArrayList<>();

        for(final VpnBasicInfo vpnBasicInfo : vpnBasicInfos) {
            if(!CollectionUtils.isEmpty(vpnBasicInfo.getAccessPointList())) {
                tps.addAll(vpnBasicInfo.getAccessPointList());
            }
        }
        return tps;
    }

    /**
     * Get VPN basic informations
     * 
     * @since SDNO 0.5
     */
    public static List<VpnBasicInfoPo> getVpnBasicInfos(final List<VpnBasicInfo> vpnBasicInfos) {
        final List<VpnBasicInfoPo> vpnBasicInfoPos = new ArrayList<>();

        for(final VpnBasicInfo vpnBasicInfo : vpnBasicInfos) {
            final VpnBasicInfoPo vpnBasicInfoPo = new VpnBasicInfoPo();
            vpnBasicInfoPo.setUuid(vpnBasicInfo.getUuid());
            vpnBasicInfoPo.setAdminStatus(vpnBasicInfo.getAdminStatus());
            vpnBasicInfoPos.add(vpnBasicInfoPo);
        }
        return vpnBasicInfoPos;
    }
}
