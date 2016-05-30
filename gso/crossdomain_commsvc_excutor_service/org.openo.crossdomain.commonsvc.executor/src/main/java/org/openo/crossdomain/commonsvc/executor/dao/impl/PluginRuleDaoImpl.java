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
package org.openo.crossdomain.commonsvc.executor.dao.impl;

import org.openo.commonservice.log.OssLog;
import org.openo.commonservice.log.OssLogFactory;
import org.openo.commonservice.mybatis.session.MapperManager;
import org.openo.commonservice.remoteservice.exception.ServiceException;

import org.apache.ibatis.exceptions.PersistenceException;
import org.openo.crossdomain.commonsvc.executor.common.constant.Constants;
import org.openo.crossdomain.commonsvc.executor.common.util.ServiceExceptionUtil;
import org.openo.crossdomain.commonsvc.executor.dao.inf.IPluginRuleDao;
import org.openo.crossdomain.commonsvc.executor.dao.mapper.PluginRuleMapper;
import org.openo.crossdomain.commonsvc.executor.model.PluginRule;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;

@Component(value = "pluginRuleDao")
public class PluginRuleDaoImpl implements IPluginRuleDao {

    private static final OssLog logger = OssLogFactory.getLogger(PluginRuleDaoImpl.class);

    @Resource
    private MapperManager mapperMgrRef;

    private <T> T getMapperManager(Class<T> type) {
        return mapperMgrRef.getMapper(type, Constants.SERVICE_EXECUTOR_DB);
    }

    /**
     * @param mapperManager The mapperManager to set.
     */
    public void setMapperManager(MapperManager mapperManager) {
        this.mapperMgrRef = mapperManager;
    }

    /**
	 *add rule list to DataBase
	 *@param ruleList Plugin Rule list
	 *@throws ServiceException when fai to add rule list
	 *@since crossdomain 0.5 2016-3-18
	 */
    @Override
    public void addRule(List<PluginRule> ruleList) throws ServiceException {
        if(ruleList == null) {
            logger.info("addRule, ruleList is null");
            return;
        }

        PluginRuleMapper mapper = getMapperManager(PluginRuleMapper.class);

        for(PluginRule rule : ruleList) {
            try {
                mapper.insertPluginRule(rule);
            } catch(PersistenceException e) {
                String msg = String.format("insertPluginRule fails: %s", e.getMessage());
                logger.error(msg);
                ServiceExceptionUtil.throwErrorException(msg);
            }
        }
    }

	/**
	 *update rule list to DataBase
	 *@param ruleList Plugin Rule list
	 *@throws ServiceException when fai to update rule list
	 *@since crossdomain 0.5 2016-3-18
	 */
    @Override
    public void updateRule(List<PluginRule> ruleList) throws ServiceException {
        if(ruleList == null) {
            logger.info("updateRule, ruleList is null");
            return;
        }

        PluginRuleMapper mapper = getMapperManager(PluginRuleMapper.class);

        for(PluginRule rule : ruleList) {
            try {
                mapper.updatePluginRule(rule);
            } catch(PersistenceException e) {
                String msg = String.format("updatePluginRule fails: %s", e.getMessage());
                logger.error(msg);
                ServiceExceptionUtil.throwErrorException(msg);
            }
        }
    }

	/**
	 *delete rule list to DataBase
	 *@param ruleList Plugin Rule list
	 *@throws ServiceException when fai to delete rule list
	 *@since crossdomain 0.5 2016-3-18
	 */
    @Override
    public void deleteRule(List<PluginRule> ruleList) throws ServiceException {
        if(ruleList == null) {
            logger.info("deleteRule, ruleList is null");
            return;
        }

        PluginRuleMapper mapper = getMapperManager(PluginRuleMapper.class);

        for(PluginRule rule : ruleList) {
            try {
                mapper.deletePluginRule(rule);
            } catch(PersistenceException e) {
                String msg = String.format("deletePluginRule fails: %s", e.getMessage());
                logger.error(msg);
                ServiceExceptionUtil.throwErrorException(msg);
            }
        }
    }

	/**
	 *get existed rule list from DataBase
	 *@param conditionList Plugin Rule list
	 *@return required Plugin Rule List
	 *@throws ServiceException when fai to get rule list
	 *@since crossdomain 0.5 2016-3-18
	 */	
    @Override
    public List<PluginRule> getExistRule(List<PluginRule> conditionList) throws ServiceException {
        PluginRuleMapper mapper = getMapperManager(PluginRuleMapper.class);

        List<PluginRule> ruleList = null;
        try {
            if((conditionList == null) || conditionList.isEmpty()) {
                ruleList = mapper.getAllPluginRule();
            } else {
                ruleList = mapper.getPluginRule(conditionList);
            }
        } catch(PersistenceException e) {
            String msg = String.format("getExistRule fails: %s", e.getMessage());
            logger.error(msg);
            ServiceExceptionUtil.throwErrorException(msg);
        }
        return ruleList;
    }
	/**
	 *get rule list from DataBase by ResourceType
	 *@param resourceType Plugin Rule type
	 *@return required Plugin Rule List
	 *@throws ServiceException when fai to get rule list
	 *@since crossdomain 0.5 2016-3-18
	 */	
    @Override
    public List<PluginRule> getRuleByType(String resourceType) throws ServiceException {
        PluginRuleMapper mapper = getMapperManager(PluginRuleMapper.class);

        List<PluginRule> ruleList = new ArrayList<>();
        try {
            ruleList.addAll(mapper.getRuleByType(resourceType));
        } catch(PersistenceException e) {
            String msg = String.format("getRuleByType fails: %s", e.getMessage());
            logger.error(msg);
            ServiceExceptionUtil.throwErrorException(msg);
        }
        return ruleList;
    }
}
