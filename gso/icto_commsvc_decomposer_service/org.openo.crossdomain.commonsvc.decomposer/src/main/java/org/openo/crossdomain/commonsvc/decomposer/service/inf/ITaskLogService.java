/*******************************************************************************
 * Copyright (c) 2016, Huawei Technologies Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package org.openo.crossdomain.commonsvc.decomposer.service.inf;

import java.util.List;

import javax.xml.ws.spi.http.HttpContext;

import org.openo.crossdomain.commonsvc.decomposer.model.Result;
import org.openo.crossdomain.commonsvc.decomposer.model.TaskLog;

/**
 * The interface for service decomposer task log.
 * 
 * @since crossdomain 0.5
 */
public interface ITaskLogService {

	/**
	 * Get task log detail information.<br>
	 * 
	 * @param context HTTP context.
	 * @param taskID task ID.
	 * @return the detail of service decomposer task log.
	 * @throws throw ServiceException if query failed.
	 * @since crossdomain 0.5
	 */
	Result<List<TaskLog>> getTaskLog(HttpContext context, String taskID)
			throws ServiceException;

	/**
	 * insert task log data to database.<br>
	 * 
	 * @param taskLog task execute log.
	 * @return operate result.
	 * @throws throw ServiceException if insert failed.
	 * @since crossdomain 0.5
	 */
	Result<String> insertLog(TaskLog taskLog) throws ServiceException;

	/**
	 * construct task log data and insert to database.<br>
	 * 
	 * @param taskID task ID.
	 * @param progress task execute progress.
	 * @param taskLog task execute log.
	 * @param result task execute result on current time.
	 * @param resultReason the reason of result.
	 * @return operate result.
	 * @throws throw ServiceException if insert failed.
	 * @since crossdomain 0.5
	 */
	Result<String> insertLog(String taskID, final String progress,
			final String result, final String resultReason);

	/**
	 * Query the quantity of task log.<br>
	 * 
	 * @param taskID task ID.
	 * @return the quantity of task log.
	 * @since crossdomain 0.5
	 */
	int getTaskLogCount(String taskID);
}
