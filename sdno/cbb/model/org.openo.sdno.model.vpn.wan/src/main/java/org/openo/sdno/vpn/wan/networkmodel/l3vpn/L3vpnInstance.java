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
import org.openo.sdno.vpn.wan.networkmodel.l3vpn.enums.NetL3vpnAfType;
import org.openo.sdno.vpn.wan.networkmodel.l3vpn.enums.NetL3vpnServiceType;
import org.openo.sdno.vpn.wan.networkmodel.l3vpn.enums.NetL3vpnStatus;

public class L3vpnInstance implements NetModel {

    private String instanceName;

    private String description;

    private String afType = NetL3vpnAfType.IPV4UNI.getName();

    private String serviceType = NetL3vpnServiceType.FULLMESH.getName();

    private String adminStatus = NetL3vpnStatus.UP.getName();

    private String operStatus = NetL3vpnStatus.UP.getName();

    private L3vpnNodes nodes;

    public String getAdminStatus() {
        return adminStatus;
    }

    public void setAdminStatus(String adminStatus) {
        this.adminStatus = adminStatus;
    }

    public String getOperStatus() {
        return operStatus;
    }

    public void setOperStatus(String operStatus) {
        this.operStatus = operStatus;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAfType() {
        return afType;
    }

    public void setAfType(String afType) {
        this.afType = afType;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public L3vpnNodes getNodes() {
        return nodes;
    }

    public void setNodes(L3vpnNodes nodes) {
        this.nodes = nodes;
    }
}
