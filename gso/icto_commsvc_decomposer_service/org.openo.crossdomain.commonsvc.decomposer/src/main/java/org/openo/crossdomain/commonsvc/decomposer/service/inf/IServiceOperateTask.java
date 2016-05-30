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

import org.openo.crossdomain.commonsvc.decomposer.logutil.TaskHeader;
import org.openo.crossdomain.commonsvc.decomposer.model.ServiceDecomposerTask;

/**
 * The interface for service decomposer task update.
 * @since   crossdomain 0.5
 */
public interface IServiceOperateTask extends IService {

    /**
     * Update the task status and write task log.<br>
     *
     * @param taskHeader contain task ID, tenant ID and user information.
     * @param progress task execute progress.
     * @param result task execute result on current time.
     * @param resultReason the reason of result.
     * @throws throw ServiceException if update failed.
     * @since   crossdomain 0.5
     */
    public void updateTaskStatusAndWriteLog(final TaskHeader taskHeader, final String progress, final String result,
            final String resultReason) throws ServiceException;

    /**
     * Update the task status and write task log.<br>
     *
     * @param task service decomposer task.
     * @param progress task execute progress.
     * @param result task execute result on current time.
     * @param resultReason the reason of result.
     * @throws throw ServiceException if update failed.
     * @since   crossdomain 0.5
     */
    public ServiceDecomposerTask updateTaskStatusAndWriteLog(final ServiceDecomposerTask task, final String progress,
            final String result, final String resultReason) throws ServiceException;

    /**
     * Update the task status.<br>
     *
     * @param taskHeader contain task ID, tenant ID and user information.
     * @param progress task execute progress.
     * @param result task execute result on current time.
     * @param resultReason the reason of result.
     * @throws throw ServiceException if update failed.
     * @since   crossdomain 0.5
     */
    public ServiceDecomposerTask updateTaskStatus(final TaskHeader taskHeader, final String progress,
            final String result, final String resultReason) throws ServiceException;

    /**
     * Get task detail information.<br>
     *
     * @param taskID task ID.
     * @param tenantID tenant ID.
     * @return the detail of service decomposer task.
     * @throws throw ServiceException if query failed.
     * @since   crossdomain 0.5
     */
    public ServiceDecomposerTask getTask(final String taskID, final String tenantID) throws ServiceException;

    /**
     * Update job ID of service decomposer task.<br>
     *
     * @param taskID task ID.
     * @param tenantID tenant ID.
     * @param jobID job ID.
     * @throws throw ServiceException if update failed.
     * @since   crossdomain 0.5
     */
    public void updateTaskJobID(final String taskID, final String tenantID, final String jobID) throws ServiceException;
}
