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

import org.openo.crossdomain.commsvc.formation.model.Parameter;
import org.openo.crossdomain.commsvc.formation.model.Template;
import org.openo.crossdomain.servicemgr.model.roamo.ServiceInfo;
import org.openo.crossdomain.servicemgr.model.roamo.ServiceModelInfo;
import org.openo.crossdomain.servicemgr.model.roamo.ServiceParamConstant;
import org.openo.crossdomain.servicemgr.util.uuid.UUIDUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.crossdomain.commsvc.common.util.jsonutil.JsonUtil;

/**
 * Definition for service parameter.<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
public class ServiceParameter {

    private final static Logger logger = LoggerFactory.getLogger(ServiceParameter.class);

    private String id;

    private String tenant_id;

    private String service_id;

    private String parameter_type;

    private String parameter_name;

    private Object parameter_value;

    private Integer flag;

    private List<ServiceListParameter> parameterGroup;

    /**
     * Get the collection of service parameters.<br/>
     *
     * @param serviceModelInfo service model information
     * @return collection of service parameter
     * @throws ServiceException
     * @since crossdomain 0.5
     */
    public static List<ServiceParameter> toServiceModel(ServiceModelInfo serviceModelInfo) throws ServiceException {
        if(null == serviceModelInfo || null == serviceModelInfo.getService()
                || CollectionUtils.isEmpty(serviceModelInfo.getService().getParameters())) {
            return null;
        }

        List<ServiceParameter> svcParamList = new ArrayList<ServiceParameter>();

        ServiceInfo service = serviceModelInfo.getService();
        String serviceID = serviceModelInfo.getService().getService_id();
        Map<String, Object> parameters = service.getParameters();
        for(Map.Entry<String, Object> entry : parameters.entrySet()) {
            ServiceParameter svcParam = new ServiceParameter();

            if(!StringUtils.hasLength(svcParam.getId())) {
                svcParam.setId(UUIDUtils.createBase64Uuid());
            }
            if(null != serviceID) {
                svcParam.setService_id(serviceID);
            }

            svcParamList.add(getSvcParam(svcParam, entry));
        }
        return svcParamList;
    }

    /**
     * Get service parameter and set the type of parameter.<br/>
     *
     * @param svcParam service parameter
     * @param entry service parameter entry
     * @return service parameter
     * @throws ServiceException
     * @since crossdomain 0.5
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static ServiceParameter getSvcParam(ServiceParameter svcParam, Map.Entry<String, Object> entry)
            throws ServiceException {
        String paramName = entry.getKey();
        Object paramVlue = entry.getValue();

        if(paramVlue instanceof String) {
            svcParam.setParameter_type(PARAM_TYPE_NUM.PARAM_TYPE_SIMPLE);
            svcParam.setParameter_name(paramName);
            svcParam.setParameter_value(paramVlue.toString());
        } else if(paramVlue instanceof List) {
            svcParam.setParameter_type(PARAM_TYPE_NUM.PARAM_TYPE_RESLIST);
            svcParam.setParameter_name(paramName);
            svcParam.setParameter_value(paramName);
            List<Object> listObj = (List<Object>)paramVlue;
            List<ServiceListParameter> svcListParam = new ArrayList<ServiceListParameter>();
            for(Object paramObj : listObj) {
                if(!(paramObj instanceof Map)) {
                    continue;
                }
                Map<String, Object> param = (Map)paramObj;

                Object logicalNameObj = param.get(ServiceParamConstant.TEMPLATE_PARAM_KEY_NUM.LOGICALNAME);
                if(null == logicalNameObj) {

                    break;
                }

                ServiceListParameter serviceListParameter = new ServiceListParameter();

                if(null != svcParam.getService_id()) {
                    serviceListParameter.setService_id(svcParam.getService_id());
                }
                serviceListParameter.setParamgroup_name(paramName);
                serviceListParameter.setKey_name(String.valueOf(logicalNameObj));

                Object action = param.get(ServiceParamConstant.TEMPLATE_PARAM_KEY_NUM.ACTION);
                if(null != action) {

                    serviceListParameter.setAction(action.toString());
                }

                serviceListParameter.setParameter_value(JsonUtil.marshal(param));
                svcListParam.add(serviceListParameter);
            }

            if(svcListParam.isEmpty()) {

                ServiceListParameter svcAttrListParameter = new ServiceListParameter();
                svcAttrListParameter.setParamgroup_name(paramName);
                svcAttrListParameter.setKey_name(paramName);
                if(null != svcParam.getService_id()) {
                    svcAttrListParameter.setService_id(svcParam.getService_id());
                }
                svcAttrListParameter.setParameter_value(JsonUtil.marshal(paramVlue));

                svcListParam.add(svcAttrListParameter);
                svcParam.setParameter_type(PARAM_TYPE_NUM.PARAM_TYPE_LIST);
            }
            svcParam.setParameterGroup(svcListParam);
        } else {

            logger.error("the type of parameter {} is not supported", paramName);
        }

        return svcParam;
    }

    /**
     * Get collection of service parameter from template.<br/>
     *
     * @param template template data
     * @return collection of service parameters
     * @since crossdomain 0.5
     */
    public static List<ServiceParameter> toServiceModel(Template template) {
        List<ServiceParameter> svcParamList = new ArrayList<ServiceParameter>();

        if(null == template) {
            return svcParamList;
        }

        List<Parameter> paramList = template.getParameterList();
        if(CollectionUtils.isEmpty(paramList)) {
            return svcParamList;
        }

        for(Parameter param : paramList) {
            if(StringUtils.hasLength(param.getHidden()) && param.getHidden().equals(HIDDEN_FALG_NUM.HIDDEN_FALG_TRUE)) {
                continue;
            }

            String paramName = param.getParameterName();
            String paramVlue = param.getValue();

            ServiceParameter svcParam = new ServiceParameter();
            if(!StringUtils.hasLength(svcParam.getId())) {
                svcParam.setId(UUIDUtils.createBase64Uuid());
            }
            svcParam.setParameter_name(paramName);
            svcParam.setParameter_value(paramVlue);

            svcParamList.add(svcParam);
        }
        return svcParamList;
    }

    /**
     * @return Returns the tenant_id.
     */
    public String getTenant_id() {
        return tenant_id;
    }

    /**
     * @param tenant_id The tenant_id to set.
     */
    public void setTenant_id(String tenant_id) {
        this.tenant_id = tenant_id;
    }

    /**
     * @return Returns the service_id.
     */
    public String getService_id() {
        return service_id;
    }

    /**
     * @param service_id The service_id to set.
     */
    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    /**
     * @return Returns the parameter_name.
     */
    public String getParameter_name() {
        return parameter_name;
    }

    /**
     * @param parameter_name The parameter_name to set.
     */
    public void setParameter_name(String parameter_name) {
        this.parameter_name = parameter_name;
    }

    /**
     * @return Returns the parameter_value.
     */
    public Object getParameter_value() {
        return parameter_value;
    }

    /**
     * @param parameter_value The parameter_value to set.
     */
    public void setParameter_value(Object parameter_value) {
        this.parameter_value = parameter_value;
    }

    /**
     * @return Returns the flag.
     */
    public Integer getFlag() {
        return flag;
    }

    /**
     * @param flag The flag to set.
     */
    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    /**
     * @return Returns the id.
     */
    public String getId() {
        return id;
    }

    /**
     * @param id The id to set.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return Returns the parameter_type.
     */
    public String getParameter_type() {
        return parameter_type;
    }

    /**
     * @param parameter_type The parameter_type to set.
     */
    public void setParameter_type(String parameter_type) {
        this.parameter_type = parameter_type;
    }

    /**
     * @return Returns the parameterGroup.
     */
    public List<ServiceListParameter> getParameterGroup() {
        return parameterGroup;
    }

    /**
     * @param parameterGroup The parameterGroup to set.
     */
    public void setParameterGroup(List<ServiceListParameter> parameterGroup) {
        this.parameterGroup = parameterGroup;
    }

    @Override
    public String toString() {
        return "ServiceParameter [id=" + id + ", tenant_id=" + tenant_id + ", service_id=" + service_id
                + ", parameter_name=" + parameter_name + ", parameter_value=" + parameter_value + ", flag=" + flag
                + "]";
    }

    /**
     * Definition of hidden flag for different parameter type.<br/>
     * 
     * @author
     * @version crossdomain 0.5 2016-3-19
     */
    public static class HIDDEN_FALG_NUM {

        public static final Integer HIDDEN_FALG_DEFAULT = 0;

        public static final String HIDDEN_FALG_TRUE = "true";
    }

    /**
     * Definition of parameter type.<br/>
     * 
     * @author
     * @version crossdomain 0.5 2016-3-19
     */
    public static class PARAM_TYPE_NUM {

        public static final String PARAM_TYPE_SIMPLE = "simple";

        public static final String PARAM_TYPE_RESLIST = "resList";

        public static final String PARAM_TYPE_LIST = "list";
    }
}
