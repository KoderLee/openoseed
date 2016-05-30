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
package org.openo.sdno.svc.vpn.composedvpn.common.vpn.inf;

import java.util.List;

import org.openo.sdno.remoteservice.exception.ServiceException;
import org.openo.sdno.roa.common.HttpContext;
import org.openo.sdno.cbb.sdnwan.util.rest.RestRsp;
import org.openo.sdno.vpn.wan.servicemodel.composedvpn.VpnAndCreatePolicy;
import org.openo.sdno.vpn.wan.servicemodel.policy.VpnCreatePolicy;
import org.openo.sdno.vpn.wan.servicemodel.tp.Tp;
import org.openo.sdno.vpn.wan.servicemodel.vpn.Vpn;
import com.puer.framework.base.service.IService;

public interface SingleAsVpnSvcIf extends IService {

    /**
     * Create VPN.
     * 
     * @since SDNO 0.5
     */
    RestRsp<Vpn> createVpn(VpnAndCreatePolicy vpn, HttpContext httpContext, boolean isSingeAs) throws ServiceException;

    /**
     * Delete VPN.
     * 
     * @since SDNO 0.5
     */
    RestRsp<Vpn> deleteVpn(VpnAndCreatePolicy vpn, HttpContext httpContext, boolean isSingeAs) throws ServiceException;

    /**
     * Delete VPN by uuid.
     * 
     * @since SDNO 0.5
     */
    RestRsp<Vpn> deleteVpn(String uuid, HttpContext httpContext, boolean isSingeAs) throws ServiceException;

    /**
     * Get VPN by uuid.
     * 
     * @since SDNO 0.5
     */
    RestRsp<Vpn> getVpnByUuid(String uuid, HttpContext httpContext) throws ServiceException;

    /**
     * Get VPN status.
     * 
     * @since SDNO 0.5
     */
    RestRsp<String> getVpnStatus(String uuid, HttpContext httpContext) throws ServiceException;

    /**
     * Get VPN by terminate point.
     * 
     * @since SDNO 0.5
     */
    RestRsp<List<Vpn>> getVpnsByTp(List<String> tpUuidList, HttpContext httpContext) throws ServiceException;

    /**
     * Delete site bind terminate point.
     * 
     * @since SDNO 0.5
     */
    RestRsp<Vpn> deleteSiteBindTp(String vpnUuid, String tpUuid, HttpContext httpContext, boolean isSingeAs)
            throws ServiceException;

    /**
     * Modify VPN description.
     * 
     * @since SDNO 0.5
     */
    RestRsp<Vpn> modifyDescription(String uuid, String desc, HttpContext httpContext, boolean isSingeAs)
            throws ServiceException;

    /**
     * Create terminate point.
     * 
     * @since SDNO 0.5
     */
    RestRsp<Tp> createTp(Tp tp) throws ServiceException;

    /**
     * Delete terminate point.
     * 
     * @since SDNO 0.5
     */
    RestRsp<Tp> deleteTp(String tpId) throws ServiceException;
}
