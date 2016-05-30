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
import java.util.List;

import org.apache.ibatis.exceptions.IbatisException;
import org.openo.crossdomain.servicemgr.constant.Constant;
import org.openo.crossdomain.servicemgr.dao.inf.IServiceModelCache;
import org.openo.crossdomain.servicemgr.dao.inf.IServiceModelDao;
import org.openo.crossdomain.servicemgr.dao.inf.IServiceParameterDao;
import org.openo.crossdomain.servicemgr.exception.ErrorCode;
import org.openo.crossdomain.servicemgr.mapper.ServiceModelMapper;
import org.openo.crossdomain.servicemgr.model.servicemo.ServiceModel;
import org.openo.crossdomain.servicemgr.model.servicemo.ServiceParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import org.openo.baseservice.mybatis.session.MapperManager;
import org.openo.baseservice.remoteservice.exception.ServiceException;

/**
 * Implementation for database operation of service model.<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
@SuppressWarnings("deprecation")
public class ServiceModelDaoImpl implements IServiceModelDao {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private MapperManager mapperManager;

    private IServiceModelCache serviceModelCache;

    private IServiceParameterDao serviceParameterDao;

    /**
     * 
     * @see org.openo.crossdomain.servicemgr.dao.inf.IServiceModelDao#insert(ServiceModel)
     */
    @Override
    public void insert(ServiceModel serviceModel) throws ServiceException {
        Assert.notNull(serviceModel, "serviceModel must not be null.");

        try {
            ServiceModelMapper serviceModelMapper = getMapperManager(ServiceModelMapper.class);
            serviceModelMapper.insert(serviceModel);
            List<ServiceParameter> parameters = serviceModel.getParameters();
            if(!CollectionUtils.isEmpty(parameters)) {
                for(ServiceParameter serviceParameter : parameters) {
                    serviceParameterDao.insert(serviceParameter);
                }
            }
            serviceModelCache.set(serviceModel);
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
     * @see org.openo.crossdomain.servicemgr.dao.inf.IServiceModelDao#update(ServiceModel)
     */
    @Override
    public void update(ServiceModel serviceModel) throws ServiceException {
        Assert.notNull(serviceModel, "serviceModel must not be null.");
        Assert.notNull(serviceModel.getTenant_id(), "tenantID must not be null.");
        Assert.notNull(serviceModel.getService_id(), "serviceID must not be null.");

        try {
            serviceModelCache.deleteByID(serviceModel.getTenant_id(), serviceModel.getService_id());

            ServiceModelMapper serviceModelMapper = getMapperManager(ServiceModelMapper.class);
            serviceModelMapper.update(serviceModel);

            List<ServiceParameter> serviceParams = serviceModel.getParameters();
            if(!CollectionUtils.isEmpty(serviceParams)) {
                for(ServiceParameter serviceParameter : serviceParams) {
                    serviceParameterDao.update(serviceParameter);
                }
            }

            ServiceModel updateSvcModel =
                    getServiceModelByServiceId(serviceModel.getTenant_id(), serviceModel.getService_id());
            serviceModelCache.set(updateSvcModel);
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
     * @see org.openo.crossdomain.servicemgr.dao.inf.IServiceModelDao#getServiceModelByServiceId(String, String)
     */
    @Override
    public ServiceModel getServiceModelByServiceId(String tenant_id, String service_id) throws ServiceException {
        Assert.hasLength(tenant_id, "tenantID must not be null or empty.");
        Assert.hasLength(service_id, "serviceID must not be null or empty.");

        try {
            ServiceModel serviceModel = serviceModelCache.getServiceModelById(tenant_id, service_id);
            if(null != serviceModel) {
                return serviceModel;
            }
            ServiceModelMapper serviceModelMapper = getMapperManager(ServiceModelMapper.class);
            List<ServiceModel> serviceModels = serviceModelMapper.getServiceModelByServiceId(tenant_id, service_id);
            if(!CollectionUtils.isEmpty(serviceModels)) {
                serviceModel = serviceModels.iterator().next();
                List<String> svcIDs = new ArrayList<String>();
                svcIDs.add(service_id);
                List<ServiceParameter> serviceParameters =
                        serviceParameterDao.getServiceParameterList(tenant_id, svcIDs);
                if(!CollectionUtils.isEmpty(serviceParameters)) {
                    serviceModel.setParameters(serviceParameters);
                }
                serviceModelCache.setnx(serviceModel);
                return serviceModel;
            }

            return null;
        } catch(IbatisException ie) {
            logger.error("getServiceModelByServiceId error, oper mysql db error.", ie);
            throw new ServiceException(ErrorCode.SVCMGR_OPER_MYSQL_DB_ERROR, "oper mysql db error");
        } catch(RuntimeException e) {
            logger.error("getServiceModelByServiceId error, oper db error.", e);
            throw new ServiceException(ErrorCode.SVCMGR_OPER_DB_ERROR, "oper db error");
        }
    }

    /**
     * 
     * @see org.openo.crossdomain.servicemgr.dao.inf.IServiceModelDao#getServiceModelByTenantId(String)
     */
    @Override
    public List<ServiceModel> getServiceModelByTenantId(String tenant_id) throws ServiceException {
        Assert.hasLength(tenant_id, "tenantID must not be null or empty.");

        try {
            List<ServiceModel> serviceModels = serviceModelCache.getServiceModelListByTenantId(tenant_id);
            if(!CollectionUtils.isEmpty(serviceModels)) {
                return serviceModels;
            }

            ServiceModelMapper serviceModelMapper = getMapperManager(ServiceModelMapper.class);
            List<ServiceModel> serviceDbs = serviceModelMapper.getServiceModelByTenantId(tenant_id);
            if(CollectionUtils.isEmpty(serviceDbs)) {
                return new ArrayList<ServiceModel>();
            }

            List<String> svcIDs = new ArrayList<String>();
            for(ServiceModel serviceModel : serviceDbs) {
                svcIDs.add(serviceModel.getService_id());
            }

            List<ServiceParameter> serviceParameters = serviceParameterDao.getServiceParameterList(tenant_id, svcIDs);

            if(!CollectionUtils.isEmpty(serviceParameters)) {
                for(ServiceModel serviceModel : serviceDbs) {
                    List<ServiceParameter> svcParams = new ArrayList<ServiceParameter>();
                    for(ServiceParameter svcParam : serviceParameters) {
                        if(serviceModel.getService_id().equals(svcParam.getService_id())) {
                            svcParams.add(svcParam);
                        }
                    }
                    serviceModel.setParameters(svcParams);
                }
            }

            serviceModelCache.insert(tenant_id, serviceDbs);
            return serviceDbs;
        } catch(IbatisException ie) {
            logger.error("getServiceModelByTenantId error, oper mysql db error.", ie);
            throw new ServiceException(ErrorCode.SVCMGR_OPER_MYSQL_DB_ERROR, "oper mysql db error");
        } catch(RuntimeException e) {
            logger.error("getServiceModelByTenantId error, oper db error.", e);
            throw new ServiceException(ErrorCode.SVCMGR_OPER_DB_ERROR, "oper db error");
        }
    }

    /**
     * 
     * @see org.openo.crossdomain.servicemgr.dao.inf.IServiceModelDao#deleteByServiceID(String, String)
     */
    @Override
    public void deleteByServiceID(String tenant_id, String service_id) throws ServiceException {
        Assert.hasLength(tenant_id, "tenantID must not be null or empty.");
        Assert.hasLength(service_id, "serviceID must not be null or empty.");

        try {
            serviceModelCache.deleteByID(tenant_id, service_id);

            ServiceModelMapper serviceModelMapper = getMapperManager(ServiceModelMapper.class);
            serviceModelMapper.deleteByServiceID(tenant_id, service_id);
            serviceParameterDao.deleteByServiceID(tenant_id, service_id);
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
     * @see org.openo.crossdomain.servicemgr.dao.inf.IServiceModelDao#getServiceModelByTemplateId(String, String)
     */
    @Override
    public List<ServiceModel> getServiceModelByTemplateId(String tenantID, String template_id) throws ServiceException {
        Assert.hasLength(template_id, "tenantID must not be null or empty.");

        try {
            ServiceModelMapper serviceModelMapper = getMapperManager(ServiceModelMapper.class);
            return serviceModelMapper.getServiceModelByTemplateId(tenantID, template_id);
        } catch(IbatisException ie) {
            logger.error("getServiceModelByTenantId error, oper mysql db error.", ie);
            throw new ServiceException(ErrorCode.SVCMGR_OPER_MYSQL_DB_ERROR, "oper mysql db error");
        } catch(RuntimeException e) {
            logger.error("getServiceModelByTenantId error, oper db error.", e);
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
     * @param serviceModelCache The serviceModelCache to set.
     */
    public void setServiceModelCache(IServiceModelCache serviceModelCache) {
        this.serviceModelCache = serviceModelCache;
    }

    /**
     * @param serviceParameterDao The serviceParameterDao to set.
     */
    public void setServiceParameterDao(IServiceParameterDao serviceParameterDao) {
        this.serviceParameterDao = serviceParameterDao;
    }
}
