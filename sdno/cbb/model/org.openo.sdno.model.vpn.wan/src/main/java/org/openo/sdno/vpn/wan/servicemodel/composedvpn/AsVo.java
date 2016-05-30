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

import org.openo.sdno.inventory.sdk.model.ManagementDomainMO;

/**
 * Autonomous system value object.<br/>
 * 
 * @author
 * @version SDNO 0.5 2016-3-18
 */
public class AsVo {

    private String asName = null;

    private MultiAsVpnVo multiAsVpnBelong = null;

    private String serviceType = null;

    private ManagementDomainMO managementDomainMo = null;

    private List<VpnVo> vpnVoList = new ArrayList<VpnVo>();

    public AsVo() {
        super();
    }

    public AsVo(String asName, MultiAsVpnVo multiAsVpnBelong, String serviceType, ManagementDomainMO managementDomainMo) {
        this.asName = asName;
        this.multiAsVpnBelong = multiAsVpnBelong;
        this.serviceType = serviceType;
        this.managementDomainMo = managementDomainMo;
    }

    public final String getAsName() {
        return asName;
    }

    public final void setAsName(String asName) {
        this.asName = asName;
    }

    public final MultiAsVpnVo getMultiAsVpnBelong() {
        return multiAsVpnBelong;
    }

    public final void setMultiAsVpnBelong(MultiAsVpnVo multiAsVpnBelong) {
        this.multiAsVpnBelong = multiAsVpnBelong;
    }

    public final String getServiceType() {
        return serviceType;
    }

    public final void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public final List<VpnVo> getVpnVoList() {
        return vpnVoList;
    }

    public final void setVpnVoList(List<VpnVo> vpnVoList) {
        this.vpnVoList = vpnVoList;
    }

    public final ManagementDomainMO getManagementDomainMo() {
        return managementDomainMo;
    }

    public final int getVpnCount() {
        return this.vpnVoList.size();
    }

    public final void setManagementDomainMo(ManagementDomainMO managementDomainMo) {
        this.managementDomainMo = managementDomainMo;
    }

    public List<TpVo> findAllTpVoList() {
        List<TpVo> tpVoList = new ArrayList<TpVo>();
        for(VpnVo vpnVo : this.getVpnVoList()) {
            tpVoList.addAll(vpnVo.getTpVoList());
        }
        return tpVoList;
    }

    @Override
    public String toString() {
        return "AsVo [asName=" + asName + ", multiAsVpnBelong=" + multiAsVpnBelong + ", serviceType=" + serviceType
                + ", managementDomainMO=" + managementDomainMo + ", vpnVoList=" + vpnVoList + "]";
    }

}
