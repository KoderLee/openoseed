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
package org.openo.sdno.vpn.wan.db.vpn;

import java.util.List;

import org.openo.sdno.inventory.sdk.model.annotation.MOInvField;
import org.openo.sdno.inventory.sdk.model.annotation.MOResType;
import org.openo.sdno.inventory.sdk.model.annotation.MOUUIDField;
import org.openo.sdno.inventory.sdk.model.annotation.NONInvField;
import org.openo.sdno.vpn.wan.db.FieldConvertUtil;
import org.openo.sdno.vpn.wan.db.PoModel;
import org.openo.sdno.vpn.wan.servicemodel.common.NvString;
import org.openo.sdno.vpn.wan.servicemodel.vpn.Vpn;

@MOResType(infoModelName = "wan_vpn")
public class VpnPo implements PoModel<Vpn> {

    @MOUUIDField
    @MOInvField(invName = "uuid")
    private String id;

    private String name;

    private String description;

    private String customerName;

    private String templateName;

    private String vpnBasicInfoId;

    private String operStatus;

    private String startTime;

    private String endTime;

    private String overlayVpnBaseInfoId;

    private String controllerId;

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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getVpnBasicInfoId() {
        return vpnBasicInfoId;
    }

    public void setVpnBasicInfoId(String vpnBasicInfoId) {
        this.vpnBasicInfoId = vpnBasicInfoId;
    }

    public String getOperStatus() {
        return operStatus;
    }

    public void setOperStatus(String operStatus) {
        this.operStatus = operStatus;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getOverlayVpnBaseInfoId() {
        return overlayVpnBaseInfoId;
    }

    public void setOverlayVpnBaseInfoId(String overlayVpnBaseInfoId) {
        this.overlayVpnBaseInfoId = overlayVpnBaseInfoId;
    }

    public List<NvString> getAddtionalInfo() {
        return addtionalInfo;
    }

    public void setAddtionalInfo(List<NvString> addtionalInfo) {
        this.addtionalInfo = addtionalInfo;
    }

    @Override
    public Vpn toSvcModel() {
        final Vpn vpn = new Vpn();
        FieldConvertUtil.setA2B(this, vpn);
        return vpn;
    }

    @Override
    public void fromSvcModel(Vpn svcModel) {
        FieldConvertUtil.setA2B(svcModel, this);
    }

    @Override
    public String getUuid() {
        return id;
    }

    @Override
    public void setUuid(String uuid) {
        id = uuid;
    }

    public String getControllerId() {
        return controllerId;
    }

    public void setControllerId(String controllerId) {
        this.controllerId = controllerId;
    }
}
