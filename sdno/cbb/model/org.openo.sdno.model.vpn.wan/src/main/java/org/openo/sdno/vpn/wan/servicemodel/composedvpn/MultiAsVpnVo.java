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
package org.openo.sdno.vpn.wan.servicemodel.composedvpn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openo.sdno.vpn.wan.servicemodel.common.ServiceType;
import org.openo.sdno.vpn.wan.servicemodel.vpn.MultiAsVpn;
import org.openo.sdno.vpn.wan.servicemodel.vpn.SegmentVpn;
import org.openo.sdno.vpn.wan.servicemodel.vpn.Vpn;

public class MultiAsVpnVo {

    private MultiAsVpn multiAsVpn;

    private List<AsVo> asVoList = new ArrayList<AsVo>();

    public final MultiAsVpn getMultiAsVpn() {
        return multiAsVpn;
    }

    public final void setMultiAsVpn(MultiAsVpn multiAsVpn) {
        this.multiAsVpn = multiAsVpn;
    }

    public final List<AsVo> getAsVoList() {
        return asVoList;
    }

    public final void setAsVoList(List<AsVo> asVoList) {
        this.asVoList = asVoList;
    }

    public TpVo findTpVoByTpNameNeId(String tpId, String neId, String vlanId) {
        for(AsVo asVo : this.getAsVoList()) {
            for(VpnVo vpnVo : asVo.getVpnVoList()) {
                for(TpVo tpVo : vpnVo.getTpVoList()) {
                    if(tpVo.isMach(tpId, neId, vlanId)) {
                        return tpVo;
                    }
                }
            }
        }
        return null;
    }

    public AsVo findAsVoByName(String asName) {
        for(AsVo asVo : this.getAsVoList()) {
            if(asVo.getAsName().equals(asName)) {
                return asVo;
            }
        }
        return null;
    }

    public AsVo findAsVoById(String asUuid) {
        for(AsVo asVo : this.getAsVoList()) {
            if(asVo.getManagementDomainMo().getUuid().equals(asUuid)) {
                return asVo;
            }
        }
        return null;
    }

    public List<Vpn> findAllVpnList() {
        List<Vpn> vpnList = new ArrayList<Vpn>();
        for(SegmentVpn seg : this.getMultiAsVpn().getSegVpnList()) {
            vpnList.add(seg.getVpnInfo());
        }
        return vpnList;
    }

    public List<AsVo> findAsVoByServiceType(ServiceType serticeType) {
        List<AsVo> asVoListTemp = new ArrayList<AsVo>();
        for(AsVo asVo : this.getAsVoList()) {
            if(asVo.getServiceType().equalsIgnoreCase(serticeType.name())) {
                asVoListTemp.add(asVo);
            }
        }
        return asVoListTemp;
    }

    public Map<String, List<AsVo>> splitAsByDomain() {
        Map<String, List<AsVo>> domainMap = new HashMap<>();
        for(AsVo asVo : this.getAsVoList()) {
            String domainId = asVo.getManagementDomainMo().getUuid();
            if(domainMap.containsKey(domainId)) {
                domainMap.get(domainId).add(asVo);
            } else {
                ArrayList<AsVo> asVoLst = new ArrayList<AsVo>();
                asVoLst.add(asVo);
                domainMap.put(domainId, asVoLst);
            }
        }
        return domainMap;
    }

    public List<TpVo> findAllTpVo() {
        List<TpVo> result = new ArrayList<TpVo>();
        for(AsVo asVo : this.asVoList) {
            result.addAll(asVo.findAllTpVoList());
        }
        return result;
    }

    @Override
    public String toString() {
        return "MultiAsVpnVo [multiAsVpn=" + multiAsVpn + ", asVoList=" + asVoList + "]";
    }
}
