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
import org.openo.sdno.vpn.wan.servicemodel.vpn.MultiAsVpn;
import org.openo.sdno.vpn.wan.servicemodel.vpn.Vpn;
import com.puer.framework.base.service.IService;

public interface IMultiAsVpnModifyService extends IService {

    /**
     * Add site.
     * 
     * @since SDNO 0.5
     */
    RestRsp<List<String>> addSite(MultiAsVpn multiAsVpn, HttpContext httpContext) throws ServiceException;

    /**
     * Delete site.
     * 
     * @since SDNO 0.5
     */
    RestRsp<List<String>> deleteSite(String tpUuid, String vpnUuid, HttpContext httpContext) throws ServiceException;

    /**
     * Modify description.
     * 
     * @since SDNO 0.5
     */
    RestRsp<Vpn> modifyDescription(String desc, String serviceType, String vpnUuid, HttpContext httpContext)
            throws ServiceException;

    /**
     * Modify multi As VPN Description
     * 
     * @since SDNO 0.5
     */
    RestRsp<MultiAsVpn> modifyMultiAsVpnDescription(String desc, String multiVpnUuid, HttpContext httpContext)
            throws ServiceException;
}
