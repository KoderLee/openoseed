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
package org.openo.crossdomain.commonsvc.executor.service.inf;

import org.openo.crossdomain.commonsvc.executor.common.module.IService;

import org.openo.commonservice.remoteservice.exception.ServiceException;
import org.openo.commonservice.roa.common.HttpContext;
import org.openo.commonservice.roa.common.RequestInputStream;

/**
 * Service Executor Timing Task<br/>
 *
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
public interface ITimingTaskService extends IService {

    /**
     * System Timing Task API triggered once a day
     * delete jobs that already completed over 30 days<br/>
     *
     * @param input RequestInputStream
     * @param context HttpContext
     * @throws ServiceException
     * @since crossdomain 0.5
     */
    void deleteJobsByTimingTask(RequestInputStream input, HttpContext context) throws ServiceException;

    /**
     * Timing Task to Check Redis Capability
     * triggered every 5 minitues, check Redis Capability
     * delete jobs that already completed, when Redis Capability utilization is over 85% <br/>
     *
     * @throws ServiceException fail to execute the timing task
     * @since crossdomain 0.5
     */
    void checkRedisFull() throws ServiceException;

    /**
     * Clear Completed Job From Redis<br/>
     *
     * @since crossdomain 0.5
     */
    void cleanCompletedJob();
}
