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
package org.openo.crossdomain.commonsvc.executor.service.impl;

import org.openo.commonservice.log.OssLog;
import org.openo.commonservice.log.OssLogFactory;

import org.openo.crossdomain.commonsvc.executor.common.constant.ErrorMessage;
import org.openo.crossdomain.commonsvc.executor.common.exception.PreprocessException;
import org.openo.crossdomain.commonsvc.executor.model.Resource;
import org.openo.crossdomain.commonsvc.executor.model.ServiceInfo;
import org.openo.crossdomain.commonsvc.executor.model.ServiceJob;
import org.openo.crossdomain.commonsvc.executor.service.inf.IPreprocessor;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.Set;

public class ServiceCheck implements IPreprocessor<ServiceJob> {

    private static final OssLog log = OssLogFactory.getLogger(ServiceCheck.class);

	/**
	 *check ServiceId
	 *@param service ServiceInfo to be checked
	 *@since crossdomain 0.5 2016-3-18
	 */
    static public void checkServiceId(ServiceInfo service) throws PreprocessException {
        if(!ResourceCheck.checkUuid(service.getServiceId())) {
            String msg = String.format(ErrorMessage.SERVICE_FIELD_UNMATCH_MSG, service.getServiceName(), "id");
            log.info(msg);
            throwCheckException(service, msg);
        }
    }

    private static void throwCheckException(ServiceInfo service, String msg) throws PreprocessException {
        throw new PreprocessException(msg, service);
    }

    /**
	 * Preproce the ServiceJob(validation)
	 *@param service ServiceInfo to be checked
	 *@since crossdomain 0.5 2016-3-18
	 */
    public void preprocess(ServiceJob job) throws PreprocessException {
        checkDependerNotExist(job.getService());
    }

    private void checkDependerNotExist(ServiceInfo service) throws PreprocessException {
        if(CollectionUtils.isEmpty(service.getResources())) {
            return;
        }

        final Set<String> labelSet = service.getResourceKey();

        for(Map.Entry<String, Resource> entry : service.getResources().entrySet()) {
            ResourceCheck.checkDependerValid(entry.getValue(), labelSet);
        }
    }

}
