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

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.openo.crossdomain.commsvc.formation.model.Template;
import org.openo.crossdomain.commsvc.formation.util.validationutil.TemplateUtil;
import org.openo.crossdomain.servicemgr.exception.ErrorCode;
import org.openo.crossdomain.servicemgr.model.roamo.ServiceParamConstant;
import org.openo.crossdomain.servicemgr.service.inf.IWidgetTypeManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import org.openo.baseservice.remoteservice.exception.ServiceException;

/**
 * Implementation for widget type manager.<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
public class WidgetTypeManagerImpl implements IWidgetTypeManager {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 
     * @see org.openo.crossdomain.servicemgr.service.inf.IWidgetTypeManager#getSvcParamWithWidgetType(String, String, HttpServletRequest)
     */
    @Override
    public Map<String, Object> getSvcParamWithWidgetType(String templateContent, String parameters,
            HttpServletRequest httpRequest) throws ServiceException {
        if(!StringUtils.hasLength(templateContent)) {
            return null;
        }

        try {

            Template template = TemplateUtil.convertTemplate(templateContent, parameters);

            Map<String, Object> templateParams = template.getParametersObj();
            if(CollectionUtils.isEmpty(templateParams)) {
                logger.error("Template Parameters is empty!");
                return null;
            }

            Map<String, Object> templateParamsRsp = new LinkedHashMap<String, Object>();
            for(Entry<String, Object> entry : templateParams.entrySet()) {
                if(entry.getValue() instanceof Map) {
                    Map<String, Object> templateParam = getServiceParamWithWidgetType(entry);
                    templateParamsRsp.put(entry.getKey(), templateParam);
                }
            }
            return templateParamsRsp;
        } catch(ServiceException e) {
            logger.error("TemplateContent parse failed!");
            throw new ServiceException(ErrorCode.SVCMGR_TEMPLATE_PARSE_ERROR, "TemplateContent parse failed!");
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getServiceParamWithWidgetType(Entry<String, Object> entry) {
        Map<String, Object> templateParam = (Map<String, Object>)entry.getValue();
        if(!templateParam.containsKey(ServiceParamConstant.TEMPLATE_PARAM_KEY_NUM.TYPE)) {
            logger.info("The Type of {} is empty!", entry.getKey());
            return null;
        }

        String paramType = templateParam.get(ServiceParamConstant.TEMPLATE_PARAM_KEY_NUM.TYPE).toString();
        if(!isTemplateParamType(paramType)) {
            logger.info("The Type of {} is not supported!", entry.getKey());
            return null;
        }

        String paramWidgetType = ServiceParamConstant.WIDGET_TYPE_NUM.TEXT;

        if(paramType.equals(ServiceParamConstant.TEMPLATE_PARAM_VALUE_NUM.TYPE_LIST_PARAMGROUP)) {
            paramWidgetType = ServiceParamConstant.WIDGET_TYPE_NUM.TYPE_LIST_GROUP;

            getListParameterWithWidgetType(templateParam);
        }

        else if(paramType.equals(ServiceParamConstant.TEMPLATE_PARAM_VALUE_NUM.TYPE_LIST_RESPARAMGROUP)) {
            paramWidgetType = ServiceParamConstant.WIDGET_TYPE_NUM.TYPE_LIST_RESGROUP;

            getListParameterWithWidgetType(templateParam);
        } else {
            paramWidgetType = setParamWidgetType(templateParam, entry.getKey());
        }

        templateParam.put(ServiceParamConstant.WIDGET_KEY_NUM.WIDGETTYPE, paramWidgetType);

        return templateParam;
    }

    private boolean isTemplateParamType(String paramType) {
        if(!paramType.equals(ServiceParamConstant.TEMPLATE_PARAM_VALUE_NUM.TYPE_STRING)
                && !paramType.equals(ServiceParamConstant.TEMPLATE_PARAM_VALUE_NUM.TYPE_NUMBER)
                && !paramType.equals(ServiceParamConstant.TEMPLATE_PARAM_VALUE_NUM.TYPE_LIST)
                && !paramType.equals(ServiceParamConstant.TEMPLATE_PARAM_VALUE_NUM.TYPE_COMMADELIMITEDLIST)
                && !paramType.equals(ServiceParamConstant.TEMPLATE_PARAM_VALUE_NUM.TYPE_LIST_PARAMGROUP)
                && !paramType.equals(ServiceParamConstant.TEMPLATE_PARAM_VALUE_NUM.TYPE_LIST_RESPARAMGROUP)) {
            return false;
        }

        return true;
    }

    @SuppressWarnings("unchecked")
    private void getListParameterWithWidgetType(Map<String, Object> templateParam) {
        Object mapObj = templateParam.get(ServiceParamConstant.TEMPLATE_PARAM_KEY_NUM.MAP);
        if(!(mapObj instanceof Map)) {
            return;
        }

        Map<String, Object> mapParam = (Map<String, Object>)mapObj;
        for(Entry<String, Object> listParam : mapParam.entrySet()) {
            Map<String, Object> listParamMap = (Map<String, Object>)listParam.getValue();

            String paramType = listParamMap.get(ServiceParamConstant.TEMPLATE_PARAM_KEY_NUM.TYPE).toString();
            if(!isTemplateParamType(paramType)) {
                logger.info("The Type {} is not supported!", paramType);
                return;
            }

            getListParameterWithWidgetType(listParamMap);
            String mapParamWidgetType = setParamWidgetType(listParamMap, listParam.getKey());
            listParamMap.put(ServiceParamConstant.WIDGET_KEY_NUM.WIDGETTYPE, mapParamWidgetType);
        }
    }

    private String setParamWidgetType(Map<String, Object> mapParam, String key) {

        String mapParamWidgetType = ServiceParamConstant.WIDGET_TYPE_NUM.TEXT;

        String paramType = mapParam.get(ServiceParamConstant.TEMPLATE_PARAM_KEY_NUM.TYPE).toString();

        if(isHidden(mapParam)) {
            if(paramType.equals(ServiceParamConstant.TEMPLATE_PARAM_VALUE_NUM.TYPE_LIST)
                    || paramType.equals(ServiceParamConstant.TEMPLATE_PARAM_VALUE_NUM.TYPE_COMMADELIMITEDLIST)) {
                if(mapParam.containsKey(ServiceParamConstant.TEMPLATE_PARAM_KEY_NUM.VALIDVALUES)
                        && null != mapParam.get(ServiceParamConstant.TEMPLATE_PARAM_KEY_NUM.VALIDVALUES)) {
                    mapParamWidgetType = ServiceParamConstant.WIDGET_TYPE_NUM.NOECHO_MULTICOMBO;
                } else {
                    mapParamWidgetType = ServiceParamConstant.WIDGET_TYPE_NUM.NOECHO_MULTITEXT;
                }
            } else if(mapParam.containsKey(ServiceParamConstant.TEMPLATE_PARAM_KEY_NUM.VALIDVALUES)
                    && null != mapParam.get(ServiceParamConstant.TEMPLATE_PARAM_KEY_NUM.VALIDVALUES)) {
                mapParamWidgetType = ServiceParamConstant.WIDGET_TYPE_NUM.NOECHO_COMBO;
            } else {
                mapParamWidgetType = ServiceParamConstant.WIDGET_TYPE_NUM.NOECHO;
            }
        } else if(paramType.equals(ServiceParamConstant.TEMPLATE_PARAM_VALUE_NUM.TYPE_LIST)
                || paramType.equals(ServiceParamConstant.TEMPLATE_PARAM_VALUE_NUM.TYPE_COMMADELIMITEDLIST)) {
            if(mapParam.containsKey(ServiceParamConstant.TEMPLATE_PARAM_KEY_NUM.VALIDVALUES)
                    && null != mapParam.get(ServiceParamConstant.TEMPLATE_PARAM_KEY_NUM.VALIDVALUES)) {
                mapParamWidgetType = ServiceParamConstant.WIDGET_TYPE_NUM.MULTICOMBO;
            } else {
                mapParamWidgetType = ServiceParamConstant.WIDGET_TYPE_NUM.MULTITEXT;
            }
        } else if(mapParam.containsKey(ServiceParamConstant.TEMPLATE_PARAM_KEY_NUM.VALIDVALUES)
                && null != mapParam.get(ServiceParamConstant.TEMPLATE_PARAM_KEY_NUM.VALIDVALUES)) {
            mapParamWidgetType = ServiceParamConstant.WIDGET_TYPE_NUM.COMBO;
        }

        else if(paramType.equals(ServiceParamConstant.TEMPLATE_PARAM_VALUE_NUM.TYPE_LIST_PARAMGROUP)) {
            mapParamWidgetType = ServiceParamConstant.WIDGET_TYPE_NUM.TYPE_LIST_GROUP;
        } else if(paramType.equals(ServiceParamConstant.TEMPLATE_PARAM_VALUE_NUM.TYPE_LIST_RESPARAMGROUP)) {
            mapParamWidgetType = ServiceParamConstant.WIDGET_TYPE_NUM.TYPE_LIST_RESGROUP;
        } else {
            logger.info("The Type of {} is Text!", key);
        }

        return mapParamWidgetType;
    }

    private boolean isHidden(Map<String, Object> mapParam) {
        return mapParam.containsKey(ServiceParamConstant.TEMPLATE_PARAM_KEY_NUM.HIDDEN)
                && ServiceParamConstant.TEMPLATE_PARAM_VALUE_NUM.HIDDEN_TRUE.equals(mapParam.get(
                        ServiceParamConstant.TEMPLATE_PARAM_KEY_NUM.HIDDEN).toString());
    }
}
