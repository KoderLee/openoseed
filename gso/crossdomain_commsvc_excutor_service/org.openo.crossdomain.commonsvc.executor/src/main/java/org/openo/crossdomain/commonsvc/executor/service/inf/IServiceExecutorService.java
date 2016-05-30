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

import java.util.List;

import org.openo.crossdomain.commonsvc.executor.common.module.IService;
import org.openo.crossdomain.commonsvc.executor.model.ServiceJob;

import org.openo.commonservice.remoteservice.exception.ServiceException;
import org.openo.commonservice.roa.common.HttpContext;
import org.openo.commonservice.roa.common.RequestInputStream;

/**
 * Service Executor Service interface class<br/>
 *
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
public interface IServiceExecutorService extends IService {

    /**
     * Execute Job Service<br/>
     *
     * @param input Request Input Stream
     * @param context HttpContext
     * @return an url using for query job detail (with key of "location")
     * @throws ServiceException when fail to execute job
     * @since crossdomain 0.5
     */
    String executeJob(RequestInputStream input, HttpContext context) throws ServiceException;

    /**
     * <br/>
     *
     * @param tenantId Tenant id
     * @param jobId UUID of the job
     * @return job detail in form of ServiceJob model
     * @throws ServiceException when fail to get the job detail
     * @since crossdomain 0.5
     */
    ServiceJob getJobDetail(String tenantId, String jobId) throws ServiceException;

    /**
     * Get Job List<br/>
     *
     * @param serviceId UUID of ServiceInfo
     * @param pageIndex page number
     * @param pageCapacity Maximum number of items can be displayed in one page
     * @return job details in form of ServiceJob List
     * @throws ServiceException when fail to get job list
     * @since crossdomain 0.5
     */
    List<ServiceJob> getJobList(String serviceId, String pageIndex, String pageCapacity) throws ServiceException;
}
