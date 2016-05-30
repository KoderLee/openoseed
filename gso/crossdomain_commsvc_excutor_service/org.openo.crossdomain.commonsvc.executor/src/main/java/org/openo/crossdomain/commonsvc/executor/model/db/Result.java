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

import org.openo.commonservice.biz.trail.AuditItem;
import org.openo.commonservice.log.OssLog;
import org.openo.commonservice.log.OssLogFactory;

import net.sf.json.JSONObject;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.openo.crossdomain.commonsvc.executor.common.constant.Constants;
import org.openo.crossdomain.commonsvc.executor.common.constant.RequestJsonConstants;
import org.openo.crossdomain.commonsvc.executor.model.util.Json2ModelUtil;
import org.springframework.util.StringUtils;

@JsonIgnoreProperties({"result"})
public class Result {

    private static final OssLog log = OssLogFactory.getLogger(Result.class);

    public static final String SUCCESS = "success";

    public static final String FAIL = "failed";

    public static final String PARTSUCC = "partly success";

    public static final String CREATEFAIL = "create failed";

    public static final int REASONLEN = 255;

    @JsonProperty("code")
    private String errorCode;

    private String reason;

    private String operObject;

    public Result() {
        errorCode = SUCCESS;
    }

    public Result(String errorCode, String reason, String operObject) {
        this.reason = trimReasonString(reason, REASONLEN);
        this.errorCode = errorCode;
        this.operObject = operObject;
    }

    public static boolean isSuccess(String error) {
        boolean bRet = true;
        if(!StringUtils.hasLength(error)) {
            return bRet;
        }

        if(!SUCCESS.equals(error)) {
            bRet = false;
        }
        return bRet;
    }

    public static Result resultRspToResult(JSONObject rspObj) {
        if(rspObj == null) {
            return null;
        }

        JSONObject resultObj = Json2ModelUtil.getJsonObject(rspObj, RequestJsonConstants.RESULT);
        if(resultObj == null) {
            return null;
        }

        Result result = new Result();
        result.setErrorCode(Json2ModelUtil.getString(resultObj, RequestJsonConstants.Result.errorCode).toLowerCase());
        result.setResultReason(Json2ModelUtil.getString(resultObj, RequestJsonConstants.Result.reason));

        return result;
    }

    public static Result json2Result(JSONObject resultObj) {
        if(resultObj == null) {
            return null;
        }

        String resultCode = Json2ModelUtil.getString(resultObj, RequestJsonConstants.RESULT).toLowerCase();
        if(!StringUtils.hasLength(resultCode)) {
            return null;
        }

        Result result = new Result();
        result.setErrorCode(resultCode);
        result.setResultReason(Json2ModelUtil.getString(resultObj, RequestJsonConstants.RESULT_REASON));

        return result;
    }

    public static Result exceptionJsonToResult(JSONObject rspObj) {
        if(rspObj == null) {
            return null;
        }

        String exceptionId = Json2ModelUtil.getString(rspObj, RequestJsonConstants.ExceptionJson.exceptionId);
        if(!StringUtils.hasLength(exceptionId)) {
            return null;
        }

        Result result = new Result();
        result.setErrorCode(Result.FAIL);
        result.setResultReason(exceptionId);

        return result;
    }

    public boolean isSuccess() {
        return isSuccess(errorCode);
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getResultReason() {
        return reason;
    }

    public void setResultReason(String reason) {
        this.reason = trimReasonString(reason, REASONLEN);
    }

    public String getOperObject() {
        return operObject;
    }

    public void setOperObject(String operObject) {
        this.operObject = operObject;
    }

    public String trimReasonString(String reason, int strLen) {
        if(!StringUtils.hasLength(reason) || reason.length() <= strLen) {
            return reason;
        }

        return reason.substring(Constants.NULL_ID, strLen);
    }

    public void setResult(Result result) {
        this.errorCode = null;
        this.reason = null;
        this.operObject = null;

        if(result != null) {
            this.errorCode = result.getErrorCode();
            this.reason = result.getResultReason();
            this.operObject = result.getOperObject();
        }
    }

    public Result getResult() {
        if(this.errorCode == null) {
            return null;
        }

        return new Result(errorCode, reason, operObject);
    }

    public JSONObject result2Json() {
        JSONObject obj = new JSONObject();
        obj.put(RequestJsonConstants.Result.errorCode, getErrorCode());
        obj.put(RequestJsonConstants.Result.reason, getResultReason());

        return obj;
    }

    public String resultRsp2Json() {
        JSONObject obj = new JSONObject();
        obj.put(RequestJsonConstants.RESULT, result2Json().toString());

        return obj.toString();
    }

    public AuditItem.AuditResult toLogResult() {
        if(errorCode == null) {
            return AuditItem.AuditResult.SUCCESS;
        }

        if(errorCode.equals(SUCCESS)) {
            return AuditItem.AuditResult.SUCCESS;
        } else if(errorCode.equals(FAIL) || errorCode.equals(CREATEFAIL)) {
            return AuditItem.AuditResult.FAILURE;
        } else if(errorCode.equals(PARTSUCC)) {
            return AuditItem.AuditResult.PARTIAL_SUCCESS;
        } else {

            log.error("unknown error code:{}", errorCode);
            return AuditItem.AuditResult.FAILURE;
        }
    }

    public String toString() {
        String str = Constants.NULL_STR;
        if(errorCode != null) {
            str += "ErrorCode:" + errorCode;
        }

        if(reason != null) {
            str += ",Reason:" + reason;
        }

        return str;
    }
}
