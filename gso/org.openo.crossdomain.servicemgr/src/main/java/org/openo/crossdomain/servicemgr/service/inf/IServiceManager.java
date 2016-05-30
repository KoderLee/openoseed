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

import javax.servlet.http.HttpServletRequest;

import org.openo.crossdomain.servicemgr.model.roamo.ServiceModelInfo;
import org.openo.crossdomain.servicemgr.model.servicemo.ServiceModel;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import net.sf.json.JSONObject;

/**
 * Interface for service manager.<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
public interface IServiceManager {

	/**
	 * Get service by tenant ID.<br/>
	 *
	 * @param tenantID tenant ID
	 * @param serviceID service ID
	 * @return service
	 * @throws ServiceException
	 * @since crossdomain 0.5
	 */
    public ServiceModel getServiceModelByServiceID(String tenantID, String serviceID) throws ServiceException;

    /**
     * Get the collection of service by tenant ID.<br/>
     *
     * @param tenantID tenant ID
     * @param project_Id project ID
     * @return collection of service
     * @throws ServiceException
     * @since crossdomain 0.5
     */
    public List<ServiceModel> getServiceModelByTenantID(String tenantID, String project_Id) throws ServiceException;

    /**
     * Get resource which is expressed in form of json by service ID.<br/>
     *
     * @param service_id sevice ID
     * @param httpRequest http request
     * @return resource object in form of json
     * @throws ServiceException
     * @since crossdomain 0.5
     */
    JSONObject getResourceByServiceID(String service_id, HttpServletRequest httpRequest) throws ServiceException;

    /**
     * Create service model.<br/>
     *
     * @param serviceModel service model
     * @param userName user name
     * @param serviceModelInfo the detail of service model
     * @param httpRequest http request
     * @return service model
     * @throws ServiceException
     * @since crossdomain 0.5
     */
    ServiceModel createServiceModel(ServiceModel serviceModel, String userName, ServiceModelInfo serviceModelInfo,
            HttpServletRequest httpRequest) throws ServiceException;

    /**
     * Update service model.<br/>
     *
     * @param updateTenantID tenant ID
     * @param updateServiceID servivc eID
     * @param updateUserName user name
     * @param serviceModelInfo the detail of service model
     * @param httpRequest http request
     * @return service model
     * @throws ServiceException
     * @since crossdomain 0.5
     */
    ServiceModel updateServiceModel(String updateTenantID, String updateServiceID, String updateUserName,
            ServiceModelInfo serviceModelInfo, HttpServletRequest httpRequest) throws ServiceException;

    /**
     * Delete service model.<br/>
     *
     * @param delTenantID tenant ID
     * @param delServiceID service ID
     * @param delUserName user name
     * @param httpRequest http request
     * @return service model
     * @throws ServiceException
     * @since crossdomain 0.5
     */
    ServiceModel delServiceModel(String delTenantID, String delServiceID, String delUserName,
            HttpServletRequest httpRequest) throws ServiceException;

    /**
     * Activate service<br/>
     *
     * @param activeTenantID tenant ID
     * @param activeServiceID service ID
     * @param activeUserName user name
     * @param httpRequest http request
     * @return service model
     * @throws ServiceException
     * @since crossdomain 0.5
     */
    ServiceModel activServiceModel(String activeTenantID, String activeServiceID, String activeUserName,
            HttpServletRequest httpRequest) throws ServiceException;

    /**
     * Deactivate service.<br/>
     *
     * @param deactiveTenantID tenant ID
     * @param deactiveServiceID service ID
     * @param deactiveUserName user name
     * @param httpRequest http request
     * @return service model
     * @throws ServiceException
     * @since crossdomain 0.5
     */
    ServiceModel deactivServiceModel(String deactiveTenantID, String deactiveServiceID, String deactiveUserName,
            HttpServletRequest httpRequest) throws ServiceException;

    /**
     * Get the collection of service by template ID.<br/>
     *
     * @param tenantID tenant ID
     * @param templateID template ID
     * @return collection of services
     * @throws ServiceException
     * @since crossdomain 0.5
     */
    List<ServiceModel> getServiceModelByTemplateID(String tenantID, String templateID) throws ServiceException;
}
