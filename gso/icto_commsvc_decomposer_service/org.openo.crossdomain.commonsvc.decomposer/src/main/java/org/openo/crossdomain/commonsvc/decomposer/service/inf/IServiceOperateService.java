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

import javax.xml.ws.spi.http.HttpContext;

import org.openo.crossdomain.commonsvc.decomposer.model.Result;

/**
 * The interface of logic layer for service decomposer flow.
 * @since   crossdomain 0.5
 */
public interface IServiceOperateService extends IService {

    /**
     * Query resource detail information by service ID.<br>
     *
     * @param context HTTP context.
     * @param serviceID Service ID.
     * @return the resource of service.
     * @throws throw ServiceException if query failed.
     * @since   crossdomain 0.5
     */
    Result<String> queryService(HttpContext context, String taskID) throws ServiceException;

    /**
     * Query service decomposer task executes process.<br>
     *
     * @param context HTTP context.
     * @param taskID task ID.
     * @return the process of service decomposer task.
     * @throws throw ServiceException if query failed.
     * @since   crossdomain 0.5
     */
    Result<String> queryExecuteProcess(HttpContext context, String taskID) throws ServiceException;

    /**
     * construct and execute decomposer task.<br>
     *
     * @param context HTTP context.
     * @param srvBody the input json.
     * @param action service decomposer action{create|update|delete|activate|deactivate}.
     * @return Result if task executes successed.
     * @throws throw ServiceException if decomposer task executes failed.
     * @since   crossdomain 0.5
     */
    Result<String> executeTask(HttpContext context, String srvBody, String action) throws ServiceException;

    /**
     * Query service decomposer task detail by service ID.<br>
     *
     * @param context HTTP context.
     * @param serviceID Service ID.
     * @return the detail of decomposer task.
     * @throws throw ServiceException if query failed.
     * @since   crossdomain 0.5
     */
    Result<String> queryTask(HttpContext context, String serviceID) throws ServiceException;

    /**
     * Query the log of service decomposer task.<br>
     *
     * @param context HTTP context.
     * @param taskID task ID.
     * @return service decomposer task log.
     * @throws throw ServiceException if query failed.
     * @since   crossdomain 0.5
     */
    Result<String> queryTaskLog(HttpContext context, String taskID) throws ServiceException;
}
