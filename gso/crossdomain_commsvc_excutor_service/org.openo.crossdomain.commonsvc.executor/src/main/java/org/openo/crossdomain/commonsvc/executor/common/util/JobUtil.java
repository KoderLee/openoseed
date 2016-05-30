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
package org.openo.crossdomain.commonsvc.executor.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openo.crossdomain.commonsvc.executor.common.enums.ActionType;
import org.openo.crossdomain.commonsvc.executor.common.enums.ExecutionStatus;
import org.openo.crossdomain.commonsvc.executor.model.Resource;
import org.openo.crossdomain.commonsvc.executor.model.ServiceJob;

public class JobUtil {

    private static JobUtil instance;

    private Map<String, Set<String>> resourceOfDecomposedSet = new HashMap<>();

    public synchronized static JobUtil getInstance() {
        if(instance == null) {
            instance = new JobUtil();
        }
        return instance;
    }

    /**
     * get All Depended Resources By Action
	 *@param serviceJob Service Job
	 *@param resource resource to be send to plugin
	 *@return Resource list
	 *@since crossdomain 0.5 2016-3-18
	 */
    public synchronized static List<Resource> getAllDependedResourcesByAction(ServiceJob serviceJob, Resource resource) {
        List<Resource> resourceList = new ArrayList<Resource>();
        if(serviceJob.getService().getAction() != ActionType.DELETE) {
            resourceList.addAll(getAllDescendantResources(serviceJob, resource));
        } else {
            resourceList.addAll(getAllAscendantResources(serviceJob, resource));
        }
        return resourceList;
    }

	/**
     * get All Descendant Resources By Action for Delete
	 *@param serviceJob Service Job
	 *@param fatherResource resource to be send to plugin
	 *@return Resource list
	 *@since crossdomain 0.5 2016-3-18
	 */
    public synchronized static List<Resource> getAllDescendantResources(ServiceJob serviceJob, Resource fatherResource) {
        List<Resource> resourceList = new ArrayList<Resource>();
        List<Resource> fatherResources = new ArrayList<Resource>();
        fatherResources.add(fatherResource);
        getAllDescendantResources(serviceJob, fatherResources, resourceList);
        return resourceList;
    }

    /**
     * get All Ascendant Resources By Action for create
	 *@param serviceJob Service Job
	 *@param childResource resource to be send to plugin
	 *@return Resource list
	 *@since crossdomain 0.5 2016-3-18
	 */
    public synchronized static List<Resource> getAllAscendantResources(ServiceJob serviceJob, Resource childResource) {
        List<Resource> resourceList = new ArrayList<Resource>();
        Set<String> existResourceKey = new HashSet<>();

        List<Resource> childList = new ArrayList<>();
        childList.add(childResource);
        getAllAscendantResources(existResourceKey, serviceJob, childList, resourceList);
        return resourceList;
    }

    private static void getAllAscendantResources(Set<String> existResourceKey, ServiceJob serviceJob,
            List<Resource> childResources, List<Resource> resourceList) {
        List<Resource> fatherResources = new ArrayList<Resource>();
        for(Resource childResource : childResources) {
            for(String dependOn : childResource.getDependon()) {
                for(Resource resource : serviceJob.getService().getResources().values()) {
                    if(!existResourceKey.contains(resource.getKey()) && dependOn.equals(resource.getKey())) {
                        fatherResources.add(resource);
                        existResourceKey.add(resource.getKey());
                    }
                }
            }
        }

        resourceList.addAll(fatherResources);

        if(!fatherResources.isEmpty()) {
            getAllAscendantResources(existResourceKey, serviceJob, fatherResources, resourceList);
        }
    }

    private static void getAllDescendantResources(ServiceJob serviceJob, List<Resource> fatherResources,
            List<Resource> resourceList) {
        List<Resource> childResources = new ArrayList<Resource>();
        for(Resource fatherResource : fatherResources) {
            for(Resource resource : serviceJob.getService().getResources().values()) {
                if(resource.getDependon().contains(fatherResource.getKey())) {
                    childResources.add(resource);
                }
            }
        }
        resourceList.addAll(childResources);

        if(!childResources.isEmpty()) {
            getAllDescendantResources(serviceJob, childResources, resourceList);
        }
    }
	/**
     * get All Child Resources
	 *@param serviceJob Service Job
	 *@param fatherResource resource to be send to plugin
	 *@return Resource list
	 *@since crossdomain 0.5 2016-3-18
	 */
    public synchronized static List<Resource> getAllChildResources(ServiceJob serviceJob, Resource fatherResource) {
        List<Resource> childResources = new ArrayList<Resource>();
        for(Resource resource : serviceJob.getService().getResources().values()) {
            if(resource.getDependon().contains(fatherResource.getKey())) {
                childResources.add(resource);
            }
        }
        return childResources;
    }
	/**
     * get All Father Resources
	 *@param serviceJob Service Job
	 *@param fatherResource resource to be send to plugin
	 *@return Resource list
	 *@since crossdomain 0.5 2016-3-18
	 */
    public synchronized static List<Resource> getAllFatherResources(ServiceJob serviceJob, Resource childResource) {
        List<Resource> fatherResources = new ArrayList<Resource>();
        for(String resourceKey : childResource.getDependon()) {
            for(Resource resource : serviceJob.getService().getResources().values()) {
                if(resourceKey.equals(resource.getKey())) {
                    fatherResources.add(resource);
                    break;
                }
            }
        }
        return fatherResources;
    }

    /**
     * get All Direct Depend Resources
	 *@param serviceJob Service Job
	 *@param fatherResource resource to be send to plugin
	 *@return Resource list
	 *@since crossdomain 0.5 2016-3-18
	 */
    public synchronized static List<Resource> getDirectDependResources(ServiceJob serviceJob, Resource resource) {
        List<Resource> resourceList = new ArrayList<>();
        if(serviceJob.getService().getAction() != ActionType.DELETE) {
            resourceList.addAll(getAllChildResources(serviceJob, resource));
        } else {
            resourceList.addAll(getAllFatherResources(serviceJob, resource));
        }

        return resourceList;
    }

	/**
     * whether jobList contains appointed job
	 *@param jobList Service Job List
	 *@param jobId job id to be judge
	 *@return boolean result
	 *@since crossdomain 0.5 2016-3-18
	 */
    public static boolean containsJob(List<ServiceJob> jobList, String jobId) {
        return getServiceJobById(jobList, jobId) != null;
    }

	/**
     * get Service Job by Job Id
	 *@param jobList Service Job List
	 *@param jobId job id to be judge
	 *@param Service Job required
	 *@since crossdomain 0.5 2016-3-18
	 */
    public static ServiceJob getServiceJobById(List<ServiceJob> jobList, String jobId) {
        if(jobList != null) {
            for(ServiceJob serviceJob : jobList) {
                if(jobId.equals(serviceJob.getJobId())) {
                    return serviceJob;
                }
            }
        }

        return null;
    }

	/**
     * get Executable Resources from Service Job
	 *@param serviceJob	 Service Job
	 *@return executable resources
	 *@since crossdomain 0.5 2016-3-18
	 */
    public synchronized List<Resource> getExecutableResources(ServiceJob serviceJob) {
        Set<String> resourceSet = resourceOfDecomposedSet.get(serviceJob.getJobId());
        if(resourceSet == null) {
            resourceSet = new HashSet<>();
            resourceOfDecomposedSet.put(serviceJob.getJobId(), resourceSet);
        }

        List<Resource> resourceList = new ArrayList<Resource>();
        for(Resource resource : serviceJob.getService().getResources().values()) {
            if(!resourceSet.contains(resource.getKey()) && (ExecutionStatus.INITEXECUTE == resource.getStatus())
                    && (resource.getDependon().isEmpty())) {
                resourceSet.add(resource.getKey());
                resourceList.add(resource);
            }
        }

        return resourceList;
    }

	/**
     * get Not Depended Resources from Service Job
	 *@param serviceJob	 Service Job
	 *@return not depended resources
	 *@since crossdomain 0.5 2016-3-18
	 */
    public synchronized List<Resource> getNotDependedResources(ServiceJob serviceJob) {
        Set<String> resourceSet = resourceOfDecomposedSet.get(serviceJob.getJobId());
        if(resourceSet == null) {
            resourceSet = new HashSet<>();
            resourceOfDecomposedSet.put(serviceJob.getJobId(), resourceSet);
        }

        Set<String> dependSet = new HashSet<>();
        for(Resource resource : serviceJob.getService().getResources().values()) {
            dependSet.addAll(resource.getDependon());
        }

        List<Resource> resourceList = new ArrayList<Resource>();
        for(Resource resource : serviceJob.getService().getResources().values()) {
            if(!resourceSet.contains(resource.getKey()) && ExecutionStatus.INITEXECUTE == resource.getStatus()
                    && !dependSet.contains(resource.getKey())) {
                resourceSet.add(resource.getKey());
                resourceList.add(resource);
            }
        }

        return resourceList;
    }

	/**
     * remove Resource from Decomposed Resource Set
	 *@param jobId service job id
	 *@since crossdomain 0.5 2016-3-18
	 */
    public synchronized void removeResourceOfDecomposed(String jobId) {
        resourceOfDecomposedSet.remove(jobId);
    }
}
