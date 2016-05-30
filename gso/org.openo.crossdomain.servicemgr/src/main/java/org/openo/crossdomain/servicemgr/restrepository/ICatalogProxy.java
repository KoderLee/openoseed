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

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.openo.crossdomain.servicemgr.model.servicemo.ServiceDefInfo;
import org.openo.crossdomain.servicemgr.model.servicemo.ServiceModel;

import org.openo.baseservice.remoteservice.exception.ServiceException;

/**
 * Interface for catalog proxy.<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
public interface ICatalogProxy {

	/**
	 * Get template content.<br/>
	 *
	 * @param service service data
	 * @param httpRequest http request
	 * @return content of template
	 * @throws ServiceException
	 * @since crossdomain 0.5
	 */
    String getTempalteContent(final ServiceModel service, HttpServletRequest httpRequest) throws ServiceException;

    /**
     * Create reference.<br/>
     *
     * @param service service data
     * @param httpRequest http request
     * @throws ServiceException
     * @since crossdomain 0.5
     */
    void createReference(final ServiceModel service, HttpServletRequest httpRequest) throws ServiceException;

    /**
     * Delete reference.<br/>
     *
     * @param service service data
     * @param httpRequest http request
     * @throws ServiceException
     * @since crossdomain 0.5
     */
    void deleteReference(final ServiceModel service, HttpServletRequest httpRequest) throws ServiceException;

    /**
     * Delete reference.<br/>
     *
     * @param service service data
     * @param tokenStr authentication token
     * @throws ServiceException
     * @since crossdomain 0.5
     */
    void deleteReference(final ServiceModel service, String tokenStr) throws ServiceException;

    /**
     * Get the url of template from catalog.<br/>
     *
     * @param service service data
     * @return url
     * @since crossdomain 0.5
     */
    String getTemplateUrlInCatalog(final ServiceModel service);

    /**
     * Get the detail of service.<br/>
     *
     * @param serviceDefId service definition ID
     * @param httpRequest http request
     * @return the detail of service
     * @throws ServiceException
     * @since crossdomain 0.5
     */
    ServiceDefInfo getServiceDetail(String serviceDefId, HttpServletRequest httpRequest) throws ServiceException;

    /**
     * Get the collection of service package.<br/>
     *
     * @param httpRequest http request
     * @param defaultCategoryId default category ID
     * @return collection of service detail
     * @throws ServiceException
     * @since crossdomain 0.5
     */
    List<ServiceDefInfo> getServicePackageList(HttpServletRequest httpRequest, String defaultCategoryId)
            throws ServiceException;
}
