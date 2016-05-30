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
package org.openo.crossdomain.servicemgr.model.roamo;

import java.util.Map;

import org.openo.baseservice.remoteservice.exception.ServiceException;

/**
 * Response for service model with parameters.<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
public class ServiceModelRspInfo extends BaseServiceModelRspInfo {

    private Map<String, Object> parameters;

    public ServiceModelRspInfo() {
        super();
    }

    public static ServiceModelRspInfo getServiceModelRspInfo(BaseServiceModelRspInfo baseServiceModelRspInfo,
            Map<String, Object> parameters) {
        return new ServiceModelRspInfo(baseServiceModelRspInfo, parameters);
    }

    public ServiceModelRspInfo(BaseServiceModelRspInfo baseServiceModelRspInfo, Map<String, Object> parameters) {
        super(baseServiceModelRspInfo);
        this.parameters = parameters;

    }

    /**
     * @return Returns the parameters.
     */
    public Map<String, Object> getParameters() {
        return parameters;
    }

    /**
     * @param parameters The parameters to set.
     */
    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

}
