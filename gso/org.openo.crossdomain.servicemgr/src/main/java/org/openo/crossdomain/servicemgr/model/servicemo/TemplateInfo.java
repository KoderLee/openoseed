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

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Definition for template.<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
public class TemplateInfo {

    @JsonProperty("id")
    private String templateId;

    @JsonProperty("name")
    private String templateName;

    @JsonProperty("url")
    private String templateUrl;

    /**
     * Constructor.<br/>
     * @param templateId template ID
     * @param templateName template name
     * @param templateUrl tempalte url
     */
    public TemplateInfo(String templateId, String templateName, String templateUrl) {
        super();
        this.templateId = templateId;
        this.templateName = templateName;
        this.templateUrl = templateUrl;
    }

    /**
     * Constructor.<br/>
     */
    public TemplateInfo() {
        super();
    }

    /**
     * @return Returns the templateId.
     */
    public String getTemplateId() {
        return templateId;
    }

    /**
     * @param templateId The templateId to set.
     */
    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    /**
     * @return Returns the templateName.
     */
    public String getTemplateName() {
        return templateName;
    }

    /**
     * @param templateName The templateName to set.
     */
    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    /**
     * @return Returns the templateUrl.
     */
    public String getTemplateUrl() {
        return templateUrl;
    }

    /**
     * @param templateUrl The templateUrl to set.
     */
    public void setTemplateUrl(String templateUrl) {
        this.templateUrl = templateUrl;
    }
}
