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
import java.util.List;

import org.openo.sdno.vpn.wan.servicemodel.vpn.Vpn;

public class VpnVo {

    private Vpn vpn;

    private AsVo asVoBelong;

    private String vpnUuid;

    private String vpnName;

    private List<TpVo> tpVoList = new ArrayList<TpVo>();

    public final Vpn getVpn() {
        return vpn;
    }

    public final void setVpn(Vpn vpn) {
        this.vpn = vpn;
    }

    public final AsVo getAsVoBelong() {
        return asVoBelong;
    }

    public final void setAsVoBelong(AsVo asVoBelong) {
        this.asVoBelong = asVoBelong;
    }

    public final List<TpVo> getTpVoList() {
        return tpVoList;
    }

    public final void setTpVoList(List<TpVo> tpVoList) {
        this.tpVoList = tpVoList;
    }

    public final String getVpnUuid() {
        return vpnUuid;
    }

    public final void setVpnUuid(String vpnUuid) {
        this.vpnUuid = vpnUuid;
    }

    public final String getVpnName() {
        return vpnName;
    }

    public final void setVpnName(String vpnName) {
        this.vpnName = vpnName;
    }

    public final int getTpCount() {
        return this.tpVoList.size();
    }

    @Override
    public String toString() {
        return "VpnVo [vpn=" + vpn + ", asVoBelong=" + asVoBelong + ", tpVoList=" + tpVoList + "]";
    }
}
