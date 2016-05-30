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
package org.openo.crossdomain.commonsvc.executor.dao.impl;

import org.openo.commonservice.log.OssLog;
import org.openo.commonservice.log.OssLogFactory;
import org.openo.commonservice.mybatis.session.MapperManager;
import org.openo.commonservice.remoteservice.exception.ServiceException;

import org.apache.ibatis.exceptions.PersistenceException;
import org.openo.crossdomain.commonsvc.executor.common.constant.Constants;
import org.openo.crossdomain.commonsvc.executor.common.util.CommonUtil;
import org.openo.crossdomain.commonsvc.executor.dao.inf.IServiceJobDao;
import org.openo.crossdomain.commonsvc.executor.dao.mapper.ServiceExecutorJobMapper;
import org.openo.crossdomain.commonsvc.executor.model.ServiceJob;
import org.openo.crossdomain.commonsvc.executor.model.db.Result;
import org.openo.crossdomain.commonsvc.executor.model.db.ServiceJobForDB;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@Component(value = "srvExectuorJobDao")
public class ServiceJobDaoImpl implements IServiceJobDao {

    private static final OssLog logger = OssLogFactory.getLogger(ServiceJobDaoImpl.class);

    @Resource
    private MapperManager mapperMgrRef;

    public <T> T getMapperManager(Class<T> type) {
        return mapperMgrRef.getMapper(type, Constants.SERVICE_EXECUTOR_DB);
    }

    /**
	 *insert service job
	 *@param job ServiceJob
	 *@return Result of process
	 *@since crossdomain 0.5 2016-3-18
	 */
    @Override
    public Result insert(@NotNull ServiceJob job) {
        if(job == null) {
            logger.info("Failed to insert Job.The Job is null.");
            return new Result(Result.FAIL, "The Job is null", "");
        }

        ServiceJobForDB dbModelDb = convertToDBModel(job);
        ServiceExecutorJobMapper sdMapper = getMapperManager(ServiceExecutorJobMapper.class);
        try {
            sdMapper.insertExecutorJob(dbModelDb);
        } catch(PersistenceException e) {
            logger.error(e.getMessage());
            return new Result(Result.FAIL, e.getMessage(), job.getService().getServiceName());
        }

        logger.info("Insert Job ok! Job id is:[" + job.getJobId() + "]");
        return new Result();
    }

    private ServiceJobForDB convertToDBModel(@NotNull ServiceJob job) {
        ServiceJobForDB dbModel =
                new ServiceJobForDB(job.getJobId(), job.getService().getServiceId(), job.getJobContent());
        dbModel.setTenantId(job.getTenantId());
        dbModel.setStatus(job.getStatus());
        dbModel.setErrorCode((job.getResult() == null) ? null : job.getResult().getErrorCode());

        if((job.getResult() != null) && (job.getResult().getResultReason() != null)) {
            dbModel.setResultReason(job.getResult().getResultReason());
        }

        dbModel.setCreatedTime(job.getCreatedTime());
        dbModel.setCompletedTime(job.getCompletedTime());
        dbModel.setAuditBasicInfo(job.getAuditBasicInfo());

        return dbModel;
    }
    /**
	 *delete service job
	 *@param job ServiceJob
	 *@return Result of process
	 *@since crossdomain 0.5 2016-3-18
	 */
    @Override
    public Result delete(@NotNull ServiceJob job) {
        if(job == null) {
            logger.info("Failed to delete Job.The Job is null.");
            return new Result(Result.FAIL, "The Job is null", "");
        }

        ServiceJobForDB dbModelDb = convertToDBModel(job);
        ServiceExecutorJobMapper sdMapper = getMapperManager(ServiceExecutorJobMapper.class);
        try {
            sdMapper.deleteExecutorJob(dbModelDb);
        } catch(PersistenceException e) {
            logger.error(e.getMessage());
            return new Result(Result.FAIL, e.getMessage(), job.getService().getServiceName());
        }

        logger.info("Delete Job ok! Job id is:[" + job.getJobId() + "]");
        return new Result();
    }

	/**
	 *update service job
	 *@param job ServiceJob
	 *@return Result of process
	 *@since crossdomain 0.5 2016-3-18
	 */
    @Override
    public Result update(@NotNull ServiceJob job) {
        if(job == null) {
            logger.info("Failed to update Job.The Job is null.");
            return new Result(Result.FAIL, "The Job is null", "");
        }

        ServiceJobForDB dbModelDb = convertToDBModel(job);
        ServiceExecutorJobMapper sdMapper = getMapperManager(ServiceExecutorJobMapper.class);
        try {
            sdMapper.updateExecutorJob(dbModelDb);
        } catch(PersistenceException e) {
            logger.error(e.getMessage());
            return new Result(Result.FAIL, e.getMessage(), job.getService().getServiceName());
        }

        logger.info("Update Job ok! Job id is:[" + job.getJobId() + "]");
        return new Result();
    }

	/**
	 *get all service job
	 *@return List of service job
	 *@throws PersistenceException when fai to getAllJobs
	 *@since crossdomain 0.5 2016-3-18
	 */
    @Override
    public @NotNull List<ServiceJob> getAllJobs() throws PersistenceException {
        ServiceExecutorJobMapper sdMapper = getMapperManager(ServiceExecutorJobMapper.class);
        List<ServiceJobForDB> dbJobs = sdMapper.getAllJobs();
        if(dbJobs == null) {
            return Collections.emptyList();
        }

        List<ServiceJob> allJobs = convertToServiceModelList(dbJobs);
        logger.info("Get all Jobs OK!");
        return allJobs;
    }
	/**
	 *get all service job by job id
	 *@param jobIdList job id list
	 *@return List of service job
	 *@throws PersistenceException when fai to getAllJobs
	 *@since crossdomain 0.5 2016-3-18
	 */
    @Override
    public List<ServiceJob> getJobsByJobId(List<String> jobIdList) throws PersistenceException {
        ServiceExecutorJobMapper sdMapper = getMapperManager(ServiceExecutorJobMapper.class);

        List<ServiceJob> allJobs = new ArrayList<>();
        List<String> tmpIdList = new ArrayList<>(Constants.BATCH_NUM);
        Iterator<String> iter = jobIdList.iterator();
        while(iter.hasNext()) {
            tmpIdList.clear();
            for(int i = Constants.NULL_ID; i < Constants.BATCH_NUM && iter.hasNext(); ++i) {
                tmpIdList.add(iter.next());
            }

            List<ServiceJobForDB> dbJobs = sdMapper.getJobsByJobId(tmpIdList);
            if(dbJobs == null) {
                return Collections.emptyList();
            }

            allJobs.addAll(convertToServiceModelList(dbJobs));
        }

        return allJobs;
    }
	/**
	 *get service job by job id
	 *@param jobId job id
	 *@return service job
	 *@throws PersistenceException when fai to getAllJobs
	 *@since crossdomain 0.5 2016-3-18
	 */
    @Override
    public ServiceJob getJob(@NotNull String jobId) throws PersistenceException {
        ServiceExecutorJobMapper sdMapper = getMapperManager(ServiceExecutorJobMapper.class);
        ServiceJobForDB dbJob = sdMapper.getJob(jobId);
        if(dbJob == null) {
            return null;
        }

        ServiceJob job = convertToServiceModel(dbJob);
        logger.info("Get Job by jobId OK!");
        return job;
    }

    private ServiceJob convertToServiceModel(@NotNull ServiceJobForDB dbJob) {
        ServiceJob job = null;
        try {
            job = ServiceJob.toServiceJob(CommonUtil.decrypt(dbJob.getJobContent()));
        } catch(ServiceException e) {
            logger.error("convertToServiceModel, decrypt fails:{}", e.getMessage());
        }
        if(job == null) {
            return null;
        }

        job.setJobId(dbJob.getJobId());
        job.setTenantId(dbJob.getTenantId());

        job.setStatus(dbJob.getStatus());

        Result result = null;
        if((dbJob.getErrorCode() != null) && (dbJob.getResultReason() != null)) {
            result = new Result(dbJob.getErrorCode(), dbJob.getResultReason(), job.getService().getServiceName());
        }
        job.setResult(result);

        job.setCreatedTime(dbJob.getCreatedTime());
        job.setCompletedTime(dbJob.getCompletedTime());
        job.setAuditBasicInfo(dbJob.getAuditBasicInfo());
        return job;
    }

    private @NotNull List<ServiceJob> convertToServiceModelList(@NotNull List<ServiceJobForDB> dbJobs) {
        List<ServiceJob> allJobs = new ArrayList<ServiceJob>(dbJobs.size());
        for(ServiceJobForDB dbJob : dbJobs) {
            if(dbJob == null) {
                continue;
            }
            allJobs.add(convertToServiceModel(dbJob));
        }
        return allJobs;
    }
	/**
	 *get all service job by service id
	 *@param serviceId service id
	 *@param startIndex start from
	 *@param pageCapacity page capacity
	 *@return List of service job
	 *@throws PersistenceException when fai to getAllJobs
	 *@since crossdomain 0.5 2016-3-18
	 */
    @NotNull
    public List<ServiceJob> getJobByServiceID(String serviceId, int startIndex, int pageCapacity)
            throws PersistenceException {
        ServiceExecutorJobMapper sdMapper = getMapperManager(ServiceExecutorJobMapper.class);
        List<ServiceJobForDB> dbJobs = sdMapper.getJobByServiceID(serviceId, startIndex, pageCapacity);

        List<ServiceJob> jobs = new ArrayList<ServiceJob>();
        if(dbJobs != null) {
            jobs = convertToServiceModelList(dbJobs);
        }

        logger.info("Get all Jobs OK!");
        return jobs;
    }

    /**
	 *get not completed job count
	 *@return count of the not completed jobs
	 *@throws PersistenceException when fai to get the count
	 *@since crossdomain 0.5 2016-3-18
	 */
    public int getNotCompletedJobCount() throws PersistenceException {
        ServiceExecutorJobMapper sdMapper = getMapperManager(ServiceExecutorJobMapper.class);
        int count = sdMapper.getNotCompletedJobCount();
        logger.info("Get not completed job count successfully!");
        return count;
    }

	/**
	 *get job id List
	 *@param statusList job status list
	 *@return List of job id
	 *@param createTime job created at
	 *@throws PersistenceException when fai to getAllJobs
	 *@since crossdomain 0.5 2016-3-18
	 */
    public List<String> getJobId(List<String> statusList, long createTime) throws PersistenceException {
        ServiceExecutorJobMapper sdMapper = getMapperManager(ServiceExecutorJobMapper.class);
        return sdMapper.getJobId(statusList, createTime);
    }
}
