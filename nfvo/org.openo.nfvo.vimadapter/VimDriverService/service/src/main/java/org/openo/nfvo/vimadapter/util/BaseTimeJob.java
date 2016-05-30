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

package org.openo.nfvo.vimadapter.util;

import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 
* Used for other service to create a time job thread, such as a cron job.<br/>
* <p>
* </p>
* 
* @author
* @version NFVO 0.5 May 15, 2016
 */
public abstract class BaseTimeJob implements Runnable {
    private ScheduledExecutorService service = Executors
            .newSingleThreadScheduledExecutor();
    private long initialDelay = 1;
    private long period = 1;
    private String startTime = "";

    @Override
    public abstract void run();

    public void start() {
        if (!("").equals(startTime)) {
            String[] times = startTime.split(":");
            if (times.length == 2) {
                long minute = Integer.parseInt(times[0]) * 60
                        + Integer.parseInt(times[1]);
                Calendar calendar = Calendar.getInstance();
                long curMinute = calendar.get(Calendar.HOUR_OF_DAY) * 60
                        + calendar.get(Calendar.MINUTE);
                if (curMinute <= minute) {
                    initialDelay = (minute - curMinute) * 60;
                } else {
                    initialDelay = (minute + 24 * 60 - curMinute) * 60;
                }
            }
        }
        service.scheduleAtFixedRate(this, initialDelay, period,
                TimeUnit.SECONDS);
    }

    public void stop() {
        service.shutdown();
    }

    protected void setInitialDelay(long initialDelay) {
        this.initialDelay = initialDelay;
    }

    protected void setPeriod(long period) {
        this.period = period;
    }
}
