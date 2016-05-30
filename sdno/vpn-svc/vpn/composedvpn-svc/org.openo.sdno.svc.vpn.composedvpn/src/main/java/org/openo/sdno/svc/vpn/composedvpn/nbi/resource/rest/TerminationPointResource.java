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
import org.openo.sdno.roa.annotation.Produces;
import org.openo.sdno.roa.annotation.QueryParam;
import org.openo.sdno.roa.annotation.Target;
import org.openo.sdno.roa.common.HttpContext;
import org.openo.sdno.cbb.sdnwan.util.rest.RestRsp;
import org.openo.sdno.inventory.sdk.model.TerminationPointMO;
import org.openo.sdno.svc.vpn.composedvpn.nbi.resource.inf.ITerminationPointService;
import com.puer.framework.base.service.IResource;

/**
 * Service restful class for Termination Point resource<br/>
 * TP - Termination Point
 * 
 * @author
 * @version SDNO 0.5 17-Mar-2016
 */
@Path("/svc/terminationpoint/v1")
@Target("sdno_sdnwan")
public class TerminationPointResource extends IResource<ITerminationPointService> {

    @Override
    public String getResUri() {
        return "/svc/terminationpoint/v1";
    }

    /**
     * Get termination point resource NE (network element) UUID -unique universal ID <br/>
     * 
     * @param neUuid - UUID of NE
     * @return RestRsp< List < TerminationPointMO>> object which contains the information of list of
     *         TPs and the result of operation
     * @throws ServiceException if the parameter is illegal or internal error
     * @since SDNO 0.5
     */
    @GET
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public RestRsp<List<TerminationPointMO>> getTpsByNeUuid(@Context HttpContext httpContext,
            @QueryParam("neUuid") String neUuid) throws ServiceException {
        return service.queryByNeid(neUuid);
    }
}
