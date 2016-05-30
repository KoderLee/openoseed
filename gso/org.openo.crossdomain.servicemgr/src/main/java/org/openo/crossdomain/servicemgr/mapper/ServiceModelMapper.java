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
package org.openo.crossdomain.servicemgr.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.openo.crossdomain.servicemgr.model.servicemo.ServiceModel;

/**
 * Interface for service model.<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
public interface ServiceModelMapper {

	/**
	 * Insert service model.<br/>
	 *
	 * @param serviceModel service data
	 * @since crossdomain 0.5
	 */
    void insert(ServiceModel serviceModel);

    /**
     * Delete service by ID<br/>
     *
     * @param tenant_id tenant ID
     * @param service_id service ID
     * @since crossdomain 0.5
     */
    void deleteByServiceID(@Param("tenant_id") String tenant_id, @Param("service_id") String service_id);

    /**
     * Update service.<br/>
     *
     * @param serviceModel service data
     * @since crossdomain 0.5
     */
    void update(ServiceModel serviceModel);

    /**
     * Get the collection of services by service ID.<br/>
     *
     * @param tenant_id tenant ID
     * @param service_id service ID
     * @return the collection of services
     * @since crossdomain 0.5
     */
    List<ServiceModel> getServiceModelByServiceId(@Param("tenant_id") String tenant_id,
            @Param("service_id") String service_id);

    /**
     * Get the collection of services by tenant ID.<br/>
     *
     * @param tenant_id tenant ID
     * @return the collection of services
     * @since crossdomain 0.5
     */
    List<ServiceModel> getServiceModelByTenantId(@Param("tenant_id") String tenant_id);

    /**
     * Get the collection of services by template ID.<br/>
     *
     * @param tenantID tenant ID
     * @param templateID template ID
     * @return the collection of services
     * @since crossdomain 0.5
     */
    List<ServiceModel> getServiceModelByTemplateId(@Param("tenant_id") String tenantID,
            @Param("template_id") String templateID);
}
