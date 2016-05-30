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

import org.codehaus.jackson.annotate.JsonProperty;

@XmlAccessorType(XmlAccessType.FIELD)
public class Pw {

    @XmlElement(name = "id")
    @JsonProperty("id")
    private String id;

    @XmlElement(name = "encaplate-type")
    @JsonProperty("encaplate-type")
    private Integer encaplateType;

    @XmlElement(name = "ingress-ne-id")
    @JsonProperty("ingress-ne-id")
    private String ingressNeId;

    @XmlElement(name = "egress-ne-id")
    @JsonProperty("egress-ne-id")
    private String egressNeId;

    @XmlElement(name = "ctrl-word-support")
    @JsonProperty("ctrl-word-support")
    private String ctrlWordSupport;

    @XmlElement(name = "sn-support")
    @JsonProperty("sn-support")
    private String snSupport;

    @XmlElement(name = "vccv-type")
    @JsonProperty("vccv-type")
    private String vccvType;

    @XmlElement(name = "conn-ack-type")
    @JsonProperty("conn-ack-type")
    private String connAckType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getEncaplateType() {
        return encaplateType;
    }

    public void setEncaplateType(Integer encaplateType) {
        this.encaplateType = encaplateType;
    }

    public String getIngressNeId() {
        return ingressNeId;
    }

    public void setIngressNeId(String ingressNeId) {
        this.ingressNeId = ingressNeId;
    }

    public String getEgressNeId() {
        return egressNeId;
    }

    public void setEgressNeId(String egressNeId) {
        this.egressNeId = egressNeId;
    }

    public String getCtrlWordSupport() {
        return ctrlWordSupport;
    }

    public void setCtrlWordSupport(String ctrlWordSupport) {
        this.ctrlWordSupport = ctrlWordSupport;
    }

    public String getSnSupport() {
        return snSupport;
    }

    public void setSnSupport(String snSupport) {
        this.snSupport = snSupport;
    }

    public String getVccvType() {
        return vccvType;
    }

    public void setVccvType(String vccvType) {
        this.vccvType = vccvType;
    }

    public String getConnAckType() {
        return connAckType;
    }

    public void setConnAckType(String connAckType) {
        this.connAckType = connAckType;
    }

}
