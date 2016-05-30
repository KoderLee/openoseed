/*******************************************************************************
 * Copyright (c) 2016, Huawei Technologies Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package org.openo.crossdomain.servicemgr.model.registermo;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Component class.<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
public class Component {

    @JsonProperty("component_id")
    private String componentId;

    @JsonProperty("component_version")
    private String compomentVersion;

    private String url;

    private List<String> action = new ArrayList<String>();

    /**
     * Construct.<br/>
     * @param componentId component ID
     * @param compomentVersion component version
     * @param url url
     * @param action operation
     */
    public Component(String componentId, String compomentVersion, String url, List<String> action) {
        super();
        this.componentId = componentId;
        this.compomentVersion = compomentVersion;
        this.url = url;
        this.action = action;
    }

    /**
     * Constructor
     */
    public Component() {
        super();
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public String getCompomentVersion() {
        return compomentVersion;
    }

    public void setCompomentVersion(String compomentVersion) {
        this.compomentVersion = compomentVersion;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getAction() {
        return action;
    }

    public void setAction(List<String> action) {
        this.action = action;
    }

}
