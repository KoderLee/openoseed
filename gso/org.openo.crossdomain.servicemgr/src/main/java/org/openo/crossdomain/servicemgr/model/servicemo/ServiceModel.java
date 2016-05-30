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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.openo.crossdomain.servicemgr.constant.Constant;
import org.openo.crossdomain.servicemgr.model.roamo.BaseServiceModelRspInfo;
import org.openo.crossdomain.servicemgr.model.roamo.ServiceInfo;
import org.openo.crossdomain.servicemgr.model.roamo.ServiceModelInfo;
import org.openo.crossdomain.servicemgr.model.roamo.ServiceModelRspInfo;
import org.openo.crossdomain.servicemgr.util.uuid.UUIDUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.crossdomain.commsvc.common.util.httpclientutil.HttpContextUtil;
import org.openo.crossdomain.commsvc.common.util.jsonutil.JsonUtil;

/**
 * Definitipn of servivce.<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
public class ServiceModel extends BaseServiceModel {

    private List<ServiceParameter> parameters;

    private String templateUrl;

    @JsonIgnore
    private String auditOperLogSn;

    @JsonIgnore
    private String auditUserName;

    @JsonIgnore
    private String auditUserID;

    @JsonIgnore
    private String auditTerminal;

    @JsonIgnore
    private boolean isSend = false;

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

    /**
     * Constructor.<br/>
     */
    public ServiceModel() {
        super();
    }

    /**
     * Constructor.<br/>
     * @param tenantID tenant ID
     * @param serviceID service ID
     * @param status service status
     */
    public ServiceModel(String tenantID, String serviceID, String status) {
        this.setTenant_id(tenantID);
        this.setService_id(serviceID);
        this.setStatus(status);
    }

    /**
     * Constructor.<br/>
     * @param id service ID
     * @param description service description
     */
    public ServiceModel(String id, String description) {
        super();
        this.setService_id(id);
        this.setDescription(description);
    }

    /**
     * Convert model.<br/>
     *
     * @param serviceModelInfo
     * @param service_id
     * @return
     * @since crossdomain 0.5
     */
    public static ServiceModel toServiceModel(ServiceModelInfo serviceModelInfo, String service_id) {
        if(null == serviceModelInfo || null == serviceModelInfo.getService()) {
            return null;
        }

        ServiceModel serviceModel = new ServiceModel();
        serviceModel.setService_id(service_id);

        ServiceInfo serviceInfo = serviceModelInfo.getService();
        if(!StringUtils.hasLength(serviceModel.getService_id())) {
            serviceModel.setService_id(UUIDUtils.createBase64Uuid());
        }

        String name = serviceInfo.getName();
        if(null != name) {
            serviceModel.setName(name);
        }

        String description = serviceInfo.getDescription();
        if(null != description) {
            serviceModel.setDescription(description);
        }

        String service_definition_id = serviceInfo.getService_definition_id();
        if(null != service_definition_id) {
            serviceModel.setService_definition_id(service_definition_id);
        }

        String template_id = serviceInfo.getTemplate_id();
        if(null != template_id) {
            serviceModel.setTemplate_id(template_id);
        }

        return serviceModel;
    }

    /**
     * Get the value of parameter.<br/>
     *
     * @param svcParam service parameter
     * @return the value of parameter
     * @throws ServiceException
     * @since crossdomain 0.5
     */
    public static Object getServiceParameterValue(ServiceParameter svcParam) throws ServiceException {
        if(ServiceParameter.PARAM_TYPE_NUM.PARAM_TYPE_RESLIST.equals(svcParam.getParameter_type())) {

            List<ServiceListParameter> serviceListParameters = svcParam.getParameterGroup();
            if(serviceListParameters.isEmpty()) {
                return null;
            }

            List<Object> svcListParamValues = new ArrayList<Object>();
            for(ServiceListParameter svcListParam : serviceListParameters) {
                svcListParamValues.add(JsonUtil.unMarshal(svcListParam.getParameter_value(), Map.class));
            }

            return svcListParamValues;
        }

        if(ServiceParameter.PARAM_TYPE_NUM.PARAM_TYPE_LIST.equals(svcParam.getParameter_type())) {

            List<ServiceListParameter> serviceListParameters = svcParam.getParameterGroup();
            if(serviceListParameters.isEmpty()) {
                return null;
            }

            ServiceListParameter svcListParam = serviceListParameters.iterator().next();

            return JsonUtil.unMarshal(svcListParam.getParameter_value(), List.class);
        }

        return svcParam.getParameter_value();
    }

    /**
     * Construct the serviceInfo object<br/>
     *
     * @return oject of serviceInfo
     * @since crossdomain 0.5
     */
    public ServiceInfo toServiceInfo() {

        return new ServiceInfo(this.getService_id(), this.getName(), this.getDescription(), this.getService_type(),
                this.getTemplate_id(), this.getService_definition_id());

    }

    /**
     * Construct the response of basic service model.<br/>
     *
     * @return the response of basic service model
     * @since crossdomain 0.5
     */
    public BaseServiceModelRspInfo toBaseServiceModelRspInfo() {
        return new BaseServiceModelRspInfo(this.getService_id(), this.getService_definition_id(),
                this.getTemplate_id(), this.getName(), this.getDescription(), this.getActive_status(),
                this.getStatus(), this.getCreated_at());
    }

    /**
     * Construct the response of service model.<br/>
     *
     * @return the response of service model
     * @throws ServiceException
     * @since crossdomain 0.5
     */
    public ServiceModelRspInfo toServiceModelRspInfo() throws ServiceException {

        Map<String, Object> parameters = new HashMap<String, Object>();

        List<ServiceParameter> svcParams = this.getParameters();
        if(!CollectionUtils.isEmpty(svcParams)) {
            for(ServiceParameter svcParam : svcParams) {
                Object svcListParamValue = ServiceModel.getServiceParameterValue(svcParam);
                parameters.put(svcParam.getParameter_name(), svcListParamValue);
            }
        }

        return new ServiceModelRspInfo(this.toBaseServiceModelRspInfo(), parameters);
    }

    /**
     * Update service model.<br/>
     *
     * @param serviceModel service model
     * @since crossdomain 0.5
     */
    public void updateServiceModel(ServiceModel serviceModel) {
        super.updateBaseServiceModel(serviceModel);
    }

    /**
     * Judge if operation is in progress.<br/>
     *
     * @return true when status is IN_PROGRESS_LABLE
     * @since crossdomain 0.5
     */
    @JsonIgnore
    public boolean isInprogress() {
        return (null != this.getStatus() && this.getStatus().contains(STATUS_NUM.IN_PROGRESS_LABLE));
    }

    /**
     * Judge if operation is activating.<br/>
     *
     * @return true when status is ACTIVE_STATUS
     * @since crossdomain 0.5
     */
    @JsonIgnore
    public boolean isActive() {
        return (null != this.getActive_status()) && this.getActive_status().equals(ACTIVE_STATUS_NUM.ACTIVE_STATUS);
    }

    /**
     * Judge if operation is deactivating.<br/>
     *
     * @return true when status is DEACTIVE_STATUS
     * @since crossdomain 0.5
     */
    @JsonIgnore
    public boolean isDeactive() {
        return (null != this.getActive_status()) && this.getActive_status().equals(ACTIVE_STATUS_NUM.DEACTIVE_STATUS);
    }

    /**
     * Judge if failing to create.<br/>
     *
     * @return true when status is CREATE_FAILED
     * @since crossdomain 0.5
     */
    @JsonIgnore
    public boolean isCreateFailed() {
        return (null != this.getStatus() && this.getStatus().equals(STATUS_NUM.CREATE_FAILED));
    }

    /**
     * @return Returns the parameters.
     */
    public List<ServiceParameter> getParameters() {
        return parameters;
    }

    /**
     * @param parameters The parameters to set.
     */
    public void setParameters(List<ServiceParameter> parameters) {
        this.parameters = parameters;
    }

    /**
     * @return Returns the auditOperLogSn.
     */
    public String getAuditOperLogSn() {
        return auditOperLogSn;
    }

    /**
     * @param auditOperLogSn The auditOperLogSn to set.
     */
    public void setAuditOperLogSn(String auditOperLogSn) {
        this.auditOperLogSn = auditOperLogSn;
    }

    /**
     * @return Returns the isSend.
     */
    public boolean isSend() {
        return isSend;
    }

    /**
     * @param isSend The isSend to set.
     */
    public void setSend(boolean isSend) {
        this.isSend = isSend;
    }

    /**
     * @return Returns the auditUserName.
     */
    public String getAuditUserName() {
        return auditUserName;
    }

    /**
     * @param auditUserName The auditUserName to set.
     */
    public void setAuditUserName(String auditUserName) {
        this.auditUserName = auditUserName;
    }

    /**
     * @return Returns the auditUserID.
     */
    public String getAuditUserID() {
        return auditUserID;
    }

    /**
     * @param auditUserID The auditUserID to set.
     */
    public void setAuditUserID(String auditUserID) {
        this.auditUserID = auditUserID;
    }

    /**
     * @return Returns the auditTerminal.
     */
    public String getAuditTerminal() {
        return auditTerminal;
    }

    /**
     * @param auditTerminal The auditTerminal to set.
     */
    public void setAuditTerminal(String auditTerminal) {
        this.auditTerminal = auditTerminal;
    }

    public void setUser(HttpServletRequest httpRequest) {
        this.auditUserID = httpRequest.getHeader(Constant.USER_ID);
        this.auditUserName = httpRequest.getHeader(Constant.USER_NAME);
        this.auditTerminal = HttpContextUtil.getLoginAddress(httpRequest);
    }

    public static class STATUS_NUM {

        public static final String CREATE_IN_PROGRESS = "CreateInProgress";

        public static final String UPDATE_IN_PROGRESS = "UpdateInProgress";

        public static final String DELETE_IN_PROGRESS = "DeleteInProgress";

        public static final String ACTIVATE_IN_PROGRESS = "ActivateInProgress";

        public static final String DEACTIVATE_IN_PROGRESS = "DeactivateInProgress";

        public static final String COMPLETED = "Completed";

        public static final String ACTIVATE = "Activate";

        public static final String DEACTIVATE = "Deactivate";

        public static final String FAILED = "Failed";

        public static final String CREATE_FAILED = "CreateFailed";

        public static final String IN_PROGRESS_LABLE = "InProgress";
    }

    public static class ACTIVE_STATUS_NUM {

        public static final String ACTIVE_STATUS = "Active";

        public static final String DEACTIVE_STATUS = "Deactive";

        public static final String ACTIVATE_IN_PROGRESS = "ActivateInProgress";

        public static final String DEACTIVATE_IN_PROGRESS = "DeactivateInProgress";
    }

    @Override
    public String toString() {
        return "ServiceModel [service_id=" + this.getService_id() + ", tenant_id=" + this.getTenant_id()
                + ", project_id=" + this.getProject_id() + ", service_definition_id=" + this.getService_definition_id()
                + ", service_type=" + this.getService_type() + ", template_id=" + this.getTemplate_id() + ", name="
                + this.getName() + ", description=" + this.getDescription() + ", created_at=" + this.getCreated_at()
                + ", update_time=" + this.getUpdate_time() + ", active_status=" + this.getActive_status() + ", status="
                + this.getStatus() + ", operation_id=" + this.getOperation_id() + ", parameters=" + parameters + "]";
    }

}
