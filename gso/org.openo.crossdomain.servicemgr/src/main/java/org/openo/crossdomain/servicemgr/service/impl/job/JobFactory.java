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
package org.openo.crossdomain.servicemgr.service.impl.job;

import java.util.UUID;

import org.openo.crossdomain.servicemgr.model.servicemo.ServiceModel;

import org.openo.baseservice.encrypt.cbb.CipherException;
import org.openo.baseservice.encrypt.cbb.CipherManager;
import org.openo.baseservice.encrypt.cbb.KeyType;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.crossdomain.commsvc.common.util.jsonutil.JsonUtil;
import org.openo.crossdomain.commsvc.jobscheduler.model.JobBean;

/**
 * Job Factory<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
public class JobFactory {

	/**
	 * Initialize job.<br/>
	 *
	 * @param serviceModel service
	 * @return job bean
	 * @throws ServiceException
	 * @since crossdomain 0.5
	 */
    public static JobBean createInitJob(ServiceModel serviceModel) throws ServiceException {
        InitJobBean bean = new InitJobBean();

        bean.setServiceModel(serviceModel);

        JobBean job = new JobBean();
        job.setType("INIT");
        job.setId(UUID.randomUUID().toString());
        job.setVersion(0);
        String context = JsonUtil.marshal(bean);
        try {
            char ct[] = CipherManager.getInstance().encrypt(context.toCharArray(), KeyType.SHARE, "common_shared");
            job.setContext(new String(ct));
        } catch(CipherException e) {
            throw new ServiceException(e);
        }

        return job;
    }

    /**
     * Initialize data of job bean.<br/>
     * 
     * @author
     * @version crossdomain 0.5 2016-3-19
     */
    public static class InitJobBean {

        private ServiceModel serviceModel;

        public ServiceModel getServiceModel() {
            return serviceModel;
        }

        public void setServiceModel(ServiceModel serviceModel) {
            this.serviceModel = serviceModel;
        }
    }

}
