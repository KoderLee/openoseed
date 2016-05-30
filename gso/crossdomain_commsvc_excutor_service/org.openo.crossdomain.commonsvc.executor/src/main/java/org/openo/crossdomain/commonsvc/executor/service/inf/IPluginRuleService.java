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
package org.openo.crossdomain.commonsvc.executor.service.inf;

import org.openo.commonservice.remoteservice.exception.ServiceException;

import java.util.List;

import org.openo.crossdomain.commonsvc.executor.common.module.IService;
import org.openo.crossdomain.commonsvc.executor.model.PluginRule;

/**
 * Plugin Rule Service interface class<br/>
 *
 * @author
 * @version crossdomain 0.5 2016-3-18
 */
public interface IPluginRuleService extends IService {

    /**
     * Register Plugin Rule<br/>
     *
     * @param ruleList Plugin Rule Model List
     * @throws ServiceException when fail to register plugin rule
     * @since crossdomain 0.5
     */
    void registerRule(List<PluginRule> ruleList) throws ServiceException;

    /**
     * Unregister Plugin Rule<br/>
     *
     * @param ruleList List of Plugin Rule Model
     * @throws ServiceException when fail to unregister plugin rule
     * @since crossdomain 0.5
     */
    void unregisterRule(List<PluginRule> ruleList) throws ServiceException;

    /**
     * Get All Plugin Rules From Database<br/>
     *
     * @return All Plugin Rules in Database
     * @throws ServiceException when fail to get all plugin rules from database
     * @since crossdomain 0.5
     */
    List<PluginRule> getAllRule() throws ServiceException;

    /**
     * Get Plugin Rule From redis<br/>
     * by resourceType,version, operType
     * 
     * @param resourceType Resource Type
     * @param version Resource Version
     * @param operType Operation Type
     * @return plugin rule
     * @throws ServiceException when fail to get plugin rule from redis
     * @since crossdomain 0.5
     */
    PluginRule getRuleByResourceTypeVersion(String resourceType, String version, String operType)
            throws ServiceException;
}
