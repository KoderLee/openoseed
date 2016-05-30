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

import org.openo.nfvo.nsservice.simpleservice.business.api.NsBusiness;

import org.openo.baseservice.log.OssLog;
import org.openo.baseservice.log.OssLogFactory;
import org.openo.baseservice.roa.annotation.DEL;
import org.openo.baseservice.roa.annotation.POST;
import org.openo.baseservice.roa.annotation.PUT;
import org.openo.baseservice.roa.annotation.Path;
import org.openo.baseservice.roa.annotation.PathParam;
import org.openo.baseservice.roa.annotation.Target;
import org.openo.baseservice.roa.common.HttpContext;

@Path("/nfv/v1/ns/resource")
@Target("do-med-ns")
public class NsRoa {

    private static final OssLog LOG = OssLogFactory.getLogger(NsRoa.class);

    private NsService nsBusiness;

    /**
     * Create the ns node<br/>
     * 
     * @param context
     * The context of http request for create ns node
     * @return ns node data
     * @since NFVO 0.5
     */
    @POST
    public String createNs(HttpContext context) {
        LOG.info("in createNs");
        return nsBusiness.doCreateNs(context);
    }

    /**
     * Delete the ns node<br/>
     * 
     * @param resourceId
     * Identifier of resource, used for service executer
     * @param context
     * The context of http request for delete ns node
     * @return ns node data
     * @since NFVO 0.5
     */
    @DEL
    @Path("/{resourceId}")
    public String delNs(@PathParam("resourceId") String resourceId, HttpContext context) {

        return nsBusiness.doDelNs(resourceId, context);
    }

    /**
     * Modify the ns node<br/>
     * 
     * @param context
     * The context of http request for modify ns node
     * @return ns node data
     * @since NFVO 0.5
     */
    @PUT
    public String putWithoutNs(HttpContext context) {

        return nsBusiness.doPutWithoutIdNs(context);
    }

    /**
     * Modify the ns node<br/>
     * 
     * @param resourceId identifier of resource, used for crossdomain service
     * @param context
     * The context of http request for modify ns node
     * @return ns node data
     * @since NFVO 0.5
     */
    @PUT
    @Path("/{resourceId}")
    public String putNs(@PathParam("resourceId") String resourceId, HttpContext context) {

        return nsBusiness.doPutNs(resourceId, context);
    }

    public void setNsBusiness(NsBusiness nsBusiness) {
        this.nsBusiness = nsBusiness;
    }

}
