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
package org.openo.crossdomain.commonsvc.executor.service.impl;

import org.openo.commonservice.datasource.DataSourceCenter;
import org.openo.commonservice.log.OssLog;
import org.openo.commonservice.log.OssLogFactory;
import org.openo.commonservice.remoteservice.exception.ServiceException;
import org.openo.commonservice.roa.common.HttpContext;
import org.openo.commonservice.roa.common.RequestInputStream;

import org.openo.crossdomain.commonsvc.executor.common.constant.Constants;
import org.openo.crossdomain.commonsvc.executor.common.enums.ExecutionStatus;
import org.openo.crossdomain.commonsvc.executor.common.redis.JobIdOper;
import org.openo.crossdomain.commonsvc.executor.common.redis.RedisJobMapProxy;
import org.openo.crossdomain.commonsvc.executor.common.util.CommonUtil;
import org.openo.crossdomain.commonsvc.executor.common.util.ExecutorContextHelper;
import org.openo.crossdomain.commonsvc.executor.common.util.SchedulerProxy;
import org.openo.crossdomain.commonsvc.executor.common.util.ServiceExceptionUtil;
import org.openo.crossdomain.commonsvc.executor.dao.inf.IServiceJobDao;
import org.openo.crossdomain.commonsvc.executor.model.ServiceJob;
import org.openo.crossdomain.commonsvc.executor.model.db.Result;
import org.openo.crossdomain.commonsvc.executor.service.Manager;
import org.openo.crossdomain.commonsvc.executor.service.inf.ITimingTaskService;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.io.IOException;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * Service Executor Timing Task<br/>
 *
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
@Service(value = "timingTaskService")
public class TimingTaskService implements ITimingTaskService {

    /**
     * log util
     */
    private static final OssLog log = OssLogFactory.getLogger(TimingTaskService.class);

    @javax.annotation.Resource
    private IServiceJobDao srvExectuorJobDao; // DAO api

    /**
     * System Timing Task API triggered once a day
     * delete jobs that already completed over 30 days<br/>
     *
     * @param input RequestInputStream
     * @param context HttpContext
     * @throws ServiceException
     * @since crossdomain 0.5
     */
    @Override
    public void deleteJobsByTimingTask(RequestInputStream input, HttpContext context) throws ServiceException {
        long currentTime = CommonUtil.getTimeInMillis(); // in millisecond
        log.info("Start to delete completed job after 30 days from DB and Redis. currTime:", currentTime);

        try {
            List<ServiceJob> alljobs = srvExectuorJobDao.getAllJobs();
            for(ServiceJob job : alljobs) {
                // if job status is not completed, ignore
                if((job == null) || (ExecutionStatus.COMPLETED != job.getStatus())) {
                    continue;
                }

                // if job status is completed, but not over 30 days, ignore
                long completedTime = job.getCompletedTime();
                long diffDays = (currentTime - completedTime) / Constants.ONE_DAY_AS_MILLIS_SECONDS;
                if(diffDays < Constants.DELETE_COMPLETED_JOBS_AFTER_DAYS) {
                    continue;
                }

                // delete jobs from database
                Result dbResult = srvExectuorJobDao.delete(job);
                if(dbResult.isSuccess()) {
                    log.info("Completed job was deleted from DB successfully.Job id: ", job.getJobId());
                } else {
                    log.error("Failed to delete completed job from DB.Job id: " + job.getJobId());
                }

                // delete jobs from redis
                boolean redisResult = Manager.getInstance().delete(job);
                if(redisResult) {
                    log.info("Completed job was deleted from Redis successfully.Job id: ", job.getJobId());
                } else {
                    log.error("Failed to delete completed job from Redis.Job id: " + job.getJobId());
                }
            }
        } catch(SQLException e) {
            ServiceExceptionUtil
                    .throwErrorException("Failed to delete completed jobs after 30 days due to datatbase error.");
        }
    }

    /**
     * Timing Task to Check Redis Capability
     * triggered every 5 minitues, check Redis Capability
     * delete jobs that already completed, when Redis Capability utilization is over 85% <br/>
     *
     * @throws ServiceException fail to execute the timing task
     * @since crossdomain 0.5
     */
    public void checkRedisFull() throws ServiceException {
        ShardedJedisPool pool = DataSourceCenter.getInstance().getRedisPool(Constants.RDB_NAME);
        if(pool == null) {
            log.error("getRedisPool return null");
            return;
        }

        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = pool.getResource();
            Iterator shardIt = shardedJedis.getAllShards().iterator();

            while(shardIt.hasNext()) {
                Jedis jedis = (Jedis)shardIt.next();

                Properties properties = new Properties();
                try {
                    properties.load(new StringReader(jedis.info()));
                } catch(IOException e) {
                    log.error("properties.load fails");
                }

                int usedMemory = Integer.parseInt(properties.getProperty(Constants.REDIS_USED_MEMORY));
                if(usedMemory > Constants.REDIS_MEM_ALARM) {
                    cleanCompletedJob();
                    break;
                }
            }
        } finally {
            if(shardedJedis != null && pool != null) {
                pool.returnResource(shardedJedis);
            }
        }
    }

    /**
     * Clear Completed Job From Redis<br/>
     *
     * @since crossdomain 0.5
     */
    @Override
    public void cleanCompletedJob() {
        RedisJobMapProxy redisProxy = RedisJobMapProxy.getInstance();

        SchedulerProxy schedulerProxy =
                (SchedulerProxy)ExecutorContextHelper.getInstance().getBean(Constants.SpringDefine.SCHEDULERPROXY);
        JobIdOper jobIdOper = new JobIdOper(schedulerProxy.getInstanceId());

        Set<String> tenantSet = jobIdOper.getTenantId();

        for(String tenantId : tenantSet) {
            Set<String> jobIdSet = jobIdOper.getJobIdByTenantId(tenantId);

            for(String jobId : jobIdSet) {
                ServiceJob job = redisProxy.getRedisOper(tenantId).get(ServiceJob.class, jobId);

                if(job == null) {
                    continue;
                }

                if(job.getStatus() != ExecutionStatus.COMPLETED) {
                    continue;
                }

                if(!job.getInstanceId().equals(schedulerProxy.getInstanceId())) {
                    continue;
                }

                // clearjobMap
                redisProxy.getRedisOper(tenantId).remove(jobId);

                // clear relation between tenantId and JobId
                jobIdOper.deleteJobId(tenantId, jobId);
            }
        }
    }
}
