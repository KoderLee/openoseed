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

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import org.openo.sdno.remoteservice.exception.ServiceException;
import org.openo.sdno.cbb.sdnwan.dao.TpDao;
import org.openo.sdno.cbb.sdnwan.dao.VpnBasicInfoDao;
import org.openo.sdno.cbb.sdnwan.dao.VpnDao;
import org.openo.sdno.cbb.sdnwan.util.error.ServiceExceptionUtil;
import org.openo.sdno.cbb.sdnwan.util.error.code.L3VpnSvcErrorCode;
import org.openo.sdno.cbb.sdnwan.util.translater.TranslaterUtil;
import org.openo.sdno.svc.vpn.l3vpn.common.ErrorCode;
import org.openo.sdno.svc.vpn.l3vpn.services.inf.L3vpnSbiService;
import org.openo.sdno.vpn.wan.networkmodel.servicel3vpn.L3vpn;
import org.openo.sdno.vpn.wan.servicemodel.common.AdminStatus;
import org.openo.sdno.vpn.wan.servicemodel.composedvpn.VpnAndCreatePolicy;
import org.openo.sdno.vpn.wan.servicemodel.tp.Tp;
import org.openo.sdno.vpn.wan.servicemodel.vpn.Vpn;

public class L3VpnDeleteService {

    private L3vpnSbiService l3vpnSbiService;

    private L3VpnQueryService l3vpnQueryService;

    @Autowired
    private VpnDao vpnDao;

    @Autowired
    private VpnBasicInfoDao vpnBasicInfoDao;

    @Autowired
    private TpDao tpDao;

    /**
     * Unbind site from VPN.
     * 
     * @since SDNO 0.5
     */
    public Vpn unbindSiteFromVpn(String vpnUuid, String tpUuid) throws ServiceException {
        final Vpn tempVpn = vpnDao.getMoById(vpnUuid);
        final Tp tempTp = tpDao.getMoById(tpUuid);
        if(null == tempVpn || null == tempTp) {
            throw new ServiceException(ServiceException.DEFAULT_ID, ServiceExceptionUtil.BAD_REQUEST);
        }
        // VPN has only one TP and the id equals TP's id
        final List<Tp> tpLst = tempVpn.getVpnBasicInfo().getAccessPointList();

        if((1 == tpLst.size()) && (tpLst.get(0).getId().equals(tpUuid))) {
            throw new ServiceException(L3VpnSvcErrorCode.DEPLOY_DELETE_SITE_ONE_TP, "");
        }

        final List<Tp> oldTpList = tempVpn.getVpnBasicInfo().getAccessPointList();
        if(CollectionUtils.isEmpty(oldTpList)) {
            return tempVpn;
        }
        // Get controller UUID
        final String ctrlUuid = l3vpnQueryService.getCtrlUuid(oldTpList);
        int index = -1;
        for(int i = 0; i < oldTpList.size(); i++) {
            if(oldTpList.get(i).getId().equals(tempTp.getId())) {
                index = i;
            }
        }
        if(index == -1) {
            return tempVpn;
        }
        oldTpList.remove(index);

        l3vpnSbiService.deleteL3vpnBindTp(ctrlUuid, vpnUuid, tpUuid);
        // Update database, delete TP from VPN
        tempVpn.getVpnBasicInfo().setAccessPointList(oldTpList);
        vpnBasicInfoDao.updateTps(tempVpn.getVpnBasicInfo());

        return tempVpn;
    }

    /**
     * Delete VPN.
     * 
     * @since SDNO 0.5
     */
    public Vpn deleteVpn(String vpnUuid) throws ServiceException {
        final Vpn vpn = vpnDao.queryById(vpnUuid);
        if(null == vpn) {
            throw new ServiceException("vpn not exist");
        }

        // Query VPN info from SNC
        L3vpn l3vpn = l3vpnQueryService.getVpnfromSnc(vpn);
        if(l3vpn != null
                && AdminStatus.ACTIVE.getCommonName().equals(TranslaterUtil.nTosAdminStatu(l3vpn.getAdminStatus()))) {
            throw ServiceExceptionUtil.getServiceException(ErrorCode.INVALID_ADMINSTATUS_FOR_VPN_DEL);
        }
        // Get controller UUID
        final String ctrlUuid = l3vpnQueryService.getCtrlUuid(vpn);

        l3vpnSbiService.deleteL3vpn(ctrlUuid, vpn.getId());

        // Store in database
        vpnDao.delMos(Collections.singletonList(vpn));
        return vpn;
    }
    
    /**
     * Delete VPN.
     * 
     * @since SDNO 0.5
     */
    public Vpn deleteVpn(String vpnUuid, VpnAndCreatePolicy vpnAndCreatePolicy) throws ServiceException {
        final Vpn vpn = vpnDao.queryById(vpnUuid);
        if(null == vpn) {
            throw new ServiceException("vpn not exist");
        }

        // Query VPN info from SNC
        L3vpn l3vpn = l3vpnQueryService.getVpnfromSnc(vpn);
        if(l3vpn != null
                && AdminStatus.ACTIVE.getCommonName().equals(TranslaterUtil.nTosAdminStatu(l3vpn.getAdminStatus()))) {
            throw ServiceExceptionUtil.getServiceException(ErrorCode.INVALID_ADMINSTATUS_FOR_VPN_DEL);
        }

        final String ctrlUuid = l3vpnQueryService.getCtrlUuid(vpn);
        l3vpnSbiService.deleteL3vpn(ctrlUuid, vpn.getId());

        // delete in database
        vpnDao.delMos(Collections.singletonList(vpn));
        return vpn;
    }

    /**
     * Set layer 3 VPN service.
     * 
     * @since SDNO 0.5
     */
    public void setL3vpnSbiService(L3vpnSbiService l3vpnSbiService) {
        this.l3vpnSbiService = l3vpnSbiService;
    }

    public void setL3vpnQueryService(L3VpnQueryService l3vpnQueryService) {
        this.l3vpnQueryService = l3vpnQueryService;
    }
}
