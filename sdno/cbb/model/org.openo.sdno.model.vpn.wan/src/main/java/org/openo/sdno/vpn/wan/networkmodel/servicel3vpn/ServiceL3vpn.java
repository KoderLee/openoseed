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

@XmlRootElement(name = "service-l3vpn")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"l3vpns"})
@JsonRootName(value = "service-l3vpn")
@JsonSerialize(include = Inclusion.NON_NULL)
@JsonPropertyOrder(value = {"l3vpns"})
public class ServiceL3vpn implements NetModel {

    @XmlElementWrapper(name = "l3vpns")
    @XmlElement(name = "l3vpn")
    @JsonProperty("l3vpns")
    private List<L3vpn> l3vpns;

    public List<L3vpn> getL3vpns() {
        return l3vpns;
    }

    public void setL3vpns(List<L3vpn> l3vpns) {
        this.l3vpns = l3vpns;
    }
}
