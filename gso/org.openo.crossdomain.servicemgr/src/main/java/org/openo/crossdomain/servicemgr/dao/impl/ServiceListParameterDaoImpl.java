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
import org.openo.crossdomain.servicemgr.dao.inf.IServiceListParameterDao;
import org.openo.crossdomain.servicemgr.exception.ErrorCode;
import org.openo.crossdomain.servicemgr.mapper.ServiceListParameterMapper;
import org.openo.crossdomain.servicemgr.model.servicemo.ServiceListParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import org.openo.baseservice.mybatis.session.MapperManager;
import org.openo.baseservice.remoteservice.exception.ServiceException;

/**
 * Implement for operating list parameter of service. <br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
@SuppressWarnings("deprecation")
public class ServiceListParameterDaoImpl implements IServiceListParameterDao {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private MapperManager mapperManager;

    /**
     * 
     * @see org.openo.crossdomain.servicemgr.dao.inf.IServiceListParameterDao#insert(ServiceListParameter)
     */
    @Override
    public void insert(ServiceListParameter serviceListParameter) throws ServiceException {
        Assert.notNull(serviceListParameter, "serviceParameter must not be null.");

        try {
            ServiceListParameterMapper serviceListParameterMapper = getMapperManager(ServiceListParameterMapper.class);
            serviceListParameterMapper.insert(serviceListParameter);
        } catch(IbatisException ie) {
            logger.error("insert error, oper mysql db error.", ie);
            throw new ServiceException(ErrorCode.SVCMGR_OPER_MYSQL_DB_ERROR, "oper mysql db error");
        } catch(RuntimeException e) {
            logger.error("insert error, oper db error.", e);
            throw new ServiceException(ErrorCode.SVCMGR_OPER_DB_ERROR, "oper db error");
        }
    }

    /**
     * 
     * @see org.openo.crossdomain.servicemgr.dao.inf.IServiceListParameterDao#update(ServiceListParameter)
     */
    @Override
    public void update(ServiceListParameter serviceListParameter) throws ServiceException {
        try {
            ServiceListParameterMapper serviceListParameterMapper = getMapperManager(ServiceListParameterMapper.class);
            serviceListParameterMapper.update(serviceListParameter);
        } catch(IbatisException ie) {
            logger.error("update error, oper mysql db error.", ie);
            throw new ServiceException(ErrorCode.SVCMGR_OPER_MYSQL_DB_ERROR, "oper mysql db error");
        } catch(RuntimeException e) {
            logger.error("update error, oper db error.", e);
            throw new ServiceException(ErrorCode.SVCMGR_OPER_DB_ERROR, "oper db error");
        }
    }

    /**
     * 
     * @see org.openo.crossdomain.servicemgr.dao.inf.IServiceListParameterDao#getServiceListParameterByKeyName(String, String, String)
     */
    @Override
    public ServiceListParameter getServiceListParameterByKeyName(String service_id, String paramgroup_name,
            String key_name) throws ServiceException {
        try {
            ServiceListParameterMapper serviceListParameterMapper = getMapperManager(ServiceListParameterMapper.class);
            return serviceListParameterMapper.getServiceListParameterByKeyName(service_id, paramgroup_name, key_name);
        } catch(IbatisException ie) {
            logger.error("getServiceListParameterByLogicalName error, oper mysql db error.", ie);
            throw new ServiceException(ErrorCode.SVCMGR_OPER_MYSQL_DB_ERROR, "oper mysql db error");
        } catch(RuntimeException e) {
            logger.error("getServiceListParameterByLogicalName error, oper db error.", e);
            throw new ServiceException(ErrorCode.SVCMGR_OPER_DB_ERROR, "oper db error");
        }
    }

    /**
     * 
     * @see org.openo.crossdomain.servicemgr.dao.inf.IServiceListParameterDao#getServiceListParameterList(List)
     */
    @Override
    public List<ServiceListParameter> getServiceListParameterList(List<String> svcIDs) throws ServiceException {
        try {
            ServiceListParameterMapper serviceListParameterMapper = getMapperManager(ServiceListParameterMapper.class);
            return serviceListParameterMapper.getServiceListParameterList(svcIDs);
        } catch(IbatisException ie) {
            logger.error("getServiceListParameterList error, oper mysql db error.", ie);
            throw new ServiceException(ErrorCode.SVCMGR_OPER_MYSQL_DB_ERROR, "oper mysql db error");
        } catch(RuntimeException e) {
            logger.error("getServiceListParameterList error, oper db error.", e);
            throw new ServiceException(ErrorCode.SVCMGR_OPER_DB_ERROR, "oper db error");
        }
    }

    /**
     * 
     * @see org.openo.crossdomain.servicemgr.dao.inf.IServiceListParameterDao#deleteByServiceID(String)
     */
    @Override
    public void deleteByServiceID(String service_id) throws ServiceException {
        try {
            ServiceListParameterMapper serviceListParameterMapper = getMapperManager(ServiceListParameterMapper.class);
            serviceListParameterMapper.deleteByServiceID(service_id);

            logger.info("deleteByServiceID Service by serviceID {}", service_id);
        } catch(IbatisException ie) {
            logger.error("deleteByServiceID error, oper mysql db error.", ie);
            throw new ServiceException(ErrorCode.SVCMGR_OPER_MYSQL_DB_ERROR, "oper mysql db error");
        } catch(RuntimeException e) {
            logger.error("deleteByServiceID error, oper db error.", e);
            throw new ServiceException(ErrorCode.SVCMGR_OPER_DB_ERROR, "oper db error");
        }
    }

    /**
     * 
     * @see org.openo.crossdomain.servicemgr.dao.inf.IServiceListParameterDao#deleteByKeyName(String, String, String)
     */
    @Override
    public void deleteByKeyName(String service_id, String paramgroup_name, String key_name) throws ServiceException {
        try {
            ServiceListParameterMapper serviceListParameterMapper = getMapperManager(ServiceListParameterMapper.class);
            serviceListParameterMapper.deleteByKeyName(service_id, paramgroup_name, key_name);

            logger.info("deleteByKeyName Service by paramgroupName {} and keyName {}", paramgroup_name, key_name);
        } catch(IbatisException ie) {
            logger.error("deleteByKeyName error, oper mysql db error.", ie);
            throw new ServiceException(ErrorCode.SVCMGR_OPER_MYSQL_DB_ERROR, "oper mysql db error");
        } catch(RuntimeException e) {
            logger.error("deleteByKeyName error, oper db error.", e);
            throw new ServiceException(ErrorCode.SVCMGR_OPER_DB_ERROR, "oper db error");
        }
    }

    private <T> T getMapperManager(Class<T> type) {
        return mapperManager.getMapper(type, Constant.SVCMGR_DB);
    }

    /**
     * @return Returns the mapperManager.
     */
    public MapperManager getMapperManager() {
        return mapperManager;
    }

    /**
     * @param mapperManager The mapperManager to set.
     */
    public void setMapperManager(MapperManager mapperManager) {
        this.mapperManager = mapperManager;
    }
}
