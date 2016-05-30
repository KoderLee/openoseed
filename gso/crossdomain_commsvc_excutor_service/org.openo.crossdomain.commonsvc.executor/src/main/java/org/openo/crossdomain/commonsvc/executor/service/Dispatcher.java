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
package org.openo.crossdomain.commonsvc.executor.service;

import org.openo.commonservice.log.OssLog;
import org.openo.commonservice.log.OssLogFactory;

import org.openo.crossdomain.commonsvc.executor.common.LoopRunnable;
import org.openo.crossdomain.commonsvc.executor.common.constant.Constants;
import org.openo.crossdomain.commonsvc.executor.common.enums.ActionType;
import org.openo.crossdomain.commonsvc.executor.common.redis.RedisJobMapProxy;
import org.openo.crossdomain.commonsvc.executor.common.util.*;
import org.openo.crossdomain.commonsvc.executor.model.Resource;
import org.openo.crossdomain.commonsvc.executor.model.ServiceJob;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.StringUtils;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class Dispatcher {

    private static final OssLog log = OssLogFactory.getLogger(ExecuteServiceController.class);

    private static Dispatcher instance = new Dispatcher();

    @javax.annotation.Resource
    private ThreadPoolTaskExecutor threadPool;

    private ListBuffer<AbstractMap.SimpleEntry<String, String>> jobIdBuffer = new ListBuffer<>();

    /**
     * Job Map
     */
    private RedisJobMapProxy redisProxy = RedisJobMapProxy.getInstance();

    private LoopRunnable polling = new PollingRunnable();

    private ExecutorService pool;

    private Dispatcher() {
        Object threadPoolObj = ExecutorContextHelper.getInstance().getBean("threadPool");
        if(threadPoolObj != null) {
            threadPool = (ThreadPoolTaskExecutor)threadPoolObj;
        }
    }

    public static Dispatcher getInstance() {
        return instance;
    }

	/**
	 *Start The Dispatcher Thread
	 *@since crossdomain 0.5 2016-3-18
	 */
    public void startPollingThread() {
        pool = ThreadUtil.startThread(polling);
    }

	/**
	 *Stop The Dispatcher Thread
	 *@since crossdomain 0.5 2016-3-18
	 */
    public void stopPollingThread() {
        pool.shutdown();
    }

	
	
	/**
	 *Execute the ServiceJob
	 *@param svcJob Service Job to be executed
	 *@since crossdomain 0.5 2016-3-18
	 */
    public void executeJob(ServiceJob svcJob) {
        try {
            jobIdBuffer.deposit(new AbstractMap.SimpleEntry(svcJob.getTenantId(), svcJob.getJobId()));
        } catch(InterruptedException e) {
            log.error("jobIdBuffer.deposit fail:{}", svcJob.getJobId());
        }
    }

	/**
	 *Close The Thread Pool
	 *@since crossdomain 0.5 2016-3-18
	 */
    public void closePool() {
        threadPool.destroy();
    }

    private class PollingRunnable extends LoopRunnable {

        private long checkTime = CommonUtil.getTimeInMillis() + Constants.FIVE_MINUTES_AS_MILLIS;

        @Override
        protected void loopRun() {
            try {

                long jobNum = jobIdBuffer.size();
                if(checkTime < CommonUtil.getTimeInMillis() && jobNum > 1000) {
                    checkTime = CommonUtil.getTimeInMillis() + Constants.FIVE_MINUTES_AS_MILLIS;
                    log.error("Job to dispatch: {}", jobNum);
                }

                AbstractMap.SimpleEntry<String, String> entry = jobIdBuffer.fetch();

                ServiceJob svcJob = redisProxy.getRedisOper(entry.getKey()).get(ServiceJob.class, entry.getValue());
                if(svcJob == null || !StringUtils.hasLength(svcJob.getJobId())) {
                    log.error("Job don't exist:{}, {}", entry.getKey(), entry.getValue());
                    return;
                }

                executeJob(svcJob);
            } catch(InterruptedException e) {
                log.error("jobIdBuffer.fetch fail");

                ThreadUtil.sleep(5000);
            }
        }

        public void executeJob(ServiceJob svcJob) {
            List<Resource> resourceList = new ArrayList<>();
            if(svcJob.getService().getAction() != ActionType.DELETE) {
                resourceList.addAll(JobUtil.getInstance().getExecutableResources(svcJob));
            } else {
                resourceList.addAll(JobUtil.getInstance().getNotDependedResources(svcJob));
            }

            for(Resource resource : resourceList) {
                threadPool.execute(new Action(resource, svcJob.getService()));
            }
        }
    }
}
