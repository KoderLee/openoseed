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
package org.openo.crossdomain.commonsvc.executor.common.constant;

public class ErrorMessage {

    public static final String NOT_DEPENDER_MSG = "Resource:%s can't find depender:%s";

    public static final String DEPENDER_REPEAT_MSG = "Resource:%s contain repeating depender:%s";

    public static final String DEPENDER_MSG = "Resource:%s can't find depender:%s";

    public static final String ACTION_UNMATCH_MSG = "Action unmatch:%s";

    public static final String RESOURCE_FIELD_UNMATCH_MSG = "Resource:%s action unmatch";

    public static final String RESOURCE_FIELD_NULL_MSG = "Resource:%s %s is null";

    public static final String SERVICE_FIELD_UNMATCH_MSG = "Service:%s %s unmatch";

    public static final String SERVICE_FIELD_INVALID_MSG = "%s of service invalid";

    public static final String SRRVICE_FIELD_NULL_MSG = "Service:%s %s is null";

    public static final String GENERATE_JOBID_FAIL_MSG = "Can't generate Job id";

    public static final String REPEAT_RESOURCE_KEY_MSG = "Service:%s has repeat ResourceKey:%s";

    public static final String SERVICE_CONTENT_NULL_MSG = "Service:%s string is null";

    public static final String SERVICE_RESOURCE_NULL_MSG = "Service:%s resources are null";

    public static final String SERVICE_JOB_INVALID_MSG = "ServiceJob invalid";

    public static final String SERVICE_JOB_NULL_MSG = "ServiceJob:%s content invalid";

    public static final String SAME_REQUEST_MSG = "RequestHandlers exists same request Name:%s";

    public static final String OBJECT_NULL_MSG = "%s is null";

    public static final String OBJECT_NULL_WITHKEY_MSG = "%s is null, %s:%s";

    public static final String JSON_CONVERT_NULL_MSG = "Json conver fails, %s is null";

    public static final String RESULT_REASON_MSG = "Result:%s, reason:%s";

    public static final String HTTP_STATUS_MSG = "Http Status:%d";

    public static final String TIMEOUT_MSG = "%s:%s timeout";

    public static final String RESOURCE_INVALID_MSG = "Resource invalid";

    public static final String RESOURCE_FIELD_INVALID_MSG = "%s of resource invalid";

    public static final String RESPONSE_ERROR_MSG = "response return error: %s";

    public static final String LENGTH_TOO_LONG_MSG = "%s length too long";

    public static final String NUMBER_TOO_MANY_MSG = "%s number too many";

    public static final String RULE_NOT_REGISTER_MSG = "Rule:%s not register";

    public static final String JSON_CONVERTO_FAIL_MSG = "Json convert to %s fail";

    public static final String CONVERTO_JSON_FAIL_MSG = "%s convert to json fail";

    public static final String CONDITION_ERR_MSG = "filter condition error";

    public static final String URI_INVALID_MSG = "Uri invalid";

    public static final String GET_AUTH_TOKEN_FAIL_MSG = "Getting authorised token fails";

    public static final String VALIDATION_ERROR_MSG = "Validation Error";

    public static final String SE_DATA_ENCRYPT_FAIL = "serviceexecutor.data_encrypt_fail";

    public static final String SE_DATA_DECRYPT_FAIL = "serviceexecutor.data_decrypt_fail";
}
