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
package org.openo.sdno.svc.vpn.l2vpn.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.NotImplementedException;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import org.openo.sdno.remoteservice.exception.ServiceException;
import org.openo.sdno.roa.util.restclient.RestfulOptions;
import org.openo.sdno.roa.util.restclient.RestfulParametes;
import org.openo.sdno.roa.util.restclient.RestfulResponse;
import org.openo.sdno.rpc.util.RestTransferUtil;
import org.openo.sdno.cbb.sdnwan.util.rest.RestRsp;
import org.openo.sdno.rest.ResponseUtils;
import org.openo.sdno.svc.vpn.l2vpn.common.constant.ErrorCode;
import org.openo.sdno.svc.vpn.l2vpn.common.constant.L2vpnSbiConstant;
import org.openo.sdno.svc.vpn.l2vpn.common.util.ServiceExceptionUtil;
import org.openo.sdno.svc.vpn.l2vpn.service.inf.AbstractL2vpnSbiService;
import org.openo.sdno.vpn.wan.networkmodel.serviceeline.L2vpnEline;
import org.openo.sdno.vpn.wan.networkmodel.servicetypes.Ac;
import org.openo.sdno.vpn.wan.servicemodel.composedvpn.VpnAndCreatePolicy;
import org.openo.sdno.vpn.wan.servicemodel.tp.Tp;
import org.openo.sdno.vpn.wan.servicemodel.vpn.Vpn;
import com.puer.framework.base.resthelper.RestfulProxy;
import com.puer.framework.base.util.JsonUtil;

/**
 * The implementation of L2VPN south service.<br/>
 * 
 * @author
 * @version SDNO 0.5 2016-3-16
 */
@Service("L2vpnSbiServiceImpl")
public class L2vpnSbiServiceImpl implements AbstractL2vpnSbiService {

    static final int TIMEOUT = 150000;

    private String translateResult(final RestfulResponse response, final String operInfor) throws ServiceException {
        if((response != null) && (response.getStatus() != ErrorCode.ERROR_HTTP_STATUS_OK_TETURN)) {
            return JsonUtil.toJson(ServiceExceptionUtil.getAdaptorLayerInnerExpRsp(response,
                    "Adaptor Layer handle error for l2vpn " + operInfor));
        }
        String jsonStr = "";
        if(null != response) {
            jsonStr = response.getResponseContent();
        }
        return jsonStr;
    }

    /**
     * Create eline.
     * 
     * @since SDNO 0.5
     */
    @Override
    public RestRsp<L2vpnEline> createEline(final String cltUuid, final L2vpnEline l2vpn) throws ServiceException {
        final RestfulParametes restParametes = new RestfulParametes();
        restParametes.putHttpContextHeader(L2vpnSbiConstant.CONTENT_TYPE_NAME, L2vpnSbiConstant.CONTENT_TYPE_VALUE);
        final String strJsonReq = RestTransferUtil.tansferRequest(l2vpn);
        restParametes.setRawData(strJsonReq);
        final RestfulOptions restfulOptions = new RestfulOptions();
        restfulOptions.setRestTimeout(TIMEOUT);
        RestfulResponse response = null;
        response =
                RestfulProxy.post(L2vpnSbiConstant.URL + cltUuid + "/" + L2vpnSbiConstant.MODULE_L2VPN + "/eline"
                        + getScene() + "?contenttype=" + getCtrlContentType(), restParametes, restfulOptions);
        ResponseUtils.checkResonseAndThrowException(response);
        final String jsonStr = translateResult(response, "create eline");
        final RestRsp<L2vpnEline> rsp = JsonUtil.fromJson(jsonStr, new TypeReference<RestRsp<L2vpnEline>>() {});
        ServiceExceptionUtil.checkFailRsp(rsp);
        return rsp;
    }

    /**
     * Delete eline.
     * 
     * @since SDNO 0.5
     */
    @Override
    public RestRsp<L2vpnEline> deleteEline(final String cltUuid, final L2vpnEline l2vpn) throws ServiceException {
        final RestfulParametes restParametes = new RestfulParametes();
        restParametes.putHttpContextHeader(L2vpnSbiConstant.CONTENT_TYPE_NAME, L2vpnSbiConstant.CONTENT_TYPE_VALUE);
        RestfulResponse response = null;
        final RestfulOptions restfulOptions = new RestfulOptions();
        restfulOptions.setRestTimeout(TIMEOUT);

        try {
            response =
                    RestfulProxy.delete(
                            L2vpnSbiConstant.URL + cltUuid + "/" + L2vpnSbiConstant.MODULE_L2VPN + "/eline"
                                    + getScene() + "?contenttype=" + getCtrlContentType() + "&serviceid="
                                    + l2vpn.getServiceId(), restParametes, restfulOptions);
        } catch(final ServiceException e) {
            return ServiceExceptionUtil.getInvokeExpRsp(e.getId(), e.getMessage());
        }

        final String jsonStr = translateResult(response, "delete eline");
        final RestRsp<String> rsp = JsonUtil.fromJson(jsonStr, new TypeReference<RestRsp<String>>() {});
        ServiceExceptionUtil.checkFailRsp(rsp);

        RestRsp<L2vpnEline> reRsp = null;
        reRsp = new RestRsp<L2vpnEline>(rsp.getResult(), rsp.getMessage());
        reRsp.setData(l2vpn);
        reRsp.setExceptionArg(rsp.getExceptionArg());
        return reRsp;
    }

    @Override
    public RestRsp<String> depolyCommit(final String ctrlUuid) {
        return ServiceExceptionUtil.getSuccesRestRsp(ctrlUuid);
    }

    @Override
    public RestRsp<String> depolyRollback(final String ctrlUuid) {
        return ServiceExceptionUtil.getSuccesRestRsp(ctrlUuid);
    }

    @Override
    public L2vpnEline bussi2South4Create(final VpnAndCreatePolicy vpnAndCreatePolicy) {
        return Bussi2SouthConvert.bussi2South4Create(vpnAndCreatePolicy, null);
    }

    @Override
    public L2vpnEline bussi2South4Del(final Vpn vpn) {
        return Bussi2SouthConvert.bussi2South4Del(vpn);
    }

    @Override
    public L2vpnEline bussi2South4Status(final Vpn vpn) {
        return Bussi2SouthConvert.bussi2South4Status(vpn);
    }

    @Override
    public Ac bussi2SouthAc4TpStatus(final Tp tp) {
        return Bussi2SouthConvert.bussi2SouthAc4TpStatus(tp);
    }

    @Override
    public String getCtrlContentType() {
        return MediaType.APPLICATION_JSON_TYPE.getSubtype();
    }

    @Override
    public String getScene() {
        // The default scene returns an empty string, corresponding to the
        // adaptor resource is "eline", "ifcar" and so on.
        return L2vpnSbiConstant.SCENE_DEFULT;
    }
}
