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

import net.sf.json.JSONObject;

import org.apache.ibatis.exceptions.PersistenceException;
import org.openo.crossdomain.commonsvc.executor.common.constant.Constants;
import org.openo.crossdomain.commonsvc.executor.common.enums.ExecutionStatus;
import org.openo.crossdomain.commonsvc.executor.common.util.CommonUtil;
import org.openo.crossdomain.commonsvc.executor.dao.inf.IServiceResDao;
import org.openo.crossdomain.commonsvc.executor.dao.mapper.ResMappingMapper;
import org.openo.crossdomain.commonsvc.executor.model.Resource;
import org.openo.crossdomain.commonsvc.executor.model.db.ResourceForDB;
import org.openo.crossdomain.commonsvc.executor.model.db.Result;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component(value = "serviceResourceDao")
public class ServiceResDaoImpl implements IServiceResDao {

    private static final OssLog logger = OssLogFactory.getLogger(ServiceResDaoImpl.class);

    @javax.annotation.Resource
    private MapperManager mapperMgrRef;

    public <T> T getMapperManager(Class<T> type) {
        return mapperMgrRef.getMapper(type, Constants.SERVICE_EXECUTOR_DB);
    }

	/**
	 *insert resource
	 *@param jobId id of the job
	 *@param serviceId service id
	 *@param resource resource
	 *@return Result of process
	 *@since crossdomain 0.5 2016-3-18
	 */
    @Override
    public Result insert(@NotNull String jobId, @NotNull String serviceId, @NotNull Resource resource) {
        if((jobId == null) || (resource == null)) {
            logger.info("Failed to insert resource.The jobId or resource is null.");
            return new Result(Result.FAIL, "The jobId or resource is null.", "");
        }

        ResourceForDB dbModel = resource.convertToDBModel(jobId, serviceId);
        ResMappingMapper sdMapper = getMapperManager(ResMappingMapper.class);
        try {
            sdMapper.insert(dbModel);
        } catch(PersistenceException e) {
            logger.error(e.toString());
            return new Result(Result.FAIL, e.getMessage(), resource.getName());
        }

        logger.info("Insert Resource ok! Resource id is:[" + resource.getKey() + "]");
        return new Result();
    }

    /**
	 *delete resource
	 *@param jobId id of the job
	 *@param resource resource
	 *@return Result of process
	 *@since crossdomain 0.5 2016-3-18
	 */
    @Override
    public Result delete(@NotNull String jobId, @NotNull Resource resource) {
        if((jobId == null) || (resource == null)) {
            logger.info("Failed to delete resource.The jobId or resource is null.");
            return new Result(Result.FAIL, "The jobId or resource is null.", "");
        }

        ResourceForDB dbModel = resource.convertToDBModel(jobId, Constants.NULL_STR);
        ResMappingMapper sdMapper = getMapperManager(ResMappingMapper.class);
        try {
            sdMapper.delete(dbModel);
        } catch(PersistenceException e) {
            logger.error(e.toString());
            return new Result(Result.FAIL, e.getMessage(), resource.getName());
        }

        logger.info("Delete Resource ok! Resource id is:[" + resource.getKey() + "]");

        return new Result();
    }

    /**
	 *delete resource by status
	 *@param jobId id of the job
	 *@param status ExecutionStatus of resource
	 *@return Result of process
	 *@since crossdomain 0.5 2016-3-18
	 */
    @Override
    public Result deleteByStatus(@NotNull String jobId, @NotNull ExecutionStatus status) {
        ResMappingMapper sdMapper = getMapperManager(ResMappingMapper.class);
        try {
            sdMapper.deleteByStatus(jobId, status.toString());
        } catch(PersistenceException e) {
            logger.error(e.toString());
            return new Result(Result.FAIL, e.getMessage(), jobId);
        }

        logger.debug("Delete Resource ok! job id is:[" + jobId + "]");

        return new Result();
    }


    /**
	 *update resource
	 *@param jobId id of the job
	 *@param resource resource
	 *@return Result of process
	 *@since crossdomain 0.5 2016-3-18
	 */	
    @Override
    public Result update(@NotNull String jobId, @NotNull Resource resource) {
        if((jobId == null) || (resource == null)) {
            logger.info("Failed to update resource.The jobId or resource is null.");
            return new Result(Result.FAIL, "The jobId or resource is null.", "");
        }

        ResourceForDB dbModel = resource.convertToDBModel(jobId, Constants.NULL_STR);
        ResMappingMapper sdMapper = getMapperManager(ResMappingMapper.class);
        try {
            sdMapper.update(dbModel);
        } catch(PersistenceException e) {
            logger.error(e.toString());
            return new Result(Result.FAIL, e.getMessage(), resource.getName());
        }

        logger.info("Update Resource ok! Resource id is:[" + resource.getKey() + "]");

        return new Result();
    }

	
    /**
	 *delete asynchronous resource status
	 *@param jobId id of the job
	 *@param resource resource
	 *@return Result of process
	 *@since crossdomain 0.5 2016-3-18
	 */
    @Override
    public Result updateAsyncStatus(@NotNull String jobId, @NotNull Resource resource) {
        ResourceForDB dbModel = resource.convertToDBModel(jobId, Constants.NULL_STR);
        ResMappingMapper sdMapper = getMapperManager(ResMappingMapper.class);
        try {
            sdMapper.updateAsyncStatus(dbModel);
        } catch(PersistenceException e) {
            logger.error(e.toString());
            return new Result(Result.FAIL, e.getMessage(), resource.getName());
        }

        logger.info("UpdateAsyncStatus Resource ok! Resource id is:[" + resource.getKey() + "]");

        return new Result();
    }

	
    /**
	 *get all resource of the service job
	 *@param jobId id of the job
	 *@return List of resource
	 *@throws PersistenceException when fail to getAllResourcesOfJob
	 *@since crossdomain 0.5 2016-3-18
	 */
    @Override
    public @NotNull List<Resource> getAllResourcesOfJob(@NotNull String jobId) throws PersistenceException {
        ResMappingMapper sdMapper = getMapperManager(ResMappingMapper.class);
        List<ResourceForDB> dbResources = sdMapper.getAllResourcesOfJob(jobId);
        logger.info("Get all Resouces of Job OK!");

        if(dbResources == null) {
            return Collections.emptyList();
        }
        List<Resource> allResources = convertToServiceModel(dbResources);
        logger.info("Get all Jobs OK!");
        return allResources;
    }

    private @NotNull List<Resource> convertToServiceModel(@NotNull List<ResourceForDB> dbResources) {
        List<Resource> allRes = new ArrayList<Resource>(dbResources.size());
        for(ResourceForDB dbResource : dbResources) {
            Resource resource = null;
            try {
                String decryptStr = CommonUtil.decrypt(dbResource.getResContent());
                resource = Resource.toResource(dbResource.getKey(), JSONObject.fromObject(decryptStr));
            } catch(ServiceException e) {
                logger.error("Json2ModelUtil.toResource fail, {}", dbResource.getKey());
                continue;
            }

            resource.setStatus(dbResource.getStatus());

            Result result = null;
            if((dbResource.getErrorCode() != null) && (dbResource.getResultReason() != null)) {
                result = new Result(dbResource.getErrorCode(), dbResource.getResultReason(), resource.getName());
            }
            resource.setResult(result);

            resource.setQueryUrl(dbResource.getQueryUrl());

            allRes.add(resource);
        }
        return allRes;
    }
}
