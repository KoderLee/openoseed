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

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;

@XmlRootElement(name = "path")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonRootName(value = "path")
public class Path {

    @XmlElement(name = "ne-id")
    @JsonProperty("ne-id")
    private String neId;

    @XmlElement(name = "ingress-ltp-id")
    @JsonProperty("ingress-ltp-id")
    private String ingressLtpId;

    @XmlElement(name = "backward-peer-id")
    @JsonProperty("backward-peer-id")
    private String backwardPeerId;

    @XmlElement(name = "egress-ltp-id")
    @JsonProperty("egress-ltp-id")
    private String egressLtpId;

    @XmlElement(name = "forward-peer-id")
    @JsonProperty("forward-peer-id")
    private String forwardPeerId;

    public String getNeId() {
        return neId;
    }

    public void setNeId(String neId) {
        this.neId = neId;
    }

    public String getIngressLtpId() {
        return ingressLtpId;
    }

    public void setIngressLtpId(String ingressLtpId) {
        this.ingressLtpId = ingressLtpId;
    }

    public String getBackwardPeerId() {
        return backwardPeerId;
    }

    public void setBackwardPeerId(String backwardPeerId) {
        this.backwardPeerId = backwardPeerId;
    }

    public String getEgressLtpId() {
        return egressLtpId;
    }

    public void setEgressLtpId(String egressLtpId) {
        this.egressLtpId = egressLtpId;
    }

    public String getForwardPeerId() {
        return forwardPeerId;
    }

    public void setForwardPeerId(String forwardPeerId) {
        this.forwardPeerId = forwardPeerId;
    }
}
