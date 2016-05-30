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
import org.openo.sdno.cbb.sdnwan.util.constans.ResultConstants;
import org.openo.sdno.cbb.sdnwan.util.rest.RestRsp;
import org.openo.sdno.exception.ErrorCode;
import org.openo.sdno.rest.ResponseUtils;
import org.openo.sdno.result.Result;
import org.openo.sdno.svc.vpn.l3vpn.services.inf.L3vpnSbiService;
import org.openo.sdno.vpn.wan.networkmodel.NetModel;
import org.openo.sdno.vpn.wan.networkmodel.servicel3vpn.L3vpn;
import org.openo.sdno.vpn.wan.networkmodel.servicel3vpn.L3vpnContainer;
import org.openo.sdno.vpn.wan.networkmodel.servicetypes.L3Ac;
import com.puer.framework.base.resthelper.RestfulProxy;
import com.puer.framework.base.util.JsonUtil;

public class DefaultL3vpnSbiService implements L3vpnSbiService {

    // timeout from svc to adapter
    static final int TIMEOUT = 150000;

    private L3VpnQueryService l3vpnQueryService;

    private AluL3vpnSbiService aluL3vpnSbiService;


    private static final String CONTENT_TYPE_NAME = "Content-Type";

    private static final String CONTENT_TYPE_VALUE = "application/json;charset=UTF-8";

    private static final String L3VPN_RESOURCE = "l3vpn";

    private static final String L3VPN_RELEASEBIND = "releaseBindVpn";

    private static final String L3VPNTP_RESOURCE = "l3vpnTp";

    private static final String BINDTP_RESOURCE = "bindTp";

    private static final String L3VPN_DESC = "desc";

    private static final String OPERSTATUS_RESOURCE = "operStatus";

    private static final String URL = "/rest/svc/sbiadp/controller/";

    private static final String ALU_CTRL_TYPE = "NSP";

    @Override
    public NetModel createL3vpn(final String ctrlUuid, final NetModel l3vpnContainer) throws ServiceException {
        if(ALU_CTRL_TYPE.equals(l3vpnQueryService.getCtrlType(ctrlUuid))) {
            return aluL3vpnSbiService.createL3vpn(ctrlUuid, ((L3vpnContainer)l3vpnContainer).getL3vpn());
        }
        final RestfulParametes restParametes = new RestfulParametes();
        restParametes.put("resource", L3VPN_RESOURCE);
        restParametes.put("isDryrun", "");

        restParametes.putHttpContextHeader(CONTENT_TYPE_NAME, CONTENT_TYPE_VALUE);
        final String jsonReq = RestTransferUtil.tansferRequest(l3vpnContainer);
        restParametes.setRawData(jsonReq);
        // Set timeout
        final RestfulOptions restfulOptions = new RestfulOptions();
        restfulOptions.setRestTimeout(TIMEOUT);
        final RestfulResponse response =
                RestfulProxy.post(URL + ctrlUuid + "/" + L3VPN_RESOURCE, restParametes, restfulOptions);

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

    /**
     * Bind terminate point to layer 3 VPN.
     * 
     * @since SDNO 0.5
     */
    @Override
    public NetModel createL3vpnBindTp(final String ctrlUuid, final String vpnId, final NetModel l3ac)
            throws ServiceException {
        if(ALU_CTRL_TYPE.equals(l3vpnQueryService.getCtrlType(ctrlUuid))) {
            return aluL3vpnSbiService.createL3vpnBindTp(ctrlUuid, vpnId, l3ac);
        }

        final RestfulParametes restParametes = new RestfulParametes();
        restParametes.put("resource", L3VPN_RESOURCE + "/" + BINDTP_RESOURCE);
        restParametes.put("isDryrun", "");

        restParametes.putHttpContextHeader(CONTENT_TYPE_NAME, CONTENT_TYPE_VALUE);
        final Map<String, String> paras = new HashMap<String, String>();
        paras.put("l3VpnId", vpnId);
        paras.put("data", RestTransferUtil.tansferRequest(l3ac));
        final String jsonReq = RestTransferUtil.tansferRequest(paras);
        restParametes.setRawData(jsonReq);
        // Set timeout
        final RestfulOptions restfulOptions = new RestfulOptions();
        restfulOptions.setRestTimeout(TIMEOUT);

        final RestfulResponse response =
                RestfulProxy.post(URL + ctrlUuid + "/" + L3VPN_RESOURCE + "/" + BINDTP_RESOURCE, restParametes,
                        restfulOptions);

        ResponseUtils.checkResonseAndThrowException(response);

        if(HttpStatusUtil.isSuccess(response.getStatus())) {
            final Result<L3Ac> rst =
                    JsonUtil.fromJson(response.getResponseContent(), new TypeReference<Result<L3Ac>>() {});
            if(rst.getErrcode() == ErrorCode.OPERATION_SUCCESS) {
                return rst.getResultObj();
            } else {
                throw sncFailedEx(rst.getErrcode());
            }
        } else {
            throw adapterFailedEx(response.getStatus());
        }
    }

    /**
     * Delete layer 3 VPN.
     * 
     * @since SDNO 0.5
     */
    @Override
    public void deleteL3vpn(final String ctrlUuid, final String uuid) throws ServiceException {

        if(ALU_CTRL_TYPE.equals(l3vpnQueryService.getCtrlType(ctrlUuid))) {
            aluL3vpnSbiService.deleteL3vpn(ctrlUuid, uuid);
            return;
        }

        final RestfulParametes restParametes = new RestfulParametes();
        restParametes.put("resource", L3VPN_RESOURCE);
        restParametes.put("isDryrun", "");
        restParametes.put("uuid", uuid);
        // Set timeout
        final RestfulOptions restfulOptions = new RestfulOptions();
        restfulOptions.setRestTimeout(TIMEOUT);
        final RestfulResponse response =
                RestfulProxy.delete(URL + ctrlUuid + "/" + L3VPN_RESOURCE, restParametes, restfulOptions);

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

    /**
     * Unbind terminate point from layer 3 VPN.
     * 
     * @since SDNO 0.5
     */
    @Override
    public void deleteL3vpnBindTp(final String ctrlUuid, final String vpnId, final String tpId) throws ServiceException {

        if(ALU_CTRL_TYPE.equals(l3vpnQueryService.getCtrlType(ctrlUuid))) {
            aluL3vpnSbiService.deleteL3vpnBindTp(ctrlUuid, vpnId, tpId);
            return;
        }

        final RestfulParametes restParametes = new RestfulParametes();
        restParametes.put("resource", L3VPN_RESOURCE + "/" + BINDTP_RESOURCE);
        restParametes.put("isDryrun", "");
        restParametes.put("l3VpnId", vpnId);
        restParametes.put("tpId", tpId);
        restParametes.putHttpContextHeader(CONTENT_TYPE_NAME, CONTENT_TYPE_VALUE);
        // Set timeout
        final RestfulOptions restfulOptions = new RestfulOptions();
        restfulOptions.setRestTimeout(TIMEOUT);
        final RestfulResponse response =
                RestfulProxy.delete(URL + ctrlUuid + "/" + L3VPN_RESOURCE + "/" + BINDTP_RESOURCE, restParametes,
                        restfulOptions);

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

    /**
     * Get operation status.
     * 
     * @since SDNO 0.5
     */
    @Override
    public L3vpn getOperStatus(final String ctrlUuid, final String uuid) throws ServiceException {

        if(ALU_CTRL_TYPE.equals(l3vpnQueryService.getCtrlType(ctrlUuid))) {
            return aluL3vpnSbiService.getOperStatus(ctrlUuid, uuid);
        }

        final RestfulParametes restParametes = new RestfulParametes();
        restParametes.put("resource", L3VPN_RESOURCE + "/" + OPERSTATUS_RESOURCE);
        restParametes.put("isDryrun", "");
        restParametes.put("uuid", uuid);
        restParametes.putHttpContextHeader(CONTENT_TYPE_NAME, CONTENT_TYPE_VALUE);
        // Set timeout
        final RestfulOptions restfulOptions = new RestfulOptions();
        restfulOptions.setRestTimeout(TIMEOUT);
        final RestfulResponse response =
                RestfulProxy.get(URL + ctrlUuid + "/" + L3VPN_RESOURCE, restParametes, restfulOptions);

        ResponseUtils.checkResonseAndThrowException(response);

        if(HttpStatusUtil.isSuccess(response.getStatus())) {
            final Result<L3vpn> rst =
                    JsonUtil.fromJson(response.getResponseContent(), new TypeReference<Result<L3vpn>>() {});
            if(rst.getErrcode() == ErrorCode.OPERATION_SUCCESS) {
                final L3vpn l3vpn = rst.getResultObj();
                if(null != l3vpn) {
                    return l3vpn;
                }
            } else {
                throw sncFailedEx(rst.getErrcode());
            }
        } else {
            throw adapterFailedEx(response.getStatus());
        }
        return null;
    }

    /**
     * Release VPN.
     * 
     * @since SDNO 0.5
     */
    @Override
    public RestRsp<String> releaseBindVpn(final String ctrlUuid, final String deleteVniId) throws ServiceException {

        if(ALU_CTRL_TYPE.equals(l3vpnQueryService.getCtrlType(ctrlUuid))) {
            return aluL3vpnSbiService.releaseBindVpn(ctrlUuid, deleteVniId);
        }

        final RestfulParametes restParametes = new RestfulParametes();
        restParametes.putHttpContextHeader(CONTENT_TYPE_NAME, CONTENT_TYPE_VALUE);
        restParametes.setRawData(deleteVniId);
        // Set timeout
        final RestfulOptions restfulOptions = new RestfulOptions();
        restfulOptions.setRestTimeout(TIMEOUT);
        final RestfulResponse response =
                RestfulProxy.post(URL + ctrlUuid + "/" + L3VPN_RESOURCE + "/" + L3VPN_RELEASEBIND, restParametes,
                        restfulOptions);
        if(HttpStatusUtil.isSuccess(response.getStatus())) {
            final Result<String> rst =
                    JsonUtil.fromJson(response.getResponseContent(), new TypeReference<Result<String>>() {});
            if(ErrorCode.OPERATION_SUCCESS == (rst.getErrcode())) {
                return new RestRsp<String>(ResultConstants.SUCCESS);
            } else {
                throw sncFailedEx(rst.getErrcode());
            }
        } else {
            throw adapterFailedEx(response.getStatus());
        }
    }

    /**
     * Update layer 3 VPN description.
     * 
     * @since SDNO 0.5
     */
    @Override
    public NetModel updateL3vpnDesc(final String ctrlUuid, final NetModel l3vpn) throws ServiceException {

        if(ALU_CTRL_TYPE.equals(l3vpnQueryService.getCtrlType(ctrlUuid))) {
            return aluL3vpnSbiService.updateL3vpnDesc(ctrlUuid, l3vpn);
        }

        final RestfulParametes restParametes = new RestfulParametes();
        restParametes.put("resource", L3VPN_RESOURCE);
        restParametes.put("isDryrun", "");
        restParametes.putHttpContextHeader(CONTENT_TYPE_NAME, CONTENT_TYPE_VALUE);
        final String jsonReq = RestTransferUtil.tansferRequest(l3vpn);
        restParametes.setRawData(jsonReq);
        // Set timeout
        final RestfulOptions restfulOptions = new RestfulOptions();
        restfulOptions.setRestTimeout(TIMEOUT);
        final RestfulResponse response =
                RestfulProxy.put(URL + ctrlUuid + "/" + L3VPN_RESOURCE + "/" + L3VPN_DESC, restParametes,
                        restfulOptions);
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

    /**
     * Update layer 3 VPN description.
     * 
     * @since SDNO 0.5
     */
    @Override
    public void updateL3vpnDesc(final String ctrlUuid, final String vpnId, final String desc) throws ServiceException {
        final L3vpn l3vpn = new L3vpn();
        l3vpn.setId(vpnId);
        l3vpn.setUserLabel(desc);
        updateL3vpnDesc(ctrlUuid, l3vpn);
    }

    public void setL3vpnQueryService(L3VpnQueryService l3vpnQueryService) {
        this.l3vpnQueryService = l3vpnQueryService;
    }

    public void setAluL3vpnSbiService(AluL3vpnSbiService aluL3vpnSbiService) {
        this.aluL3vpnSbiService = aluL3vpnSbiService;
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
}
