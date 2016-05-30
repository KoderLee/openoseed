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
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.openo.crossdomain.servicemgr.model.servicemo.ServiceParameter;

/**
 * Interface for mapper of service parameter.<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
public interface ServiceParameterMapper {

	/**
	 * Insert service parameter.<br/>
	 *
	 * @param serviceParameter service parameter
	 * @since crossdomain 0.5
	 */
    void insert(ServiceParameter serviceParameter);

    /**
     * Delete service parameters by service IDs.<br/>
     *
     * @param ids
     * @since crossdomain 0.5
     */
    void delete(List<Integer> ids);

    /**
     * Delete service parameter by service ID.<br/>
     *
     * @param tenant_id tenant ID
     * @param service_id service ID
     * @since crossdomain 0.5
     */
    void deleteByServiceID(@Param("tenant_id") String tenant_id, @Param("service_id") String service_id);

    /**
     * Update service parameter.<br/>
     *
     * @param serviceParameter service parameter
     * @since crossdomain 0.5
     */
    void update(ServiceParameter serviceParameter);

    /**
     * Get service parameter by service ID.<br/>
     *
     * @param id service ID
     * @return service parameter
     * @since crossdomain 0.5
     */
    ServiceParameter getServiceParameterById(String id);

    /**
     * Get the collection of service parameters.<br/>
     *
     * @param params collection of parameters
     * @return collection of parameters
     * @since crossdomain 0.5
     */
    List<ServiceParameter> getServiceParameterList(Map<String, Object> params);
}
