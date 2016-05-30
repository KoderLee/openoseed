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
package org.openo.crossdomain.commonsvc.executor.service.impl;

import org.openo.commonservice.log.OssLog;
import org.openo.commonservice.log.OssLogFactory;
import org.openo.commonservice.remoteservice.exception.ServiceException;

import org.openo.crossdomain.commonsvc.executor.common.constant.Constants;
import org.openo.crossdomain.commonsvc.executor.common.constant.ErrorMessage;
import org.openo.crossdomain.commonsvc.executor.common.redis.RedisMap;
import org.openo.crossdomain.commonsvc.executor.common.util.ServiceExceptionUtil;
import org.openo.crossdomain.commonsvc.executor.dao.inf.IPluginRuleDao;
import org.openo.crossdomain.commonsvc.executor.model.PluginRule;
import org.openo.crossdomain.commonsvc.executor.service.inf.IPluginRuleService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

import java.util.*;

/**
 * Plugin Rule Service class<br/>
 *
 * @author
 * @version crossdomain 0.5 2016-3-18
 */
@Service(value = "pluginRuleService")
public class PluginRuleService implements IPluginRuleService {

    private static final OssLog log = OssLogFactory.getLogger(PluginRuleService.class);

    @Resource
    private IPluginRuleDao pluginRuleDao;

    private RedisMap<PluginRule> mapOper = new RedisMap<>(Constants.RULES, Constants.NULL_STR);

    private RedisMap<String> versionOper = new RedisMap<>(Constants.RULEVERSION, Constants.NULL_STR);

    /**
     * Register Plugin Rule<br/>
     *
     * @param ruleList Plugin Rule Model List
     * @throws ServiceException when fail to register plugin rule
     * @since crossdomain 0.5
     */
    public void registerRule(List<PluginRule> ruleList) throws ServiceException {
        List<PluginRule> newRulelist = new ArrayList<>();
        List<PluginRule> modifyRulelist = new ArrayList<>();
        List<PluginRule> modifyDbRulelist = new ArrayList<>();
        List<PluginRule> redisNewList = new ArrayList<>();
        List<PluginRule> redisModifyList = new ArrayList<>();
        List<PluginRule> redisDeleteList = new ArrayList<>();
        List<PluginRule> redisRuleList = null;

        try {
            List<PluginRule> dbRuleList = pluginRuleDao.getExistRule(ruleList);

            compareRuleDb(ruleList, dbRuleList, newRulelist, modifyRulelist, modifyDbRulelist);

            // get plugin rules to be modified from redis
            redisRuleList = getRuleFromRedis(modifyDbRulelist);

            compareRuleRedis(modifyRulelist, redisRuleList, redisNewList, redisModifyList, redisDeleteList);

            // create or modify plugin rules in redis
            addRuleToRedis(getRedisRuleList(ruleList));
            deleteRuleFromRedis(redisDeleteList);

            // update database
            pluginRuleDao.addRule(newRulelist);
            pluginRuleDao.updateRule(modifyRulelist);

            // update the field "ResourceVersion" of plugin rule in redis
            Set<String> typeSet = new HashSet<>();
            for(PluginRule rule : ruleList) {
                typeSet.add(rule.getTypeName());
            }
            updateResourceVersion(typeSet);
        } catch(ServiceException e) {
            log.error("registerRule fail");

            // roll back, if register plugin rule fail
            addRuleToRedis(redisRuleList);
            deleteRuleFromRedis(redisNewList);

            throw e;
        }
    }

    /**
     * Unregister Plugin Rule<br/>
     *
     * @param ruleList List of Plugin Rule Model
     * @throws ServiceException when fail to unregister plugin rule
     * @since crossdomain 0.5
     */
    public void unregisterRule(List<PluginRule> ruleList) throws ServiceException {
        // backup the plugin rules for rolling back
        List<PluginRule> redisRuleList = new ArrayList<>();

        try {
            List<PluginRule> dbRuleList = pluginRuleDao.getExistRule(ruleList);

            redisRuleList.addAll(getRuleFromRedis(dbRuleList));

            // delete plugin rules from redis
            deleteRuleFromRedis(redisRuleList);

            // delete plugin rules from database
            pluginRuleDao.deleteRule(ruleList);

            // remove ResourceVersion date of the plugin rules
            removeResourceVersion(getRedisRuleList(dbRuleList));

            // update ResourceVersion
            Set<String> typeSet = new HashSet<>();
            for(PluginRule rule : dbRuleList) {
                typeSet.add(rule.getTypeName());
            }
            updateResourceVersion(typeSet);
        } catch(ServiceException e) {
            log.error("unregisterRule fail");

            // roll back
            addRuleToRedis(redisRuleList);

            throw e;
        }
    }

    /**
     * Get All Plugin Rules From Database<br/>
     *
     * @return All Plugin Rules in Database
     * @throws ServiceException when fail to get all plugin rules from database
     * @since crossdomain 0.5
     */
    public List<PluginRule> getAllRule() throws ServiceException {
        return pluginRuleDao.getExistRule(null);
    }

    /**
     * Remove ResourceVersion Data of Plugin Rules<br/>
     * in Redis
     * 
     * @param redisRuleList Plugin Rules in Redis
     * @throws ServiceException when fail to remove ResourceVersion Data
     * @since crossdomain 0.5
     */
    public void removeResourceVersion(List<PluginRule> redisRuleList) throws ServiceException {
        for(PluginRule rule : redisRuleList) {
            String typeOperTypeKey = PluginRule.generateTypeOperTypeRedisKey(rule.getTypeName(), rule.getOperType());
            versionOper.remove(typeOperTypeKey);
        }
    }

    /**
     * update ResourceVersion in Redis<br/>
     *
     * @param typeSet ResourceType Set
     * @throws ServiceException when fail to update ResourceVersion
     * @since crossdomain 0.5
     */
    public void updateResourceVersion(Set<String> typeSet) throws ServiceException {
        Map<String, Integer> typeOperTypeMap = new HashMap<>();
        for(String resourceType : typeSet) {
            // get Plugin Rule List from Database by resourceType
            List<PluginRule> ruleList = pluginRuleDao.getRuleByType(resourceType);

            for(PluginRule rule : getRedisRuleList(ruleList)) {
                String typeOperTypeKey =
                        PluginRule.generateTypeOperTypeRedisKey(rule.getTypeName(), rule.getOperType());

                int newVersion = rule.getIntVersion();
                Integer version = typeOperTypeMap.get(typeOperTypeKey);
                if(version == null || version < newVersion) {
                    typeOperTypeMap.put(typeOperTypeKey, newVersion);
                }
            }
        }

        for(Map.Entry<String, Integer> entry : typeOperTypeMap.entrySet()) {
            versionOper.put(entry.getKey(), PluginRule.generateVersion(entry.getValue()));
        }
    }

    /**
     * Check Resource Version<br/>
     *
     * @param resourceType Resource Type
     * @param version Resource Version
     * @param operType Operation Type
     * @return Resource Version(if input "version" is empty, get it from redis)
     * @throws ServiceException
     * @since crossdomain 0.5
     */
    public String checkResourceVersion(String resourceType, String version, String operType) throws ServiceException {
        // check Resource Version whether valid
        if(StringUtils.hasLength(version)) {
            return version;
        }

        String typeOperKey = PluginRule.generateTypeOperTypeRedisKey(resourceType, operType);

        String tmpVersion = versionOper.get(String.class, typeOperKey);
        if(!StringUtils.hasLength(tmpVersion)) {
            updateRuleToRedisByResourceType(resourceType);

            tmpVersion = versionOper.get(String.class, typeOperKey);

            if(!StringUtils.hasLength(tmpVersion)) {
                String msg = String.format(ErrorMessage.RULE_NOT_REGISTER_MSG, resourceType);
                log.error(msg);
                ServiceExceptionUtil.throwErrorException(msg);
            }
        }

        return tmpVersion;
    }

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
    public PluginRule getRuleByResourceTypeVersion(String resourceType, String version, String operType)
            throws ServiceException {
        String tmpVersion = checkResourceVersion(resourceType, version, operType);

        String ruleKey = PluginRule.generateRedisKey(resourceType, tmpVersion, operType);

        return mapOper.get(PluginRule.class, ruleKey);
    }

    /**
     * update Plugin Rules to Redis
     * get Plugin Rules from Database By ResourceType, then update to Redis<br/>
     *
     * @param resourceType Resource Type
     * @throws ServiceException when fail to update Plugin Rules to Redis
     * @since crossdomain 0.5
     */
    public void updateRuleToRedisByResourceType(String resourceType) throws ServiceException {
        List<PluginRule> ruleList = pluginRuleDao.getRuleByType(resourceType);

        addRuleToRedis(getRedisRuleList(ruleList));

        Set<String> typeSet = new HashSet<>();
        for(PluginRule rule : ruleList) {
            typeSet.add(rule.getTypeName());
        }

        updateResourceVersion(typeSet);
    }

    /**
     * Add new Plugin Rules to Redis<br/>
     *
     * @param ruleList input plugin rule list
     * @since crossdomain 0.5
     */
    private void addRuleToRedis(List<PluginRule> ruleList) {
        if(ruleList == null || ruleList.isEmpty()) {
            return;
        }

        for(PluginRule rule : ruleList) {
            mapOper.put(rule.getRedisKey(), rule);
        }
    }

    /**
     * Delete Plugin Rule From Redis<br/>
     *
     * @param ruleList input Plugin Rule List
     * @since crossdomain 0.5
     */
    private void deleteRuleFromRedis(List<PluginRule> ruleList) {
        for(PluginRule rule : ruleList) {
            mapOper.remove(rule.getRedisKey());
        }
    }

    /**
     * Get Plugin Rule from Redis
     * by keyList of input plugin rule list<br/>
     *
     * @param ruleList input plugin rule list
     * @return plugin rule detail list
     * @since crossdomain 0.5
     */
    private List<PluginRule> getRuleFromRedis(List<PluginRule> ruleList) {
        if(ruleList == null || ruleList.isEmpty()) {
            return new ArrayList<PluginRule>();
        }

        List<String> keyList = new ArrayList<>();
        for(PluginRule rule : ruleList) {
            for(PluginRule redisRule : rule.getRedisRule()) {
                keyList.add(redisRule.getRedisKey());
            }
        }

        String[] strings = new String[keyList.size()];
        keyList.toArray(strings);
        List<PluginRule> redisRuleList = mapOper.get(PluginRule.class, strings);
        redisRuleList.removeAll(Collections.singleton(null));
        return redisRuleList;
    }

    /**
     * Compare Input Plugin Rule List with Records in Database<br/>
     *
     * @param ruleList plugin rules to register
     * @param dbRuleList plugin rules in database
     * @param newRuleList plugin rules to be added
     * @param modifyRuleList plugin rules to be modified
     * @param modifyDbRuleList plugin rules to be modified in database
     * @since crossdomain 0.5
     */
    private void compareRuleDb(List<PluginRule> ruleList, List<PluginRule> dbRuleList, List<PluginRule> newRuleList,
            List<PluginRule> modifyRuleList, List<PluginRule> modifyDbRuleList) {
        for(PluginRule rule : ruleList) {
            boolean bGet = false;
            for(PluginRule dbRule : dbRuleList) {
                if(rule.getTypeName().equals(dbRule.getTypeName()) && rule.getVersion().equals(dbRule.getVersion())) {
                    bGet = true;
                    if(!rule.getOperType().equals(dbRule.getOperType())
                            || !rule.getUriPrefix().equals(dbRule.getUriPrefix())) {
                        modifyRuleList.add(rule);
                        modifyDbRuleList.add(dbRule);
                        break;
                    }
                }
            }

            if(!bGet) {
                newRuleList.add(rule);
            }
        }
    }

    /**
     * Compare Input Plugin Rule List with Records in Redis<br/>
     *
     * @param ruleList plugin rules to register
     * @param redisRuleList plugin rules in redis
     * @param newRedisRuleList plugin rules to be added
     * @param modifyRedisRuleList plugin rules to be modified
     * @param deleteRedisRuleList plugin rules to be deleted
     * @since crossdomain 0.5
     */
    public void compareRuleRedis(List<PluginRule> ruleList, List<PluginRule> redisRuleList,
            List<PluginRule> newRedisRuleList, List<PluginRule> modifyRedisRuleList,
            List<PluginRule> deleteRedisRuleList) {
        if(redisRuleList == null) {
            redisRuleList = new ArrayList<>();
        }

        for(PluginRule tmpRedisRule : getRedisRuleList(ruleList)) {
            boolean bGet = false;
            for(PluginRule redisRule : redisRuleList) {
                if(tmpRedisRule.getRedisKey().equals(redisRule.getRedisKey())) {
                    bGet = true;
                    modifyRedisRuleList.add(tmpRedisRule);
                    break;
                }
            }
            if(!bGet) {
                newRedisRuleList.add(tmpRedisRule);
            }
        }

        for(PluginRule redisRule : redisRuleList) {
            boolean bGet = false;
            for(PluginRule tmpRedisRule : getRedisRuleList(ruleList)) {
                if(tmpRedisRule.getRedisKey().equals(redisRule.getRedisKey())) {
                    bGet = true;
                    break;
                }
            }
            if(!bGet) {
                deleteRedisRuleList.add(redisRule);
            }
        }
    }

    /**
     * Get Plugin Rule From Redis By Plugin Rule
     * operType are stored in redis independent<br/>
     *
     * @param ruleList Plugin Rule List, operType is in form of "create|delete"
     * @return Plugin Rule List in Redis
     * @since crossdomain 0.5
     */
    private List<PluginRule> getRedisRuleList(List<PluginRule> ruleList) {
        List<PluginRule> redisRuleList = new ArrayList<>();
        for(PluginRule rule : ruleList) {
            redisRuleList.addAll(rule.getRedisRule());
        }

        return redisRuleList;
    }

}
