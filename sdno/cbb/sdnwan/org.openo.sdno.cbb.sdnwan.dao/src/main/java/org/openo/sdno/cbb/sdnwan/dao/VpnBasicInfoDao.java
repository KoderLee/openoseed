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
import org.springframework.util.StringUtils;

import org.openo.sdno.remoteservice.exception.ServiceException;
import org.openo.sdno.util.NewUtil;
import org.openo.sdno.inventory.sdk.model.NetworkElementMO;
import org.openo.sdno.inventory.sdk.model.RelationMO;
import org.openo.sdno.result.Result;
import org.openo.sdno.vpn.wan.db.relation.VpnTpRelationPo;
import org.openo.sdno.vpn.wan.db.tp.TpPo;
import org.openo.sdno.vpn.wan.db.vpn.VpnBasicInfoPo;
import org.openo.sdno.vpn.wan.servicemodel.relation.VpnTpRelation;
import org.openo.sdno.vpn.wan.servicemodel.topology.Topology;
import org.openo.sdno.vpn.wan.servicemodel.tp.Tp;
import org.openo.sdno.vpn.wan.servicemodel.vpn.PathInfo;
import org.openo.sdno.vpn.wan.servicemodel.vpn.VpnBasicInfo;

@Repository("vpnBasicInfoDao")
public class VpnBasicInfoDao extends DafaultDao<VpnBasicInfoPo, VpnBasicInfo> {

    @Autowired
    private TopologyDao topologyDao;

    @Autowired
    private VpnTpRelationDao vpnTpRelationDao;

    @Autowired
    private TpDao tpDao;

    @Autowired
    private VpnDao vpnDao;

    @Autowired
    private PathInfoDao pathInfoDao;

    @Autowired
    private InvNetworkElementMoDao invNetworkElementMoDao;

    @Override
    protected Class<VpnBasicInfoPo> getPoClass() {
        return VpnBasicInfoPo.class;
    }

    @Override
    public void addMos(List<VpnBasicInfo> mos) throws ServiceException {
        if(CollectionUtils.isEmpty(mos)) {
            return;
        }
        DaoUtil.setUuidIfEmpty(mos);
        insert(DaoUtil.batchMoConvert(mos, VpnBasicInfoPo.class));
        final List<Topology> topologys = new ArrayList<>();
        final List<PathInfo> pathInfos = new ArrayList<>();
        for(final VpnBasicInfo vpnBasicInfo : mos) {
            // Topology
            VpnBasicInfoDaoHelper.prepareTopology4Add(vpnBasicInfo, topologys);
            // PathInfo
            VpnBasicInfoDaoHelper.preparePathInfo4Add(vpnBasicInfo, pathInfos);

            // nes
            addNes(vpnBasicInfo);
        }
        DaoUtil.resetUuids(topologys);
        DaoUtil.resetUuids(pathInfos);
        topologyDao.addMos(topologys);

        pathInfoDao.addMos(pathInfos);
        handelTp4VpnChange(mos, false);
    }

    private void addNes(final VpnBasicInfo vpnBasicInfo) throws ServiceException {
        if(!CollectionUtils.isEmpty(vpnBasicInfo.getNes())) {
            for(final String neUuid : vpnBasicInfo.getNes()) {
                final RelationMO relationMo =
                        new RelationMO(VpnBasicInfoPo.class, NetworkElementMO.class, vpnBasicInfo.getUuid(), neUuid,
                                RelationMO.ASSOCIATION_TYPE, "");
                invDao.addRelation(relationMo);
            }
        }
    }

    @Override
    public boolean delMos(List<VpnBasicInfo> mos) throws ServiceException {
        if(CollectionUtils.isEmpty(mos)) {
            return true;
        }

        if(!delete(DaoUtil.batchMoConvert(mos, VpnBasicInfoPo.class))) {
            return false;
        }
        boolean rst = true;
        List<String> uuids = new ArrayList<>();
        final List<Tp> tps = new ArrayList<>();
        final List<Topology> topologys = new ArrayList<>();
        final List<PathInfo> pathInfos = new ArrayList<>();
        for(final VpnBasicInfo vpnBasicInfo : mos) {
            // VpnTpRelation
            uuids.add(vpnBasicInfo.getUuid());
            // Topology
            DaoUtil.addCollections(vpnBasicInfo.getToplogyspec(), topologys);
            // PathInfo
            DaoUtil.addCollections(vpnBasicInfo.getPathList(), pathInfos);
            // Tp
            DaoUtil.addCollections(vpnBasicInfo.getAccessPointList(), tps);
        }
        rst &= vpnTpRelationDao.deleteByConditions("vpnBasicInfoId", uuids);
        rst &= topologyDao.delMos(topologys);
        rst &= pathInfoDao.delMos(pathInfos);
        rst &= delTpRelated(tps);
        return rst;
    }

    /**
     * Update admin status.
     * 
     * @since SDNO 0.5
     */
    public boolean updateAdminStatus(String uuid, String adminStatus) throws ServiceException {
        VpnBasicInfoPo po = selectById(uuid);
        if(po == null) {
            throw new ServiceException("vpnBasicInfo not exist");
        }
        po.setAdminStatus(adminStatus);
        return update(Collections.singletonList(po));
    }

    private boolean delTpRelated(List<Tp> tps) throws ServiceException {
        if(tps == null) {
            return true;
        }
        final Map<String, Tp> tpMap = new HashMap<String, Tp>();
        for(final Tp tp : tps) {
            tpMap.put(tp.getId(), tp);
        }
        final List<VpnTpRelation> vpnTpRelations =
                vpnTpRelationDao.selectByTpIds(new ArrayList<String>(tpMap.keySet()));
        if(vpnTpRelations != null) {
            for(final VpnTpRelation vpnTpRelation : vpnTpRelations) {
                tpMap.remove(vpnTpRelation.getTpId());
            }
        }

        return tpDao.delMos(new ArrayList<Tp>(tpMap.values()));
    }

    @Override
    public List<VpnBasicInfo> assembleMo(List<VpnBasicInfoPo> pos) throws ServiceException {
        final List<VpnBasicInfo> mos = DaoUtil.batchPoConvert(pos, VpnBasicInfo.class);
        for(final VpnBasicInfo mo : mos) {
            final List<Topology> topologyLst = topologyDao.getMoByCondition("vpnBasicInfoId", mo.getUuid(), true);
            mo.setToplogyspec(topologyLst);

            final List<VpnTpRelation> vpnTpRelationLst = vpnTpRelationDao.selectByVpnBasicInfoId(mo.getUuid());
            final List<String> tpIds = new ArrayList<String>(vpnTpRelationLst.size());
            for(final VpnTpRelation vpnTpRelation : vpnTpRelationLst) {
                tpIds.add(vpnTpRelation.getTpId());
            }
            final List<Tp> tpLst = tpDao.getMoByIds(tpIds);
            mo.setAccessPointList(tpLst);

            final List<String> serverConnectionList = vpnDao.queryIdsByOverlayVpnBaseInfoId(mo.getUuid());
            mo.setServerConnectionList(serverConnectionList);

            final List<PathInfo> pathList = pathInfoDao.getMoByCondition("vpnBasicInfoId", mo.getUuid(), true);
            mo.setPathList(pathList);

            assembleNes(mo);
        }
        return mos;
    }

    private void assembleNes(final VpnBasicInfo vpnBasicInfo) throws ServiceException {
        final RelationMO relation =
                new RelationMO(VpnBasicInfoPo.class, NetworkElementMO.class, vpnBasicInfo.getUuid(), null,
                        RelationMO.ASSOCIATION_TYPE, "");
        final Result<List<NetworkElementMO>> result = invNetworkElementMoDao.queryByRelation(relation);
        if(result.isSucess() && !CollectionUtils.isEmpty(result.getResultObj())) {
            final List<String> nes = NewUtil.newArrayList();
            for(final NetworkElementMO networkElementMo : result.getResultObj()) {
                nes.add(networkElementMo.getUuid());
            }

            vpnBasicInfo.setNes(nes);
        }
    }

    @Override
    public boolean updateMos(List<VpnBasicInfo> mos) throws ServiceException {
        if(CollectionUtils.isEmpty(mos)) {
            return true;
        }
        boolean rst = update(DaoUtil.batchMoConvert(mos, VpnBasicInfoPo.class));
        if(!rst) {
            return false;
        }

        for(final VpnBasicInfo vpnBasicInfo : mos) {
            // Topology
            rst &=
                    topologyDao.handleModifyMosWithFkey(vpnBasicInfo.getToplogyspec(), "vpnBasicInfoId",
                            vpnBasicInfo.getUuid());
            // PathInfo
            rst &=
                    pathInfoDao.handleModifyMosWithFkey(vpnBasicInfo.getPathList(), "vpnBasicInfoId",
                            vpnBasicInfo.getUuid());
        }
        // TP
        handelTp4VpnChange(mos, true);
        return rst;
    }

    /**
     * Update terminate point.
     * 
     * @since SDNO 0.5
     */
    public void updateTps(VpnBasicInfo mo) throws ServiceException {
        handelTp4VpnChange(Collections.singletonList(mo), true);
    }

    private void handelTp4VpnChange(List<VpnBasicInfo> mos, boolean modify) throws ServiceException {
        List<VpnTpRelation> olRealtions = null;
        if(modify) {
            olRealtions = vpnTpRelationDao.selectByVpnBasicInfoIds(DaoUtil.getUuids(mos));
            vpnTpRelationDao.delMos(olRealtions);
        }
        final List<Tp> toAddTps = new ArrayList<Tp>();
        final List<Tp> existTps = new ArrayList<Tp>();
        prepareTps(mos, toAddTps, existTps);

        tpDao.addMos(toAddTps);
        addVpnTpRelations(mos);

        if(modify) {
            tpDao.updateMos(existTps);

            olRealtions = DaoUtil.safeList(olRealtions);
            final Set<String> tpIds = new HashSet<String>();
            for(final VpnTpRelation olRealtion : olRealtions) {
                tpIds.add(olRealtion.getTpId());
            }
            for(final Tp tp : existTps) {
                tpIds.remove(tp.getId());
            }

            delTpRelated(tpDao.getMoByIds(new ArrayList<String>(tpIds)));
        }
    }

    private void prepareTps(List<VpnBasicInfo> mos, List<Tp> toAddTps, List<Tp> existTps) throws ServiceException {
        final Map<String, Tp> tpidMap = new HashMap<String, Tp>();
        for(final VpnBasicInfo mo : mos) {
            List<Tp> newTps = DaoUtil.safeList(mo.getAccessPointList());
            for(final Tp tp : newTps) {
                if(StringUtils.hasLength(tp.getId())) {
                    tpidMap.put(tp.getId(), tp);
                } else {
                    DaoUtil.resetUuid(tp);
                    toAddTps.add(tp);
                }
            }
        }
        final List<String> ids = new ArrayList<String>(tpidMap.keySet());
        List<TpPo> olTppos = DaoUtil.safeList(tpDao.selectByIds(ids));
        for(final TpPo tppo : olTppos) {
            final Tp tp = tpidMap.remove(tppo.getId());
            existTps.add(tp);
        }
        for(final Tp tp : tpidMap.values()) {
            toAddTps.add(tp);
        }
    }

    private void addVpnTpRelations(List<VpnBasicInfo> mos) throws ServiceException {
        final List<VpnTpRelation> relations = new ArrayList<VpnTpRelation>();
        for(final VpnBasicInfo mo : mos) {
            List<Tp> tps = DaoUtil.safeList(mo.getAccessPointList());
            for(final Tp tp : tps) {
                final VpnTpRelation rl = new VpnTpRelation();
                rl.setVpnBasicInfoId(mo.getUuid());
                rl.setTpId(tp.getId());
                DaoUtil.resetUuid(rl);
                relations.add(rl);
            }
        }
        vpnTpRelationDao.addMos(relations);
    }

    /**
     * Get vpn basic information by terminate point uuid.
     * 
     * @since SDNO 0.5
     */
    public VpnBasicInfo getByTpId(String tpId, boolean complete) throws ServiceException {
        final List<VpnTpRelationPo> vpnTpRelationPos =
                vpnTpRelationDao.selectByConditions("tpId", Collections.singletonList(tpId));
        if(CollectionUtils.isEmpty(vpnTpRelationPos)) {
            return null;
        }
        final VpnBasicInfoPo po = selectById(vpnTpRelationPos.iterator().next().getVpnBasicInfoId());
        return po == null ? null : po.toSvcModel();
    }

    /**
     * Get basic abstract information by basic information ids.
     * 
     * @since SDNO 0.5
     */
    public List<VpnBasicInfo> getBasicAbstractInfo(final List<String> basicInfoIds) throws ServiceException {
        final List<VpnBasicInfoPo> vpnBasicInfoPos = selectByIds(basicInfoIds);

        final List<VpnTpRelation> vpnTpRelations = vpnTpRelationDao.selectByVpnBasicInfoIds(basicInfoIds);
        final List<String> tpIds = new ArrayList<>();
        for(final VpnTpRelation vpnTpRelation : vpnTpRelations) {
            tpIds.add(vpnTpRelation.getTpId());
        }
        final List<TpPo> tpPos = tpDao.selectByIds(tpIds);
        return assembleAbstractInfo(vpnBasicInfoPos, vpnTpRelations, tpPos);
    }

    private List<VpnBasicInfo> assembleAbstractInfo(final List<VpnBasicInfoPo> vpnBasicInfoPos,
            final List<VpnTpRelation> vpnTpRelations, final List<TpPo> tpPos) {
        final Map<String, List<Tp>> vpnTpMap = VpnBasicInfoDaoHelper.getVpnTpMap(vpnTpRelations, tpPos);

        final List<VpnBasicInfo> vpnBasicInfos = new ArrayList<>();
        // assemble vpn basic info of single domain vpn
        for(final VpnBasicInfoPo vpnBasicInfoPo : vpnBasicInfoPos) {
            final VpnBasicInfo vpnBasicInfo = vpnBasicInfoPo.toSvcModel();

            final List<Tp> tps = vpnTpMap.get(vpnBasicInfo.getUuid());
            vpnBasicInfo.setAccessPointList(tps);

            vpnBasicInfos.add(vpnBasicInfo);
        }

        return vpnBasicInfos;
    }

    /**
     * Update status.
     * 
     * @since SDNO 0.5
     */
    public void updateStatus(final List<VpnBasicInfo> vpnBasicInfos) throws ServiceException {
        final List<VpnBasicInfoPo> vpnBasicInfoPos = VpnBasicInfoDaoHelper.getVpnBasicInfoPo4Status(vpnBasicInfos);

        update(vpnBasicInfoPos);
        tpDao.updateStatus(VpnBasicInfoDaoHelper.getTps(vpnBasicInfos));
    }

}
