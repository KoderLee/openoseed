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

import org.openo.sdno.remoteservice.exception.ServiceException;
import org.openo.sdno.cbb.sdnwan.util.rest.RestRsp;
import org.openo.sdno.vpn.wan.networkmodel.NetModel;
import org.openo.sdno.vpn.wan.networkmodel.servicel3vpn.L3vpn;
import com.puer.framework.base.service.IService;

public interface L3vpnSbiService extends IService {

    /**
     * Create layer 3 VPN.
     * 
     * @since SDNO 0.5
     */
    <T extends NetModel> T createL3vpn(String cltuuid, NetModel l3vpn) throws ServiceException;

    /**
     * Update layer 3 VPN.
     * 
     * @since SDNO 0.5
     */
    <T extends NetModel> T updateL3vpn(String cltuuid, T l3vpn) throws ServiceException;

    /**
     * Update layer 3 VPN description.
     * 
     * @since SDNO 0.5
     */
    void updateL3vpnDesc(String cltuuid, String vpnId, String desc) throws ServiceException;

    /**
     * Update layer 3 VPN description.
     * 
     * @since SDNO 0.5
     */
    <T extends NetModel> T updateL3vpnDesc(String cltuuid, T l3vpn) throws ServiceException;

    /**
     * Update layer 3 VPN admin status.
     * 
     * @since SDNO 0.5
     */
    void updateL3vpnAdminStatus(String cltuuid, String vpnId, String adminStatus) throws ServiceException;

    /**
     * Delete layer 3 VPN.
     * 
     * @since SDNO 0.5
     */
    void deleteL3vpn(String cltuuid, String uuid) throws ServiceException;

    /**
     * Bind terminate point to layer 3 VPN.
     * 
     * @since SDNO 0.5
     */
    <T extends NetModel> T createL3vpnBindTp(String cltuuid, String vpnId, T l3ac) throws ServiceException;

    /**
     * Unbind terminate point from layer VPN.
     * 
     * @since SDNO 0.5
     */
    void deleteL3vpnBindTp(String cltuuid, String vpnId, String tpId) throws ServiceException;

    /**
     * Get opration status.
     * 
     * @since SDNO 0.5
     */
    L3vpn getOperStatus(String cltuuid, String uuid) throws ServiceException;

    /**
     * Release VPN.
     * 
     * @since SDNO 0.5
     */
    RestRsp<String> releaseBindVpn(String ctrlUuid, String deleteVniId) throws ServiceException;

}
