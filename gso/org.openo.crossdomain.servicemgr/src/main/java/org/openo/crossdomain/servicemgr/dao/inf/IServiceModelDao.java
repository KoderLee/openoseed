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

import org.openo.crossdomain.servicemgr.model.servicemo.ServiceModel;

import org.openo.baseservice.remoteservice.exception.ServiceException;

/**
 * Interface for database operation.<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
public interface IServiceModelDao {

	/**
	 * Insert data.<br/>
	 *
	 * @param serviceModel service data
	 * @throws ServiceException
	 * @since crossdomain 0.5
	 */
    void insert(ServiceModel serviceModel) throws ServiceException;

    /**
     * Delete service by service ID.<br/>
     *
     * @param tenant_id tenant ID
     * @param service_id service ID
     * @throws ServiceException
     * @since crossdomain 0.5
     */
    void deleteByServiceID(String tenant_id, String service_id) throws ServiceException;

    /**
     * Update service data.<br/>
     *
     * @param serviceModel service data
     * @throws ServiceException
     * @since crossdomain 0.5
     */
    void update(ServiceModel serviceModel) throws ServiceException;

    /**
     * Get service by service ID.<br/>
     *
     * @param tenant_id tenant ID
     * @param service_id service ID
     * @return service data
     * @throws ServiceException
     * @since crossdomain 0.5
     */
    ServiceModel getServiceModelByServiceId(String tenant_id, String service_id) throws ServiceException;

    /**
     * Get list of service by tenant ID<br/>
     *
     * @param tenant_id tenant ID
     * @return list of service
     * @throws ServiceException
     * @since crossdomain 0.5
     */
    List<ServiceModel> getServiceModelByTenantId(String tenant_id) throws ServiceException;

    /**
     * Get list of service by template ID.<br/>
     *
     * @param tenantID tenant ID
     * @param templateID template ID
     * @return list of service
     * @throws ServiceException
     * @since crossdomain 0.5
     */
    List<ServiceModel> getServiceModelByTemplateId(String tenantID, String templateID) throws ServiceException;    
}
