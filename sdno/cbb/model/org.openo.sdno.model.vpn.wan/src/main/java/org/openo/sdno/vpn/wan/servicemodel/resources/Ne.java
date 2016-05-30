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
package org.openo.sdno.vpn.wan.servicemodel.resources;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

import org.openo.sdno.vpn.wan.paradesc.ContainerSizeDesc;
import org.openo.sdno.vpn.wan.paradesc.EnumDesc;
import org.openo.sdno.vpn.wan.paradesc.IntegerDesc;
import org.openo.sdno.vpn.wan.paradesc.IpDesc;
import org.openo.sdno.vpn.wan.paradesc.StringDesc;
import org.openo.sdno.vpn.wan.servicemodel.AbstractSvcModel;
import org.openo.sdno.vpn.wan.servicemodel.common.AdminStatus;
import org.openo.sdno.vpn.wan.servicemodel.common.DomainRole;
import org.openo.sdno.vpn.wan.servicemodel.common.EdgePointRole;
import org.openo.sdno.vpn.wan.servicemodel.common.NvString;
import org.openo.sdno.vpn.wan.servicemodel.common.OperStatus;

public class Ne extends AbstractSvcModel {

    @StringDesc(maxLen = 36)
    private String id;

    @StringDesc(maxLen = 200)
    private String name;

    @StringDesc(maxLen = 200)
    private String description;

    @StringDesc(maxLen = 200)
    private String location;

    @StringDesc(maxLen = 200)
    private String longitude;

    @StringDesc(maxLen = 200)
    private String latitude;

    @StringDesc(maxLen = 200)
    private String deviceDetailType;

    @IpDesc
    private String adminIp;

    @StringDesc(maxLen = 200)
    private String version;

    @StringDesc(maxLen = 200)
    private String vendorName;

    @EnumDesc(AdminStatus.class)
    private String adminStatus;

    @EnumDesc(OperStatus.class)
    private String operStatus;

    @StringDesc(maxLen = 200)
    private String belongingAdminSubnetworkName;

    @IntegerDesc(minVal = 0)
    private Integer asNumber;

    @EnumDesc(DomainRole.class)
    private String domainRole;

    @EnumDesc(EdgePointRole.class)
    private String edgePointRole;

    @IntegerDesc(minVal = 0)
    private Integer cost;

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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getDeviceDetailType() {
        return deviceDetailType;
    }

    public void setDeviceDetailType(String deviceDetailType) {
        this.deviceDetailType = deviceDetailType;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
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

    public String getBelongingAdminSubnetworkName() {
        return belongingAdminSubnetworkName;
    }

    public void setBelongingAdminSubnetworkName(String belongingAdminSubnetworkName) {
        this.belongingAdminSubnetworkName = belongingAdminSubnetworkName;
    }

    public Integer getAsNumber() {
        return asNumber;
    }

    public void setAsNumber(Integer asNumber) {
        this.asNumber = asNumber;
    }

    public String getDomainRole() {
        return domainRole;
    }

    public void setDomainRole(String domainRole) {
        this.domainRole = domainRole;
    }

    public String getEdgePointRole() {
        return edgePointRole;
    }

    public void setEdgePointRole(String edgePointRole) {
        this.edgePointRole = edgePointRole;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public List<NvString> getAddtionalInfo() {
        return addtionalInfo;
    }

    public void setAddtionalInfo(List<NvString> addtionalInfo) {
        this.addtionalInfo = addtionalInfo;
    }

    public String getAdminIp() {
        return adminIp;
    }

    public void setAdminIp(String adminIp) {
        this.adminIp = adminIp;
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

    /**
     * @return Returns the latitude.
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * @param latitude The latitude to set.
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
}
