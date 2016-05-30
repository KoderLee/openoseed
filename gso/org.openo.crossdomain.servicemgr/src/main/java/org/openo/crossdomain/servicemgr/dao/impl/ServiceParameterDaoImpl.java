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

import org.apache.ibatis.exceptions.IbatisException;
import org.openo.crossdomain.servicemgr.constant.Constant;
import org.openo.crossdomain.servicemgr.dao.inf.IServiceListParameterDao;
import org.openo.crossdomain.servicemgr.dao.inf.IServiceParameterDao;
import org.openo.crossdomain.servicemgr.exception.ErrorCode;
import org.openo.crossdomain.servicemgr.mapper.ServiceParameterMapper;
import org.openo.crossdomain.servicemgr.model.servicemo.ServiceListParameter;
import org.openo.crossdomain.servicemgr.model.servicemo.ServiceModel;
import org.openo.crossdomain.servicemgr.model.servicemo.ServiceParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import org.openo.baseservice.mybatis.session.MapperManager;
import org.openo.baseservice.remoteservice.exception.ServiceException;

/**
 * Implementation for database operation of service parameter.<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
@SuppressWarnings("deprecation")
public class ServiceParameterDaoImpl implements IServiceParameterDao {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private MapperManager mapperManager;

    private IServiceListParameterDao serviceListParameterDao;

    /**
     * 
     * @see org.openo.crossdomain.servicemgr.dao.inf.IServiceParameterDao#insert(ServiceParameter)
     */
    @Override
    public void insert(ServiceParameter serviceParameter) throws ServiceException {
        Assert.notNull(serviceParameter, "serviceParameter must not be null.");

        try {
            ServiceParameterMapper serviceParameterMapper = getMapperManager(ServiceParameterMapper.class);
            serviceParameterMapper.insert(serviceParameter);
            List<ServiceListParameter> listParameters = serviceParameter.getParameterGroup();
            if(!CollectionUtils.isEmpty(listParameters)) {
                for(ServiceListParameter serviceListParameter : listParameters) {
                    serviceListParameterDao.insert(serviceListParameter);
                }
            }
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
     * @see org.openo.crossdomain.servicemgr.dao.inf.IServiceParameterDao#delete(List)
     */
    @Override
    public void delete(List<Integer> ids) throws ServiceException {
        try {
            ServiceParameterMapper serviceParameterMapper = getMapperManager(ServiceParameterMapper.class);
            serviceParameterMapper.delete(ids);
        } catch(IbatisException ie) {
            logger.error("delete error, oper mysql db error.", ie);
            throw new ServiceException(ErrorCode.SVCMGR_OPER_MYSQL_DB_ERROR, "oper mysql db error");
        } catch(RuntimeException e) {
            logger.error("delete error, oper db error.", e);
            throw new ServiceException(ErrorCode.SVCMGR_OPER_DB_ERROR, "oper db error");
        }
    }

    /**
     * 
     * @see org.openo.crossdomain.servicemgr.dao.inf.IServiceParameterDao#update(ServiceParameter)
     */
    @Override
    public void update(ServiceParameter serviceParameter) throws ServiceException {
        try {
            ServiceParameterMapper serviceParameterMapper = getMapperManager(ServiceParameterMapper.class);
            serviceParameterMapper.update(serviceParameter);

            List<ServiceListParameter> serviceListParams = serviceParameter.getParameterGroup();
            if(CollectionUtils.isEmpty(serviceListParams)) {
                return;
            }

            for(ServiceListParameter serviceListParameter : serviceListParams) {
                final String action = serviceListParameter.getAction();
                if(ServiceListParameter.ACTION_TYPE_NUM.ACTION_TYPE_CREATE.equals(action)) {
                    serviceListParameterDao.insert(serviceListParameter);
                } else if(ServiceListParameter.ACTION_TYPE_NUM.ACTION_TYPE_DELETE.equals(action)) {
                    deleteByKeyName(serviceListParameter);
                } else if(ServiceListParameter.ACTION_TYPE_NUM.ACTION_TYPE_UPDATE.equals(action)) {
                    serviceListParameterDao.update(serviceListParameter);
                } else {
                    logger.error("update error, action {} is not exist.", action);
                }
            }
        } catch(IbatisException ie) {
            logger.error("update error, oper mysql db error.", ie);
            throw new ServiceException(ErrorCode.SVCMGR_OPER_MYSQL_DB_ERROR, "oper mysql db error");
        } catch(RuntimeException e) {
            logger.error("update error, oper db error.", e);
            throw new ServiceException(ErrorCode.SVCMGR_OPER_DB_ERROR, "oper db error");
        }
    }

    /**
     * Delete parameter by the name of parameter and the name of group.<br/>
     *
     * @param serviceListParameter parameter that the type is list.
     * @throws ServiceException
     * @since crossdomain 0.5
     */
    public void deleteByKeyName(ServiceListParameter serviceListParameter) throws ServiceException {
        serviceListParameterDao.deleteByKeyName(serviceListParameter.getService_id(),
                serviceListParameter.getParamgroup_name(), serviceListParameter.getKey_name());
    }

    /**
     * 
     * @see org.openo.crossdomain.servicemgr.dao.inf.IServiceParameterDao#getServiceParameterById(String)
     */
    @Override
    public ServiceParameter getServiceParameterById(String id) throws ServiceException {
        try {
            ServiceParameterMapper serviceParameterMapper = getMapperManager(ServiceParameterMapper.class);
            return serviceParameterMapper.getServiceParameterById(id);
        } catch(IbatisException ie) {
            logger.error("getServiceParameterById error, oper mysql db error.", ie);
            throw new ServiceException(ErrorCode.SVCMGR_OPER_MYSQL_DB_ERROR, "oper mysql db error");
        } catch(RuntimeException e) {
            logger.error("getServiceParameterById error, oper db error.", e);
            throw new ServiceException(ErrorCode.SVCMGR_OPER_DB_ERROR, "oper db error");
        }
    }

    /**
     * 
     * @see org.openo.crossdomain.servicemgr.dao.inf.IServiceParameterDao#getServiceParameterList(String, List)
     */
    @Override
    public List<ServiceParameter> getServiceParameterList(String tenant_id, List<String> svcIDs)
            throws ServiceException {
        try {
            ServiceParameterMapper serviceParameterMapper = getMapperManager(ServiceParameterMapper.class);
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("svcIDs", svcIDs);
            params.put("tenant_id", tenant_id);
            List<ServiceParameter> svcParams = serviceParameterMapper.getServiceParameterList(params);

            if(CollectionUtils.isEmpty(svcParams)) {
                return null;
            }

            List<ServiceListParameter> serviceListParameters =
                    serviceListParameterDao.getServiceListParameterList(svcIDs);
            for(ServiceParameter svcParam : svcParams) {
                if(svcParam.getParameter_type().equals(ServiceParameter.PARAM_TYPE_NUM.PARAM_TYPE_SIMPLE)) {
                    continue;
                }

                svcParam.setParameterGroup(processSvcListParams(svcParam, serviceListParameters));
            }

            return svcParams;
        } catch(IbatisException ie) {
            logger.error("getServiceParameterList error, oper mysql db error.", ie);
            throw new ServiceException(ErrorCode.SVCMGR_OPER_MYSQL_DB_ERROR, "oper mysql db error");
        } catch(RuntimeException e) {
            logger.error("getServiceParameterList error, oper db error.", e);
            throw new ServiceException(ErrorCode.SVCMGR_OPER_DB_ERROR, "oper db error");
        }
    }

    /**
     * Get list of service parameter that need to be operated.<br/>
     *
     * @param svcParam service parameter
     * @param serviceListParameters parameter that the type is list
     * @return list of parameter that the type is list
     * @since crossdomain 0.5
     */
    public List<ServiceListParameter> processSvcListParams(ServiceParameter svcParam,
            List<ServiceListParameter> serviceListParameters) {
        List<ServiceListParameter> svcListParams = new ArrayList<ServiceListParameter>();
        for(ServiceListParameter svcListparam : serviceListParameters) {
            if(svcParam.getService_id().equals(svcListparam.getService_id())
                    && svcParam.getParameter_name().equals(svcListparam.getParamgroup_name())) {
                svcListParams.add(svcListparam);
            }
        }
        return svcListParams;
    }

    /**
     * 
     * @see org.openo.crossdomain.servicemgr.dao.inf.IServiceParameterDao#deleteByServiceID(String, String)
     */
    @Override
    public void deleteByServiceID(String tenant_id, String service_id) throws ServiceException {
        try {
            ServiceParameterMapper serviceParameterMapper = getMapperManager(ServiceParameterMapper.class);
            serviceParameterMapper.deleteByServiceID(tenant_id, service_id);
            serviceListParameterDao.deleteByServiceID(service_id);

            logger.info("deleteByServiceID Service by tenantID {} and serviceID {}", tenant_id, service_id);
        } catch(IbatisException ie) {
            logger.error("deleteByServiceID error, oper mysql db error.", ie);
            throw new ServiceException(ErrorCode.SVCMGR_OPER_MYSQL_DB_ERROR, "oper mysql db error");
        } catch(RuntimeException e) {
            logger.error("deleteByServiceID error, oper db error.", e);
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

    /**
     * @param serviceListParameterDao The serviceListParameterDao to set.
     */
    public void setServiceListParameterDao(IServiceListParameterDao serviceListParameterDao) {
        this.serviceListParameterDao = serviceListParameterDao;
    }
}
