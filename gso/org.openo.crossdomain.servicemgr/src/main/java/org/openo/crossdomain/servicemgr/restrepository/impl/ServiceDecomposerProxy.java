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
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.openo.crossdomain.servicemgr.constant.Constant;
import org.openo.crossdomain.servicemgr.model.roamo.DecomposerTaskInfo;
import org.openo.crossdomain.servicemgr.model.roamo.ServiceInfo;
import org.openo.crossdomain.servicemgr.model.roamo.ServiceModelInfo;
import org.openo.crossdomain.servicemgr.model.servicemo.ServiceModel;
import org.openo.crossdomain.servicemgr.restrepository.IServiceDecomposerProxy;
import org.openo.crossdomain.servicemgr.util.http.HttpClientUtil;
import org.openo.crossdomain.servicemgr.util.http.ResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.crossdomain.commsvc.common.util.jsonutil.JsonUtil;

import net.sf.json.JSONObject;

/**
 * Implementation for service decomposer proxy.<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
public class ServiceDecomposerProxy implements IServiceDecomposerProxy {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String SVCDECOMPOSER_SERVICE_REST_URL_PREFIX = "/rest/decomposer/v1/services/";

    private static final String SVCDECOMPOSER_TASK_REST_URL_PREFIX = "/rest/decomposer/v1/tasks";

    private static final String RESOURCES = "resources";

    /**
     * 
     * @see org.openo.crossdomain.servicemgr.restrepository.IServiceDecomposerProxy#getResourceByServiceID(String, HttpServletRequest)
     */
    @Override
    public JSONObject getResourceByServiceID(String serviceID, HttpServletRequest httpRequest) throws ServiceException {
        logger.info("Get Resource from Decomposer by serviceID is {}.", serviceID);

        // /rest/decomposer/v1/services/{service_id}/reources
        final String url =
                new StringBuilder(SVCDECOMPOSER_SERVICE_REST_URL_PREFIX).append(serviceID)
                        .append(Constant.FORWARD_SLASH).append(RESOURCES).toString();

        Map<String, String> httpHeaders = new HashMap<String, String>();

        final RestfulResponse response = HttpClientUtil.get(url, httpHeaders, httpRequest);

        String responseCotent = ResponseUtils.transferResponse(response);
        JSONObject responseJson = JsonUtil.unMarshal(responseCotent, JSONObject.class);
        return responseJson;
    }

    /**
     * 
     * @see org.openo.crossdomain.servicemgr.restrepository.IServiceDecomposerProxy#createDecomposer(ServiceModelInfo, ServiceModel, HttpServletRequest)
     */
    @Override
    public RestfulResponse createDecomposer(ServiceModelInfo serviceModelInfo, ServiceModel service,
            HttpServletRequest httpRequest) throws ServiceException {
        // /rest/decomposer/v1/tasks?action=create
        configServiceModelInfo(serviceModelInfo, service);

        String url =
                new StringBuilder(SVCDECOMPOSER_TASK_REST_URL_PREFIX).append(Constant.PARAM_SPLIT_LABLE)
                        .append(DecomposerTaskInfo.TASK_INFO_NUM.ACTION).append(Constant.EQUIVALENT)
                        .append(DecomposerTaskInfo.TASK_INFO_NUM.CREATE).toString();

        logger.info("The decomposer request url is {}, create service by {}.", url, service.getService_id());
        return HttpClientUtil.post(url, serviceModelInfo, httpRequest);

    }

    /**
     * 
     * @see org.openo.crossdomain.servicemgr.restrepository.IServiceDecomposerProxy#updateDecomposer(ServiceModelInfo, ServiceModel, HttpServletRequest)
     */
    @Override
    public RestfulResponse updateDecomposer(ServiceModelInfo serviceModelInfo, ServiceModel service,
            HttpServletRequest httpRequest) throws ServiceException {
        // /rest/decomposer/v1/tasks?action=update鈥�
        configServiceModelInfo(serviceModelInfo, service);
        ServiceInfo serviceInfo = serviceModelInfo.getService();
        serviceInfo.setService_definition_id(service.getService_definition_id());
        serviceInfo.setTemplate_id(service.getTemplate_id());

        String url =
                new StringBuilder(SVCDECOMPOSER_TASK_REST_URL_PREFIX).append(Constant.PARAM_SPLIT_LABLE)
                        .append(DecomposerTaskInfo.TASK_INFO_NUM.ACTION).append(Constant.EQUIVALENT)
                        .append(DecomposerTaskInfo.TASK_INFO_NUM.UPDATE).toString();

        logger.info("The decomposer request url is {}, update service by {}.", url, serviceInfo.getService_id());
        return HttpClientUtil.post(url, serviceModelInfo, httpRequest);

    }

    /**
     * 
     * @see org.openo.crossdomain.servicemgr.restrepository.IServiceDecomposerProxy#activeDecomposer(ServiceModel, HttpServletRequest)
     */
    @Override
    public RestfulResponse activeDecomposer(ServiceModel service, HttpServletRequest httpRequest)
            throws ServiceException {
        // /rest/decomposer/v1/tasks?action=deactivate 鈥�
        if(null == service) {
            logger.error("RequestData must not be null or ServiceModel must not be null!");
            throw new ServiceException(ServiceException.DEFAULT_ID,
                    "RequestData must not be null or ServiceModel must not be null!");
        }

        String url =
                new StringBuilder(SVCDECOMPOSER_TASK_REST_URL_PREFIX).append(Constant.PARAM_SPLIT_LABLE)
                        .append(DecomposerTaskInfo.TASK_INFO_NUM.ACTION).append(Constant.EQUIVALENT)
                        .append(DecomposerTaskInfo.TASK_INFO_NUM.ACTIVATE).toString();

        ServiceModelInfo serviceModelInfo = new ServiceModelInfo(service.toServiceInfo());

        ServiceInfo serviceInfo = serviceModelInfo.getService();
        setOtherCfg(serviceInfo);

        logger.info("The decomposer request url is {}, active service by {}.", url, service.getService_id());
        return HttpClientUtil.post(url, serviceModelInfo, httpRequest);

    }

    /**
     * 
     * @see org.openo.crossdomain.servicemgr.restrepository.IServiceDecomposerProxy#deactivDecomposer(ServiceModel, HttpServletRequest)
     */
    @Override
    public RestfulResponse deactivDecomposer(ServiceModel service, HttpServletRequest httpRequest)
            throws ServiceException {
        // /rest/decomposer/v1/tasks?action=deactivate 鈥�
        if(null == service) {
            logger.error("RequestData must not be null or ServiceModel must not be null!");
            throw new ServiceException(ServiceException.DEFAULT_ID,
                    "RequestData must not be null or ServiceModel must not be null!");
        }

        String url =
                new StringBuilder(SVCDECOMPOSER_TASK_REST_URL_PREFIX).append(Constant.PARAM_SPLIT_LABLE)
                        .append(DecomposerTaskInfo.TASK_INFO_NUM.ACTION).append(Constant.EQUIVALENT)
                        .append(DecomposerTaskInfo.TASK_INFO_NUM.DEACTIVATE).toString();

        ServiceModelInfo serviceModelInfo = new ServiceModelInfo(service.toServiceInfo());

        ServiceInfo serviceInfo = serviceModelInfo.getService();
        setOtherCfg(serviceInfo);

        logger.info("The decomposer request url is {}, deactive service by {}.", url, service.getService_id());
        return HttpClientUtil.post(url, serviceModelInfo, httpRequest);

    }

    /**
     * 
     * @see org.openo.crossdomain.servicemgr.restrepository.IServiceDecomposerProxy#deleteDecomposer(ServiceModel, HttpServletRequest)
     */
    @Override
    public RestfulResponse deleteDecomposer(ServiceModel service, HttpServletRequest httpRequest)
            throws ServiceException {
        // /rest/decomposer/v1/tasks?action=delete
        if(null == service) {
            logger.error("ServiceModel must not be null!");
            throw new ServiceException(ServiceException.DEFAULT_ID, "ServiceModel must not be null!");
        }

        final String url =
                new StringBuilder(SVCDECOMPOSER_TASK_REST_URL_PREFIX).append(Constant.PARAM_SPLIT_LABLE)
                        .append(DecomposerTaskInfo.TASK_INFO_NUM.ACTION).append(Constant.EQUIVALENT)
                        .append(DecomposerTaskInfo.TASK_INFO_NUM.DELETE).toString();

        ServiceModelInfo serviceModelInfo = new ServiceModelInfo(service.toServiceInfo());
        ServiceInfo serviceInfo = serviceModelInfo.getService();

        setOtherCfg(serviceInfo);

        logger.info("The decomposer request url is {}, delete service by {}.", url, service.getService_id());
        return HttpClientUtil.post(url, serviceModelInfo, httpRequest);

    }

    private void configServiceModelInfo(ServiceModelInfo serviceModelInfo, ServiceModel service)
            throws ServiceException {
        if(null == serviceModelInfo || null == serviceModelInfo.getService() || null == service) {
            logger.error("RequestData must not be null or ServiceModel must not be null!");
            throw new ServiceException(ServiceException.DEFAULT_ID,
                    "RequestData must not be null or ServiceModel must not be null!");
        }

        ServiceInfo serviceInfo = serviceModelInfo.getService();
        serviceInfo.setService_id(service.getService_id());
        serviceInfo.setType(service.getService_type());

        setOtherCfg(serviceInfo);
    }

    private void setOtherCfg(ServiceInfo serviceInfo) {
        if(null == serviceInfo.getParameters()) {
            serviceInfo.setParameters(new HashMap<String, Object>());
        }

        if(null == serviceInfo.getNsd_script()) {
            serviceInfo.setNsd_script(new Object());
        }
    }
}
