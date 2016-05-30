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
package org.openo.crossdomain.servicemgr.util.service;

import java.util.TimerTask;

import org.openo.crossdomain.servicemgr.dao.inf.IServiceOperationDao;

import org.openo.baseservice.log.OssLog;
import org.openo.baseservice.log.OssLogFactory;

/**
 * Timer task of service operation.<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
public class ServiceOperationTimerTask extends TimerTask {

    private static OssLog logger = OssLogFactory.getLog(ServiceOperationTimerTask.class);

    /**
     * Start timing task.
     * @see java.util.TimerTask#run()
     */
    @Override
    public void run() {
        try {
            IServiceOperationDao serviceOperationDao =
                    (IServiceOperationDao)SpringContextUtils.getBeanById("serviceOperationDao");

            serviceOperationDao.deleteHistory();
            logger.info("deleting serviceOperation history data executed.");
        } catch(Exception e) {
            logger.error("failed to delete serviceOperation history.", e);
        }

    }

}
