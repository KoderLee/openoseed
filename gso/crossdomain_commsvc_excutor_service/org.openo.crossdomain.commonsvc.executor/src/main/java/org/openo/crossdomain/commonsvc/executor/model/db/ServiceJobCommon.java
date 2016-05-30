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
package org.openo.crossdomain.commonsvc.executor.model.db;

import org.openo.commonservice.biz.trail.AuditItem;
import org.openo.commonservice.log.OssLog;
import org.openo.commonservice.log.OssLogFactory;

import org.codehaus.jackson.map.ObjectMapper;
import org.openo.crossdomain.commonsvc.executor.common.check.CheckAttr;
import org.openo.crossdomain.commonsvc.executor.common.check.CheckType;
import org.openo.crossdomain.commonsvc.executor.common.check.ModelChecker;
import org.openo.crossdomain.commonsvc.executor.common.constant.Constants;
import org.openo.crossdomain.commonsvc.executor.common.enums.ExecutionStatus;

import java.io.IOException;

public class ServiceJobCommon extends Result {

    private static final OssLog log = OssLogFactory.getLogger(ServiceJobCommon.class);

    @CheckAttr(type = CheckType.String, required = true, allowNull = true, min = 1, max = ModelChecker.UUID_MAX_LENGTH)
    protected String jobId;

    protected String tenantId = Constants.NULL_STR;

    protected ExecutionStatus status = ExecutionStatus.INITEXECUTE;

    protected String jobContent;

    protected long createdTime;

    protected long completedTime;

    protected String srvVersion = Constants.SERVICE_EXECUTOR_VERSION;

    protected String auditBasicInfo;

    public ServiceJobCommon() {
        super(null, null, null);
    }

    public String getJobId() {
        return jobId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public ExecutionStatus getStatus() {
        return status;
    }

    public void setStatus(ExecutionStatus status) {
        this.status = status;
    }

    /**
     * @return Returns the jobContent.
     */
    public String getJobContent() {
        return jobContent;
    }

    /**
     * @param jobContent The jobContent to set.
     */
    public void setJobContent(String jobContent) {
        this.jobContent = jobContent;
    }

    public void setSrvVersion(String srvVersion) {
        this.srvVersion = srvVersion;
    }

    /**
     * @return Returns the srvVersion.
     */
    public String getSrvVersion() {
        return srvVersion;
    }

    /**
     * @return Returns the createdTime.
     */
    public long getCreatedTime() {
        return createdTime;
    }

    /**
     * @param createdTime The createdTime to set.
     */
    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    /**
     * @return Returns the completedTime.
     */
    public long getCompletedTime() {
        return completedTime;
    }

    /**
     * @param completedTime The completedTime to set.
     */
    public void setCompletedTime(long completedTime) {
        this.completedTime = completedTime;
    }

    public String getAuditBasicInfo() {
        return auditBasicInfo;
    }

    public void setAuditBasicInfo(String auditBasicInfo) {
        this.auditBasicInfo = auditBasicInfo;
    }

    public void setAuditBasicInfoByAuditItem(AuditItem item) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            auditBasicInfo = mapper.writeValueAsString(item);
        } catch(IOException e) {
            log.error("setLogBasicInfoByAuditItem, writeValueAsString fail");
        }
    }

    public AuditItem getAuditItem() {
        if(auditBasicInfo == null) {
            return null;
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(auditBasicInfo, AuditItem.class);
        } catch(IOException e) {
            log.error("getAuditItem, readValue fail");
            return null;
        }
    }
}
