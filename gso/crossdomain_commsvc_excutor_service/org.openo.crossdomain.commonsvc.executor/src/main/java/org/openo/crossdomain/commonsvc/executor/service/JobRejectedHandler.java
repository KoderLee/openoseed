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
package org.openo.crossdomain.commonsvc.executor.service;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import org.openo.commonservice.log.OssLog;
import org.openo.commonservice.log.OssLogFactory;

public class JobRejectedHandler implements RejectedExecutionHandler {

    private static OssLog logger = OssLogFactory.getLog(JobRejectedHandler.class);

    /**
     * JobRejectedHandler, when Execution rejected<br/>
     * @param r Runable
	 * @param executor ThreadPoolExecutor
     * @version crossdomain 0.5 2016-3-18
     */	
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        try {

            executor.getQueue().put(r);
        } catch(InterruptedException e) {
            logger.error(e.getMessage());
        }
    }
}
