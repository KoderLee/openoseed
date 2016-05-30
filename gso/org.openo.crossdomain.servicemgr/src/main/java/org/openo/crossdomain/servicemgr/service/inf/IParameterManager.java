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
import org.openo.crossdomain.servicemgr.model.servicemo.ServiceParamInfo;
import org.openo.crossdomain.servicemgr.model.servicemo.ServiceParameter;

import org.openo.baseservice.remoteservice.exception.ServiceException;

/**
 * Interface for service parameter manager.<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
public interface IParameterManager {

	/**
	 * Decode the service parameter.<br/>
	 *
	 * @param serviceModel service data
	 * @param serviceModelInfo the detail of service
	 * @param httpRequest http request
	 * @return service parameter
	 * @throws ServiceException
	 * @since crossdomain 0.5
	 */
    public ServiceParamInfo decodeServiceParameter(ServiceModel serviceModel, ServiceModelInfo serviceModelInfo,
            HttpServletRequest httpRequest) throws ServiceException;

    /**
     * Set the value of parameter that the type is list.<br/>
     *
     * @param serviceParameter service parameter
     * @throws ServiceException
     * @since crossdomain 0.5
     */
    public void setServiceListParamValue(ServiceParameter serviceParameter) throws ServiceException;

    /**
     * Get the service parameters.<br/>
     *
     * @param tenantID tenant ID
     * @param serviceID servivce ID
     * @return the collection of service parameter
     * @throws ServiceException
     * @since crossdomain 0.5
     */
    List<ServiceParameter> getServiceParamterList(String tenantID, String serviceID) throws ServiceException;

    /**
     * Get service parameters by template.<br/>
     *
     * @param tenantID tenant ID
     * @param ID service ID
     * @param templateID template ID
     * @param httpRequest http request
     * @return service parameter
     * @throws ServiceException
     * @since crossdomain 0.5
     */
    Object getServiceParametersByTemplate(String tenantID, String ID, String templateID, HttpServletRequest httpRequest)
                    throws ServiceException;

    /**
     * Get the detail of service parameter.<br/>
     *
     * @param serviceModel service data
     * @param serviceModelInfo the detail of service data
     * @param httpRequest http request
     * @return the detail of service parameter
     * @throws ServiceException
     * @since crossdomain 0.5
     */
    public ServiceParamInfo getServiceParameter(ServiceModel serviceModel, ServiceModelInfo serviceModelInfo,
            HttpServletRequest httpRequest) throws ServiceException;
}
