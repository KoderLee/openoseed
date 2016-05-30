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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import org.openo.sdno.remoteservice.exception.ServiceException;
import org.openo.sdno.cbb.sdnwan.dao.VpnDao;
import org.openo.sdno.cbb.sdnwan.util.accessor.VpnModelAccessor;
import org.openo.sdno.cbb.sdnwan.util.rest.RestRsp;
import org.openo.sdno.svc.vpn.l2vpn.common.constant.ErrorCode;
import org.openo.sdno.svc.vpn.l2vpn.common.constant.L2vpnType;
import org.openo.sdno.svc.vpn.l2vpn.common.util.ServiceExceptionUtil;
import org.openo.sdno.svc.vpn.l2vpn.service.inf.AbstractL2vpnSbiService;
import org.openo.sdno.vpn.wan.networkmodel.serviceeline.L2vpn;
import org.openo.sdno.vpn.wan.networkmodel.serviceeline.L2vpnEline;
import org.openo.sdno.vpn.wan.networkmodel.servicetypes.Ac;
import org.openo.sdno.vpn.wan.servicemodel.composedvpn.VpnAndCreatePolicy;
import org.openo.sdno.vpn.wan.servicemodel.policy.VpnCreatePolicy;
import org.openo.sdno.vpn.wan.servicemodel.tp.Tp;
import org.openo.sdno.vpn.wan.servicemodel.vpn.Vpn;
import org.openo.sdno.vpn.wan.servicemodel.vpn.VpnBasicInfo;
import com.puer.framework.container.util.UUIDUtils;

/**
 * Create VPN templates.<br/>
 * 
 * @author
 * @version SDNO 0.5 2016-3-16
 */
@Service("TemplateCreate")
public class TemplateCreate {

    @Resource
    protected VpnDao vpnDao;

    @Resource
    protected CommonOper commonOper;

    public final RestRsp<Vpn> create(final VpnAndCreatePolicy vpnAndPolicy, final AbstractL2vpnSbiService sbiSvc)
            throws ServiceException {
        final Vpn vpn = vpnAndPolicy.getVpn();

        // Set ID and admin status.
        setVpnAndTpId(vpn);
        setAdminStatus(vpnAndPolicy);
        final L2vpnEline southVpn = sbiSvc.bussi2South4Create(vpnAndPolicy);
        final RestRsp<L2vpnEline> rsp = deploy(vpn, southVpn, sbiSvc);
        return ServiceExceptionUtil.convertRestRsp(rsp, vpn);
    }

    private final RestRsp<L2vpnEline> deploy(final Vpn bussinessVpn, final L2vpnEline southVpn,
            final AbstractL2vpnSbiService sbiSvc) throws ServiceException {
        final String ctrlUuid = commonOper.getCtrlUuidByVpn(bussinessVpn);
        bussinessVpn.setControllerId(ctrlUuid);
        RestRsp<L2vpnEline> rsp = new RestRsp<L2vpnEline>();
        if(L2vpnType.ELINE.getName().equals(commonOper.getL2vpnType(bussinessVpn))) {
            rsp = sbiSvc.createEline(ctrlUuid, southVpn);
        }

        if(rsp.getResult() == ErrorCode.RESULT_SUCCESS) {
            sbiSvc.depolyCommit(ctrlUuid);

            // In accordance with the norms of sdno: first put in storage,
            // and then issued a controller, in the actual scene L2VPN will
            // appear non ServiceException,this time will come into damaged
            // data, so temporarily under the first controller, if successful,
            // put in storage.
            L2vpn retl2vpn = rsp.getData();
            resetL2vpn(bussinessVpn, retl2vpn);
            vpnDao.add(bussinessVpn);
        } else {
            sbiSvc.depolyRollback(ctrlUuid);
        }
        return rsp;
    }

    private void resetL2vpn(final Vpn vpn, final L2vpn l2vpn) {
        final List<Tp> tps = VpnModelAccessor.getAccessPointList(vpn);
        if(CollectionUtils.isEmpty(tps)) {
            return;
        }
        final List<Ac> egressAcs = ((L2vpnEline)l2vpn).getEgressAcs();

        final List<Ac> ingressAcs = ((L2vpnEline)l2vpn).getIngressAcs();

        if(CollectionUtils.isEmpty(egressAcs) && CollectionUtils.isEmpty(ingressAcs)) {
            return;
        }

        final Map<String, Ac> acMap = new HashMap<String, Ac>();
        for(final Ac ac : egressAcs) {
            acMap.put(ac.getId(), ac);
        }

        for(final Ac ac : ingressAcs) {
            acMap.put(ac.getId(), ac);
        }

        for(final Tp tp : tps) {
            final Ac ac = acMap.get(tp.getId());
            if(ac != null) {
                tp.setName(ac.getName());
            }
        }
    }

    private void setVpnAndTpId(Vpn vpn) {
        if(vpn != null) {
            // Distribute UUID of VPN.
            vpn.setUuid(UUIDUtils.createBase64Uuid());
            VpnBasicInfo vb = vpn.getVpnBasicInfo();
            if(vb != null) {
                List<Tp> tps = vb.getAccessPointList();
                for(Tp tp : tps) {
                    tp.setParentTp(tp.getId());

                    // Distribute UUIDd of TP.
                    tp.setId(UUIDUtils.createBase64Uuid());
                }
            }
        }
    }

    public void setVpnDao(VpnDao vpnDao) {
        this.vpnDao = vpnDao;
    }

    public void setCommonOper(CommonOper commonOper) {
        this.commonOper = commonOper;
    }

    private void setAdminStatus(VpnAndCreatePolicy vpnAndPolicy) {
        Vpn vpn = vpnAndPolicy.getVpn();
        VpnCreatePolicy vpnCreatePolicy = vpnAndPolicy.getVpnCreatePolicy();
        if(null != vpn && null != vpn.getVpnBasicInfo() && null != vpnCreatePolicy
                && null != vpnCreatePolicy.getBasicVpnInfo()) {
            vpn.getVpnBasicInfo().setAdminStatus(vpnCreatePolicy.getBasicVpnInfo().getAdminStatus());
            setTpAdminStatus(vpn);
        }
    }

    private void setTpAdminStatus(Vpn vpn) {
        String adminStatus = vpn.getVpnBasicInfo().getAdminStatus();
        List<Tp> tpList = vpn.getVpnBasicInfo().getAccessPointList();
        if(!tpList.isEmpty()) {
            for(Tp tp : tpList) {
                tp.setAdminStatus(adminStatus);
            }
        }
    }
}
