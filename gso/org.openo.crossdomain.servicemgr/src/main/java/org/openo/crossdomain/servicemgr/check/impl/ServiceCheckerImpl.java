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
package org.openo.crossdomain.servicemgr.check.impl;

import javax.servlet.http.HttpServletRequest;

import org.openo.crossdomain.servicemgr.check.inf.IServiceChecker;
import org.openo.crossdomain.servicemgr.exception.ErrorCode;
import org.openo.crossdomain.servicemgr.model.roamo.ServiceModelConstant;
import org.openo.crossdomain.servicemgr.model.servicemo.ServiceDefInfo;
import org.openo.crossdomain.servicemgr.model.servicemo.ServiceModel;
import org.openo.crossdomain.servicemgr.restrepository.ICatalogProxy;
import org.openo.crossdomain.servicemgr.util.validate.ValidateUtil;

import org.openo.baseservice.remoteservice.exception.ServiceException;

/**
 * Implement class for operating service.<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
public class ServiceCheckerImpl implements IServiceChecker {

    private ICatalogProxy catalogProxy;

    /**
     * Check the status of service package.<br/>
     *
     * @param serviceModel service model
     * @param httpRequest http request
     * @throws ServiceException It happens when the status of service package is not active.
     * @since crossdomain 0.5
     */
    @Override
    public void checkCreate(ServiceModel serviceModel, HttpServletRequest httpRequest) throws ServiceException {
        ValidateUtil.assertNotNull(serviceModel, ServiceModelConstant.VALIDATE_FIELDS_NUM.SERVICE_MODEL);

        ServiceDefInfo serviceDefInfo =
                catalogProxy.getServiceDetail(serviceModel.getService_definition_id(), httpRequest);
        String servicePkgStatus = serviceDefInfo.getStatus();
        ValidateUtil.assertNotEmpty(servicePkgStatus, ServiceModelConstant.VALIDATE_FIELDS_NUM.SERVICE_TYPE);

        if(servicePkgStatus.equals(SVCPKG_STATUS_NUM.SVCMGR_PACKAGE_STATUS_INACTIVE_LABLE)) {
            throw new ServiceException(ErrorCode.SVCMGR_SVCPKG_STATUS_INACTIVE_ERROR,
                    "status of serivcePackage is inactiving, can not create service");
        } else if(servicePkgStatus.equals(SVCPKG_STATUS_NUM.SVCMGR_PACKAGE_STATUS_DELETEDEPNDING_LABLE)) {
            throw new ServiceException(ErrorCode.SVCMGR_SVCPKG_STATUS_DELETEDEPENDING_ERROR,
                    "status of serivcePackage is deleting, can not create service");
        } else if(servicePkgStatus.equals(SVCPKG_STATUS_NUM.SVCMGR_PACKAGE_STATUS_ACTIVE_LABLE)) {
            return;
        } else {
            throw new ServiceException(ErrorCode.SVCMGR_SVCPKG_NOT_ACTIVATION,
                    "servicePackage is not activation, can not create service");
        }
    }

    /**
     * Check if the service can be update.<br/>
     *
     * @param serviceModel
     * @param httpRequest
     * @throws ServiceException It happens when the status of service is failure or in progress, the service package is not satisfied with requirement.
     * @since crossdomain 0.5
     */
    @Override
    public void checkUpdate(ServiceModel serviceModel, HttpServletRequest httpRequest) throws ServiceException {
        ValidateUtil.assertNotNull(serviceModel, ServiceModelConstant.VALIDATE_FIELDS_NUM.SERVICE_MODEL);

        if(serviceModel.isCreateFailed()) {
            throw new ServiceException(ErrorCode.SVCMGR_SERVICE_CREATE_FAILED, "service create failed.");
        }

        if(serviceModel.isInprogress()) {
            throw new ServiceException(ErrorCode.SERVICEMGR_SERVICE_STATUS_INPROGRESS,
                    "status is in progress, can not update service model");
        }

        checkCreate(serviceModel, httpRequest);
    }

    /**
     * Check if it deletes service.<br/>
     *
     * @param serviceModel service data
     * @throws ServiceException It happens when the status of service is active or in progress.
     * @since crossdomain 0.5
     */
    @Override
    public void checkDelete(ServiceModel serviceModel) throws ServiceException {
        ValidateUtil.assertNotNull(serviceModel, ServiceModelConstant.VALIDATE_FIELDS_NUM.SERVICE_MODEL);

        if(serviceModel.isActive()) {
            throw new ServiceException(ErrorCode.SERVICEMGR_SERVICE_STATUS_ACTIVE,
                    "status is active can not delete service model");
        }

        if(serviceModel.isInprogress()) {
            throw new ServiceException(ErrorCode.SERVICEMGR_SERVICE_STATUS_INPROGRESS,
                    "status is in progress, can not delete service model");
        }
    }

    /**
     * Check if the service can be activated.<br/>
     *
     * @param serviceModel service data
     * @throws ServiceException It happens when the status of service is failure, active or in progeress.
     * @since crossdomain 0.5
     */
    @Override
    public void checkActive(ServiceModel serviceModel) throws ServiceException {
        ValidateUtil.assertNotNull(serviceModel, ServiceModelConstant.VALIDATE_FIELDS_NUM.SERVICE_MODEL);

        if(serviceModel.isCreateFailed()) {
            throw new ServiceException(ErrorCode.SVCMGR_SERVICE_CREATE_FAILED, "service create failed.");
        }

        if(serviceModel.isActive()) {
            throw new ServiceException(ErrorCode.SERVICEMGR_SERVICE_STATUS_ACTIVE,
                    "status is active can not active service model");
        }

        if(serviceModel.isInprogress()) {
            throw new ServiceException(ErrorCode.SERVICEMGR_SERVICE_STATUS_INPROGRESS,
                    "status is in progress, can not active service model");
        }
    }

    /**
     * Check if the service can be deactivated.<br/>
     *
     * @param serviceModel service data
     * @throws ServiceException It happens when the status of service is failure, inactive or in progress.
     * @since crossdomain 0.5
     */
    @Override
    public void checkDeactive(ServiceModel serviceModel) throws ServiceException {
        ValidateUtil.assertNotNull(serviceModel, ServiceModelConstant.VALIDATE_FIELDS_NUM.SERVICE_MODEL);

        if(serviceModel.isCreateFailed()) {
            throw new ServiceException(ErrorCode.SVCMGR_SERVICE_CREATE_FAILED, "service create failed.");
        }

        if(serviceModel.isDeactive()) {
            throw new ServiceException(ErrorCode.SERVICEMGR_SERVICE_STATUS_DEACTIVE,
                    "status is deactive can not deactive service model");
        }
        if(serviceModel.isInprogress()) {
            throw new ServiceException(ErrorCode.SERVICEMGR_SERVICE_STATUS_INPROGRESS,
                    "status is in progress, can not deactive service model");
        }
    }

    /**
     * the status of service package
     */
    public static class SVCPKG_STATUS_NUM {

        public static final String SVCMGR_PACKAGE_STATUS_ACTIVE_LABLE = "active";

        public static final String SVCMGR_PACKAGE_STATUS_INACTIVE_LABLE = "inactive";

        public static final String SVCMGR_PACKAGE_STATUS_DELETEDEPNDING_LABLE = "delete pending";
    }

    /**
     * @param catalogProxy The catalogProxy to set.
     */
    public void setCatalogProxy(ICatalogProxy catalogProxy) {
        this.catalogProxy = catalogProxy;
    }

}
