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
package org.openo.sdno.svc.vpn.l2vpn.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import org.openo.sdno.vpn.wan.networkmodel.commontypes.AdminStatus;
import org.openo.sdno.vpn.wan.networkmodel.commontypes.OperStatus;
import org.openo.sdno.vpn.wan.networkmodel.serviceeline.L2vpnEline;
import org.openo.sdno.vpn.wan.networkmodel.servicetypes.Ac;
import org.openo.sdno.vpn.wan.networkmodel.servicetypes.AccessType;
import org.openo.sdno.vpn.wan.networkmodel.servicetypes.L2Access;
import org.openo.sdno.vpn.wan.networkmodel.servicetypes.SignalingType;
import org.openo.sdno.vpn.wan.networkmodel.servicetypes.TunnelMode;
import org.openo.sdno.vpn.wan.networkmodel.servicetypes.TunnelService;
import org.openo.sdno.vpn.wan.servicemodel.common.EncapType;
import org.openo.sdno.vpn.wan.servicemodel.common.ReversionMode;
import org.openo.sdno.vpn.wan.servicemodel.common.TunnelTechType;
import org.openo.sdno.vpn.wan.servicemodel.composedvpn.VpnAndCreatePolicy;
import org.openo.sdno.vpn.wan.servicemodel.policy.RoutingConstrains;
import org.openo.sdno.vpn.wan.servicemodel.policy.TunnelTechBrief;
import org.openo.sdno.vpn.wan.servicemodel.policy.VpnCreatePolicy;
import org.openo.sdno.vpn.wan.servicemodel.tp.EthernetTpSpec;
import org.openo.sdno.vpn.wan.servicemodel.tp.Tp;
import org.openo.sdno.vpn.wan.servicemodel.tp.TpTypeSpec;
import org.openo.sdno.vpn.wan.servicemodel.vpn.Vpn;

/**
 * Conversion business model to South model.<br/>
 * 
 * @author
 * @version SDNO 0.5 2016-3-16
 */
public class Bussi2SouthConvert {

    private Bussi2SouthConvert() {
    }

    /**
     * Service model to south interface model for creation.
     * 
     * @since SDNO 0.5
     */
    public static L2vpnEline bussi2South4Create(final VpnAndCreatePolicy vpnAndCreatePolicy,
            final Map<String, Integer> mapTpPir2CirRatio) {
        final L2vpnEline eline = new L2vpnEline();
        if((vpnAndCreatePolicy != null) && (vpnAndCreatePolicy.getVpn() != null)) {
            final Vpn vpn = vpnAndCreatePolicy.getVpn();

            setElineGlobal(eline, vpn);

            buildElineAcs(eline, vpn, vpnAndCreatePolicy.getVpnCreatePolicy(), mapTpPir2CirRatio);

            buildElineTunnelService(eline, vpn, vpnAndCreatePolicy.getVpnCreatePolicy());

        }
        return eline;
    }

    private static void setElineGlobal(final L2vpnEline eline, final Vpn vpn) {
        eline.setId(vpn.getId());
        eline.setName(vpn.getName());
        eline.setUserLabel(vpn.getDescription());
        eline.setParentNcdId("");
        eline.setNetworkType("simple");
        eline.setOperateStatus(OperStatus.OPERATE_UP.getName());
        final String svcMoAdminUp = org.openo.sdno.vpn.wan.servicemodel.common.AdminStatus.ACTIVE.getCommonName();
        eline.setAdminStatus(svcMoAdminUp.equals(vpn.getVpnBasicInfo().getAdminStatus()) ? AdminStatus.ADMIN_UP
                .getName() : AdminStatus.ADMIN_DOWN.getName());
    }

    private static void buildElineAcs(final L2vpnEline eline, final Vpn vpn, final VpnCreatePolicy vpnCreatePolicy,
            final Map<String, Integer> mapTpPir2CirRatio) {
        final List<Ac> ingressAcs = new ArrayList<Ac>();
        final List<Ac> egressAcs = new ArrayList<Ac>();
        eline.setIngressAcs(ingressAcs);
        eline.setEgressAcs(egressAcs);
        final Tp tp1 = vpn.getVpnBasicInfo().getAccessPointList().get(0);
        final Tp tp2 = vpn.getVpnBasicInfo().getAccessPointList().get(1);
        if(tp1.getId().compareTo(tp2.getId()) > 0) {
            ingressAcs.add(tp2Ac(vpn, tp2, vpnCreatePolicy, mapTpPir2CirRatio));
            egressAcs.add(tp2Ac(vpn, tp1, vpnCreatePolicy, mapTpPir2CirRatio));
        } else {
            ingressAcs.add(tp2Ac(vpn, tp1, vpnCreatePolicy, mapTpPir2CirRatio));
            egressAcs.add(tp2Ac(vpn, tp2, vpnCreatePolicy, mapTpPir2CirRatio));
        }
    }

    private static Ac tp2Ac(final Vpn vpn, final Tp tp, final VpnCreatePolicy polcicy,
            final Map<String, Integer> mapTpPir2CirRatio) {
        final Ac ac = new Ac();

        // Set global information for AC.
        setAcGlobal(tp, ac, vpn);

        // Set VLAN processing information of two layer AC.
        buildAcAccess(tp, ac);
        return ac;
    }

    private static void setAcGlobal(final Tp tp, final Ac ac, final Vpn vpn) {
        ac.setId(tp.getId());
        ac.setName(tp.getName());
        ac.setNeId(tp.getNeId());
        ac.setLtpId(tp.getParentTp());
        ac.setRole(Role.MASTER.getName());
        ac.setOperateStatus(OperStatus.OPERATE_UP.getName());
        final String svcMoAdminUp = org.openo.sdno.vpn.wan.servicemodel.common.AdminStatus.ACTIVE.getCommonName();
        String vpnStatus = "";
        if(vpn.getVpnBasicInfo() != null) {
            vpnStatus = vpn.getVpnBasicInfo().getAdminStatus();
        } else {
            vpnStatus = tp.getAdminStatus();
        }
        ac.setAdminStatus(svcMoAdminUp.equals(vpnStatus) ? AdminStatus.ADMIN_UP.getName() : AdminStatus.ADMIN_DOWN
                .getName());
        ac.setOperateStatus("operate-up");
    }

    private static void buildAcAccess(final Tp tp, final Ac ac) {
        final L2Access l2Access = new L2Access();
        ac.setL2Access(l2Access);
        final List<TpTypeSpec> specList = tp.getTypeSpecList();
        EthernetTpSpec ethSpec = null;
        for(final TpTypeSpec spec : specList) {
            if(spec.getEthernetTpSpec() != null) {
                ethSpec = spec.getEthernetTpSpec();
                break;
            }
        }
        setAcAccessInfo(l2Access, ethSpec);
    }

    private static void setAcAccessInfo(final L2Access l2Access, final EthernetTpSpec ethSpec) {
        l2Access.setAccessType(AccessType.PORT.getName());
        l2Access.setDot1qVlanBitmap("");
        if((ethSpec != null) && StringUtils.isNotEmpty(ethSpec.getSvlanList())
                && EncapType.DOT1Q.getCommonName().equals(ethSpec.getEncapType())) {
            l2Access.setAccessType(AccessType.DOT1Q.getName());
            l2Access.setDot1qVlanBitmap(ethSpec.getSvlanList());
        } else {
            l2Access.setAccessType(AccessType.PORT.getName());
        }
        l2Access.setQinqSvlanBitmap("");
        l2Access.setQinqCvlanBitmap("");
        l2Access.setAccessAction("");
        l2Access.setActionVlanId(0);
    }
    
    private static void buildElineTunnelService(final L2vpnEline eline, final Vpn vpn,
            final VpnCreatePolicy vpnCreatePolicy) {
        final TunnelService tunnelService = new TunnelService();

        // Default tunneling policy
        tunnelService.setSignalingType("AUTO-SELECT");
        tunnelService.setTunnelMode(TunnelMode.N2ONE.getName());
        tunnelService.setLatency(5);

        // Set tunnel type
        setTunnelTypePolicy(vpnCreatePolicy, tunnelService);

        eline.setTunnelService(tunnelService);
    }

    private static void setTunnelTypePolicy(final VpnCreatePolicy vpnCreatePolicy, final TunnelService tunnelService) {
        if(vpnCreatePolicy != null) {
            final List<RoutingConstrains> routingConstrains = vpnCreatePolicy.getRoutingConstrains();
            RoutingConstrains cns = null;
            if((routingConstrains != null) && !routingConstrains.isEmpty()) {
                cns = routingConstrains.get(0);
            }
            if((cns != null) && (cns.getTunnelTechBrief() != null)) {
                final TunnelTechBrief tunnelTec = cns.getTunnelTechBrief();
                if(TunnelTechType.RSVP_TE.getCommonName().equals(tunnelTec.getTunnelTech())) {
                    tunnelService.setSignalingType(SignalingType.RSVPTE.getName());
                }
                if(TunnelTechType.LDP.getCommonName().equalsIgnoreCase(tunnelTec.getTunnelTech())) {
                    tunnelService.setSignalingType(SignalingType.LDP.getName());
                }
                if(TunnelTechType.SR_TE.getCommonName().equalsIgnoreCase(tunnelTec.getTunnelTech())) {
                    tunnelService.setSignalingType(SignalingType.SRTE.getName());
                }

                if(tunnelTec.getDelay() != null) {
                    tunnelService.setLatency(Integer.parseInt(tunnelTec.getDelay()));
                }
            }
        }
    }

    /**
     * Service model to south interface model for deletion.
     * 
     * @since SDNO 0.5
     */
    public static L2vpnEline bussi2South4Del(final Vpn vpn) {
        final L2vpnEline eline = new L2vpnEline();
        setElineGlobal(eline, vpn);
        return eline;
    }
}
