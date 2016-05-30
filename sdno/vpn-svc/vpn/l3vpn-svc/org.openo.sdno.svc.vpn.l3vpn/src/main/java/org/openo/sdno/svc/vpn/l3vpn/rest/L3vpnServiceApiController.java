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
package org.openo.sdno.svc.vpn.l3vpn.rest;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.openo.sdno.remoteservice.exception.ServiceException;
import org.openo.sdno.roa.annotation.Consumes;
import org.openo.sdno.roa.annotation.DEL;
import org.openo.sdno.roa.annotation.GET;
import org.openo.sdno.roa.annotation.POST;
import org.openo.sdno.roa.annotation.PUT;
import org.openo.sdno.roa.annotation.Path;
import org.openo.sdno.roa.annotation.PathParam;
import org.openo.sdno.roa.annotation.Produces;
import org.openo.sdno.roa.annotation.QueryParam;
import org.openo.sdno.roa.annotation.Target;
import org.openo.sdno.roa.common.RequestInputStream;
import org.openo.sdno.cbb.sdnwan.util.ScopeChecker;
import org.openo.sdno.cbb.sdnwan.util.UuidUtil;
import org.openo.sdno.cbb.sdnwan.util.checker.StringChecker;
import org.openo.sdno.cbb.sdnwan.util.constans.ResultConstants;
import org.openo.sdno.cbb.sdnwan.util.error.ServiceExceptionUtil;
import org.openo.sdno.cbb.sdnwan.util.rest.RestRsp;
import org.openo.sdno.cbb.sdnwan.util.rest.RoaInputStreamParser;
import org.openo.sdno.svc.vpn.l3vpn.services.inf.L3vpnService;
import org.openo.sdno.vpn.wan.servicemodel.composedvpn.VpnAndCreatePolicy;
import org.openo.sdno.vpn.wan.servicemodel.policy.VpnCreatePolicy;
import org.openo.sdno.vpn.wan.servicemodel.tp.Tp;
import org.openo.sdno.vpn.wan.servicemodel.vpn.Vpn;
import com.puer.framework.base.service.IResource;

/**
 * L3VPN Restful API service class.<br/>
 * 
 * @author
 * @version SDNO 0.5 2016-3-11
 */
@Path("/svc/l3vpn/v1/l3vpnservice")
@Target("sdno_l3vpn")
public class L3vpnServiceApiController extends IResource<L3vpnService> {

    static final int MAX_DESCRIPTION_LEN = 255;


    /**
     * Send Restful request to controller to bind TP with VPN and update TP info in database.<br/>
     * 
     * @param vpnUuid UUID of VPN
     * @param tpUuid UUID of TP
     * @param input Contains VPN creation policy
     * @return VPN info
     * @throws ServiceException when UUID is invalid or add binding relationship failed
     * @since SDNO 0.5
     */
    @POST
    @Path("/{uuid1}/tps/{uuid2}/bindsite")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public RestRsp<Vpn> bindSiteToVpn(@PathParam("uuid1") final String vpnUuid,
            @PathParam("uuid2") final String tpUuid, final RequestInputStream input) throws ServiceException {
        final VpnCreatePolicy vpnCreatePolicy = RoaInputStreamParser.fromJson(input, VpnCreatePolicy.class);
        if(!UuidUtil.validate(vpnUuid) || !UuidUtil.validate(tpUuid)) {
            ServiceExceptionUtil.throwBadRequestException();
        }
        final Vpn newVpn = service.bindSiteToVpn(vpnUuid, tpUuid, vpnCreatePolicy);
        return new RestRsp<Vpn>(ResultConstants.SUCCESS, newVpn);
    }

    /**
     * Send Restful request to controller to create VPN and add VPN info into database.<br/>
     * 
     * @param input Contains VPN info and VPN creation policy
     * @return VPN info
     * @throws ServiceException when input is invalid or create VPN failed
     * @since SDNO 0.5
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public RestRsp<Vpn> createVpn(final RequestInputStream input) throws ServiceException {
        final VpnAndCreatePolicy vpnAndCreatePolicy = RoaInputStreamParser.fromJson(input, VpnAndCreatePolicy.class);
        ScopeChecker.checkScope(vpnAndCreatePolicy.getVpn());
        ScopeChecker.checkScope(vpnAndCreatePolicy.getVpnCreatePolicy());
        // It should have an agreement with application, if the VPN is null,
        // application should report error.
        if(vpnAndCreatePolicy.getVpn() == null) {
            ServiceExceptionUtil.throwBadRequestException();
        }

        final Vpn vpn = service.createVpn(vpnAndCreatePolicy);

        return new RestRsp<Vpn>(ResultConstants.SUCCESS, vpn);
    }



    /**
     * Send Restful request to controller to unbind TP with VPN and update TP info in database.<br/>
     * 
     * @param vpnUuid UUID of VPN
     * @param tpUuid UUID of TP
     * @return VPN info
     * @throws ServiceException when UUID is invalid or delete binding relationship failed
     * @since SDNO 0.5
     */
    @POST
    @Path("/{uuid1}/tps/{uuid2}/unbindsite")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public RestRsp<Vpn> unbindSiteFromVpn(@PathParam("uuid1") final String vpnUuid,
            @PathParam("uuid2") final String tpUuid) throws ServiceException {
        if(!UuidUtil.validate(vpnUuid) || !UuidUtil.validate(tpUuid)) {
            ServiceExceptionUtil.throwBadRequestException();
        }
        final Vpn newVpn = service.unbindSiteFromVpn(vpnUuid, tpUuid);
        return new RestRsp<Vpn>(ResultConstants.SUCCESS, newVpn);
    }

    /**
     * Send Restful request to controller to delete VPN and delete VPN info from database.<br/>
     * 
     * @param vpnUuid UUID of VPN
     * @return VPN info
     * @throws ServiceException when UUID is invalid or delete VPN failed
     * @since SDNO 0.5
     */
    @DEL
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public RestRsp<Vpn> deleteVpn(@PathParam("id") final String vpnUuid) throws ServiceException {
        if(!UuidUtil.validate(vpnUuid)) {
            ServiceExceptionUtil.throwBadRequestException();
        }
        final Vpn newVpn = service.deleteVpn(vpnUuid);

        return new RestRsp<Vpn>(ResultConstants.SUCCESS, newVpn);
    }

    /**
     * Get Rest path.<br/>
     * 
     * @return Rest path
     * @since SDNO 0.5
     */
    @Override
    public String getResUri() {
        return "/svc/l3vpn/v1/l3vpnservice";
    }

    /**
     * Query VPN info from database by UUID.<br/>
     * 
     * @param vpnUuid UUID of VPN
     * @return VPN info
     * @throws ServiceException when UUID is invalid or query VPN failed
     * @since SDNO 0.5
     */
    @GET
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public RestRsp<Vpn> getVpn(@PathParam("id") final String vpnUuid) throws ServiceException {
        // check vpnUuid
        if(!UuidUtil.validate(vpnUuid)) {
            ServiceExceptionUtil.throwBadRequestException();
        }

        final Vpn vpn = service.getVpnById(vpnUuid);

        return new RestRsp<Vpn>(ResultConstants.SUCCESS, vpn);
    }

    /**
     * Query VPN status from database by UUID.<br/>
     * 
     * @param vpnUuid UUID of VPN
     * @return String of VPN status
     * @throws ServiceException when UUID is invalid or query VPN failed
     * @since SDNO 0.5
     */
    @GET
    @Path("/{uuid}/syncstatus")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public RestRsp<String> getVpnStatus(@PathParam("uuid") final String vpnUuid) throws ServiceException {
        // check vpnUuid
        if(!UuidUtil.validate(vpnUuid)) {
            ServiceExceptionUtil.throwBadRequestException();
        }
        final String status = service.getVpnStatus(vpnUuid);
        return new RestRsp<String>(ResultConstants.SUCCESS, "", status);
    }

    /**
     * Send Restful request to controller to modify description of VPN and update it in database.<br/>
     * 
     * @param vpnUuid UUID of VPN
     * @param description Description string
     * @return VPN info
     * @throws ServiceException when UUID is invalid or modify description failed
     * @since SDNO 0.5
     */
    @PUT
    @Path("/description/{uuid}/{description}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public RestRsp<Vpn> updateDescription(@PathParam("uuid") final String vpnUuid,
            @PathParam("description") final String description) throws ServiceException {
        StringChecker.checkLength(1, MAX_DESCRIPTION_LEN, description, "description");
        final Vpn vpn = service.updateDescription(vpnUuid, description);
        return new RestRsp<Vpn>(ResultConstants.SUCCESS, vpn);
    }

    /**
     * Send Restful request to controller to update VPN info and update it in database.<br/>
     * 
     * @param input Contains VPN info
     * @return VPN info
     * @throws ServiceException when input is invalid or update VPN failed
     * @since SDNO 0.5
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public RestRsp<Vpn> updateVpn(final RequestInputStream input) throws ServiceException {
        final Vpn vpn = RoaInputStreamParser.fromJson(input, Vpn.class);
        // It should have an agreement with application, if the VPN is null,
        // application should report error.
        if(vpn == null) {
            ServiceExceptionUtil.throwBadRequestException();
        }
        ScopeChecker.checkScope(vpn);

        final Vpn newVpn = service.updateVpn(vpn);
        return new RestRsp<Vpn>(ResultConstants.SUCCESS, newVpn);
    }

}
