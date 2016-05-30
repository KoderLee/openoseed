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
package org.openo.crossdomain.servicemgr.dao.inf;

import java.util.List;

import org.openo.crossdomain.servicemgr.model.servicemo.ServiceOperation;

import org.openo.baseservice.remoteservice.exception.ServiceException;

/**
 * Interface for the status of service operation.<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
public interface IServiceOperationDao {

	/**
	 * Insert service operation.<br/>
	 *
	 * @param serviceOperation service operation
	 * @since crossdomain 0.5
	 */
    void insert(ServiceOperation serviceOperation);

    /**
     * Update service operation.<br/>
     *
     * @param serviceOperation service operation
     * @since crossdomain 0.5
     */
    void update(ServiceOperation serviceOperation);

    /**
     * Delete service operation<br/>
     *
     * @param serivceId service ID
     * @since crossdomain 0.5
     */
    void delete(String serivceId);

    /**
     * Get service operation by service ID and operation ID.<br/>
     *
     * @param serviceID service ID
     * @param operationID operation ID
     * @return service operation
     * @since crossdomain 0.5
     */
    ServiceOperation getServiceOperationByID(String serviceID, String operationID);

    /**
     * Get list of service operation by service ID.<br/>
     *
     * @param serviceID service ID
     * @return list of service operation
     * @since crossdomain 0.5
     */
    List<ServiceOperation> getServiceOperationsByServiceID(String serviceID);

    /**
     * Delete the history operation data from mapper.<br/>
     *
     * @since crossdomain 0.5
     */
    void deleteHistory();
}
