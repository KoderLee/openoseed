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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.openo.baseservice.log.OssLog;
import org.openo.baseservice.log.OssLogFactory;

/**
 * Tools for service operation timing task.<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
public class ServiceOperationTimerUtil {

    private static OssLog logger = OssLogFactory.getLog(ServiceOperationTimerUtil.class);

    private static final int THREAD_NUM = 1;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(THREAD_NUM);

    private final static String ICTO_POLL_TIME_CFG_FILE_PATH = "./etc/ICTO_POLL_TIME_CFG.property";

    private static final long PERIOD_WEEK = 7 * 24 * 60 * 60 * 1000;

    /**
     * Start timer.<br/>
     *
     * @since crossdomain 0.5
     */
    public void startTimer() {
        logger.info("start timer.");
        doTask();
    }

    /**
     * Stop timer.<br/>
     *
     * @since crossdomain 0.5
     */
    public void stopTimer() {
        logger.info("stop timer.");
        scheduler.shutdown();
    }

    private void doTask() {

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        calendar.set(year, month, day, 02, 00, 00);
        Date date = calendar.getTime();

        if(date.before(new Date())) {
            date = this.addDay(date, 1);
        }

        ServiceOperationTimerTask task = new ServiceOperationTimerTask();
        File file = new File(ICTO_POLL_TIME_CFG_FILE_PATH);
        long periodTime = getPeriodTime(file);

        logger.info("start ServiceOperationTimer time: {}.", date);

        scheduler.scheduleAtFixedRate(task, date.getTime(), periodTime, TimeUnit.MILLISECONDS);

    }

    private Date addDay(Date date, int num) {
        Calendar startDT = Calendar.getInstance();
        startDT.setTime(date);
        startDT.add(Calendar.DAY_OF_MONTH, num);
        return startDT.getTime();
    }

    private static long getPeriodTime(File file) {

        FileInputStream ins = null;
        try {
            ins = new FileInputStream(file);
            if(file.exists()) {
                Properties properties = new Properties();
                properties.load(ins);

                final String time = properties.getProperty("ICTO_DELETE_HISTORY_OPERATION_PERIOD_TIME");
                if(time != null) {
                    return Long.parseLong(time);
                }
            }
        } catch(FileNotFoundException e) {
            logger.error("ICTO_POLL_TIME_CFG.property is not exist!", e);
        } catch(IOException e) {
            logger.error("exception", e);
        } finally {
            if(ins != null) {
                close(ins);
            }
        }

        return PERIOD_WEEK;
    }

    private static void close(final FileInputStream toClosed) {
        if(toClosed != null) {
            try {
                toClosed.close();
            } catch(IOException e) {
                logger.error("exception", e);
            }
        }
    }
}
