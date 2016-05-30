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

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "path-list")
@JsonRootName(value = "path-list")
@XmlType(propOrder = {"pathLayer", "pathRole", "pathStatus", "paths"})
@JsonSerialize(include = Inclusion.NON_NULL)
@JsonPropertyOrder(value = {"pathLayer", "pathRole", "pathStatus", "paths"})
public class PathList {

    @XmlElement(name = "path-layer")
    @JsonProperty("path-layer")
    private String pathLayer;

    @XmlElement(name = "path-role")
    @JsonProperty("path-role")
    private String pathRole;

    @XmlElement(name = "path-status")
    @JsonProperty("path-status")
    private String pathStatus;

    @XmlElementWrapper(name = "paths")
    @XmlElement(name = "path")
    @JsonProperty("paths")
    private List<Path> paths;

    public String getPathLayer() {
        return pathLayer;
    }

    public void setPathLayer(String pathLayer) {
        this.pathLayer = pathLayer;
    }

    public String getPathRole() {
        return pathRole;
    }

    public void setPathRole(String pathRole) {
        this.pathRole = pathRole;
    }

    public String getPathStatus() {
        return pathStatus;
    }

    public void setPathStatus(String pathStatus) {
        this.pathStatus = pathStatus;
    }

    public List<Path> getPaths() {
        return paths;
    }

    public void setPaths(List<Path> paths) {
        this.paths = paths;
    }

}
