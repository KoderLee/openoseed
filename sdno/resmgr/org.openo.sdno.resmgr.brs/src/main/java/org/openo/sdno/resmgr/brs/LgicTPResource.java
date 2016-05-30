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
package org.openo.sdno.resmgr.brs;

@Path("brs/v1/logical-ternmination-points")
@Target("brs")
public class LgicTPResource {

    @GET
    @Produces("application/json")
    @consumes("application/json")
    @Path("/{object_id}")
    public Object getLgicTP(@PathParam("object_id") String objectId, HttpContext context) {

    }

    @GET
    @Produces("application/json")
    @consumes("application/json")
    public Object getLgicTPList(HttpContext context) {

    }

    @POST
    @Produces("application/json")
    @consumes("application/json")
    public Object addLgicTP(final RequestInputStream requestBody, HttpContext context) {

    }

    @PUT
    @Produces("application/json")
    @consumes("application/json")
    @Path("/{object_id}")
    public Object updateLgicTP(@PathParam("object_id") String objectId, final RequestInputStream requestBody,
            HttpContext context) {

    }

    @DEL
    @Produces("application/json")
    @consumes("application/json")
    @Path("/{object_id}")
    public Object delLgicTP(@PathParam("object_id") String objectId, HttpContext context) {

    }

}