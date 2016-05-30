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
package org.openo.crossdomain.servicemgr.restrepository.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openo.crossdomain.servicemgr.constant.Constant;
import org.openo.crossdomain.servicemgr.model.roamo.ServiceModelConstant;
import org.openo.crossdomain.servicemgr.model.servicemo.ServiceDefInfo;
import org.openo.crossdomain.servicemgr.model.servicemo.ServiceModel;
import org.openo.crossdomain.servicemgr.restrepository.ICatalogProxy;
import org.openo.crossdomain.servicemgr.util.http.HttpClientUtil;
import org.openo.crossdomain.servicemgr.util.http.ResponseUtils;
import org.openo.crossdomain.servicemgr.util.validate.ValidateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.Assert;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;

/**
 * Implementation for catalog proxy.<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
public class CatalogProxyImpl implements ICatalogProxy {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String CATALOG_REST_URL_PREFIX = "/rest/catalog/v1/servicedefs/";

    private static final String SVC_INSTANCE_ID = "serviceInstanceId";

    private static final String REFERENCES = "references";

    private static final String CATALOG_REST_URL_PREFIXS = "/rest/catalog/v1/";

    /**
     * Catalog REST URL servicedefs
     */
    private static final String CATALOG_REST_URL_SERVICEDEFS = "servicedefs";

    /**
     * Catalog REST URL detail
     */
    private static final String CATALOG_REST_URL_DETAIL = "detail";

    private static final String FORWARD_SLASH = "/";

    /**
     * Catalog REST URL templates
     */
    private static final String CATALOG_REST_URL_TEMPLATES = "templates";

    /**
     * 
     * @see org.openo.crossdomain.servicemgr.restrepository.ICatalogProxy#getTempalteContent(ServiceModel, HttpServletRequest)
     */
    @Override
    public String getTempalteContent(ServiceModel service, HttpServletRequest httpRequest) throws ServiceException {
        ValidateUtil.assertNotNull(service, ServiceModelConstant.RESPONSE_KEY_NUM.SERVICE_KEY);

        logger.info("Get templateContent from Catalog.");

        String url = service.getTemplateUrl();
        ValidateUtil.assertNotNull(url, ServiceModelConstant.VALIDATE_FIELDS_NUM.TEMPLATE_URL);

        Map<String, String> httpHeaders = new HashMap<String, String>();
        httpHeaders.put("Content-Type", "application/json;charset=UTF-8");
        final RestfulResponse response = HttpClientUtil.get(url, httpHeaders, httpRequest);

        return ResponseUtils.transferResponse(response);
    }

    /**
     * 
     * @see org.openo.crossdomain.servicemgr.restrepository.ICatalogProxy#createReference(ServiceModel, HttpServletRequest)
     */
    @Override
    public void createReference(ServiceModel service, HttpServletRequest httpRequest) throws ServiceException {
        ValidateUtil.assertNotNull(service, ServiceModelConstant.RESPONSE_KEY_NUM.SERVICE_KEY);

        logger.info("Create reference by Catalog, serviceinst_id is {}.", service.getService_id());

        Map<String, String> reqObjMap = new HashMap<String, String>();
        reqObjMap.put(SVC_INSTANCE_ID, service.getService_id());

        String url =
                new StringBuilder(CATALOG_REST_URL_PREFIX).append(service.getService_definition_id())
                        .append(Constant.FORWARD_SLASH).append(REFERENCES).toString();

        final RestfulResponse response = HttpClientUtil.post(url, reqObjMap, httpRequest);

        ResponseUtils.checkResonseAndThrowException(response);
    }

    /**
     * 
     * @see org.openo.crossdomain.servicemgr.restrepository.ICatalogProxy#deleteReference(ServiceModel, HttpServletRequest)
     */
    @Override
    public void deleteReference(ServiceModel service, HttpServletRequest httpRequest) throws ServiceException {
        // /catalog/v1/servicedefs/{servicedef_id}/references/{serviceinst_id}
        ValidateUtil.assertNotNull(service, ServiceModelConstant.RESPONSE_KEY_NUM.SERVICE_KEY);

        logger.info("Delete reference by Catalog, serviceDefId is {}, serviceinst_id is {}.",
                service.getService_definition_id(), service.getService_id());

        String url =
                new StringBuilder(CATALOG_REST_URL_PREFIX).append(service.getService_definition_id())
                        .append(Constant.FORWARD_SLASH).append(REFERENCES).append(Constant.FORWARD_SLASH)
                        .append(service.getService_id()).toString();

        final RestfulResponse response = HttpClientUtil.delete(url, httpRequest);
        ResponseUtils.checkResonseAndThrowException(response);
    }

    /**
     * 
     * @see org.openo.crossdomain.servicemgr.restrepository.ICatalogProxy#deleteReference(ServiceModel, String)
     */
    @Override
    public void deleteReference(ServiceModel service, String tokenStr) throws ServiceException {
        // /catalog/v1/servicedefs/{servicedef_id}/references/{serviceinst_id}
        ValidateUtil.assertNotNull(service, ServiceModelConstant.RESPONSE_KEY_NUM.SERVICE_KEY);

        String url =
                new StringBuilder(CATALOG_REST_URL_PREFIX).append(service.getService_definition_id())
                        .append(Constant.FORWARD_SLASH).append(REFERENCES).append(Constant.FORWARD_SLASH)
                        .append(service.getService_id()).toString();

        final RestfulResponse response = HttpClientUtil.delete(url, tokenStr);
        ResponseUtils.checkResonseAndThrowException(response);
    }

    /**
     * 
     * @see org.openo.crossdomain.servicemgr.restrepository.ICatalogProxy#getTemplateUrlInCatalog(org.openo.crossdomain.servicemgr.model.servicemo.ServiceModel)
     */
    @Override
    public String getTemplateUrlInCatalog(ServiceModel service) {
        // /rest/catalog/v1/servicedefs/{servicedef_id}/templates/{template_id}
        return new StringBuilder(CATALOG_REST_URL_PREFIX).append(service.getService_definition_id())
                .append(Constant.FORWARD_SLASH).append(CATALOG_REST_URL_TEMPLATES).append(Constant.FORWARD_SLASH)
                .append(service.getTemplate_id()).toString();
    }

    /**
     * 
     * @see org.openo.crossdomain.servicemgr.restrepository.ICatalogProxy#getServiceDetail(String, HttpServletRequest)
     */
    public ServiceDefInfo getServiceDetail(String serviceDefId, HttpServletRequest httpRequest) throws ServiceException {
        logger.info("Get ServicePackage DetailInformation from Catalog, serviceDefId is {}.", serviceDefId);
        // /catalog/v1/servicedefs/{servicedef_id}/detail
        Assert.hasLength(serviceDefId, "serviceDefId must not be null or empty.");

        final String url =
                new StringBuilder(CATALOG_REST_URL_PREFIXS).append(CATALOG_REST_URL_SERVICEDEFS).append(FORWARD_SLASH)
                        .append(serviceDefId).append(FORWARD_SLASH).append(CATALOG_REST_URL_DETAIL).toString();

        Map<String, String> httpHeaders = new HashMap<String, String>();
        httpHeaders.put("Content-Type", "application/json;charset=UTF-8");
        final RestfulResponse restfulResponse = HttpClientUtil.get(url, httpHeaders, httpRequest);
        final String responseContent = ResponseUtils.transferResponse(restfulResponse);

        return ServiceDefInfo.toServiceDefInfoFromJsonStr(responseContent);
    }

    /**
     * 
     * @see org.openo.crossdomain.servicemgr.restrepository.ICatalogProxy#getServicePackageList(HttpServletRequest, String)
     */
    @Override
    public List<ServiceDefInfo> getServicePackageList(HttpServletRequest httpRequest, String defaultCategoryId)
            throws ServiceException {
        logger.info("Get ServicePackageList from Catalog.");

        // catalog/v1/servicedefs
        final String url = new StringBuilder(CATALOG_REST_URL_PREFIXS).append(CATALOG_REST_URL_SERVICEDEFS).toString();

        Map<String, String> httpHeaders = new HashMap<String, String>();
        httpHeaders.put("Content-Type", "application/json;charset=UTF-8");
        final RestfulResponse restfulResponse = HttpClientUtil.get(url, httpHeaders, httpRequest);

        final String responseContent = ResponseUtils.transferResponse(restfulResponse);
        return ServiceDefInfo.toServiceDefInfoListFromJsonStr(responseContent, defaultCategoryId);
    }

}
