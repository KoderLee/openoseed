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
package org.openo.crossdomain.servicemgr.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openo.crossdomain.servicemgr.constant.Constant;
import org.openo.crossdomain.servicemgr.dao.inf.IServiceModelCache;
import org.openo.crossdomain.servicemgr.model.servicemo.ServiceModel;
import org.openo.crossdomain.servicemgr.util.redis.ServiceManagerMapOper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import org.openo.baseservice.redis.RedisAccessException;

/**
 * Implementation for caching service model.<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
public class ServiceModelCacheImpl implements IServiceModelCache {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String DOMAIN = "servicemgr:servicemodelmap";

    private final ServiceManagerMapOper<ServiceModel> serviceModelOper = new ServiceManagerMapOper<ServiceModel>(
            DOMAIN, Constant.SVCMGR_R_DB);

    /**
     * 
     * @see org.openo.crossdomain.servicemgr.dao.inf.IServiceModelCache#set(org.openo.crossdomain.servicemgr.model.servicemo.ServiceModel)
     */
    @Override
    public void set(ServiceModel serviceModel) {
        try {
            this.serviceModelOper.put(serviceModel.getTenant_id(), serviceModel.getService_id(), serviceModel);
        } catch(RedisAccessException je) {
            logger.error("insert error, oper redis db error.", je);
        }
    }

    /**
     * 
     * @see org.openo.crossdomain.servicemgr.dao.inf.IServiceModelCache#setnx(org.openo.crossdomain.servicemgr.model.servicemo.ServiceModel)
     */
    @Override
    public void setnx(ServiceModel serviceModel) {
        try {
            this.serviceModelOper.putnx(serviceModel.getTenant_id(), serviceModel.getService_id(), serviceModel);
        } catch(RedisAccessException je) {
            logger.error("insert error, oper redis db error.", je);
        }
    }

    /**
     * 
     * @see org.openo.crossdomain.servicemgr.dao.inf.IServiceModelCache#deleteByID(java.lang.String, java.lang.String)
     */
    @Override
    public void deleteByID(String key, String service_id) {
        try {
            serviceModelOper.remove(key, service_id);
        } catch(RedisAccessException je) {
            logger.error("insert error, oper redis db error.", je);
        }
    }

    /**
     * 
     * @see org.openo.crossdomain.servicemgr.dao.inf.IServiceModelCache#getServiceModelById(java.lang.String, java.lang.String)
     */
    @Override
    public ServiceModel getServiceModelById(String key, String service_id) {
        try {
            List<ServiceModel> serviceModels = this.serviceModelOper.get(key, ServiceModel.class, service_id);
            if(!CollectionUtils.isEmpty(serviceModels)) {
                return serviceModels.iterator().next();
            }
            return null;
        } catch(RedisAccessException je) {
            logger.error("getServiceModelById error, oper redis db error.", je);
        }

        return null;
    }

    /**
     * 
     * @see org.openo.crossdomain.servicemgr.dao.inf.IServiceModelCache#getServiceModelListByTenantId(java.lang.String)
     */
    @Override
    public List<ServiceModel> getServiceModelListByTenantId(String key) {
        Map<String, ServiceModel> serviceModelMap = this.serviceModelOper.getAll(key, ServiceModel.class);
        if(CollectionUtils.isEmpty(serviceModelMap)) {
            return new ArrayList<ServiceModel>();
        }

        return new ArrayList<ServiceModel>(serviceModelMap.values());
    }

    /**
     * 
     * @see org.openo.crossdomain.servicemgr.dao.inf.IServiceModelCache#insert(java.lang.String, java.util.List)
     */
    @Override
    public void insert(String key, List<ServiceModel> serviceModels) {
        if(CollectionUtils.isEmpty(serviceModels)) {
            return;
        }

        Map<String, ServiceModel> serviceModelMap = new HashMap<String, ServiceModel>();
        for(ServiceModel serviceModel : serviceModels) {
            serviceModelMap.put(serviceModel.getService_id(), serviceModel);
        }
        serviceModelOper.putAll(key, serviceModelMap);
    }
}
