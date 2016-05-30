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
package org.openo.crossdomain.commonsvc.executor.common.constant;

import java.util.concurrent.TimeUnit;

public class Constants {

    public static final String BUNDLE_NAME = "executor";

    public static final String SERVICE_EXECUTOR_VERSION = "v1";

    public static final String TARGET = "excutor";

    public static final String REST_PRE = "/rest";

    public static final String REST_PATTERN = "/executor/v1/*";

    public static final String URIPATH_SERVICE = "executor/v1/jobs";

    public static final String URIPATH_EXECUTE_SERVICE = "/rest/executor/v1/jobs";

    public static final String URIPATH_RULES_REGISTER = "/rest/executor/v1/rules/register";

    public static final String URIPATH_RULES_UNREGISTER = "/rest/executor/v1/rules/unregister";

    public static final String RDB_NAME = "svcexecutorrdb";

    public static final String SERVICE_EXECUTOR_DB = "serviceexecutordb";

    public static final String NULL_STR = "";

    public static final String EMPTY_JSON = "{}";

    public static final String POLICY_ROLLBACK = "rollback";

    public static final String ACTIVE_URL = "action?action=activate";

    public static final String DEACTIVE_URL = "action?action=deactivate";

    public static final int NULL_ID = 0;

    public static final int INVALID_ID = -1;

    public static final int NUM_ONE = 1;

    public static final int BATCH_NUM = 300;

    public static final int URL_LEN_MAX = 255;

    public static final String APPLICATION_JSON = "application/json";

    public static final String LOCATION_KEY = "location";

    public static final String RULES = "rules";

    public static final String RULEVERSION = "ruleversion";

    public static final String SERVICE = "Service";

    public static final int REST_TIMEOUT = 600000;

    public static final String REQUEST_BODY = "request body";

    public static final String VALID_CHAR = "[0-9a-fA-F]";

    public static final String SHORT_UUID_MATCH = VALID_CHAR + "{1,32}";

    public static final String LONG_UUID_MATCH = VALID_CHAR + "{8}-" + VALID_CHAR + "{4}-" + VALID_CHAR + "{4}-"
            + VALID_CHAR + "{4}-" + VALID_CHAR + "{12}";

    public static final String VERSION_PRE = "v";

    /**
     * one day = 1000 * 60 * 60 * 24(ms)=86400000(ms)
     */
    public static final int ONE_DAY_AS_MILLIS_SECONDS = 86_400_000;

    public static final int ALMOST_ONE_DAY_AS_MS = 80_000_000;

    /**
     * 5min = 1000 * 60 * 5(ms)=300_000(ms)
     */
    public static final int FIVE_MINUTES_AS_MILLIS = 300_000;

    public static final int DELETE_COMPLETED_JOBS_AFTER_DAYS = 30;

    public static final String EXECUTED_JOB_RD = "executedJob";

    public static final String TENANT_ID_SET_RD = "tenantIdSet";

    public static final String JOB_ID_SET_RD = "jobIdSet";

    public static final String DEAD_INSTANCE_RD = "deadInstance";

    public static final String TENANT_TOKEN_RD = "tenantToken";

    public static final String GETRESULT = "GetResult";

    public static final String SEPARATOR = ":";

    public static final int TYPELEN_MAX = 64;

    public static final String REDIS_USED_MEMORY = "used_memory";

    public static final String SLASH = "/";

    /**
     * <p>
     * 1073741824(1G) * 0.85
     * </p>
     */
    public static final int REDIS_MEM_ALARM = 912680550;

    public static class ThreadPoolConfig {

        public static final int COREPOOLSIZE = 10;

        public static final int MAXIMUMPOOLSIZE = 15;

        public static final long KEEPALIVETIME = 0L;

        public static final TimeUnit KEEPALIVETIMEUNIT = TimeUnit.MILLISECONDS;

        public static final int POOLQUEUEMAXSIZE = 100;
    }

    public static class HttpContext {

        public static final String CONTENT_TYPE_HEADER = "Content-Type";

        public static final String MEDIA_TYPE_JSON = "application/json;charset=UTF-8";

        public static final String X_TENANT_ID = "X-Tenant-Id";

        public static final String X_USER_ID = "X-User-Id";

        public static final String X_USER_NAME = "X-User-Name";

        public static final String X_REAL_CLIENT_ADDR = "x-real-client-addr";

        public static final String X_AUTH_TOKEN = "X-Auth-Token";

        public static final String X_SUBJECT_TOKEN = "X-Subject-Token";
    }

    public static class SpringDefine {

        public static final String SCHEDULER = "scheduler";

        public static final String RESTORER = "restorer";

        public static final String SCHEDULERPROXY = "schedulerProxy";

        public static final String DEADINSTANCEOPER = "deadInstanceOper";

        public static final String JOBIDOPER = "jobIdOper";
    }

    public static class LogDefine {

        public static final String DECOMPOSER_SOURCE = "ServiceDecomposer";

        public static final String SERVICEPLUGIN_SOURCE = "ServicePlugin";

        public static final String EXECUTOR_TARGET = "ServiceExecutor";

        public static final String CREATE_OPERATION = "create execution Job";

        public static final String REGISTER_RULE_OPERATION = "register execution rule";

        public static final String UNREGISTER_RULE_OPERATION = "unregister execution rule";

        public static final String JOB_START = "job start";

        public static final String JOB_END = "job end";
    }
}
