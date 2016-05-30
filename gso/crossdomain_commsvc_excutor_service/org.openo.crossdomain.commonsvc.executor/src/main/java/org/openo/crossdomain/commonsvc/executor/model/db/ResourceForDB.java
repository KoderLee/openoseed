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
package org.openo.crossdomain.commonsvc.executor.model.db;

import org.openo.crossdomain.commonsvc.executor.common.check.CheckAttr;
import org.openo.crossdomain.commonsvc.executor.common.check.CheckType;
import org.openo.crossdomain.commonsvc.executor.common.check.ModelChecker;

public class ResourceForDB extends ResourceCommon {

    @CheckAttr(type = CheckType.String, required = true, min = 1, max = ModelChecker.UUID_MAX_LENGTH)
    private String jobId;

    private String serviceId;

    private String dependson;

    public ResourceForDB(String jobId, String serviceId, String key) {
        this.jobId = jobId;
        this.serviceId = serviceId;
        this.key = key;
    }

    /**
     * @return Returns the jobId.
     */
    public String getJobId() {
        return jobId;
    }

    /**
     * @return Returns the serviceId.
     */
    public String getServiceId() {
        return serviceId;
    }

    /**
     * @return Returns the dependson.
     */
    public String getDependson() {
        return dependson;
    }

    /**
     * @param dependson The dependson to set.
     */
    public void setDependson(String dependson) {
        this.dependson = dependson;
    }
}
