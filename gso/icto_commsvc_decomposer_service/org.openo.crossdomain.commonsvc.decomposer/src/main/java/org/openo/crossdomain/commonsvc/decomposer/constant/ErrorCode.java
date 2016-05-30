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
package org.openo.crossdomain.commonsvc.decomposer.constant;

/**
 * Error code definition
 * 
 * @since crossdomain 0.5
 */
public class ErrorCode {

	public static final String SUCCESS = EnumResult.SUCCESS.getName();

	public static final String FAIL = EnumResult.FAIL.getName();

	public static final String SD_BAD_PARAM = "servicedecomposer.bad_param";

	public static final String SD_OPER_REDIS_ERROR = "servicedecomposer.oper_redis_error";

	public static final String SD_OPER_DB_ERROR = "servicedecomposer.oper_db_error";

	public static final String SD_SERVICE_NOT_EXIST = "servicedecomposer.service_not_exist";

	public static final String SD_TASK_EXECUTE_FAIL = "servicedecomposer.task_execute_fail";

	public static final String SD_PARAMETER_VALIDATE_ERROR = "servicedecomposer.service_parameter_validate_error";

	public static final String SD_DATA_ENCRYPT_FAIL = "servicedecomposer.data_encrypt_fail";

	public static final String SD_DATA_DECRYPT_FAIL = "servicedecomposer.data_decrypt_fail";
}
