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

public class RequestJsonConstants {

    public static final String SERVICE_JOB = "service_job";

    public static final String STATUS = "status";

    public static final String PROGRESS = "progress";

    public static final String RESULT = "result";

    public static final String RESULT_REASON = "result_reason";

    public static final String action = "action";

    public static final String finished = "finished";

    public static final String data = "data";

    public static class ServiceJob {

        public static final String jobId = "job_id";

        public static final String service = "service";

        public static final String serviceList = "services";

        public static final String srvVersion = "srv_version";

        public static final String createdTime = "created_at";

        public static final String completedTime = "completed_at";
    }

    public static class Service {

        public static final String serviceId = "service_id";

        public static final String serviceName = "name";

        public static final String isDryrun = "is_dryrun";

        public static final String onFailure = "onfailure";

        public static final String resources = "resources";

        public static final String subServices = "subservices";

        public static final String resStatusList = "res_status";
    }

    public static class Resource {

        public static final String id = "id";

        public static final String name = "name";

        public static final String type = "type";

        public static final String properties = "properties";

        public static final String dependOn = "depends";
    }

    public static class Result {

        public static final String errorCode = "code";

        public static final String reason = "reason";
    }

    public static class ExceptionJson {

        public static final String exceptionId = "exceptionId";

        public static final String exceptionType = "exceptionType";
    }
}
