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

@XmlRootElement(name = "l2-access")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"accessType", "dot1qVlanBitmap", "qinqSvlanBitmap", "qinqCvlanBitmap", "accessAction",
                "actionVlanId"})
@JsonRootName(value = "l2-access")
@JsonSerialize(include = Inclusion.NON_NULL)
@JsonPropertyOrder(value = {"accessType", "dot1qVlanBitmap", "qinqSvlanBitmap", "qinqCvlanBitmap", "accessAction",
                "actionVlanId"})
public class L2Access {

    @XmlElement(name = "access-type")
    @JsonProperty("access-type")
    private String accessType;

    @XmlElement(name = "dot1q-vlan-bitmap")
    @JsonProperty("dot1q-vlan-bitmap")
    private String dot1qVlanBitmap;

    @XmlElement(name = "qinq-svlan-bitmap")
    @JsonProperty("qinq-svlan-bitmap")
    private String qinqSvlanBitmap;

    @XmlElement(name = "qinq-cvlan-bitmap")
    @JsonProperty("qinq-cvlan-bitmap")
    private String qinqCvlanBitmap;

    @XmlElement(name = "access-action")
    @JsonProperty("access-action")
    private String accessAction;

    @XmlElement(name = "action-vlan-id")
    @JsonProperty("action-vlan-id")
    private Integer actionVlanId;

    public String getAccessType() {
        return accessType;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }

    public String getDot1qVlanBitmap() {
        return dot1qVlanBitmap;
    }

    public void setDot1qVlanBitmap(String dot1qVlanBitmap) {
        this.dot1qVlanBitmap = dot1qVlanBitmap;
    }

    public String getQinqSvlanBitmap() {
        return qinqSvlanBitmap;
    }

    public void setQinqSvlanBitmap(String qinqSvlanBitmap) {
        this.qinqSvlanBitmap = qinqSvlanBitmap;
    }

    public String getQinqCvlanBitmap() {
        return qinqCvlanBitmap;
    }

    public void setQinqCvlanBitmap(String qinqCvlanBitmap) {
        this.qinqCvlanBitmap = qinqCvlanBitmap;
    }

    public String getAccessAction() {
        return accessAction;
    }

    public void setAccessAction(String accessAction) {
        this.accessAction = accessAction;
    }

    public Integer getActionVlanId() {
        return actionVlanId;
    }

    public void setActionVlanId(Integer actionVlanId) {
        this.actionVlanId = actionVlanId;
    }

}
