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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import org.openo.sdno.remoteservice.exception.ServiceException;
import org.openo.sdno.vpn.wan.db.relation.VpnTpRelationPo;
import org.openo.sdno.vpn.wan.db.vpn.VpnPo;
import org.openo.sdno.vpn.wan.servicemodel.vpn.Vpn;
import org.openo.sdno.vpn.wan.servicemodel.vpn.VpnBasicInfo;

@Repository("vpnDao")
public class VpnDao extends DafaultDao<VpnPo, Vpn> {

    @Autowired
    private VpnBasicInfoDao vpnBasicInfoDao;

    @Autowired
    private VpnTpRelationDao vpnTpRelationDao;

    /**
     * Query dao by uuid.
     * 
     * @since SDNO 0.5
     */
    public Vpn queryById(String uuid) throws ServiceException {
        return getMoById(uuid);
    }

    /**
     * Add mo.
     * 
     * @since SDNO 0.5
     */
    public void add(Vpn mo) throws ServiceException {
        addMos(Collections.singletonList(mo));
    }

    /**
     * Query ids by Overlay VPN base information id.
     * 
     * @since SDNO 0.5
     */
    public List<String> queryIdsByOverlayVpnBaseInfoId(String overlayVpnBaseInfoId) throws ServiceException {
        final List<VpnPo> pos = selectByCondition("overlayVpnBaseInfoId", overlayVpnBaseInfoId, true);
        if(pos == null) {
            return null;
        }

        final List<String> ids = new ArrayList<String>(pos.size());
        for(final VpnPo po : pos) {
            ids.add(po.getId());
        }
        return ids;
    }

    /**
     * Get VPN by terminate point ids.
     * 
     * @since SDNO 0.5
     */
    public List<Vpn> getVpnsByTpIds(List<String> tpUuidList) throws ServiceException {
        final List<VpnTpRelationPo> vpnTpRelationPos = vpnTpRelationDao.selectByConditions("tpId", tpUuidList);
        if(CollectionUtils.isEmpty(vpnTpRelationPos)) {
            return null;
        }

        final Set<String> vpnIds = new HashSet<String>();
        for(final VpnTpRelationPo vpnTpRelationPo : vpnTpRelationPos) {
            vpnIds.add(vpnTpRelationPo.getVpnBasicInfoId());
        }

        return getMoByIds(new ArrayList<String>(vpnIds));
    }

    /**
     * Update VPN.
     * 
     * @since SDNO 0.5
     */
    public void updateVpn(Vpn oldVpn) throws ServiceException {
        updateMos(Collections.singletonList(oldVpn));
    }

    @Override
    protected Class<VpnPo> getPoClass() {
        return VpnPo.class;
    }

    @Override
    public void addMos(List<Vpn> mos) throws ServiceException {
        if(CollectionUtils.isEmpty(mos)) {
            return;
        }
        final List<VpnPo> vpnPos = new ArrayList<VpnPo>();
        final List<VpnBasicInfo> vpnBasicInfos = new ArrayList<VpnBasicInfo>();
        DaoUtil.setUuidIfEmpty(mos);
        for(final Vpn vpn : mos) {
            final VpnPo vpnPo = new VpnPo();
            vpnPo.fromSvcModel(vpn);
            vpnPos.add(vpnPo);

            final VpnBasicInfo vpnBasicInfo = vpn.getVpnBasicInfo();
            if(vpnBasicInfo != null) {
                vpnBasicInfo.setUuid(vpn.getId());
                vpnBasicInfos.add(vpnBasicInfo);
                vpnPo.setVpnBasicInfoId(vpn.getId());
            }
        }

        insert(vpnPos);
        vpnBasicInfoDao.addMos(vpnBasicInfos);
    }

    @Override
    public boolean delMos(List<Vpn> mos) throws ServiceException {
        if(CollectionUtils.isEmpty(mos)) {
            return true;
        }
        if(!delete(DaoUtil.batchMoConvert(mos, VpnPo.class))) {
            return false;
        }

        final List<VpnBasicInfo> vpnBasicInfos = new ArrayList<VpnBasicInfo>();
        for(final Vpn vpn : mos) {
            if(vpn.getVpnBasicInfo() != null) {
                vpnBasicInfos.add(vpn.getVpnBasicInfo());
            }
        }
        return vpnBasicInfoDao.delMos(vpnBasicInfos);
    }

    @Override
    public List<Vpn> assembleMo(List<VpnPo> vpnPos) throws ServiceException {
        if(CollectionUtils.isEmpty(vpnPos)) {
            return null;
        }
        final List<String> vpnBasicInfoIds = new ArrayList<String>(vpnPos.size());
        for(final VpnPo vpnPo : vpnPos) {
            vpnBasicInfoIds.add(vpnPo.getVpnBasicInfoId());
        }
        final List<Vpn> vpnMos = DaoUtil.batchPoConvert(vpnPos, Vpn.class);

        final List<VpnBasicInfo> vpnBasicInfos = vpnBasicInfoDao.getMoByIds(vpnBasicInfoIds);
        final Map<String, VpnBasicInfo> vpnbiidMap = new HashMap<String, VpnBasicInfo>();
        if(vpnBasicInfos != null) {
            for(final VpnBasicInfo vpnBasicInfo : vpnBasicInfos) {
                vpnbiidMap.put(vpnBasicInfo.getUuid(), vpnBasicInfo);
            }
        }
        for(int i = 0; i < vpnMos.size(); i++) {
            vpnMos.get(i).setVpnBasicInfo(vpnbiidMap.get(vpnPos.get(i).getVpnBasicInfoId()));
        }
        return vpnMos;
    }

    @Override
    public boolean updateMos(List<Vpn> mos) throws ServiceException {
        if(!update(DaoUtil.batchMoConvert(mos, VpnPo.class))) {
            return false;
        }
        final List<VpnBasicInfo> vpnBasicInfos = new ArrayList<VpnBasicInfo>();
        for(final Vpn vpn : mos) {
            if(vpn.getVpnBasicInfo() == null) {
                continue;
            }
            vpnBasicInfos.add(vpn.getVpnBasicInfo());
        }
        return vpnBasicInfoDao.updateMos(vpnBasicInfos);
    }

    /**
     * Update VPN description.
     * 
     * @since SDNO 0.5
     */
    public boolean updateDescription(String uuid, String description) throws ServiceException {
        VpnPo po = selectById(uuid);
        if(po == null) {
            throw new ServiceException("vpn not exist");
        }
        po.setDescription(description);
        return update(Collections.singletonList(po));
    }

    /**
     * Get VPN by name.
     * 
     * @since SDNO 0.5
     */
    public List<Vpn> getVpnsByVpnName(String name) throws ServiceException {
        final List<VpnPo> vpnPos = selectByCondition("name", name, false);
        return assembleMo(vpnPos);
    }

    /**
     * Get VPN abstract informaton.
     * 
     * @since SDNO 0.5
     */
    public List<Vpn> getVpnAbstractInfo(final List<String> vpnIds) throws ServiceException {
        final List<VpnPo> vpnPos = selectByIds(vpnIds);

        final List<String> vpnBasicInfoIds = new ArrayList<>();

        for(final VpnPo vpnPo : vpnPos) {
            vpnBasicInfoIds.add(vpnPo.getVpnBasicInfoId());
        }

        final List<VpnBasicInfo> basicAbstractInfo = vpnBasicInfoDao.getBasicAbstractInfo(vpnBasicInfoIds);
        return assembleAbstractInfo(vpnPos, basicAbstractInfo);
    }

    private List<Vpn> assembleAbstractInfo(final List<VpnPo> vpnPos, final List<VpnBasicInfo> vpnBasicInfos) {
        final List<Vpn> vpnList = new ArrayList<>();

        for(final VpnPo vpnPo : vpnPos) {
            final Vpn vpn = vpnPo.toSvcModel();

            // assemble vpn basic info of single domain vpn
            for(final VpnBasicInfo vpnBasicInfo : vpnBasicInfos) {
                if(vpnBasicInfo.getUuid().equals(vpnPo.getVpnBasicInfoId())) {
                    vpn.setVpnBasicInfo(vpnBasicInfo);
                    break;
                }
            }
            vpnList.add(vpn);
        }
        return vpnList;
    }
}
