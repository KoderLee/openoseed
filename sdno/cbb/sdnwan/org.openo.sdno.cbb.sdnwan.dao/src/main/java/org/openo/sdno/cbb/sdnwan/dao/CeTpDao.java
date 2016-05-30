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

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Repository;

import org.openo.sdno.remoteservice.exception.ServiceException;
import org.openo.sdno.vpn.wan.db.tp.CeTpPo;
import org.openo.sdno.vpn.wan.servicemodel.tp.CeTp;

@Repository("ceTpDao")
public class CeTpDao extends DafaultDao<CeTpPo, CeTp> {

    @Override
    protected Class<CeTpPo> getPoClass() {
        return CeTpPo.class;
    }

    /**
     * Delete Po by uuid.
     * @since SDNO 0.5
     */
    public boolean deleteById(String uuid) throws ServiceException {
        final CeTpPo ceTpPo = new CeTpPo();
        ceTpPo.setUuid(uuid);
        return delete(Collections.singletonList(ceTpPo));
    }

    @Override
    public void addMos(List<CeTp> mos) throws ServiceException {
        insert(DaoUtil.batchMoConvert(mos, CeTpPo.class));
    }

    @Override
    public boolean delMos(List<CeTp> mos) throws ServiceException {
        final List<CeTpPo> ceTpPos = DaoUtil.batchMoConvert(mos, CeTpPo.class);
        return delete(ceTpPos);
    }

    @Override
    public boolean updateMos(List<CeTp> mos) throws ServiceException {
        return update(DaoUtil.batchMoConvert(mos, CeTpPo.class));
    }

    @Override
    public List<CeTp> assembleMo(List<CeTpPo> pos) throws ServiceException {
        return DaoUtil.batchPoConvert(pos, CeTp.class);
    }
}
