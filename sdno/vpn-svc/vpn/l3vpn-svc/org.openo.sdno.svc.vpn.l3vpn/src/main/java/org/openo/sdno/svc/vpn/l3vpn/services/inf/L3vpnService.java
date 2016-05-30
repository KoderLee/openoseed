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
package org.openo.sdno.svc.vpn.l3vpn.services.inf;

import java.util.List;

import org.openo.sdno.remoteservice.exception.ServiceException;
import org.openo.sdno.vpn.wan.servicemodel.composedvpn.VpnAndCreatePolicy;
import org.openo.sdno.vpn.wan.servicemodel.policy.VpnCreatePolicy;
import org.openo.sdno.vpn.wan.servicemodel.tp.Tp;
import org.openo.sdno.vpn.wan.servicemodel.vpn.Vpn;
import com.puer.framework.base.service.IService;

public interface L3vpnService extends IService {

    /**
     * Get layer 3 VPNs.
     * 
     * @since SDNO 0.5
     */
    List<Vpn> getAllL3vpns() throws ServiceException;

    /**
     * Get VPN by id.
     * 
     * @since SDNO 0.5
     */
    Vpn getVpnById(String uuid) throws ServiceException;

    /**
     * Get VPN by terminate point id.
     * 
     * @since SDNO 0.5
     */
    List<Vpn> getVpnsByTpIds(List<String> tpUuids) throws ServiceException;

    /**
     * Create VPN.
     * 
     * @since SDNO 0.5
     */
    Vpn createVpn(VpnAndCreatePolicy vpnAndCreatePolicy) throws ServiceException;

    /**
     * Delete VPN.
     * 
     * @since SDNO 0.5
     */
    Vpn deleteVpn(String vpnuuid) throws ServiceException;

    /**
     * Delete VPN.
     * 
     * @since SDNO 0.5
     */
    Vpn deleteVpn(String vpnuuid, VpnAndCreatePolicy vpnAndCreatePolicy) throws ServiceException;

    /**
     * Bind site to VPN.
     * 
     * @since SDNO 0.5
     */
    Vpn bindSiteToVpn(String l3VpnUuid, String tpUuid, VpnCreatePolicy vpnCreatePolicy) throws ServiceException;

    /**
     * Unbind site from VPN.
     * 
     * @since SDNO 0.5
     */
    Vpn unbindSiteFromVpn(String vpnUuid, String tpUuid) throws ServiceException;

    /**
     * Get VPN status.
     * 
     * @since SDNO 0.5
     */
    String getVpnStatus(String uuid) throws ServiceException;

    /**
     * Update VPN description.
     * 
     * @since SDNO 0.5
     */
    Vpn updateDescription(String uuid, String desc) throws ServiceException;

}
