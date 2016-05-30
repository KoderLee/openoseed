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
import java.util.List;

import javax.annotation.Resource;

import org.openo.sdno.remoteservice.exception.ServiceException;
import org.openo.sdno.roa.common.HttpContext;
import org.openo.sdno.cbb.sdnwan.dao.VpnDao;
import org.openo.sdno.cbb.sdnwan.util.rest.RestRsp;
import org.openo.sdno.svc.vpn.l2vpn.common.constant.ErrorCode;
import org.openo.sdno.svc.vpn.l2vpn.common.constant.ErrorInfor;
import org.openo.sdno.svc.vpn.l2vpn.common.util.ServiceExceptionUtil;
import org.openo.sdno.svc.vpn.l2vpn.service.inf.AbstractL2vpnSbiService;
import org.openo.sdno.svc.vpn.l2vpn.service.inf.IL2vpnBussiController;
import org.openo.sdno.svc.vpn.l2vpn.service.inf.IL2vpnBussiValidate;
import org.openo.sdno.vpn.wan.networkmodel.serviceeline.L2vpnEline;
import org.openo.sdno.vpn.wan.servicemodel.composedvpn.VpnAndCreatePolicy;
import org.openo.sdno.vpn.wan.servicemodel.tp.Tp;
import org.openo.sdno.vpn.wan.servicemodel.vpn.Vpn;

/**
 * The realization of L2Vpn service, achieve crud functions of Vpn.<br/>
 * 
 * @author
 * @version SDNO 0.5 2016-3-16
 */
public class L2vpnBussiControllerImpl implements IL2vpnBussiController {

    @Resource
    private VpnDao vpnDao;

    @Resource
    private CommonOper commonOper;

    @Resource(name = "L2vpnBussiValidateImpl")
    private IL2vpnBussiValidate iL2vpnBussiValidate;

    @Resource(name = "L2vpnSbiServiceImpl")
    private AbstractL2vpnSbiService abstractL2vpnSbiService;

    @Resource(name = "TemplateCreate")
    private TemplateCreate templateCreate;

    @Resource(name = "TemplateDelete")
    private TemplateDelete templateDelete;

    public AbstractL2vpnSbiService getAbstractL2vpnSbiService() {
        return abstractL2vpnSbiService;
    }

    public void setAbstractL2vpnSbiService(final AbstractL2vpnSbiService abstractL2vpnSbiService) {
        this.abstractL2vpnSbiService = abstractL2vpnSbiService;
    }

    public IL2vpnBussiValidate getiL2vpnBussiValidate() {
        return iL2vpnBussiValidate;
    }

    public void setiL2vpnBussiValidate(final IL2vpnBussiValidate iL2vpnBussiValidate) {
        this.iL2vpnBussiValidate = iL2vpnBussiValidate;
    }

    public TemplateCreate getTemplateCreate() {
        return templateCreate;
    }

    public void setTemplateCreate(final TemplateCreate templateCreate) {
        this.templateCreate = templateCreate;
    }

    public TemplateDelete getTemplateDelete() {
        return templateDelete;
    }

    public void setTemplateDelete(final TemplateDelete templateDelete) {
        this.templateDelete = templateDelete;
    }

    public void setVpnDao(final VpnDao vpnDao) {
        this.vpnDao = vpnDao;
    }

    public void setCommonOper(final CommonOper commonOper) {
        this.commonOper = commonOper;
    }

    @Override
    public RestRsp<Vpn> createVpn(final VpnAndCreatePolicy vpnAndPolicy, final HttpContext httpContext)
            throws ServiceException {
        iL2vpnBussiValidate.validateCreate(vpnAndPolicy);

        return templateCreate.create(vpnAndPolicy, abstractL2vpnSbiService);
    }

    @Override
    public RestRsp<Vpn> deleteVpn(final String vpnUuid, final HttpContext httpContext) throws ServiceException {
        RestRsp<Vpn> validRsp = new RestRsp<Vpn>();
        try {
            validRsp = iL2vpnBussiValidate.validateDelete(vpnUuid);
        } catch(final ServiceException e) {
            return ServiceExceptionUtil.getFailRspByDetailExp(e);
        }

        RestRsp<Vpn> rsp = new RestRsp<Vpn>();
        try {
            rsp = templateDelete.delete(validRsp.getData(), abstractL2vpnSbiService);
        } catch(final ServiceException e) {
            return ServiceExceptionUtil.getOtherExpRsp(e.getId(), e.getMessage());
        }

        return rsp;
    }

    @Override
    public RestRsp<List<Vpn>> getVpns(final HttpContext httpContext) throws ServiceException {
        List<Vpn> vpns = new ArrayList<Vpn>();
        try {
            vpns = vpnDao.getAllMo();
        } catch(final ServiceException e) {
            return ServiceExceptionUtil.getOtherExpRsp(e.getId(), e.getMessage());
        }
        final List<Vpn> reList = commonOper.filterL2vpn(vpns);
        return ServiceExceptionUtil.getSuccesRestRsp(reList);
    }

    @Override
    public RestRsp<List<Vpn>> getVpnsByUuid(final String uuid, final HttpContext httpContext) throws ServiceException {
        Vpn vpn = null;
        try {
            vpn = vpnDao.queryById(uuid);
        } catch(final ServiceException e) {
            return ServiceExceptionUtil.getOtherExpRsp(e.getId(), e.getMessage());
        }
        final List<Vpn> reList = new ArrayList<Vpn>();
        if(vpn != null) {
            reList.add(vpn);
        }
        return ServiceExceptionUtil.getSuccesRestRsp(reList);
    }

    @Override
    public RestRsp<String> getStatusByUuid(final String uuid, final HttpContext httpContext) throws ServiceException {
        return commonOper.getStatusByUuid(uuid, httpContext);
    }

    @Override
    public RestRsp<List<Vpn>> getVpnsByTp(final List<String> tpUuidList, final HttpContext httpContext)
            throws ServiceException {
        List<Vpn> vpns = new ArrayList<Vpn>();
        try {
            vpns = vpnDao.getVpnsByTpIds(tpUuidList);
        } catch(final ServiceException e) {
            return ServiceExceptionUtil.getOtherExpRsp(e.getId(), e.getMessage());
        }
        final List<Vpn> reList = commonOper.filterL2vpn(vpns);
        return ServiceExceptionUtil.getSuccesRestRsp(reList);
    }

    @Override
    public RestRsp<Vpn> modifyDescription(final String uuid, final String desc, final HttpContext httpContext)
            throws ServiceException {
        Vpn vpn = null;
        vpn = vpnDao.getMoById(uuid);
        if(vpn == null) {
            return ServiceExceptionUtil.getBadRequestRsp(ErrorInfor.VPNOBJECT_NULL);
        }
        final String ctrlUuid = vpn.getControllerId();
        final L2vpnEline southL2vpn = new L2vpnEline();
        southL2vpn.setId(uuid);
        southL2vpn.setUserLabel(desc);
        final RestRsp<L2vpnEline> rsp = abstractL2vpnSbiService.updateL2Vpn(ctrlUuid, southL2vpn);

        if(rsp.getResult() == ErrorCode.RESULT_SUCCESS) {
            vpnDao.updateDescription(uuid, desc);
        }
        return new RestRsp<Vpn>(ErrorCode.RESULT_SUCCESS, vpn);
    }
}
