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
package org.openo.crossdomain.commonsvc.executor.service.asyn;

import org.openo.commonservice.log.OssLog;
import org.openo.commonservice.log.OssLogFactory;

import org.openo.crossdomain.commonsvc.executor.common.LoopRunnable;
import org.openo.crossdomain.commonsvc.executor.common.constant.Constants;
import org.openo.crossdomain.commonsvc.executor.common.enums.ExecutionStatus;
import org.openo.crossdomain.commonsvc.executor.common.redis.JobIdOper;
import org.openo.crossdomain.commonsvc.executor.common.redis.RedisJobMapProxy;
import org.openo.crossdomain.commonsvc.executor.common.util.ExecutorContextHelper;
import org.openo.crossdomain.commonsvc.executor.common.util.SchedulerProxy;
import org.openo.crossdomain.commonsvc.executor.common.util.ThreadUtil;
import org.openo.crossdomain.commonsvc.executor.model.Resource;
import org.openo.crossdomain.commonsvc.executor.model.ServiceInfo;
import org.openo.crossdomain.commonsvc.executor.model.ServiceJob;
import org.openo.crossdomain.commonsvc.executor.model.db.Result;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;

public class AsynClient {

    private static final OssLog log = OssLogFactory.getLogger(AsynClient.class);

    private static AsynClient instance = null;

    private long pollingTime;

    private LoopRunnable polling = new PollingRunnable();

    private ExecutorService pool;

    /**
     * Job Map
     */
    private RedisJobMapProxy redisProxy = RedisJobMapProxy.getInstance();

    private JobIdOper jobIdOper;

    private AsynClient() {
        setTime(3000L);

        SchedulerProxy schedulerProxy =
                (SchedulerProxy)ExecutorContextHelper.getInstance().getBean(Constants.SpringDefine.SCHEDULERPROXY);
        jobIdOper = new JobIdOper(schedulerProxy.getInstanceId());
    }

    public synchronized static AsynClient getInstance() {
        if(instance == null) {
            instance = new AsynClient();
        }
        return instance;
    }

    public void setTime(long pollingTime) {
        this.pollingTime = pollingTime;
    }

    public void startPollingThread() {
        pool = ThreadUtil.startThread(polling);
    }

    public void stopPollingThread() {
        pool.shutdown();
    }

    private class PollingRunnable extends LoopRunnable {

        @Override
        protected void loopRun() {
            ThreadUtil.sleep(pollingTime);

            Set<String> tenantSet = jobIdOper.getTenantId();

            for(String tenantId : tenantSet) {
                Set<String> jobIdSet = jobIdOper.getJobIdByTenantId(tenantId);

                for(String jobId : jobIdSet) {
                    do {
                    } while(pollingServiceJob(tenantId, jobId));
                }
            }

        }

        private boolean pollingServiceJob(String tenantId, String jobId) {
            boolean isContinue = false;

            ServiceJob job = redisProxy.getRedisOper(tenantId).get(ServiceJob.class, jobId);

            if(job == null) {
                return isContinue;
            }

            if(job.getStatus() != ExecutionStatus.EXECUTING) {
                return isContinue;
            }

            for(Map.Entry<String, Resource> entry : job.getService().getResources().entrySet()) {
                if(entry.getValue().getStatus() != ExecutionStatus.EXECUTING) {
                    continue;
                }

                if(!StringUtils.hasLength(entry.getValue().getQueryUrl())) {
                    continue;
                }

                if(pollingResource(job.getService(), entry.getValue())) {
                    isContinue = true;
                }
            }

            return isContinue;
        }

        private boolean pollingResource(ServiceInfo service, Resource resource) {
            ServiceInfo baseService = new ServiceInfo();
            baseService.setJobId(service.getJobId());
            baseService.setServiceName(service.getServiceName());
            baseService.setAction(service.getAction());

            Map<String, Resource> resourceMap = new HashMap<>();
            resourceMap.put(resource.getKey(), resource);
            baseService.setResources(resourceMap);

            boolean ret = false;
            GetExecuteResult getResult = new GetExecuteResult(resource.getQueryUrl(), baseService);
            Result result = getResult.process();
            if(result.isSuccess() && Constants.GETRESULT.equals(result.getOperObject())) {
                ret = true;
            }

            return ret;
        }
    }
}
