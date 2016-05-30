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
package org.openo.sdno.vpn.wan.db.tp;

import java.util.List;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import org.openo.sdno.inventory.sdk.model.annotation.MOInvField;
import org.openo.sdno.inventory.sdk.model.annotation.MOResType;
import org.openo.sdno.inventory.sdk.model.annotation.MOUUIDField;
import org.openo.sdno.inventory.sdk.model.annotation.NONInvField;
import org.openo.sdno.vpn.wan.db.FieldConvertUtil;
import org.openo.sdno.vpn.wan.db.PoModel;
import org.openo.sdno.vpn.wan.servicemodel.common.NvString;
import org.openo.sdno.vpn.wan.servicemodel.tp.Tp;
import com.puer.framework.base.util.JsonUtil;

@MOResType(infoModelName = "wan_tp")
public class TpPo implements PoModel<Tp> {

    @MOUUIDField
    @MOInvField(invName = "uuid")
    private String id;

    private String name;

    private String description;

    private String containingAsName;

    private String neId;

    private String edgePointRole;

    private String templateRole;

    private String templateName;

    private String topologyRole;

    private String type;

    private String detailType;

    private Integer maxBandwidth;

    private String workingLayer;

    private String containedLayer;

    private String adminStatus;

    private String operStatus;

    private String parentTp;

    private String peerCeTpId;

    private String pathInfoId4Include;

    @NONInvField
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

    public String getContainedLayer() {
        return containedLayer;
    }

    public void setContainedLayer(String containedLayer) {
        this.containedLayer = containedLayer;
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

    public String getPeerCeTpId() {
        return peerCeTpId;
    }

    public void setPeerCeTpId(String peerCeTpId) {
        this.peerCeTpId = peerCeTpId;
    }

    public List<NvString> getAddtionalInfo() {
        return addtionalInfo;
    }

    public void setAddtionalInfo(List<NvString> addtionalInfo) {
        this.addtionalInfo = addtionalInfo;
    }

    public String getPathInfoId4Include() {
        return pathInfoId4Include;
    }

    public void setPathInfoId4Include(String pathInfoId4Include) {
        this.pathInfoId4Include = pathInfoId4Include;
    }

    public String getParentTp() {
        return parentTp;
    }

    public void setParentTp(String parentTp) {
        this.parentTp = parentTp;
    }

    public String getEdgePointRole() {
        return edgePointRole;
    }

    public void setEdgePointRole(String edgePointRole) {
        this.edgePointRole = edgePointRole;
    }

    @Override
    public String getUuid() {
        return id;
    }

    @Override
    public void setUuid(String uuid) {
        id = uuid;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Tp toSvcModel() {
        final Tp svcModel = new Tp();
        FieldConvertUtil.setA2B(this, svcModel);
        if(StringUtils.hasLength(containedLayer)) {
            svcModel.setContainedLayer(JsonUtil.fromJson(containedLayer, List.class));
        }
        return svcModel;
    }

    @Override
    public void fromSvcModel(Tp svcModel) {
        FieldConvertUtil.setA2B(svcModel, this);
        if(CollectionUtils.isEmpty(svcModel.getContainedLayer())) {
            setContainedLayer(JsonUtil.toJson(svcModel.getContainedLayer()));
        }
    }

    public String getTopologyRole() {
        return topologyRole;
    }

    public void setTopologyRole(String topologyRole) {
        this.topologyRole = topologyRole;
    }
}
