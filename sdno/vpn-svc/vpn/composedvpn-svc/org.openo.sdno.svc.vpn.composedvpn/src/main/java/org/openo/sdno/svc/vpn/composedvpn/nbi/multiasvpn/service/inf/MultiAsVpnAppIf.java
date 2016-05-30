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
package org.openo.sdno.svc.vpn.composedvpn.nbi.multiasvpn.service.inf;

import java.util.List;

import org.openo.sdno.remoteservice.exception.ServiceException;
import org.openo.sdno.roa.common.HttpContext;
import org.openo.sdno.cbb.sdnwan.util.rest.RestRsp;
import org.openo.sdno.svc.vpn.composedvpn.nbi.multiasvpn.result.StatusData;
import org.openo.sdno.vpn.wan.servicemodel.vpn.MultiAsVpn;
import com.puer.framework.base.service.IService;

public interface MultiAsVpnAppIf extends IService {

    /**
     * Create multi AS VPN
     * 
     * @since SDNO 0.5
     */
    RestRsp<MultiAsVpn> createMultiAsVpn(MultiAsVpn multiAsVpn, HttpContext httpContext) throws ServiceException;

    /**
     * Delete multi AS VPNs
     * 
     * @since SDNO 0.5
     */
    RestRsp<List<MultiAsVpn>> deleteMultiAsVpns(List<String> uuids, HttpContext httpContext) throws ServiceException;

    /**
     * Delete multi AS VPN.
     * 
     * @since SDNO 0.5
     */
    RestRsp<MultiAsVpn> deleteMultiAsVpn(String uuid, HttpContext httpContext) throws ServiceException;

    /**
     * Update multi AS VPN.
     * 
     * @since SDNO 0.5
     */
    RestRsp<MultiAsVpn> updateMultiAsVpn(MultiAsVpn multiAsVpn, HttpContext httpContext) throws ServiceException;

    /**
     * Get multi AS VPNs.
     * 
     * @since SDNO 0.5
     */
    RestRsp<List<MultiAsVpn>> getMultiAsVpns(HttpContext httpContext) throws ServiceException;

    /**
     * Get multi AS VPN by id.
     * 
     * @since SDNO 0.5
     */
    RestRsp<MultiAsVpn> getMultiAsVpnById(String uuid, HttpContext httpContext) throws ServiceException;

    /**
     * Get multi status by ids.
     * 
     * @since SDNO 0.5
     */
    RestRsp<List<StatusData>> getMultiStatusByIds(String uuids, HttpContext httpContext) throws ServiceException;
}
