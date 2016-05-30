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

import org.openo.crossdomain.servicemgr.constant.Constant;
import org.openo.crossdomain.servicemgr.dao.inf.IServiceOperationDao;
import org.openo.crossdomain.servicemgr.model.roamo.OperationConstant;
import org.openo.crossdomain.servicemgr.model.servicemo.ServiceListParameter;
import org.openo.crossdomain.servicemgr.model.servicemo.ServiceModel;
import org.openo.crossdomain.servicemgr.model.servicemo.ServiceOperation;
import org.openo.crossdomain.servicemgr.model.servicemo.ServiceParameter;
import org.openo.crossdomain.servicemgr.service.inf.IServiceOperationManager;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import org.openo.baseservice.remoteservice.exception.ServiceException;

/**
 * Implementation for service operation manager.<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
public class ServiceOperationManagerImpl implements IServiceOperationManager {

    private IServiceOperationDao serviceOperationDao;

    /**
     * 
     * @see org.openo.crossdomain.servicemgr.service.inf.IServiceOperationManager#updateServiceOperation(ServiceOperation, List, String, String)
     */
    @Override
    public void updateServiceOperation(ServiceOperation serviceOperation, List<ServiceParameter> serviceParameters,
            String operResult, String reason) throws ServiceException {
        if(null == serviceOperation) {
            return;
        }

        serviceOperation.updateServiceOperation(operResult, reason, OperationConstant.OPERATION_PROGRESS_NUM.DONE);

        String operationContent =
                getOperationContent(serviceParameters, serviceOperation.getOperation(), operResult,
                        OperationConstant.OPERATION_PROGRESS_NUM.DONE);
        serviceOperation.setOperation_content(operationContent);

        serviceOperationDao.update(serviceOperation);
    }

    /**
     * 
     * @see org.openo.crossdomain.servicemgr.service.inf.IServiceOperationManager#createServiceOperationWithInProgress(String, ServiceModel, List, String)
     */
    @Override
    public ServiceOperation createServiceOperationWithInProgress(String userName, ServiceModel serviceModel,
            List<ServiceParameter> serviceParameters, String operation) throws ServiceException {
        if(null == serviceModel) {
            return null;
        }

        ServiceOperation serviceOperation =
                ServiceOperation.getServiceOperation(serviceModel, userName, operation,
                        OperationConstant.OPERATION_PROGRESS_NUM.IN_PROGRESS);

        String operationContent =
                getOperationContent(serviceParameters, operation, null,
                        OperationConstant.OPERATION_PROGRESS_NUM.IN_PROGRESS);
        serviceOperation.setOperation_content(operationContent);

        serviceOperationDao.insert(serviceOperation);

        return serviceOperation;
    }

    private String getOperationContent(List<ServiceParameter> serviceParameters, String operation, String result,
            String progress) {
        StringBuilder operationContent =
                StringUtils.hasLength(operation) ? (new StringBuilder(operation)) : (new StringBuilder());

        if(!CollectionUtils.isEmpty(serviceParameters)) {
            for(ServiceParameter serviceParameter : serviceParameters) {
                getOperationParameter(operationContent, serviceParameter);
            }
        }

        if(StringUtils.hasLength(result)) {
            operationContent.append(Constant.COMMA).append(Constant.RESULT).append(Constant.COLON).append(result);
        }

        if(StringUtils.hasLength(progress)) {
            operationContent.append(Constant.COMMA).append(Constant.PROGRESS).append(Constant.COLON).append(progress);
        }

        return operationContent.toString();
    }

    private void getOperationParameter(StringBuilder operationContent, ServiceParameter serviceParameter) {
        if(ServiceParameter.PARAM_TYPE_NUM.PARAM_TYPE_SIMPLE.equals(serviceParameter.getParameter_type())) {
            operationContent.append(Constant.COMMA).append(serviceParameter.getParameter_name()).append(Constant.COLON)
                    .append(serviceParameter.getParameter_value());
        } else {
            getOperListParam(operationContent, serviceParameter);
        }
    }

    private void getOperListParam(StringBuilder operationContent, ServiceParameter serviceParameter) {
        List<ServiceListParameter> serviceListParameters = serviceParameter.getParameterGroup();
        if(!CollectionUtils.isEmpty(serviceListParameters)) {
            for(ServiceListParameter serviceListParameter : serviceListParameters) {
                operationContent.append(Constant.COMMA).append(serviceListParameter.getKey_name());
            }
        }
    }

    /**
     * 
     * @see org.openo.crossdomain.servicemgr.service.inf.IServiceOperationManager#deleteServiceOperation(String)
     */
    @Override
    public void deleteServiceOperation(String serviceID) throws ServiceException {
        serviceOperationDao.delete(serviceID);
    }

    /**
     * 
     * @see org.openo.crossdomain.servicemgr.service.inf.IServiceOperationManager#getServiceOperationsByServiceID(String)
     */
    @Override
    public List<ServiceOperation> getServiceOperationsByServiceID(String serviceID) throws ServiceException {
        return serviceOperationDao.getServiceOperationsByServiceID(serviceID);
    }

    /**
     * 
     * @see org.openo.crossdomain.servicemgr.service.inf.IServiceOperationManager#getServiceOperationByID(String, String)
     */
    @Override
    public ServiceOperation getServiceOperationByID(String serviceID, String operationID) throws ServiceException {
        return serviceOperationDao.getServiceOperationByID(serviceID, operationID);
    }

    public void setServiceOperationDao(IServiceOperationDao serviceOperationDao) {
        this.serviceOperationDao = serviceOperationDao;
    }
}
