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

import org.openo.commonservice.log.OssLog;
import org.openo.commonservice.log.OssLogFactory;
import org.openo.commonservice.remoteservice.exception.ServiceException;

import net.sf.json.JSONObject;

import org.openo.crossdomain.commonsvc.executor.common.constant.Constants;
import org.openo.crossdomain.commonsvc.executor.common.constant.ErrorMessage;
import org.openo.crossdomain.commonsvc.executor.common.constant.RequestJsonConstants;
import org.openo.crossdomain.commonsvc.executor.common.util.ServiceExceptionUtil;
import org.springframework.util.StringUtils;

public final class Json2ModelUtil {

    private static final OssLog log = OssLogFactory.getLogger(Json2ModelUtil.class);

    /**
	 *judge whether is dryrun
	 *@param content input string
	 *@return whether is dryrun
	 *@since crossdomain 0.5 2016-3-18
	 */
    public static boolean jsonToDryrun(String content) throws ServiceException {
        boolean bDryrun = false;
        if(!StringUtils.hasLength(content)) {
            return bDryrun;
        }

        if(content.equalsIgnoreCase("false")) {
            bDryrun = false;
        } else if(content.equalsIgnoreCase("true")) {
            bDryrun = true;
        } else {
            String msg = String.format(ErrorMessage.SERVICE_FIELD_INVALID_MSG, RequestJsonConstants.Service.isDryrun);
            log.error(msg);
            ServiceExceptionUtil.throwBadRequestException(msg);
        }

        return bDryrun;
    }

	/**
	 *judge whether is onfailure
	 *@param content input string
	 *@return whether is onfailure
	 *@since crossdomain 0.5 2016-3-18
	 */
    public static String jsonToOnfailure(String content) throws ServiceException {
        if(!StringUtils.hasLength(content)) {
            return Constants.NULL_STR;
        }

        if(!Constants.POLICY_ROLLBACK.equalsIgnoreCase(content)) {
            String msg = String.format(ErrorMessage.SERVICE_FIELD_INVALID_MSG, RequestJsonConstants.Service.onFailure);
            ServiceExceptionUtil.throwBadRequestException(msg);
        }
        return Constants.POLICY_ROLLBACK;
    }


	/**
	 *get url by location
	 *@param content input string
	 *@return location url
	 *@since crossdomain 0.5 2016-3-18
	 */	
    public static String jsonToLocation(String content) {
        if(content == null) {
            return Constants.NULL_STR;
        }

        JSONObject locationObj = JSONObject.fromObject(content);

        String location = getString(locationObj, Constants.LOCATION_KEY);
        if(!StringUtils.hasLength(location)) {
            String msg = String.format(ErrorMessage.OBJECT_NULL_MSG, Constants.LOCATION_KEY);
            log.error(msg);

            return Constants.NULL_STR;
        }

        return location;
    }

    /**
	 *judge whether is finished
	 *@param rspObj reponse
	 *@return whether is finished
	 *@since crossdomain 0.5 2016-3-18
	 */
    public static boolean toProcessing(JSONObject rspObj) {
        String boolStr = getString(rspObj, RequestJsonConstants.finished).toLowerCase();
        return Boolean.parseBoolean(boolStr);
    }

	/**
	 *get json object value by key
	 *@param jsonObject input jsonObject
	 *@param key json object key
	 *@return value
	 *@since crossdomain 0.5 2016-3-18
	 */
    public static String getString(JSONObject jsonObject, String key) {
        return jsonObject.containsKey(key) ? jsonObject.getString(key) : "";
    }
	/**
	 *get json object by key
	 *@param jsonObject input jsonObject
	 *@param key json object key
	 *@return jsonObject
	 *@since crossdomain 0.5 2016-3-18
	 */
    public static JSONObject getJsonObject(JSONObject jsonObject, String key) {
        return jsonObject.containsKey(key) ? jsonObject.getJSONObject(key) : null;
    }
}
