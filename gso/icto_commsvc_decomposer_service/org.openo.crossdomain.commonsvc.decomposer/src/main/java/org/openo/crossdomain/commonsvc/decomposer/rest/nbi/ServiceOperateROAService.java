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

import javax.xml.ws.spi.http.HttpContext;

import org.openo.crossdomain.commonsvc.decomposer.constant.Constant;
import org.openo.crossdomain.commonsvc.decomposer.constant.ConstantURL;
import org.openo.crossdomain.commonsvc.decomposer.constant.ErrorCode;
import org.openo.crossdomain.commonsvc.decomposer.model.ExecuteTaskAction;
import org.openo.crossdomain.commonsvc.decomposer.model.Result;
import org.openo.crossdomain.commonsvc.decomposer.module.AbsResource;
import org.openo.crossdomain.commonsvc.decomposer.service.inf.IServiceOperateService;
import org.openo.crossdomain.commonsvc.decomposer.util.DecomposerUtil;
import org.openo.crossdomain.commonsvc.decomposer.util.ErrorCodeUtil;
import org.openo.crossdomain.commonsvc.decomposer.util.JsonUtils;

/**
 * The class of ServiceOperateROAService control the decomposer behavior.
 * 
 * @author
 * @version     crossdomain 0.5  2016年4月7日
 */
@Path(ConstantURL.DECOMPOSER_PREFIX)
@Target("ServiceDecomposer")
public class ServiceOperateROAService extends AbsResource<IServiceOperateService> {

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
        return ConstantURL.DECOMPOSER_PREFIX;
    }

    /**
     * Service decomposer flow.<br>
     *
     * @param input HTTP input stream.
     * @param context HTTP context.
     * @param action service decomposer action{create|update|delete|activate|deactivate}.
     * @return if successed return the location of decomposer task.
     * @throws throw ServiceException if decomposer task executes failed.
     * @since   crossdomain 0.5
     */
    @POST
    @Path(ConstantURL.TASKS)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String executeTask(final RequestInputStream input, final HttpContext context,
            @QueryParam("action") final String action) throws ServiceException {
        final String svcBody = input.getBodyStr();

        ErrorCodeUtil.hasLength(logger, svcBody, "svcBody is null!", ErrorCode.SD_BAD_PARAM);

        if(!ExecuteTaskAction.contains(action)) {
            logger.error("ServiceDecomposer::executeTask param action error!");
            throw new ServiceException(ErrorCode.SD_BAD_PARAM, "action error! action: " + action);
        }

        logger.info("ServiceDecomposer::executeTask: " + action);

        // data checker
        JsonUtils.checkCreateSrvData(svcBody);

        final Result<?> result = service.executeTask(context, svcBody, action);

        // {
        // "location":"/decomposer/v1/tasks/{task_id}"
        // }
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constant.LOCATION, result.getData());
        final String retData = jsonObject.toString();
        logger.info("ServiceDecomposer::executeTask: " + retData);

        return retData;
    }

    /**
     * Query service decomposer task detail by service ID.<br>
     *
     * @param context HTTP context.
     * @param serviceID Service ID.
     * @return the detail of decomposer task.
     * @throws throw ServiceException if query failed.
     * @since   crossdomain 0.5
     */
    @GET
    @Path(ConstantURL.TASKS)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String queryTask(final HttpContext context, @QueryParam("serviceid") final String serviceID)
            throws ServiceException {
        // return all records when the input parameters is empty.
        logger.info("ServiceDecomposer::queryTask: ServiceID: " + DecomposerUtil.escapeLinefeed(serviceID));

        final Result<String> result = service.queryTask(context, serviceID);

        // @formatter:off
        // e.g.
        /*
         * "tasks": [{
         * "task_id": "<task uuid>",
         * "tenant_id": "<tenant uuid>",
         * "opertype": "create",
         * "progress": "executing",
         * "result": "",
         * "result_reason": ""
         * },
         * {
         * "task_id": "<task uuid>",
         * "tenant_id": "<tenant uuid>",
         * "opertype": "update",
         * "progress": "decomposing",
         * "result": "",
         * "result_reason": ""
         * }]
         */
        // @formatter:on

        logger.info("ServiceDecomposer::queryTask Success!");
        return result.getData();
    }

    /**
     * Query service detail information by service ID.<br>
     *
     * @param context HTTP context.
     * @param serviceID Service ID.
     * @return the detail of service.
     * @throws throw ServiceException if query failed.
     * @since   crossdomain 0.5
     */
    @GET
    @Path(ConstantURL.QUERY_SERVICE_DETAIL)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String queryService(final HttpContext context, @PathParam("service_id") final String serviceID)
            throws ServiceException {
        logger.info("ServiceDecomposer::queryService ID: " + DecomposerUtil.escapeLinefeed(serviceID));
        ErrorCodeUtil.hasLength(logger, serviceID, "service_id is null!", ErrorCode.SD_BAD_PARAM);

        final Result<String> result = service.queryService(context, serviceID);

        logger.info("ServiceDecomposer::queryService Success!");
        return result.getData();
    }

    /**
     * Query service decomposer task executes process.<br>
     *
     * @param context HTTP context.
     * @param taskID task ID.
     * @return the process of service decomposer task.
     * @throws throw ServiceException if query failed.
     * @since   crossdomain 0.5
     */
    @GET
    @Path(ConstantURL.QUERY_DECOMPOSE_TASK_DETAIL)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String queryExecuteProcess(final HttpContext context, @PathParam("task_id") final String taskID)
            throws ServiceException {
        logger.info("ServiceDecomposer::queryExecuteProcess TaskID: " + DecomposerUtil.escapeLinefeed(taskID));
        ErrorCodeUtil.hasLength(logger, taskID, "TaskID is null!", ErrorCode.SD_BAD_PARAM);

        final Result<String> result = service.queryExecuteProcess(context, taskID);

        logger.info("ServiceDecomposer::queryExecuteProcess Success!");
        return result.getData();
    }

    /**
     * Query service decomposer task executes process, only for client.<br>
     *
     * @param context HTTP context.
     * @param taskID task ID.
     * @return the process of service decomposer task.
     * @throws throw ServiceException if query failed.
     * @since   crossdomain 0.5
     */
    @GET
    @Path(ConstantURL.QUERY_DECOMPOSE_TASK_DETAIL)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String queryTaskProgress(final HttpContext context, @PathParam("task_id") final String taskID)
            throws ServiceException {
        logger.info("ServiceDecomposer::queryTaskProgress TaskID: " + DecomposerUtil.escapeLinefeed(taskID));
        ErrorCodeUtil.hasLength(logger, taskID, "TaskID is null!", ErrorCode.SD_BAD_PARAM);

        final Result<String> result = service.queryExecuteProcess(context, taskID);

        logger.info("ServiceDecomposer::queryTaskProgress Success!");
        return result.getData();
    }

    /**
     * Query the log of service decomposer task.<br>
     *
     * @param context HTTP context.
     * @param taskID task ID.
     * @return service decomposer task log.
     * @throws throw ServiceException if query failed.
     * @since   crossdomain 0.5
     */
    @GET
    @Path(ConstantURL.QUERY_DECOMPOSE_TASK_LOG)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String queryTaskLog(final HttpContext context, @PathParam("task_id") final String taskID)
            throws ServiceException {
        logger.info("ServiceDecomposer::queryTaskLog TaskID: " + DecomposerUtil.escapeLinefeed(taskID));

        ErrorCodeUtil.hasLength(logger, taskID, "TaskID is error!", ErrorCode.SD_BAD_PARAM);

        final Result<String> result = service.queryTaskLog(context, taskID);

        // @formatter:off
        // e.g.
        /*
         * "task": {
         * "task_id": "<task uuid>",
         * "logs": [{
         * "sequence_id": "1",
         * "timestap": "13213",
         * "description": "decompose begin",
         * "result": "",
         * "result_reason": ""
         * },
         * {
         * "sequence_id": "2",
         * "timestap": "12131",
         * "description": "decompose end",
         * "result": "sucess",
         * "result_reason": ""
         * }]
         * }
         */
        // @formatter:on

        final String retData = result.getData();
        logger.info("ServiceDecomposer::queryTaskLog: " + retData);
        return retData;
    }
}
