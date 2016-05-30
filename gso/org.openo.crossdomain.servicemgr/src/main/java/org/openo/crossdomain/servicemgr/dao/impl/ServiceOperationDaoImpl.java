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

import java.util.List;

import org.apache.ibatis.exceptions.IbatisException;
import org.openo.crossdomain.servicemgr.constant.Constant;
import org.openo.crossdomain.servicemgr.dao.inf.IServiceOperationDao;
import org.openo.crossdomain.servicemgr.mapper.ServiceOperationMapper;
import org.openo.crossdomain.servicemgr.model.servicemo.ServiceOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import org.openo.baseservice.mybatis.session.MapperManager;
import org.openo.baseservice.remoteservice.exception.ServiceException;

/**
 * Implementation for database operation of service operation.<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
@SuppressWarnings("deprecation")
public class ServiceOperationDaoImpl implements IServiceOperationDao {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private MapperManager mapperManager;

    /**
     * 
     * @see org.openo.crossdomain.servicemgr.dao.inf.IServiceOperationDao#insert(org.openo.crossdomain.servicemgr.model.servicemo.ServiceOperation)
     */
    @Override
    public void insert(ServiceOperation serviceOperation) {
        Assert.notNull(serviceOperation, "serviceOperation must not be null.");

        try {
            ServiceOperationMapper serviceOperationMapper = getMapperManager(ServiceOperationMapper.class);
            serviceOperationMapper.insert(serviceOperation);
        } catch(IbatisException ie) {
            logger.error("insert error, oper mysql db error.", ie);
        }
    }

    /**
     * 
     * @see org.openo.crossdomain.servicemgr.dao.inf.IServiceOperationDao#update(org.openo.crossdomain.servicemgr.model.servicemo.ServiceOperation)
     */
    @Override
    public void update(ServiceOperation serviceOperation) {

        Assert.notNull(serviceOperation, "serviceOperation must not be null.");

        try {
            ServiceOperationMapper serviceOperationMapper = getMapperManager(ServiceOperationMapper.class);
            serviceOperationMapper.update(serviceOperation);
        } catch(IbatisException ie) {
            logger.error("update error, oper mysql db error.", ie);
        }
    }

    /**
     * 
     * @see org.openo.crossdomain.servicemgr.dao.inf.IServiceOperationDao#delete(java.lang.String)
     */
    @Override
    public void delete(String serivceId) {
        Assert.notNull(serivceId, "serviceId must not be null.");
        try {
            ServiceOperationMapper serviceOperationMapper = getMapperManager(ServiceOperationMapper.class);
            serviceOperationMapper.delete(serivceId);
        } catch(IbatisException ie) {
            logger.error("delete error, oper mysql db error.", ie);
        }
    }

    /**
     * 
     * @see org.openo.crossdomain.servicemgr.dao.inf.IServiceOperationDao#getServiceOperationsByServiceID(java.lang.String)
     */
    @Override
    public List<ServiceOperation> getServiceOperationsByServiceID(String serivceId) {
        Assert.notNull(serivceId, "serviceId must not be null.");

        try {
            ServiceOperationMapper serviceOperationMapper = getMapperManager(ServiceOperationMapper.class);
            return serviceOperationMapper.getServiceOperationsByServiceID(serivceId);
        } catch(IbatisException ie) {
            logger.error("getServiceOperationsByServiceID error, oper mysql db error.", ie);
        }
        return null;
    }

    /**
     * 
     * @see org.openo.crossdomain.servicemgr.dao.inf.IServiceOperationDao#getServiceOperationByID(java.lang.String, java.lang.String)
     */
    @Override
    public ServiceOperation getServiceOperationByID(String serivceId, String operationId) {
        Assert.notNull(operationId, "operationId must not be null.");

        try {
            ServiceOperationMapper serviceOperationMapper = getMapperManager(ServiceOperationMapper.class);
            return serviceOperationMapper.getServiceOperationByID(serivceId, operationId);
        } catch(IbatisException ie) {
            logger.error("getServiceOperationByID error, oper mysql db error.", ie);
        }
        return null;
    }

    /**
     * 
     * @see org.openo.crossdomain.servicemgr.dao.inf.IServiceOperationDao#deleteHistory()
     */
    @Override
    public void deleteHistory() {
        try {
            ServiceOperationMapper serviceOperationMapper = getMapperManager(ServiceOperationMapper.class);
            serviceOperationMapper.deleteHistory();
        } catch(IbatisException ie) {
            logger.error("deleteHistory error, oper mysql db error.", ie);
        }
    }

    private <T> T getMapperManager(Class<T> type) {
        return mapperManager.getMapper(type, Constant.SVCMGR_DB);
    }

    /**
     * @param mapperManager The mapperManager to set.
     */
    public void setMapperManager(MapperManager mapperManager) {
        this.mapperManager = mapperManager;
    }
}
