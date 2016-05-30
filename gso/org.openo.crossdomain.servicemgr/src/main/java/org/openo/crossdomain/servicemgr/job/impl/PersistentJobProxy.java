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
package org.openo.crossdomain.servicemgr.job.impl;

import java.util.HashMap;
import java.util.Map;

import org.openo.crossdomain.servicemgr.util.service.SpringContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.openo.crossdomain.commsvc.jobscheduler.job.IPersistentJob;
import org.openo.crossdomain.commsvc.jobscheduler.job.StepPolicy;
import org.openo.crossdomain.commsvc.jobscheduler.model.JobBean;

/**
 * Proxy for persistent job.<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
public class PersistentJobProxy implements IPersistentJob {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * job bean
     */
    private JobBean jobBean;

    private static Map<String, String> JOB_MAP = new HashMap<String, String>();
    static {
        JOB_MAP.put("CREATEQUERY", "createQueryProgressJob");
        JOB_MAP.put("UPDATEQUERY", "updateQueryProgressJob");
        JOB_MAP.put("ACTIVEQUERY", "activeQueryProgressJob");
        JOB_MAP.put("DEACTIVQUERY", "deactivQueryProgressJob");
        JOB_MAP.put("DELETEQUERY", "deleteQueryProgressJob");
    }

    /**
     * Constructor. <br/>
     * @param jobBean job bean
     */
    public PersistentJobProxy(JobBean jobBean) {
        this.jobBean = jobBean;
    }

    /**
     * Start operation policy.<br/>
     *
     * @return operation policy
     * @since crossdomain 0.5
     */
    @Override
    public StepPolicy run() {

        String beanID = JOB_MAP.get(jobBean.getType());

        AbsPersistentJob persistentJob = (AbsPersistentJob)SpringContextUtils.getBeanById(beanID);

        if(persistentJob == null) {
            logger.error("can not found job processor! type:" + jobBean.getType());
            return null;
        }
        persistentJob.setJobBean(jobBean);

        return persistentJob.run();
    }
}
