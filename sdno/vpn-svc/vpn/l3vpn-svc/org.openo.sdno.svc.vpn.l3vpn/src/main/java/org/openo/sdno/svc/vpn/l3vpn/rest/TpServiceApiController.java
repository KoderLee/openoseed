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

import java.util.List;

import org.openo.sdno.remoteservice.exception.ServiceException;
import org.openo.sdno.roa.annotation.Consumes;
import org.openo.sdno.roa.annotation.DEL;
import org.openo.sdno.roa.annotation.GET;
import org.openo.sdno.roa.annotation.POST;
import org.openo.sdno.roa.annotation.Path;
import org.openo.sdno.roa.annotation.PathParam;
import org.openo.sdno.roa.annotation.Produces;
import org.openo.sdno.roa.annotation.Target;
import org.openo.sdno.cbb.sdnwan.util.ScopeChecker;
import org.openo.sdno.cbb.sdnwan.util.UuidUtil;
import org.openo.sdno.cbb.sdnwan.util.constans.ResultConstants;
import org.openo.sdno.cbb.sdnwan.util.error.ServiceExceptionUtil;
import org.openo.sdno.cbb.sdnwan.util.rest.RestRsp;
import org.openo.sdno.svc.vpn.l3vpn.services.inf.VpnTpService;
import org.openo.sdno.vpn.wan.servicemodel.tp.Tp;
import com.puer.framework.base.service.IResource;

/**
 * TP Restful API service class.<br/>
 * 
 * @author
 * @version SDNO 0.5 2016-3-11
 */
@Path("/svc/vpn/v1/tpservice")
@Target("sdno_l3vpn")
public class TpServiceApiController extends IResource<VpnTpService> {

    /**
     * Create TP and store TP info in database.<br/>
     * 
     * @param tp TP info
     * @return TP info
     * @throws ServiceException when input is invalid or create TP failed
     * @since SDNO 0.5
     */
    @POST
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public RestRsp<Tp> createSite(final Tp tp) throws ServiceException {
        ScopeChecker.checkScope(tp);
        return new RestRsp<Tp>(ResultConstants.SUCCESS, service.createTp(tp));
    }

    /**
     * Delete TP from database by id.<br/>
     * 
     * @param id Id of TP
     * @return TP info
     * @throws ServiceException when id is invalid or delete TP failed
     * @since SDNO 0.5
     */
    @DEL
    @Path("/{id}")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public RestRsp<Tp> deleteSite(@PathParam("id") final String id) throws ServiceException {
        if(!UuidUtil.validate(id)) {
            ServiceExceptionUtil.throwBadRequestException();
        }
        return new RestRsp<Tp>(ResultConstants.SUCCESS, service.deleteTp(id));
    }

    /**
     * Get Rest path.<br/>
     * 
     * @return Rest path
     * @since SDNO 0.5
     */
    @Override
    public String getResUri() {
        return "/svc/vpn/v1/tpservice";
    }

    /**
     * Query TP info by id.<br/>
     * 
     * @param id Id of TP
     * @return TP info
     * @throws ServiceException when id is invalid or query TP failed
     * @since SDNO 0.5
     */
    @GET
    @Path("/{id}")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public RestRsp<Tp> getSite(@PathParam("id") final String id) throws ServiceException {
        if(!UuidUtil.validate(id)) {
            ServiceExceptionUtil.throwBadRequestException();
        }
        return new RestRsp<Tp>(ResultConstants.SUCCESS, service.getSite(id));
    }

    /**
     * Query all the TP info.<br/>
     * 
     * @return All TP info
     * @throws ServiceException when query TP failed
     * @since SDNO 0.5
     */
    @GET
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public RestRsp<List<Tp>> getSites() throws ServiceException {
        return new RestRsp<List<Tp>>(ResultConstants.SUCCESS, service.getSites());
    }
}
