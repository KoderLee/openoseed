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

import org.openo.sdno.vpn.wan.servicemodel.tp.Tp;

@XmlRootElement(name = "ac")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"connection"})
@JsonRootName(value = "ac")
@JsonSerialize(include = Inclusion.NON_NULL)
@JsonPropertyOrder(value = {"id", "name", "neId", "ltpId", "adminStatus", "operateStatus", "role", "l2Access",
                "qosPolicy", "connection"})
public class L3Ac extends Ac {

    @XmlElement(name = "connection")
    @JsonProperty("connection")
    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void inject(Tp tp) {
        setLtpId(tp.getParentTp());
        setId(tp.getId());
        setNeId(tp.getNeId());
    }
}
