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
import org.openo.crossdomain.commonsvc.decomposer.model.TaskLog;

import org.openo.commonservice.remoteservice.exception.ServiceException;

/**
 * The interface of dao layer for task log.
 * 
 * @since crossdomain 0.5
 */
public interface ITaskLogDao {

	/**
	 * insert task log
	 * 
	 * @param taskLog task log
	 * @return insert result
	 * @throws ServiceException if insert failed
	 * @since crossdomain 0.5
	 */
	Result<Object> insert(TaskLog taskLog) throws ServiceException;

	/**
	 * Query task logs by task ID .<br>
	 * 
	 * @param taskID task ID
	 * @return the result of task log list
	 * @throws ServiceException if query failed
	 * @since crossdomain 0.5
	 */
	Result<List<TaskLog>> getTaskLogByTaskID(String taskID)
			throws ServiceException;

	/**
	 * Query the number of task logs by task ID .
	 * 
	 * @param taskID task ID.
	 * @return the number of task logs
	 * @since crossdomain 0.5
	 */
	int getTaskLogCountByTaskID(String taskID);
}
