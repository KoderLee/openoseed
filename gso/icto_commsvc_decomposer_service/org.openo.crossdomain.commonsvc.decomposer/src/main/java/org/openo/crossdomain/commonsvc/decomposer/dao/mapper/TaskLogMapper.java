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

import org.apache.ibatis.exceptions.PersistenceException;
import org.openo.crossdomain.commonsvc.decomposer.model.TaskLog;

/**
 * The interface of mybatis Mapper for task log.
 * 
 * @since crossdomain 0.5
 */
public interface TaskLogMapper {

	/**
	 * insert task log
	 * 
	 * @param taskLog task log
	 * @throws PersistenceException if insert failed
	 * @since crossdomain 0.5
	 */
	void insert(TaskLog taskLog) throws PersistenceException;

	/**
	 * Query task log by task ID.<br>
	 * 
	 * @param taskID task ID
	 * @throws PersistenceException if query failed
	 * @since crossdomain 0.5
	 */
	List<TaskLog> getTaskLogByTaskID(String taskID) throws PersistenceException;

	/**
	 * Query the number of task log by task ID.<br>
	 * 
	 * @param taskID task ID
	 * @throws PersistenceException if query failed
	 * @since crossdomain 0.5
	 */
	int getTaskLogCountByTaskID(String taskID) throws PersistenceException;
}
