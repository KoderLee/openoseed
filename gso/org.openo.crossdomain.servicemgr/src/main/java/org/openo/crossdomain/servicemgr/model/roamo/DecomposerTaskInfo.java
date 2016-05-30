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
 * Operation type of decomposer task.<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
public class DecomposerTaskInfo {

    public static class TASK_INFO_NUM {

        /**
         * task
         */
        public static final String TASK = "task";

        public static final String ACTION = "action";

        public static final String CREATE = "create";

        public static final String UPDATE = "update";

        public static final String DELETE = "delete";

        public static final String ACTIVATE = "activate";

        public static final String DEACTIVATE = "deactivate";

    }
}
