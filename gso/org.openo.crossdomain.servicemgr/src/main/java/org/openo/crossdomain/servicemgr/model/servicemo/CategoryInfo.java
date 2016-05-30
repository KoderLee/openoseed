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
package org.openo.crossdomain.servicemgr.model.servicemo;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Category structure.<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
public class CategoryInfo {

    @JsonProperty("id")
    private String categoryId;

    @JsonProperty("name")
    private String categoryName;

    private List<ServiceDefInfo> serviceDefInfos = new ArrayList<ServiceDefInfo>();

    /**
     * Constructor.<br/>
     */
    public CategoryInfo() {
        super();
    }

    /**
     * Constructor.<br/>
     * @param categoryId category ID
     * @param categoryName category name
     * @param serviceDefInfos the collection of serviceDefInfos object 
     */
    public CategoryInfo(String categoryId, String categoryName, List<ServiceDefInfo> serviceDefInfos) {
        super();
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.serviceDefInfos = serviceDefInfos;
    }

    /**
     * @return Returns the categoryId.
     */
    public String getCategoryId() {
        return categoryId;
    }

    /**
     * @param categoryId The categoryId to set.
     */
    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * @return Returns the categoryName.
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * @param categoryName The categoryName to set.
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    /**
     * @return Returns the serviceDefInfos.
     */
    public List<ServiceDefInfo> getServiceDefInfos() {
        return serviceDefInfos;
    }

    /**
     * @param serviceDefInfos The serviceDefInfos to set.
     */
    public void setServiceDefInfos(List<ServiceDefInfo> serviceDefInfos) {
        this.serviceDefInfos = serviceDefInfos;
    }
}
