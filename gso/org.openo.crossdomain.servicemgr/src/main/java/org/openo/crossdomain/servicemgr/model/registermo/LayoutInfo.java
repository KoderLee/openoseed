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
 * Position class.<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
public class LayoutInfo {

    private String gridx;

    private String gridy;

    @JsonProperty("grid_width")
    private String gridWidth;

    @JsonProperty("grid_height")
    private String gridHeight;

    @JsonProperty("grid_weightx")
    private String gridWeightx;

    @JsonProperty("grid_weighty")
    private String gridWeighty;

    public LayoutInfo() {
        super();
    }

    public LayoutInfo(String gridx, String gridy, String gridWidth, String gridHeight, String gridWeightx,
            String gridWeighty) {
        super();
        this.gridx = gridx;
        this.gridy = gridy;
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        this.gridWeightx = gridWeightx;
        this.gridWeighty = gridWeighty;
    }

    public String getGridx() {
        return gridx;
    }

    public void setGridx(String gridx) {
        this.gridx = gridx;
    }

    public String getGridy() {
        return gridy;
    }

    public void setGridy(String gridy) {
        this.gridy = gridy;
    }

    public String getGridWidth() {
        return gridWidth;
    }

    public void setGridWidth(String gridWidth) {
        this.gridWidth = gridWidth;
    }

    public String getGridHeight() {
        return gridHeight;
    }

    public void setGridHeight(String gridHeight) {
        this.gridHeight = gridHeight;
    }

    public String getGridWeightx() {
        return gridWeightx;
    }

    public void setGridWeightx(String gridWeightx) {
        this.gridWeightx = gridWeightx;
    }

    public String getGridWeighty() {
        return gridWeighty;
    }

    public void setGridWeighty(String gridWeighty) {
        this.gridWeighty = gridWeighty;
    }

}
