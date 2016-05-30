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

import org.codehaus.jackson.annotate.JsonIgnore;

import org.openo.sdno.vpn.wan.paradesc.ContainerSizeDesc;
import org.openo.sdno.vpn.wan.paradesc.EnumDesc;
import org.openo.sdno.vpn.wan.paradesc.StringDesc;
import org.openo.sdno.vpn.wan.servicemodel.AbstractSvcModel;
import org.openo.sdno.vpn.wan.servicemodel.common.NvString;
import org.openo.sdno.vpn.wan.servicemodel.common.OperStatus;

public class MultiAsVpn extends AbstractSvcModel {

    @StringDesc(maxLen = 36)
    private String id;

    @StringDesc(maxLen = 200)
    private String name;

    @StringDesc(maxLen = 200)
    private String description;

    @StringDesc(maxLen = 200)
    private String customerName;

    @StringDesc(maxLen = 200)
    private String businessTypeName;

    private VpnBasicInfo vpnBasicInfo;

    @ContainerSizeDesc(maxSize = 1000)
    private List<SegmentVpn> segVpnList;

    @EnumDesc(OperStatus.class)
    private String operStatus;

    @StringDesc(maxLen = 200)
    private String startTime;

    @StringDesc(maxLen = 200)
    private String endTime;

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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getBusinessTypeName() {
        return businessTypeName;
    }

    public void setBusinessTypeName(String businessTypeName) {
        this.businessTypeName = businessTypeName;
    }

    public VpnBasicInfo getVpnBasicInfo() {
        return vpnBasicInfo;
    }

    public void setVpnBasicInfo(VpnBasicInfo vpnBasicInfo) {
        this.vpnBasicInfo = vpnBasicInfo;
    }

    public List<SegmentVpn> getSegVpnList() {
        return segVpnList;
    }

    public void setSegVpnList(List<SegmentVpn> segVpnList) {
        this.segVpnList = segVpnList;
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

    public List<NvString> getAddtionalInfo() {
        return addtionalInfo;
    }

    public void setAddtionalInfo(List<NvString> addtionalInfo) {
        this.addtionalInfo = addtionalInfo;
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
}
