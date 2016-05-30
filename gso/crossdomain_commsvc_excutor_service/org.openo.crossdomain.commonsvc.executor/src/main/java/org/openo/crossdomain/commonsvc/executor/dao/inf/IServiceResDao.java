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

import org.openo.crossdomain.commonsvc.executor.common.enums.ExecutionStatus;
import org.openo.crossdomain.commonsvc.executor.model.Resource;
import org.openo.crossdomain.commonsvc.executor.model.db.Result;

public interface IServiceResDao {

    Result insert(@NotNull String jobId, @NotNull String serviceId, @NotNull Resource resource);

    Result delete(@NotNull String jobId, @NotNull Resource resource);

    Result deleteByStatus(@NotNull String jobId, @NotNull ExecutionStatus status);

    Result update(@NotNull String jobId, @NotNull Resource resource);

    Result updateAsyncStatus(@NotNull String jobId, @NotNull Resource resource);

    @NotNull
    List<Resource> getAllResourcesOfJob(@NotNull String jobId) throws SQLException;
}
