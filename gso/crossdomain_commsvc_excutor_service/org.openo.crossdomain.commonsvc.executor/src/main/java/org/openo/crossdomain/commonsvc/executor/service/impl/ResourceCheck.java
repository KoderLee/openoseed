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

import org.openo.crossdomain.commonsvc.executor.common.check.ModelChecker;
import org.openo.crossdomain.commonsvc.executor.common.constant.Constants;
import org.openo.crossdomain.commonsvc.executor.common.constant.ErrorMessage;
import org.openo.crossdomain.commonsvc.executor.common.enums.ActionType;
import org.openo.crossdomain.commonsvc.executor.common.exception.PreprocessException;
import org.openo.crossdomain.commonsvc.executor.model.Resource;
import org.openo.crossdomain.commonsvc.executor.model.ServiceJob;
import org.openo.crossdomain.commonsvc.executor.service.inf.IPreprocessor;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ResourceCheck implements IPreprocessor<ServiceJob> {

    private static final OssLog log = OssLogFactory.getLogger(ResourceCheck.class);

	/**
	 *check Resource Id
	 *@param resource Resource to be checked
	 *@throws PreprocessException when fail to check the resourceId
	 *@since crossdomain 0.5 2016-3-18
	 */
    static public void checkResourceId(Resource resource) throws PreprocessException {

        if(ActionType.CREATE != resource.getOperType() && !checkUuid(resource.getId())) {
            String msg = String.format(ErrorMessage.RESOURCE_FIELD_INVALID_MSG, "id");
            log.info(msg);
            throwCheckException(resource, msg);
        }
    }

	/**
	 *check Resource Depender
	 *@param resource Resource to be checked
	 *@throws PreprocessException when Resource Depender is too long
	 *@since crossdomain 0.5 2016-3-18
	 */
    static public void checkDependerTooLong(Resource resource) throws PreprocessException {

        if(resource.dependonToString().length() > ModelChecker.STRING_MAX_LENGTH) {
            String msg = String.format(ErrorMessage.LENGTH_TOO_LONG_MSG, "resource depender");
            log.info(msg);
            throwCheckException(resource, msg);
        }

        for(String depend : resource.getDependon()) {
            if(depend.length() > ModelChecker.STRING_MAX_LENGTH) {
                String msg = String.format(ErrorMessage.LENGTH_TOO_LONG_MSG, "resource depender");
                log.info(msg);
                throwCheckException(resource, msg);
            }
        }
    }
	/**
	 *check Resource Depender
	 *@param resource Resource to be checked
	 *@throws PreprocessException when Resource Depender is repeat
	 *@since crossdomain 0.5 2016-3-18
	 */
    static public void checkDependerRepeat(Resource resource) throws PreprocessException {
        final List<String> depends = resource.getDependon();
        Set<String> dependSet = new HashSet<>();

        for(String depender : depends) {
            boolean bNotExist = dependSet.add(depender);
            if(!bNotExist) {
                String msg = String.format(ErrorMessage.DEPENDER_REPEAT_MSG, resource.getName(), depender);
                log.info(msg);
                throwCheckException(resource, msg);
            }
        }
    }

	/**
	 *check UUID
	 *@param uuid uuid to be checked
	 *@return check result
	 *@since crossdomain 0.5 2016-3-18
	 */
    static public boolean checkUuid(String uuid) {
        boolean valid = false;
        if(!StringUtils.hasLength(uuid)) {
            return valid;
        }

        if(uuid.matches(Constants.SHORT_UUID_MATCH) || uuid.matches(Constants.LONG_UUID_MATCH)) {
            valid = true;
        }

        return valid;
    }

    private static void throwCheckException(Resource resource, String msg) throws PreprocessException {
        throw new PreprocessException(msg, resource);
    }

    /**
	 *check Resource Depender
	 *@param resource Resource to be checked
	 *@param resourceLabelSet Set<String> resourceKey Set
	 *@throws PreprocessException when Resource Depender is invalid
	 *@since crossdomain 0.5 2016-3-18
	 */
    static public void checkDependerValid(Resource resource, Set<String> resourceLabelSet) throws PreprocessException {
        final List<String> dependList = resource.getDependon();
        for(String depender : dependList) {
            if(!resourceLabelSet.contains(depender)) {
                String msg = String.format(ErrorMessage.NOT_DEPENDER_MSG, resource.getName(), depender);
                log.info(msg);
                throwCheckException(resource, msg);
            }
        }
    }
    /**
	 *Resource Preprocess
	 *@param job ServiceJob to be checked
	 *@throws PreprocessException when Resource Preprocess is failed
	 *@since crossdomain 0.5 2016-3-18
	 */
	
    @Override
    public void preprocess(ServiceJob job) throws PreprocessException {
        for(Resource resource : job.getService().getResources().values()) {
            // checkResourceId(resource);
            checkDependerTooLong(resource);
            checkDependerRepeat(resource);
        }
    }
}
