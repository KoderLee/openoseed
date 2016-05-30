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

/**
 * Response information for basic data of service model.<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
public class BaseServiceModelRspInfo {

    private String service_id;

    private String service_definition_id;

    private String template_id;

    private String name;

    private String description;

    private Long created_at;

    private String active_status;

    private String status;

    public BaseServiceModelRspInfo() {
        super();
    }

    public BaseServiceModelRspInfo(BaseServiceModelRspInfo baseServiceModelRspInfo) {
        super();
        this.service_id = baseServiceModelRspInfo.getService_id();
        this.service_definition_id = baseServiceModelRspInfo.getService_definition_id();
        this.template_id = baseServiceModelRspInfo.getTemplate_id();
        this.name = baseServiceModelRspInfo.getName();
        this.description = baseServiceModelRspInfo.getDescription();
        this.active_status = baseServiceModelRspInfo.getActive_status();
        this.status = baseServiceModelRspInfo.getStatus();
        this.created_at = baseServiceModelRspInfo.getCreated_at();

    }

    public BaseServiceModelRspInfo(String serviceID, String serviceDefinitionId, String templateID, String name,
            String description, String activeStatus, String status, long createAt) {
        this.service_id = serviceID;
        this.service_definition_id = serviceDefinitionId;
        this.template_id = templateID;
        this.name = name;
        this.description = description;
        this.active_status = activeStatus;
        this.status = status;
        this.created_at = createAt;
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
     * @return Returns the active_status.
     */
    public String getActive_status() {
        return active_status;
    }

    /**
     * @param active_status The active_status to set.
     */
    public void setActive_status(String active_status) {
        this.active_status = active_status;
    }

    /**
     * @return Returns the status.
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status The status to set.
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return Returns the created_at.
     */
    public Long getCreated_at() {
        return created_at;
    }

    /**
     * @param created_at The created_at to set.
     */
    public void setCreated_at(Long created_at) {
        this.created_at = created_at;
    }
}
