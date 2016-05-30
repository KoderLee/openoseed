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
package org.openo.crossdomain.servicemgr.model.servicemo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.codehaus.jackson.annotate.JsonProperty;
import org.openo.crossdomain.servicemgr.util.validate.ValidateUtil;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.crossdomain.commsvc.common.util.jsonutil.JsonUtil;

/**
 * Service definition.<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
public class ServiceDefInfo {

    private String serviceDefId;

    @JsonProperty("name")
    private String serviceName;

    @JsonProperty("description")
    private String serviceDes;

    private String serviceType;

    private int serviceNum;

    private String domain;

    private String status;

    private String version;

    private static final String CATALOG_SERVICEDEFS_DATA_KEY = "data";

    private static final String SERVICE_TEMPLATES_KEY = "templates";

    private static final String CATALOG_REST_URL_CATEGORIES = "categories";

    private static final String CATALOG_CATEGORY_DEFAULT_GROUP = "DefaultGroup";

    private List<TemplateInfo> templates = new ArrayList<TemplateInfo>();

    private List<CategoryInfo> categorys = new ArrayList<CategoryInfo>();

    
    /**
     * Get template by ID.<br/>
     *
     * @param templateID template ID
     * @return template
     * @since crossdomain 0.5
     */
    public TemplateInfo getTemplateInfoByTempalteID(String templateID) {
        for(TemplateInfo template : templates) {
            if(template.getTemplateId().equals(templateID)) {
                return template;
            }
        }
        return null;
    }

    public List<CategoryInfo> getCategorys() {
        return categorys;
    }

    public void setCategorys(List<CategoryInfo> categorys) {
        this.categorys = categorys;
    }

    /**
     * @return Returns the domain.
     */
    public String getDomain() {
        return domain;
    }

    /**
     * @param domain The domain to set.
     */
    public void setDomain(String domain) {
        this.domain = domain;
    }

    /**
     * @return Returns the status.
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status The status to set.
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return Returns the serviceDec.
     */
    public String getServiceDes() {
        return serviceDes;
    }

    /**
     * @param serviceDec The serviceDec to set.
     */
    public void setServiceDes(String serviceDes) {
        this.serviceDes = serviceDes;
    }

    /**
     * @return Returns the serviceNum.
     */
    public int getServiceNum() {
        return serviceNum;
    }

    /**
     * @param serviceNum The serviceNum to set.
     */
    public void setServiceNum(int serviceNum) {
        this.serviceNum = serviceNum;
    }

    public ServiceDefInfo() {
        super();
    }

    /**
     * Constructor.<br/>
     * @param serviceDefId service definition ID
     * @param serviceName service Name
     * @param serviceDes service description
     * @param domain service domain
     * @param status service status
     * @param serviceType type of service
     * @param version service version
     * @param serviceNum the number of service
     * @param templates templates
     * @param categorys categories
     */
    public ServiceDefInfo(String serviceDefId, String serviceName, String serviceDes, String domain, String status,
            String serviceType, String version, int serviceNum, List<TemplateInfo> templates,
            List<CategoryInfo> categorys) {
        super();
        this.serviceDefId = serviceDefId;
        this.serviceName = serviceName;
        this.serviceDes = serviceDes;
        this.domain = domain;
        this.status = status;
        this.serviceType = serviceType;
        this.version = version;
        this.serviceNum = serviceNum;
        this.templates = templates;
        this.categorys = categorys;
    }

    /**
     * @return Returns the serviceDefId.
     */
    public String getServiceDefId() {
        return serviceDefId;
    }

    /**
     * @param serviceDefId The serviceDefId to set.
     */
    public void setServiceDefId(String serviceDefId) {
        this.serviceDefId = serviceDefId;
    }

    /**
     * @return Returns the serviceName.
     */
    public String getServiceName() {
        return serviceName;
    }

    /**
     * @param serviceName The serviceName to set.
     */
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    /**
     * @return Returns the templates.
     */
    public List<TemplateInfo> getTemplates() {
        return templates;
    }

    /**
     * @param templates The templates to set.
     */
    public void setTemplates(List<TemplateInfo> templates) {
        this.templates = templates;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    private static List<TemplateInfo> getTemplateInfoList(List<Map<String, String>> templateLists)
            throws ServiceException {
        List<TemplateInfo> templateInfos = new ArrayList<TemplateInfo>();
        if(!CollectionUtils.isEmpty(templateLists)) {
            for(Map<String, String> templateList : templateLists) {
                if(templateList != null) {
                    TemplateInfo templateInfo = JsonUtil.unMarshal(JsonUtil.marshal(templateList), TemplateInfo.class);
                    templateInfos.add(templateInfo);
                }
            }
        }
        return templateInfos;
    }

    private static List<CategoryInfo> getCategoryInfoList(List<Map<String, String>> categoryLists,
            String defaultCategoryId) throws ServiceException {
        List<CategoryInfo> categoryInfos = new ArrayList<CategoryInfo>();

        if(!CollectionUtils.isEmpty(categoryLists)) {
            for(Map<String, String> categoryList : categoryLists) {
                if(categoryList == null) {
                    continue;
                }
                CategoryInfo categoryInfo = JsonUtil.unMarshal(JsonUtil.marshal(categoryList), CategoryInfo.class);
                categoryInfos.add(categoryInfo);
            }
        }
        if(CollectionUtils.isEmpty(categoryLists)) {
            CategoryInfo categoryInfo = new CategoryInfo();
            categoryInfo.setCategoryId(defaultCategoryId);
            categoryInfo.setCategoryName(CATALOG_CATEGORY_DEFAULT_GROUP);
            categoryInfos.add(categoryInfo);
        }
        return categoryInfos;
    }

    private static List<ServiceDefInfo> getServiceDefInfoList(List<Map<String, Object>> serviceDefDatas,
            String defaultCategoryId) throws ServiceException {
        List<ServiceDefInfo> serviceDefInfos = new ArrayList<ServiceDefInfo>();
        if(!CollectionUtils.isEmpty(serviceDefDatas)) {
            for(Map<String, Object> serviceDefData : serviceDefDatas) {
                if(serviceDefData != null) {
                    ServiceDefInfo serviceDefInfo =
                            JsonUtil.unMarshal(JsonUtil.marshal(serviceDefData), ServiceDefInfo.class);

                    if(serviceDefData.containsKey(CATALOG_REST_URL_CATEGORIES)
                            && serviceDefData.get(CATALOG_REST_URL_CATEGORIES) == null) {
                        List<CategoryInfo> categoryInfos = new ArrayList<CategoryInfo>();
                        CategoryInfo categoryInfo = new CategoryInfo();
                        categoryInfo.setCategoryId(defaultCategoryId);
                        categoryInfo.setCategoryName(CATALOG_CATEGORY_DEFAULT_GROUP);
                        categoryInfos.add(categoryInfo);
                        serviceDefInfo.setCategorys(categoryInfos);
                    }

                    if(serviceDefData.containsKey(CATALOG_REST_URL_CATEGORIES)
                            && serviceDefData.get(CATALOG_REST_URL_CATEGORIES) instanceof List) {
                        @SuppressWarnings("unchecked")
                        List<Map<String, String>> categoryLists =
                                (List<Map<String, String>>)serviceDefData.get(CATALOG_REST_URL_CATEGORIES);
                        serviceDefInfo.setCategorys(getCategoryInfoList(categoryLists, defaultCategoryId));
                    }
                    serviceDefInfos.add(serviceDefInfo);
                }
            }
        }

        return serviceDefInfos;
    }
    
    /**
     * Convert service json definition to class structure definition.<br/>
     *
     * @param servicedefDetail the detail of service definition
     * @return  ServiceDefInfo object 
     * @throws ServiceException
     * @since crossdomain 0.5
     */
    @SuppressWarnings("unchecked")
    public static ServiceDefInfo toServiceDefInfoFromJsonStr(String servicedefDetail) throws ServiceException {
        ValidateUtil.assertNotEmpty(servicedefDetail, "servicedefDetail");
        Map<String, Object> jsonObject = JsonUtil.unMarshal(servicedefDetail, Map.class);
        ValidateUtil.assertNotNull(jsonObject, "jsonObject");
        ServiceDefInfo serviceDefInfo = JsonUtil.unMarshal(JsonUtil.marshal(jsonObject), ServiceDefInfo.class);

        if(jsonObject.containsKey(SERVICE_TEMPLATES_KEY) && jsonObject.get(SERVICE_TEMPLATES_KEY) instanceof List) {
            List<Map<String, String>> templateLists = (List<Map<String, String>>)jsonObject.get(SERVICE_TEMPLATES_KEY);
            serviceDefInfo.setTemplates(getTemplateInfoList(templateLists));
        }
        return serviceDefInfo;
    }

    /**
     * Convert service json definition to the collection of ServiceDefInfo object.<br/>
     *
     * @param servicedefs definition of services
     * @param defaultCategoryId category ID
     * @return the collection of ServiceDefInfo object
     * @throws ServiceException
     * @since crossdomain 0.5
     */
    @SuppressWarnings("unchecked")
    public static List<ServiceDefInfo> toServiceDefInfoListFromJsonStr(String servicedefs, String defaultCategoryId)
            throws ServiceException {
        ValidateUtil.assertNotEmpty(servicedefs, "servicedefs");
        Map<String, Object> jsonObject = JsonUtil.unMarshal(servicedefs, Map.class);
        ValidateUtil.assertNotNull(jsonObject, "jsonObject");

        List<ServiceDefInfo> serviceDefInfos = new ArrayList<ServiceDefInfo>();

        if(jsonObject.containsKey(CATALOG_SERVICEDEFS_DATA_KEY)
                && jsonObject.get(CATALOG_SERVICEDEFS_DATA_KEY) instanceof List) {
            List<Map<String, Object>> serviceDefDatas =
                    (List<Map<String, Object>>)jsonObject.get(CATALOG_SERVICEDEFS_DATA_KEY);
            serviceDefInfos = getServiceDefInfoList(serviceDefDatas, defaultCategoryId);
        }
        return serviceDefInfos;
    }
}
