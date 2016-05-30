/*******************************************************************************
 * Copyright (c) 2016, Huawei Technologies Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

package org.openo.nfvo.nsservice.simpleservice.roa;

import org.openo.nfvo.nsservice.simpleservice.business.api.VnfBusiness;

import org.openo.baseservice.log.OssLog;
import org.openo.baseservice.log.OssLogFactory;
import org.openo.baseservice.roa.annotation.DEL;
import org.openo.baseservice.roa.annotation.POST;
import org.openo.baseservice.roa.annotation.PUT;
import org.openo.baseservice.roa.annotation.Path;
import org.openo.baseservice.roa.annotation.PathParam;
import org.openo.baseservice.roa.annotation.Target;
import org.openo.baseservice.roa.common.HttpContext;

/**
 * 
* The Rest interface of create/delete/update VNF<br/>
* <p>
* </p>
* 
* @author
* @version NFVO 0.5 May 15, 2016
 */
@Path("/nfv/v1/vnf/resource")
@Target("do-med-vnf")
public class VnfRoa {

    private static final OssLog LOG = OssLogFactory.getLogger(VnfRoa.class);

    private VnfService vnfBusiness;

    /**
     * Create the vnf node<br/>
     * 
     * @param context
     * The context of http request for create vnf node
     * @return vnf data
     * @since NFVO 0.5
     */
    @POST
    public String createVnf(HttpContext context) {
        LOG.info("in createVnf");
        return vnfBusiness.doCreateVnf(context);
    }

    /**
     * Delete the vnf node<br/>
     * 
     * @param resourceId
     * Identifier of resource, used for service executer
     * @param context
     * The context of http request for delete vnf node
     * @return vnf data
     * @since NFVO 0.5
     */
    @DEL
    @Path("/{resourceId}")
    public String delVnf(@PathParam("resourceId") String resourceId, HttpContext context) {

        return vnfBusiness.doDelVnf(resourceId, context);
    }

    /**
     * Modify the vnf node<br/>
     * 
     * @param context
     * The context of http request for modify vnf node
     * @return vnf data
     * @since NFVO 0.5
     */
    @PUT
    public String putWithoutVnf(HttpContext context) {

        return vnfBusiness.doPutWithoutIdVnf(context);
    }

    /**
     * Modify the vnf node<br/>
     * 
     * @param resourceId
     * Identifier of resource, used for service executer
     * @param context
     * The context of http request for modify vnf node
     * @return vnf data
     * @since NFVO 0.5
     */
    @PUT
    @Path("/{resourceId}")
    public String putVnf(@PathParam("resourceId") String resourceId, HttpContext context) {

        return vnfBusiness.doPutVnf(resourceId, context);
    }

    public void setVnfBusiness(VnfBusiness vnfBusiness) {
        this.vnfBusiness = vnfBusiness;
    }

}
