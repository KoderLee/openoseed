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
package org.openo.crossdomain.commonsvc.executor.dao.mapper;

import org.apache.ibatis.exceptions.PersistenceException;
import org.openo.crossdomain.commonsvc.executor.model.PluginRule;

import java.util.List;

public interface PluginRuleMapper {

    void insertPluginRule(PluginRule rule) throws PersistenceException;

    void deletePluginRule(PluginRule rule) throws PersistenceException;

    void updatePluginRule(PluginRule rule) throws PersistenceException;

    List<PluginRule> getAllPluginRule() throws PersistenceException;

    List<PluginRule> getPluginRule(List<PluginRule> conditionList) throws PersistenceException;

    List<PluginRule> getRuleByType(String resourceType) throws PersistenceException;
}
