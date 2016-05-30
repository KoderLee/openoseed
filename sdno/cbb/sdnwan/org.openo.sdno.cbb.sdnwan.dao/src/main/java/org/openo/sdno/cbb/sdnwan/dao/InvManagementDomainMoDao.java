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
import org.openo.sdno.inventory.sdk.model.ManagementDomainMO;
import org.openo.sdno.result.Result;

@Repository("invManagementDomainMoDao")
public class InvManagementDomainMoDao extends CommonInvDao<ManagementDomainMO> {

    @Override
    public Result<ManagementDomainMO> query(String uuid) throws ServiceException {
        return super.query(uuid, ManagementDomainMO.class);
    }

    @Override
    public Result<List<ManagementDomainMO>> query(String filter, String attr, String sort, String sortType)
            throws ServiceException {
        return super.query(ManagementDomainMO.class, filter, attr, sort, sortType);
    }
}
