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

import org.openo.crossdomain.servicemgr.dao.inf.IServiceModelDao;
import org.openo.crossdomain.servicemgr.model.roamo.OperationConstant;
import org.openo.crossdomain.servicemgr.model.servicemo.ServiceModel;
import org.openo.crossdomain.servicemgr.model.servicemo.ServiceOperation;
import org.openo.crossdomain.servicemgr.service.inf.IServiceOperationManager;
import org.openo.crossdomain.servicemgr.util.audit.OperationLog;
import org.springframework.util.StringUtils;

import org.openo.baseservice.biz.trail.AuditItem.AuditResult;
import org.openo.baseservice.biz.trail.AuditItem.LogSeverity;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.crossdomain.commsvc.jobscheduler.dao.inf.IJobDao;

/**
 * Query progress job for activating operation.<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
public class ActiveQueryProgressJob extends QueryProgressJob {

	/**
	 * 
	 * @see org.openo.crossdomain.servicemgr.job.impl.QueryProgressJob#handleComplete(org.openo.crossdomain.servicemgr.model.servicemo.ServiceModel)
	 */
    @Override
    public void handleComplete(ServiceModel serviceModel) {
        try {

            jobDao.delete(jobBean.getId());

            setSvcModelStatus(serviceModel, ServiceModel.STATUS_NUM.COMPLETED,
                    ServiceModel.ACTIVE_STATUS_NUM.ACTIVE_STATUS);

            ServiceOperation serviceOperation =
                    operationManager.getServiceOperationByID(serviceModel.getService_id(),
                            serviceModel.getOperation_id());
            operationManager.updateServiceOperation(serviceOperation, null,
                    OperationConstant.OPERATION_RESULT_NUM.SUCCESS, null);
            logger.info("ActiveQueryProgressJob : completed");

            OperationLog.writeLogString(OperationLog.OPER_TYPE.ACTIVE_TASK_OPERATION, serviceModel.getService_id(),
                    AuditResult.SUCCESS, serviceModel, LogSeverity.WARNING);
        } catch(ServiceException e) {
            logger.error("ActiveQueryProgressJob : update serviceModelDao failed, error {}", e);
        }
    }

    /**
     * 
     * @see org.openo.crossdomain.servicemgr.job.impl.QueryProgressJob#handleFailed(org.openo.crossdomain.servicemgr.model.servicemo.ServiceModel, java.lang.String)
     */
    @Override
    public void handleFailed(ServiceModel serviceModel, String reason) {

        try {

            jobDao.delete(jobBean.getId());

            setSvcModelStatus(serviceModel, ServiceModel.STATUS_NUM.FAILED, null);

            ServiceOperation serviceOperation =
                    operationManager.getServiceOperationByID(serviceModel.getService_id(),
                            serviceModel.getOperation_id());
            if(!StringUtils.hasLength(reason)) {
                reason = "serviceDecomposer active failed";
            }
            operationManager.updateServiceOperation(serviceOperation, null,
                    OperationConstant.OPERATION_RESULT_NUM.FAILED, reason);

            logger.info("ActiveQueryProgressJob : failed");

            OperationLog.writeLogString(OperationLog.OPER_TYPE.ACTIVE_TASK_OPERATION, serviceModel.getService_id(),
                    AuditResult.FAILURE, serviceModel, LogSeverity.WARNING);
        } catch(ServiceException e) {
            logger.error("ActiveQueryProgressJob : update serviceModelDao failed, error {}", e);
        }
    }

    private void setSvcModelStatus(ServiceModel serviceModel, String status, String activeStatus)
            throws ServiceException {
        ServiceModel toActiveServiceModel = new ServiceModel();

        toActiveServiceModel.setTenant_id(serviceModel.getTenant_id());
        toActiveServiceModel.setService_id(serviceModel.getService_id());
        toActiveServiceModel.setStatus(status);
        if(null != activeStatus) {
            toActiveServiceModel.setActive_status(activeStatus);
        }

        serviceModelDao.update(toActiveServiceModel);
    }

    /**
     * Set DAO.<br/>
     *
     * @param jobDao job DAO
     * @since crossdomain 0.5
     */
    public void setJobDao(IJobDao jobDao) {
        this.jobDao = jobDao;
    }

    /**
     * Set service model DAO.<br/>
     *
     * @param serviceModelDao service model dao
     * @since crossdomain 0.5
     */
    public void setServiceModelDao(IServiceModelDao serviceModelDao) {
        this.serviceModelDao = serviceModelDao;
    }

    /**
     * @param operationManager The operationManager to set.
     */
    public void setOperationManager(IServiceOperationManager operationManager) {
        this.operationManager = operationManager;
    }
}
