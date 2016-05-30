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
package org.openo.crossdomain.commonsvc.executor.model;

import java.util.*;

import org.openo.commonservice.log.OssLog;
import org.openo.commonservice.log.OssLogFactory;
import org.openo.commonservice.remoteservice.exception.ServiceException;

import net.sf.json.JSONObject;

import org.openo.crossdomain.commonsvc.executor.common.check.CheckAttr;
import org.openo.crossdomain.commonsvc.executor.common.check.CheckType;
import org.openo.crossdomain.commonsvc.executor.common.check.ModelChecker;
import org.openo.crossdomain.commonsvc.executor.common.constant.Constants;
import org.openo.crossdomain.commonsvc.executor.common.constant.ErrorMessage;
import org.openo.crossdomain.commonsvc.executor.common.constant.RequestJsonConstants;
import org.openo.crossdomain.commonsvc.executor.common.enums.ActionType;
import org.openo.crossdomain.commonsvc.executor.model.db.Result;
import org.openo.crossdomain.commonsvc.executor.model.util.Json2ModelUtil;
import org.openo.crossdomain.commonsvc.executor.model.util.Model2JsonUtil;
import org.springframework.util.StringUtils;

public class ServiceInfo {

    private static final OssLog log = OssLogFactory.getLogger(ServiceInfo.class);

    @CheckAttr(type = CheckType.String, required = true, allowNull = true, min = 1, max = ModelChecker.UUID_MAX_LENGTH)
    private String jobId;

    private String tenantId = Constants.NULL_STR;

    @CheckAttr(type = CheckType.String, required = true, min = 1, max = ModelChecker.UUID_MAX_LENGTH)
    private String serviceId;

    private String serviceName;

    private ActionType action;

    private boolean isDryrun;

    private String onFailure;

    private Map<String, Resource> resources;

    private List<ServiceInfo> subServices;

    private Result result;

    public static ServiceInfo toService(JSONObject serviceObj) {
        if(serviceObj == null) {
            return null;
        }

        JSONObject resourceObj = Json2ModelUtil.getJsonObject(serviceObj, RequestJsonConstants.Service.resources);

        Map<String, Resource> resourceMap;
        try {
            resourceMap = toResourceMap(resourceObj);
        } catch(ServiceException e) {
            return null;
        }

        boolean isDryrun = false;
        try {
            isDryrun =
                    Json2ModelUtil.jsonToDryrun(Json2ModelUtil.getString(serviceObj,
                            RequestJsonConstants.Service.isDryrun));
        } catch(ServiceException e) {
            return null;
        }

        String rollback = Constants.NULL_STR;
        try {
            rollback =
                    Json2ModelUtil.jsonToOnfailure(Json2ModelUtil.getString(serviceObj,
                            RequestJsonConstants.Service.onFailure));
        } catch(ServiceException e) {
            return null;
        }

        ActionType action = ActionType.toAction(serviceObj);
        if(action == null) {
            String msg = String.format(ErrorMessage.SERVICE_FIELD_INVALID_MSG, RequestJsonConstants.action);
            log.error(msg);
            return null;
        }

        ServiceInfo service = new ServiceInfo();
        service.setServiceId(Json2ModelUtil.getString(serviceObj, RequestJsonConstants.Service.serviceId));
        service.setServiceName(Json2ModelUtil.getString(serviceObj, RequestJsonConstants.Service.serviceName));
        service.setAction(action);
        service.setIsDryrun(isDryrun);
        service.setOnFailure(rollback);

        service.setResources(resourceMap);

        return service;
    }

    public static ServiceInfo toServiceExecuteRsp(JSONObject rspObj) {
        if(rspObj == null) {
            return null;
        }

        Map<String, Resource> resourceMap;
        try {
            JSONObject resourceObj = Json2ModelUtil.getJsonObject(rspObj, RequestJsonConstants.Service.resources);

            resourceMap = toResourceMap(resourceObj);
        } catch(ServiceException e) {
            return null;
        }

        String serviceId = Json2ModelUtil.getString(rspObj, RequestJsonConstants.Service.serviceId);
        if(!StringUtils.hasLength(serviceId)) {
            String msg = String.format(ErrorMessage.JSON_CONVERT_NULL_MSG, RequestJsonConstants.Service.serviceId);
            log.error(msg);
            return null;
        }

        ServiceInfo service = new ServiceInfo();
        service.setServiceId(serviceId);
        service.setServiceName(Json2ModelUtil.getString(rspObj, RequestJsonConstants.Service.serviceName));
        service.setAction(ActionType.toAction(rspObj));

        service.setResources(resourceMap);

        return service;
    }

    public static ServiceInfo getServiceFromAsyncResult(JSONObject rspObj) {
        String serviceJson = Json2ModelUtil.getString(rspObj, RequestJsonConstants.data);

        if(!StringUtils.hasLength(serviceJson)) {
            return null;
        }

        return toService(JSONObject.fromObject(serviceJson));
    }

    public static Map<String, Resource> toResourceMap(JSONObject resourceObj) throws ServiceException {
        if(resourceObj == null) {
            return Collections.emptyMap();
        }

        Map<String, Resource> resourceMap = new HashMap<>();

        Iterator it = resourceObj.keys();
        while(it.hasNext()) {
            String key = (String)it.next();

            resourceMap.put(key, Resource.toResource(key, resourceObj));
        }

        return resourceMap;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public ActionType getAction() {
        return action;
    }

    public void setAction(ActionType action) {
        this.action = action;
    }

    public boolean getIsDryrun() {
        return isDryrun;
    }

    public void setIsDryrun(boolean isDryrun) {
        this.isDryrun = isDryrun;
    }

    public String getOnFailure() {
        return onFailure;
    }

    public void setOnFailure(String onFailure) {
        this.onFailure = onFailure;
    }

    public Map<String, Resource> getResources() {
        return resources;
    }

    public void setResources(Map<String, Resource> resources) {
        this.resources = resources;
    }

    public List<ServiceInfo> getSubServices() {
        return subServices;
    }

    public void setSubServices(List<ServiceInfo> subServices) {
        this.subServices = subServices;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public Set<String> getResourceKey() {
        if(resources == null) {
            return null;
        }

        Set<String> labelSet = new HashSet<>();
        for(Map.Entry<String, Resource> entry : resources.entrySet()) {
            labelSet.add(entry.getKey());
        }

        return labelSet;
    }

    public void setBasicInfo(ServiceInfo basicService) {
        jobId = basicService.getJobId();
        tenantId = basicService.getTenantId();
        serviceId = basicService.getServiceId();
        action = basicService.getAction();
        serviceName = basicService.getServiceName();
    }

    public String generateServiceExecuteContent() {
        JSONObject contentObj = new JSONObject();
        contentObj.put(RequestJsonConstants.Service.serviceId, getServiceId());
        contentObj.put(RequestJsonConstants.Service.serviceName, getServiceName());

        contentObj.put(RequestJsonConstants.action, getAction().toString());

        final JSONObject resourceObj = resourceMap2Json(getResources(), false);
        if(resourceObj != null) {
            Model2JsonUtil.putKeyValue(contentObj, RequestJsonConstants.Service.resources, resourceObj.toString());
        }

        return contentObj.toString();
    }

    public JSONObject serviceRsp2Json() {
        JSONObject serviceObj = serviceBase2Json();

        if(result != null) {
            JSONObject resultObj = result.result2Json();
            if(resultObj != null) {
                Model2JsonUtil.putKeyValue(serviceObj, RequestJsonConstants.RESULT, resultObj.toString());
            }
        }

        JSONObject resourceObj = resourceMap2Json(getResources(), true);
        if(resourceObj != null) {
            Model2JsonUtil.putKeyValue(serviceObj, RequestJsonConstants.Service.resources, resourceObj.toString());
        }

        return serviceObj;
    }

    public JSONObject serviceBase2Json() {
        JSONObject serviceObj = new JSONObject();

        Model2JsonUtil.putKeyValue(serviceObj, RequestJsonConstants.Service.serviceId, getServiceId());
        if(result != null) {
            JSONObject resultObj = result.result2Json();
            if(resultObj != null) {
                Model2JsonUtil.putKeyValue(serviceObj, RequestJsonConstants.RESULT, resultObj.toString());
            }
        }

        return serviceObj;
    }

    public JSONObject resourceMap2Json(Map<String, Resource> resourceMap, boolean bWithStatus) {
        if(resourceMap == null) {
            return null;
        }

        JSONObject mapObj = new JSONObject();

        for(Map.Entry<String, Resource> entry : resourceMap.entrySet()) {
            JSONObject resourceObj = JSONObject.fromObject(entry.getValue().resource2Json(bWithStatus));
            Iterator it = resourceObj.keys();
            while(it.hasNext()) {
                String key = (String)it.next();

                mapObj.put(key, Json2ModelUtil.getJsonObject(resourceObj, key).toString());
            }
        }

        return mapObj;
    }

}
