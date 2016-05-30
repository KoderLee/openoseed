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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import org.openo.sdno.remoteservice.exception.ServiceException;
import org.openo.sdno.cbb.sdnwan.dao.NetworkElementDao;
import org.openo.sdno.cbb.sdnwan.dao.VpnDao;
import org.openo.sdno.cbb.sdnwan.util.EnumUtil;
import org.openo.sdno.cbb.sdnwan.util.UuidUtil;
import org.openo.sdno.cbb.sdnwan.util.accessor.TpModelAccessor;
import org.openo.sdno.cbb.sdnwan.util.accessor.VpnModelAccessor;
import org.openo.sdno.cbb.sdnwan.util.error.ServiceExceptionUtil;
import org.openo.sdno.cbb.sdnwan.util.translater.TranslaterUtil;
import org.openo.sdno.inventory.sdk.inf.IControllerService;
import org.openo.sdno.inventory.sdk.model.ControllerMO;
import org.openo.sdno.result.Result;
import org.openo.sdno.svc.vpn.l3vpn.services.inf.L3vpnSbiService;
import org.openo.sdno.vpn.wan.networkmodel.servicel3vpn.L3vpn;
import org.openo.sdno.vpn.wan.networkmodel.servicetypes.Ac;
import org.openo.sdno.vpn.wan.networkmodel.servicetypes.L3Ac;
import org.openo.sdno.vpn.wan.servicemodel.common.ServiceType;
import org.openo.sdno.vpn.wan.servicemodel.tp.Tp;
import org.openo.sdno.vpn.wan.servicemodel.vpn.Vpn;

public class L3VpnQueryService {

    @Autowired
    IControllerService controllerService;

    private L3vpnSbiService l3vpnSbiService;

    @Autowired
    private VpnDao vpnDao;

    @Autowired
    private NetworkElementDao networkElementDao;

    /**
     * Get layer 3 VPNs.
     * 
     * @since SDNO 0.5
     */
    public List<Vpn> getAllL3vpns() throws ServiceException {
        final List<Vpn> vpns = vpnDao.getAllMo();
        if(CollectionUtils.isEmpty(vpns)) {
            return vpns;
        }
        final List<Vpn> l3vpns = new ArrayList<Vpn>();
        for(final Vpn vpn : vpns) {
            final ServiceType serviceType = EnumUtil.valueOf(ServiceType.class, vpn.getVpnBasicInfo().getServiceType());
            if(serviceType == ServiceType.L3VPN) {
                l3vpns.add(vpn);
            }
        }
        return l3vpns;
    }

    /**
     * Get control uuid.
     * 
     * @since SDNO 0.5
     */
    public String getCtrlUuid(final List<Tp> tps) throws ServiceException {
        final String neUuid = TpModelAccessor.getNeId(tps);
        if(!StringUtils.hasText(neUuid)) {
            throw new ServiceException("neuuid is empty");
        }
        // Get controller UUID
        final String ctrlUuid = networkElementDao.getCtrlUuidFromNe(neUuid);
        if(!StringUtils.hasText(ctrlUuid)) {
            throw new ServiceException("cant not find Controller");
        }
        return ctrlUuid;
    }

    /**
     * Get control uuid.
     * 
     * @since SDNO 0.5
     */
    public String getCtrlUuid(final Vpn vpn) throws ServiceException {
        String ctrlUuid = vpn.getControllerId();
        if(StringUtils.hasText(ctrlUuid)) {
            return ctrlUuid;
        }

        final String neUuid = VpnModelAccessor.getNeId(vpn);
        if(!StringUtils.hasText(neUuid)) {
            throw new ServiceException("neuuid is empty");
        }

        // Get controller UUID
        ctrlUuid = networkElementDao.getCtrlUuidFromNe(neUuid);
        if(!StringUtils.hasText(ctrlUuid)) {
            throw new ServiceException("cant not find Controller");
        }
        return ctrlUuid;
    }

    /**
     * Get control type.
     * 
     * @since SDNO 0.5
     */
    public String getCtrlType(final String ctrlUuid) throws ServiceException {
        Result<ControllerMO> ctrlMoRs = controllerService.getController(ctrlUuid);
        if(!ctrlMoRs.isValid()) {
            throw new ServiceException("cant not find Controller type.");
        }
        return ctrlMoRs.getResultObj().getType();
    }

    public Vpn getVpnById(final String uuid) throws ServiceException {
        return vpnDao.queryById(uuid);
    }

    public List<Vpn> getVpnsByTpIds(final List<String> tpUuids) throws ServiceException {
        if(!UuidUtil.validate(tpUuids)) {
            ServiceExceptionUtil.throwBadRequestException();
        }
        return vpnDao.getVpnsByTpIds(tpUuids);
    }

    /**
     * Get VPN status.
     * 
     * @since SDNO 0.5
     */
    public String getVpnStatus(final String uuid) throws ServiceException {
        final List<Vpn> vpns = vpnDao.getVpnAbstractInfo(Collections.singletonList(uuid));
        if(CollectionUtils.isEmpty(vpns)) {
            throw new ServiceException("vpn not exist");
        }
        Vpn vpn = vpns.get(0);
        // Get controller UUID
        final String ctrlUuid = getCtrlUuid(vpn);
        final L3vpn l3vpn = l3vpnSbiService.getOperStatus(ctrlUuid, uuid);
        String status = "";
        if(null != l3vpn) {
            final String operStatus = TranslaterUtil.nTosOperStatu(l3vpn.getOperateStatus());
            final String adminStatus = TranslaterUtil.nTosAdminStatu(l3vpn.getAdminStatus());

            // Update TP status
            vpn = updateTpStatus(vpn, l3vpn);
            // refresh VPN and TP status
            vpn.setOperStatus(operStatus);
            vpn.getVpnBasicInfo().setAdminStatus(adminStatus);
            vpnDao.updateStatus(Collections.singletonList(vpn));

            status = operStatus + "," + adminStatus;
        }
        return status;
    }

    public L3vpn getVpnfromSnc(final Vpn vpn) throws ServiceException {
        // Get controller UUID
        final String ctrlUuid = getCtrlUuid(vpn);
        return l3vpnSbiService.getOperStatus(ctrlUuid, vpn.getId());
    }

    public void setL3vpnSbiService(final L3vpnSbiService l3vpnSbiService) {
        this.l3vpnSbiService = l3vpnSbiService;
    }

    private Vpn updateTpStatus(final Vpn vpn, final L3vpn l3vpn) {
        final List<Tp> tps = vpn.getVpnBasicInfo().getAccessPointList();
        final List<L3Ac> inAcs = l3vpn.getAcs();
        final HashMap<String, Ac> acMap = new HashMap<String, Ac>();

        for(final Ac inAc : inAcs) {
            acMap.put(inAc.getId(), inAc);
        }

        for(final Tp tp : tps) {
            final String tpId = tp.getId();
            if(acMap.containsKey(tpId)) {
                final Ac acInfo = acMap.get(tpId);
                tp.setOperStatus(TranslaterUtil.nTosOperStatu(acInfo.getOperateStatus()));
                tp.setAdminStatus(TranslaterUtil.nTosAdminStatu(acInfo.getAdminStatus()));
                tp.setName(acInfo.getName());
            }

        }

        vpn.getVpnBasicInfo().setAccessPointList(tps);

        return vpn;
    }

}
