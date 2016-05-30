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

import org.openo.crossdomain.commonsvc.executor.common.check.ModelChecker;
import org.openo.crossdomain.commonsvc.executor.common.exception.PreprocessException;
import org.openo.crossdomain.commonsvc.executor.model.ServiceJob;
import org.openo.crossdomain.commonsvc.executor.service.inf.IPreprocessor;

import org.openo.commonservice.remoteservice.exception.ServiceException;

public class BasicCheckerForJob implements IPreprocessor<ServiceJob> {

    /**
	 *validate ServiceJob
	 *@param job ServiceJob to be validated
	 *@throws ServiceException when fail to validate ServiceJob
	 *@since crossdomain 0.5 2016-3-18
	 */
    public static void validate(ServiceJob job) throws ServiceException {

        ModelChecker.validateModel(job);

        BasicCheckerForService.validate(job);
    }

    private static void throwCheckException(ServiceJob job, String msg) throws PreprocessException {
        throw new PreprocessException(msg, job);
    }
	/**
	 *ServiceJob Preprocess
	 *@param job ServiceJob to be checked
	 *@throws PreprocessException when Resource Preprocess is failed
	 *@since crossdomain 0.5 2016-3-18
	 */
    public void preprocess(ServiceJob job) throws PreprocessException {
        try {
            validate(job);
        } catch(ServiceException e) {
            throwCheckException(job, e.getMessage());
        }
    }
}
