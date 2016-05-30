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
package org.openo.sdno.svc.vpn.composedvpn.nbi.te.rest;

import java.util.List;

import org.openo.sdno.remoteservice.exception.ServiceException;
import org.openo.sdno.roa.annotation.Consumes;
import org.openo.sdno.roa.annotation.GET;
import org.openo.sdno.roa.annotation.Path;
import org.openo.sdno.roa.annotation.Produces;
import org.openo.sdno.roa.annotation.QueryParam;
import org.openo.sdno.roa.annotation.Target;
import org.openo.sdno.roa.common.HttpContext;
import org.openo.sdno.cbb.sdnwan.util.rest.RestRsp;
import org.openo.sdno.svc.vpn.composedvpn.nbi.te.service.inf.TeServiceIf;
import org.openo.sdno.vpn.wan.servicemodel.composedvpn.TePathQueryVo;
import org.openo.sdno.vpn.wan.servicemodel.tepath.TePathList;
import com.puer.framework.base.service.IResource;

/**
 * Service restful class for TE resource (Traffic Engineering)<br/>
 * 
 * @author
 * @version SDNO 0.5 17-Mar-2016
 */
@Path("svc/te/v1")
@Target("sdno_sdnwan")
public class TeResource extends IResource<TeServiceIf> {

    @Override
    public String getResUri() {
        return "svc/te/v1";
    }

    /**
     * Get composed VPN TE path <br/>
     * 
     * @param vpnId - ID of VPN
     * @param srcNeId - NEID of the source device
     * @param destNeId - NEID of the destination device
     * @param srcAcId - source AC ID
     * @param destAcId - Destination AC ID
     * @return RestRsp< List< TePathList>> object which contains the information of list of TE paths
     *         and the result of operation
     * @throws ServiceException if the parameter is illegal or internal error
     * @since SDNO 0.5
     */
    @GET
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public RestRsp<List<TePathList>> getComposedVpnTePath(@QueryParam("vpnId") String vpnId,
            @QueryParam("srcNeId") String srcNeId, @QueryParam("destNeId") String destNeId,
            @QueryParam("srcAcId") String srcAcId, @QueryParam("destAcId") String destAcId, HttpContext httpContext)
            throws ServiceException {
        TePathQueryVo queryVo = new TePathQueryVo(vpnId, "ComposedVPN", srcNeId, destNeId, srcAcId, destAcId);
        return service.getComposedVpnTePath(queryVo);
    }
}
