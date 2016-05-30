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
package org.openo.crossdomain.commonsvc.executor.service;

import org.openo.commonservice.log.OssLog;
import org.openo.commonservice.log.OssLogFactory;
import org.openo.commonservice.remoteservice.exception.ServiceException;
import org.openo.commonservice.roa.util.restclient.RestfulOptions;
import org.openo.commonservice.roa.util.restclient.RestfulParametes;
import org.openo.commonservice.roa.util.restclient.RestfulResponse;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpStatus;
import org.openo.crossdomain.commonsvc.executor.common.constant.Constants;
import org.openo.crossdomain.commonsvc.executor.common.constant.ErrorMessage;
import org.openo.crossdomain.commonsvc.executor.common.constant.RequestJsonConstants;
import org.openo.crossdomain.commonsvc.executor.common.enums.ActionType;
import org.openo.crossdomain.commonsvc.executor.common.redis.TenantTokenOper;
import org.openo.crossdomain.commonsvc.executor.common.resthelper.impl.ResourceConfigClient;
import org.openo.crossdomain.commonsvc.executor.common.resthelper.inf.IRestClient;
import org.openo.crossdomain.commonsvc.executor.common.util.ExecutorContextHelper;
import org.openo.crossdomain.commonsvc.executor.model.PluginRule;
import org.openo.crossdomain.commonsvc.executor.model.Resource;
import org.openo.crossdomain.commonsvc.executor.model.ServiceInfo;
import org.openo.crossdomain.commonsvc.executor.model.db.Result;
import org.openo.crossdomain.commonsvc.executor.model.util.Json2ModelUtil;
import org.openo.crossdomain.commonsvc.executor.service.inf.IPluginRuleService;
import org.springframework.util.StringUtils;

public class ExecuteServiceController {

    private static final OssLog log = OssLogFactory.getLogger(ExecuteServiceController.class);

    private static final String PLUGIN_URI = "/rest/%s/resource";

    private static final int REST_TIMEOUT = 600000;

    private IRestClient client = new ResourceConfigClient();

    private boolean isDryrun = false;

    private String jobId;

    private String tenantId;

    private ServiceInfo service;

    private RestfulResponse response;

	/**
	 *Constructor
	 *@since crossdomain 0.5 2016-3-18
	 */
    public ExecuteServiceController() {
    }

	/**
	 *Constructor
	 *@param service ServiceInfo to be processed
	 *@since crossdomain 0.5 2016-3-18
	 */
    public ExecuteServiceController(ServiceInfo service) {
        this.service = service;
        this.jobId = service.getJobId();
        this.tenantId = service.getTenantId();
    }
	/**
	 *Constructor
	 *@param response RestfulResponse to be processed
	 *@since crossdomain 0.5 2016-3-18
	 */
    public ExecuteServiceController(RestfulResponse response) {
        this.response = response;
    }

	/**
	 *get Class Name
	 *@return class name
	 *@since crossdomain 0.5 2016-3-18
	 */
    public String getName() {
        return "ExecuteServiceController";
    }

	/**
	 *process the resource
	 *@return Result of process
	 *@since crossdomain 0.5 2016-3-18
	 */
    public Result process() {
        Resource resource = (Resource)service.getResources().values().toArray()[0];

        String routerId;
        Result result;
        try {
            routerId = getRouterId();

            if(!StringUtils.hasLength(routerId)) {
                String msg = String.format(ErrorMessage.OBJECT_NULL_MSG, getName());
                log.info(msg);
                result = new Result();

                resource.setResult(result);
                updateService();

                return result;
            }

            final RestfulParametes parametes = buildParametes(tenantId, service.getAction());
            sendToPlugin(service.getAction(), routerId, parametes, buildOptions());

            if(response == null) {
                String msg = String.format(ErrorMessage.OBJECT_NULL_MSG, getName());
                log.error(msg);

                result = new Result(Result.FAIL, msg, null);
                resource.setResult(result);
            } else {
                result = processResponse();
            }
        } catch(ServiceException e) {
            result = new Result(Result.FAIL, e.getMessage(), null);
            resource.setResult(result);
        }

        if(!result.isSuccess()) {
            updateService();
        }

        return result;
    }

	/**
	 *Send resource to plugin
	 *@param action resoure action type
	 *@param url rest url
	 *@param parametes RestfulParametes to send to Plugin
	 *@param options RestfulOptions to send to Plugin
	 *@since crossdomain 0.5 2016-3-18
	 */
    public void sendToPlugin(ActionType action, String url, RestfulParametes parametes, RestfulOptions options) {
        switch(action) {
            case CREATE:
            case ACTIVE:
            case DEACTIVE:
                response = client.post(url, parametes, options);
                break;
            case MODIFY:
            case UPDATE:
                response = client.put(url, parametes, options);
                break;
            case DELETE:
                response = client.delete(url, parametes, options);
                break;
            default:
                log.error("sendToPlugin, {} invalid", action);
                break;
        }
    }

    /**
	 *get rest url
	 *@return rest url
	 *@throws ServiceException when fail to get rest url
	 *@since crossdomain 0.5 2016-3-18
	 */
    public String getRouterId() throws ServiceException {
        Resource resource = (Resource)service.getResources().values().toArray()[0];

        IPluginRuleService ruleService =
                (IPluginRuleService)ExecutorContextHelper.getInstance().getBean("pluginRuleService");
        PluginRule rule =
                ruleService.getRuleByResourceTypeVersion(resource.getType(), null, resource.getOperType().toString());

        String url = Constants.NULL_STR;
        if(rule != null) {
            url = String.format(PLUGIN_URI, rule.getUriPrefix());

            if(ActionType.CREATE != service.getAction() && StringUtils.hasLength(resource.getId())) {
                url += "/" + resource.getId();
            }

            switch(service.getAction()) {
                case ACTIVE:
                    url += "/" + Constants.ACTIVE_URL;
                    break;
                case DEACTIVE:
                    url += "/" + Constants.DEACTIVE_URL;
                    break;
                default:
                    break;
            }
        }

        return url;
    }

	/**
	 *build parametes
	 *@param tenantId Tenant IDLEntity
	 *@param action resource action type
	 *@since crossdomain 0.5 2016-3-18
	 */
    public RestfulParametes buildParametes(String tenantId, ActionType action) {
        String strJsonReq = service.generateServiceExecuteContent();

        RestfulParametes restParametes = new RestfulParametes();

        restParametes.put(RequestJsonConstants.Service.isDryrun, isDryrun ? "true" : "false");

        TenantTokenOper.getInstance().setSecureToken(tenantId, restParametes);

        if(ActionType.DELETE != action) {
            restParametes.setRawData(strJsonReq);
        }

        Resource resource = (Resource)service.getResources().values().toArray()[0];
        log.error("sendToPlugin: action:{}, service_id:{}, resource:{}", action, service.getServiceId(),
                resource.getKey());

        return restParametes;
    }

	/**
	 *process restful Response
	 *@return Result of process
	 *@since crossdomain 0.5 2016-3-18
	 */
    public Result processResponse() {
        Result result;
        Resource resource = (Resource)service.getResources().values().toArray()[0];

        if(response.getStatus() == HttpStatus.SC_ACCEPTED) {
            result = processResponseAccept(response.getResponseContent());
        } else if(response.getStatus() == HttpStatus.SC_CREATED || response.getStatus() == HttpStatus.SC_OK) {
            result = processSyncResponse(response.getResponseContent());
        } else {
            String msg = String.format(ErrorMessage.HTTP_STATUS_MSG, response.getStatus());
            log.error(msg);
            result = new Result(Result.FAIL, msg, null);

            resource.setResult(result);
        }

        return result;
    }

	/**
	 *process Accept Restful Response
	 *@param content response content
	 *@return Result of process
	 *@since crossdomain 0.5 2016-3-18
	 */
    public Result processResponseAccept(String content) {
        Resource resource = (Resource)service.getResources().values().toArray()[0];

        String location = Json2ModelUtil.jsonToLocation(content);
        Result result = checkLoactionValid(location);
        if(!result.isSuccess()) {

            resource.setResult(result);
            return result;
        }

        resource.setQueryUrl(location);

        log.error(String.format("plugin return: asyn url:%s", location));

        Manager.getInstance().startToExcute(tenantId, jobId, resource);

        return result;
    }

	/**
	 *process Synchronous Restful Response
	 *@param content response content
	 *@return Result of process
	 *@since crossdomain 0.5 2016-3-18
	 */
    public Result processSyncResponse(String content) {
        Result result;
        switch(service.getAction()) {
            case DELETE:
                result = processResponseDelete(content);
                break;
            default:
                result = processResponseCreate(content);
                break;
        }

        return result;
    }

	
	/**
	 *process Delete Restful Response
	 *@param content response content
	 *@return Result of process
	 *@since crossdomain 0.5 2016-3-18
	 */
    public Result processResponseDelete(String content) {
        JSONObject retObj = JSONObject.fromObject(content);

        Resource resource = (Resource)service.getResources().values().toArray()[0];

        Result result = Result.json2Result(retObj);

        String resultStr = Constants.NULL_STR;
        if(result != null) {
            resultStr = result.toString();
        }
        log.error(String.format("plugin return: delete:%s", resultStr));

        resource.setResult(result);

        updateService();

        if(result == null) {
            result = new Result();
        }

        return result;
    }

	/**
	 *process Create Restful Response
	 *@param content response content
	 *@return Result of process
	 *@since crossdomain 0.5 2016-3-18
	 */
    public Result processResponseCreate(String content) {
        JSONObject retObj = JSONObject.fromObject(content);

        Resource resource = (Resource)service.getResources().values().toArray()[0];

        Result result = new Result();
        ServiceInfo retService = ServiceInfo.toServiceExecuteRsp(retObj);
        if(retService == null) {
            String msg = String.format(ErrorMessage.OBJECT_NULL_MSG, "ServiceInfo");
            log.error(msg);
            result.setErrorCode(Result.FAIL);
            result.setResultReason(msg);

            resource.setResult(result);

            retService = service;
        } else {

            retService.setJobId(jobId);
            retService.setTenantId(tenantId);

            if(retService.getResources().isEmpty()) {
                log.error("plugin return: null resource, action:{}, service_id:{}, resource:{}",
                        resource.getOperType(), service.getServiceId(), resource.getKey());
            } else {
                Resource retResource = (Resource)retService.getResources().values().toArray()[0];
                log.error("plugin return: action:{}, service_id:{}, resource:{}", retResource.getOperType(),
                        retService.getServiceId(), retResource.getKey());
            }
        }

        Manager.getInstance().startToExcute(tenantId, jobId, resource);

        Manager.getInstance().executed(retService);

        return result;
    }

    protected RestfulOptions buildOptions() {
        RestfulOptions options = new RestfulOptions();
        options.setRestTimeout(REST_TIMEOUT);
        return options;
    }

	/**
	 *get Restful Response
	 *@return RestfulResponse of process
	 *@since crossdomain 0.5 2016-3-18
	 */
    public RestfulResponse getResponse() {
        return response;
    }

    private void updateService() {
        Resource resource = (Resource)service.getResources().values().toArray()[0];

        Manager.getInstance().startToExcute(tenantId, jobId, resource);

        Manager.getInstance().executed(service);
    }

    private Result checkLoactionValid(String location) {
        Result result = new Result();
        if(!StringUtils.hasLength(location)) {
            String msg = String.format(ErrorMessage.OBJECT_NULL_MSG, Constants.LOCATION_KEY);
            result.setErrorCode(Result.FAIL);
            result.setResultReason(msg);
        } else if(location.length() > Constants.URL_LEN_MAX) {
            String msg = String.format(ErrorMessage.URI_INVALID_MSG);
            result.setErrorCode(Result.FAIL);
            result.setResultReason(msg);
        }

        return result;
    }
}
