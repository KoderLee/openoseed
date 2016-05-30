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
package org.openo.crossdomain.commonsvc.executor.common.util;

import org.openo.commonservice.biz.trail.AuditItem;
import org.openo.commonservice.biz.trail.AuditLog;
import org.openo.commonservice.log.OssLog;
import org.openo.commonservice.log.OssLogFactory;
import org.openo.commonservice.remoteservice.exception.ServiceException;
import org.openo.commonservice.roa.common.HttpContext;

import javax.servlet.http.HttpServletRequest;

import org.openo.crossdomain.commonsvc.executor.common.constant.Constants;
import org.openo.crossdomain.commonsvc.executor.model.ServiceJob;
import org.openo.crossdomain.commonsvc.executor.model.db.Result;

import java.util.HashMap;
import java.util.Map;

public final class LogUtil {

    private static final OssLog log = OssLogFactory.getLogger(LogUtil.class);

    private static Map<String, String> operatonMap = new HashMap<>();

    static {
        operatonMap.put(Constants.URIPATH_EXECUTE_SERVICE, Constants.LogDefine.CREATE_OPERATION);
        operatonMap.put(Constants.URIPATH_RULES_REGISTER, Constants.LogDefine.REGISTER_RULE_OPERATION);
        operatonMap.put(Constants.URIPATH_RULES_UNREGISTER, Constants.LogDefine.UNREGISTER_RULE_OPERATION);
    }

	/**
     * write Start Create Job Log
	 *@param item AuditItem of audit log
	 *@param job ServiceJob
	 *@since crossdomain 0.5 2016-3-18
	 */	
    public static void writeStartCreateJobLog(AuditItem item, ServiceJob job) {
        writeLog(item, job.toLogDetail(false), new Result());
    }

    /**
     * write Create Job Completed Log
	 *@param item AuditItem of audit log
	 *@param job ServiceJob
	 *@since crossdomain 0.5 2016-3-18
	 */
    public static void writeCreateJobCompletedLog(AuditItem item, ServiceJob job) {
        writeLog(item, job.toLogDetail(true), job.getResult());
    }

	/**
     * write Operate Exception Log
	 *@param context Httpcontext
	 *@param level audit log severity level
	 *@param e ServiceException
	 *@since crossdomain 0.5 2016-3-18
	 */
    public static void writeOperateExceptionLog(HttpContext context, AuditItem.LogSeverity level, ServiceException e) {
        if(e.getExceptionArgs().getReasonArgs() != null
                && e.getExceptionArgs().getReasonArgs().length > Constants.NULL_ID) {
            Result result = new Result(Result.FAIL, null, null);
            AuditItem item = getAuditItem(context, null, level);
            writeLog(item, e.getExceptionArgs().getReasonArgs()[0], result);
        }
    }

    /**
     * write Log
	 *@param item audit log item
	 *@param detail audit log detail
	 *@param result audit log result
	 *@since crossdomain 0.5 2016-3-18
	 */
    public static void writeLog(AuditItem item, String detail, Result result) {
        if(item == null) {
            log.error("writeLog, item is null");
            return;
        }

        item.result = result.toLogResult();
        item.detail = detail;

        AuditLog.record(item);
    }

	/**
     * get audit Log item
	 *@param context Httpcontext
	 *@param targetObj operation targetObj
	 *@param e ServiceException
	 *@since crossdomain 0.5 2016-3-18
	 */	
    public static AuditItem getAuditItem(HttpContext context, String targetObj, AuditItem.LogSeverity level) {
        if(targetObj == null) {
            targetObj = Constants.LogDefine.EXECUTOR_TARGET;
        }

        HttpServletRequest req = context.getHttpServletRequest();
        String userId = req.getHeader(Constants.HttpContext.X_USER_ID);
        String userName = req.getHeader(Constants.HttpContext.X_USER_NAME);
        String tenantId = req.getHeader(Constants.HttpContext.X_TENANT_ID);
        String clientAddr = req.getHeader(Constants.HttpContext.X_REAL_CLIENT_ADDR);

        String url = req.getRequestURI();
        String operation = operatonMap.get(url);

        AuditItem item = new AuditItem();
        item.setUser(userId, userName, tenantId, null);
        item.level = level;
        item.operation = operation;
        item.terminal = clientAddr;
        item.eventType = AuditItem.LogEventType.OPERATION.toString();
        item.targetObj = targetObj;

        return item;
    }

}
