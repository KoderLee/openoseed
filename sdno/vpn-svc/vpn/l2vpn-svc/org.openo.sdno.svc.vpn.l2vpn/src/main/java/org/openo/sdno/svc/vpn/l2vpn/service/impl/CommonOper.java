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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import org.openo.sdno.remoteservice.exception.ServiceException;
import org.openo.sdno.roa.common.HttpContext;
import org.openo.sdno.roa.util.restclient.RestfulParametes;
import org.openo.sdno.roa.util.restclient.RestfulResponse;
import org.openo.sdno.rpc.util.RestTransferUtil;
import org.openo.sdno.cbb.sdnwan.dao.NetworkElementDao;
import org.openo.sdno.cbb.sdnwan.dao.VpnDao;
import org.openo.sdno.cbb.sdnwan.util.rest.RestRsp;
import org.openo.sdno.cbb.sdnwan.util.translater.TranslaterUtil;
import org.openo.sdno.inventory.sdk.model.ControllerMO;
import org.openo.sdno.rest.ResponseUtils;
import org.openo.sdno.svc.vpn.l2vpn.common.constant.ErrorCode;
import org.openo.sdno.svc.vpn.l2vpn.common.constant.L2vpnType;
import org.openo.sdno.svc.vpn.l2vpn.common.util.ServiceExceptionUtil;
import org.openo.sdno.vpn.wan.networkmodel.serviceeline.L2vpnEline;
import org.openo.sdno.vpn.wan.networkmodel.servicetypes.Ac;
import org.openo.sdno.vpn.wan.servicemodel.common.ServiceType;
import org.openo.sdno.vpn.wan.servicemodel.common.TechnologyType;
import org.openo.sdno.vpn.wan.servicemodel.common.TopologyType;
import org.openo.sdno.vpn.wan.servicemodel.tp.Tp;
import org.openo.sdno.vpn.wan.servicemodel.vpn.Vpn;
import com.puer.framework.base.resthelper.RestfulProxy;
import com.puer.framework.base.util.JsonUtil;

/**
 * General operation.<br/>
 * 
 * @author
 * @version SDNO 0.5 2016-3-16
 */
@Service("CommonOper")
public class CommonOper {

    @Resource
    protected NetworkElementDao networkElementDao;

    @Resource
    private VpnDao vpnDao;

    /**
     * Get control uuid by VPN.
     * 
     * @since SDNO 0.5
     */
    public String getCtrlUuidByVpn(final Vpn vpn) {
        String ctrlUuid = "";
        if(vpn == null) {
            return ctrlUuid;
        }
        final List<Tp> vpnTpLst = vpn.getVpnBasicInfo().getAccessPointList();
        String neUuid = "";
        for(final Tp tp : vpnTpLst) {
            neUuid = tp.getNeId();
            break;
        }
        final ControllerMO controller = networkElementDao.getControllerFromNe(neUuid);
        if(controller != null) {
            ctrlUuid = controller.getUuid();
        }
        return ctrlUuid;
    }

    /**
     * Get control uuid by terminate point.
     * 
     * @since SDNO 0.5
     */
    public String getCtrlUuidByTp(final Tp tp) {
        String ctrlUuid = "";
        if(tp == null) {
            return ctrlUuid;
        }
        final ControllerMO controller = networkElementDao.getControllerFromNe(tp.getNeId());
        if(controller != null) {
            ctrlUuid = controller.getUuid();
        }
        return ctrlUuid;
    }

    /**
     * Filter layer 2 VPN.
     * 
     * @since SDNO 0.5
     */
    public List<Vpn> filterL2vpn(final List<Vpn> vpns) {
        final List<Vpn> reList = new ArrayList<Vpn>();
        if(vpns != null) {
            for(final Vpn vpn : vpns) {
                if(ServiceType.L2VPN.getCommonName().equals(vpn.getVpnBasicInfo().getServiceType())
                        && TechnologyType.MPLS.getCommonName().equals(vpn.getVpnBasicInfo().getTechnology())) {
                    reList.add(vpn);
                }
            }
        }
        return reList;
    }

    /**
     * Get layer 2 VPN type.
     * 
     * @since SDNO 0.5
     */
    public String getL2vpnType(final Vpn vpn) {
        final String unknown = L2vpnType.UNKNOWN.getName();
        if(vpn == null) {
            return unknown;
        }
        final String topology = vpn.getVpnBasicInfo().getTopology();
        final String tecnoly = vpn.getVpnBasicInfo().getTechnology();
        final String type = vpn.getVpnBasicInfo().getServiceType();
        final List<Tp> tps = vpn.getVpnBasicInfo().getAccessPointList();
        if(ServiceType.L2VPN.getCommonName().equals(type) && TechnologyType.MPLS.getCommonName().equals(tecnoly)
                && TopologyType.POINT_TO_POINT.getCommonName().equals(topology) && (tps != null) && (tps.size() == 2)) {
            return L2vpnType.ELINE.getName();
        }
        return unknown;
    }

    /**
     * Get VPN datus by uuid.
     * 
     * @since SDNO 0.5
     */
    @SuppressWarnings("unchecked")
    public RestRsp<String> getStatusByUuid(final String uuid, final HttpContext httpContext) throws ServiceException {
        List<Vpn> vpns = null;
        Vpn vpn = null;

        vpns = vpnDao.getVpnAbstractInfo(Collections.singletonList(uuid));
        if(!CollectionUtils.isEmpty(vpns)) {
            vpn = vpns.get(0);
        } else {
            return ServiceExceptionUtil.getOtherExpRsp(String.valueOf(ErrorCode.ERROR_INVALID_PARAMETER_FAILED), "");
        }
        final String ctrluuid = vpn.getControllerId();
        final RestfulParametes restParametes = new RestfulParametes();
        restParametes.put("uuid", uuid);

        RestfulResponse response = null;
        response =
                RestfulProxy.get("/rest/svc/sbiadp/controller/" + ctrluuid + "/" + "l2vpn" + "/" + "vpnstatus",
                        restParametes);
        ResponseUtils.checkResonseAndThrowException(response);

        String status = "";
        if(null != response) {
            RestRsp<L2vpnEline> rsp = null;
            rsp = RestTransferUtil.transferResponse(response.getResponseContent(), RestRsp.class);

            String adminStatus = "";
            if((null != rsp) && (0 == rsp.getResult())) {
                final RestRsp<L2vpnEline> rst =
                        JsonUtil.fromJson(response.getResponseContent(), new TypeReference<RestRsp<L2vpnEline>>() {});
                if(rst.getResult() == ErrorCode.RESULT_SUCCESS) {
                    final L2vpnEline l2vpnEline = rst.getData();
                    if(null != l2vpnEline) {
                        status = TranslaterUtil.nTosOperStatu(l2vpnEline.getOperateStatus());
                        adminStatus = TranslaterUtil.nTosAdminStatu(l2vpnEline.getAdminStatus());

                        vpn.setOperStatus(status);
                        vpn.getVpnBasicInfo().setAdminStatus(adminStatus);

                        vpn = updateTpStatus(vpn, l2vpnEline);

                        vpnDao.updateStatus(Collections.singletonList(vpn));
                    }
                } else {
                    throw sncFailedEx(rst.getResult());
                }
            }

            status = status + ",";
            status = status + adminStatus;
        } else {
        }
        return ServiceExceptionUtil.getSuccesRestRsp(status);
    }

    /**
     * Get VPN by uuid.
     * 
     * @since SDNO 0.5
     */
    @SuppressWarnings("unchecked")
    public RestRsp<L2vpnEline> getVpnByUuid(final Vpn vpn) throws ServiceException {
        RestfulResponse response = null;
        try {
            if(null != vpn) {
                final String ctrluuid = vpn.getControllerId();
                final RestfulParametes restParametes = new RestfulParametes();
                restParametes.put("uuid", vpn.getUuid());
                response =
                        RestfulProxy.get("/rest/svc/sbiadp/controller/" + ctrluuid + "/" + "l2vpn" + "/" + "vpnstatus",
                                restParametes);
            }
        } catch(final ServiceException e) {
            return ServiceExceptionUtil.getOtherExpRsp(e.getId(), e.getMessage());
        }
        RestRsp<L2vpnEline> rsp = new RestRsp<L2vpnEline>();
        rsp.setResult(ErrorCode.ERROR_HTTP_STATUS_OK_TETURN);
        if(null != response) {
            rsp = RestTransferUtil.transferResponse(response.getResponseContent(), RestRsp.class);

            if((null != rsp) && (ErrorCode.RESULT_SUCCESS == rsp.getResult())) {
                final RestRsp<L2vpnEline> rst =
                        JsonUtil.fromJson(response.getResponseContent(), new TypeReference<RestRsp<L2vpnEline>>() {});
                if(rst.getResult() == ErrorCode.RESULT_SUCCESS) {
                    return rst;
                } else {
                    throw sncFailedEx(rst.getResult());
                }
            }
        }
        return rsp;
    }

    private ServiceException sncFailedEx(final int errorCode) {
        final ServiceException ex = new ServiceException();
        ex.setId("error code:" + errorCode);
        return ex;
    }

    private Vpn updateTpStatus(final Vpn vpn, final L2vpnEline l2vpnEline) {
        final List<Tp> tps = vpn.getVpnBasicInfo().getAccessPointList();
        final List<Ac> inAcs = l2vpnEline.getIngressAcs();
        final List<Ac> enAcs = l2vpnEline.getEgressAcs();

        final HashMap<String, Ac> acMap = new HashMap<String, Ac>();

        for(final Ac inAc : inAcs) {
            acMap.put(inAc.getId(), inAc);
        }

        for(final Ac enAc : enAcs) {
            acMap.put(enAc.getId(), enAc);
        }

        for(final Tp tp : tps) {
            final String tpId = tp.getId();
            if(acMap.containsKey(tpId)) {
                final Ac acInfo = acMap.get(tpId);
                tp.setOperStatus(TranslaterUtil.nTosOperStatu(acInfo.getOperateStatus()));
                tp.setAdminStatus(TranslaterUtil.nTosAdminStatu(acInfo.getAdminStatus()));
            }
        }

        vpn.getVpnBasicInfo().setAccessPointList(tps);
        return vpn;
    }

    public void setNetworkElementDao(final NetworkElementDao networkElementDao) {
        this.networkElementDao = networkElementDao;
    }

    public void setVpnDao(final VpnDao vpnDao) {
        this.vpnDao = vpnDao;
    }

}
