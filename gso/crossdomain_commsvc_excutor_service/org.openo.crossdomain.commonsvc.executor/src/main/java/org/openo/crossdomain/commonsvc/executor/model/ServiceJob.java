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

import org.openo.commonservice.log.OssLog;
import org.openo.commonservice.log.OssLogFactory;
import org.openo.commonservice.remoteservice.exception.ServiceException;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.codehaus.jackson.map.ObjectMapper;
import org.openo.crossdomain.commonsvc.executor.common.constant.Constants;
import org.openo.crossdomain.commonsvc.executor.common.constant.ErrorMessage;
import org.openo.crossdomain.commonsvc.executor.common.constant.RequestJsonConstants;
import org.openo.crossdomain.commonsvc.executor.common.enums.ExecutionStatus;
import org.openo.crossdomain.commonsvc.executor.common.util.CommonUtil;
import org.openo.crossdomain.commonsvc.executor.model.db.Result;
import org.openo.crossdomain.commonsvc.executor.model.db.ServiceJobCommon;
import org.openo.crossdomain.commonsvc.executor.model.util.Json2ModelUtil;
import org.openo.crossdomain.commonsvc.executor.model.util.Model2JsonUtil;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class ServiceJob extends ServiceJobCommon {

    private static final OssLog log = OssLogFactory.getLogger(ServiceJob.class);

    private ServiceInfo service;

    private String instanceId;

    public static ServiceJob toServiceJob(String json) {
        JSONObject obj = JSONObject.fromObject(json);
        JSONObject jobObj = null;
        try {
            jobObj = Json2ModelUtil.getJsonObject(obj, RequestJsonConstants.SERVICE_JOB);
        } catch(JSONException e) {
            log.error("ServiceJob Json fail, {}", e.getMessage());
        }

        if(jobObj == null) {
            String msg = String.format(ErrorMessage.SERVICE_JOB_NULL_MSG, Constants.NULL_STR);
            log.error(msg);
            return null;
        }

        ServiceInfo service = null;
        JSONObject serviceObj = Json2ModelUtil.getJsonObject(jobObj, RequestJsonConstants.ServiceJob.service);
        if(serviceObj != null) {
            service = ServiceInfo.toService(serviceObj);
            if(service == null) {
                return null;
            }
        }

        ServiceJob serviceJob = new ServiceJob();
        serviceJob.setService(service);

        serviceJob.setJobId(Json2ModelUtil.getString(jobObj, RequestJsonConstants.ServiceJob.jobId));

        try {
            serviceJob.setJobContent(CommonUtil.encrypt(json));
        } catch(ServiceException e) {
            log.error("toServiceJob, encrypt fail:{}", e.getMessage());
            return null;
        }

        return serviceJob;
    }

    public void computeResult() {
        boolean containsSuccess = false;
        boolean containsFailed = false;
        for(Resource resource : service.getResources().values()) {
            if(resource.getResult() == null || resource.getResult().isSuccess()) {
                containsSuccess = true;
            } else {
                containsFailed = true;
            }
        }

        Result result = new Result();
        result.setOperObject(service.getServiceName());
        if(containsSuccess && containsFailed) {
            result.setErrorCode(Result.PARTSUCC);
            result.setResultReason("Operation Partially Succeed");
        } else if(containsFailed) {
            result.setErrorCode(Result.FAIL);
            result.setResultReason("Operation Failed");
        } else {
            result.setErrorCode(Result.SUCCESS);
            result.setResultReason("Operation Succeed");
        }
        setResult(result);
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;

        if(service != null) {
            service.setJobId(jobId);
        }
    }

    public void setTenantId(String tenantId) {
        if(StringUtils.hasLength(tenantId)) {
            this.tenantId = tenantId;
        }

        if(service != null) {
            service.setTenantId(this.tenantId);
        }
    }

    public ServiceInfo getService() {
        return service;
    }

    public void setService(ServiceInfo service) {
        this.service = service;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String generateExecuteCallBackContent() {
        JSONObject jobObj = serviceJob2Json();
        JSONObject rspObj = new JSONObject();
        Model2JsonUtil.putKeyValue(rspObj, RequestJsonConstants.SERVICE_JOB, jobObj.toString());

        return rspObj.toString();
    }

    public JSONObject serviceJob2Json() {
        JSONObject jobObj = new JSONObject();

        Model2JsonUtil.putKeyValue(jobObj, RequestJsonConstants.ServiceJob.jobId, getJobId());

        if(getStatus() != null) {
            Model2JsonUtil.putKeyValue(jobObj, RequestJsonConstants.PROGRESS, getStatus().toString());
        }

        if(getCreatedTime() != Constants.NULL_ID) {
            Model2JsonUtil.putKeyValue(jobObj, RequestJsonConstants.ServiceJob.createdTime,
                    String.valueOf(getCreatedTime()));
        }
        if(getCompletedTime() != Constants.NULL_ID) {
            Model2JsonUtil.putKeyValue(jobObj, RequestJsonConstants.ServiceJob.completedTime,
                    String.valueOf(getCompletedTime()));
        }

        Result result = getResult();
        if(result != null) {
            Model2JsonUtil.putKeyValue(jobObj, RequestJsonConstants.RESULT, result.getErrorCode());
            Model2JsonUtil.putKeyValue(jobObj, RequestJsonConstants.RESULT_REASON, result.getResultReason());
        }

        if(service != null) {
            JSONObject serviceObj = service.serviceRsp2Json();
            if(serviceObj != null) {
                Model2JsonUtil.putKeyValue(jobObj, RequestJsonConstants.ServiceJob.service, serviceObj.toString());
            }
        }

        return jobObj;
    }

    public String toLogDetail(boolean dealResult) {

        StringBuilder builder = new StringBuilder();

        Map<String, Object> serviceItemMap = new LinkedHashMap<>();
        if(service == null) {
            return Constants.EMPTY_JSON;
        }

        serviceItemMap.put(RequestJsonConstants.Service.serviceId, service.getServiceId());
        serviceItemMap.put(RequestJsonConstants.Service.serviceName, service.getServiceName());
        serviceItemMap.put(RequestJsonConstants.PROGRESS, status.toString());

        if(status != ExecutionStatus.COMPLETED) {
            serviceItemMap.put(RequestJsonConstants.ServiceJob.createdTime, createdTime);
        } else if(completedTime != Constants.NULL_ID) {
            serviceItemMap.put(RequestJsonConstants.ServiceJob.completedTime, completedTime);
        }

        if(dealResult) {
            resultToMap(serviceItemMap, getResult());
        }

        if(service.getResources() != null) {
            Map<String, Object> resourceMap = new LinkedHashMap<>();
            for(Map.Entry<String, Resource> entry : service.getResources().entrySet()) {
                Map<String, Object> resourceItemMap = new LinkedHashMap<>();
                resourceItemMap.put(RequestJsonConstants.Resource.name, entry.getValue().getName());

                if(dealResult) {
                    resultToMap(resourceItemMap, entry.getValue().getResult());
                }

                resourceMap.put(entry.getKey(), resourceItemMap);
            }

            serviceItemMap.put(RequestJsonConstants.Service.resources, resourceMap);
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(serviceItemMap);
        } catch(IOException e) {
            log.error("toLogDetail, writeValueAsString fail");
            return Constants.NULL_STR;
        }
    }

    private void resultToMap(Map<String, Object> map, Result result) {
        if(result == null) {
            return;
        }

        map.put(RequestJsonConstants.RESULT, result.getErrorCode());

        if(result.getResultReason() != null) {
            map.put(RequestJsonConstants.RESULT_REASON, result.getResultReason());
        }
    }
}
