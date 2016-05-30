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
package org.openo.sdno.svc.vpn.l2vpn.service.inf;

import org.openo.sdno.remoteservice.exception.ServiceException;
import org.openo.sdno.cbb.sdnwan.util.rest.RestRsp;
import org.openo.sdno.vpn.wan.networkmodel.serviceeline.L2vpnEline;
import org.openo.sdno.vpn.wan.networkmodel.servicetypes.Ac;

/**
 * L2VPN southbound interface.<br/>
 * 
 * @author
 * @version SDNO 0.5 2016-3-16
 */
public interface L2vpnSbiService {

    /**
     * Create L2vpnEline.<br/>
     */
    RestRsp<L2vpnEline> createEline(String cltUuid, L2vpnEline l2vpn) throws ServiceException;

    /**
     * Delete L2vpnEline.<br/>
     */
    RestRsp<L2vpnEline> deleteEline(String cltUuid, L2vpnEline l2vpn) throws ServiceException;

    /**
     * Depoly commit.<br/>
     */
    RestRsp<String> depolyCommit(String ctrlUuid);

    /**
     * Depoly rollback.<br/>
     */
    RestRsp<String> depolyRollback(String ctrlUuid);

    /**
     * Modify L2vpnEline.<br/>
     */
    RestRsp<L2vpnEline> updateL2Vpn(String ctrlUuid, L2vpnEline southL2vpn) throws ServiceException;
}
