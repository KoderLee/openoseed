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
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Repository;

import org.openo.sdno.remoteservice.exception.ServiceException;
import org.openo.sdno.vpn.wan.db.vpn.SegmentVpnPo;
import org.openo.sdno.vpn.wan.servicemodel.vpn.SegmentVpn;
import org.openo.sdno.vpn.wan.servicemodel.vpn.Vpn;

@Repository("segmentVpnDao")
public class SegmentVpnDao extends DafaultDao<SegmentVpnPo, SegmentVpn> {

    @Override
    public List<SegmentVpn> assembleMo(List<SegmentVpnPo> segmentVpnPos) throws ServiceException {
        final List<SegmentVpn> segmentVpns = new ArrayList<>();
        for(SegmentVpnPo segmentVpnPo : segmentVpnPos) {
            final SegmentVpn segmentVpn = segmentVpnPo.toSvcModel();
            Vpn vpn = new Vpn();
            vpn.setUuid(segmentVpnPo.getVpnInfoId());
            segmentVpn.setVpnInfo(vpn);
            segmentVpns.add(segmentVpn);
        }
        return segmentVpns;
    }

    @Override
    public void addMos(List<SegmentVpn> segmentVpns) throws ServiceException {
        final List<SegmentVpnPo> segmentVpnPos = new ArrayList<>();
        for(final SegmentVpn segmentVpn : segmentVpns) {
            final SegmentVpnPo segmentVpnPo = new SegmentVpnPo();
            segmentVpnPo.fromSvcModel(segmentVpn);
            segmentVpnPo.setVpnInfoId(segmentVpn.getVpnInfo().getId());
            segmentVpnPos.add(segmentVpnPo);
        }
        final List<String> uuids = insert(segmentVpnPos);
        final Iterator<SegmentVpn> segmentVpnIterator = segmentVpns.iterator();
        final Iterator<String> stringIterator = uuids.iterator();
        while(segmentVpnIterator.hasNext() && stringIterator.hasNext()) {
            segmentVpnIterator.next().setUuid(stringIterator.next());
        }
    }

    @Override
    public boolean delMos(List<SegmentVpn> segmentVpns) throws ServiceException {
        List<SegmentVpnPo> pos = new ArrayList<>();
        for(SegmentVpn segmentVpn : segmentVpns) {
            SegmentVpnPo po = new SegmentVpnPo();
            po.fromSvcModel(segmentVpn);
            pos.add(po);
        }
        delete(pos);
        return true;
    }

    @Override
    public boolean updateMos(List<SegmentVpn> segmentVpns) throws ServiceException {
        List<SegmentVpnPo> pos = new ArrayList<>();
        for(SegmentVpn segmentVpn : segmentVpns) {
            SegmentVpnPo po = new SegmentVpnPo();
            po.fromSvcModel(segmentVpn);
            pos.add(po);
        }
        update(pos);
        return true;
    }

    @Override
    protected Class<SegmentVpnPo> getPoClass() {
        return SegmentVpnPo.class;
    }
}
