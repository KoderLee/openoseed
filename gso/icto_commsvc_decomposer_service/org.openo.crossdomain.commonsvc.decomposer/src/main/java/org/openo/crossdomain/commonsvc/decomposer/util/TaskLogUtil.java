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
package org.openo.crossdomain.commonsvc.decomposer.util;

import org.openo.crossdomain.commonsvc.decomposer.service.inf.ITaskLogService;

/**
 * task log util class
 * 
 * @since crossdomain 0.5
 */
public final class TaskLogUtil {

	/**
	 * distribute task log ID
	 * 
	 * @param taskID task ID
	 * @return task log ID
	 * @since crossdomain 0.5
	 */
	public synchronized static int distributeID(String taskID) {
		ITaskLogService taskLogService = (ITaskLogService) DecomposerContextHelper
				.getInstance().getBean("taskLogService");
		return taskLogService.getTaskLogCount(taskID) + 1;
	}
}
