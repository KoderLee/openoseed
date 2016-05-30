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
package org.openo.crossdomain.commonsvc.executor.service.asyn;

import org.openo.commonservice.log.OssLog;
import org.openo.commonservice.log.OssLogFactory;
import org.openo.commonservice.roa.util.restclient.RestfulOptions;
import org.openo.commonservice.roa.util.restclient.RestfulParametes;
import org.openo.commonservice.roa.util.restclient.RestfulResponse;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpStatus;
import org.openo.crossdomain.commonsvc.executor.common.constant.Constants;
import org.openo.crossdomain.commonsvc.executor.common.constant.ErrorMessage;
import org.openo.crossdomain.commonsvc.executor.common.redis.TenantTokenOper;
import org.openo.crossdomain.commonsvc.executor.common.resthelper.impl.ResourceConfigClient;
import org.openo.crossdomain.commonsvc.executor.common.resthelper.inf.IRestClient;
import org.openo.crossdomain.commonsvc.executor.model.Resource;
import org.openo.crossdomain.commonsvc.executor.model.ServiceInfo;
import org.openo.crossdomain.commonsvc.executor.model.db.Result;
import org.openo.crossdomain.commonsvc.executor.model.util.Json2ModelUtil;
import org.openo.crossdomain.commonsvc.executor.service.Manager;
import org.springframework.util.StringUtils;

public class GetExecuteResult {

    private static final OssLog log = OssLogFactory.getLogger(GetExecuteResult.class);

    protected IRestClient client = new ResourceConfigClient();

    protected RestfulResponse response;

    private ServiceInfo service;

    private String url;

    public GetExecuteResult() {
        super();
    }

    public GetExecuteResult(String url, ServiceInfo service) {
        this.url = url;
        this.service = service;
    }

    public ServiceInfo getService() {
        return service;
    }

	/**
	 *Asynchronous loop query result
	 *@return Result of the process
	 *@since crossdomain 0.5 2016-3-18
	 */
    public Result process() {
        RestfulParametes parametes = new RestfulParametes();

        TenantTokenOper.getInstance().setSecureToken(service.getTenantId(), parametes);

        response = client.get(url, parametes, buildOptions());

        return processResponse();
    }

	/**
	 *Asynchronous loop query response process
	 *@return Result of the process
	 *@since crossdomain 0.5 2016-3-18
	 */
    public Result processResponse() {
        Result result = null;
        if(response.getStatus() == HttpStatus.SC_OK && StringUtils.hasLength(response.getResponseContent())) {
            result = processResponseOk(response.getResponseContent());
        } else {
            String msg = String.format(ErrorMessage.RESPONSE_ERROR_MSG, response.getStatus());
            log.error(msg);
            result = new Result(Result.FAIL, msg, null);

            updateServiceJob(null, result);
        }

        return result;
    }

    /**
	 *Asynchronous loop query result of StatusCode 200
	 *@param content response content
	 *@return Result of the process
	 *@since crossdomain 0.5 2016-3-18
	 */
    public Result processResponseOk(String content) {
        JSONObject retObj = JSONObject.fromObject(content);

        Result result = new Result();
        if(!Json2ModelUtil.toProcessing(retObj)) {
            return result;
        }

        ServiceInfo retService = ServiceInfo.getServiceFromAsyncResult(retObj);
        if(retService == null) {
            String msg = String.format(ErrorMessage.OBJECT_NULL_MSG, "ServiceInfo");
            log.error(msg);

            result.setErrorCode(Result.FAIL);
            result.setResultReason(msg);
        }

        result.setOperObject(Constants.GETRESULT);

        updateServiceJob(retService, result);

        return result;
    }

    private void updateServiceJob(ServiceInfo retService, Result result) {
        if(retService == null) {

            Resource resource = (Resource)service.getResources().values().toArray()[0];
            resource.setResult(result);

            retService = service;
        } else {
            retService.setJobId(service.getJobId());
        }

        Manager.getInstance().executed(retService);
    }

    protected RestfulOptions buildOptions() {
        RestfulOptions options = new RestfulOptions();
        options.setRestTimeout(Constants.REST_TIMEOUT);
        return options;
    }

    public String getUrl() {
        return url;
    }
}
