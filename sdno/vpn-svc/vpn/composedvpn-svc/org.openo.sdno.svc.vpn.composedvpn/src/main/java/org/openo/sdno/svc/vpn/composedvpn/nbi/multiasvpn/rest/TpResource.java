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

import java.util.List;

import org.openo.sdno.remoteservice.exception.ServiceException;
import org.openo.sdno.roa.annotation.Consumes;
import org.openo.sdno.roa.annotation.DEL;
import org.openo.sdno.roa.annotation.POST;
import org.openo.sdno.roa.annotation.Path;
import org.openo.sdno.roa.annotation.PathParam;
import org.openo.sdno.roa.annotation.Produces;
import org.openo.sdno.roa.annotation.Target;
import org.openo.sdno.roa.common.HttpContext;
import org.openo.sdno.roa.common.RequestInputStream;
import org.openo.sdno.cbb.sdnwan.util.rest.RestRsp;
import org.openo.sdno.cbb.sdnwan.util.rest.RoaInputStreamParser;
import org.openo.sdno.svc.vpn.composedvpn.nbi.l3vpn.result.ErrorCode;
import org.openo.sdno.svc.vpn.composedvpn.nbi.multiasvpn.service.inf.IMultiAsVpnModifyService;
import org.openo.sdno.svc.vpn.composedvpn.nbi.multiasvpn.service.inf.TpAppIf;
import org.openo.sdno.vpn.wan.servicemodel.tp.Tp;
import org.openo.sdno.vpn.wan.servicemodel.vpn.MultiAsVpn;
import com.puer.framework.base.service.IResource;
import com.puer.framework.container.util.SysEnvironment;

/**
 * Service restful class for composed VPN TP<br/>
 * MultiAsVpn Multiple Autonomous Virtual Private Networks
 * 
 * @author
 * @version SDNO 0.5 17-Mar-2016
 */
@Path("svc/tps/v1")
@Target("sdno_sdnwan")
public class TpResource extends IResource<TpAppIf> {

    private IMultiAsVpnModifyService vpnModifyService;


    @Override
    public String getResUri() {
        return "svc/tps/v1";
    }

    /**
     * Add the site .<br/>
     * 
     * @param input Composed VPN information
     * @return RestRsp object which contains the result of operation.
     * @throws ServiceException if the parameter is illegal or internal error
     * @since SDNO 0.5
     */
    @POST
    @Path("/l3vpn/addsite")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public RestRsp<List<String>> addSite(RequestInputStream input, HttpContext httpContext) throws ServiceException {
        MultiAsVpn multiAsVpn = RoaInputStreamParser.fromJson(input, MultiAsVpn.class);
        RestRsp<List<String>> result;

        try {
            result = vpnModifyService.addSite(multiAsVpn, httpContext);
        } catch(ServiceException e) {
            result = new RestRsp<>();
            result.setResult(ErrorCode.RESULT_FAILED);
            result.setExceptionArg(e.getExceptionArgs());
            result.setMessage(e.getId());

        }

        SysEnvironment.getLocale();

        return result;
    }

    /**
     * Delete the site .<br/>
     * 
     * @param tpUuid ID of Termination Point
     * @param vpnUuid ID of the composed VPN
     * @return RestRsp object which contains the result of operation.
     * @since SDNO 0.5
     */
    @DEL
    @Path("/l3vpn/{vpnuuid}/deletesite/{tpUuid}")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public RestRsp<List<String>> deleteSite(@PathParam("vpnuuid") String vpnUuid, @PathParam("tpUuid") String tpUuid,
            HttpContext httpContext) {
        RestRsp<List<String>> result;

        try {
            result = vpnModifyService.deleteSite(tpUuid, vpnUuid, httpContext);
        } catch(ServiceException e) {
            result = new RestRsp<>();
            result.setResult(ErrorCode.RESULT_FAILED);
            result.setExceptionArg(e.getExceptionArgs());
            result.setMessage(e.getId());
        }

        SysEnvironment.getLocale();

        return result;
    }

    public final void setVpnModifyService(IMultiAsVpnModifyService vpnModifyService) {
        this.vpnModifyService = vpnModifyService;
    }

}
