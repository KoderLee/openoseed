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
package org.openo.crossdomain.commonsvc.executor.rest;

import org.openo.commonservice.biz.trail.AuditItem;
import org.openo.commonservice.remoteservice.exception.ServiceException;
import org.openo.commonservice.roa.annotation.*;
import org.openo.commonservice.roa.common.HttpContext;
import org.openo.commonservice.roa.common.RequestInputStream;

import org.openo.crossdomain.commonsvc.executor.common.check.ModelChecker;
import org.openo.crossdomain.commonsvc.executor.common.constant.Constants;
import org.openo.crossdomain.commonsvc.executor.common.constant.ErrorMessage;
import org.openo.crossdomain.commonsvc.executor.common.constant.RequestJsonConstants;
import org.openo.crossdomain.commonsvc.executor.common.module.IResource;
import org.openo.crossdomain.commonsvc.executor.common.util.LogUtil;
import org.openo.crossdomain.commonsvc.executor.common.util.ServiceExceptionUtil;
import org.openo.crossdomain.commonsvc.executor.model.PluginRule;
import org.openo.crossdomain.commonsvc.executor.model.db.Result;
import org.openo.crossdomain.commonsvc.executor.model.util.Model2JsonUtil;
import org.openo.crossdomain.commonsvc.executor.model.util.PluginRuleUtil;
import org.openo.crossdomain.commonsvc.executor.service.inf.IPluginRuleService;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Plugin Rules ROA Service API<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-18
 */
@Path("/executor/v1/rules")
@Target("executor")
public class PluginRuleROAService extends IResource<IPluginRuleService> {

    /**
     * Get Rest URI<br/>
     *
     * @return Rest URI
     * @since crossdomain 0.5
     */
    @Override
    public String getResUri() {
        return "/executor/v1/rules";
    }

    /**
     * Register Plugin Rules API<br/>
     *
     * @param input Request Input Stream
     * @param context HttpContext
     * @return the result of registering plugin rules
     * @throws ServiceException when fail to register plugin rules
     * @since crossdomain 0.5
     */
    @PUT
    @Path("/register")
    @Consumes({Constants.APPLICATION_JSON})
    @Produces({Constants.APPLICATION_JSON})
    public String registerPluginRule(RequestInputStream input, HttpContext context) throws ServiceException {
        List<PluginRule> ruleLst = null;
        try {
            ruleLst = jsonToPluginRules(input.getBodyStr());

            service.registerRule(ruleLst);
        } catch(ServiceException e) {
            LogUtil.writeOperateExceptionLog(context, AuditItem.LogSeverity.WARNING, e);
            throw e;
        }

        Result result = new Result();

        // record the operation logs
        AuditItem logItem = LogUtil.getAuditItem(context, null, AuditItem.LogSeverity.WARNING);
        LogUtil.writeLog(logItem, PluginRuleUtil.toLogDetail(ruleLst), result);

        return Model2JsonUtil.value2Json(RequestJsonConstants.RESULT, result.getErrorCode());
    }

    /**
     * Unregister Plugin Rules API<br/>
     *
     * @param input Request Input Stream
     * @param context HttpContext
     * @return the result of registering plugin rules
     * @throws ServiceException when fail to unregister plugin rules
     * @since crossdomain 0.5
     */
    @PUT
    @Path("/unregister")
    @Consumes({Constants.APPLICATION_JSON})
    @Produces({Constants.APPLICATION_JSON})
    public String unregisterPluginRule(RequestInputStream input, HttpContext context) throws ServiceException {
        List<PluginRule> ruleLst = null;
        try {
            ruleLst = jsonToPluginRules(input.getBodyStr());

            service.unregisterRule(ruleLst);
        } catch(ServiceException e) {
            LogUtil.writeOperateExceptionLog(context, AuditItem.LogSeverity.WARNING, e);
            throw e;
        }

        Result result = new Result();

        // record the operation logs
        AuditItem logItem = LogUtil.getAuditItem(context, null, AuditItem.LogSeverity.WARNING);
        LogUtil.writeLog(logItem, PluginRuleUtil.toLogDetail(ruleLst), result);

        return Model2JsonUtil.value2Json(RequestJsonConstants.RESULT, result.getErrorCode());
    }

    /**
     * Query All Registed Plugin Rules API<br/>
     *
     * @param context HttpContext
     * @return all registed plugin rules in json format
     * @throws ServiceException when fail to query all registed plugin rules
     * @since crossdomain 0.5
     */
    @GET
    @Consumes({Constants.APPLICATION_JSON})
    @Produces({Constants.APPLICATION_JSON})
    public String getAllPluginRule(HttpContext context) throws ServiceException {
        return PluginRuleUtil.pluginRule2Json(service.getAllRule());
    }

    /**
     * Convert Json String to Plugin Rules Model API<br/>
     *
     * @param content Body String from RequestInputStream(json format)
     * @return List of PluginRule Model
     * @throws ServiceException when fail to complete the conversion
     * @since crossdomain 0.5
     */
    private List<PluginRule> jsonToPluginRules(String content) throws ServiceException {
        if(!StringUtils.hasLength(content)) {
            String msg = String.format(ErrorMessage.OBJECT_NULL_MSG, Constants.REQUEST_BODY);
            ServiceExceptionUtil.throwBadRequestException(msg);
        }

        List<PluginRule> ruleLst = PluginRuleUtil.json2PluginRule(content);

        try {
            ModelChecker.validateModel(ruleLst);
        } catch(ServiceException e) {
            ServiceExceptionUtil.throwBadRequestException(e.getMessage());
        }

        if(!PluginRule.checkRuleValid(ruleLst)) {
            ServiceExceptionUtil.throwBadRequestException(ErrorMessage.VALIDATION_ERROR_MSG);
        }

        return ruleLst;
    }

}
