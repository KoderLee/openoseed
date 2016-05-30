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
package org.openo.crossdomain.servicemgr.service.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.openo.crossdomain.servicemgr.check.inf.IServiceChecker;
import org.openo.crossdomain.servicemgr.dao.inf.IServiceModelDao;
import org.openo.crossdomain.servicemgr.exception.ErrorCode;
import org.openo.crossdomain.servicemgr.job.inf.ICreateJobSch;
import org.openo.crossdomain.servicemgr.model.roamo.OperationConstant;
import org.openo.crossdomain.servicemgr.model.roamo.ServiceModelConstant;
import org.openo.crossdomain.servicemgr.model.roamo.ServiceModelInfo;
import org.openo.crossdomain.servicemgr.model.servicemo.ServiceDefInfo;
import org.openo.crossdomain.servicemgr.model.servicemo.ServiceModel;
import org.openo.crossdomain.servicemgr.model.servicemo.ServiceOperation;
import org.openo.crossdomain.servicemgr.model.servicemo.ServiceParamInfo;
import org.openo.crossdomain.servicemgr.model.servicemo.ServiceParameter;
import org.openo.crossdomain.servicemgr.model.servicemo.TemplateInfo;
import org.openo.crossdomain.servicemgr.model.servicemo.ServiceModel.ACTIVE_STATUS_NUM;
import org.openo.crossdomain.servicemgr.restrepository.ICatalogProxy;
import org.openo.crossdomain.servicemgr.restrepository.IServiceDecomposerProxy;
import org.openo.crossdomain.servicemgr.service.inf.IParameterManager;
import org.openo.crossdomain.servicemgr.service.inf.IServiceManager;
import org.openo.crossdomain.servicemgr.service.inf.IServiceOperationManager;
import org.openo.crossdomain.servicemgr.util.validate.ValidateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.openo.baseservice.remoteservice.exception.ServiceException;

import net.sf.json.JSONObject;

/**
 * Implementation for service manager.<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
public class ServiceManagerImpl implements IServiceManager {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * catalog
     */
    private ICatalogProxy catalogProxy;

    private IServiceModelDao serviceModelDao;

    private IServiceDecomposerProxy svcDecomposerProxy;

    private IServiceChecker serviceChecker;

    private ICreateJobSch createJobSch;

    private IParameterManager parameterManager;

    private IServiceOperationManager operationManager;

    /**
     * 
     * @see org.openo.crossdomain.servicemgr.service.inf.IServiceManager#getServiceModelByServiceID(String, String)
     */
    @Override
    public ServiceModel getServiceModelByServiceID(String tenantID, String serviceID) throws ServiceException {
        return serviceModelDao.getServiceModelByServiceId(tenantID, serviceID);
    }

    /**
     * 
     * @see org.openo.crossdomain.servicemgr.service.inf.IServiceManager#createServiceModel(ServiceModel, String, ServiceModelInfo, HttpServletRequest)
     */
    @Override
    public ServiceModel createServiceModel(ServiceModel serviceModel, String userName,
            ServiceModelInfo serviceModelInfo, HttpServletRequest httpRequest) throws ServiceException {
        List<ServiceParameter> serviceParameters = null;
        ServiceOperation serviceOperation = null;

        boolean success = false;
        boolean isOperServiceModelDbSucess = false;
        try {

            serviceChecker.checkCreate(serviceModel, httpRequest);

            ServiceDefInfo serviceDefInfo =
                    catalogProxy.getServiceDetail(serviceModel.getService_definition_id(), httpRequest);
            String serviceType = serviceDefInfo.getDomain();
            ValidateUtil.assertNotEmpty(serviceType, ServiceModelConstant.VALIDATE_FIELDS_NUM.SERVICE_TYPE);

            TemplateInfo templateInf = serviceDefInfo.getTemplateInfoByTempalteID(serviceModel.getTemplate_id());
            ValidateUtil.assertNotNull(templateInf, ErrorCode.SVCMGR_TEMPLATE_NOT_EXIST,
                    ServiceModelConstant.VALIDATE_FIELDS_NUM.TEMPLATE_MODEL);

            String templateUrl = templateInf.getTemplateUrl();
            ValidateUtil.assertNotEmpty(templateUrl, ServiceModelConstant.VALIDATE_FIELDS_NUM.TEMPLATE_URL);

            serviceModel.setTemplateUrl(templateUrl);
            serviceModel.setService_type(serviceType);
            serviceModel.setCreated_at(System.currentTimeMillis());
            serviceModel.setUpdate_time(System.currentTimeMillis());
            serviceModel.setActive_status(ACTIVE_STATUS_NUM.DEACTIVE_STATUS);
            serviceModel.setStatus(ServiceModel.STATUS_NUM.CREATE_IN_PROGRESS);

            serviceParameters =
                    parameterManager.getServiceParameter(serviceModel, serviceModelInfo, httpRequest)
                            .getParameterList();
            serviceModel.setParameters(serviceParameters);

            serviceOperation =
                    operationManager.createServiceOperationWithInProgress(userName, serviceModel, serviceParameters,
                            OperationConstant.OPERATION_NUM.CREATE);

            serviceModel.setOperation_id(serviceOperation.getOperation_id());

            serviceModelDao.insert(serviceModel);
            isOperServiceModelDbSucess = true;

            catalogProxy.createReference(serviceModel, httpRequest);

            createJobSch.createServiceSch(serviceModel, serviceModelInfo, httpRequest);
            success = true;
        } catch(ServiceException e) {

            operationManager.updateServiceOperation(serviceOperation, serviceParameters,
                    OperationConstant.OPERATION_RESULT_NUM.FAILED, e.getId());
            throw e;
        } finally {
            if(null != serviceModel && !success && isOperServiceModelDbSucess) {

                logger.error("createServiceModel error, serviceID is {}", serviceModel.getService_id());

                setSvcModelStatus(serviceModel, ServiceModel.STATUS_NUM.CREATE_FAILED, null);
            }
        }

        return serviceModel;
    }

    /**
     * 
     * @see org.openo.crossdomain.servicemgr.service.inf.IServiceManager#updateServiceModel(String, String, String, ServiceModelInfo, HttpServletRequest)
     */
    @Override
    public ServiceModel updateServiceModel(String updateTenantID, String updateServiceID, String updateUserName,
            ServiceModelInfo serviceModelInfo, HttpServletRequest httpRequest) throws ServiceException {
        ServiceModel serviceModel = null;
        List<ServiceParameter> serviceParams = null;
        ServiceParamInfo serviceParamInfo = null;
        ServiceOperation serviceOperation = null;
        boolean success = false;
        boolean isOperServiceModelDbSucess = false;
        try {

            ServiceModel serviceModelDB = serviceModelDao.getServiceModelByServiceId(updateTenantID, updateServiceID);
            ValidateUtil.assertNotNull(serviceModelDB, ErrorCode.SVCMGR_SERVICE_NOT_EXIST,
                    ServiceModelConstant.VALIDATE_FIELDS_NUM.SERVICE_MODEL);

            serviceChecker.checkUpdate(serviceModelDB, httpRequest);

            serviceModel = ServiceModel.toServiceModel(serviceModelInfo, updateServiceID);
            ValidateUtil.assertNotNull(serviceModel, ServiceModelConstant.VALIDATE_FIELDS_NUM.SERVICE_MODEL);
            serviceModel.updateServiceModel(serviceModelDB);

            ServiceDefInfo serviceDefInfo =
                    catalogProxy.getServiceDetail(serviceModel.getService_definition_id(), httpRequest);
            TemplateInfo templateInf = serviceDefInfo.getTemplateInfoByTempalteID(serviceModel.getTemplate_id());
            ValidateUtil.assertNotNull(templateInf, ErrorCode.SVCMGR_TEMPLATE_NOT_EXIST,
                    ServiceModelConstant.VALIDATE_FIELDS_NUM.TEMPLATE_MODEL);

            String templateUrl = templateInf.getTemplateUrl();
            ValidateUtil.assertNotEmpty(templateUrl, ServiceModelConstant.VALIDATE_FIELDS_NUM.TEMPLATE_URL);
            serviceModel.setTemplateUrl(templateUrl);

            serviceParamInfo = parameterManager.getServiceParameter(serviceModel, serviceModelInfo, httpRequest);

            serviceParams = serviceParamInfo.getParameterList();
            setNewParamToServiceModel(serviceModel, serviceParams);

            serviceOperation =
                    operationManager.createServiceOperationWithInProgress(updateUserName, serviceModel, serviceParams,
                            OperationConstant.OPERATION_NUM.UPDATE);
            serviceModel.setOperation_id(serviceOperation.getOperation_id());

            ServiceModel serviceModelStatus =
                    new ServiceModel(serviceModel.getTenant_id(), serviceModel.getService_id(),
                            ServiceModel.STATUS_NUM.UPDATE_IN_PROGRESS);
            serviceModelDao.update(serviceModelStatus);
            isOperServiceModelDbSucess = true;

            if(serviceParamInfo.isModify()
                    || (null != serviceModelInfo.getService() && null != serviceModelInfo.getService().getNsd_script())) {

                createJobSch.updateServiceSch(serviceModel, serviceModelInfo, httpRequest);
            }

            serviceModelDao.update(serviceModel);
            success = true;
        } catch(ServiceException e) {

            operationManager.updateServiceOperation(serviceOperation, serviceParams,
                    OperationConstant.OPERATION_RESULT_NUM.FAILED, e.getId());
            throw e;
        } finally {
            if(null != serviceModel && !success && isOperServiceModelDbSucess) {
                logger.error("updateServiceModel error, serviceID is {}", serviceModel.getService_id());

                setSvcModelStatus(serviceModel, ServiceModel.STATUS_NUM.FAILED, null);
            }

            if(null != serviceModel && null != serviceParamInfo && !serviceParamInfo.isModify() && success) {

                setSvcModelStatus(serviceModel, ServiceModel.STATUS_NUM.COMPLETED, null);
            }
        }

        return serviceModel;
    }

    /**
     * 
     * @see org.openo.crossdomain.servicemgr.service.inf.IServiceManager#activServiceModel(String, String, String, HttpServletRequest)
     */
    @Override
    public ServiceModel activServiceModel(String activeTenantID, String activeServiceID, String activeUserName,
            HttpServletRequest httpRequest) throws ServiceException {
        ServiceModel serviceModel = null;
        ServiceOperation serviceOperation = null;
        String oldActiveStatus = null;
        boolean success = false;
        boolean isOperServiceModelDbSucess = false;
        try {

            serviceModel = serviceModelDao.getServiceModelByServiceId(activeTenantID, activeServiceID);
            ValidateUtil.assertNotNull(serviceModel, ErrorCode.SVCMGR_SERVICE_NOT_EXIST,
                    ServiceModelConstant.VALIDATE_FIELDS_NUM.SERVICE_MODEL);

            serviceChecker.checkActive(serviceModel);

            oldActiveStatus = serviceModel.getActive_status();
            serviceModel.setUpdate_time(System.currentTimeMillis());
            serviceModel.setStatus(ServiceModel.STATUS_NUM.ACTIVATE_IN_PROGRESS);
            serviceModel.setActive_status(ServiceModel.ACTIVE_STATUS_NUM.ACTIVATE_IN_PROGRESS);
            serviceModel.setParameters(null);

            serviceOperation =
                    operationManager.createServiceOperationWithInProgress(activeUserName, serviceModel, null,
                            OperationConstant.OPERATION_NUM.ACTIVATE);
            serviceModel.setOperation_id(serviceOperation.getOperation_id());

            serviceModelDao.update(serviceModel);
            isOperServiceModelDbSucess = true;

            createJobSch.activeServiceSch(serviceModel, httpRequest);
            success = true;
        } catch(ServiceException e) {

            operationManager.updateServiceOperation(serviceOperation, null,
                    OperationConstant.OPERATION_RESULT_NUM.FAILED, e.getId());
            throw e;
        } finally {
            if(null != serviceModel && !success && isOperServiceModelDbSucess) {
                logger.error("activServiceModel error, serviceID is {}", serviceModel.getService_id());

                setSvcModelStatus(serviceModel, ServiceModel.STATUS_NUM.FAILED, oldActiveStatus);
            }
        }

        return serviceModel;
    }

    /**
     * 
     * @see org.openo.crossdomain.servicemgr.service.inf.IServiceManager#deactivServiceModel(String, String, String, HttpServletRequest)
     */
    @Override
    public ServiceModel deactivServiceModel(String deactiveTenantID, String deactiveServiceID, String deactiveUserName,
            HttpServletRequest httpRequest) throws ServiceException {
        ServiceModel serviceModel = null;
        String oldActiveStatus = null;
        ServiceOperation serviceOperation = null;
        boolean success = false;
        boolean isOperServiceModelDbSucess = false;
        try {

            serviceModel = serviceModelDao.getServiceModelByServiceId(deactiveTenantID, deactiveServiceID);
            ValidateUtil.assertNotNull(serviceModel, ErrorCode.SVCMGR_SERVICE_NOT_EXIST,
                    ServiceModelConstant.VALIDATE_FIELDS_NUM.SERVICE_MODEL);

            serviceChecker.checkDeactive(serviceModel);

            oldActiveStatus = serviceModel.getActive_status();
            serviceModel.setUpdate_time(System.currentTimeMillis());
            serviceModel.setStatus(ServiceModel.STATUS_NUM.DEACTIVATE_IN_PROGRESS);
            serviceModel.setActive_status(ServiceModel.ACTIVE_STATUS_NUM.DEACTIVATE_IN_PROGRESS);
            serviceModel.setParameters(null);

            serviceOperation =
                    operationManager.createServiceOperationWithInProgress(deactiveUserName, serviceModel, null,
                            OperationConstant.OPERATION_NUM.DEACTIVATE);
            serviceModel.setOperation_id(serviceOperation.getOperation_id());

            serviceModelDao.update(serviceModel);
            isOperServiceModelDbSucess = true;

            createJobSch.deactivServiceSch(serviceModel, httpRequest);
            success = true;
        } catch(ServiceException e) {

            operationManager.updateServiceOperation(serviceOperation, null,
                    OperationConstant.OPERATION_RESULT_NUM.FAILED, e.getId());
            throw e;
        } finally {
            if(null != serviceModel && !success && isOperServiceModelDbSucess) {
                logger.error("deactivServiceModel error, serviceID is {}", serviceModel.getService_id());

                setSvcModelStatus(serviceModel, ServiceModel.STATUS_NUM.FAILED, oldActiveStatus);
            }
        }

        return serviceModel;
    }

    /**
     * 
     * @see org.openo.crossdomain.servicemgr.service.inf.IServiceManager#delServiceModel(String, String, String, HttpServletRequest)
     */
    @Override
    public ServiceModel delServiceModel(String delTenantID, String delServiceID, String delUserName,
            HttpServletRequest httpRequest) throws ServiceException {
        ServiceModel serviceModel = null;
        ServiceOperation serviceOperation = null;
        boolean success = false;
        boolean isOperServiceModelDbSucess = false;
        boolean isCreateFailed = false;
        try {

            serviceModel = serviceModelDao.getServiceModelByServiceId(delTenantID, delServiceID);

            if(null == serviceModel) {
                return new ServiceModel();
            }

            if(serviceModel.isCreateFailed()) {
                isCreateFailed = true;
            }

            serviceChecker.checkDelete(serviceModel);
            serviceModel.setStatus(ServiceModel.STATUS_NUM.DELETE_IN_PROGRESS);

            serviceOperation =
                    operationManager.createServiceOperationWithInProgress(delUserName, serviceModel, null,
                            OperationConstant.OPERATION_NUM.DELETE);
            serviceModel.setOperation_id(serviceOperation.getOperation_id());

            if(isCreateFailed) {

                catalogProxy.deleteReference(serviceModel, httpRequest);

                operationManager.deleteServiceOperation(serviceModel.getService_id());

                serviceModelDao.deleteByServiceID(serviceModel.getTenant_id(), serviceModel.getService_id());
                isOperServiceModelDbSucess = true;
            } else {

                serviceModel.setSend(true);

                serviceModelDao.update(serviceModel);
                isOperServiceModelDbSucess = true;

                createJobSch.delServiceSch(serviceModel, httpRequest);
            }
            success = true;
        } catch(ServiceException e) {

            operationManager.updateServiceOperation(serviceOperation, null,
                    OperationConstant.OPERATION_RESULT_NUM.FAILED, e.getId());
            throw e;
        } finally {
            if(null != serviceModel && !success && isOperServiceModelDbSucess) {
                logger.error("delServiceModel error, serviceID is {}", serviceModel.getService_id());
                setSvcModelStatus(serviceModel, ServiceModel.STATUS_NUM.FAILED, null);
            }
        }

        return serviceModel;
    }

    private void setSvcModelStatus(ServiceModel serviceModel, String status, String activeStatus) {
        ServiceModel toUpdateServiceModel = new ServiceModel();

        toUpdateServiceModel.setTenant_id(serviceModel.getTenant_id());
        toUpdateServiceModel.setService_id(serviceModel.getService_id());
        toUpdateServiceModel.setStatus(status);
        if(null != activeStatus) {
            toUpdateServiceModel.setActive_status(activeStatus);
        }

        try {
            serviceModelDao.update(toUpdateServiceModel);
        } catch(ServiceException e) {
            logger.error("update error, oper db error.", e);
        }
    }

    /**
     * 
     * @see org.openo.crossdomain.servicemgr.service.inf.IServiceManager#getServiceModelByTenantID(String, String)
     */
    @Override
    public List<ServiceModel> getServiceModelByTenantID(String tenantID, String projectID) throws ServiceException {
        return serviceModelDao.getServiceModelByTenantId(tenantID);
    }

    /**
     * 
     * @see org.openo.crossdomain.servicemgr.service.inf.IServiceManager#getResourceByServiceID(String, HttpServletRequest)
     */
    @Override
    public JSONObject getResourceByServiceID(String service_id, HttpServletRequest httpRequest) throws ServiceException {
        return svcDecomposerProxy.getResourceByServiceID(service_id, httpRequest);
    }

    /**
     * 
     * @see org.openo.crossdomain.servicemgr.service.inf.IServiceManager#getServiceModelByTemplateID(String, String)
     */
    @Override
    public List<ServiceModel> getServiceModelByTemplateID(String tenantID, String templateID) throws ServiceException {
        return serviceModelDao.getServiceModelByTemplateId(tenantID, templateID);
    }

    /**
     * Set new parameter for sevice model.<br/>
     *
     * @param serviceModel service
     * @param serviceParams parameters of service
     * @throws ServiceException
     * @since crossdomain 0.5
     */
    public void setNewParamToServiceModel(ServiceModel serviceModel, List<ServiceParameter> serviceParams)
            throws ServiceException {
        if(!CollectionUtils.isEmpty(serviceParams)) {
            for(ServiceParameter serviceParameter : serviceParams) {
                parameterManager.setServiceListParamValue(serviceParameter);
            }
            serviceModel.setParameters(serviceParams);
        }
    }

    /**
     * @param catalogProxy The catalogProxy to set.
     */
    public void setCatalogProxy(ICatalogProxy catalogProxy) {
        this.catalogProxy = catalogProxy;
    }

    /**
     * @param serviceModelDao The serviceModelDao to set.
     */
    public void setServiceModelDao(IServiceModelDao serviceModelDao) {
        this.serviceModelDao = serviceModelDao;
    }

    /**
     * @param svcDecomposerProxy The svcDecomposerProxy to set.
     */
    public void setSvcDecomposerProxy(IServiceDecomposerProxy svcDecomposerProxy) {
        this.svcDecomposerProxy = svcDecomposerProxy;
    }

    /**
     * @param serviceChecker The serviceChecker to set.
     */
    public void setServiceChecker(IServiceChecker serviceChecker) {
        this.serviceChecker = serviceChecker;
    }

    /**
     * @param createJobSch The createJobSch to set.
     */
    public void setCreateJobSch(ICreateJobSch createJobSch) {
        this.createJobSch = createJobSch;
    }

    /**
     * @param parameterManager The parameterManager to set.
     */
    public void setParameterManager(IParameterManager parameterManager) {
        this.parameterManager = parameterManager;
    }

    /**
     * @param operationManager The operationManager to set.
     */
    public void setOperationManager(IServiceOperationManager operationManager) {
        this.operationManager = operationManager;
    }
}
