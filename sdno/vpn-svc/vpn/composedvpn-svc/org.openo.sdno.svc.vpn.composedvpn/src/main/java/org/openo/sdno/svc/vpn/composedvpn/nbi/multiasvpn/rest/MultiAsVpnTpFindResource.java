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
package org.openo.sdno.svc.vpn.composedvpn.nbi.multiasvpn.rest;

import org.openo.sdno.remoteservice.exception.ServiceException;
import org.openo.sdno.roa.annotation.Consumes;
import org.openo.sdno.roa.annotation.POST;
import org.openo.sdno.roa.annotation.Path;
import org.openo.sdno.roa.annotation.Produces;
import org.openo.sdno.roa.annotation.Target;
import org.openo.sdno.roa.common.HttpContext;
import org.openo.sdno.roa.common.RequestInputStream;
import org.openo.sdno.cbb.sdnwan.util.rest.RestRsp;
import org.openo.sdno.cbb.sdnwan.util.rest.RoaInputStreamParser;
import org.openo.sdno.svc.vpn.composedvpn.nbi.multiasvpn.service.inf.IMultiAsPathCalculate;
import org.openo.sdno.vpn.wan.servicemodel.composedvpn.VpnDetail;
import org.openo.sdno.vpn.wan.servicemodel.vpn.MultiAsVpn;
import com.puer.framework.base.service.IResource;

/**
 * Service restful class to find paths for the composed VPN <br/>
 * MultiAsVpn - Multiple Autonomous Virtual Private Networks
 * 
 * @author
 * @version SDNO 0.5 17-Mar-2016
 */
@Path("svc/multiaspathfind/v1")
@Target("sdno_sdnwan")
public class MultiAsVpnTpFindResource extends IResource<IMultiAsPathCalculate> {

    @Override
    public String getResUri() {
        return "svc/multiaspathfind/v1";
    }

    /**
     * Find path details between end-points for the given composed VPN.<br/>
     * 
     * @param input - MultiAsVpn - Composed VPN details - (L2VPN + L3VPN or VLL)
     * @return RestRsp< VpnDetail> object which contains the information about links and the
     *         result of operation.
     * @since SDNO 0.5
     */
    @POST
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public RestRsp<VpnDetail> pathFind(RequestInputStream input, HttpContext httpContext) throws ServiceException {
        MultiAsVpn multiAsVpn = RoaInputStreamParser.fromJson(input, MultiAsVpn.class);
        return service.pathFind(multiAsVpn);
    }
}
