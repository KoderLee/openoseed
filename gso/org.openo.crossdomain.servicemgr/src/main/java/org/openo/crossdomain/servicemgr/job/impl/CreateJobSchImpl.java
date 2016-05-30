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
package org.openo.crossdomain.servicemgr.job.impl;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.openo.crossdomain.servicemgr.constant.Constant;
import org.openo.crossdomain.servicemgr.dao.inf.IServiceOperationDao;
import org.openo.crossdomain.servicemgr.exception.ErrorCode;
import org.openo.crossdomain.servicemgr.job.inf.ICreateJobSch;
import org.openo.crossdomain.servicemgr.model.roamo.ServiceModelInfo;
import org.openo.crossdomain.servicemgr.model.servicemo.ServiceModel;
import org.openo.crossdomain.servicemgr.model.servicemo.ServiceOperation;
import org.openo.crossdomain.servicemgr.restrepository.IServiceDecomposerProxy;
import org.openo.crossdomain.servicemgr.service.impl.job.JobFactory;
import org.openo.crossdomain.servicemgr.util.http.ResponseUtils;
import org.openo.crossdomain.servicemgr.util.validate.ValidateUtil;

import net.sf.json.JSONObject;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.crossdomain.commsvc.common.util.jsonutil.JsonUtil;
import org.openo.crossdomain.commsvc.jobscheduler.core.inf.IScheduler;
import org.openo.crossdomain.commsvc.jobscheduler.model.JobBean;

/**
 * Implementation for job scheduling.<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
public class CreateJobSchImpl implements ICreateJobSch {

    private IServiceDecomposerProxy svcDecomposerProxy;

    private IScheduler scheduler;

    private IServiceOperationDao serviceOperationDao;

    /**
     * 
     * @see org.openo.crossdomain.servicemgr.job.inf.ICreateJobSch#createServiceSch(ServiceModel, ServiceModelInfo, HttpServletRequest)
     */
    public void createServiceSch(final ServiceModel serviceModel, final ServiceModelInfo serviceModelInfo,
            HttpServletRequest httpRequest) throws ServiceException {
        RestfulResponse response = svcDecomposerProxy.createDecomposer(serviceModelInfo, serviceModel, httpRequest);
        ResponseUtils.checkResonseAndThrowException(response);

        JobBean job = JobFactory.createInitJob(serviceModel);
        setJobTypeAndAttr(job, "CREATEQUERY", response, serviceModel);

        scheduler.addNewJob(job);
    }

    /**
     * 
     * @see org.openo.crossdomain.servicemgr.job.inf.ICreateJobSch#updateServiceSch(ServiceModel, ServiceModelInfo, HttpServletRequest)
     */
    public void updateServiceSch(final ServiceModel serviceModel, final ServiceModelInfo serviceModelInfo,
            HttpServletRequest httpRequest) throws ServiceException {
        RestfulResponse response = svcDecomposerProxy.updateDecomposer(serviceModelInfo, serviceModel, httpRequest);
        ResponseUtils.checkResonseAndThrowException(response);

        JobBean job = JobFactory.createInitJob(serviceModel);
        setJobTypeAndAttr(job, "UPDATEQUERY", response, serviceModel);

        scheduler.addNewJob(job);
    }

    /**
     * 
     * @see org.openo.crossdomain.servicemgr.job.inf.ICreateJobSch#activeServiceSch(ServiceModel, HttpServletRequest)
     */
    public void activeServiceSch(final ServiceModel serviceModel, HttpServletRequest httpRequest)
            throws ServiceException {
        RestfulResponse response = svcDecomposerProxy.activeDecomposer(serviceModel, httpRequest);
        ResponseUtils.checkResonseAndThrowException(response);

        JobBean job = JobFactory.createInitJob(serviceModel);
        setJobTypeAndAttr(job, "ACTIVEQUERY", response, serviceModel);

        scheduler.addNewJob(job);
    }

    /**
     * 
     * @see org.openo.crossdomain.servicemgr.job.inf.ICreateJobSch#deactivServiceSch(ServiceModel, HttpServletRequest)
     */
    public void deactivServiceSch(final ServiceModel serviceModel, HttpServletRequest httpRequest)
            throws ServiceException {
        RestfulResponse response = svcDecomposerProxy.deactivDecomposer(serviceModel, httpRequest);
        ResponseUtils.checkResonseAndThrowException(response);

        JobBean job = JobFactory.createInitJob(serviceModel);
        setJobTypeAndAttr(job, "DEACTIVQUERY", response, serviceModel);

        scheduler.addNewJob(job);
    }

    /**
     * 
     * @see org.openo.crossdomain.servicemgr.job.inf.ICreateJobSch#delServiceSch(ServiceModel, HttpServletRequest)
     */
    public void delServiceSch(final ServiceModel serviceModel, HttpServletRequest httpRequest) throws ServiceException {
        RestfulResponse response = svcDecomposerProxy.deleteDecomposer(serviceModel, httpRequest);
        ResponseUtils.checkResonseAndThrowException(response);

        JobBean job = JobFactory.createInitJob(serviceModel);
        setJobTypeAndAttr(job, "DELETEQUERY", response, serviceModel);

        scheduler.addNewJob(job);
    }

    @SuppressWarnings("unchecked")
    private void setJobTypeAndAttr(JobBean job, String type, RestfulResponse response, ServiceModel serviceModel)
            throws ServiceException {
        if(null == response.getResponseContent()) {
            throw new ServiceException(ErrorCode.SVCMGR_SERVICEMGR_BAD_PARAM, "svcDecomposerProxy response  is null!");
        }

        JSONObject attributeJson = new JSONObject();

        Map<String, Object> resultData;

        resultData = JsonUtil.unMarshal(response.getResponseContent(), Map.class);

        Object location = resultData.get(Constant.LOCATION);
        ValidateUtil.assertNotEmpty(String.valueOf(location), Constant.LOCATION);

        attributeJson.put(Constant.LOCATION, location);

        job.setType(type);
        job.setAttribute(attributeJson.toString());

        ServiceOperation serviceOperation =
                serviceOperationDao.getServiceOperationByID(serviceModel.getService_id(),
                        serviceModel.getOperation_id());
        serviceOperation.setTask_uri(resultData.get(Constant.LOCATION).toString());
        serviceOperationDao.update(serviceOperation);
    }

    /**
     * Set service decomposer proxy.<br/>
     *
     * @param svcDecomposerProxy service decomposer proxy
     * @since crossdomain 0.5
     */
    public void setSvcDecomposerProxy(IServiceDecomposerProxy svcDecomposerProxy) {
        this.svcDecomposerProxy = svcDecomposerProxy;
    }

    /**
     * Set scheduler.<br/>
     *
     * @param scheduler
     * @since crossdomain 0.5
     */
    public void setScheduler(IScheduler scheduler) {
        this.scheduler = scheduler;
    }

    /**
     * Set service operation DAO.<br/>
     *
     * @param serviceOperationDao
     * @since crossdomain 0.5
     */
    public void setServiceOperationDao(IServiceOperationDao serviceOperationDao) {
        this.serviceOperationDao = serviceOperationDao;
    }

}
