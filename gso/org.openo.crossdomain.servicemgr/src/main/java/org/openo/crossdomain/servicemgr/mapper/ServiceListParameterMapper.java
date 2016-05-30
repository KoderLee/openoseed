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
import org.openo.crossdomain.servicemgr.model.servicemo.ServiceListParameter;


/**
 * Interface for mapper of service parameter which type is list.<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
public interface ServiceListParameterMapper {

	/**
	 * Insert data.<br/>
	 *
	 * @param serviceListParameter service parameter which type is list.
	 * @since crossdomain 0.5
	 */
    void insert(ServiceListParameter serviceListParameter);

    /**
     * Delete by service ID.<br/>
     *
     * @param service_id service id
     * @since crossdomain 0.5
     */
    void deleteByServiceID(@Param("service_id") String service_id);

    /**
     * Delete by the name of parameter.<br/>
     *
     * @param service_id service ID
     * @param paramgroup_name the name of parameter group
     * @param key_name the name of parameter
     * @since crossdomain 0.5
     */
    void deleteByKeyName(@Param("service_id") String service_id, @Param("paramgroup_name") String paramgroup_name,
            @Param("key_name") String key_name);

    /**
     * Update parameter.<br/>
     *
     * @param serviceListParameter service parameter which type is list.
     * @since crossdomain 0.5
     */
    void update(ServiceListParameter serviceListParameter);

    /**
     * Get the list-form parameters by name.<br/>
     *
     * @param service_id service ID
     * @param paramgroup_name the name of parameter group
     * @param key_name the name of parameter
     * @return service parameters which type is list
     * @since crossdomain 0.5
     */
    ServiceListParameter getServiceListParameterByKeyName(@Param("service_id") String service_id,
            @Param("paramgroup_name") String paramgroup_name, @Param("key_name") String key_name);

    /**
     * Get the collection of list-form service parameter.<br/>
     *
     * @param svcIDs service IDs
     * @return the collection of list-from service parameter
     * @since crossdomain 0.5
     */
    List<ServiceListParameter> getServiceListParameterList(List<String> svcIDs);
}
