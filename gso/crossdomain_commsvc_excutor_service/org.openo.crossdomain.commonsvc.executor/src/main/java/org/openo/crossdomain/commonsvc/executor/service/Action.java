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
package org.openo.crossdomain.commonsvc.executor.service;

import java.util.HashMap;
import java.util.Map;

import org.openo.crossdomain.commonsvc.executor.model.Resource;
import org.openo.crossdomain.commonsvc.executor.model.ServiceInfo;

public class Action implements Runnable {

    private ServiceInfo basicService;

    private Resource resource;

    public Action(Resource resource, ServiceInfo basicService) {
        this.resource = resource;
        this.basicService = basicService;
    }

    @Override
    public void run() {
        ServiceInfo service = new ServiceInfo();
        Map<String, Resource> resourceMap = new HashMap<>();
        resourceMap.put(resource.getKey(), resource);
        service.setResources(resourceMap);

        service.setBasicInfo(basicService);

        ExecuteServiceController controller = new ExecuteServiceController(service);
        controller.process();
    }

    public Resource getResource() {
        return resource;
    }

    public String getJobId() {
        return basicService.getJobId();
    }
}
