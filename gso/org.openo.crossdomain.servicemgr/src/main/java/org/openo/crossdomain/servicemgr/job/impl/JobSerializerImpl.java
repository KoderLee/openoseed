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

import org.openo.crossdomain.commsvc.jobscheduler.job.IJobSerializer;
import org.openo.crossdomain.commsvc.jobscheduler.job.IPersistentJob;
import org.openo.crossdomain.commsvc.jobscheduler.model.JobBean;

/**
 * Job serialize.<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
public class JobSerializerImpl implements IJobSerializer {

	/**
	 * Unserialize.<br/>
	 *
	 * @param job job bean
	 * @return persistent job
	 * @since crossdomain 0.5
	 */
    public IPersistentJob unSerialize(JobBean job) {
        PersistentJobProxy jobProxy = new PersistentJobProxy(job);
        return jobProxy;
    }
}
