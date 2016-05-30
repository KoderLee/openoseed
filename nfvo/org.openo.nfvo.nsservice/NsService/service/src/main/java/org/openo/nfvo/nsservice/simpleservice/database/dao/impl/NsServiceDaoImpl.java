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

package org.openo.nfvo.nsservice.simpleservice.database.dao.impl;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.openo.nfvo.nsservice.simpleservice.database.dao.api.AbstractDao;
import org.openo.nfvo.nsservice.simpleservice.database.dao.api.NsServiceDao;
import org.openo.nfvo.nsservice.simpleservice.database.mapper.NsServiceMapper;
import org.openo.nfvo.nsservice.simpleservice.entity.NsService;

import org.openo.baseservice.remoteservice.exception.ServiceException;

/**
 * 
*  The implement of NsService Dao abstract dao class<br/>
* <p>
* </p>
* 
* @author
* @version NFVO 0.5 May 15, 2016
 */
public class NsServiceDaoImpl extends AbstractDao implements NsServiceDao {

    @Override
    public int insertNsService(NsService nsBean) throws ServiceException {
        if(null == nsBean.getId() || nsBean.getId().isEmpty()) {
            nsBean.setId(UUID.randomUUID().toString());
        }
        return this.getMapperManager(NsServiceMapper.class).insert(nsBean);
    }

    @Override
    public int deleteNsService(String id) throws ServiceException {
        return this.getMapperManager(NsServiceMapper.class).deleteById(id);
    }

    @Override
    public int updateNsService(NsService nsBean) throws ServiceException {
        return this.getMapperManager(NsServiceMapper.class).updateByPrimaryKeySelective(nsBean);
    }

    @Override
    public NsService getNsServiceByPrimaryKey(String id) {
        return this.getMapperManager(NsServiceMapper.class).selectByPrimaryKey(id);
    }

    @Override
    public List<NsService> getNsServiceByServiceId(Map<String, Object> condition) {
        return this.getMapperManager(NsServiceMapper.class).selectByServiceId(condition);
    }
}
