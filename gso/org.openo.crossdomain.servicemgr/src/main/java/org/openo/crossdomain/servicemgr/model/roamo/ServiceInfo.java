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
package org.openo.crossdomain.servicemgr.model.roamo;

import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;

import org.openo.crossdomain.commsvc.validator.rules.StrRule;

/**
 * Service definition.<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
public class ServiceInfo {

    @StrRule(range = "1-22", paramName = "service_id")
    private String service_id;

    @StrRule(range = "1-255", paramName = "name")
    private String name;

    @StrRule(range = "1-255", paramName = "type")
    private String type;

    /**
     * Project ID
     */
    @StrRule(range = "1-255", paramName = "project_id")
    private String project_id;

    @StrRule(range = "1-36", paramName = "service_definition_id")
    private String service_definition_id;

    @StrRule(range = "1-36", paramName = "template_id")
    private String template_id;

    @JsonIgnore
    private String template_url;

    @StrRule(range = "1-255", paramName = "description")
    private String description;

    private Map<String, Object> parameters;

    private Object nsd_script;

    public ServiceInfo() {
        super();
    }

    public ServiceInfo(String serviceId, String name, String desctiption, String type, String templateID,
            String serviceDefinetionID) {
        this.service_id = serviceId;
        this.name = name;
        this.description = desctiption;
        this.type = type;
        this.template_id = templateID;
        this.service_definition_id = serviceDefinetionID;
    }

    /**
     * @return Returns the service_id.
     */
    public String getService_id() {
        return service_id;
    }

    /**
     * @param service_id The service_id to set.
     */
    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Returns the project_id.
     */
    public String getProject_id() {
        return project_id;
    }

    /**
     * @param project_id The project_id to set.
     */
    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    /**
     * @return Returns the service_definition_id.
     */
    public String getService_definition_id() {
        return service_definition_id;
    }

    /**
     * @param service_definition_id The service_definition_id to set.
     */
    public void setService_definition_id(String service_definition_id) {
        this.service_definition_id = service_definition_id;
    }

    /**
     * @return Returns the template_id.
     */
    public String getTemplate_id() {
        return template_id;
    }

    /**
     * @param template_id The template_id to set.
     */
    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
    }

    /**
     * @return Returns the description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return Returns the parameters.
     */
    public Map<String, Object> getParameters() {
        return parameters;
    }

    /**
     * @param parameters The parameters to set.
     */
    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    /**
     * @return Returns the type.
     */
    public String getType() {
        return type;
    }

    /**
     * @param type The type to set.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return Returns the template_url.
     */
    public String getTemplate_url() {
        return template_url;
    }

    /**
     * @param template_url The template_url to set.
     */
    public void setTemplate_url(String template_url) {
        this.template_url = template_url;
    }

    /**
     * @return Returns the nsd_script.
     */
    public Object getNsd_script() {
        return nsd_script;
    }

    /**
     * @param nsd_script The nsd_script to set.
     */
    public void setNsd_script(Object nsd_script) {
        this.nsd_script = nsd_script;
    }
}
