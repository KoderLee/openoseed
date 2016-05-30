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
package org.openo.crossdomain.commonsvc.executor.dao.inf;

import org.openo.commonservice.remoteservice.exception.ServiceException;

import java.util.List;

import org.openo.crossdomain.commonsvc.executor.model.PluginRule;

public interface IPluginRuleDao {

    void addRule(List<PluginRule> ruleList) throws ServiceException;

    void updateRule(List<PluginRule> ruleList) throws ServiceException;

    void deleteRule(List<PluginRule> ruleList) throws ServiceException;

    List<PluginRule> getExistRule(List<PluginRule> conditionList) throws ServiceException;

    List<PluginRule> getRuleByType(String resType) throws ServiceException;
}
