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
package org.openo.sdno.vpn.wan.servicemodel.tp;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

import org.openo.sdno.vpn.wan.paradesc.ContainerSizeDesc;
import org.openo.sdno.vpn.wan.paradesc.EnumDesc;
import org.openo.sdno.vpn.wan.paradesc.IntegerDesc;
import org.openo.sdno.vpn.wan.paradesc.StringDesc;
import org.openo.sdno.vpn.wan.servicemodel.AbstractSvcModel;
import org.openo.sdno.vpn.wan.servicemodel.common.AdminStatus;
import org.openo.sdno.vpn.wan.servicemodel.common.EdgePointRole;
import org.openo.sdno.vpn.wan.servicemodel.common.LayerRate;
import org.openo.sdno.vpn.wan.servicemodel.common.NvString;
import org.openo.sdno.vpn.wan.servicemodel.common.OperStatus;
import org.openo.sdno.vpn.wan.servicemodel.common.TemplateRole;
import org.openo.sdno.vpn.wan.servicemodel.common.TopoNodeRole;
import org.openo.sdno.vpn.wan.servicemodel.common.TpDetailType;
import org.openo.sdno.vpn.wan.servicemodel.common.TpType;
import org.openo.sdno.vpn.wan.servicemodel.routeprotocol.RouteProtocolSpec;

public class Tp extends AbstractSvcModel {

    /**
     * UUID-STR for TP.
     */
    @StringDesc(maxLen = 36)
    private String id;

    /**
     * Must abbey to name rule defined in system.
     */
    @StringDesc(maxLen = 200)
    private String name;

    @StringDesc(maxLen = 200)
    private String description;

    @StringDesc(maxLen = 200)
    private String containingAsName;

    /**
     * UUID-STR for NE.
     */
    @StringDesc(maxLen = 36)
    private String neId;

    @EnumDesc(TemplateRole.class)
    private String templateRole;

    @EnumDesc(EdgePointRole.class)
    private String edgePointRole;

    @StringDesc(maxLen = 200)
    private String templateName;

    @EnumDesc(TopoNodeRole.class)
    private String topologyRole;

    @EnumDesc(TpType.class)
    private String type;

    @EnumDesc(TpDetailType.class)
    private String detailType;

    @IntegerDesc(minVal = 0)
    private Integer maxBandwidth;

    @EnumDesc(LayerRate.class)
    private String workingLayer;

    @EnumDesc(LayerRate.class)
    @ContainerSizeDesc(maxSize = 1000)
    private List<String> containedLayer;

    @ContainerSizeDesc(maxSize = 1000)
    private List<TpTypeSpec> typeSpecList;

    @EnumDesc(AdminStatus.class)
    private String adminStatus;

    @EnumDesc(OperStatus.class)
    private String operStatus;

    @StringDesc(maxLen = 36)
    private String parentTp;

    private CeTp peerCeTp;

    @ContainerSizeDesc(maxSize = 1000)
    private List<RouteProtocolSpec> routeProtocolSpecs;

    @ContainerSizeDesc(maxSize = 1000)
    private List<NvString> addtionalInfo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContainingAsName() {
        return containingAsName;
    }

    public void setContainingAsName(String containingAsName) {
        this.containingAsName = containingAsName;
    }

    public String getNeId() {
        return neId;
    }

    public void setNeId(String neId) {
        this.neId = neId;
    }

    public String getTemplateRole() {
        return templateRole;
    }

    public void setTemplateRole(String templateRole) {
        this.templateRole = templateRole;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDetailType() {
        return detailType;
    }

    public void setDetailType(String detailType) {
        this.detailType = detailType;
    }

    public Integer getMaxBandwidth() {
        return maxBandwidth;
    }

    public void setMaxBandwidth(Integer maxBandwidth) {
        this.maxBandwidth = maxBandwidth;
    }

    public String getWorkingLayer() {
        return workingLayer;
    }

    public void setWorkingLayer(String workingLayer) {
        this.workingLayer = workingLayer;
    }

    public List<String> getContainedLayer() {
        return containedLayer;
    }

    public void setContainedLayer(List<String> containedLayer) {
        this.containedLayer = containedLayer;
    }

    public List<TpTypeSpec> getTypeSpecList() {
        return typeSpecList;
    }

    public void setTypeSpecList(List<TpTypeSpec> typeSpecList) {
        this.typeSpecList = typeSpecList;
    }

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

    public CeTp getPeerCeTp() {
        return peerCeTp;
    }

    public void setPeerCeTp(CeTp peerCeTp) {
        this.peerCeTp = peerCeTp;
    }

    public List<RouteProtocolSpec> getRouteProtocolSpecs() {
        return routeProtocolSpecs;
    }

    public void setRouteProtocolSpecs(List<RouteProtocolSpec> routeProtocolSpecs) {
        this.routeProtocolSpecs = routeProtocolSpecs;
    }

    public List<NvString> getAddtionalInfo() {
        return addtionalInfo;
    }

    public void setAddtionalInfo(List<NvString> addtionalInfo) {
        this.addtionalInfo = addtionalInfo;
    }

    public String getParentTp() {
        return parentTp;
    }

    public void setParentTp(String parentTp) {
        this.parentTp = parentTp;
    }

    @Override
    @JsonIgnore
    public String getUuid() {
        return id;
    }

    @Override
    @JsonIgnore
    public void setUuid(String uuid) {
        id = uuid;
    }

    public String getTopologyRole() {
        return topologyRole;
    }

    public void setTopologyRole(String topologyRole) {
        this.topologyRole = topologyRole;
    }

    public String getEdgePointRole() {
        return edgePointRole;
    }

    public void setEdgePointRole(String edgePointRole) {
        this.edgePointRole = edgePointRole;
    }

}
