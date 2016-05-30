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

import org.springframework.beans.factory.annotation.Autowired;

import org.openo.sdno.remoteservice.exception.ServiceException;
import org.openo.sdno.roa.annotation.Consumes;
import org.openo.sdno.roa.annotation.DEL;
import org.openo.sdno.roa.annotation.GET;
import org.openo.sdno.roa.annotation.POST;
import org.openo.sdno.roa.annotation.PUT;
import org.openo.sdno.roa.annotation.Path;
import org.openo.sdno.roa.annotation.PathParam;
import org.openo.sdno.roa.annotation.Produces;
import org.openo.sdno.roa.annotation.Target;
import org.openo.sdno.roa.common.HttpContext;
import org.openo.sdno.roa.common.RequestInputStream;
import org.openo.sdno.cbb.sdnwan.util.rest.RestRsp;
import org.openo.sdno.cbb.sdnwan.util.rest.RoaInputStreamParser;
import org.openo.sdno.svc.vpn.composedvpn.nbi.multiasvpn.result.ErrorCode;
import org.openo.sdno.svc.vpn.composedvpn.nbi.multiasvpn.result.StatusData;
import org.openo.sdno.svc.vpn.composedvpn.nbi.multiasvpn.service.inf.IMultiAsVpnModifyService;
import org.openo.sdno.svc.vpn.composedvpn.nbi.multiasvpn.service.inf.MultiAsVpnAppIf;
import org.openo.sdno.svc.vpn.composedvpn.nbi.multiasvpn.service.inf.consistency.IComposedVpnConsistencyService;
import org.openo.sdno.vpn.wan.servicemodel.composedvpn.VpnDescriptionModifyVo;
import org.openo.sdno.vpn.wan.servicemodel.vpn.MultiAsVpn;
import com.puer.framework.base.service.IResource;

/**
 * Restful interface for composed VPN service or MultiAsVpn.<br/>
 * MultiAsVpn - Multiple Autonomous Virtual Private Networks
 * <p>
 * External restful interface for composed VPN.The class contain methods to create, delete, get.
 * </p>
 * 
 * @author
 * @version SDNO 0.5 17-Mar-2016
 */
@Path("svc/composedvpn/v1")
@Target("sdno_sdnwan")
public class MultiAsVpnResource extends IResource<MultiAsVpnAppIf> {

    private IMultiAsVpnModifyService vpnModifyService;

    @Autowired
    private IComposedVpnConsistencyService consistencyService;


    @Override
    public String getResUri() {
        return "svc/composedvpn/v1";
    }

    /**
     * Get composed VPN by VPN id from database <br/>
     * 
     * @param id - ID of the VPN
     * @param httpContext HTTP context for the rest call
     * @return RestRsp< MultiAsVpn> object which contains the basic information of composed
     *         VPN and the result of operation.
     * @since SDNO 0.5
     */
    @GET
    @Path("/detail/{id}")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public RestRsp<MultiAsVpn> getMultiAsVpnsById(@PathParam("id") String id, HttpContext httpContext)
            throws ServiceException {
        return service.getMultiAsVpnById(id, httpContext);
    }

    /**
     * Get status of composed VPNs by list of VPN IDs.<br/>
     * 
     * @param ids List of VPN IDs
     * @param httpContext HTTP context for the rest call
     * @return RestRsp< List< StatusData>> object which contains the list of status information of
     *         input composed VPN and the result of operation.
     * @since SDNO 0.5
     */
    @GET
    @Path("/status/{ids}")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public RestRsp<List<StatusData>> getStatusByIds(@PathParam("ids") final String ids, final HttpContext httpContext) {
        RestRsp<List<StatusData>> result;

        try {
            result = service.getMultiStatusByIds(ids, httpContext);
        } catch(final ServiceException e) {
            result = new RestRsp<>();
            result.setResult(ErrorCode.RESULT_FAILED);
            result.setExceptionArg(e.getExceptionArgs());
            result.setMessage(e.getId());

        }

        return result;
    }



    /**
     * Create a composed VPN <br/>
     * 
     * @param input MultiAsVpn - Composed VPN information which consists of Segment VPN info and VPN
     *            Basic Info
     * @param httpContext HTTP context for the rest call
     * @return RestRsp< MultiAsVpn> object which contains the information of newly created composed
     *         VPN, and the result of operation.
     * @throws ServiceException if the parameter is illegal or internal error
     * @since SDNO 0.5
     */
    @POST
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public RestRsp<MultiAsVpn> createVpn(RequestInputStream input, HttpContext httpContext) throws ServiceException {
        MultiAsVpn multiAsVpn = RoaInputStreamParser.fromJson(input, MultiAsVpn.class);
        RestRsp<MultiAsVpn> result;

        try {
            result = service.createMultiAsVpn(multiAsVpn, httpContext);
        } catch(ServiceException e) {
            result = setExceptionInfo(e);

        }

        return result;
    }

    /**
     * Delete a composed VPN by VPN ID.If the VPN is active then the delete will fail.<br/>
     * 
     * @param vpnId - ID of the composed VPN
     * @param httpContext HTTP context for the rest call
     * @return RestRsp< MultiAsVpn> object which contains the information of deleted composed VPN,
     *         and the result of operation.
     * @since SDNO 0.5
     */
    @DEL
    @Path("/{id}")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public RestRsp<MultiAsVpn> deleteMultiAsVpn(@PathParam("id") String vpnId, HttpContext httpContext) {
        RestRsp<MultiAsVpn> result;

        try {
            result = service.deleteMultiAsVpn(vpnId, httpContext);
        } catch(ServiceException e) {
            result = setExceptionInfo(e);

        }

        return result;
    }

    /**
     * Forcefully delete the composed VPN.Delete is success even if VPN is active. <br/>
     * 
     * @param vpnID - ID of the composed VPN
     * @param httpContext HTTP context for the rest call
     * @return RestRsp< MultiAsVpn> object which contains the information of forcefully deleted
     *         composed VPN, and the result of operation.
     * @throws ServiceException if the parameter is illegal or internal error
     * @since SDNO 0.5
     */
    @DEL
    @Path("/forceDelete/{id}")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public MultiAsVpn forceDelete(@PathParam("id") String vpnId, HttpContext httpContext) throws ServiceException {
        return consistencyService.forceDelete(vpnId, httpContext);
    }

    /**
     * Modify composed VPN description<br/>
     * 
     * @param input - VpnDescriptionModifyVo - Contains VPNID, service type and description
     *            information
     * @param httpContext -HTTP context for the rest call
     * @return RestRsp< MultiAsVpn> object which contains the information of modified composed VPN,
     *         and the result of operation.
     * @throws ServiceException if the parameter is illegal or internal error
     * @since SDNO 0.5
     */
    @PUT
    @Path("/vpndescription")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public RestRsp<MultiAsVpn> modifyVpnDescription(RequestInputStream input, HttpContext httpContext)
            throws ServiceException {
        VpnDescriptionModifyVo modifyVo = RoaInputStreamParser.fromJson(input, VpnDescriptionModifyVo.class);
        RestRsp<MultiAsVpn> result;

        try {
            result =
                    vpnModifyService.modifyMultiAsVpnDescription(modifyVo.getDescription(), modifyVo.getUuid(),
                            httpContext);
        } catch(ServiceException e) {
            result = setExceptionInfo(e);

        }

        return result;
    }

    public final void setVpnModifyService(IMultiAsVpnModifyService vpnModifyService) {
        this.vpnModifyService = vpnModifyService;
    }

    private RestRsp<MultiAsVpn> setExceptionInfo(ServiceException e) {
        RestRsp<MultiAsVpn> result;
        result = new RestRsp<>();

        result.setResult(ErrorCode.RESULT_FAILED);
        result.setExceptionArg(e.getExceptionArgs());
        result.setMessage(e.getId());

        return result;
    }
}
