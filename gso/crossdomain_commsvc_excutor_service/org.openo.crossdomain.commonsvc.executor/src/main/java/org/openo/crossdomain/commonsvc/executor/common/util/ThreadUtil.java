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
package org.openo.crossdomain.commonsvc.executor.common.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.openo.commonservice.log.OssLog;
import org.openo.commonservice.log.OssLogFactory;

/**
 * thread tool
 */
public final class ThreadUtil {

    private static final OssLog logger = OssLogFactory.getLogger(ThreadUtil.class);

    /**
     * sleep current thread
     *
     * @param milliSecond sleep time, milliSecond
     */
    public static void sleep(long milliSecond) {
        try {
            TimeUnit.MILLISECONDS.sleep(milliSecond);
        } catch(InterruptedException e) {
            logger.error("", e);
        }
    }

    /**
     * start a thread
     *
     * @param runnable runnable
     */
    public static ExecutorService startThread(Runnable runnable) {
        ExecutorService pool = Executors.newSingleThreadExecutor();
        pool.submit(runnable);

        return pool;
    }
}
