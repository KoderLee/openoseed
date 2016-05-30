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

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.exceptions.PersistenceException;
import org.openo.crossdomain.commonsvc.decomposer.model.ServiceDecomposerTask;

/**
 * The interface of mybatis Mapper for task.
 * 
 * @since crossdomain 0.5
 */
public interface ServiceDecomposerTaskMapper {

	/**
	 * insert decomposition task
	 * 
	 * @param srvDecomposerTask task
	 * @throws PersistenceException if insert failed
	 * @since crossdomain 0.5
	 */
	void insertSrvDecomposerTask(ServiceDecomposerTask srvDecomposerTask)
			throws PersistenceException;

	/**
	 * delete decomposition task
	 * 
	 * @param srvDecomposerTask task
	 * @throws PersistenceException if delete failed
	 * @since crossdomain 0.5
	 */
	void deleteSrvDecomposerTask(ServiceDecomposerTask srvDecomposerTask)
			throws PersistenceException;

	/**
	 * update decomposition task
	 * 
	 * @param srvDecomposerTask task
	 * @throws PersistenceException if update failed
	 * @since crossdomain 0.5
	 */
	void updateSrvDecomposerTask(ServiceDecomposerTask srvDecomposerTask)
			throws PersistenceException;

	/**
	 * Query decomposition task by service ID and tenant ID.<br>
	 * 
	 * @param serviceID service ID
	 * @param tenantID tenant ID
	 * @return task list
	 * @throws PersistenceException if query failed
	 * @since crossdomain 0.5
	 */
	List<ServiceDecomposerTask> getSrvDecomposerTask(
			@Param("serviceID") String serviceID,
			@Param("tenantID") String tenantID) throws PersistenceException;

	/**
	 * Query decomposition task by task ID and tenant ID.<br>
	 * 
	 * @param taskID task ID
	 * @param tenantID tenant ID
	 * @return task
	 * @throws PersistenceException if query failed
	 * @since crossdomain 0.5
	 */
	ServiceDecomposerTask qrySrvDecomposerTask(@Param("taskID") String taskID,
			@Param("tenantID") String tenantID) throws PersistenceException;
}
