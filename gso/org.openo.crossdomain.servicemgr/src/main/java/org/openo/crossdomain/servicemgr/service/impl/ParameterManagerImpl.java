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
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.openo.crossdomain.servicemgr.dao.inf.IServiceListParameterDao;
import org.openo.crossdomain.servicemgr.dao.inf.IServiceParameterDao;
import org.openo.crossdomain.servicemgr.exception.ErrorCode;
import org.openo.crossdomain.servicemgr.model.roamo.ServiceModelConstant;
import org.openo.crossdomain.servicemgr.model.roamo.ServiceModelInfo;
import org.openo.crossdomain.servicemgr.model.roamo.ServiceParamConstant;
import org.openo.crossdomain.servicemgr.model.servicemo.ServiceDefInfo;
import org.openo.crossdomain.servicemgr.model.servicemo.ServiceListParameter;
import org.openo.crossdomain.servicemgr.model.servicemo.ServiceModel;
import org.openo.crossdomain.servicemgr.model.servicemo.ServiceParamInfo;
import org.openo.crossdomain.servicemgr.model.servicemo.ServiceParameter;
import org.openo.crossdomain.servicemgr.model.servicemo.TemplateInfo;
import org.openo.crossdomain.servicemgr.restrepository.ICatalogProxy;
import org.openo.crossdomain.servicemgr.service.inf.IParameterManager;
import org.openo.crossdomain.servicemgr.service.inf.IWidgetTypeManager;
import org.openo.crossdomain.servicemgr.util.validate.ValidateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.crossdomain.commsvc.common.util.jsonutil.JsonUtil;

/**
 * Implementation for parameter manager.<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
public class ParameterManagerImpl implements IParameterManager {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * catalog
     */
    private ICatalogProxy catalogProxy;

    private IServiceParameterDao serviceParameterDao;

    private IServiceListParameterDao serviceListParameterDao;

    private IWidgetTypeManager widgetTypeManager;

    /**
     * 
     * @see org.openo.crossdomain.servicemgr.service.inf.IParameterManager#decodeServiceParameter(ServiceModel, ServiceModelInfo, HttpServletRequest)
     */
    public ServiceParamInfo decodeServiceParameter(ServiceModel serviceModel, ServiceModelInfo serviceModelInfo,
            HttpServletRequest httpRequest) throws ServiceException {

        List<ServiceParameter> serviceParameters = ServiceParameter.toServiceModel(serviceModelInfo);
        if(CollectionUtils.isEmpty(serviceParameters)) {
            logger.info("serviceParameters is empty!");
            return null;
        }

        String templateContent = catalogProxy.getTempalteContent(serviceModel, httpRequest);

        String parameterMap = JsonUtil.marshal(serviceModelInfo.getService().getParameters());

        Map<String, Object> temParameterMap =
                widgetTypeManager.getSvcParamWithWidgetType(templateContent, parameterMap, httpRequest);

        if(temParameterMap == null || temParameterMap.isEmpty()) {
            logger.error("Template Parameters is empty!");
            throw new ServiceException(ErrorCode.SVCMGR_TEMPLATE_NOT_EXIST, "Template Parameters is empty!");
        }

        List<ServiceParameter> serviceParametersRsp = new ArrayList<ServiceParameter>();
        parameterFilter(serviceParameters, temParameterMap, serviceParametersRsp);

        ServiceParamInfo svcParamInfo = new ServiceParamInfo();
        svcParamInfo.setParameterList(serviceParametersRsp);
        svcParamInfo.setModify(true);

        return svcParamInfo;
    }

    @SuppressWarnings("unchecked")
    private void parameterFilter(List<ServiceParameter> serviceParameters, Map<String, Object> temParameterMap,
            List<ServiceParameter> serviceParametersRsp) throws ServiceException {
        for(ServiceParameter serviceParameter : serviceParameters) {

            if(!temParameterMap.containsKey(serviceParameter.getParameter_name())) {
                serviceParametersRsp.add(serviceParameter);
                continue;
            }

            Map<String, Object> templateParam =
                    (Map<String, Object>)temParameterMap.get(serviceParameter.getParameter_name());

            if(!CollectionUtils.isEmpty(templateParam)
                    && templateParam.containsKey(ServiceParamConstant.WIDGET_KEY_NUM.WIDGETTYPE)) {
                String paramWidgetType = templateParam.get(ServiceParamConstant.WIDGET_KEY_NUM.WIDGETTYPE).toString();
                if(ServiceParamConstant.WIDGET_TYPE_NUM.NOECHO.equals(paramWidgetType)) {
                    continue;
                }

                if(ServiceParamConstant.WIDGET_TYPE_NUM.TYPE_LIST_RESGROUP.equals(paramWidgetType)) {

                    processResListParameter(serviceParameter, templateParam);
                }

                if(ServiceParamConstant.WIDGET_TYPE_NUM.TYPE_LIST_GROUP.equals(paramWidgetType)) {

                    processListParameter(serviceParameter, templateParam);
                }
            }

            serviceParametersRsp.add(serviceParameter);
        }
    }

    /**
     * 
     * @see org.openo.crossdomain.servicemgr.service.inf.IParameterManager#setServiceListParamValue(ServiceParameter)
     */
    public void setServiceListParamValue(ServiceParameter serviceParameter) throws ServiceException {
        List<ServiceListParameter> svcListParams = serviceParameter.getParameterGroup();
        if(!CollectionUtils.isEmpty(svcListParams)) {
            for(ServiceListParameter svcListParam : svcListParams) {
                svcListParam.setService_id(serviceParameter.getService_id());

                if(!ServiceListParameter.ACTION_TYPE_NUM.ACTION_TYPE_UPDATE.equals(svcListParam.getAction())) {
                    updateParameterValue(svcListParam);
                    continue;
                }

                ServiceListParameter toUpdateSvcListParam = getServiceListParameter(svcListParam);
                if(null == toUpdateSvcListParam) {
                    throw new ServiceException(ErrorCode.SVCMGR_SERVICE_NOT_EXIST, "ServiceListParameter is empty!");
                }

                updateServiceListParameter(svcListParam, toUpdateSvcListParam);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void updateParameterValue(ServiceListParameter svcListParam) throws ServiceException {
        final Map<String, Object> toCrOrDelValue = JsonUtil.unMarshal(svcListParam.getParameter_value(), Map.class);
        toCrOrDelValue.remove(ServiceParamConstant.TEMPLATE_PARAM_KEY_NUM.ACTION);
        svcListParam.setParameter_value(JsonUtil.marshal(toCrOrDelValue));

    }

    private ServiceListParameter getServiceListParameter(ServiceListParameter svcListParam) throws ServiceException {
        return serviceListParameterDao.getServiceListParameterByKeyName(svcListParam.getService_id(),
                svcListParam.getParamgroup_name(), svcListParam.getKey_name());
    }

    @SuppressWarnings("unchecked")
    private void
            updateServiceListParameter(ServiceListParameter svcListParam, ServiceListParameter toUpdateSvcListParam)
                    throws ServiceException {
        final Map<String, Object> toUpdateValue = JsonUtil.unMarshal(svcListParam.getParameter_value(), Map.class);
        toUpdateValue.remove(ServiceParamConstant.TEMPLATE_PARAM_KEY_NUM.ACTION);
        final Map<String, Object> existValue = JsonUtil.unMarshal(toUpdateSvcListParam.getParameter_value(), Map.class);

        for(Map.Entry<String, Object> entry : toUpdateValue.entrySet()) {
            existValue.put(entry.getKey(), entry.getValue());
        }
        svcListParam.setParameter_value(JsonUtil.marshal(existValue));
    }

    /**
     * 
     * @see org.openo.crossdomain.servicemgr.service.inf.IParameterManager#getServiceParamterList(String, String)
     */
    @Override
    public List<ServiceParameter> getServiceParamterList(String tenantID, String serviceID) throws ServiceException {
        Assert.hasLength(tenantID, "tenantID must not be null or empty.");
        Assert.hasLength(serviceID, "serviceID must not be null or empty.");

        List<ServiceParameter> svcParamList = new ArrayList<ServiceParameter>();

        List<String> svcIDs = new ArrayList<String>();
        svcIDs.add(serviceID);
        svcParamList = serviceParameterDao.getServiceParameterList(tenantID, svcIDs);

        if(CollectionUtils.isEmpty(svcParamList)) {
            logger.error("Get ServiceParameterList by tenantID {} and serviceID {} failed!", tenantID, serviceID);
            return null;
        }

        return svcParamList;
    }

    /**
     * 
     * @see org.openo.crossdomain.servicemgr.service.inf.IParameterManager#getServiceParametersByTemplate(String, String, String, HttpServletRequest)
     */
    @Override
    public Object getServiceParametersByTemplate(String tenantID, String servicePackageID, String templateID,
            HttpServletRequest httpRequest) throws ServiceException {
        Assert.hasLength(tenantID, "tenantID must not be null or empty.");
        Assert.hasLength(servicePackageID, "servicePackageID must not be null or empty.");
        Assert.hasLength(templateID, "templateID must not be null or empty.");

        ServiceModel serviceModel = new ServiceModel();
        serviceModel.setTenant_id(tenantID);

        ServiceDefInfo serviceDefInfo = catalogProxy.getServiceDetail(servicePackageID, httpRequest);

        TemplateInfo tempateInf = serviceDefInfo.getTemplateInfoByTempalteID(templateID);
        ValidateUtil.assertNotNull(tempateInf, ErrorCode.SVCMGR_TEMPLATE_NOT_EXIST,
                ServiceModelConstant.VALIDATE_FIELDS_NUM.TEMPLATE_MODEL);

        String templateUrl = tempateInf.getTemplateUrl();
        ValidateUtil.assertNotEmpty(templateUrl, ServiceModelConstant.VALIDATE_FIELDS_NUM.TEMPLATE_URL);
        serviceModel.setTemplateUrl(tempateInf.getTemplateUrl());

        String templateContent = catalogProxy.getTempalteContent(serviceModel, httpRequest);

        return widgetTypeManager.getSvcParamWithWidgetType(templateContent, null, httpRequest);
    }

    /**
     * Progress the list parameters of resource.<br/>
     *
     * @param serviceParameter service parameter
     * @param templateParam parameter of tempalte
     * @throws ServiceException
     * @since crossdomain 0.5
     */
    @SuppressWarnings("unchecked")
    public void processResListParameter(ServiceParameter serviceParameter, Map<String, Object> templateParam)
            throws ServiceException {
        Map<String, Object> mapParam =
                (Map<String, Object>)templateParam.get(ServiceParamConstant.TEMPLATE_PARAM_KEY_NUM.MAP);
        List<ServiceListParameter> resListParams = serviceParameter.getParameterGroup();
        for(ServiceListParameter resListParam : resListParams) {
            Map<String, Object> paramValue = JsonUtil.unMarshal(resListParam.getParameter_value(), Map.class);

            processParamValue(paramValue, mapParam);

            resListParam.setParameter_value(JsonUtil.marshal(paramValue));
        }
    }

    /**
     * Process parameter which type is list.<br/>
     *
     * @param serviceParameter service parameter
     * @param templateParam parameter of template
     * @throws ServiceException
     * @since crossdomain 0.5
     */
    @SuppressWarnings("unchecked")
    public void processListParameter(ServiceParameter serviceParameter, Map<String, Object> templateParam)
            throws ServiceException {
        Map<String, Object> mapParam =
                (Map<String, Object>)templateParam.get(ServiceParamConstant.TEMPLATE_PARAM_KEY_NUM.MAP);
        List<ServiceListParameter> listParams = serviceParameter.getParameterGroup();
        for(ServiceListParameter listParam : listParams) {
            List<Object> paramValue = JsonUtil.unMarshal(listParam.getParameter_value(), List.class);

            processParamValue(paramValue, mapParam);

            listParam.setParameter_value(JsonUtil.marshal(paramValue));
        }
    }

    /**
     * Progress the value of parameter.<br/>
     *
     * @param paramValue the value of parameter
     * @param mapParam the map collection of parameters
     * @throws ServiceException
     * @since crossdomain 0.5
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void processParamValue(Object paramValue, Map<String, Object> mapParam) throws ServiceException {
        if(paramValue instanceof Map) {
            Map<String, Object> paramMap = (Map<String, Object>)paramValue;
            List<String> toRemoveKey = new ArrayList<String>();
            for(Entry<String, Object> entry : paramMap.entrySet()) {

                Map<String, Object> param = (Map<String, Object>)mapParam.get(entry.getKey());

                if(!param.containsKey(ServiceParamConstant.WIDGET_KEY_NUM.WIDGETTYPE)) {
                    continue;
                }

                String paramWidgetType = param.get(ServiceParamConstant.WIDGET_KEY_NUM.WIDGETTYPE).toString();

                if(ServiceParamConstant.WIDGET_TYPE_NUM.NOECHO.equals(paramWidgetType)) {
                    toRemoveKey.add(entry.getKey());
                }

                Object valueObj = entry.getValue();
                if(!(valueObj instanceof List)) {
                    continue;
                }

                List<Map> mapList = (List)valueObj;
                Map<String, Object> listParam =
                        (Map<String, Object>)param.get(ServiceParamConstant.TEMPLATE_PARAM_KEY_NUM.MAP);
                for(Map<String, Object> valueMap : mapList) {
                    processParamValue(valueMap, listParam);
                }
            }

            if(CollectionUtils.isEmpty(toRemoveKey)) {
                return;
            }

            for(String key : toRemoveKey) {
                paramMap.remove(key);
            }
        }

        if(paramValue instanceof List) {
            List<Object> mapList = (List)paramValue;
            for(Object obj : mapList) {
                if(!(obj instanceof Map)) {
                    continue;
                }

                Map<String, Object> objMap = (Map<String, Object>)obj;
                processParamValue(objMap, mapParam);
            }
        }
    }

    /**
     * 
     * @see org.openo.crossdomain.servicemgr.service.inf.IParameterManager#getServiceParameter(ServiceModel, ServiceModelInfo, HttpServletRequest)
     */
    public ServiceParamInfo getServiceParameter(ServiceModel serviceModel, ServiceModelInfo serviceModelInfo,
            HttpServletRequest httpRequest) throws ServiceException {
        ServiceParamInfo serviceParamInfo = decodeServiceParameter(serviceModel, serviceModelInfo, httpRequest);
        if(null == serviceParamInfo) {
            serviceParamInfo = new ServiceParamInfo();
        }

        List<ServiceParameter> serviceParameters = serviceParamInfo.getParameterList();
        if(!CollectionUtils.isEmpty(serviceParameters)) {
            for(ServiceParameter serviceParameter : serviceParameters) {
                serviceParameter.setTenant_id(serviceModel.getTenant_id());
                serviceParameter.setService_id(serviceModel.getService_id());
                serviceParameter.setFlag(ServiceParameter.HIDDEN_FALG_NUM.HIDDEN_FALG_DEFAULT);

                if(CollectionUtils.isEmpty(serviceParameter.getParameterGroup())) {
                    continue;
                }

                for(ServiceListParameter svcListParam : serviceParameter.getParameterGroup()) {
                    svcListParam.setService_id(serviceModel.getService_id());
                }
            }
        }
        return serviceParamInfo;
    }

    /**
     * @param catalogProxy The catalogProxy to set.
     */
    public void setCatalogProxy(ICatalogProxy catalogProxy) {
        this.catalogProxy = catalogProxy;
    }

    /**
     * @param serviceParameterDao The serviceParameterDao to set.
     */
    public void setServiceParameterDao(IServiceParameterDao serviceParameterDao) {
        this.serviceParameterDao = serviceParameterDao;
    }

    /**
     * @param serviceListParameterDao The serviceListParameterDao to set.
     */
    public void setServiceListParameterDao(IServiceListParameterDao serviceListParameterDao) {
        this.serviceListParameterDao = serviceListParameterDao;
    }

    public void setWidgetTypeManager(IWidgetTypeManager widgetTypeManager) {
        this.widgetTypeManager = widgetTypeManager;
    }

}
