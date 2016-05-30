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

import java.util.ArrayList;
import java.util.List;

import org.openo.sdno.remoteservice.exception.ServiceException;
import org.openo.sdno.roa.annotation.Consumes;
import org.openo.sdno.roa.annotation.GET;
import org.openo.sdno.roa.annotation.Path;
import org.openo.sdno.roa.annotation.Produces;
import org.openo.sdno.roa.annotation.QueryParam;
import org.openo.sdno.roa.annotation.Target;
import org.openo.sdno.roa.common.HttpContext;
import org.openo.sdno.cbb.sdnwan.util.constans.ResultConstants;
import org.openo.sdno.cbb.sdnwan.util.rest.RestRsp;
import org.openo.sdno.svc.vpn.l2vpn.service.inf.IL2vpnTeService;
import org.openo.sdno.vpn.wan.servicemodel.tepath.TePath;
import org.openo.sdno.vpn.wan.servicemodel.tepath.TePathList;
import org.openo.sdno.vpn.wan.servicemodel.tepath.TePathQueryKey;
import com.puer.framework.base.service.IResource;

/**
 * TE service API restful class in order to get TE paths.<br/>
 * 
 * @author
 * @version SDNO 0.5 2016-3-16
 */
@Path("/svc/l2vpn/v1/teservice")
@Target("sdno_l2vpn")
public class TeServiceApiController extends IResource<IL2vpnTeService> {

    @Override
    public String getResUri() {
        return "/svc/l2vpn/v1/teservice";
    }

    /**
     * Get TE path.<br/>
     * <p>
     * Build TePathQueryKey object by parameters,and query the L2vpnTePath by TePathQueryKey.<br/>
     * Add description for L2vpnTePath, and add all L2vpnTePath to the list.
     * </p>
     * 
     * @param vpnId The ID of VPN
     * @param srcNeId The ID of source NE
     * @param destNeId The ID of destination NE
     * @param srcAcId The ID of source AC
     * @param destAcId The ID of destination AC
     * @param httpContext HttpContext
     * @return The list of all L2vpnTePath that have been queried
     * @since SDNO 0.5
     */
    @GET
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public RestRsp<List<TePathList>> getTePath(@QueryParam("vpnId") final String vpnId,
            @QueryParam("srcNeId") final String srcNeId, @QueryParam("destNeId") final String destNeId,
            @QueryParam("srcAcId") final String srcAcId, @QueryParam("destAcId") final String destAcId,
            final HttpContext httpContext) {
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

        TePathQueryKey negTePathQueryKey = null;
        try {
            negTePathQueryKey = new TePathQueryKey(vpnId, destNeId, srcNeId, destAcId, srcAcId);
            final List<TePathList> negTePathLists = getTePathLists(negTePathQueryKey, "negative");
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

        final RestRsp<List<TePath>> rsp = service.getL2vpnTePath(queryKey);
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
