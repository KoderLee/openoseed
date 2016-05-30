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

@XmlRootElement(name = "static-route")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"ipPrefix", "maskLength", "nextHop", "preference"})
@JsonRootName(value = "static-route")
@JsonSerialize(include = Inclusion.NON_NULL)
@JsonPropertyOrder(value = {"ipPrefix", "maskLength", "nextHop", "preference"})
public class StaticRoute {

    @XmlElement(name = "ip-prefix")
    @JsonProperty("ip-prefix")
    private String ipPrefix;

    @XmlElement(name = "mask-length")
    @JsonProperty("mask-length")
    private Integer maskLength;

    @XmlElement(name = "next-hop")
    @JsonProperty("next-hop")
    private String nextHop;

    @XmlElement(name = "preference")
    @JsonProperty("preference")
    private Integer preference;

    public String getIpPrefix() {
        return ipPrefix;
    }

    public void setIpPrefix(String ipPrefix) {
        this.ipPrefix = ipPrefix;
    }

    public Integer getMaskLength() {
        return maskLength;
    }

    public void setMaskLength(Integer maskLength) {
        this.maskLength = maskLength;
    }

    public String getNextHop() {
        return nextHop;
    }

    public void setNextHop(String nextHop) {
        this.nextHop = nextHop;
    }

    public Integer getPreference() {
        return preference;
    }

    public void setPreference(Integer preference) {
        this.preference = preference;
    }

}
