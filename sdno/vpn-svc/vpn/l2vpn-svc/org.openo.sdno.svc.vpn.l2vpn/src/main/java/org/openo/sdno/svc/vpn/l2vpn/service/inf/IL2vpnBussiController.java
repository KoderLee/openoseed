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

import java.util.List;

import org.openo.sdno.remoteservice.exception.ServiceException;
import org.openo.sdno.roa.common.HttpContext;
import org.openo.sdno.cbb.sdnwan.util.rest.RestRsp;
import org.openo.sdno.vpn.wan.servicemodel.composedvpn.VpnAndCreatePolicy;
import org.openo.sdno.vpn.wan.servicemodel.tp.Tp;
import org.openo.sdno.vpn.wan.servicemodel.vpn.Vpn;
import com.puer.framework.base.service.IService;

/**
 * L2VPN bussiController interface.<br/>
 * 
 * @author
 * @version SDNO 0.5 2016-3-16
 */
public interface IL2vpnBussiController extends IService {

    /**
     * Create VPN.<br/>
     */
    RestRsp<Vpn> createVpn(VpnAndCreatePolicy vpnAndPolicy, HttpContext httpContext) throws ServiceException;

    /**
     * Delete VPN by UUID.<br>
     */
    RestRsp<Vpn> deleteVpn(String vpnUuid, HttpContext httpContext) throws ServiceException;

    /**
     * Batch query VPN.<br/>
     */
    RestRsp<List<Vpn>> getVpns(HttpContext httpContext) throws ServiceException;

    /**
     * Query VPNs by UUID.<br/>
     */
    RestRsp<List<Vpn>> getVpnsByUuid(String uuid, HttpContext httpContext) throws ServiceException;

    /**
     * Batch query VPN through the list of TP's UUID.<br/>
     */
    RestRsp<List<Vpn>> getVpnsByTp(List<String> tpUuidList, HttpContext httpContext) throws ServiceException;
    
    /**
     * Modify L2VPN's description.<br/>
     */
    RestRsp<Vpn> modifyDescription(String uuid, String desc, HttpContext httpContext) throws ServiceException;

    /**
     * Get status by UUID.<br/>
     */
    RestRsp<String> getStatusByUuid(String uuid, HttpContext httpContext) throws ServiceException;
}
