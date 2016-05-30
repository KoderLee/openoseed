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
import org.openo.sdno.vpn.wan.db.tp.EthernetTpSpecPo;
import org.openo.sdno.vpn.wan.servicemodel.tp.EthernetTpSpec;

@Repository("ethernetTpSpecDao")
public class EthernetTpSpecDao extends DafaultDao<EthernetTpSpecPo, EthernetTpSpec> {

    @Override
    protected Class<EthernetTpSpecPo> getPoClass() {
        return EthernetTpSpecPo.class;
    }

    @Override
    public void addMos(List<EthernetTpSpec> mos) throws ServiceException {
        final List<EthernetTpSpecPo> ethernetTpSpecPos = DaoUtil.batchMoConvert(mos, EthernetTpSpecPo.class);
        insert(ethernetTpSpecPos);
    }

    @Override
    public boolean delMos(List<EthernetTpSpec> mos) throws ServiceException {
        final List<EthernetTpSpecPo> ethernetTpSpecPos = DaoUtil.batchMoConvert(mos, EthernetTpSpecPo.class);
        return delete(ethernetTpSpecPos);
    }

    @Override
    public boolean updateMos(List<EthernetTpSpec> mos) throws ServiceException {
        return update(DaoUtil.batchMoConvert(mos, EthernetTpSpecPo.class));
    }

    @Override
    public List<EthernetTpSpec> assembleMo(List<EthernetTpSpecPo> pos) throws ServiceException {
        return DaoUtil.batchPoConvert(pos, EthernetTpSpec.class);
    }

}
