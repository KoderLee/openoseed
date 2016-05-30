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
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import org.openo.sdno.remoteservice.exception.ServiceException;
import org.openo.sdno.vpn.wan.db.tp.EthernetTpSpecPo;
import org.openo.sdno.vpn.wan.db.tp.TpTypeSpecPo;
import org.openo.sdno.vpn.wan.servicemodel.tp.EthernetTpSpec;
import org.openo.sdno.vpn.wan.servicemodel.tp.IpTpSpec;
import org.openo.sdno.vpn.wan.servicemodel.tp.TpTypeSpec;

@Repository("tpTypeSpecDao")
public class TpTypeSpecDao extends DafaultDao<TpTypeSpecPo, TpTypeSpec> {

    @Autowired
    private EthernetTpSpecDao ethernetTpSpecDao;

    @Autowired
    private IpTpSpecDao ipTpSpecDao;

    @Override
    protected Class<TpTypeSpecPo> getPoClass() {
        return TpTypeSpecPo.class;
    }

    @Override
    public List<TpTypeSpec> assembleMo(List<TpTypeSpecPo> tpTypeSpecPos) throws ServiceException {
        if(CollectionUtils.isEmpty(tpTypeSpecPos)) {
            return null;
        }

        final List<TpTypeSpec> tpTypeSpecs = DaoUtil.batchPoConvert(tpTypeSpecPos, TpTypeSpec.class);

        final List<String> ethernetTpSpecIds = new ArrayList<String>();
        final List<String> ipTpSpecIds = new ArrayList<String>();
        final List<String> vxlanTpSpecIds = new ArrayList<String>();

        final Map<String, TpTypeSpec> mapTpTypSpcEthntTpSpcs = new HashMap<String, TpTypeSpec>();
        final Map<String, TpTypeSpec> mapTpTypSpcIpTpSpcs = new HashMap<String, TpTypeSpec>();
        final Map<String, TpTypeSpec> mapTpTypSpcVxlanTpSpcIds = new HashMap<String, TpTypeSpec>();

        for(int i = 0; i < tpTypeSpecPos.size(); i++) {
            if(tpTypeSpecPos.get(i).getEthernetTpSpecId() != null) {
                ethernetTpSpecIds.add(tpTypeSpecPos.get(i).getEthernetTpSpecId());
                mapTpTypSpcEthntTpSpcs.put(tpTypeSpecPos.get(i).getEthernetTpSpecId(), tpTypeSpecs.get(i));
            }
            if(tpTypeSpecPos.get(i).getIpTpSpecId() != null) {
                ipTpSpecIds.add(tpTypeSpecPos.get(i).getIpTpSpecId());
                mapTpTypSpcIpTpSpcs.put(tpTypeSpecPos.get(i).getIpTpSpecId(), tpTypeSpecs.get(i));
            }
            if(tpTypeSpecPos.get(i).getVxlanTpSpecId() != null) {
                vxlanTpSpecIds.add(tpTypeSpecPos.get(i).getVxlanTpSpecId());
                mapTpTypSpcVxlanTpSpcIds.put(tpTypeSpecPos.get(i).getVxlanTpSpecId(), tpTypeSpecs.get(i));
            }
        }

        final List<EthernetTpSpec> ethernetTpSpecs = ethernetTpSpecDao.getMoByIds(ethernetTpSpecIds);
        assembleEthernetTpSpecs(ethernetTpSpecs, mapTpTypSpcEthntTpSpcs);
        final List<IpTpSpec> ipTpSpecs = ipTpSpecDao.getMoByIds(ipTpSpecIds);
        assembleIpTpSpecs(ipTpSpecs, mapTpTypSpcIpTpSpcs);
        return tpTypeSpecs;
    }

    private void assembleEthernetTpSpecs(List<EthernetTpSpec> ethernetTpSpecs,
            final Map<String, TpTypeSpec> mapTpTypSpcEthntTpSpcs) {
        if(ethernetTpSpecs != null) {
            for(final EthernetTpSpec ethernetTpSpec : ethernetTpSpecs) {
                if(mapTpTypSpcEthntTpSpcs.containsKey(ethernetTpSpec.getUuid())) {
                    mapTpTypSpcEthntTpSpcs.get(ethernetTpSpec.getUuid()).setEthernetTpSpec(ethernetTpSpec);
                }
            }
        }
    }

    private void assembleIpTpSpecs(List<IpTpSpec> ipTpSpecs, Map<String, TpTypeSpec> mapTpTypSpcIpTpSpcs) {
        if(ipTpSpecs != null) {
            for(final IpTpSpec ipTpSpec : ipTpSpecs) {
                if(mapTpTypSpcIpTpSpcs.containsKey(ipTpSpec.getUuid())) {
                    mapTpTypSpcIpTpSpcs.get(ipTpSpec.getUuid()).setIpTpSpec(ipTpSpec);
                }
            }
        }
    }

    @Override
    public void addMos(List<TpTypeSpec> mos) throws ServiceException {
        if(CollectionUtils.isEmpty(mos)) {
            return;
        }
        final List<TpTypeSpecPo> pos = DaoUtil.batchMoConvert(mos, TpTypeSpecPo.class);

        final List<EthernetTpSpec> ethernetTpSpecs = new ArrayList<EthernetTpSpec>();
        final List<IpTpSpec> ipTpSpecs = new ArrayList<IpTpSpec>();

        for(int i = 0; i < pos.size(); i++) {
            final TpTypeSpecPo po = pos.get(i);
            final TpTypeSpec mo = mos.get(i);
            if(mo.getEthernetTpSpec() != null) {
                DaoUtil.resetUuid(mo.getEthernetTpSpec());
                ethernetTpSpecs.add(mo.getEthernetTpSpec());
                po.setEthernetTpSpecId(mo.getEthernetTpSpec().getUuid());
            }
            if(mo.getIpTpSpec() != null) {
                DaoUtil.resetUuid(mo.getIpTpSpec());
                ipTpSpecs.add(mo.getIpTpSpec());
                po.setIpTpSpecId(mo.getIpTpSpec().getUuid());
            }

        }
        final List<String> ids = insert(pos);
        if(CollectionUtils.isEmpty(ids)) {
            return;
        }
        ethernetTpSpecDao.addMos(ethernetTpSpecs);
        ipTpSpecDao.addMos(ipTpSpecs);
    }

    @Override
    public boolean delMos(List<TpTypeSpec> mos) throws ServiceException {
        final List<EthernetTpSpec> ethernetTpSpecs = new ArrayList<EthernetTpSpec>();
        final List<IpTpSpec> ipTpSpecs = new ArrayList<IpTpSpec>();

        for(final TpTypeSpec tpTypeSpec : mos) {
            if(tpTypeSpec.getEthernetTpSpec() != null) {
                ethernetTpSpecs.add(tpTypeSpec.getEthernetTpSpec());
            }

            if(tpTypeSpec.getIpTpSpec() != null) {
                ipTpSpecs.add(tpTypeSpec.getIpTpSpec());
            }

        }

        if(ethernetTpSpecDao.delMos(ethernetTpSpecs) && ipTpSpecDao.delMos(ipTpSpecs)
                && delete(DaoUtil.batchMoConvert(mos, TpTypeSpecPo.class))) {
            return true;
        }

        return false;
    }

    @Override
    public boolean updateMos(List<TpTypeSpec> mos) throws ServiceException {
        boolean result = false;
        for(final TpTypeSpec tpTypeSpec : mos) {
            DaoUtil.updateSlaveMo(tpTypeSpec, tpTypeSpec.getEthernetTpSpec(), this, ethernetTpSpecDao,
                    "ethernetTpSpecId");

            DaoUtil.updateSlaveMo(tpTypeSpec, tpTypeSpec.getIpTpSpec(), this, ipTpSpecDao, "ipTpSpecId");

            final TpTypeSpecPo newTpTypeSpecPo = new TpTypeSpecPo();
            newTpTypeSpecPo.fromSvcModel(tpTypeSpec);

            if(tpTypeSpec.getEthernetTpSpec() != null) {
                newTpTypeSpecPo.setEthernetTpSpecId(tpTypeSpec.getEthernetTpSpec().getUuid());
            }

            if(tpTypeSpec.getIpTpSpec() != null) {
                newTpTypeSpecPo.setIpTpSpecId(tpTypeSpec.getIpTpSpec().getUuid());
            }

            result = update(Collections.singletonList(newTpTypeSpecPo));
        }

        return result;
    }

    /**
     * Get terminate point specification.
     * 
     * @since SDNO 0.5
     */
    public Map<String, List<TpTypeSpec>> getEthInfo(final List<String> tpIds) throws ServiceException {
        final List<TpTypeSpecPo> tpTypeSpecPos = selectByConditions("tpId", tpIds);
        final List<String> ethSpecIdList = new ArrayList<>();
        for(final TpTypeSpecPo tpTypeSpecPo : tpTypeSpecPos) {
            ethSpecIdList.add(tpTypeSpecPo.getEthernetTpSpecId());
        }

        final List<EthernetTpSpecPo> ethernetTpSpecPos = ethernetTpSpecDao.selectByIds(ethSpecIdList);

        final Map<String, List<TpTypeSpec>> tpTypeSpecMap = new HashMap<>();
        for(final TpTypeSpecPo tpTypeSpecPo : tpTypeSpecPos) {
            final TpTypeSpec tpTypeSpec = tpTypeSpecPo.toSvcModel();
            List<TpTypeSpec> tpTypeSpecs = tpTypeSpecMap.get(tpTypeSpecPo.getTpId());
            if(null == tpTypeSpecs) {
                tpTypeSpecs = new ArrayList<>();
                tpTypeSpecMap.put(tpTypeSpecPo.getTpId(), tpTypeSpecs);
            }
            tpTypeSpecs.add(tpTypeSpec);

            for(final EthernetTpSpecPo ethernetTpSpecPo : ethernetTpSpecPos) {
                if(ethernetTpSpecPo.getUuid().equals(tpTypeSpecPo.getEthernetTpSpecId())) {
                    final EthernetTpSpec ethernetTpSpec = ethernetTpSpecPo.toSvcModel();
                    tpTypeSpec.setEthernetTpSpec(ethernetTpSpec);
                    break;
                }
            }
        }
        return tpTypeSpecMap;
    }
}
