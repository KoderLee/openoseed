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
package org.openo.crossdomain.servicemgr.job.inf;

import javax.servlet.http.HttpServletRequest;

import org.openo.crossdomain.servicemgr.model.roamo.ServiceModelInfo;
import org.openo.crossdomain.servicemgr.model.servicemo.ServiceModel;

import org.openo.baseservice.remoteservice.exception.ServiceException;

/**
 * Interface for job scheduling.<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
public interface ICreateJobSch {

	/**
	 * Create service scheduling.<br/>
	 *
	 * @param serviceModel service model
	 * @param serviceModelInfo service detail data
	 * @param httpRequest http request
	 * @throws ServiceException
	 * @since crossdomain 0.5
	 */
    public void createServiceSch(final ServiceModel serviceModel, final ServiceModelInfo serviceModelInfo,
            HttpServletRequest httpRequest) throws ServiceException;

	/**
	 * Update service scheduling.<br/>
	 *
	 * @param serviceModel service model
	 * @param serviceModelInfo service detail data
	 * @param httpRequest http request
	 * @throws ServiceException
	 * @since crossdomain 0.5
	 */
    public void updateServiceSch(final ServiceModel serviceModel, final ServiceModelInfo serviceModelInfo,
            HttpServletRequest httpRequest) throws ServiceException;

	/**
	 * Activate service scheduling.<br/>
	 *
	 * @param serviceModel service model
	 * @param httpRequest http request
	 * @throws ServiceException
	 * @since crossdomain 0.5
	 */
    public void activeServiceSch(final ServiceModel serviceModel, HttpServletRequest httpRequest)
            throws ServiceException;

	/**
	 * Deactivate service scheduling.<br/>
	 *
	 * @param serviceModel service model
	 * @param httpRequest http request
	 * @throws ServiceException
	 * @since crossdomain 0.5
	 */
    public void deactivServiceSch(final ServiceModel serviceModel, HttpServletRequest httpRequest)
            throws ServiceException;

	/**
	 * Delete service scheduling.<br/>
	 *
	 * @param serviceModel service model
	 * @param httpRequest http request
	 * @throws ServiceException
	 * @since crossdomain 0.5
	 */
    public void delServiceSch(final ServiceModel serviceModel, HttpServletRequest httpRequest) throws ServiceException;
}
