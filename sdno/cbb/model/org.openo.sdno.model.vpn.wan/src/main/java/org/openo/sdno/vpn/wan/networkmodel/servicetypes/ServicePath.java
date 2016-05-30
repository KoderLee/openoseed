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

import org.codehaus.jackson.annotate.JsonProperty;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "service-path")
public class ServicePath {

    @XmlElement(name = "service-id")
    @JsonProperty("service-id")
    private String serviceId;

    @XmlElement(name = "source-ne-id")
    @JsonProperty("source-ne-id")
    private String sourceNeId;

    @XmlElement(name = "source-ac-id")
    @JsonProperty("source-ac-id")
    private String sourceAcId;

    @XmlElement(name = "destination-ne-id")
    @JsonProperty("destination-ne-id")
    private String destinationNeId;

    @XmlElement(name = "destination-ac-id")
    @JsonProperty("destination-ac-id")
    private String destinationAcId;

    @XmlElementWrapper(name = "path-lists")
    @XmlElement(name = "path-list")
    @JsonProperty("path-lists")
    private List<PathList> pathLists;

    public String getPathId() {
        return serviceId;
    }

    public void setPathId(String pathId) {
        this.serviceId = pathId;
    }

    public String getSourceNeId() {
        return sourceNeId;
    }

    public void setSourceNeId(String sourceNeId) {
        this.sourceNeId = sourceNeId;
    }

    public String getSourceAcId() {
        return sourceAcId;
    }

    public void setSourceAcId(String sourceAcId) {
        this.sourceAcId = sourceAcId;
    }

    public String getDestinationNeId() {
        return destinationNeId;
    }

    public void setDestinationNeId(String destinationNeId) {
        this.destinationNeId = destinationNeId;
    }

    public String getDestinationAcId() {
        return destinationAcId;
    }

    public void setDestinationAcId(String destinationAcId) {
        this.destinationAcId = destinationAcId;
    }

    public List<PathList> getPathLists() {
        return pathLists;
    }

    public void setPathLists(List<PathList> pathLists) {
        this.pathLists = pathLists;
    }

}
