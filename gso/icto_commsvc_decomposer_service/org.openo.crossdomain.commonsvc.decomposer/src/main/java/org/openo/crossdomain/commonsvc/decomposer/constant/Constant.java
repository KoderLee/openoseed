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
package org.openo.crossdomain.commonsvc.decomposer.constant;

/**
 * Constant definition
 * 
 * @since crossdomain 0.5
 */
public final class Constant {

	public static final String JOB_ID = "job_id";

	public static final String IS_DRYRUN = "is_dryrun";

	public static final String ONFAILURE = "onfailure";

	public static final String SERVICEDECOMPOSER_DB = "servicedecomposerdb";

	public static final String SERVICEDECOMPOSER_RDB = "servicedecomposerdb";

	public static final String STATUS = "status";

	public static final String SERVICEID = "service_id";

	public static final String ID = "id";

	public static final String RESOURCES = "resources";

	public static final String DEC_RESOURCES = "decresources";

	public static final String RESOURCE = "resource";

	public static final String NAME = "name";

	public static final String SERVICE_JOB = "service_job";

	public static final String SERVICE = "service";

	public static final String RES_STATUS = "res_status";

	public static final String TYPE = "type";

	public static final String TOTALPROCESS = "totalprocess";

	public static final String PROGRESS = "progress";

	public static final String TOKEN = "token";

	public static final String USERID = "userID";

	public static final String USERNAME = "userName";

	public static final String TERMINAL = "terminal";

	public static final String REGISTER_KEY = "rules";

	public static final String DESCRIPTION = "description";

	public static final String DEPENDS = "depends";

	public static final String ACTION = "action";

	public static final String VERSION_DOMAIN = "versionDomain";

	public static final String TASK_ID = "taskid";

	public static final String RESULT = "result";

	public static final String CODE = "code";

	public static final String REASON = "reason";

	public static final String RESULT_REASON = "result_reason";

	public static final String SRV_BASE = "srvBase";

	public static final String TENANT_ID = "tenantid";

	public static final String RESKEY = "resKey";

	public static final String LOCATION = "location";

	public static final String BLANK_STRING = "";

	public static final int NUM_INIT_ZERO = 0;

	/**
	 * key-->properties
	 */
	public static final String PROPERTIES = "properties";

	public static final String ACTIVE_STATUS = "activestatus";

	public static final String VERSION = "version";

	public static final String VERSION_RULS = "[vV]{1}([1-9]|[1-9]\\d)";

	public static final String REDIS_DB = "sdDB";

	public static final String SD_MAP = "decomposer:sdMap";

	public static final String DECOMPOSED_RES = "decomposedRes";

	public static final String TASK_ATTR = "taskAttr";

	public static final String REDIS_FIELD_TASKLOGID = "logID";

	public static final String ACTIVE = "active";

	public static final String DEACTIVE = "deactive";

	public static class OperType {

		public final static String CREATE_DEFINE = "create";

		public final static String UPDATE_DEFINE = "update";

		public final static String DELETE_DEFINE = "delete";

		public final static String ACTIVATE_DEFINE = "activate";

		public final static String DEACTIVATE_DEFINE = "deactivate";
	}

	public static class TaskCommon {

		/**
		 * KEY-->task
		 */
		public final static String TASK = "task";

		/**
		 * KEY-->tasks
		 */
		public final static String TASKS = "tasks";

		/**
		 * KEY-->task_id
		 */
		public final static String TASK_ID = "task_id";

		/**
		 * KEY-->tenant_id
		 */
		public final static String TENANT_ID = "tenant_id";

		/**
		 * KEY-->opertype
		 */
		public final static String OPERTYPE = "opertype";

		/**
		 * KEY-->progress
		 */
		public final static String PROGRESS = "progress";

		/**
		 * KEY-->result
		 */
		public final static String RESULT = "result";

		/**
		 * KEY-->result
		 */
		public final static String RESULT_REASON = "result_reason";

		/**
		 * KEY-->code
		 */
		public final static String CODE = "code";

		/**
		 * KEY-->reason
		 */
		public final static String REASON = "reason";

		/**
		 * KEY-->logs
		 */
		public final static String LOGS = "logs";
	}

	public static class LogKey {

		/**
		 * KEY-->logs
		 */
		public final static String LOG_DETAIL = "detail";
	}

	public static class Data {

		public static final String SRV_RES_DAO = "srvResDao";

		public static final String SRV_BODY = "srvBody";

		public static final String TENANT_ID = "tenantID";

		public static final String CONTEXT = "context";
	}

	public static class Event {

		public static final String RESOURCE_POST_PROCESS = "resourcePostProcess";

		public static final String EXECUT_POST_PROCESS = "executePostProcess";
	}

	public static class Security {

		public static final String X_TENANT_ID = "X-Tenant-Id";

		/**
		 * token key
		 */
		public static final String X_AUTH_TOKEN = "X-Auth-Token";

		/**
		 * token key
		 */
		public static final String X_SUBJECT_TOKEN = "X-Subject-Token";

		public static final String X_USER_ID = "X-User-Id";

		public static final String X_USER_NAME = "X-User-Name";

		public static final String X_CLIENT_ADDR = "x-real-client-addr";
	}

	public static class SpringDefine {

		public static final String SRV_DECOMPOSER_SERVICE = "srvDecomposerService";

		public static final String SRV_OPERATE_SERVICE = "srvOperateService";

		public static final String SRV_OPERATE_TASK = "srvOperateTask";

		public static final String SRV_FORMATION = "srvFormation";

		public static final String SRV_OPERATE_DAO = "srvOperateDao";
	}
}
