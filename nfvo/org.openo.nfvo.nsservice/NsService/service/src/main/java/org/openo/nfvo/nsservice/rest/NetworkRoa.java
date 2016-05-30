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

package org.openo.nfvo.nsservice.rest;

import org.openo.nfvo.nsservice.simpleservice.services.api.NetworkService;

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
* create virtual network, request from initialize network service.<br/>
* <p>
* </p>
* 
* @author
* @version NFVO 0.5 May 15, 2016
 */
@Path("/nfv/v1/network/resource")
@Target("do-med-network")
public class NetworkRoa {

    private static final OssLog LOG = OssLogFactory.getLogger(NetworkRoa.class);

    private NetworkService netWorkBusiness;

    /**
     * Create the network node<br/>
     * 
     * @param context
     * The parameter for the request
     * @return network data
     * @since NFVO 0.5
     */
    @POST
    public String createNetwork(HttpContext context) {
        LOG.info("in createNetwork");
        return netWorkBusiness.doCreateNetwork(context);
    }

    /**
     * Delete the network node<br/>
     * 
     * @param resourceId
     * Identifier of resource, used for service executer
     * @param context
     * Context of http request for delete network
     * @return network data
     * @since NFVO 0.5
     */
    @DEL
    @Path("/{resourceId}")
    public String delNetwork(@PathParam("resourceId") String resourceId, HttpContext context) {

        return netWorkBusiness.doDelNetwork(resourceId, context);
    }

    /**
     * Modify the network node<br/>
     * 
     * @param context
     * The parameter for the request
     * @return network data
     * @since NFVO 0.5
     */
    @PUT
    public String putWithoutNetwork(HttpContext context) {

        return netWorkBusiness.doPutWithoutIdNetwork(context);
    }

    /**
     * Modify the network node<br/>
     * 
     * @param resourceId
     * Identifier of resource, used for service executer
     * @param context
     * The parameter for the request
     * @return network data
     * @since NFVO 0.5
     */
    @PUT
    @Path("/{resourceId}")
    public String putNetwork(@PathParam("resourceId") String resourceId, HttpContext context) {

        return netWorkBusiness.doPutNetwork(resourceId, context);
    }

    public void setNetworkBusiness(NetworkBusiness netWorkBusiness) {
        this.netWorkBusiness = netWorkBusiness;
    }

}
