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
package org.openo.crossdomain.commonsvc.executor.dao.inf;

import java.sql.SQLException;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.apache.ibatis.exceptions.PersistenceException;
import org.openo.crossdomain.commonsvc.executor.dao.mapper.ServiceExecutorJobMapper;
import org.openo.crossdomain.commonsvc.executor.model.ServiceJob;
import org.openo.crossdomain.commonsvc.executor.model.db.Result;

public interface IServiceJobDao {

    Result insert(@NotNull ServiceJob job);

    Result delete(@NotNull ServiceJob job);

    Result update(@NotNull ServiceJob job);

    @NotNull
    List<ServiceJob> getAllJobs() throws SQLException;

    List<ServiceJob> getJobsByJobId(List<String> jobIdList) throws PersistenceException;

    ServiceJob getJob(@NotNull String jobId) throws SQLException;

    @NotNull
    List<ServiceJob> getJobByServiceID(String serviceId, int startIndex, int pageCapacity) throws SQLException;

    int getNotCompletedJobCount() throws SQLException;

    List<String> getJobId(List<String> statusList, long createTime) throws PersistenceException;
}
