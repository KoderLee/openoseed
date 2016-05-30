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
package org.openo.crossdomain.servicemgr.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.openo.crossdomain.servicemgr.dao.inf.IServiceModelDao;
import org.openo.crossdomain.servicemgr.model.servicemo.CategoryInfo;
import org.openo.crossdomain.servicemgr.model.servicemo.ServiceDefInfo;
import org.openo.crossdomain.servicemgr.model.servicemo.ServiceModel;
import org.openo.crossdomain.servicemgr.restrepository.ICatalogProxy;
import org.openo.crossdomain.servicemgr.service.inf.ICatalogInfoMgr;
import org.openo.crossdomain.servicemgr.util.uuid.UUIDUtils;

import org.openo.baseservice.remoteservice.exception.ServiceException;

/**
 * Implementation for catalog manager.<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
public class CatalogInfoMgrImpl implements ICatalogInfoMgr {

    private IServiceModelDao serviceModelDao;

    private ICatalogProxy catalogProxy;

    /**
     * @param serviceModelDao The serviceModelDao to set.
     */
    public void setServiceModelDao(IServiceModelDao serviceModelDao) {
        this.serviceModelDao = serviceModelDao;
    }

    /**
     * @param catalogProxy The catalogProxy to set.
     */
    public void setCatalogProxy(ICatalogProxy catalogProxy) {
        this.catalogProxy = catalogProxy;
    }

    private Map<String, List<ServiceModel>> getServiceModelMap(String tenantID) throws ServiceException {
        Map<String, List<ServiceModel>> serviceModelMap = new HashMap<String, List<ServiceModel>>();
        List<ServiceModel> services = serviceModelDao.getServiceModelByTenantId(tenantID);
        if(!CollectionUtils.isEmpty(services)) {
            for(ServiceModel serviceModel : services) {
                if(serviceModel != null) {
                    String serviceDefId = serviceModel.getService_definition_id();
                    if(!serviceModelMap.containsKey(serviceDefId)) {
                        serviceModelMap.put(serviceDefId, new ArrayList<ServiceModel>());
                    }
                    List<ServiceModel> tempServices = serviceModelMap.get(serviceDefId);
                    tempServices.add(serviceModel);
                }
            }
        }
        return serviceModelMap;
    }

    private List<ServiceDefInfo> getserviceDefInfos(Map<String, List<ServiceModel>> serviceModelMap,
            HttpServletRequest httpRequest) throws ServiceException {
        String defaultCategoryId = UUIDUtils.createBase64Uuid();
        List<ServiceDefInfo> serviceDefInfos = catalogProxy.getServicePackageList(httpRequest, defaultCategoryId);
        List<ServiceDefInfo> serviceDefInfosTemp = new ArrayList<ServiceDefInfo>();
        if(!CollectionUtils.isEmpty(serviceDefInfos)) {
            for(ServiceDefInfo serviceDefInfo : serviceDefInfos) {
                if(serviceDefInfo != null) {
                    String serviceDefId = serviceDefInfo.getServiceDefId();
                    int serviceNum = 0;
                    if(serviceModelMap.containsKey(serviceDefId)) {
                        serviceNum = serviceModelMap.get(serviceDefId).size();
                    }
                    serviceDefInfo.setServiceNum(serviceNum);
                    serviceDefInfosTemp.add(serviceDefInfo);
                }
            }
        }
        return serviceDefInfosTemp;
    }

    private Map<String, CategoryInfo> getCategoryInfoMap(List<ServiceDefInfo> serviceDefInfos) throws ServiceException {

        LinkedHashMap<String, List<ServiceDefInfo>> serviceDefInfoMap =
                new LinkedHashMap<String, List<ServiceDefInfo>>();
        if(!CollectionUtils.isEmpty(serviceDefInfos)) {
            for(ServiceDefInfo serviceDefInfo : serviceDefInfos) {
                List<CategoryInfo> categorys = serviceDefInfo.getCategorys();
                for(CategoryInfo category : categorys) {
                    String categoryId = category.getCategoryId();
                    if(!serviceDefInfoMap.containsKey(categoryId)) {
                        serviceDefInfoMap.put(categoryId, new ArrayList<ServiceDefInfo>());
                    }
                    List<ServiceDefInfo> tempServiceDefInfos = serviceDefInfoMap.get(categoryId);
                    tempServiceDefInfos.add(serviceDefInfo);
                }
            }
        }

        LinkedHashMap<String, CategoryInfo> categoryInfoMap = new LinkedHashMap<String, CategoryInfo>();
        if(!CollectionUtils.isEmpty(serviceDefInfos)) {
            for(ServiceDefInfo serviceDefInfo : serviceDefInfos) {
                List<CategoryInfo> categorys = serviceDefInfo.getCategorys();
                for(CategoryInfo category : categorys) {
                    String categoryId = category.getCategoryId();
                    if(!categoryInfoMap.containsKey(categoryId)) {
                        categoryInfoMap.put(categoryId, new CategoryInfo());
                    }
                    CategoryInfo tempCategoryInfo = categoryInfoMap.get(categoryId);
                    tempCategoryInfo.setServiceDefInfos(serviceDefInfoMap.get(categoryId));
                    tempCategoryInfo.setCategoryName(category.getCategoryName());
                }
            }
        }
        return categoryInfoMap;
    }

    @Override
    public List<CategoryInfo> getCategoryInfos(String tenantID, HttpServletRequest httpRequest) throws ServiceException {

        Map<String, List<ServiceModel>> serviceModelMap = getServiceModelMap(tenantID);

        List<ServiceDefInfo> serviceDefInfos = getserviceDefInfos(serviceModelMap, httpRequest);

        Map<String, CategoryInfo> categoryInfoMap = getCategoryInfoMap(serviceDefInfos);

        List<CategoryInfo> categoryInfos = new ArrayList<CategoryInfo>();
        for(Entry<String, CategoryInfo> entry : categoryInfoMap.entrySet()) {
            CategoryInfo categoryInfo = new CategoryInfo();
            categoryInfo.setCategoryId(entry.getKey());
            categoryInfo.setCategoryName(entry.getValue().getCategoryName());
            categoryInfo.setServiceDefInfos(entry.getValue().getServiceDefInfos());
            categoryInfos.add(categoryInfo);
        }
        return categoryInfos;
    }
}
