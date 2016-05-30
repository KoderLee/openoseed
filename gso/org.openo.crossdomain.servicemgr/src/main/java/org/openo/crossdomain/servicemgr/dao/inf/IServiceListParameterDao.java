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

import org.openo.crossdomain.servicemgr.model.servicemo.ServiceListParameter;

import org.openo.baseservice.remoteservice.exception.ServiceException;

/**
 * Interface that provide operation of service parameter which type is list.<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
public interface IServiceListParameterDao {

	/**
	 * Insert list parameters into database.<br/>
	 *
	 * @param serviceListParameter paramter which type is list
	 * @throws ServiceException
	 * @since crossdomain 0.5
	 */
    void insert(ServiceListParameter serviceListParameter) throws ServiceException;

    /**
     * Delete service by service ID.<br/>
     *
     * @param service_id service id
     * @throws ServiceException
     * @since crossdomain 0.5
     */
    void deleteByServiceID(String service_id) throws ServiceException;

    /**
     * Delete service by key name<br/>
     *
     * @param service_id service name
     * @param paramgroup_name the name of parameter group
     * @param key_name key name
     * @throws ServiceException
     * @since crossdomain 0.5
     */
    void deleteByKeyName(String service_id, String paramgroup_name, String key_name) throws ServiceException;

    /**
     * Update the list parameter of service.<br/>
     *
     * @param serviceListParameter service parameter
     * @throws ServiceException
     * @since crossdomain 0.5
     */
    void update(ServiceListParameter serviceListParameter) throws ServiceException;

    /**
     * Get the list parameter of service by key name.<br/>
     *
     * @param service_id service ID
     * @param paramgroup_name the name of parameter group
     * @param key_name key name
     * @return the list parameter of service
     * @throws ServiceException
     * @since crossdomain 0.5
     */
    ServiceListParameter getServiceListParameterByKeyName(String service_id, String paramgroup_name, String key_name)
            throws ServiceException;

    /**
     * Get the list parameter of services by ID of servivces<br/>
     *
     * @param svcIDs the IDs of service
     * @return the collection of list parameter that services have.
     * @throws ServiceException
     * @since crossdomain 0.5
     */
    List<ServiceListParameter> getServiceListParameterList(List<String> svcIDs) throws ServiceException;
}
