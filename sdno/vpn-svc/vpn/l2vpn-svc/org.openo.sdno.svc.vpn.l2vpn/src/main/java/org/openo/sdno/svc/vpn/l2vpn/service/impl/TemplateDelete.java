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

import java.util.Collections;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import org.openo.sdno.remoteservice.exception.ServiceException;
import org.openo.sdno.cbb.sdnwan.dao.VpnDao;
import org.openo.sdno.cbb.sdnwan.util.rest.RestRsp;
import org.openo.sdno.svc.vpn.l2vpn.common.constant.ErrorCode;
import org.openo.sdno.svc.vpn.l2vpn.common.constant.L2vpnType;
import org.openo.sdno.svc.vpn.l2vpn.common.util.ServiceExceptionUtil;
import org.openo.sdno.svc.vpn.l2vpn.service.inf.AbstractL2vpnSbiService;
import org.openo.sdno.vpn.wan.networkmodel.serviceeline.L2vpnEline;
import org.openo.sdno.vpn.wan.servicemodel.vpn.Vpn;

/**
 * Delete VPN template.<br/>
 * 
 * @author
 * @version SDNO 0.5 2016-3-16
 */
@Service("TemplateDelete")
public class TemplateDelete {

    @Resource
    protected VpnDao vpnDao;

    @Resource
    protected CommonOper commonOper;

    public final RestRsp<Vpn> delete(final Vpn vpn, final AbstractL2vpnSbiService sbiSvc) throws ServiceException {
        if(vpn == null) {
            return ServiceExceptionUtil.getSuccesRestRsp(vpn);
        }
        final L2vpnEline southVpn = sbiSvc.bussi2South4Del(vpn);
        final RestRsp<L2vpnEline> rsp = deploy(vpn, southVpn, sbiSvc);
        return ServiceExceptionUtil.convertRestRsp(rsp, vpn);
    }

    private final RestRsp<L2vpnEline> deploy(final Vpn bussinessVpn, final L2vpnEline southVpn,
            final AbstractL2vpnSbiService sbiSvc) throws ServiceException {
        final String ctrlUuid = bussinessVpn.getControllerId();
        RestRsp<L2vpnEline> rsp = new RestRsp<L2vpnEline>();
        if(L2vpnType.ELINE.getName().equals(commonOper.getL2vpnType(bussinessVpn))) {
            rsp = sbiSvc.deleteEline(ctrlUuid, southVpn);
        }
        if(rsp != null) {
            if(rsp.getResult() == ErrorCode.RESULT_SUCCESS) {
                sbiSvc.depolyCommit(ctrlUuid);
                vpnDao.delMos(Collections.singletonList(bussinessVpn));
            } else {
                sbiSvc.depolyRollback(ctrlUuid);
            }
        }
        return rsp;
    }

    public void setVpnDao(final VpnDao vpnDao) {
        this.vpnDao = vpnDao;
    }

    public void setCommonOper(final CommonOper commonOper) {
        this.commonOper = commonOper;
    }
}
