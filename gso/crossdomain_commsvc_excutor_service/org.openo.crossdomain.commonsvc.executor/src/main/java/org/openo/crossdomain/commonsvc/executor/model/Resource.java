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

import org.openo.crossdomain.commonsvc.executor.common.check.CheckAttr;
import org.openo.crossdomain.commonsvc.executor.common.check.CheckType;
import org.openo.crossdomain.commonsvc.executor.common.check.ModelChecker;
import org.openo.crossdomain.commonsvc.executor.common.constant.Constants;
import org.openo.crossdomain.commonsvc.executor.common.constant.ErrorMessage;
import org.openo.crossdomain.commonsvc.executor.common.constant.RequestJsonConstants;
import org.openo.crossdomain.commonsvc.executor.common.enums.ActionType;
import org.openo.crossdomain.commonsvc.executor.common.util.CommonUtil;
import org.openo.crossdomain.commonsvc.executor.common.util.ServiceExceptionUtil;
import org.openo.crossdomain.commonsvc.executor.model.db.ResourceCommon;
import org.openo.crossdomain.commonsvc.executor.model.db.ResourceForDB;
import org.openo.crossdomain.commonsvc.executor.model.db.Result;
import org.openo.crossdomain.commonsvc.executor.model.util.Json2ModelUtil;
import org.openo.crossdomain.commonsvc.executor.model.util.Model2JsonUtil;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;

import java.util.*;

public class Resource extends ResourceCommon {

    public static final String SEPARATOR = ",";

    private static final OssLog log = OssLogFactory.getLogger(Resource.class);

    private final List<String> dependon = new ArrayList<String>();

    @CheckAttr(type = CheckType.String, required = true, allowNull = true, min = 1, max = ModelChecker.UUID_MAX_LENGTH)
    private String id;

    private String name;

    public static Resource toResource(String resourceLabel, JSONObject resObj) throws ServiceException {

        JSONObject resourceObj = null;
        try {
            resourceObj = Json2ModelUtil.getJsonObject(resObj, resourceLabel);
        } catch(JSONException e) {
            String msg = String.format(ErrorMessage.REPEAT_RESOURCE_KEY_MSG, Constants.NULL_STR, resourceLabel);
            log.error(msg);
            ServiceExceptionUtil.throwErrorException(msg);
        }

        ActionType action = ActionType.toAction(resourceObj);
        if(action == null) {
            String msg = String.format(ErrorMessage.RESOURCE_FIELD_INVALID_MSG, RequestJsonConstants.action);
            log.error(msg);
            ServiceExceptionUtil.throwErrorException(msg);
        }

        Resource resource = new Resource();
        resource.setKey(resourceLabel);
        resource.setId(Json2ModelUtil.getString(resourceObj, RequestJsonConstants.Resource.id));
        resource.setName(Json2ModelUtil.getString(resourceObj, RequestJsonConstants.Resource.name));
        resource.setType(Json2ModelUtil.getString(resourceObj, RequestJsonConstants.Resource.type));
        resource.setOperType(action);

        String dependStr = Json2ModelUtil.getString(resourceObj, RequestJsonConstants.Resource.dependOn);
        if(StringUtils.hasLength(dependStr)) {

            LinkedList<String> list = new LinkedList<>();
            String[] strArray = dependStr.split(Resource.SEPARATOR);
            Collections.addAll(list, strArray);
            resource.getDependon().addAll(list);
        }

        resource.setResult(Result.resultRspToResult(resourceObj));

        resourceObj.remove(RequestJsonConstants.Resource.dependOn);

        String resContent = Model2JsonUtil.resource2Json(resourceLabel, resourceObj.toString()).toString();
        resource.setResContent(CommonUtil.encrypt(resContent));

        return resource;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getDependon() {
        return dependon;
    }

    public String dependonToString() {
        StringBuilder dependOnString = new StringBuilder();
        for(String depend : dependon) {
            dependOnString.append(depend).append(SEPARATOR);
        }

        int index = dependOnString.lastIndexOf(SEPARATOR);
        if(index != Constants.INVALID_ID) {
            dependOnString.deleteCharAt(index);
        }
        return dependOnString.toString();
    }

    public String resource2Json(boolean bWithStatus) {
        String decryptStr = null;
        try {
            decryptStr = CommonUtil.decrypt(getResContent());
        } catch(ServiceException e) {
            return decryptStr;
        }

        if(!bWithStatus) {
            return decryptStr;
        }

        JSONObject resourceObj = JSONObject.fromObject(decryptStr);

        Iterator it = resourceObj.keys();
        if(it.hasNext()) {
            String key = (String)it.next();

            JSONObject contentObj = Json2ModelUtil.getJsonObject(resourceObj, key);

            if(getStatus() != null) {
                Model2JsonUtil.putKeyValue(contentObj, RequestJsonConstants.STATUS, getStatus().toString());
            }

            Result result = getResult();
            if(result != null) {
                JSONObject resultObj = Json2ModelUtil.getJsonObject(resourceObj, RequestJsonConstants.RESULT);
                if(resultObj == null) {
                    Model2JsonUtil
                            .putKeyValue(contentObj, RequestJsonConstants.RESULT, result.result2Json().toString());
                }
            }
        }

        return resourceObj.toString();
    }

    public String resource2String() {
        return resource2Json(true);
    }

    public ResourceForDB convertToDBModel(@NotNull String jobId, @NotNull String serviceId) {
        ResourceForDB dbModel = new ResourceForDB(jobId, serviceId, getKey());
        dbModel.setResContent(getResContent());
        dbModel.setOperType(getOperType());
        dbModel.setStatus(getStatus());
        dbModel.setErrorCode((getResult() == null) ? null : getResult().getErrorCode());

        String reasonString = null;
        if((getResult() != null) && (getResult().getResultReason() != null)) {
            reasonString = getResult().getResultReason();
        }
        dbModel.setResultReason(reasonString);
        dbModel.setType((getType() == null) ? null : getType());
        dbModel.setDependson(dependonToString());
        dbModel.setQueryUrl(getQueryUrl());

        return dbModel;
    }
}
