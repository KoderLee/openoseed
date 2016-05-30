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
package org.openo.crossdomain.commonsvc.executor.dao.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.exceptions.PersistenceException;
import org.openo.crossdomain.commonsvc.executor.dao.inf.IServiceJobDao;
import org.openo.crossdomain.commonsvc.executor.model.db.ServiceJobForDB;

import java.util.List;

public interface ServiceExecutorJobMapper {

    void insertExecutorJob(ServiceJobForDB job) throws PersistenceException;

    void deleteExecutorJob(ServiceJobForDB job) throws PersistenceException;

    void updateExecutorJob(ServiceJobForDB job) throws PersistenceException;

    List<ServiceJobForDB> getAllJobs() throws PersistenceException;

    List<ServiceJobForDB> getJobsByJobId(@Param("jobIdList") List<String> jobIdList) throws PersistenceException;

    ServiceJobForDB getJob(@Param("jobId") String jobId) throws PersistenceException;

    List<ServiceJobForDB> getJobByServiceID(@Param("serviceID") String serviceId, @Param("startIndex") int startIndex,
            @Param("pageCapacity") int pageCapacity) throws PersistenceException;

    int getNotCompletedJobCount() throws PersistenceException;

    List<String> getJobId(@Param("statusList") List<String> statusList, @Param("createdTime") long createdTime);

}
