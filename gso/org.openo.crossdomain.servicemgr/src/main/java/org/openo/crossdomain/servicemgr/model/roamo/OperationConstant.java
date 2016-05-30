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
package org.openo.crossdomain.servicemgr.model.roamo;

/**
 * Operation definition.<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
public class OperationConstant {

    public static class RESPONSE_KEY_NUM {

        public static final String OPERATION_KEY = "operation";

        public static final String OPERATIONS_KEY = "operations";

    }

    public static class VALIDATE_FIELDS_NUM {

        public static final String OPERATION_ID = "operation_id";
    }

    public static class OPERATION_NUM {

        public static final String CREATE = "Create";

        public static final String UPDATE = "Update";

        public static final String DELETE = "Delete";

        public static final String ACTIVATE = "Activate";

        public static final String DEACTIVATE = "Deactivate";
    }

    public static class OPERATION_RESULT_NUM {

        public static final String SUCCESS = "success";

        public static final String FAILED = "failed";
    }

    public static class OPERATION_PROGRESS_NUM {

        public static final String DONE = "done";

        public static final String IN_PROGRESS = "inprogress";
    }
}
