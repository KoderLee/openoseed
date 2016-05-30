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

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.openo.crossdomain.commonsvc.executor.common.constant.Constants;
import org.openo.crossdomain.commonsvc.executor.common.constant.ErrorMessage;
import org.openo.crossdomain.commonsvc.executor.common.util.ServiceExceptionUtil;
import org.openo.crossdomain.commonsvc.executor.model.PluginRule;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PluginRuleUtil {

    private static final OssLog log = OssLogFactory.getLogger(PluginRuleUtil.class);

	/**
	 *Convert PluginRule from model to JSON
	 *@param ruleList Plugin Rule List for convertion
	 *@throws ServiceException when fail to convert
	 *@return PluginRule in JSON
	 *@since crossdomain 0.5 2016-3-18
	 */
    public static String pluginRule2Json(List<PluginRule> ruleList) throws ServiceException {
        if(ruleList == null) {
            String msg = String.format(ErrorMessage.OBJECT_NULL_MSG, "ruleList");
            ServiceExceptionUtil.throwErrorException(msg);
        }

        Map<String, List<PluginRule>> ruleMap = new HashMap<>();
        ruleMap.put(Constants.RULES, ruleList);

        ObjectMapper mapper = new ObjectMapper();
        String content = null;
        try {
            content = mapper.writeValueAsString(ruleMap);
        } catch(IOException e) {
            String msg = String.format(ErrorMessage.CONVERTO_JSON_FAIL_MSG, Constants.RULES);
            ServiceExceptionUtil.throwBadRequestException(msg);
        }

        return content;
    }

	/**
	 *Convert PluginRule from JSON to model
	 *@param content PluginRule in JSON
	 *@throws ServiceException when fail to convert
	 *@return PluginRule model list
	 *@since crossdomain 0.5 2016-3-18
	 */
    public static List<PluginRule> json2PluginRule(String content) throws ServiceException {
        ObjectMapper mapper = new ObjectMapper();
        List<PluginRule> ruleList = null;
        try {
            Map<String, List<PluginRule>> ruleMap =
                    mapper.readValue(content, new TypeReference<Map<String, List<PluginRule>>>() {});
            ruleList = ruleMap.get(Constants.RULES);
        } catch(IOException e) {
            String msg = String.format(ErrorMessage.JSON_CONVERTO_FAIL_MSG, Constants.RULES);
            ServiceExceptionUtil.throwBadRequestException(msg);
        }

        return ruleList;
    }

	/**
	 *Convert PluginRule from model to JSON for audit log detail
	 *@param ruleList Plugin Rule List for convertion
	 *@return PluginRule audit log detail in JSON
	 *@since crossdomain 0.5 2016-3-18
	 */	
    public static String toLogDetail(List<PluginRule> ruleList) {
        Map<String, String> map = new LinkedHashMap();
        for(PluginRule rule : ruleList) {
            map.put("Resource Type", rule.getTypeName());
            map.put("Resource Version", rule.getVersion());
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(map);
        } catch(IOException e) {
            log.error("toLogDetail, writeValueAsString fail");
            return Constants.NULL_STR;
        }
    }
}
