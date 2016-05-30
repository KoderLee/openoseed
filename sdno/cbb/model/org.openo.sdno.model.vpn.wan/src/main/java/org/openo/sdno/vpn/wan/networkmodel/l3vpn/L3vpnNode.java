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
package org.openo.sdno.vpn.wan.networkmodel.l3vpn;

import org.openo.sdno.vpn.wan.networkmodel.NetModel;
import org.openo.sdno.vpn.wan.networkmodel.l3vpn.enums.NetL3vpnNodeRole;

public class L3vpnNode implements NetModel {

    private String nodeId;

    private String nodeRole = NetL3vpnNodeRole.DEFAULT.getName();

    private String rd;

    private VpnTargets vpnTargets;

    private AcIfs acIfs;

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getNodeRole() {
        return nodeRole;
    }

    public void setNodeRole(String nodeRole) {
        this.nodeRole = nodeRole;
    }

    public String getRd() {
        return rd;
    }

    public void setRd(String rd) {
        this.rd = rd;
    }

    public VpnTargets getVpnTargets() {
        return vpnTargets;
    }

    public void setVpnTargets(VpnTargets vpnTargets) {
        this.vpnTargets = vpnTargets;
    }

    public AcIfs getAcIfs() {
        return acIfs;
    }

    public void setAcIfs(AcIfs acIfs) {
        this.acIfs = acIfs;
    }
}
