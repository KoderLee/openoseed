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
package org.openo.crossdomain.commonsvc.executor.common.redis;

import java.util.Set;

import org.openo.crossdomain.commonsvc.executor.common.constant.Constants;

public class JobIdOper {

    private String instanceId;

    private RedisSet<String> tenantIdSet;

    private RedisSet<String> jobIdSet;

    public JobIdOper(String instanceId) {
        this.instanceId = instanceId;
    }

    /**
	 *Add Job Id to Set
	 *@param tenantId Tenant Id
	 *@param jobId id of ServiceJob
	 *@since crossdomain 0.5 2016-3-18
	 */
    public void addJobId(String tenantId, String jobId) {
        getTenantIdSet().addMember(tenantId);
        getJobIdSet(tenantId).addMember(jobId);
    }

	/**
	 *Delete Job Id to Set
	 *@param tenantId Tenant Id
	 *@param jobId id of ServiceJob
	 *@since crossdomain 0.5 2016-3-18
	 */
    public void deleteJobId(String tenantId, String jobId) {
        getJobIdSet(tenantId).deleteMember(jobId);
        if(!getJobIdSet(tenantId).exists()) {
            getTenantIdSet().deleteMember(tenantId);
        }
    }

	/**
	 *Get Job Id Set By TenantId
	 *@param tenantId Tenant Id
	 *@return jobId Set of ServiceJob
	 *@since crossdomain 0.5 2016-3-18
	 */
    public Set<String> getJobIdByTenantId(String tenantId) {
        return getJobIdSet(tenantId).getMember();
    }

    /**
	 *Get TenantId
	 *@since crossdomain 0.5 2016-3-18
	 */
    public Set<String> getTenantId() {
        return getTenantIdSet().getMember();
    }

    private RedisSet<String> getTenantIdSet() {
        if(tenantIdSet == null) {
            tenantIdSet = new RedisSet<String>(Constants.TENANT_ID_SET_RD, instanceId);
        }

        return tenantIdSet;
    }

    private RedisSet<String> getJobIdSet(String tenantId) {
        if(jobIdSet == null) {
            String preName = tenantId + Constants.SEPARATOR + instanceId;
            jobIdSet = new RedisSet<String>(Constants.JOB_ID_SET_RD, preName);
        }
        return jobIdSet;
    }
}
