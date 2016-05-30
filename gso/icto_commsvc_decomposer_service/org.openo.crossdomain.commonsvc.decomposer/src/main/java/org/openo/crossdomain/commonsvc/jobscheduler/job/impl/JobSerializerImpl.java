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
package org.openo.crossdomain.commonsvc.jobscheduler.job.impl;

import com.huawei.icto.commsvc.jobscheduler.job.IJobSerializer;
import com.huawei.icto.commsvc.jobscheduler.job.IPersistentJob;
import com.huawei.icto.commsvc.jobscheduler.model.JobBean;

/**
 * the implement class for Job Serializer
 * 
 * @since crossdomain 0.5
 */
public class JobSerializerImpl implements IJobSerializer {

	/**
	 * Deserialize the job.<br>
	 * 
	 * @param job job
	 * @return
	 * @since crossdomain 0.5
	 */
	@Override
	public IPersistentJob unSerialize(JobBean job) {
		PersistentJobProxy jobProxy = new PersistentJobProxy(job);

		return jobProxy;
	}
}
