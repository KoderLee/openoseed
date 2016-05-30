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
import org.openo.sdno.vpn.wan.db.vpn.PathInfoPo;
import org.openo.sdno.vpn.wan.servicemodel.vpn.PathInfo;

@Repository("pathInfoDao")
public class PathInfoDao extends DafaultDao<PathInfoPo, PathInfo> {

    @Override
    protected Class<PathInfoPo> getPoClass() {
        return PathInfoPo.class;
    }

    @Override
    public void addMos(List<PathInfo> mos) throws ServiceException {
        final List<PathInfoPo> pos = DaoUtil.batchMoConvert(mos, PathInfoPo.class);
        insert(pos);
    }

    @Override
    public boolean delMos(List<PathInfo> mos) throws ServiceException {
        final List<PathInfoPo> pos = DaoUtil.batchMoConvert(mos, PathInfoPo.class);
        return delete(pos);
    }

    @Override
    public boolean updateMos(List<PathInfo> mos) throws ServiceException {
        return update(DaoUtil.batchMoConvert(mos, PathInfoPo.class));
    }

    @Override
    public List<PathInfo> assembleMo(List<PathInfoPo> pos) throws ServiceException {
        return DaoUtil.batchPoConvert(pos, PathInfo.class);
    }
}
