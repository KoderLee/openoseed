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
package org.openo.crossdomain.servicemgr.exception;

/**
 * Error definition.<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
public class ErrorCode {

    public static final String SERVICEMGR_SERVICE_STATUS_INPROGRESS = "servicemgr.service.status_inprogress";

    public static final String SERVICEMGR_SERVICE_STATUS_ACTIVE = "servicemgr.service.status_active";

    public static final String SERVICEMGR_SERVICE_STATUS_DEACTIVE = "servicemgr.service.status_deactive";

    public static final String SVCMGR_SERVICEMGR_BAD_PARAM = "servicemgr.bad_param";

    public static final String SVCMGR_OPER_MYSQL_DB_ERROR = "servicemgr.mysql.oper_mysql_db_error";

    public static final String SVCMGR_OPER_REDIS_DB_ERROR = "servicemgr.redis.oper_redis_db_error";

    public static final String SVCMGR_OPER_DB_ERROR = "servicemgr.db.oper_db_error";

    public static final String SVCMGR_SERVICE_NOT_EXIST = "servicemgr.service.not_exist";

    public static final String SVCMGR_SERVICE_CREATE_FAILED = "servicemgr.service.create_failed";

    public static final String SVCMGR_TEMPLATE_NOT_EXIST = "servicemgr.template.not_exist";

    public static final String SVCMGR_PARAMETER_VALIDATE_ERROR = "servicemgr.service.parameter.validate_error";

    public static final String SVCMGR_TEMPLATE_PARSE_ERROR = "servicemgr.service.template.parse_error";

    public static final String SVCMGR_SERVICE_STATUS_PARAM_ERROR = "servicemgr.service.status_param_error";

    public static final String SVCMGR_SVCPKG_NOT_ACTIVATION = "servicemgr.servicePackage.not_activation";

    public static final String SVCMGR_SVCPKG_STATUS_INACTIVE_ERROR =
            "servicemgr.service.service_package_status_inactive";

    public static final String SVCMGR_SVCPKG_STATUS_DELETEDEPENDING_ERROR =
            "servicemgr.service.service_package_status_delete_depending";

    public static final String SVCMGR_SERVICE_CREATE_QUERYPROGRESS_JOB_FAIL =
            "servicemgr.service.create_queryprogress_job_fail";

    public static final String SVCMGR_SERVICE_ACTIVE_QUERYPROGRESS_JOB_FAIL =
            "servicemgr.service.active_queryprogress_job_fail";

    public static final String SVCMGR_SERVICE_DEACTIVE_QUERYPROGRESS_JOB_FAIL =
            "servicemgr.service.deactive_queryprogress_job_fail";

    public static final String SVCMGR_SERVICE_DELETE_QUERYPROGRESS_JOB_FAIL =
            "servicemgr.service.delete_queryprogress_job_fail";

    public static final String SVCMGR_SERVICE_UPDATE_QUERYPROGRESS_JOB_FAIL =
            "servicemgr.service.update_queryprogress_job_fail";

    public static final String SVCMGR_PERMISSION_DENIED = "servicemgr.permission_denied";
}
