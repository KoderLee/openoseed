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

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.type.TypeReference;

import org.openo.sdno.remoteservice.exception.ServiceException;
import org.openo.sdno.roa.util.restclient.RestfulOptions;
import org.openo.sdno.roa.util.restclient.RestfulParametes;
import org.openo.sdno.roa.util.restclient.RestfulResponse;
import org.openo.sdno.rpc.util.RestTransferUtil;
import org.openo.sdno.cbb.sdnwan.util.HttpStatusUtil;
import org.openo.sdno.cbb.sdnwan.util.rest.RestRsp;
import org.openo.sdno.exception.ErrorCode;
import org.openo.sdno.rest.ResponseUtils;
import org.openo.sdno.result.Result;
import org.openo.sdno.svc.vpn.l3vpn.services.inf.L3vpnSbiService;
import org.openo.sdno.vpn.wan.networkmodel.NetModel;
import org.openo.sdno.vpn.wan.networkmodel.servicel3vpn.L3vpn;
import com.puer.framework.base.resthelper.RestfulProxy;
import com.puer.framework.base.util.JsonUtil;

public class AluL3vpnSbiService implements L3vpnSbiService {

    static final int TIMEOUT = 150000;

    private static final String CONTENT_TYPE_NAME = "Content-Type";

    private static final String CONTENT_TYPE_VALUE = "application/json;charset=UTF-8";

    private static final String L3VPN_RESOURCE = "sd_utn_service_l3vpn:service-l3vpn/l3vpns";

    private static final String URL = "/rest/svc/sbiadp/config/";

    /**
     * Create layer 3 VPN.
     * 
     * @since SDNO 0.5
     */
    @Override
    @SuppressWarnings("unchecked")
    public NetModel createL3vpn(String ctrlUuid, final NetModel l3vpn) throws ServiceException {
        final RestfulParametes restParametes = new RestfulParametes();

        restParametes.putHttpContextHeader(CONTENT_TYPE_NAME, CONTENT_TYPE_VALUE);
        final Map<String, NetModel> paras = new HashMap<String, NetModel>();
        paras.put("l3vpn", l3vpn);
        final String jsonReq = RestTransferUtil.tansferRequest(paras);
        restParametes.setRawData(jsonReq);
        // Set timeout
        final RestfulOptions restfulOptions = new RestfulOptions();
        restfulOptions.setRestTimeout(TIMEOUT);
        final RestfulResponse response =
                RestfulProxy.post(URL + ctrlUuid + "/" + L3VPN_RESOURCE, restParametes, restfulOptions);

        return postRep(response);
    }

    /**
     * Update layer 3 VPN description.
     * 
     * @since SDNO 0.5
     */
    @Override
    @SuppressWarnings("unchecked")
    public NetModel updateL3vpnDesc(final String ctrlUuid, final NetModel l3vpn) throws ServiceException {
        final L3vpn l3vpnInstance = new L3vpn();
        l3vpnInstance.setId(((L3vpn)l3vpn).getId());
        l3vpnInstance.setUserLabel(((L3vpn)l3vpn).getUserLabel());
        return updateL3vpn(ctrlUuid, l3vpn);
    }

    /**
     * Delete layer 3 VPN.
     * 
     * @since SDNO 0.5
     */
    @Override
    public void deleteL3vpn(String ctrlUuid, String uuid) throws ServiceException {
        final RestfulParametes restParametes = new RestfulParametes();
        // Set timeout
        final RestfulOptions restfulOptions = new RestfulOptions();
        restfulOptions.setRestTimeout(TIMEOUT);
        final RestfulResponse response =
                RestfulProxy.delete(URL + ctrlUuid + "/" + L3VPN_RESOURCE + "/l3vpn/" + uuid, restParametes,
                        restfulOptions);
        postRepVoid(response);
    }

    /**
     * Create layer 3 VPN bind terminate point.
     * 
     * @since SDNO 0.5
     */
    @Override
    @SuppressWarnings("unchecked")
    public NetModel createL3vpnBindTp(final String ctrlUuid, final String vpnId, final NetModel l3ac)
            throws ServiceException {
        return null;
    }

    /**
     * Unbind layer 3 VPN bind terminate point.
     * 
     * @since SDNO 0.5
     */
    @Override
    public void deleteL3vpnBindTp(String ctrlUuid, String vpnId, String tpId) throws ServiceException {
        // To implement delete L3vpn.
    }

    /**
     * Update terminate point.
     * 
     * @since SDNO 0.5
     */
    @Override
    public void updateTp(final String ctrlUuid, final String vpnId, final NetModel l3ac) throws ServiceException {
        // To implement update L3vpn.
    }

    @Override
    public L3vpn getOperStatus(String ctrlUuid, String uuid) throws ServiceException {
        return (L3vpn)(this.getL3Vpn(ctrlUuid, uuid));
    }

    @Override
    public RestRsp<String> releaseBindVpn(String ctrlUuid, String deleteVniId) throws ServiceException {
        return null;
    }

    private NetModel getL3Vpn(final String ctrlUuid, final String uuid) throws ServiceException {
        final RestfulParametes restParametes = new RestfulParametes();
        restParametes.putHttpContextHeader(CONTENT_TYPE_NAME, CONTENT_TYPE_VALUE);
        // Set timeout
        final RestfulOptions restfulOptions = new RestfulOptions();
        restfulOptions.setRestTimeout(TIMEOUT);
        final RestfulResponse response =
                RestfulProxy.get(URL + ctrlUuid + "/" + L3VPN_RESOURCE + "/l3vpn/" + uuid, restParametes,
                        restfulOptions);
        return this.postRep(response);
    }

    private ServiceException adapterFailedEx(final int httpCode) {
        final ServiceException ex = new ServiceException();
        ex.setHttpCode(httpCode);
        return ex;
    }

    private ServiceException sncFailedEx(final int errorCode) {
        final ServiceException ex = new ServiceException();
        ex.setId("error code:" + errorCode);
        return ex;
    }

    private NetModel postRep(final RestfulResponse response) throws ServiceException {
        ResponseUtils.checkResonseAndThrowException(response);

        if(HttpStatusUtil.isSuccess(response.getStatus())) {
            final Result<L3vpn> rst =
                    JsonUtil.fromJson(response.getResponseContent(), new TypeReference<Result<L3vpn>>() {});
            if(rst.getErrcode() == ErrorCode.OPERATION_SUCCESS) {
                return rst.getResultObj();
            } else {
                throw sncFailedEx(rst.getErrcode());
            }
        } else {
            throw adapterFailedEx(response.getStatus());
        }
    }

    private void postRepVoid(final RestfulResponse response) throws ServiceException {
        ResponseUtils.checkResonseAndThrowException(response);

        if(HttpStatusUtil.isSuccess(response.getStatus())) {
            final Result<String> rst =
                    JsonUtil.fromJson(response.getResponseContent(), new TypeReference<Result<String>>() {});
            if(rst.getErrcode() == ErrorCode.OPERATION_SUCCESS) {
                return;
            } else {
                throw sncFailedEx(rst.getErrcode());
            }
        } else {
            throw adapterFailedEx(response.getStatus());
        }
    }

}
