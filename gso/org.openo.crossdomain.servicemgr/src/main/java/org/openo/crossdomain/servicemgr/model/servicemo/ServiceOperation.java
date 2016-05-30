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

import org.openo.crossdomain.servicemgr.util.uuid.UUIDUtils;

/**
 * Definition for service operation.<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
public class ServiceOperation {

    private String operation_id;

    private String operation;

    private String tenant_id;

    private String service_id;

    private String user_id;

    private String operation_content;

    private String result;

    private String reason;

    private String progress;

    private String task_uri;

    private long operate_at;

    private long finished_at;

    /**
     * Constructor.<br/>
     *
     * @param serviceModel service data
     * @param userName name of user
     * @param operation service operation
     * @param progress service progress
     * @return service operation
     * @since crossdomain 0.5
     */
    public static ServiceOperation getServiceOperation(ServiceModel serviceModel, String userName, String operation,
            String progress) {
        return new ServiceOperation(serviceModel, userName, operation, progress);
    }

    /**
     * Constructor.<br/>
     * @param serviceModel service model
     * @param userName name of user
     * @param operation service operation
     * @param progress service progress
     */
    private ServiceOperation(ServiceModel serviceModel, String userName, String operation, String progress) {
        this.operation_id = UUIDUtils.createBase64Uuid();
        this.operation = operation;
        this.tenant_id = serviceModel.getTenant_id();
        this.service_id = serviceModel.getService_id();
        this.user_id = userName;
        this.progress = progress;
        this.operate_at = System.currentTimeMillis();
        this.finished_at = System.currentTimeMillis();
    }

    /**
     * Constructor.<br/>
     */
    public ServiceOperation() {
        super();
    }

    /**
     * Update service operations.<br/>
     *
     * @param operResult result of operation
     * @param reason advice of result
     * @param progress service progress
     * @since crossdomain 0.5
     */
    public void updateServiceOperation(String operResult, String reason, String progress) {
        this.result = operResult;
        this.reason = reason;
        this.progress = progress;
        this.finished_at = System.currentTimeMillis();
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

    /**
     * @return Returns the operation.
     */
    public String getOperation() {
        return operation;
    }

    /**
     * @param opertaion The operation to set.
     */
    public void setOperation(String operation) {
        this.operation = operation;
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
     * @return Returns the user_id.
     */
    public String getUser_id() {
        return user_id;
    }

    /**
     * @param user_id The user_id to set.
     */
    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    /**
     * @return Returns the operation_content.
     */
    public String getOperation_content() {
        return operation_content;
    }

    /**
     * @param operation_content The operation_content to set.
     */
    public void setOperation_content(String operation_content) {
        this.operation_content = operation_content;
    }

    /**
     * @return Returns the result.
     */
    public String getResult() {
        return result;
    }

    /**
     * @param result The result to set.
     */
    public void setResult(String result) {
        this.result = result;
    }

    /**
     * @return Returns the reason.
     */
    public String getReason() {
        return reason;
    }

    /**
     * @param reason The reason to set.
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * @return Returns the progress.
     */
    public String getProgress() {
        return progress;
    }

    /**
     * @param progress The progress to set.
     */
    public void setProgress(String progress) {
        this.progress = progress;
    }

    /**
     * @return Returns the task_uri.
     */
    public String getTask_uri() {
        return task_uri;
    }

    /**
     * @param task_uri The task_uri to set.
     */
    public void setTask_uri(String task_uri) {
        this.task_uri = task_uri;
    }

    /**
     * @return Returns the operate_at.
     */
    public long getOperate_at() {
        return operate_at;
    }

    /**
     * @param operate_at The operate_at to set.
     */
    public void setOperate_at(long operate_at) {
        this.operate_at = operate_at;
    }

    /**
     * @return Returns the finished_at.
     */
    public long getFinished_at() {
        return finished_at;
    }

    /**
     * @param finished_at The finished_at to set.
     */
    public void setFinished_at(long finished_at) {
        this.finished_at = finished_at;
    }
}
