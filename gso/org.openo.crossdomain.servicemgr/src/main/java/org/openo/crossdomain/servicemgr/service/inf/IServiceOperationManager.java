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
package org.openo.crossdomain.servicemgr.service.inf;

import java.util.List;

import org.openo.crossdomain.servicemgr.model.servicemo.ServiceModel;
import org.openo.crossdomain.servicemgr.model.servicemo.ServiceOperation;
import org.openo.crossdomain.servicemgr.model.servicemo.ServiceParameter;

import org.openo.baseservice.remoteservice.exception.ServiceException;

/**
 * Interface for service operation manager.<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
public interface IServiceOperationManager {

	/**
	 * Add service operation that service is in operating progress.<br/>
	 *
	 * @param userName user name
	 * @param serviceModel service data
	 * @param serviceParameters service parameters
	 * @param operation service operation
	 * @return service operation
	 * @throws ServiceException
	 * @since crossdomain 0.5
	 */
    ServiceOperation createServiceOperationWithInProgress(String userName, ServiceModel serviceModel,
            List<ServiceParameter> serviceParameters, String operation) throws ServiceException;

    /**
     * Update service operation.<br/>
     *
     * @param serviceOperation service operation
     * @param serviceParameters sevice parameters
     * @param operResult result of operation
     * @param reason operation reason
     * @throws ServiceException
     * @since crossdomain 0.5
     */
    void updateServiceOperation(ServiceOperation serviceOperation, List<ServiceParameter> serviceParameters,
            String operResult, String reason) throws ServiceException;

    /**
     * Delete service operation<br/>
     *
     * @param serviceID sevice ID
     * @throws ServiceException
     * @since crossdomain 0.5
     */
    void deleteServiceOperation(String serviceID) throws ServiceException;

    /**
     * AGet service operations by service ID.<br/>
     *
     * @param serviceID service ID
     * @return collection of service operations
     * @throws ServiceException
     * @since crossdomain 0.5
     */
    List<ServiceOperation> getServiceOperationsByServiceID(String serviceID) throws ServiceException;

    /**
     * Get service operation by operation ID.<br/>
     *
     * @param serviceID service ID
     * @param operationID operation ID
     * @return service Operation
     * @throws ServiceException
     * @since crossdomain 0.5
     */
    ServiceOperation getServiceOperationByID(String serviceID, String operationID) throws ServiceException;
}
