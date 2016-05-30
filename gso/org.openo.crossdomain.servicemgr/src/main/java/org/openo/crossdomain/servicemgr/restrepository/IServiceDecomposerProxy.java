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
package org.openo.crossdomain.servicemgr.restrepository;

import javax.servlet.http.HttpServletRequest;

import org.openo.crossdomain.servicemgr.model.roamo.ServiceModelInfo;
import org.openo.crossdomain.servicemgr.model.servicemo.ServiceModel;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;

import net.sf.json.JSONObject;

/**
 * Interface for service decomposer proxy.<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
public interface IServiceDecomposerProxy {

	/**
	 * Get resource by service ID.<br/>
	 *
	 * @param serviceID service ID
	 * @param httpRequest http request
	 * @return resource in the form of json
	 * @throws ServiceException
	 * @since crossdomain 0.5
	 */
    public JSONObject getResourceByServiceID(final String serviceID, HttpServletRequest httpRequest)
            throws ServiceException;

    /**
     * Create service by decomposer component.<br/>
     *
     * @param serviceModelInfo the detail of service
     * @param service service data
     * @param httpRequest http 
     * @return response
     * @throws ServiceException
     * @since crossdomain 0.5
     */
    public RestfulResponse createDecomposer(final ServiceModelInfo serviceModelInfo, final ServiceModel service,
            HttpServletRequest httpRequest) throws ServiceException;

    /**
     * Update service by decomposer component.<br/>
     *
     * @param serviceModelInfo the detail of service
     * @param service service data
     * @param httpRequest http 
     * @return response
     * @throws ServiceException
     * @since crossdomain 0.5
     */
    public RestfulResponse updateDecomposer(final ServiceModelInfo serviceModelInfo, final ServiceModel service,
            HttpServletRequest httpRequest) throws ServiceException;

    /**
     * Activate service by decomposer component.<br/>
     *
     * @param service service data
     * @param httpRequest http 
     * @return response
     * @throws ServiceException
     * @since crossdomain 0.5
     */
    public RestfulResponse activeDecomposer(final ServiceModel service, HttpServletRequest httpRequest)
            throws ServiceException;

    /**
     * Deactivate service by decomposer component.<br/>
     *
     * @param service service data
     * @param httpRequest http 
     * @return response
     * @throws ServiceException
     * @since crossdomain 0.5
     */
    public RestfulResponse deactivDecomposer(final ServiceModel service, HttpServletRequest httpRequest)
            throws ServiceException;

    /**
     * Delete service by decomposer component.<br/>
     *
     * @param service service data
     * @param httpRequest http 
     * @return response
     * @throws ServiceException
     * @since crossdomain 0.5
     */
    public RestfulResponse deleteDecomposer(final ServiceModel service, HttpServletRequest httpRequest)
            throws ServiceException;

}
