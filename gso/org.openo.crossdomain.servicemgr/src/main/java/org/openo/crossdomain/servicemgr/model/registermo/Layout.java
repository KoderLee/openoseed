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

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * LayOut class.<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
public class Layout {

    @JsonProperty("view_id")
    private String viewId;

    @JsonProperty("view_version")
    private String viewVersion;

    @JsonProperty("component_id")
    private String componentId;

    @JsonProperty("component_version")
    private String componentVersion;

    @JsonProperty("layout_constraints")
    private Object layoutConstraints;

    public Layout() {
        super();
    }

    public Layout(String viewId, String viewVersion, String componentId, String componentVersion,
            Object layoutConstraints) {
        super();
        this.viewId = viewId;
        this.viewVersion = viewVersion;
        this.componentId = componentId;
        this.componentVersion = componentVersion;
        this.layoutConstraints = layoutConstraints;
    }

    public String getViewId() {
        return viewId;
    }

    public void setViewId(String viewId) {
        this.viewId = viewId;
    }

    public String getViewVersion() {
        return viewVersion;
    }

    public void setViewVersion(String viewVersion) {
        this.viewVersion = viewVersion;
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public String getComponentVersion() {
        return componentVersion;
    }

    public void setComponentVersion(String componentVersion) {
        this.componentVersion = componentVersion;
    }

    public Object getLayoutConstraints() {
        return layoutConstraints;
    }

    public void setLayoutConstraints(Object layoutConstraints) {
        this.layoutConstraints = layoutConstraints;
    }

}
