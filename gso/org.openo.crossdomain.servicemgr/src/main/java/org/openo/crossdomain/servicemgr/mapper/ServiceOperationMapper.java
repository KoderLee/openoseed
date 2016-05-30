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
import org.openo.crossdomain.servicemgr.model.servicemo.ServiceOperation;

import org.openo.baseservice.remoteservice.exception.ServiceException;

/**
 * Interface for mapper of service operation.<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
public interface ServiceOperationMapper {

	/**
	 * Insert service Operation.<br/>
	 *
	 * @param serviceOperation service operation
	 * @since crossdomain 0.5
	 */
    void insert(ServiceOperation serviceOperation);

    /**
     * Update service Operation<br/>
     *
     * @param serviceOperation service Operation
     * @since crossdomain 0.5
     */
    void update(ServiceOperation serviceOperation);

    /**
     * Delete service Operation by service ID.<br/>
     *
     * @param serivceId service ID
     * @since crossdomain 0.5
     */
    void delete(@Param("service_id") String serivceId);

    /**
     * Get the collection of service operation by service ID.<br/>
     *
     * @param serivceId service ID
     * @return the collection of service operation
     * @since crossdomain 0.5
     */
    List<ServiceOperation> getServiceOperationsByServiceID(@Param("service_id") String serivceId);

    /**
     * Get Service Operation by ID.<br/>
     *
     * @param serivceId service ID
     * @param operationId operation ID
     * @return service operation
     * @since crossdomain 0.5
     */
    ServiceOperation getServiceOperationByID(@Param("service_id") String serivceId,
            @Param("operation_id") String operationId);

    /**
     * Delete the record of operation.<br/>
     *
     * @since crossdomain 0.5
     */
    void deleteHistory();
}
