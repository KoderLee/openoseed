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

import org.openo.commonservice.biz.trail.AuditLog;
import org.openo.commonservice.log.OssLog;
import org.openo.commonservice.log.OssLogFactory;
import org.openo.commonservice.roa.ROA;
import org.openo.crossdomain.commsvc.authorization.filter.AuthFilter;
import org.openo.crossdomain.commsvc.jobscheduler.core.inf.IScheduler;

import javax.annotation.Resource;

import org.openo.crossdomain.commonsvc.executor.activator.Activator;
import org.openo.crossdomain.commonsvc.executor.common.constant.Constants;
import org.openo.crossdomain.commonsvc.executor.common.module.IServiceModule;
import org.openo.crossdomain.commonsvc.executor.service.asyn.AsynClient;

import java.util.concurrent.Executors;

/**
 * Service Executor Module(load Service Executor)<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-18
 */
public class ServiceExecutorModule extends IServiceModule {

    private static OssLog log = OssLogFactory.getLogger(Activator.class);

    @Resource
    IScheduler scheduler;

    @Resource
    Restorer restorer;

    @Resource
    Preparer preparer;

    protected void init() {
        log.info("ServiceExecutorModule init.");

        AuditLog.init(Constants.LogDefine.EXECUTOR_TARGET);

        scheduler.startup();

        // TODO temp

        ROA.addFilter(Constants.REST_PRE, new AuthFilter(), Constants.REST_PATTERN, 1);

        Executors.newSingleThreadExecutor().submit(preparer);

        AsynClient.getInstance().startPollingThread();

        Dispatcher.getInstance().startPollingThread();

        restorer.restoreDataFromStartUp();
    }

    protected void destroy() {
        log.info("ServiceExecutorModule destroy.");

        Preparer.getInstance().exit();
        Dispatcher.getInstance().closePool();
        AsynClient.getInstance().stopPollingThread();
    }
}
