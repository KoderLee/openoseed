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

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.openo.crossdomain.commonsvc.executor.common.check.CheckAttr;
import org.openo.crossdomain.commonsvc.executor.common.check.CheckType;
import org.openo.crossdomain.commonsvc.executor.common.constant.Constants;
import org.openo.crossdomain.commonsvc.executor.common.enums.ActionType;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties({"redisKey", "redisRule", "intVersion"})
public class PluginRule {

    private static final String CONNECTOR = ":";

    private static final String OPER_SEPARATOR = "\\|";

    private static final int URL_LEN = 255;

    @CheckAttr(type = CheckType.String, required = true, min = 1, max = Constants.TYPELEN_MAX)
    private String typeName;

    @CheckAttr(type = CheckType.String, required = true, min = 1, max = Constants.TYPELEN_MAX)
    private String version;

    @JsonProperty("oper_type")
    @CheckAttr(type = CheckType.String, required = true, allowNull = true, min = 1, max = Constants.TYPELEN_MAX)
    private String operType;

    @JsonProperty("uri_prefix")
    @CheckAttr(type = CheckType.String, required = true, allowNull = true, min = 1, max = URL_LEN)
    private String uriPrefix;

    static public String generateRedisKey(String typeName, String version, String operType) {
        return (typeName + CONNECTOR + version + CONNECTOR + operType);
    }

    static public String generateTypeOperTypeRedisKey(String typeName, String operType) {
        return (typeName + CONNECTOR + operType);
    }

    static public String generateVersion(int intVersion) {
        return (Constants.VERSION_PRE + String.valueOf(intVersion));
    }

    static public boolean checkRuleValid(List<PluginRule> ruleList) {
        boolean valid = true;
        for(PluginRule rule : ruleList) {
            if(!(checkVersionValid(rule.getVersion()) && checkOperTypeValid(rule.getOperType()))) {
                valid = false;
                break;
            }
        }

        return valid;
    }

    static public boolean checkVersionValid(String version) {
        return version.matches("v[1-9][0-9]*");
    }

    static public boolean checkOperTypeValid(String operType) {
        boolean valid = true;
        if(!StringUtils.hasLength(operType)) {
            return valid;
        }

        String[] operTypeArry = operType.split(OPER_SEPARATOR);
        for(String oper : operTypeArry) {
            if(ActionType.enumValueOf(oper) == null) {
                valid = false;
                break;
            }
        }

        return valid;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getOperType() {
        return operType;
    }

    public void setOperType(String operType) {
        this.operType = operType;
    }

    public String getUriPrefix() {
        return uriPrefix;
    }

    public void setUriPrefix(String uriPrefix) {
        this.uriPrefix = uriPrefix;
    }

    public String getRedisKey() {
        return (typeName + CONNECTOR + version + CONNECTOR + operType);
    }

    public List<PluginRule> getRedisRule() {
        List<PluginRule> ruleList = new ArrayList<>();

        String[] operTypeArry = operType.split(OPER_SEPARATOR);
        for(String oper : operTypeArry) {
            PluginRule rule = new PluginRule();
            rule.setTypeName(typeName);
            rule.setVersion(version);
            rule.setOperType(oper);
            rule.setUriPrefix(uriPrefix);
            ruleList.add(rule);
        }

        return ruleList;
    }

    public int getIntVersion() {
        return Integer.parseInt(version.substring(1));
    }
}
