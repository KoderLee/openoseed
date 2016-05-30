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

import org.openo.crossdomain.commonsvc.executor.common.redis.RedisQueue;
import org.openo.crossdomain.commonsvc.executor.common.util.CommonUtil;
import org.openo.crossdomain.commonsvc.executor.common.util.JobUtil;
import org.openo.crossdomain.commonsvc.executor.common.util.SchedulerProxy;
import org.openo.crossdomain.commonsvc.executor.dao.inf.IServiceJobDao;
import org.openo.crossdomain.commonsvc.executor.model.ServiceJob;
import org.openo.crossdomain.commonsvc.executor.model.db.Result;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import java.util.List;

@Component(value = "receiver")
public class Receiver {

    private final OssLog log = OssLogFactory.getLogger(Receiver.class);

    private RedisQueue<ServiceJob> jobQueue = new RedisQueue<ServiceJob>("waitingJob", 1000);

    @Resource
    private IServiceJobDao srvExectuorJobDao;

    @Resource
    private SchedulerProxy schedulerProxy;

		
    /**
     * Insert Service Job Into DataBase<br/>
	 * @param job ServiceJob to be processed
     * @return result of the operation
     * @version crossdomain 0.5 2016-3-18
     */
    public synchronized Result insertJobToDb(ServiceJob job) {
        Result result = srvExectuorJobDao.insert(job);
        if((result != null) && !result.isSuccess()) {
            log.error("srvExectuorJobDao.insert fail: {}", result.getErrorCode());
        }
        return result;
    }

		
    /**
     * Push the Service Job to DataBase and Redis<br/>
	 * @param job ServiceJob to be processed
     * @return result of the operation
     * @version crossdomain 0.5 2016-3-18
     */
    public Result pushJob(ServiceJob job) {
        return pushJob(job, false);
    }

    /**
     * Push the Service Job to DataBase and Redis<br/>
	 * @param job ServiceJob to be processed
	 * @param force whether to push the job by force
     * @return result of the operation
     * @version crossdomain 0.5 2016-3-18
     */	
    public Result pushJobOnly(ServiceJob job, boolean force) {
        jobQueue.push(job, force);

        return new Result();
    }

	
	/**
     * Push the Service Job to DataBase and Redis<br/>
	 * @param job ServiceJob to be processed
	 * @param force whether to push the job by force
     * @return result of the operation
     * @version crossdomain 0.5 2016-3-18
     */	
    public Result pushJob(ServiceJob job, boolean force) {
        log.debug("received: {}", job.toString());

        job.setJobId(CommonUtil.generateUuid());

        job.setCreatedTime(CommonUtil.getTimeInMillis());

        job.setInstanceId(schedulerProxy.getInstanceId());

        Result result = insertJobToDb(job);
        if(!result.isSuccess()) {
            return result;
        }

        jobQueue.push(job, force);

        return result;
    }

	/**
     * Pop The Service Job From RedisQueue<br/>
     * @return ServiceJob to be processed
     * @version crossdomain 0.5 2016-3-18
     */	
    public ServiceJob popJob() {
        return jobQueue.pop(ServiceJob.class);
    }

    /**
     * Get Service Job By JobId<br/>
     * @return ServiceJob to be processed
     * @version crossdomain 0.5 2016-3-18
     */	
    public ServiceJob getServiceJob(String jobId) {
        List<ServiceJob> allJob = jobQueue.get(ServiceJob.class);
        return JobUtil.getServiceJobById(allJob, jobId);
    }

	/**
     * Clean The RedisQueue<br/>
     * @version crossdomain 0.5 2016-3-18
     */	
    public void clearQueue() {
        jobQueue.clear();
    }
}
