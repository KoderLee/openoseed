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
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import org.openo.sdno.remoteservice.exception.ServiceException;
import org.openo.sdno.inventory.sdk.model.QueryParams;
import org.openo.sdno.result.Result;
import org.openo.sdno.vpn.wan.db.vpn.MultiAsVpnPo;
import org.openo.sdno.vpn.wan.db.vpn.VpnBasicInfoPo;
import org.openo.sdno.vpn.wan.servicemodel.vpn.MultiAsVpn;
import org.openo.sdno.vpn.wan.servicemodel.vpn.SegmentVpn;
import org.openo.sdno.vpn.wan.servicemodel.vpn.VpnBasicInfo;
import com.puer.framework.base.util.JsonUtil;

@Repository("multiAsVpnDao")
public class MultiAsVpnDao extends DafaultDao<MultiAsVpnPo, MultiAsVpn> {

    @Autowired
    private VpnBasicInfoDao vpnBasicInfoDao;

    @Autowired
    private SegmentVpnDao segmentVpnDao;

    @Autowired
    private VpnDao vpnDao;

    @Override
    public List<MultiAsVpn> assembleMo(List<MultiAsVpnPo> multiAsVpnPos) throws ServiceException {
        List<MultiAsVpn> multiAsVpns = new ArrayList<>();
        for(MultiAsVpnPo multiAsVpnPo : multiAsVpnPos) {
            final MultiAsVpn multiAsVpn = multiAsVpnPo.toSvcModel();

            final VpnBasicInfo vpnBasicInfo = vpnBasicInfoDao.getMoById(multiAsVpnPo.getVpnBasicInfoId());
            multiAsVpn.setVpnBasicInfo(vpnBasicInfo);

            final List<SegmentVpn> segmentVpns =
                    segmentVpnDao.getMoByConditions("multiAsVpnId", Collections.singletonList(multiAsVpnPo.getUuid()));
            multiAsVpn.setSegVpnList(segmentVpns);
            multiAsVpns.add(multiAsVpn);
        }
        return multiAsVpns;
    }

    @Override
    public void addMos(List<MultiAsVpn> multiAsVpns) throws ServiceException {
        for(MultiAsVpn multiAsVpn : multiAsVpns) {
            DaoUtil.setUuidIfEmpty(Collections.singletonList(multiAsVpn.getVpnBasicInfo()));
            vpnBasicInfoDao.addMos(Collections.singletonList(multiAsVpn.getVpnBasicInfo()));

            DaoUtil.setUuidIfEmpty(Collections.singletonList(multiAsVpn));
            multiAsVpn.setValue4Po("vpnBasicInfoId", multiAsVpn.getVpnBasicInfo().getUuid());
            insert(DaoUtil.batchMoConvert(Collections.singletonList(multiAsVpn), MultiAsVpnPo.class));

            for(SegmentVpn segmentVpn : multiAsVpn.getSegVpnList()) {
                segmentVpn.setValue4Po("multiAsVpnId", multiAsVpn.getUuid());
            }
            segmentVpnDao.addMos(multiAsVpn.getSegVpnList());
        }
    }

    @Override
    public boolean delMos(List<MultiAsVpn> multiAsVpns) throws ServiceException {
        List<MultiAsVpnPo> pos = new ArrayList<>();
        for(MultiAsVpn multiAsVpn : multiAsVpns) {
            vpnBasicInfoDao.delMos(Collections.singletonList(multiAsVpn.getVpnBasicInfo()));
            segmentVpnDao.delMos(multiAsVpn.getSegVpnList());

            MultiAsVpnPo po = new MultiAsVpnPo();
            po.fromSvcModel(multiAsVpn);
            pos.add(po);
        }
        delete(pos);
        return true;
    }

    @Override
    public boolean updateMos(List<MultiAsVpn> multiAsVpns) throws ServiceException {
        List<MultiAsVpnPo> pos = new ArrayList<>();
        for(MultiAsVpn multiAsVpn : multiAsVpns) {
            segmentVpnDao.updateMos(multiAsVpn.getSegVpnList());
            vpnBasicInfoDao.updateMos(Collections.singletonList(multiAsVpn.getVpnBasicInfo()));
            MultiAsVpnPo po = new MultiAsVpnPo();
            po.fromSvcModel(multiAsVpn);
            pos.add(po);
        }
        update(pos);
        return true;
    }

    @Override
    protected Class<MultiAsVpnPo> getPoClass() {
        return MultiAsVpnPo.class;
    }
}
