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
package org.openo.crossdomain.servicemgr.check.inf;

import javax.servlet.http.HttpServletRequest;

import org.openo.crossdomain.servicemgr.model.servicemo.ServiceModel;

import org.openo.baseservice.remoteservice.exception.ServiceException;

/**
 * Check interface for operating service.<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
public interface IServiceChecker {

    /**
     * Check the status of service package.<br/>
     *
     * @param serviceModel service model
     * @param httpRequest http request
     * @throws ServiceException It happens when the status of service package is not active.
     * @since crossdomain 0.5
     */
    void checkCreate(ServiceModel serviceModel, HttpServletRequest httpRequest) throws ServiceException;

    /**
     * Check if the service can be update.<br/>
     *
     * @param serviceModel
     * @param httpRequest
     * @throws ServiceException It happens when the status of service is failure or in progress, the service package is not satisfied with requirement.
     * @since crossdomain 0.5
     */
    void checkUpdate(ServiceModel serviceModel, HttpServletRequest httpRequest) throws ServiceException;

    /**
     * Check if it deletes service.<br/>
     *
     * @param serviceModel service data
     * @throws ServiceException It happens when the status of service is active or in progress.
     * @since crossdomain 0.5
     */
    void checkDelete(ServiceModel serviceModel) throws ServiceException;

    /**
     * Check if the service can be activated.<br/>
     *
     * @param serviceModel service data
     * @throws ServiceException It happens when the status of service is failure, active or in progeress.
     * @since crossdomain 0.5
     */
    void checkActive(ServiceModel serviceModel) throws ServiceException;

    /**
     * Check if the service can be deactivated.<br/>
     *
     * @param serviceModel service data
     * @throws ServiceException It happens when the status of service is failure, inactive or in progress.
     * @since crossdomain 0.5
     */
    void checkDeactive(ServiceModel serviceModel) throws ServiceException;
}
