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

import org.openo.crossdomain.servicemgr.model.servicemo.ServiceParameter;

import org.openo.baseservice.remoteservice.exception.ServiceException;

/**
 * Interface for service ordinary parameters which are simple type.<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
public interface IServiceParameterDao {

	/**
	 * Insert service parameter.<br/>
	 *
	 * @param serviceParameter service parameter
	 * @throws ServiceException
	 * @since crossdomain 0.5
	 */
    void insert(ServiceParameter serviceParameter) throws ServiceException;

    /**
     * Delete services by service IDs.<br/>
     *
     * @param ids service IDs
     * @throws ServiceException
     * @since crossdomain 0.5
     */
    void delete(List<Integer> ids) throws ServiceException;

    /**
     * Delete a service by service ID.<br/>
     *
     * @param tenant_id tenant ID
     * @param service_id service ID
     * @throws ServiceException
     * @since crossdomain 0.5
     */
    void deleteByServiceID(String tenant_id, String service_id) throws ServiceException;

    /**
     * Update service parameter.<br/>
     *
     * @param serviceParameter service parameter
     * @throws ServiceException
     * @since crossdomain 0.5
     */
    void update(ServiceParameter serviceParameter) throws ServiceException;

    /**
     * Get service paramter by service ID.<br/>
     *
     * @param id service ID
     * @return service parameter
     * @throws ServiceException
     * @since crossdomain 0.5
     */
    ServiceParameter getServiceParameterById(String id) throws ServiceException;

    /**
     * Get list of service paramter.<br/>
     *
     * @param tenant_id tenant ID
     * @param svcIDs service IDs
     * @return list of service parameter
     * @throws ServiceException
     * @since crossdomain 0.5
     */
    List<ServiceParameter> getServiceParameterList(String tenant_id, List<String> svcIDs) throws ServiceException;
}
