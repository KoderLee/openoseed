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
import org.openo.crossdomain.commsvc.jobscheduler.job.StepPolicy;
import org.openo.crossdomain.commsvc.jobscheduler.model.JobBean;

import java.util.HashMap;
import java.util.Map;

public class PersistentJobProxy extends AbsPersistentJob {

    private static Map<String, Class<?>> JOB_MAP = new HashMap<String, Class<?>>();

    private final OssLog logger = OssLogFactory.getLog(PersistentJobProxy.class);

    public PersistentJobProxy(final JobBean jobBean) {
        this.jobBean = jobBean;
    }

    @Override
    public StepPolicy run() {
        Class<?> persistentJobCls = JOB_MAP.get(jobBean.getType());
        if(persistentJobCls == null) {
            logger.error("can not found job processor! type:" + jobBean.getType());
            return null;
        }

        AbsPersistentJob persistentJob;
        try {
            persistentJob = (AbsPersistentJob)persistentJobCls.newInstance();
            persistentJob.setJobBean(jobBean);
            return persistentJob.run();
        } catch(InstantiationException | IllegalAccessException e) {
            logger.error(e.getMessage());

            getScheduler().deleteJob(jobBean.getId());

            return StepPolicy.createDeletedPolicy();
        }
    }
}
