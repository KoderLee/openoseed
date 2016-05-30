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
package org.openo.crossdomain.commonsvc.decomposer.util;

import net.sf.json.JSONObject;

import org.openo.crossdomain.commonsvc.decomposer.check.CheckResult;
import org.openo.crossdomain.commonsvc.decomposer.check.ModelChecker;
import org.openo.crossdomain.commonsvc.decomposer.constant.Constant;
import org.openo.crossdomain.commonsvc.decomposer.constant.ErrorCode;
import org.springframework.util.StringUtils;

import org.openo.commonservice.remoteservice.exception.ServiceException;

/**
 * json util class
 * 
 * @since crossdomain 0.5
 */
public class JsonUtils {

	/**
	 * check service data for create
	 * 
	 * @param srvJsonString service string
	 * @throws ServiceException if format error
	 * @since crossdomain 0.5
	 */
	public static void checkCreateSrvData(final String srvJsonString)
			throws ServiceException {
		JSONObject jsonSvcBody = JSONObject.fromObject(srvJsonString);
		JSONObject jsonSvc = jsonSvcBody.getJSONObject(Constant.SERVICE);
		if (jsonSvc.isEmpty() || jsonSvc.isNullObject()) {
			throw new ServiceException(ErrorCode.SD_PARAMETER_VALIDATE_ERROR,
					"Service is null!");
		}
		if (!jsonSvc.has(Constant.SERVICEID)
				|| !StringUtils
						.hasLength(jsonSvc.getString(Constant.SERVICEID))) {
			throw new ServiceException(ErrorCode.SD_PARAMETER_VALIDATE_ERROR,
					"service_id is null!");
		}
	}

	/**
	 * Verify that the jsonString does not contain the specified key.<br>
	 * 
	 * @param jsonString Verify string
	 * @param key Verify key
	 * @return contains return null , not return CheckResult.
	 * @since crossdomain 0.5
	 */
	public static CheckResult checkJsonString(final String jsonString,
			final String key) {
		JSONObject jsonObj = JSONObject.fromObject(jsonString);
		return checkJsonString(jsonObj, key);
	}

	/**
	 * Verify that the jsonString does not contain the specified key.<br>
	 * 
	 * @param jsonString Verify string
	 * @param key Verify key
	 * @return contains return null , not return CheckResult.
	 * @since crossdomain 0.5
	 */
	public static CheckResult checkJsonObj(final String jsonString,
			final String key) {
		JSONObject jsonObj = JSONObject.fromObject(jsonString);
		return checkJsonObj(jsonObj, key);
	}

	/**
	 * Verify that the json object does not contain the specified key.<br>
	 * 
	 * @param jsonString Verify string
	 * @param key Verify key
	 * @return contains return null , not return CheckResult.
	 * @since crossdomain 0.5
	 */
	public static CheckResult checkJsonObj(final JSONObject jsonObj,
			final String key) {
		if (!jsonObj.containsKey(key)) {
			return new CheckResult(ModelChecker.ERROR_CODE_CHECK, key
					+ " is null");
		}
		return null;
	}

	private static CheckResult checkJsonString(final JSONObject jsonObj,
			final String key) {
		if (!jsonObj.has(key) || !StringUtils.hasLength(jsonObj.getString(key))) {
			return new CheckResult(ModelChecker.ERROR_CODE_CHECK, key
					+ " is null");
		}
		return null;
	}

	/**
	 * get string value form json object by key
	 * 
	 * @param json json object
	 * @param key key
	 * @return contains return string , not return null.
	 * @since crossdomain 0.5
	 */
	public static String getString(final JSONObject json, final String key) {
		if (json.has(key)) {
			return json.getString(key);
		}
		return null;
	}

	/**
	 * get json object form json object by key
	 * 
	 * @param json json object
	 * @param key key
	 * @return contains return JSON objects, not return null.
	 * @since crossdomain 0.5
	 */
	public static JSONObject getJsonObject(final JSONObject json,
			final String key) {
		if (json.has(key)) {
			return json.getJSONObject(key);
		}
		return null;
	}
}
