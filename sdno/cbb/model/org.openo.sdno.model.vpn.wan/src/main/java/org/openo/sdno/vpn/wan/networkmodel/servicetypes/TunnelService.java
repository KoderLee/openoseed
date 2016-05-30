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

@XmlRootElement(name = "tunnel-service")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"signalingType", "tunnelMode", "latency",  "oamPolicy"})
@JsonRootName(value = "tunnel-service")
@JsonSerialize(include = Inclusion.NON_NULL)
@JsonPropertyOrder(value = {"signalingType", "tunnelMode", "latency",  "oamPolicy"})
public class TunnelService implements NetModel {

    @XmlElement(name = "signaling-type")
    @JsonProperty("signaling-type")
    private String signalingType;

    @XmlElement(name = "tunnel-mode")
    @JsonProperty("tunnel-mode")
    private String tunnelMode;

    @XmlElement(name = "latency")
    @JsonProperty("latency")
    private Integer latency;

    public String getSignalingType() {
        return signalingType;
    }

    public void setSignalingType(String signalingType) {
        this.signalingType = signalingType;
    }

    public String getTunnelMode() {
        return tunnelMode;
    }

    public void setTunnelMode(String tunnelMode) {
        this.tunnelMode = tunnelMode;
    }

    public Integer getLatency() {
        return latency;
    }

    public void setLatency(Integer latency) {
        this.latency = latency;
    }

}
