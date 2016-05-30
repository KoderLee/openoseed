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

package org.openo.nfvo.nsservice.utils;

import java.util.Iterator;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

/**
 * 
* The utilty of json string operation .<br/>
* <p>
* </p>
* 
* @author
* @version NFVO 0.5 May 15, 2016
 */
public final class JsonUtil {

    private static final OssLog LOG = OssLogFactory.getLogger(JsonUtil.class);

    private JsonUtil() {
    }

    /**
     * Get string from json object by the specific fieldName<br/>
     * 
     * @param jsonObj
     * The json object that the string to be getted 
     * @param fieldName
     * The string to be getted's field name
     * @return the string to be getted
     * @since NFVO 0.5
     */
    public static String getJsonFieldStr(JSONObject jsonObj, String fieldName) {
        try {
            if(null != jsonObj && jsonObj.has(fieldName)) {
                String value = jsonObj.getString(fieldName);
                if("null".equalsIgnoreCase(value)) {
                    return "";
                }
                return jsonObj.getString(fieldName);
            }
        } catch(JSONException e) {
            LOG.error("function=getJsonFieldStr, msg=JSONException, e={}.", e);
            return null;
        }
        return null;
    }

    /**
     * Get integer from json object by the specific fieldName <br/>
     * 
     * @param jsonObj
     * The json object that the string to be getted 
     * @param fieldName
     * The integer to be getted's field name
     * @return the integer to be getted
     * @since NFVO 0.5
     */
    public static Integer getJsonFieldInt(JSONObject jsonObj, String fieldName) {
        try {
            if(null != jsonObj && jsonObj.has(fieldName)) {
                return jsonObj.getInt(fieldName);
            }
        } catch(JSONException e) {
            LOG.error("function=getJsonFieldInt, msg=JSONException, e={}.", e);
            return null;
        }
        return null;
    }

    /**
     * Get JSONArray from json object by the specific fieldName <br/>
     * 
     * @param jsonObj
     * The json object that the string to be getted 
     * @param fieldName
     * The JSONArray to be getted's field name
     * @return the JSONArray to be getted
     * @since NFVO 0.5
     */
    public static JSONArray getJsonFieldArr(JSONObject obj, String fieldName) {
        try {
            if(null != obj && obj.has(fieldName)) {
                return obj.getJSONArray(fieldName);
            }
        } catch(JSONException e) {
            LOG.error("function=getJsonFieldArr, msg=JSONException, e={}.", e);
            return null;
        }
        return null;
    }

    /**
     * Get JSONObject from json object by the specific fieldName <br/>
     * 
     * @param jsonObj
     * The json object that the string to be getted 
     * @param fieldName
     * The JSONObject to be getted's field name
     * @return the JSONObject to be getted
     * @since NFVO 0.5
     */
    public static JSONObject getJsonFieldJson(JSONObject obj, String fieldName) {
        try {
            if(null != obj && obj.has(fieldName)) {
                return obj.getJSONObject(fieldName);
            }
        } catch(JSONException e) {
            LOG.error("function=getJsonFieldJson, msg=JSONException, e={}.", e);
            return null;
        }
        return null;
    }

    public static Long getJsonFieldLong(JSONObject jsonObj, String fieldName) {
        try {
            if(null != jsonObj && jsonObj.has(fieldName)) {
                return jsonObj.getLong(fieldName);
            }
        } catch(JSONException e) {
            LOG.error("function=getJsonFieldLong, msg=JSONException, e={}.", e);
            return null;
        }
        return null;
    }
}
