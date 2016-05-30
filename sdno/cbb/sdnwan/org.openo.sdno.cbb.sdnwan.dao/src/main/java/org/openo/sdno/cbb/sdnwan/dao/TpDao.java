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
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import org.openo.sdno.remoteservice.exception.ServiceException;
import org.openo.sdno.vpn.wan.db.tp.TpPo;
import org.openo.sdno.vpn.wan.servicemodel.routeprotocol.RouteProtocolSpec;
import org.openo.sdno.vpn.wan.servicemodel.tp.CeTp;
import org.openo.sdno.vpn.wan.servicemodel.tp.Tp;
import org.openo.sdno.vpn.wan.servicemodel.tp.TpTypeSpec;

@Repository("tpDao")
public class TpDao extends DafaultDao<TpPo, Tp> {

    @Autowired
    private VpnTpRelationDao vpnTpRelationDao;

    @Autowired
    private TpTypeSpecDao tpTypeSpecDao;

    @Autowired
    private CeTpDao ceTpDao;

    @Override
    public void addMos(List<Tp> mos) throws ServiceException {
        if(CollectionUtils.isEmpty(mos)) {
            return;
        }
        DaoUtil.setUuidIfEmpty(mos);

        final List<CeTp> ceTps = new ArrayList<CeTp>();
        final List<TpTypeSpec> tpTypeSpecs = new ArrayList<TpTypeSpec>();
        final List<RouteProtocolSpec> routeProtocolSpecs = new ArrayList<RouteProtocolSpec>();
        for(final Tp tp : mos) {
            final CeTp ceTp = tp.getPeerCeTp();
            if(ceTp != null) {
                ceTps.add(ceTp);
                DaoUtil.resetUuid(ceTp);
                tp.setValue4Po("peerCeTpId", ceTp.getUuid());
            }
            if(!CollectionUtils.isEmpty(tp.getTypeSpecList())) {
                tpTypeSpecs.addAll(tp.getTypeSpecList());
                for(final TpTypeSpec tpTypeSpec : tp.getTypeSpecList()) {
                    tpTypeSpec.setValue4Po("tpId", tp.getId());
                }
            }
            if(!CollectionUtils.isEmpty(tp.getRouteProtocolSpecs())) {
                routeProtocolSpecs.addAll(tp.getRouteProtocolSpecs());
                for(final RouteProtocolSpec routeProtocolSpec : tp.getRouteProtocolSpecs()) {
                    routeProtocolSpec.setValue4Po("tpId", tp.getId());
                }

            }
        }

        insert(DaoUtil.batchMoConvert(mos, TpPo.class));
        ceTpDao.addMos(ceTps);
        DaoUtil.resetUuids(tpTypeSpecs);
        tpTypeSpecDao.addMos(tpTypeSpecs);
        DaoUtil.resetUuids(routeProtocolSpecs);
    }

    /**
     * Delete terminate point.
     * 
     * @since SDNO 0.5
     */
    public boolean deleteTp(String tpUuid) throws ServiceException {
        final Tp tp = getMoById(tpUuid);
        if(tp == null) {
            return true;
        }
        return delMos(Collections.singletonList(tp));
    }

    @Override
    public boolean updateMos(List<Tp> mos) throws ServiceException {
        boolean result = false;
        for(final Tp tp : mos) {
            result = DaoUtil.updateSlaveMo(tp, tp.getPeerCeTp(), this, ceTpDao, "peerCeTpId");

            final TpPo oldTpPo = selectById(tp.getUuid());
            if(null == oldTpPo) {
                return false;
            }
            final TpPo newTpPo = new TpPo();

            newTpPo.fromSvcModel(tp);
            newTpPo.setPathInfoId4Include(oldTpPo.getPathInfoId4Include());
            if(tp.getPeerCeTp() != null) {
                newTpPo.setPeerCeTpId(tp.getPeerCeTp().getUuid());
            }
            result &= update(Collections.singletonList(newTpPo));

            result &= tpTypeSpecDao.handleModifyMosWithFkey(tp.getTypeSpecList(), "tpId", tp.getUuid());

        }
        return result;
    }

    /**
     * Update admin status.
     * 
     * @since SDNO 0.5
     */
    public boolean updateAdminStatus(String uuid, String adminStatus) throws ServiceException {
        TpPo po = selectById(uuid);
        if(po == null) {
            throw new ServiceException("Tp not exist");
        }
        po.setAdminStatus(adminStatus);
        return update(Collections.singletonList(po));
    }

    @Override
    protected Class<TpPo> getPoClass() {
        return TpPo.class;
    }

    @Override
    public boolean delMos(List<Tp> mos) throws ServiceException {
        if(CollectionUtils.isEmpty(mos)) {
            return true;
        }

        final List<CeTp> ceTps = new ArrayList<CeTp>();
        final List<TpTypeSpec> tpTypeSpecs = new ArrayList<TpTypeSpec>();
        final List<String> tpIds = new ArrayList<String>();
        for(final Tp tp : mos) {
            tpIds.add(tp.getId());
            if(tp.getPeerCeTp() != null) {
                ceTps.add(tp.getPeerCeTp());
            }

            if(!CollectionUtils.isEmpty(tp.getTypeSpecList())) {
                tpTypeSpecs.addAll(tp.getTypeSpecList());
            }
        }

        boolean result = ceTpDao.delMos(ceTps);
        result &= tpTypeSpecDao.delMos(tpTypeSpecs);

        result &= delete(DaoUtil.batchMoConvert(mos, TpPo.class));
        result &= vpnTpRelationDao.delMos(vpnTpRelationDao.selectByTpIds(tpIds));

        return result;
    }

    @Override
    public List<Tp> assembleMo(List<TpPo> pos) throws ServiceException {
        final List<Tp> tps = DaoUtil.batchPoConvert(pos, Tp.class);
        for(int i = 0; i < pos.size(); i++) {
            final Tp tp = tps.get(i);
            final List<TpTypeSpec> tpTypeSpecs =
                    tpTypeSpecDao.getMoByConditions("tpId", Collections.singletonList(tp.getId()));
            tp.setTypeSpecList(tpTypeSpecs);
            final CeTp ceTp = ceTpDao.getMoById(pos.get(i).getPeerCeTpId());
            tp.setPeerCeTp(ceTp);

        }
        return tps;
    }

    /**
     * Get terminate point information.
     * 
     * @since SDNO 0.5
     */
    public List<Tp> getEthInfo(final List<String> parentTps) throws ServiceException {
        final List<TpPo> tpPos = selectByConditions("parentTp", parentTps);

        final List<String> tpIds = new ArrayList<>();
        for(final TpPo tpPo : tpPos) {
            tpIds.add(tpPo.getUuid());
        }

        final Map<String, List<TpTypeSpec>> ethInfos = tpTypeSpecDao.getEthInfo(tpIds);

        final List<Tp> tps = new ArrayList<>();
        for(final TpPo tpPo : tpPos) {
            final Tp tp = tpPo.toSvcModel();
            tp.setTypeSpecList(ethInfos.get(tp.getId()));
            tps.add(tp);
        }
        return tps;
    }

    /**
     * Update status.
     * 
     * @since SDNO 0.5
     */
    public void updateStatus(final List<Tp> tps) throws ServiceException {
        final List<TpPo> tpPos = new ArrayList<>();

        for(final Tp tp : tps) {
            final TpPo tpPo = new TpPo();
            tpPo.setAdminStatus(tp.getAdminStatus());
            tpPo.setOperStatus(tp.getOperStatus());
            tpPo.setUuid(tp.getUuid());

            tpPos.add(tpPo);
        }

        update(tpPos);
    }
}
