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
package org.openo.crossdomain.commonsvc.executor.model.util;

import net.sf.json.JSONObject;
import org.springframework.util.StringUtils;

public final class Model2JsonUtil {

	/**
	 *Convert key-value from model to JSON
	 *@param key json object key
	 *@param value json object value
	 *@return value in JSON
	 *@since crossdomain 0.5 2016-3-18
	 */
    public static String value2Json(String key, String value) {
        JSONObject obj = new JSONObject();

        putKeyValue(obj, key, value);

        return obj.toString();
    }

	/**
	 *build json object
	 *@param key json object key
	 *@param value json object value
	 *@return json object
	 *@since crossdomain 0.5 2016-3-18
	 */
    public static JSONObject resource2Json(String key, String value) {
        JSONObject obj = new JSONObject();

        putKeyValue(obj, key, value);

        return obj;
    }

	/**
	 *build json object
	 *@param key json object key
	 *@param value json object value
	 *@param obj json object
	 *@since crossdomain 0.5 2016-3-18
	 */
    public static void putKeyValue(JSONObject obj, String key, String value) {
        if((obj == null) || (key == null) || (!StringUtils.hasLength(value))) {
            return;
        }

        obj.put(key, value);
    }
}