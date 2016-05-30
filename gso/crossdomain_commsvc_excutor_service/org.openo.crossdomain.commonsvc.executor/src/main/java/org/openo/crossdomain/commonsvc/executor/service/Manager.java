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
import org.openo.commonservice.remoteservice.exception.ServiceException;
import org.openo.crossdomain.commsvc.formation.exception.FunctionException;
import org.openo.crossdomain.commsvc.formation.func.facade.Facade;

import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.openo.crossdomain.commonsvc.executor.common.constant.Constants;
import org.openo.crossdomain.commonsvc.executor.common.constant.ErrorMessage;
import org.openo.crossdomain.commonsvc.executor.common.enums.ActionType;
import org.openo.crossdomain.commonsvc.executor.common.enums.ExecutionStatus;
import org.openo.crossdomain.commonsvc.executor.common.redis.JobIdOper;
import org.openo.crossdomain.commonsvc.executor.common.redis.RedisJobMapProxy;
import org.openo.crossdomain.commonsvc.executor.common.util.*;
import org.openo.crossdomain.commonsvc.executor.dao.inf.IServiceJobDao;
import org.openo.crossdomain.commonsvc.executor.dao.inf.IServiceResDao;
import org.openo.crossdomain.commonsvc.executor.model.Resource;
import org.openo.crossdomain.commonsvc.executor.model.ServiceInfo;
import org.openo.crossdomain.commonsvc.executor.model.ServiceJob;
import org.openo.crossdomain.commonsvc.executor.model.db.Result;

public class Manager {

    private static Manager instance;

    private static IServiceJobDao srvExectuorJobDao;

    private static IServiceResDao srvExectuorResourceDao;

    private final OssLog log = OssLogFactory.getLogger(Manager.class);

    private final Lock lock;

    /**
     * Job Map
     */
    private RedisJobMapProxy redisProxy = RedisJobMapProxy.getInstance();

    private JobIdOper jobIdOper;

    private String intanceId;

    private Manager() {
        lock = new ReentrantLock();
        Object resourceDao = ExecutorContextHelper.getInstance().getBean("serviceResourceDao");
        if(resourceDao != null) {
            srvExectuorResourceDao = (IServiceResDao)resourceDao;
        }

        Object executorDao = ExecutorContextHelper.getInstance().getBean("srvExectuorJobDao");
        if(executorDao != null) {
            srvExectuorJobDao = (IServiceJobDao)executorDao;
        }

        SchedulerProxy schedulerProxy =
                (SchedulerProxy)ExecutorContextHelper.getInstance().getBean(Constants.SpringDefine.SCHEDULERPROXY);
        intanceId = schedulerProxy.getInstanceId();

        jobIdOper = new JobIdOper(intanceId);
    }

	/**
     * Get the Manager Instance<br/>
     * @return Manager to be processed
     * @version crossdomain 0.5 2016-3-18
     */	
    public synchronized static Manager getInstance() {
        if(instance == null) {
            instance = new Manager();
        }
        return instance;
    }

	/**
     * Put The Service Job into Redis<br/>
     * @param job ServiceJob to be processed
     * @version crossdomain 0.5 2016-3-18
     */	
    public void put(ServiceJob job) {
        try {
            lock.lockInterruptibly();

            job.setInstanceId(intanceId);

            redisProxy.getRedisOper(job.getTenantId()).put(job.getJobId(), job);

            jobIdOper.addJobId(job.getTenantId(), job.getJobId());

            log.debug("job start to execute: {}", job.toString());
        } catch(InterruptedException e) {
            log.debug("put lock interrupted. job id : {}", job.getJobId());
        } finally {
            lock.unlock();
        }
    }
	/**
     * Delete The Service Job into Redis<br/>
     * @param job ServiceJob to be processed
	 * @return the result of the operation
     * @version crossdomain 0.5 2016-3-18
     */	
    public boolean delete(ServiceJob job) {
        try {
            lock.lockInterruptibly();
            redisProxy.getRedisOper(job.getTenantId()).remove(job.getJobId());

            jobIdOper.deleteJobId(job.getTenantId(), job.getJobId());

            log.info("A job was deleted. Job id: ", job.toString());
            return true;
        } catch(InterruptedException e) {
            log.error("deletion lock interrupted. job id: ", job.getJobId());
            return false;
        } finally {
            lock.unlock();
        }
    }

	
    /**
     * Get The Service Job By TenantId and JobId<br/>
     * @param jobId ServiceJob Id
	 * @param tenantId Tenant Id
     * @version crossdomain 0.5 2016-3-18
     */	
    public ServiceJob get(String tennantId, String jobId) {
        try {
            lock.lockInterruptibly();
            return redisProxy.getRedisOper(tennantId).get(ServiceJob.class, jobId);
        } catch(InterruptedException e) {
            log.debug("get lock interrupted. job id : {}", jobId);
            return null;
        } finally {
            lock.unlock();
        }
    }

	/**
     * Clean the RedisMap<br/>
     * @version crossdomain 0.5 2016-3-18
     */	
    public void clearMap() {
        try {
            lock.lockInterruptibly();

            Set<String> tenantIdSet = jobIdOper.getTenantId();
            for(String tenantId : tenantIdSet) {
                Set<String> jobIdSet = jobIdOper.getJobIdByTenantId(tenantId);

                for(String jobId : jobIdSet) {
                    redisProxy.getRedisOper(tenantId).remove(jobId);
                    jobIdOper.deleteJobId(tenantId, jobId);
                }
            }
        } catch(InterruptedException e) {
            log.debug("get lock interrupted.");
        } finally {
            lock.unlock();
        }
    }

	/**
     * Start to Execute the Resource<br/>
     * @param jobId ServiceJob Id
	 * @param tenantId Tenant Id
	 * @param resource resource to be send to plugin
     * @version crossdomain 0.5 2016-3-18
     */	
    public void startToExcute(String tenantId, String jobId, Resource resource) {
        try {
            lock.lockInterruptibly();
            ServiceJob serviceJob = null;
            serviceJob = redisProxy.getRedisOper(tenantId).get(ServiceJob.class, jobId);
            if(serviceJob != null) {

                Resource storedResource = serviceJob.getService().getResources().get(resource.getKey());

                storedResource.setQueryUrl(resource.getQueryUrl());

                srvExectuorResourceDao.insert(serviceJob.getJobId(), serviceJob.getService().getServiceId(),
                        storedResource);

                storedResource.setStatus(ExecutionStatus.EXECUTING);

                redisProxy.getRedisOper(tenantId).put(jobId, serviceJob);

            }
        } catch(InterruptedException e) {
            log.debug("start to execute lock interrupted. job id : {}", jobId);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Start to Execute the Service Job<br/>
     * @param service ServiceInfo of the ServiceJob
     * @version crossdomain 0.5 2016-3-18
     */	
    public void executed(ServiceInfo service) {
        try {
            lock.lockInterruptibly();

            if(service == null) {
                log.error(ErrorMessage.OBJECT_NULL_MSG, Constants.SERVICE);
                return;
            }

            ServiceJob serviceJob =
                    redisProxy.getRedisOper(service.getTenantId()).get(ServiceJob.class, service.getJobId());
            if(serviceJob == null) {
                log.error("can not find job, service id: {}", service.getJobId());
                return;
            }

            for(Entry<String, Resource> entry : service.getResources().entrySet()) {
                Resource oriRes = serviceJob.getService().getResources().get(entry.getKey());
                if(oriRes == null) {
                    log.error("can not find resource, resource id: {}", entry.getValue().getId());
                    continue;
                }
                oriRes.setId(entry.getValue().getId());
                oriRes.setResult(entry.getValue().getResult());
                oriRes.setStatus(ExecutionStatus.COMPLETED);
                oriRes.setResContent(entry.getValue().getResContent());

                srvExectuorResourceDao.update(serviceJob.getJobId(), oriRes);
                refreshResource(oriRes, serviceJob);

            }

            redisProxy.getRedisOper(serviceJob.getTenantId()).put(serviceJob.getJobId(), serviceJob);

            if(!isJobCompelete(serviceJob)) {
                Dispatcher.getInstance().executeJob(serviceJob);
            } else {
                serviceJob.setStatus(ExecutionStatus.COMPLETED);

                serviceJob.setCompletedTime(CommonUtil.getTimeInMillis());

                serviceJob.computeResult();

                srvExectuorJobDao.update(serviceJob);

                redisProxy.getRedisOper(serviceJob.getTenantId()).put(serviceJob.getJobId(), serviceJob);

                JobUtil.getInstance().removeResourceOfDecomposed(serviceJob.getJobId());

                LogUtil.writeCreateJobCompletedLog(serviceJob.getAuditItem(), serviceJob);
                // LogUtil.writeCreateJobCompletedLog();

            }
        } catch(InterruptedException e) {
            log.debug("executed lock interrupted. service id : {}", service.getServiceId());
        } finally {
            lock.unlock();
        }

    }

    /**
     * Start to Refresh the Resource Status<br/>
     * @param serviceJob ServiceJob
	 * @param resource resource to be send to plugin
     * @version crossdomain 0.5 2016-3-18
     */	
    public void refreshResource(Resource resource, ServiceJob serviceJob) {

        if((resource.getResult() != null) && Result.FAIL.equals(resource.getResult().getErrorCode())) {
            List<Resource> descendantResources = JobUtil.getAllDependedResourcesByAction(serviceJob, resource);
            updateResourceForDependFailed(serviceJob.getJobId(), resource, descendantResources);
        }

        else {
            List<Resource> dependList = JobUtil.getDirectDependResources(serviceJob, resource);
            if(!dependList.isEmpty()) {

                updateResourceDependon(serviceJob.getService().getAction(), resource, dependList);

                String decryptStr = null;
                try {
                    decryptStr = CommonUtil.decrypt(resource.getResContent());
                } catch(ServiceException e) {
                    log.error("refreshResource, decrypt fails:{}", e.getMessage());
                    return;
                }

                List<JSONObject> dependObjList = new ArrayList<>();
                dependObjList.add(JSONObject.fromObject(decryptStr));

                for(Resource child : dependList) {

                    replaceResDynamicFunction(child, dependObjList);

                    srvExectuorResourceDao.update(serviceJob.getJobId(), child);
                }
            }
        }
    }

    /**
     * Refresh the Status of the Resource<br/>
     * @param serviceJob ServiceJob
     * @version crossdomain 0.5 2016-3-18
     */	
    public void resetResourceInitStatus(ServiceJob serviceJob) {
        for(Map.Entry<String, Resource> entry : serviceJob.getService().getResources().entrySet()) {
            if(entry.getValue().getStatus() == ExecutionStatus.EXECUTING) {
                entry.getValue().setStatus(ExecutionStatus.INITEXECUTE);
                entry.getValue().setQueryUrl(Constants.NULL_STR);
            }
        }
    }

    private boolean isJobCompelete(ServiceJob serviceJob) {
        boolean isJobCompelete = true;
        for(Resource resource : serviceJob.getService().getResources().values()) {
            if(resource.getStatus() != ExecutionStatus.COMPLETED) {
                isJobCompelete = false;
                break;
            }
        }

        return isJobCompelete;
    }

    private void replaceResDynamicFunction(Resource child, List<JSONObject> dependObjList) {

        org.openo.crossdomain.commsvc.formation.model.Entry<Boolean, JSONObject> entry = null;
        try {
            JSONObject childObj = JSONObject.fromObject(CommonUtil.decrypt(child.getResContent()));
            entry = Facade.replaceResDynamicFunction(childObj, dependObjList);
            if(entry.getKey()) {
                child.setResContent(CommonUtil.encrypt(entry.getValue().toString()));
            }
        } catch(FunctionException | ServiceException e) {
            log.error("replaceResDynamicFunction fails: {}", child.getKey());
        }
    }

    private void updateResourceForDependFailed(String jobId, Resource resource, List<Resource> dependList) {
        for(Resource dependResource : dependList) {
            dependResource.setStatus(ExecutionStatus.COMPLETED);
            Result rst = new Result(Result.FAIL, "Failed to execute depend resource.", resource.getName());
            dependResource.setResult(rst);
            srvExectuorResourceDao.update(jobId, dependResource);
        }
    }

    private void updateResourceDependon(ActionType action, Resource resource, List<Resource> dependList) {
        if(action != ActionType.DELETE) {
            for(Resource child : dependList) {
                child.getDependon().remove(resource.getKey());
            }
        } else {
            for(Resource father : dependList) {
                resource.getDependon().remove(father.getKey());
            }
        }
    }
}
