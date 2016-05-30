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
package org.openo.crossdomain.commonsvc.decomposer.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.exceptions.PersistenceException;
import org.openo.crossdomain.commonsvc.decomposer.model.ResMapping;

/**
 * The interface of mybatis Mapper for service resource.
 * 
 * @since crossdomain 0.5
 */
public interface ResMappingMapper {

	/**
	 * insert resource
	 * 
	 * @param resMapping resource
	 * @throws PersistenceException if insert failed
	 * @since crossdomain 0.5
	 */
	void insertResMapping(ResMapping resMapping) throws PersistenceException;

	/**
	 * Bulk insert resource
	 * 
	 * @param resMapping resource list
	 * @throws PersistenceException if insert failed
	 * @since crossdomain 0.5
	 */
	void batInsertResMapping(List<ResMapping> resMapping)
			throws PersistenceException;

	/**
	 * delete resource
	 * 
	 * @param resMapping resource
	 * @throws PersistenceException if delete failed
	 * @since crossdomain 0.5
	 */
	void deleteResMapping(ResMapping resMapping) throws PersistenceException;

	/**
	 * Bulk delete resource
	 * 
	 * @param resMapping resource list
	 * @throws PersistenceException if delete failed
	 * @since crossdomain 0.5
	 */
	void batDeleteResMapping(List<ResMapping> resMapping)
			throws PersistenceException;

	/**
	 * update resource
	 * 
	 * @param resMapping resource
	 * @throws PersistenceException if update failed
	 * @since crossdomain 0.5
	 */
	void updateResMapping(ResMapping resMapping) throws PersistenceException;

	/**
	 * Query service resources by resource ID and tenant ID.<br>
	 * 
	 * @param resID resource ID
	 * @param tenantID tenant ID
	 * @throws PersistenceException if query failed
	 * @since crossdomain 0.5
	 */
	ResMapping getResMapping(@Param("resID") String resID,
			@Param("tenantID") String tenantID) throws PersistenceException;

	/**
	 * Query service resources by service ID and tenant ID.<br>
	 * 
	 * @param serviceID service ID
	 * @param tenantID tenant ID
	 * @throws PersistenceException if query failed
	 * @since crossdomain 0.5
	 */
	List<ResMapping> getResMappings(@Param("serviceID") String serviceID,
			@Param("tenantID") String tenantID) throws PersistenceException;
}
