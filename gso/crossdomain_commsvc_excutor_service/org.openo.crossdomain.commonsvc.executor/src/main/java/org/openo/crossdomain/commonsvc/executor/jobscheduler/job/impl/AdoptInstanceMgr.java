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
package org.openo.crossdomain.commonsvc.executor.jobscheduler.job.impl;

import org.openo.commonservice.log.OssLog;
import org.openo.commonservice.log.OssLogFactory;

import org.openo.crossdomain.commonsvc.executor.common.constant.Constants;
import org.openo.crossdomain.commonsvc.executor.common.enums.ExecutionStatus;
import org.openo.crossdomain.commonsvc.executor.common.redis.DeadInstanceOper;
import org.openo.crossdomain.commonsvc.executor.common.redis.JobIdOper;
import org.openo.crossdomain.commonsvc.executor.common.redis.RedisJobMapProxy;
import org.openo.crossdomain.commonsvc.executor.common.redis.RedisMap;
import org.openo.crossdomain.commonsvc.executor.common.util.ExecutorContextHelper;
import org.openo.crossdomain.commonsvc.executor.model.ServiceJob;
import org.openo.crossdomain.commonsvc.executor.service.Restorer;
import org.springframework.util.StringUtils;

import java.util.Set;

public class AdoptInstanceMgr {

    private final OssLog logger = OssLogFactory.getLogger(AdoptInstanceMgr.class);

    private Restorer restorer;

	/**
	 *adopt job Instance
	 *@param deadInstanceId instance id
     *@param stopTime time to stop
	 *@since crossdomain 0.5 2016-3-18
	 */
    public void adoptInstance(String deadInstantId, long stopTime) {
        DeadInstanceOper deadInstanceOper =
                (DeadInstanceOper)ExecutorContextHelper.getInstance().getBean(Constants.SpringDefine.DEADINSTANCEOPER);

        if(!deadInstanceOper.adoptInstance(deadInstantId)) {
            return;
        }

        JobIdOper jobIdOper = new JobIdOper(deadInstantId);
        Set<String> tenantIdSet = jobIdOper.getTenantId();

        for(String tenantId : tenantIdSet) {
            RedisMap<ServiceJob> deadJobMap = RedisJobMapProxy.getInstance().getRedisOper(tenantId);

            Set<String> jobIdSet = jobIdOper.getJobIdByTenantId(tenantId);
            for(String jobId : jobIdSet) {
                ServiceJob job = deadJobMap.get(ServiceJob.class, jobId);

                if(!deadInstantId.equals(job.getInstanceId())) {
                    continue;
                }

                if(job.getCreatedTime() > stopTime) {
                    continue;
                }

                deadJobMap.remove(jobId);

                if(!StringUtils.hasLength(job.getJobId()) || job.getStatus() == ExecutionStatus.COMPLETED) {
                    continue;
                }

                if(restorer == null) {
                    restorer = (Restorer)ExecutorContextHelper.getInstance().getBean(Constants.SpringDefine.RESTORER);
                }
                restorer.restoreJob(job);
            }
        }

        deadInstanceOper.deleteDeadInstance(deadInstantId);
    }
}
