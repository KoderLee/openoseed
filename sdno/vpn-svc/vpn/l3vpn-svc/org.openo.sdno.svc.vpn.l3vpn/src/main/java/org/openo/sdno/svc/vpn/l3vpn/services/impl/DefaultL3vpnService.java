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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import org.openo.sdno.remoteservice.exception.ServiceException;
import org.openo.sdno.cbb.sdnwan.dao.TpDao;
import org.openo.sdno.cbb.sdnwan.dao.VpnBasicInfoDao;
import org.openo.sdno.cbb.sdnwan.dao.VpnDao;
import org.openo.sdno.cbb.sdnwan.util.accessor.VpnModelAccessor;
import org.openo.sdno.cbb.sdnwan.util.error.ServiceExceptionUtil;
import org.openo.sdno.cbb.sdnwan.util.translater.ModelTranslater;
import org.openo.sdno.cbb.sdnwan.util.translater.OperType;
import org.openo.sdno.cbb.sdnwan.util.translater.TranslaterCtx;
import org.openo.sdno.svc.vpn.l3vpn.services.inf.L3vpnSbiService;
import org.openo.sdno.svc.vpn.l3vpn.services.inf.L3vpnService;
import org.openo.sdno.vpn.wan.networkmodel.servicel3vpn.L3vpn;
import org.openo.sdno.vpn.wan.networkmodel.servicel3vpn.L3vpnContainer;
import org.openo.sdno.vpn.wan.networkmodel.servicetypes.L3Ac;
import org.openo.sdno.vpn.wan.servicemodel.common.AdminStatus;
import org.openo.sdno.vpn.wan.servicemodel.composedvpn.VpnAndCreatePolicy;
import org.openo.sdno.vpn.wan.servicemodel.policy.VpnCreatePolicy;
import org.openo.sdno.vpn.wan.servicemodel.tp.Tp;
import org.openo.sdno.vpn.wan.servicemodel.vpn.Vpn;
import com.puer.framework.container.util.UUIDUtils;

public class DefaultL3vpnService implements L3vpnService {

    private L3vpnSbiService l3vpnSbiService;

    private L3VpnQueryService l3vpnQueryService;

    private L3VpnModifyService l3vpnModifyService;

    private L3VpnDeleteService l3vpnDeleteService;

    private L3VpnMergePolicyService l3vpnMergePolicyService;

    private L3VpnDefaultValueService l3vpnDefaultValueService;

    @Autowired
    private ModelTranslater<Vpn, L3vpn> l3vpnTranslater;

    @Autowired
    private VpnDao vpnDao;

    @Autowired
    private VpnBasicInfoDao vpnBasicInfoDao;

    @Autowired
    private TpDao tpDao;

    public DefaultL3vpnService() {
        super();
    }

    /**
     * Bind site to VPN.
     * 
     * @since SDNO 0.5
     */
    @Override
    public Vpn bindSiteToVpn(final String l3vpnUuid, final String tpUuid, final VpnCreatePolicy vpnCreatePolicy)
            throws ServiceException {
        final Tp tempTp = tpDao.getMoById(tpUuid);
        final Vpn vpn = vpnDao.queryById(l3vpnUuid);
        if((null == tempTp) || (vpn == null)) {
            throw new ServiceException(ServiceException.DEFAULT_ID, ServiceExceptionUtil.BAD_REQUEST);
        }

        List<Tp> oldTpList = vpn.getVpnBasicInfo().getAccessPointList();

        if(null == oldTpList) {
            oldTpList = new ArrayList<Tp>();
            vpn.getVpnBasicInfo().setAccessPointList(oldTpList);
        }
        // If VPN is exist
        for(final Tp existTp : oldTpList) {
            if(existTp.getId().equals(tempTp.getId())) {
                return vpn;
            }
        }

        oldTpList.add(tempTp);

        // Get SNC id
        final String cltrUuid = l3vpnQueryService.getCtrlUuid(oldTpList);

        // Translate VPN to VPN network model
        final L3vpn newL3vpn = new L3vpn();
        final TranslaterCtx translaterCtx = new TranslaterCtx();
        translaterCtx.addVal("vpnCreatePolicy", vpnCreatePolicy);

        l3vpnTranslater.translate(translaterCtx, vpn, newL3vpn);
        L3Ac l3ac = null;
        final List<L3Ac> acs = newL3vpn.getAcs();
        for(final L3Ac ac : acs) {
            if(tpUuid.equals(ac.getId())) {
                l3ac = ac;
                break;
            }
        }

        final L3Ac l3acRet = l3vpnSbiService.createL3vpnBindTp(cltrUuid, vpn.getId(), l3ac);
        tempTp.setName(l3acRet.getName());

        // Store in database
        vpnBasicInfoDao.updateTps(vpn.getVpnBasicInfo());
        return vpn;
    }

    /**
     * Create VPN.
     * 
     * @since SDNO 0.5
     */
    @Override
    public Vpn createVpn(final VpnAndCreatePolicy vpnAndCreatePolicy) throws ServiceException {

        final Vpn vpn = vpnAndCreatePolicy.getVpn();
        final VpnCreatePolicy vpnCreatePolicy = vpnAndCreatePolicy.getVpnCreatePolicy();
        vpn.setId(UUIDUtils.createBase64Uuid());

        // merge segVpnPolicy and segmentVpn
        l3vpnMergePolicyService.mergeadminStatus(vpn, vpnCreatePolicy);
        l3vpnMergePolicyService.mergeScene(vpn, vpnCreatePolicy);

        // Get controller UUID
        final String ctrlUuid = l3vpnQueryService.getCtrlUuid(vpn);

        // Translate VPN to VPN network model
        final L3vpn l3vpn = new L3vpn();
        final L3vpnContainer l3vpnContainer = new L3vpnContainer();
        l3vpnContainer.setL3vpn(l3vpn);
        l3vpnContainer.getParamMap().put("nes", vpn.getVpnBasicInfo().getNes());
        final TranslaterCtx translaterCtx = new TranslaterCtx();
        translaterCtx.addVal("vpnCreatePolicy", vpnAndCreatePolicy.getVpnCreatePolicy());
        translaterCtx.setOperType(OperType.CREATE);
        translaterCtx.addVal("l3vpnContainer", l3vpnContainer);

        l3vpnTranslater.translate(translaterCtx, vpn, l3vpn);

        l3vpnDefaultValueService.setTunnelModel(vpn, l3vpn);

        final L3vpn retL3vpn = l3vpnSbiService.createL3vpn(ctrlUuid, l3vpnContainer);
        // Update VPN info according to the message returned by the adapter
        updateVpnInfo(vpn, retL3vpn);

        vpn.setControllerId(ctrlUuid);
        // Store in database
        vpnDao.add(vpn);
        return vpn;
    }

    /**
     * Unbind site from VPN.
     * 
     * @since SDNO 0.5
     */
    @Override
    public Vpn unbindSiteFromVpn(final String vpnUuid, final String tpUuid) throws ServiceException {
        return l3vpnDeleteService.unbindSiteFromVpn(vpnUuid, tpUuid);
    }

    /**
     * Delete VPN.
     * 
     * @since SDNO 0.5
     */
    @Override
    public Vpn deleteVpn(final String vpnUuid) throws ServiceException {
        return l3vpnDeleteService.deleteVpn(vpnUuid);
    }

    /**
     * Delete VPN.
     * 
     * @since SDNO 0.5
     */
    @Override
    public Vpn deleteVpn(String vpnId, VpnAndCreatePolicy vpnAndCreatePolicy) throws ServiceException {
        return l3vpnDeleteService.deleteVpn(vpnId, vpnAndCreatePolicy);
    }

    @Override
    public List<Vpn> getAllL3vpns() throws ServiceException {
        return l3vpnQueryService.getAllL3vpns();
    }

    @Override
    public Vpn getVpnById(final String uuid) throws ServiceException {
        return l3vpnQueryService.getVpnById(uuid);
    }

    @Override
    public List<Vpn> getVpnsByTpIds(final List<String> tpUuids) throws ServiceException {
        return l3vpnQueryService.getVpnsByTpIds(tpUuids);
    }

    @Override
    public String getVpnStatus(final String uuid) throws ServiceException {
        return l3vpnQueryService.getVpnStatus(uuid);

    }

    @Override
    public Vpn updateDescription(final String uuid, final String desc) throws ServiceException {
        return l3vpnModifyService.updateDescription(uuid, desc);
    }

    @Override
    public Vpn updateVpn(final Vpn vpn) throws ServiceException {
        return l3vpnModifyService.updateVpn(vpn);
    }

    public void setL3vpnSbiService(final L3vpnSbiService l3vpnSbiService) {
        this.l3vpnSbiService = l3vpnSbiService;
    }

    public void setL3VpnQueryService(final L3VpnQueryService l3vpnQueryService) {
        this.l3vpnQueryService = l3vpnQueryService;
    }

    public void setL3VpnModifyService(final L3VpnModifyService l3vpnModifyService) {
        this.l3vpnModifyService = l3vpnModifyService;
    }

    public void setL3VpnDeleteService(final L3VpnDeleteService l3vpnDeleteService) {
        this.l3vpnDeleteService = l3vpnDeleteService;
    }

    public void setL3VpnMergePolicyService(L3VpnMergePolicyService l3vpnMergePolicyService) {
        this.l3vpnMergePolicyService = l3vpnMergePolicyService;
    }

    public void setL3VpnDefaultValueService(L3VpnDefaultValueService l3vpnDefaultValueService) {
        this.l3vpnDefaultValueService = l3vpnDefaultValueService;
    }

    private Vpn updateAdminStatus(final String vpnUuid, final String adminStatus) throws ServiceException {
        return l3vpnModifyService.updateAdminStatus(vpnUuid, adminStatus);
    }

    private void updateVpnInfo(final Vpn vpn, final L3vpn l3vpn) {
        final List<Tp> tps = VpnModelAccessor.getAccessPointList(vpn);
        if(CollectionUtils.isEmpty(tps) || l3vpn == null) {
            return;
        }

        final List<L3Ac> acs = l3vpn.getAcs();
        if(CollectionUtils.isEmpty(acs)) {
            return;
        }

        final Map<String, L3Ac> acMap = new HashMap<String, L3Ac>();
        for(final L3Ac ac : acs) {
            acMap.put(ac.getId(), ac);
        }

        for(final Tp tp : tps) {
            final L3Ac ac = acMap.get(tp.getId());
            if(ac != null) {
                tp.setName(ac.getName());
            }
        }
    }

}
