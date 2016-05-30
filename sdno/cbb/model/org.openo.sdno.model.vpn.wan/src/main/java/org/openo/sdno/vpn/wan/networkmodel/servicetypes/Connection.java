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

import org.openo.sdno.vpn.wan.networkmodel.l3vpn.StaticRoute;

@XmlRootElement(name = "connection")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"ipAddress", "maskLength", "protocols", "staticRoutes", "bgpParameters"})
@JsonRootName(value = "connection")
@JsonSerialize(include = Inclusion.NON_NULL)
@JsonPropertyOrder(value = {"ipAddress", "maskLength", "protocols", "staticRoutes", "bgpParameters"})
public class Connection {

    @XmlElement(name = "ip-address")
    @JsonProperty("ip-address")
    private String ipAddress;

    @XmlElement(name = "mask-length")
    @JsonProperty("mask-length")
    private Integer maskLength;

    @XmlElement(name = "protocols")
    @JsonProperty("protocols")
    private String protocols;

    @XmlElementWrapper(name = "static-routes")
    @XmlElement(name = "static-route")
    @JsonProperty("static-routes")
    private List<StaticRoute> staticRoutes;

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Integer getMaskLength() {
        return maskLength;
    }

    public void setMaskLength(Integer maskLength) {
        this.maskLength = maskLength;
    }

    public String getProtocols() {
        return protocols;
    }

    public void setProtocols(String protocols) {
        this.protocols = protocols;
    }

    public List<StaticRoute> getStaticRoutes() {
        return staticRoutes;
    }

    public void setStaticRoutes(List<StaticRoute> staticRoutes) {
        this.staticRoutes = staticRoutes;
    }

}
