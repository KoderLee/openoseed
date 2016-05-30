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
import org.openo.sdno.vpn.wan.db.relation.VpnTpRelationPo;
import org.openo.sdno.vpn.wan.servicemodel.relation.VpnTpRelation;

@Repository("vpnTpRelationDao")
public class VpnTpRelationDao extends DafaultDao<VpnTpRelationPo, VpnTpRelation> {

    /**
     * Query terminate point relation by VPN basic information id.
     * 
     * @since SDNO 0.5
     */
    public List<VpnTpRelation> selectByVpnBasicInfoId(String vpnBasicInfoId) throws ServiceException {
        final List<VpnTpRelationPo> vpnTpRelationPos = selectByCondition("vpnBasicInfoId", vpnBasicInfoId, true);
        return DaoUtil.batchPoConvert(vpnTpRelationPos, VpnTpRelation.class);
    }

    /**
     * Select terminate point relation by VPN basic information ids.
     * 
     * @since SDNO 0.5
     */
    public List<VpnTpRelation> selectByVpnBasicInfoIds(List<String> vpnBasicInfoIds) throws ServiceException {
        final List<VpnTpRelationPo> vpnTpRelationPos = selectByConditions("vpnBasicInfoId", vpnBasicInfoIds);
        return DaoUtil.batchPoConvert(vpnTpRelationPos, VpnTpRelation.class);
    }

    /**
     * Select terminate point relation by terminate point id.
     * 
     * @since SDNO 0.5
     */
    public List<VpnTpRelation> selectByTpId(String tpId) throws ServiceException {
        final List<VpnTpRelationPo> vpnTpRelationPos = selectByCondition("tpId", tpId, true);
        return DaoUtil.batchPoConvert(vpnTpRelationPos, VpnTpRelation.class);
    }
    
    /**
     * Select terminate point by terminate ids.
     * 
     * @since SDNO 0.5
     */
    public List<VpnTpRelation> selectByTpIds(List<String> tpIds) throws ServiceException {
        final List<VpnTpRelationPo> vpnTpRelationPos = selectByConditions("tpId", tpIds);
        return DaoUtil.batchPoConvert(vpnTpRelationPos, VpnTpRelation.class);
    }

    @Override
    protected Class<VpnTpRelationPo> getPoClass() {
        return VpnTpRelationPo.class;
    }

    @Override
    public void addMos(List<VpnTpRelation> mos) throws ServiceException {
        insert(DaoUtil.batchMoConvert(mos, VpnTpRelationPo.class));
    }

    @Override
    public boolean delMos(List<VpnTpRelation> mos) throws ServiceException {
        return delete(DaoUtil.batchMoConvert(mos, VpnTpRelationPo.class));
    }

    @Override
    public boolean updateMos(List<VpnTpRelation> mos) throws ServiceException {
        return update(DaoUtil.batchMoConvert(mos, VpnTpRelationPo.class));
    }

    @Override
    public List<VpnTpRelation> assembleMo(List<VpnTpRelationPo> pos) throws ServiceException {
        return DaoUtil.batchPoConvert(pos, VpnTpRelation.class);
    }
}
