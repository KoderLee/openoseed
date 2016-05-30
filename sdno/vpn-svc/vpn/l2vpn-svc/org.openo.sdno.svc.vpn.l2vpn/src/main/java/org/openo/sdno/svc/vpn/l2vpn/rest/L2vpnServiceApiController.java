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
package org.openo.sdno.svc.vpn.l2vpn.rest;

import java.util.Arrays;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;

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
import org.openo.sdno.roa.common.HttpContext;
import org.openo.sdno.roa.common.RequestInputStream;
import org.openo.sdno.rpc.util.RestTransferUtil;
import org.openo.sdno.cbb.sdnwan.util.ScopeChecker;
import org.openo.sdno.cbb.sdnwan.util.rest.RestRsp;
import org.openo.sdno.cbb.sdnwan.util.rest.RoaInputStreamParser;
import org.openo.sdno.svc.vpn.l2vpn.common.constant.ErrorCode;
import org.openo.sdno.svc.vpn.l2vpn.common.util.ParaVerifyUtils;
import org.openo.sdno.svc.vpn.l2vpn.common.util.ServiceExceptionUtil;
import org.openo.sdno.svc.vpn.l2vpn.service.inf.IL2vpnBussiController;
import org.openo.sdno.vpn.wan.servicemodel.composedvpn.VpnAndCreatePolicy;
import org.openo.sdno.vpn.wan.servicemodel.vpn.Vpn;
import com.puer.framework.base.service.IResource;

/**
 * Restful interface for l2vpn service.<br/>
 * <p>
 * External restful interface of L2VPN, the main interface to operate on L2VPN contain methods to
 * create, get, update, and to check VPN's status.
 * </p>
 * 
 * @author
 * @version SDNO 0.5 2016-3-17
 */
@Path("/svc/l2vpn/v1/l2vpnservice")
@Target("sdno_sdnwan")
public class L2vpnServiceApiController extends IResource<IL2vpnBussiController> {


    @Override
    public String getResUri() {
        return "/svc/l2vpn/v1/l2vpnservice";
    }

    /**
     * To create L2vpn, input json to represent a VPN and return a RestRsp object to store the
     * operation result<br/>
     * 
     * @param input input stream
     * @param httpContext httpcontext
     * @return RestRsp object which contains the basic information of input L2VPN, and the result of
     *         operation.
     * @throws ServiceException if the parameter is illegal or internal error
     * @since SDNO 0.5
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public RestRsp<Vpn> creatL2vpn(RequestInputStream input, HttpContext httpContext) throws ServiceException {
        VpnAndCreatePolicy vpnAndPolicy = RoaInputStreamParser.fromJson(input, VpnAndCreatePolicy.class);
        String strJsonReq = RestTransferUtil.tansferRequest(vpnAndPolicy);

        ParaVerifyUtils.checkNullObj("VpnAndCreatePolicy", vpnAndPolicy);
        ParaVerifyUtils.checkNullObj("Vpn", vpnAndPolicy.getVpn());
        ScopeChecker.checkScope(vpnAndPolicy.getVpn());

        RestRsp<Vpn> rsp = service.createVpn(vpnAndPolicy, httpContext);
        ServiceExceptionUtil.setHttpRspStatus(rsp, httpContext);

        return rsp;
    }

    /**
     * Delete L2vpn by UUID.<br/>
     * 
     * @param vpnUuid UUID of VPN
     * @param httpContext httpcontext
     * @return RestRsp object which contains the result of operation.
     * @throws ServiceException if the parameter is illegal or internal error
     * @since SDNO 0.5
     */
    @DEL
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public RestRsp<Vpn> deleteL2vpn(@PathParam("id") String vpnUuid, HttpContext httpContext) throws ServiceException {

        ParaVerifyUtils.checkLenthStr("id", vpnUuid, 1, ErrorCode.UUID_MAX_LENGTH);

        RestRsp<Vpn> rsp = service.deleteVpn(vpnUuid, httpContext);

        ServiceExceptionUtil.setHttpRspStatus(rsp, httpContext);

        return rsp;
    }

    /**
     * Get L2vpn by L2vpn information, query the vpn through it's tp uuid or cosTempId<br/>
     * 
     * @param tpUuids UUID of tp
     * @param cosTempId costom's template id
     * @param httpContext httpcontext
     * @return RestRsp object which contains the result of operation.
     * @throws ServiceException if the parameter is illegal or internal error
     * @since SDNO 0.5
     */
    @GET
    @Path("/l2vpnservice/l2vpninfor")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public RestRsp<List<Vpn>> getL2vpns(@QueryParam("access_point_list") String tpUuids,
            @QueryParam("cos_template_id") String cosTempId, HttpContext httpContext) throws ServiceException {
        RestRsp<List<Vpn>> rsp = null;
        if(StringUtils.isNotEmpty(tpUuids)) {
            ParaVerifyUtils.checkLenthStr("access_point_list", tpUuids, ErrorCode.MIN_INPUT, ErrorCode.MAX_INPUT);

            String[] tpUuidStrings = tpUuids.split(",");
            rsp = service.getVpnsByTp(Arrays.asList(tpUuidStrings), httpContext);
        } else {
            rsp = service.getVpns(httpContext);
        }
        ServiceExceptionUtil.setHttpRspStatus(rsp, httpContext);

        return rsp;
    }

    /**
     * Get L2vpn by UUID<br/>
     * 
     * @param uuid UUID of L2VPN
     * @param httpContext httpcontext
     * @return RestRsp object which contains the result of operation.
     * @throws ServiceException if the parameter is illegal or internal error
     * @since SDNO 0.5
     */
    @GET
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public RestRsp<List<Vpn>> getL2vpn(@PathParam("id") String uuid, HttpContext httpContext) throws ServiceException {

        ParaVerifyUtils.checkLenthStr("id", uuid, 1, ErrorCode.UUID_MAX_LENGTH);

        RestRsp<List<Vpn>> rsp = service.getVpnsByUuid(uuid, httpContext);
        ServiceExceptionUtil.setHttpRspStatus(rsp, httpContext);

        return rsp;
    }

    /**
     * Get L2VPN status by UUID, to see the vpn is activate or not.<br/>
     * 
     * @param uuid UUID of L2VPN
     * @param httpContext httpcontext
     * @return RestRsp object which contains the result of operation.
     * @throws ServiceException if the parameter is illegal or internal error
     * @since SDNO 0.5
     */
    @GET
    @Path("/status/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public RestRsp<String> getStatusById(@PathParam("id") final String uuid, final HttpContext httpContext)
            throws ServiceException {

        final RestRsp<String> rsp = service.getStatusByUuid(uuid, httpContext);
        ServiceExceptionUtil.setHttpRspStatus(rsp, httpContext);

        return rsp;
    }

    /**
     * Get L2vpn status by uuid, query VPN status from the controller by it's UUID<br/>
     * 
     * @param uuid UUID of L2VPN
     * @param httpContext httpcontext
     * @return RestRsp object which contains the result of operation.
     * @throws ServiceException if the parameter is illegal or internal error
     * @since SDNO 0.5
     */
    @GET
    @Path("/{uuid}/syncstatus")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public RestRsp<String> getL2vpnStatus(@PathParam("uuid") String uuid, HttpContext httpContext)
            throws ServiceException {

        ParaVerifyUtils.checkLenthStr("uuid", uuid, 1, ErrorCode.UUID_MAX_LENGTH);

        RestRsp<List<Vpn>> rsp = service.getVpnsByUuid(uuid, httpContext);
        if(rsp != null && rsp.getData() != null) {
            List<Vpn> vpns = rsp.getData();
            if(!vpns.isEmpty()) {
                RestRsp<String> rspTemp =
                        ServiceExceptionUtil.getSuccesRestRsp(vpns.get(0).getVpnBasicInfo().getAdminStatus());
                ServiceExceptionUtil.setHttpRspStatus(rspTemp, httpContext);
                return rspTemp;
            }
        }
        RestRsp<String> rspRe = ServiceExceptionUtil.getSuccesRestRsp("");
        ServiceExceptionUtil.setHttpRspStatus(rspRe, httpContext);

        return rspRe;
    }

    /**
     * Update L2vpn description by UUID, change the VPN's description by UUID<br/>
     * 
     * @param uuid UUID of L2VPN
     * @param description description
     * @param httpContext httpcontext
     * @return RestRsp object which contains the result of operation.
     * @throws ServiceException if the parameter is illegal or internal error
     * @since SDNO 0.5
     */
    @PUT
    @Path("/description/{uuid}/{description}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public RestRsp<Vpn> updateL2vpnDescription(@PathParam("uuid") String uuid,
            @PathParam("description") String description, HttpContext httpContext) throws ServiceException {

        ParaVerifyUtils.checkLenthStr("description", description, 1, ErrorCode.MAX_INPUT);

        RestRsp<Vpn> rsp = service.modifyDescription(uuid, description, httpContext);
        ServiceExceptionUtil.setHttpRspStatus(rsp, httpContext);

        return rsp;
    }

}
