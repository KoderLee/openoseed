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
package org.openo.sdno.vpn.wan.servicemodel.vpn;

import java.util.List;

import org.openo.sdno.vpn.wan.paradesc.ContainerSizeDesc;
import org.openo.sdno.vpn.wan.paradesc.EnumDesc;
import org.openo.sdno.vpn.wan.paradesc.StringDesc;
import org.openo.sdno.vpn.wan.servicemodel.AbstractSvcModel;
import org.openo.sdno.vpn.wan.servicemodel.common.AdminStatus;
import org.openo.sdno.vpn.wan.servicemodel.common.NvString;
import org.openo.sdno.vpn.wan.servicemodel.common.ServiceType;
import org.openo.sdno.vpn.wan.servicemodel.common.TechnologyType;
import org.openo.sdno.vpn.wan.servicemodel.common.TopologyType;
import org.openo.sdno.vpn.wan.servicemodel.topology.Topology;
import org.openo.sdno.vpn.wan.servicemodel.tp.Tp;

public class VpnBasicInfo extends AbstractSvcModel {

    @StringDesc(maxLen = 36)
    private String uuid;

    @EnumDesc(TopologyType.class)
    private String topology;

    @ContainerSizeDesc(maxSize = 1000)
    private List<Topology> toplogyspec;

    @EnumDesc(ServiceType.class)
    private String serviceType;

    @EnumDesc(TechnologyType.class)
    private String technology;

    @EnumDesc(AdminStatus.class)
    private String adminStatus;

    @ContainerSizeDesc(maxSize = 1000)
    private List<Tp> accessPointList;

    @ContainerSizeDesc(maxSize = 1000)
    @StringDesc(maxLen = 200)
    private List<String> serverConnectionList;

    @ContainerSizeDesc(maxSize = 1000)
    private List<PathInfo> pathList;

    private boolean cn3DciScene = false;

    private List<String> nes;

    @ContainerSizeDesc(maxSize = 1000)
    private List<NvString> addtionalInfo;

    public String getTopology() {
        return topology;
    }

    public void setTopology(String topology) {
        this.topology = topology;
    }

    public List<Topology> getToplogyspec() {
        return toplogyspec;
    }

    public void setToplogyspec(List<Topology> toplogyspec) {
        this.toplogyspec = toplogyspec;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getTechnology() {
        return technology;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }

    public String getAdminStatus() {
        return adminStatus;
    }

    public void setAdminStatus(String adminStatus) {
        this.adminStatus = adminStatus;
    }

    public List<Tp> getAccessPointList() {
        return accessPointList;
    }

    public void setAccessPointList(List<Tp> accessPointList) {
        this.accessPointList = accessPointList;
    }

    public List<String> getServerConnectionList() {
        return serverConnectionList;
    }

    public void setServerConnectionList(List<String> serverConnectionList) {
        this.serverConnectionList = serverConnectionList;
    }

    public List<PathInfo> getPathList() {
        return pathList;
    }

    public void setPathList(List<PathInfo> pathList) {
        this.pathList = pathList;
    }

    public List<NvString> getAddtionalInfo() {
        return addtionalInfo;
    }

    public void setAddtionalInfo(List<NvString> addtionalInfo) {
        this.addtionalInfo = addtionalInfo;
    }

    @Override
    public String getUuid() {
        return uuid;
    }

    @Override
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public boolean isCn3DciScene() {
        return cn3DciScene;
    }

    public void setCn3DciScene(boolean cn3DciScene) {
        this.cn3DciScene = cn3DciScene;
    }

    public List<String> getNes() {
        return nes;
    }

    public void setNes(final List<String> nes) {
        this.nes = nes;
    }

}
