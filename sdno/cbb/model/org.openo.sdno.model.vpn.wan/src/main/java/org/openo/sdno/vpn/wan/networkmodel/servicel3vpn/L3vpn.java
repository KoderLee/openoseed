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
package org.openo.sdno.vpn.wan.networkmodel.servicel3vpn;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonRootName;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import org.openo.sdno.vpn.wan.networkmodel.NetModel;
import org.openo.sdno.vpn.wan.networkmodel.servicetypes.L3Ac;
import org.openo.sdno.vpn.wan.networkmodel.servicetypes.TunnelService;

@XmlRootElement(name = "l3vpn")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"id", "name", "userLabel", "parentNcdId", "adminStatus", "operateStatus", "serviceType", "acs",
                 "tunnelService"})
@JsonRootName(value = "l3vpn")
@JsonSerialize(include = Inclusion.NON_NULL)
@JsonPropertyOrder(value = {"id", "name", "userLabel", "parentNcdId", "adminStatus", "operateStatus", "serviceType",
                "acs", "tunnelService"})
public class L3vpn implements NetModel {

    @XmlElement(name = "id")
    @JsonProperty("id")
    private String id;

    @XmlElement(name = "name")
    @JsonProperty("name")
    private String name;

    @XmlElement(name = "user-label")
    @JsonProperty("user-label")
    private String userLabel;

    @XmlElement(name = "parent-ncd-id")
    @JsonProperty("parent-ncd-id")
    private String parentNcdId;

    @XmlElement(name = "admin-status")
    @JsonProperty("admin-status")
    private String adminStatus;

    @XmlElement(name = "operate-status")
    @JsonProperty("operate-status")
    private String operateStatus;

    @XmlElementWrapper(name = "acs")
    @XmlElement(name = "ac")
    @JsonProperty("acs")
    private List<L3Ac> acs;

    @XmlElement(name = "service-type")
    @JsonProperty("service-type")
    private String serviceType;

    @XmlElement(name = "tunnel-service")
    @JsonProperty("tunnel-service")
    private TunnelService tunnelService;

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

    public String getUserLabel() {
        return userLabel;
    }

    public void setUserLabel(String userLabel) {
        this.userLabel = userLabel;
    }

    public String getParentNcdId() {
        return parentNcdId;
    }

    public void setParentNcdId(String parentNcdId) {
        this.parentNcdId = parentNcdId;
    }

    public String getAdminStatus() {
        return adminStatus;
    }

    public void setAdminStatus(String adminStatus) {
        this.adminStatus = adminStatus;
    }

    public String getOperateStatus() {
        return operateStatus;
    }

    public void setOperateStatus(String operateStatus) {
        this.operateStatus = operateStatus;
    }

    public List<L3Ac> getAcs() {
        return acs;
    }

    public void setAcs(List<L3Ac> acs) {
        this.acs = acs;
    }

    public TunnelService getTunnelService() {
        return tunnelService;
    }

    public void setTunnelService(TunnelService tunnelService) {
        this.tunnelService = tunnelService;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }
}
