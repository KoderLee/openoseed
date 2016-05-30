/*******************************************************************************
 * Copyright (c) 2016, Huawei Technologies Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package org.openo.crossdomain.commonsvc.decomposer.rest.nbi;

import java.awt.PageAttributes.MediaType;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.spi.http.HttpContext;

import org.openo.crossdomain.commonsvc.decomposer.check.ModelChecker;
import org.openo.crossdomain.commonsvc.decomposer.constant.Constant;
import org.openo.crossdomain.commonsvc.decomposer.constant.ConstantURL;
import org.openo.crossdomain.commonsvc.decomposer.constant.ErrorCode;
import org.openo.crossdomain.commonsvc.decomposer.logutil.ServicedecomposerAuditLog;
import org.openo.crossdomain.commonsvc.decomposer.model.Result;
import org.openo.crossdomain.commonsvc.decomposer.model.ServiceDecomposerMapping;
import org.openo.crossdomain.commonsvc.decomposer.module.AbsResource;
import org.openo.crossdomain.commonsvc.decomposer.service.inf.IServiceDecomposerService;
import org.openo.crossdomain.commonsvc.decomposer.util.DecomposerUtil;
import org.openo.crossdomain.commonsvc.decomposer.util.ErrorCodeUtil;

/**
 * 
 * The class of ServiceDecomposerROAService which provides REGISTER/UNREGISTER/QUERY
 * interface for service/resource design service.
 * 
 * @author
 * @version     crossdomain 0.5  2016年4月7日
 */
@Path(ConstantURL.DECOMPOSER_RULES_PREFIX)
@Target("ServiceDecomposer")
public class ServiceDecomposerROAService extends AbsResource<IServiceDecomposerService> {

    /**
     * logger for the ServiceDecomposerROAService class<br>
     */
    private final OssLog logger = OssLogFactory.getLogger(ServiceDecomposerROAService.class);

    /**
     * Get Rest URI<br/>
     *
     * @return Rest URI
     * @since   crossdomain 0.5
     */
    @Override
    public String getResUri() {
        return ConstantURL.DECOMPOSER_RULES_PREFIX;
    }

    /**
     * Construct the error data.<br>
     *
     * @param obj the error object.
     * @param key the error key.
     * @param errID the error ID.
     * @return error detail.
     * @since  crossdomain 0.5
     */
    private String getErrDetail(final Object obj, final String key, final String errID) {
        JSONArray jsonSDArrayErr = JSONArray.fromObject(obj);
        JSONObject jsonRulesErr = new JSONObject();
        if((ErrorCode.SD_BAD_PARAM == errID) || (ErrorCode.SD_PARAMETER_VALIDATE_ERROR == errID)) {
            jsonRulesErr.put(Constant.REASON, "Input Error.");
        } else {
            jsonRulesErr.put(key, jsonSDArrayErr);
        }
        return jsonRulesErr.toString();
    }

    /**
     * Register decomposer rules for service/resource design.<br>
     *
     * @param input HTTP input stream.
     * @param HttpContext HTTP context.
     * @return result if register rules successed.
     * @throws ServiceException if register rules failed.
     * @since  crossdomain 0.5
     */
    @POST
    @Path(ConstantURL.REGISTE)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String regService(final RequestInputStream input, final HttpContext context) throws ServiceException {
        logger.info("ServiceDecomposer::regService");
        AuditItem item = new AuditItem();
        ServicedecomposerAuditLog.initHeader(item, context, "ServiceDecomposer", "Register rules", LogSeverity.WARNING);
        List<ServiceDecomposerMapping> sdMappingLst = null;
        try {
            String jsonbody = input.getBodyStr();
            sdMappingLst = getDecomposerMapping(jsonbody);
            ErrorCodeUtil.checkObject(logger, input, "Input is Null!", ErrorCode.SD_BAD_PARAM);

            ModelChecker.validateModel(sdMappingLst);

            DecomposerUtil.checkUriPrefix(sdMappingLst);

            final Result<String> result = service.regService(context, sdMappingLst);

            if(result.checkSuccess()) {
                JSONArray jsonSDArray = JSONArray.fromObject(sdMappingLst);
                JSONObject jsonRules = new JSONObject();
                jsonRules.put(Constant.REGISTER_KEY, jsonSDArray);
                item.detail = jsonRules.toString();
                item.result = AuditResult.SUCCESS;
                AuditLog.record(item);
                logger.info("ServiceDecomposer::Register Service Success!");
                return jsonObjSucess.toString();
            } else {
                logger.error("ServiceDecomposer::Register Service Fail!");
                throw new ServiceException(ErrorCode.SD_TASK_EXECUTE_FAIL, "register Service failed!");
            }
        } catch(ServiceException e) {
            item.result = AuditResult.FAILURE;
            item.detail = getErrDetail(sdMappingLst, Constant.REGISTER_KEY, e.getId());
            AuditLog.record(item);
            throw e;
        } catch(JSONException e) {
            item.result = AuditResult.FAILURE;
            JSONObject jsonRulesErr = new JSONObject();
            jsonRulesErr.put(Constant.REASON, "Input Error.");
            item.detail = jsonRulesErr.toString();
            AuditLog.record(item);
            throw new ServiceException(ErrorCode.SD_BAD_PARAM, "Input Error!");
        }
    }

    /**
     * 
     * Query decomposer rules.<br/>
     * 
     * @param context HTTP context.
     * @param typename the register object name.
     * @param regtype the register object type, only support service/resource.
     * @return result if query rules successed.
     * @throws ServiceException if query rules failed.
     * @since  crossdomain 0.5
     */
    @GET
    // @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String queryService(final HttpContext context, @QueryParam("typename") final String typename,
            @QueryParam("regtype") final String regtype) throws ServiceException {
        // return all records when the input parameters are empty.
        logger.info("ServiceDecomposer::queryService");

        final Result<String> result = service.queryService(context, typename, regtype);

        // @formatter:off
        // e.g.
        /*
         * "rules": [{
         * "typeName": "type1",
         * "regType": "resource",
         * "version": "v1",
         * "uri_prefix": "SDN"
         * },
         * {
         * "typeName": "stack",
         * "regType": "service",
         * "version": "v1",
         * "uri_prefix": "ICT"
         * }]
         */
        // @formatter:on

        final String retData = result.getData();
        logger.info("ServiceDecomposer::queryService " + retData);
        return retData;
    }

    /**
     * Unregister decomposer rules.<br>
     *
     * @param input HTTP input stream.
     * @param HttpContext HTTP context.
     * @return unregister result, if unregister rules successed.
     * @throws ServiceException if register rules failed.
     * @since  crossdomain 0.5
     */
    @POST
    @Path(ConstantURL.UNREGISTE)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteService(final RequestInputStream input, final HttpContext context) throws ServiceException {
        logger.info("ServiceDecomposer::deleteService");
        AuditItem item = new AuditItem();
        ServicedecomposerAuditLog.initHeader(item, context, "ServiceDecomposer", "Unregister rules", LogSeverity.RISK);
        List<ServiceDecomposerMapping> sdMappingLst = null;
        try {
            ErrorCodeUtil.checkObject(logger, input, "Input is Null!", ErrorCode.SD_BAD_PARAM);
            String svcBody = input.getBodyStr();
            sdMappingLst = getDecomposerMapping(svcBody);
            ModelChecker.validateModel(sdMappingLst);
            logger.info("all check ok stating deleteService");

            Result<String> result = service.deleteService(context, sdMappingLst);

            if(result.checkSuccess()) {
                item.result = AuditResult.SUCCESS;
                JSONArray jsonSDArray = JSONArray.fromObject(sdMappingLst);
                JSONObject jsonRules = new JSONObject();
                jsonRules.put(Constant.REGISTER_KEY, jsonSDArray);
                item.detail = jsonRules.toString();
                AuditLog.record(item);
                logger.info("ServiceDecomposer::Unregister Service Success!");
                return jsonObjSucess.toString();
            } else {
                logger.error("ServiceDecomposer::Unregister Service Fail!");
                throw new ServiceException(ErrorCode.SD_TASK_EXECUTE_FAIL, "unregister Service failed!");
            }
        } catch(ServiceException e) {
            item.result = AuditResult.FAILURE;
            item.detail = getErrDetail(sdMappingLst, Constant.REGISTER_KEY, e.getId());
            AuditLog.record(item);
            throw e;
        } catch(JSONException e) {
            item.result = AuditResult.FAILURE;
            JSONObject jsonRulesErr = new JSONObject();
            jsonRulesErr.put(Constant.REASON, "Input Error.");
            item.detail = jsonRulesErr.toString();
            AuditLog.record(item);
            throw new ServiceException(ErrorCode.SD_BAD_PARAM, "Input Error!");
        }
    }

    /**
     * 
     * Convert Json to the ServiceDecomposerMapping model.<br>
     *
     * @param svcBody the input json String.
     * @return ServiceDecomposerMapping list.
     * @throws ServiceException if parse data failed.
     * @since  crossdomain 0.5
     */
    @SuppressWarnings("unchecked")
    private List<ServiceDecomposerMapping> getDecomposerMapping(final String svcBody) throws ServiceException {
        ErrorCodeUtil.hasLength(logger, svcBody, "[getDecomposerMapping] svcBody is null!", ErrorCode.SD_BAD_PARAM);

        final JSONObject jsonObject = JSONObject.fromObject(svcBody);
        final JSONArray jsonArray = jsonObject.getJSONArray(Constant.REGISTER_KEY);

        List<ServiceDecomposerMapping> allList = new ArrayList<ServiceDecomposerMapping>();
        allList.addAll(JSONArray.toCollection(jsonArray, ServiceDecomposerMapping.class));

        if(allList.isEmpty()) {
            throw new ServiceException(ErrorCode.SD_BAD_PARAM, "Can not get data from request.");
        }

        return allList;
    }
}
