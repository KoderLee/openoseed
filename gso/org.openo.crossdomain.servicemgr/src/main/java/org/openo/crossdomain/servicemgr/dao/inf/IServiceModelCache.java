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

/**
 * Cache service data.<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
public interface IServiceModelCache {

	/**
	 * Set service data.<br/>
	 *
	 * @param serviceModel service data
	 * @since crossdomain 0.5
	 */
    void set(ServiceModel serviceModel);

    /**
     * Set service data into nginx.<br/>
     *
     * @param serviceModel service data
     * @since crossdomain 0.5
     */
    void setnx(ServiceModel serviceModel);

    /**
     * Delete service by service ID.<br/>
     *
     * @param key model identification
     * @param service_id service ID
     * @since crossdomain 0.5
     */
    void deleteByID(String key, String service_id);

    /**
     * Get service model data by service ID.<br/>
     *
     * @param key model identification
     * @param service_id service ID
     * @return service model data
     * @since crossdomain 0.5
     */
    ServiceModel getServiceModelById(String key, String service_id);

    /**
     * Get the list of service by tenant ID.<br/>
     *
     * @param key tenant ID
     * @return list of service
     * @since crossdomain 0.5
     */
    List<ServiceModel> getServiceModelListByTenantId(String key);

    /**
     * Insert service model data.<br/>
     *
     * @param key model identification
     * @param serviceModels list of service
     * @since crossdomain 0.5
     */
    void insert(String key, List<ServiceModel> serviceModels);
}
