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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import org.openo.sdno.remoteservice.exception.ServiceException;
import org.openo.sdno.inventory.sdk.inf.IInventoryDaoService;
import org.openo.sdno.inventory.sdk.model.TerminationPointMO;
import org.openo.sdno.result.Result;

@Repository("invTerminationPointMoDao")
public class InvTerminationPointMoDao extends CommonInvDao<TerminationPointMO> {

    @Autowired
    IInventoryDaoService<TerminationPointMO> invServices;

    @Override
    public Result<TerminationPointMO> query(String uuid) throws ServiceException {
        Result<TerminationPointMO> result = new Result<TerminationPointMO>();
        TerminationPointMO queryMo = new TerminationPointMO();
        queryMo.setUuid(uuid);
        Result<List<TerminationPointMO>> query = invServices.query(Arrays.asList(queryMo), TerminationPointMO.class);
        if(query != null && query.isValid() && !query.getResultObj().isEmpty()) {
            result.setResultObj(query.getResultObj().get(0));
        }
        return result;
    }

    /**
     * Query termination point mo by uuid.
     * 
     * @since SDNO 0.5
     */
    public Result<List<TerminationPointMO>> query(List<String> uuids) throws ServiceException {

        List<TerminationPointMO> terMoLst = new ArrayList<TerminationPointMO>();
        for(String uuid : uuids) {
            TerminationPointMO queryMo = new TerminationPointMO();
            queryMo.setUuid(uuid);
            terMoLst.add(queryMo);
        }
        return invServices.query(terMoLst, TerminationPointMO.class);
    }

    @Override
    public Result<List<TerminationPointMO>> query(String filter, String attr, String sort, String sortType)
            throws ServiceException {
        return super.query(TerminationPointMO.class, filter, attr, sort, sortType);
    }
}
