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
package org.openo.crossdomain.servicemgr.restrepository.impl;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.openo.crossdomain.servicemgr.model.registermo.Flag;
import org.openo.crossdomain.servicemgr.restrepository.IServicePortalProxy;
import org.openo.crossdomain.servicemgr.util.authorization.SecureInnerTokenHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class for register component.<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
public class RegComponent implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(RegComponent.class);

    public static final int SLEEP_30_SECOND = 30_000;

    public static final String SUCCESS = "SUCCESS";

    private int sleepTime = SLEEP_30_SECOND;

    private final ScheduledExecutorService scheduler;

    public RegComponent(ScheduledExecutorService scheduler) {
        this.scheduler = scheduler;
    }

    private int regComponentResult = Flag.FAIL;

    private int regLayoutresult = Flag.FAIL;

    /**
     * 
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        try {
            runImpl();
        } catch(Exception e) {
            logger.error("register error", e);
        }
    }

    /**
     * Start component.<br/>
     *
     * @since crossdomain 0.5
     */
    public void runImpl() {
        IServicePortalProxy regComponent = new ServicePortalProxy();

        String token = SecureInnerTokenHolder.getInnerSecureToken();

        while(regComponentResult != Flag.SUCCESS) {

            String Componentresult = regComponent.registerComponent(token);
            if(Componentresult.equals(SUCCESS)) {
                regComponentResult = Flag.SUCCESS;

                while(regLayoutresult != Flag.SUCCESS) {

                    String Layresult = regComponent.registerComponentLayout(token);
                    if(Layresult.equals(SUCCESS)) {
                        regLayoutresult = Flag.SUCCESS;
                        return;
                    } else {
                        scheduler.schedule(this, sleepTime, TimeUnit.MILLISECONDS);
                        return;
                    }
                }
            } else {
                scheduler.schedule(this, sleepTime, TimeUnit.MILLISECONDS);
                return;
            }
        }
        scheduler.shutdown();
    }
}
