/*
 * Copyright (c) 2016, Huawei Technologies Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *  
 */
package org.openo.sdno.cbb.sdnwan.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.openo.sdno.remoteservice.exception.ServiceException;
import org.openo.sdno.inventory.sdk.inf.IInvDAO;
import org.openo.sdno.inventory.sdk.model.QueryParams;
import org.openo.sdno.inventory.sdk.model.RelationMO;
import org.openo.sdno.result.Result;

abstract class CommonInvDao<M> {

    @Autowired
    protected IInvDAO<M> invDao;

    /**
     * Query by relation.
     * 
     * @since SDNO 0.5
     */
    public Result<List<M>> queryByRelation(RelationMO relation) throws ServiceException {
        return invDao.queryByRelation(relation);
    }

    /**
     * Query by uuid and mo type.
     * @since SDNO 0.5
     */
    protected Result<M> query(String uuid, Class<M> moType) throws ServiceException {
        return invDao.query(uuid, moType);
    }

    /**
     * Query by mo type filter conditions and sort result by sort type.
     * 
     * @since SDNO 0.5
     */
    protected Result<List<M>> query(Class<M> moType, String filter, String attr, String sort, String sortType)
            throws ServiceException {
        return invDao.query(moType, new QueryParams(filter, attr, sort, sortType));
    }

    /**
     * Query by uuid.
     * 
     * @since SDNO 0.5
     */
    public abstract Result<M> query(String uuid) throws ServiceException;

    /**
     * Query by filter conditions and sort results by sort type.
     * 
     * @since SDNO 0.5
     */
    public abstract Result<List<M>> query(String filter, String attr, String sort, String sortType)
            throws ServiceException;
}
