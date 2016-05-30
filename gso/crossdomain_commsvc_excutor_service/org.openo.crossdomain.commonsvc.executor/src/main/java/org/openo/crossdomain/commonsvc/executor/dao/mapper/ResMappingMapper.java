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

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.exceptions.PersistenceException;
import org.openo.crossdomain.commonsvc.executor.dao.inf.IServiceResDao;
import org.openo.crossdomain.commonsvc.executor.model.db.ResourceForDB;

public interface ResMappingMapper {

    void insert(ResourceForDB resource) throws PersistenceException;

    void delete(ResourceForDB resource) throws PersistenceException;

    void deleteByStatus(final @Param("jobId") String jobId, final @Param("status") String status)
            throws PersistenceException;

    void update(ResourceForDB resource) throws PersistenceException;

    void updateAsyncStatus(ResourceForDB resource) throws PersistenceException;

    List<ResourceForDB> getAllResourcesOfJob(final @Param("jobId") String jobId) throws PersistenceException;
}
