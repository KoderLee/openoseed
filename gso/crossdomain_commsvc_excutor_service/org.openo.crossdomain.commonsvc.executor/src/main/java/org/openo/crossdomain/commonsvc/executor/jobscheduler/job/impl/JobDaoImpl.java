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

import org.openo.commonservice.mybatis.session.MapperManager;
import org.openo.crossdomain.commsvc.jobscheduler.dao.inf.IJobDao;
import org.openo.crossdomain.commsvc.jobscheduler.dao.mapper.IJobMapper;
import org.openo.crossdomain.commsvc.jobscheduler.model.JobBean;

import org.openo.crossdomain.commonsvc.executor.common.constant.Constants;
import org.openo.crossdomain.commonsvc.executor.common.util.CommonUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import java.util.Collection;

@Component(value = "jobDaoRef")
public class JobDaoImpl implements IJobDao {

    private static final int ADOPT_LIMIT = 100;

    @Resource
    private MapperManager mapperMgrRef;

    private String thisInstanceID;

    /**
	 *adoptJobs
	 *@param otherDeadInstanceID instanceId
	 *@param limit limit number
	 *@param JobBean Collection
	 *@since crossdomain 0.5 2016-3-18
	 */
    @Override
    public Collection<JobBean> adoptJobs(String otherDeadInstanceID, int limit) {
        final long now = CommonUtil.getTimeInMillis();

        AdoptInstanceMgr adoptInstanceMgr = new AdoptInstanceMgr();
        adoptInstanceMgr.adoptInstance(otherDeadInstanceID, now);

        IJobMapper mapper = getMapperManager(IJobMapper.class);

        mapper.adoptJobs(this.thisInstanceID, otherDeadInstanceID, now);
        return mapper.loadAdoptJobs(this.thisInstanceID, now, limit);
    }

	/**
	 *delete Job
	 *@param jobID ID of the job
	 *@since crossdomain 0.5 2016-3-18
	 */
    @Override
    public void delete(String jobID) {
        IJobMapper mapper = getMapperManager(IJobMapper.class);
        mapper.delete(jobID);
    }

    /**
	 *insert Job
	 *@param job jobbean of the job
	 *@since crossdomain 0.5 2016-3-18
	 */
    @Override
    public void insert(JobBean job) {
        IJobMapper mapper = getMapperManager(IJobMapper.class);
        mapper.insert(job);
    }
	
	/**
	 *load Last Scheduled Jobs 
	 *@param JobBean Collection
	 *@since crossdomain 0.5 2016-3-18
	 */

    @Override
    public Collection<JobBean> loadLastScheduledJobs() {
        final long now = System.currentTimeMillis();

        IJobMapper mapper = getMapperManager(IJobMapper.class);

        mapper.adoptJobs(this.thisInstanceID, this.thisInstanceID, now);

        return mapper.loadAdoptJobs(this.thisInstanceID, now, ADOPT_LIMIT);
    }

    /**
	 *update Job
	 *@param job jobbean of the job
	 *@param fields other parameters
	 *@since crossdomain 0.5 2016-3-18
	 */
    @Override
    public void update(JobBean job, String... fields) {
        IJobMapper mapper = getMapperManager(IJobMapper.class);
        mapper.update(job);
    }

    /**
     * @return Returns the mapperManager.
     */
    private <T> T getMapperManager(Class<T> type) {
        return mapperMgrRef.getMapper(type, Constants.SERVICE_EXECUTOR_DB);
    }

    /**
     * @param mapperManager The mapperManager to set.
     */
    public void setMapperManager(MapperManager mapperManager) {
        this.mapperMgrRef = mapperManager;
    }

    /**
     * @return Returns the thisInstanceID.
     */
    public String getThisInstanceID() {
        return this.thisInstanceID;
    }

    /**
     * @param thisInstanceID The thisInstanceID to set.
     */
    public void setThisInstanceID(String thisInstanceID) {
        this.thisInstanceID = thisInstanceID;
    }
}
