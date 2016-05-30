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

import java.util.ArrayList;
import java.util.List;

import org.openo.sdno.remoteservice.exception.ServiceException;
import org.openo.sdno.roa.annotation.Consumes;
import org.openo.sdno.roa.annotation.GET;
import org.openo.sdno.roa.annotation.Path;
import org.openo.sdno.roa.annotation.Produces;
import org.openo.sdno.roa.annotation.QueryParam;
import org.openo.sdno.roa.annotation.Target;
import org.openo.sdno.cbb.sdnwan.util.constans.ResultConstants;
import org.openo.sdno.cbb.sdnwan.util.rest.RestRsp;
import org.openo.sdno.svc.vpn.l3vpn.services.inf.L3vpnTeService;
import org.openo.sdno.vpn.wan.servicemodel.tepath.TePath;
import org.openo.sdno.vpn.wan.servicemodel.tepath.TePathList;
import org.openo.sdno.vpn.wan.servicemodel.tepath.TePathQueryKey;
import com.puer.framework.base.service.IResource;

/**
 * TE Restful API service class.<br/>
 * 
 * @author
 * @version SDNO 0.5 2016-3-11
 */
@Path("/svc/l3vpn/v1/teservice")
@Target("sdno_l3vpn")
public class TeServiceApiController extends IResource<L3vpnTeService> {

    /**
     * Get Rest path.<br/>
     * 
     * @return Rest path
     * @since SDNO 0.5
     */
    @Override
    public String getResUri() {
        return "/svc/l3vpn/v1/teservice";
    }

    /**
     * Query TE path from the source and destination sides.<br/>
     * 
     * @param vpnId UUID of VPN
     * @param srcNeId Id of source NE
     * @param destNeId Id of destination NE
     * @param srcAcId Id of source AC
     * @param destAcId Id of destination AC
     * @return List of TePathList
     * @throws ServiceException when query TE path failed
     * @since SDNO 0.5
     */
    @GET
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public RestRsp<List<TePathList>> getTePath(@QueryParam("vpnId") final String vpnId,
            @QueryParam("srcNeId") final String srcNeId, @QueryParam("destNeId") final String destNeId,
            @QueryParam("srcAcId") final String srcAcId, @QueryParam("destAcId") final String destAcId)
            throws ServiceException {

        // Get a two-way TE path, the sdno will query two times and package
        // the results.
        final List<TePathList> tePathLists = new ArrayList<TePathList>();
        TePathQueryKey posTePathQueryKey = null;
        try {
            posTePathQueryKey = new TePathQueryKey(vpnId, srcNeId, destNeId, srcAcId, destAcId);
            final List<TePathList> posTePathLists = getTePathLists(posTePathQueryKey, "positive");
            if(!posTePathLists.isEmpty()) {
                tePathLists.addAll(posTePathLists);
            }
        } catch(final ServiceException e) {
            
        }

        // Query reverse TE path
        List<TePathList> negTePathLists = null;
        TePathQueryKey negTePathQueryKey = null;
        try {
            negTePathQueryKey = new TePathQueryKey(vpnId, destNeId, srcNeId, destAcId, srcAcId);
            negTePathLists = getTePathLists(negTePathQueryKey, "negative");
            if(!negTePathLists.isEmpty()) {
                tePathLists.addAll(negTePathLists);
            }
        } catch(final ServiceException e) {
           
        }

        return new RestRsp<List<TePathList>>(ResultConstants.SUCCESS, tePathLists);

    }

    private List<TePathList> getTePathLists(final TePathQueryKey queryKey, final String direction)
            throws ServiceException {
        final List<TePathList> tePathLists = new ArrayList<TePathList>();

        final RestRsp<List<TePath>> rsp = service.getL3vpnTePath(queryKey);
        if(rsp.getResult() == ResultConstants.SUCCESS) {
            final TePathList tePathList = new TePathList();
            tePathList.setDirection(direction);
            tePathList.setTePaths(rsp.getData());
            if((rsp.getData() != null) && !rsp.getData().isEmpty()) {
                tePathLists.add(tePathList);
            }
        } else {

        }

        return tePathLists;
    }

}
