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
package org.openo.sdno.svc.vpn.composedvpn.nbi.resource.rest;

import java.util.List;

import javax.ws.rs.core.Context;

import org.openo.sdno.remoteservice.exception.ServiceException;
import org.openo.sdno.roa.annotation.Consumes;
import org.openo.sdno.roa.annotation.GET;
import org.openo.sdno.roa.annotation.Path;
import org.openo.sdno.roa.annotation.PathParam;
import org.openo.sdno.roa.annotation.Produces;
import org.openo.sdno.roa.annotation.QueryParam;
import org.openo.sdno.roa.annotation.Target;
import org.openo.sdno.roa.common.HttpContext;
import org.openo.sdno.cbb.sdnwan.util.rest.RestRsp;
import org.openo.sdno.svc.vpn.composedvpn.nbi.resource.inf.ISiteService;
import org.openo.sdno.vpn.wan.servicemodel.composedvpn.SiteVo;
import com.puer.framework.base.service.IResource;

/**
 * Service restful class for Site resource<br/>
 * 
 * @author
 * @version SDNO 0.5 17-Mar-2016
 */
@Path("/svc/site/v1")
@Target("sdno_sdnwan")
public class SiteResource extends IResource<ISiteService> {

    @Override
    public String getResUri() {
        return "/svc/site/v1";
    }

    /**
     * Get site by ID and attributes <br/>
     * 
     * @param attr - Attributes
     * @return RestRsp< SiteVo> object which contains the information of site, and the
     *         result of operation.
     * @since SDNO 0.5
     */
    @GET
    @Path("/{uuid}")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public RestRsp<SiteVo> getSite(@Context HttpContext httpContext, @PathParam("uuid") String uuid,
            @QueryParam("attr") String attr) throws ServiceException {
        return service.getSite(httpContext, uuid, attr);
    }
}
