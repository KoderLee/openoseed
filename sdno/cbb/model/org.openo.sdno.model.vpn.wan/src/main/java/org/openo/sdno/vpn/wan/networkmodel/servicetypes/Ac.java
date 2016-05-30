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
package org.openo.sdno.vpn.wan.networkmodel.servicetypes;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonRootName;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import org.openo.sdno.vpn.wan.networkmodel.NetModel;

/**
 * Attachment Circuit.<br/>
 * 
 * @author
 * @version SDNO 0.5 2016-3-18
 */
@XmlRootElement(name = "ac")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"id", "name", "neId", "ltpId", "adminStatus", "operateStatus", "role", "l2Access", "qosPolicy"})
@JsonRootName(value = "ac")
@JsonSerialize(include = Inclusion.NON_NULL)
@JsonPropertyOrder(value = {"id", "name", "neId", "ltpId", "adminStatus", "operateStatus", "role", "l2Access",
                "qosPolicy"})
public class Ac implements NetModel {

    @XmlElement(name = "id")
    @JsonProperty("id")
    private String id;

    @XmlElement(name = "name")
    @JsonProperty("name")
    private String name;

    @XmlElement(name = "ne-id")
    @JsonProperty("ne-id")
    private String neId;

    @XmlElement(name = "ltp-id")
    @JsonProperty("ltp-id")
    private String ltpId;

    @XmlElement(name = "admin-status")
    @JsonProperty("admin-status")
    private String adminStatus;

    @XmlElement(name = "operate-status")
    @JsonProperty("operate-status")
    private String operateStatus;

    @XmlElement(name = "l2-access")
    @JsonProperty("l2-access")
    private L2Access l2Access;

    @XmlElement(name = "role")
    @JsonProperty("role")
    private String role;

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

    public String getNeId() {
        return neId;
    }

    public void setNeId(String neId) {
        this.neId = neId;
    }

    public String getLtpId() {
        return ltpId;
    }

    public void setLtpId(String ltpId) {
        this.ltpId = ltpId;
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

    public L2Access getL2Access() {
        return l2Access;
    }

    public void setL2Access(L2Access l2Access) {
        this.l2Access = l2Access;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
