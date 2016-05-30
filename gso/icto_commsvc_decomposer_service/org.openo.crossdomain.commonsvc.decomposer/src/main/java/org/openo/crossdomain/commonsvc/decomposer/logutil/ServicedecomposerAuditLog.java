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
package org.openo.crossdomain.commonsvc.decomposer.logutil;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.openo.crossdomain.commonsvc.decomposer.constant.Constant;
import org.openo.crossdomain.commonsvc.decomposer.constant.EnumResult;
import org.openo.crossdomain.commonsvc.decomposer.constant.Constant.LogKey;
import org.openo.crossdomain.commonsvc.decomposer.constant.Constant.OperType;
import org.openo.crossdomain.commonsvc.decomposer.constant.Constant.Security;
import org.openo.crossdomain.commonsvc.decomposer.model.ServiceDecomposerTask;

import org.openo.commonservice.biz.trail.AuditItem;
import org.openo.commonservice.biz.trail.AuditItem.AuditResult;
import org.openo.commonservice.biz.trail.AuditItem.LogEventType;
import org.openo.commonservice.biz.trail.AuditItem.LogSeverity;
import org.openo.commonservice.biz.trail.AuditLog;
import org.openo.commonservice.roa.common.HttpContext;

/**
 * Service decomposer audit log util class
 * 
 * @since crossdomain 0.5
 */
public class ServicedecomposerAuditLog {

	public static class Operation {

		public final static String CREATE = "Service Create";

		public final static String UPDATE = "Service Update";

		public final static String DELETE = "Service Delete";

		public final static String ACTIVATE = "Service Activate";

		public final static String DEACTIVATE = "Service Deactivate";
	}

	/**
	 * initialize Audit log module
	 * 
	 * @since crossdomain 0.5
	 */
	public static void initModule() {
		AuditLog.init("ServiceDecomposer");
	}

	/**
	 * initialize header
	 * 
	 * @param task task
	 * @param item audit item
	 * @since crossdomain 0.5
	 */
	public static void initHeader(final ServiceDecomposerTask task,
			final AuditItem item) {
		task.getUser().SetAuditLogUser(item, task.getTenantID());
		item.eventType = LogEventType.OPERATION.toString();

		item.level = getLevel(task.getOperType());
		item.operation = getOperaion(task.getOperType());
		item.targetObj = task.getServiceID();
	}

	/**
	 * record audit log at the start of the task
	 * 
	 * @param task task
	 * @since crossdomain 0.5
	 */
	public static void addAuditLogBegin(final ServiceDecomposerTask task) {
		initModule();
		AuditItem item = new AuditItem();
		initHeader(task, item);

		final Map<String, Object> resAttributeMap = new HashMap<String, Object>();
		resAttributeMap.put(Constant.TASK_ID, task.getTaskID());
		resAttributeMap.put(LogKey.LOG_DETAIL, "Execute task begin.");
		item.detail = JSONObject.fromObject(resAttributeMap).toString();
		item.result = AuditResult.SUCCESS;
		AuditLog.record(item);
	}

	/**
	 * record audit log at the end of the task
	 * 
	 * @param task task
	 * @since crossdomain 0.5
	 */
	public static void addAuditLogEnd(final ServiceDecomposerTask task) {
		initModule();
		AuditItem item = new AuditItem();
		initHeader(task, item);

		final Map<String, Object> detailMap = new HashMap<String, Object>();
		detailMap.put("status", "Execute task end.");

		if (task.getResult().equals(EnumResult.SUCCESS.getName())) {
			item.result = AuditResult.SUCCESS;
		} else {
			detailMap.put(Constant.RESULT, task.getResult());
			detailMap.put(Constant.RESULT_REASON, task.getResultReason());
			item.result = AuditResult.FAILURE;
		}

		String detail = JSONObject.fromObject(detailMap).toString();
		final Map<String, Object> resAttributeMap = new HashMap<String, Object>();
		resAttributeMap.put(Constant.TASK_ID, task.getTaskID());
		if (!StringUtils.isEmpty(task.getSeJobID())) {
			resAttributeMap.put(Constant.JOB_ID, task.getSeJobID());
		}
		resAttributeMap.put(LogKey.LOG_DETAIL, detail);
		item.detail = JSONObject.fromObject(resAttributeMap).toString();
		AuditLog.record(item);
	}

	private static LogSeverity getLevel(String operType) {
		switch (operType) {
		case OperType.CREATE_DEFINE:
		case OperType.ACTIVATE_DEFINE:
			return AuditItem.LogSeverity.INFO;

		case OperType.DEACTIVATE_DEFINE:
		case OperType.DELETE_DEFINE:
			return AuditItem.LogSeverity.RISK;

		case OperType.UPDATE_DEFINE:
			return AuditItem.LogSeverity.WARNING;

		default:
			return AuditItem.LogSeverity.RISK;
		}
	}

	private static String getOperaion(String operType) {
		switch (operType) {
		case OperType.CREATE_DEFINE:
			return Operation.CREATE;

		case OperType.ACTIVATE_DEFINE:
			return Operation.ACTIVATE;

		case OperType.DEACTIVATE_DEFINE:
			return Operation.DEACTIVATE;

		case OperType.DELETE_DEFINE:
			return Operation.DELETE;

		case OperType.UPDATE_DEFINE:
			return Operation.UPDATE;

		default:
			return "";
		}
	}

	/**
	 * initialize header
	 * 
	 * @param item audit item
	 * @param context http context
	 * @param operObj operate object
	 * @param operation operation
	 * @param level level
	 * @since crossdomain 0.5
	 */
	public static void initHeader(final AuditItem item,
			final HttpContext context, final String operObj,
			final String operation, final LogSeverity level) {
		String userId = context.getHttpServletRequest().getHeader(
				Security.X_USER_ID);
		if (StringUtils.isEmpty(userId)) {
			return;
		}

		String userName = context.getHttpServletRequest().getHeader(
				Security.X_USER_NAME);
		if (StringUtils.isEmpty(userName)) {
			return;
		}

		String domain = context.getHttpServletRequest().getHeader(
				Security.X_TENANT_ID);
		if (StringUtils.isEmpty(domain)) {
			return;
		}

		initModule();
		item.setUser(userId, userName, domain, "");
		item.operation = operation;
		item.terminal = context.getHttpServletRequest().getRemoteAddr();
		item.eventType = LogEventType.OPERATION.toString();
		item.level = level;
		item.targetObj = operObj;
	}
}
