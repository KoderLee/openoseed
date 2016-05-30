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
package org.openo.crossdomain.commonsvc.executor.rest;

import org.openo.crossdomain.commonsvc.executor.common.constant.Constants;
import org.openo.crossdomain.commonsvc.executor.common.module.IResource;
import org.openo.crossdomain.commonsvc.executor.service.inf.ITimingTaskService;

import org.openo.commonservice.remoteservice.exception.ServiceException;
import org.openo.commonservice.roa.annotation.*;
import org.openo.commonservice.roa.common.HttpContext;
import org.openo.commonservice.roa.common.RequestInputStream;

/**
 * Service Executor Timing Task API
 * Useing CRON Timing mechanism supplied by CloudSOP Platform
 * encapsulated in form of Rest Service<br/>
 *
 * @author
 * @version crossdomain 0.5 2016-3-18
 */
@Path("/executor/v1/timingtask")
@Target("executor")
public class ServiceExecutorTimingTask extends IResource<ITimingTaskService> {

    /**
     * Get Rest URI<br/>
     *
     * @return Rest URI
     * @since crossdomain 0.5
     */
    @Override
    public String getResUri() {
        return "/executor/v1/timingtask";
    }

    /**
     * Delete Jobs by Timing Task
     * System Timing Task API triggered once a day
     * delete jobs that already completed over 30 days<br/>
     *
     * @param input Request Input Stream
     * @param context HttpContext
     * @throws ServiceException fail to execute the timing task
     * @since crossdomain 0.5
     */
    @POST
    @Path("/jobs/deletion")
    @Consumes({Constants.APPLICATION_JSON})
    @Produces({Constants.APPLICATION_JSON})
    public void deleteJobsByTimingTask(RequestInputStream input, HttpContext context) throws ServiceException {
        service.deleteJobsByTimingTask(input, context);
    }

    /**
     * Timing Task to Check Redis Capability
     * triggered every 5 minitues, check Redis Capability
     * delete jobs that already completed, when Redis Capability utilization is over 85% <br/>
     *
     * @throws ServiceException fail to execute the timing task
     * @since crossdomain 0.5
     */
    @POST
    @Path("/jobs/check_redis_full")
    @Consumes({Constants.APPLICATION_JSON})
    @Produces({Constants.APPLICATION_JSON})
    public void checkRedisFullByTimingTask() throws ServiceException {
        service.checkRedisFull();
    }

}
