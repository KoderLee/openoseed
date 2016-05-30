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

import org.springframework.stereotype.Repository;

import org.openo.sdno.remoteservice.exception.ServiceException;
import org.openo.sdno.inventory.sdk.model.SiteMO;
import org.openo.sdno.result.Result;

@Repository("invSiteMoDao")
public class InvSiteMoDao extends CommonInvDao<SiteMO> {

    @Override
    public Result<SiteMO> query(String uuid) throws ServiceException {
        return super.query(uuid, SiteMO.class);
    }

    @Override
    public Result<List<SiteMO>> query(String filter, String attr, String sort, String sortType) throws ServiceException {
        return super.query(SiteMO.class, filter, attr, sort, sortType);
    }
}
