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
import org.openo.sdno.cbb.sdnwan.util.accessor.VpnModelAccessor;
import org.openo.sdno.cbb.sdnwan.util.error.ServiceExceptionUtil;
import org.openo.sdno.cbb.sdnwan.util.translater.ModelTranslater;
import org.openo.sdno.cbb.sdnwan.util.translater.OperType;
import org.openo.sdno.cbb.sdnwan.util.translater.TranslaterCtx;
import org.openo.sdno.cbb.sdnwan.util.translater.TranslaterUtil;
import org.openo.sdno.svc.vpn.l3vpn.services.inf.L3vpnSbiService;
import org.openo.sdno.vpn.wan.networkmodel.servicel3vpn.L3vpn;
import org.openo.sdno.vpn.wan.networkmodel.servicetypes.L3Ac;
import org.openo.sdno.vpn.wan.servicemodel.tp.Tp;
import org.openo.sdno.vpn.wan.servicemodel.vpn.Vpn;

public class L3VpnModifyService {

    private L3vpnSbiService l3vpnSbiService;

    private L3VpnQueryService l3vpnQueryService;

    @Autowired
    private ModelTranslater<Vpn, L3vpn> l3vpnTranslater;

    @Autowired
    private VpnDao vpnDao;

    @Autowired
    private VpnBasicInfoDao vpnBasicInfoDao;

    @Autowired
    private TpDao tpDao;


    /**
     * Update description.
     * 
     * @since SDNO 0.5
     */
    public Vpn updateDescription(String uuid, String desc) throws ServiceException {
        final Vpn vpn = vpnDao.queryById(uuid);
        if(vpn == null) {
            throw new ServiceException("vpn not exist");
        }
        // Get controller UUID
        final String ctrlUuid = l3vpnQueryService.getCtrlUuid(vpn);
        l3vpnSbiService.updateL3vpnDesc(ctrlUuid, uuid, desc);
        vpnDao.updateDescription(uuid, desc);

        vpn.setDescription(desc);
        return vpn;
    }

    public void setL3vpnSbiService(L3vpnSbiService l3vpnSbiService) {
        this.l3vpnSbiService = l3vpnSbiService;
    }

    public void setL3vpnQueryService(L3VpnQueryService l3vpnQueryService) {
        this.l3vpnQueryService = l3vpnQueryService;
    }

}
