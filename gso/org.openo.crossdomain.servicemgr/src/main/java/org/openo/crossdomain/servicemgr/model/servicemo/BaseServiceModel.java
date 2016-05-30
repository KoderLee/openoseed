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

import org.openo.crossdomain.commsvc.validator.rules.StrEnumRule;
import org.openo.crossdomain.commsvc.validator.rules.StrRule;

/**
 * Definition for basic information of service model.<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
public class BaseServiceModel {

    @StrRule(range = "1-22", paramName = "service_id")
    private String service_id;

    @StrRule(range = "1-255", paramName = "tenant_id")
    private String tenant_id;

    /**
     * Project ID
     */
    @StrRule(range = "1-255", paramName = "project_id")
    private String project_id;

    @StrRule(range = "1-36", paramName = "service_definition_id")
    private String service_definition_id;

    @StrRule(range = "1-255", paramName = "service_type")
    private String service_type;

    @StrRule(range = "1-36", paramName = "template_id")
    private String template_id;

    @StrRule(range = "1-255", paramName = "name")
    private String name;

    @StrRule(range = "1-255", paramName = "description")
    private String description;

    private Long created_at;

    private Long update_time;

    @StrEnumRule(range = "Active,Deactive,ActivateInProgress,DeactivateInProgress", paramName = "active_status")
    private String active_status;

    @StrEnumRule(range = "CreateInProgress,UpdateInProgress,DeleteInProgress,ActivateInProgress,DeactivateInProgress,Completed,Activate,Deactivate,Failed,CreateFailed", paramName = "status")
    private String status;

    @StrRule(range = "1-22", paramName = "description")
    private String operation_id;

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
     * @return Returns the tenant_id.
     */
    public String getTenant_id() {
        return tenant_id;
    }

    /**
     * @param tenant_id The tenant_id to set.
     */
    public void setTenant_id(String tenant_id) {
        this.tenant_id = tenant_id;
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
     * @return Returns the service_type.
     */
    public String getService_type() {
        return service_type;
    }

    /**
     * @param service_type The service_type to set.
     */
    public void setService_type(String service_type) {
        this.service_type = service_type;
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

    /**
     * @return Returns the update_time.
     */
    public Long getUpdate_time() {
        return update_time;
    }

    /**
     * @param update_time The update_time to set.
     */
    public void setUpdate_time(Long update_time) {
        this.update_time = update_time;
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
     * @return Returns the operation_id.
     */
    public String getOperation_id() {
        return operation_id;
    }

    /**
     * @param operation_id The operation_id to set.
     */
    public void setOperation_id(String operation_id) {
        this.operation_id = operation_id;
    }

    public void updateBaseServiceModel(BaseServiceModel baseServiceModel) {
        this.tenant_id = baseServiceModel.getTenant_id();
        this.service_id = baseServiceModel.getService_id();
        this.service_definition_id = baseServiceModel.getService_definition_id();
        this.template_id = baseServiceModel.getTemplate_id();
        this.update_time = System.currentTimeMillis();
        this.service_type = baseServiceModel.getService_type();
    }
}
