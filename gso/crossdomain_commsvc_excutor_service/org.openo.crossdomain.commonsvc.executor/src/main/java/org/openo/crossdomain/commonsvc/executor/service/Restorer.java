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

import org.apache.ibatis.exceptions.PersistenceException;
import org.openo.crossdomain.commonsvc.executor.common.constant.Constants;
import org.openo.crossdomain.commonsvc.executor.common.enums.ExecutionStatus;
import org.openo.crossdomain.commonsvc.executor.common.redis.DeadInstanceOper;
import org.openo.crossdomain.commonsvc.executor.common.redis.RedisJobMapProxy;
import org.openo.crossdomain.commonsvc.executor.common.util.CommonUtil;
import org.openo.crossdomain.commonsvc.executor.common.util.SchedulerProxy;
import org.openo.crossdomain.commonsvc.executor.dao.inf.IServiceJobDao;
import org.openo.crossdomain.commonsvc.executor.dao.inf.IServiceResDao;
import org.openo.crossdomain.commonsvc.executor.model.Resource;
import org.openo.crossdomain.commonsvc.executor.model.ServiceJob;
import org.openo.crossdomain.commonsvc.executor.model.db.Result;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component(value = "restorer")
public class Restorer {

    private OssLog logger = OssLogFactory.getLog(Restorer.class);

    @javax.annotation.Resource
    private IServiceJobDao srvExectuorJobDao;

    @javax.annotation.Resource
    private IServiceResDao serviceResourceDao;

    @javax.annotation.Resource
    private Receiver receiver;

    @javax.annotation.Resource
    private DeadInstanceOper deadInstanceOper;

    @javax.annotation.Resource
    private SchedulerProxy schedulerProxy;

    private RedisJobMapProxy jobMapProxy = RedisJobMapProxy.getInstance();

		
    /**
     * Restore Data From Database When StartUp<br/>
     * 
     * @version crossdomain 0.5 2016-3-18
     */
    public void restoreDataFromStartUp() {

        if(!deadInstanceOper.adoptInstance(schedulerProxy.getInstanceId())) {
            return;
        }

        long now = CommonUtil.getTimeInMillis();

        Manager.getInstance().clearMap();

        restoreDataDirectly(now);

        deadInstanceOper.deleteDeadInstance(schedulerProxy.getInstanceId());
    }

		
    /**
     * Resore Data Directly<br/>
     * 
     * @version crossdomain 0.5 2016-3-18
     */
    public void restoreDataDirectly(long stopTime) {
        long statTime = CommonUtil.getTimeInMillis();
        logger.warn("time start: restoreDataDirectly:{}", statTime);
        try {
            List<String> statusList = new ArrayList<>();
            statusList.add(ExecutionStatus.INITEXECUTE.toString());
            statusList.add(ExecutionStatus.EXECUTING.toString());
            List<String> jobIdList = srvExectuorJobDao.getJobId(statusList, stopTime);
            logger.warn("restoreDataDirectly: jobIdList:{}", jobIdList.size());

            List<String> tmpIdList = new ArrayList<>(Constants.BATCH_NUM);
            Iterator<String> iter = jobIdList.iterator();
            for(int batch = Constants.NULL_ID; iter.hasNext(); ++batch) {
                logger.warn("restoreDataDirectly: batch num:{}", batch);

                tmpIdList.clear();
                for(int i = Constants.NULL_ID; i < Constants.BATCH_NUM && iter.hasNext(); ++i) {
                    tmpIdList.add(iter.next());
                }

                List<ServiceJob> jobList = srvExectuorJobDao.getJobsByJobId(tmpIdList);
                for(ServiceJob serviceJob : jobList) {
                    restoreJob(serviceJob);
                }
            }
        } catch(PersistenceException e)

        {
            String msg = String.format("db error: %s", e.getMessage());
            logger.error(msg);
        }

        long endTime = CommonUtil.getTimeInMillis();
        logger.warn("time end: restoreDataDirectly:{}", endTime, endTime - statTime);
    }

	
		
    /**
     * Restore The Service Job<br/>
     * 
     * @version crossdomain 0.5 2016-3-18
     */
    public void restoreJob(ServiceJob serviceJob) {
        try {

            if(jobMapProxy.getRedisOper(serviceJob.getTenantId()).containsKey(serviceJob.getJobId())) {
                return;
            }

            if(serviceJob.getStatus() == ExecutionStatus.INITEXECUTE) {
                receiver.pushJobOnly(serviceJob, true);
            }

            else if(serviceJob.getStatus() == ExecutionStatus.EXECUTING) {

                Result result = serviceResourceDao.deleteByStatus(serviceJob.getJobId(), ExecutionStatus.EXECUTING);
                if(!result.isSuccess()) {
                    logger.error("deleteByStatus fails: {}", serviceJob.getJobId());
                    return;
                }

                List<Resource> resourceList = serviceResourceDao.getAllResourcesOfJob(serviceJob.getJobId());
                List<Resource> completeResList = new ArrayList<>();
                for(Resource resource : resourceList) {
                    serviceJob.getService().getResources().put(resource.getKey(), resource);

                    if(resource.getStatus() == ExecutionStatus.COMPLETED) {
                        completeResList.add(resource);
                    }
                }

                for(Resource resource : completeResList) {
                    Manager.getInstance().refreshResource(resource, serviceJob);
                }

                Manager.getInstance().resetResourceInitStatus(serviceJob);

                if(completeResList.size() == serviceJob.getService().getResources().size()) {
                    serviceJob.setStatus(ExecutionStatus.COMPLETED);
                    srvExectuorJobDao.update(serviceJob);

                    Manager.getInstance().put(serviceJob);

                } else {

                    Manager.getInstance().put(serviceJob);

                    Dispatcher.getInstance().executeJob(serviceJob);
                }
            }
        } catch(SQLException e) {
            String msg = String.format("db error: %s", e.getMessage());
            logger.error(msg);
        }
    }

}
