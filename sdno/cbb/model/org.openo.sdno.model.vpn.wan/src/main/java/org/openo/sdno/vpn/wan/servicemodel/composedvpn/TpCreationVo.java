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

import org.openo.sdno.inventory.sdk.model.TerminationPointMO;
import org.openo.sdno.vpn.wan.servicemodel.common.ServiceType;
import org.openo.sdno.vpn.wan.servicemodel.tp.Tp;

public class TpCreationVo {

    private String asName;

    private TerminationPointMO physicalTpMo = null;

    private ServiceType sericeType = null;

    private Tp tpSeed = null;

    private String ceAsbrName = null;

    private String vlanId = null;

    private String ip = null;

    public final String getAsName() {
        return asName;
    }

    public final void setAsName(String asName) {
        this.asName = asName;
    }

    public final TerminationPointMO getPhysicalTpMo() {
        return physicalTpMo;
    }

    public final void setPhysicalTpMo(TerminationPointMO physicalTpMo) {
        this.physicalTpMo = physicalTpMo;
    }

    public final ServiceType getSericeType() {
        return sericeType;
    }

    public final void setSericeType(ServiceType sericeType) {
        this.sericeType = sericeType;
    }

    public final Tp getTpSeed() {
        return tpSeed;
    }

    public final void setTpSeed(Tp tpSeed) {
        this.tpSeed = tpSeed;
    }

    public final String getCeAsbrName() {
        return ceAsbrName;
    }

    public final void setCeAsbrName(String ceAsbrName) {
        this.ceAsbrName = ceAsbrName;
    }

    public final String getVlanId() {
        return vlanId;
    }

    public final void setVlanId(String vlanId) {
        this.vlanId = vlanId;
    }

    public final String getIp() {
        return ip;
    }

    public final void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {
        return "TpCreationVo [asName=" + asName + ", physicalTpMo=" + physicalTpMo + ", sericeType=" + sericeType
                + ", tpSeed=" + tpSeed + ", ceAsbrName=" + ceAsbrName + ", vlanId=" + vlanId + ", ip=" + ip + "]";
    }
}
