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
package org.openo.sdno.svc.vpn.l3vpn.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import org.openo.sdno.remoteservice.exception.ServiceException;
import org.openo.sdno.roa.util.restclient.RestfulParametes;
import org.openo.sdno.roa.util.restclient.RestfulResponse;
import org.openo.sdno.cbb.sdnwan.dao.InvTerminationPointMoDao;
import org.openo.sdno.cbb.sdnwan.dao.NetworkElementDao;
import org.openo.sdno.cbb.sdnwan.util.constans.ResultConstants;
import org.openo.sdno.cbb.sdnwan.util.rest.RestRsp;
import org.openo.sdno.inventory.sdk.model.TerminationPointMO;
import org.openo.sdno.rest.ResponseUtils;
import org.openo.sdno.result.Result;
import org.openo.sdno.svc.vpn.l3vpn.services.inf.L3vpnTeService;
import org.openo.sdno.vpn.wan.networkmodel.servicetypes.Path;
import org.openo.sdno.vpn.wan.networkmodel.servicetypes.PathList;
import org.openo.sdno.vpn.wan.networkmodel.servicetypes.ServicePath;
import org.openo.sdno.vpn.wan.servicemodel.tepath.ServiceTePath;
import org.openo.sdno.vpn.wan.servicemodel.tepath.TePath;
import org.openo.sdno.vpn.wan.servicemodel.tepath.TePathQueryKey;
import com.puer.framework.base.resthelper.RestfulProxy;
import com.puer.framework.base.util.JsonUtil;

public class DefaultL3vpnTeService implements L3vpnTeService {


    private static final String L3VPNTE_RESOURCE = "l3vpn/te";

    private static final String URL = "/rest/svc/sbiadp/controller/";

    @Resource
    private InvTerminationPointMoDao invTerminationPointMoDao;

    @Autowired
    private NetworkElementDao networkElementDao;

    /**
     * Get layer 3 VPN traffic engineering.
     * 
     * @since SDNO 0.5
     */
    @Override
    public RestRsp<List<TePath>> getL3vpnTePath(final TePathQueryKey queryKey) throws ServiceException {
        // Get controller UUID
        final String ctrluuid = networkElementDao.getCtrlUuidFromNe(queryKey.getSrcNeId());
        final RestfulParametes restParametes = new RestfulParametes();

        final Map<String, String> params = queryKey.getParams();
        params.put("resource", L3VPNTE_RESOURCE);
        restParametes.setParamMap(params);

        final String urlPath = URL + ctrluuid + "/" + L3VPNTE_RESOURCE;

        final RestfulResponse response;
        final RestRsp<List<TePath>> svcResp = new RestRsp<List<TePath>>(ResultConstants.SUCCESS);

        response = RestfulProxy.get(urlPath, restParametes);
        ResponseUtils.checkResonseAndThrowException(response);

        final Result<ServicePath> result =
                JsonUtil.fromJson(response.getResponseContent(), new TypeReference<Result<ServicePath>>() {});

        svcResp.setData(convertTePath(result.getResultObj()));

        return svcResp;
    }

    private List<TePath> convertTePath(final ServicePath servicePath) throws ServiceException {
        final List<TePath> tePathList = new ArrayList<TePath>();

        if(servicePath == null || servicePath.getPathLists() == null) {
            return tePathList;
        }
        final List<PathList> pathList = servicePath.getPathLists();

        for(int i = 0; i < pathList.size(); i++) {
            final TePath tePath = new TePath();
            tePath.setPathRole(pathList.get(i).getPathRole());
            tePath.setPathStatus(pathList.get(i).getPathStatus());
            tePath.setPathList(getServicePath(pathList.get(i).getPaths()));
            tePathList.add(tePath);
        }

        return tePathList;
    }

    private void updateEgressTpInfo(final String egressTpId, final Map<String, TerminationPointMO> mapTpIdToTp,
            final ServiceTePath servicePath) {
        if(mapTpIdToTp.containsKey(egressTpId)) {
            final TerminationPointMO tpMo = mapTpIdToTp.get(egressTpId);
            servicePath.setNeId(tpMo.getNe().getUuid());

            servicePath.setNeName(tpMo.getNe().getName());

            servicePath.setEgressTpName(tpMo.getName());
        }

        servicePath.setEgressTpId(egressTpId);
    }

    private void updateIngressTpInfo(final String ingressTpId, final Map<String, TerminationPointMO> mapTpIdToTp,
            final ServiceTePath servicePath) {
        if(mapTpIdToTp.containsKey(ingressTpId)) {
            final TerminationPointMO tpMo = mapTpIdToTp.get(ingressTpId);
            servicePath.setNeId(tpMo.getNe().getUuid());

            servicePath.setNeName(tpMo.getNe().getName());

            servicePath.setIngressTpName(tpMo.getName());
        }

        servicePath.setIngressTpId(ingressTpId);
    }

    private List<ServiceTePath> getServicePath(final List<Path> pathList) throws ServiceException {
        final List<String> tpIds = new ArrayList<String>();
        if(CollectionUtils.isEmpty(pathList)) {
            throw new ServiceException("getL3vpnTePath getServicePath pathList is Empty");
        }

        for(final Path path : pathList) {
            if(path.getIngressLtpId() != null) {
                tpIds.add(path.getIngressLtpId());
            }
            if(path.getEgressLtpId() != null) {
                tpIds.add(path.getEgressLtpId());
            }
        }

        // Query TP info from inventory
        final Result<List<TerminationPointMO>> tpList = invTerminationPointMoDao.query(tpIds);

        final Map<String, TerminationPointMO> mapTpIdToTp = new HashMap<String, TerminationPointMO>();
        for(final TerminationPointMO tpMo : tpList.getResultObj()) {
            mapTpIdToTp.put(tpMo.getUuid(), tpMo);
        }

        final List<ServiceTePath> servicePaList = new ArrayList<ServiceTePath>();
        for(final Path path : pathList) {
            final ServiceTePath servicePath = new ServiceTePath();
            if(path.getEgressLtpId() != null) {
                updateEgressTpInfo(path.getEgressLtpId(), mapTpIdToTp, servicePath);
            }
            if(path.getIngressLtpId() != null) {
                updateIngressTpInfo(path.getIngressLtpId(), mapTpIdToTp, servicePath);
            }

            servicePaList.add(servicePath);
        }

        return servicePaList;
    }
}
