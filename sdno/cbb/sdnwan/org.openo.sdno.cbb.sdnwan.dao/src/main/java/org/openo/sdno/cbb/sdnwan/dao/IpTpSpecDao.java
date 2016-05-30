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
import org.openo.sdno.vpn.wan.db.tp.IpTpSpecPo;
import org.openo.sdno.vpn.wan.servicemodel.tp.IpTpSpec;

@Repository("ipTpSpecDao")
public class IpTpSpecDao extends DafaultDao<IpTpSpecPo, IpTpSpec> {

    @Override
    protected Class<IpTpSpecPo> getPoClass() {
        return IpTpSpecPo.class;
    }

    @Override
    public void addMos(List<IpTpSpec> mos) throws ServiceException {
        final List<IpTpSpecPo> ipTpSpecPos = DaoUtil.batchMoConvert(mos, IpTpSpecPo.class);
        insert(ipTpSpecPos);
    }

    @Override
    public boolean delMos(List<IpTpSpec> mos) throws ServiceException {
        final List<IpTpSpecPo> ipTpSpecPos = DaoUtil.batchMoConvert(mos, IpTpSpecPo.class);
        return delete(ipTpSpecPos);
    }

    @Override
    public boolean updateMos(List<IpTpSpec> mos) throws ServiceException {
        return update(DaoUtil.batchMoConvert(mos, IpTpSpecPo.class));
    }

    @Override
    public List<IpTpSpec> assembleMo(List<IpTpSpecPo> pos) throws ServiceException {
        return DaoUtil.batchPoConvert(pos, IpTpSpec.class);
    }
}
