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

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

import org.openo.sdno.vpn.wan.servicemodel.tp.Tp;
import org.openo.sdno.vpn.wan.servicemodel.tp.TpTypeSpec;

public class TpVo {

    private VpnVo vpnBelong;

    private NeVo neBelong;

    private Tp tp;

    private String tpName;

    private String neId;

    private String vlanId;

    public TpVo() {
        // Constructed function.
    }

    public TpVo(String tpName, String neId, String vlanId) {
        this.tpName = tpName;
        this.neId = neId;
        this.vlanId = vlanId;
    }

    public final VpnVo getVpnBelong() {
        return vpnBelong;
    }

    public final void setVpnBelong(VpnVo vpnBelong) {
        this.vpnBelong = vpnBelong;
    }

    public final NeVo getNeBelong() {
        return neBelong;
    }

    public final void setNeBelong(NeVo neBelong) {
        this.neBelong = neBelong;
    }

    public final Tp getTp() {
        return tp;
    }

    public final void setTp(Tp tp) {
        this.tp = tp;
    }

    public final String getTpName() {
        return tpName;
    }

    public final void setTpName(String tpName) {
        this.tpName = tpName;
    }

    public final String getNeId() {
        return neId;
    }

    public final void setNeId(String neId) {
        this.neId = neId;
    }

    public final String getVlanId() {
        return vlanId;
    }

    public final void setVlanId(String vlanId) {
        this.vlanId = vlanId;
    }

    public final String getPeerCeName() {
        if(this.tp.getPeerCeTp() != null) {
            return this.tp.getPeerCeTp().getCeName();
        }
        return null;
    }

    public final String getPeerSiteName() {
        if(this.tp.getPeerCeTp() != null) {
            return this.tp.getPeerCeTp().getSiteName();
        }
        return null;
    }

    public boolean isMach(String tpId, String neId, String vlanId) {
        if(this.getTp().getId().equals(tpId) && this.getTp().getNeId().equals(neId)) {
            List<TpTypeSpec> typeSpecList = this.getTp().getTypeSpecList();

            for(TpTypeSpec tpSpec : typeSpecList) {
                if((vlanId == null || vlanId.isEmpty()) && isSvlanListNull(tpSpec)) {
                    return true;
                }
                if(!isSvlanListNull(tpSpec) && tpSpec.getEthernetTpSpec().getSvlanList().equals(vlanId)) {
                    return true;
                }
            }
        }
        return false;
    }

    @JsonIgnore
    private boolean isSvlanListNull(TpTypeSpec tpSpec) {
        return !(null != tpSpec && null != tpSpec.getEthernetTpSpec() && null != tpSpec.getEthernetTpSpec()
                .getSvlanList());
    }

    public void fillTpInfo(final VpnVo vpnVo, final Tp tp, final NeVo neVo) {
        this.setNeBelong(neVo);
        this.setVpnBelong(vpnVo);
        this.setTpName(tp.getName());
        this.setTp(tp);
        this.setNeId(tp.getNeId());
    }

    @Override
    public String toString() {
        return "TpVo [vpnBelong=" + vpnBelong + ", neBelong=" + neBelong + ", tp=" + tp + ", tpName=" + tpName
                + ", neId=" + neId + ", vlanId=" + vlanId + "]";
    }
}
