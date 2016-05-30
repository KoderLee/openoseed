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
package org.openo.crossdomain.commonsvc.decomposer.dao.inf;

import java.util.List;

import org.openo.crossdomain.commonsvc.decomposer.model.Result;
import org.openo.crossdomain.commonsvc.decomposer.model.ServiceDecomposerTask;

import org.openo.commonservice.remoteservice.exception.ServiceException;

/**
 * The interface of dao layer for service operation.
 * 
 * @since crossdomain 0.5
 */
public interface IServiceOperateDao {

	/**
	 * insert task
	 * 
	 * @param task the task
	 * @return insert result
	 * @throws ServiceException if insert failed.
	 * @since crossdomain 0.5
	 */
	Result<Object> insert(ServiceDecomposerTask task) throws ServiceException;

	/**
	 * delete task
	 * 
	 * @param task the task
	 * @return delete result
	 * @throws ServiceException if delete failed.
	 * @since crossdomain 0.5
	 */
	Result<Object> delete(ServiceDecomposerTask task) throws ServiceException;

	/**
	 * update task
	 * 
	 * @param task the task
	 * @return update result
	 * @throws ServiceException if update failed.
	 * @since crossdomain 0.5
	 */
	Result<Object> update(ServiceDecomposerTask task) throws ServiceException;

	/**
	 * Query service decomposer task list by service ID and tenant ID.<br>
	 * 
	 * @param serviceID Service ID.
	 * @param tenantID tenant ID.
	 * @return the task list
	 * @throws ServiceException if query failed.
	 * @since crossdomain 0.5
	 */
	Result<List<ServiceDecomposerTask>> getSDTaskBySvcID(String serviceID,
			String tenantID) throws ServiceException;

	/**
	 * Query service decomposer task detail by task ID and tenant ID.<br>
	 * 
	 * @param taskID task ID.
	 * @param tenantID tenant ID.
	 * @return the task list
	 * @throws ServiceException if query failed.
	 * @since crossdomain 0.5
	 */
	Result<ServiceDecomposerTask> getTaskByTaskID(String taskID, String tenantID)
			throws ServiceException;
}
