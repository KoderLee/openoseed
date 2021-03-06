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
package org.openo.crossdomain.commonsvc.executor.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Abstract LoopRunnable API<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-18
 */
public abstract class LoopRunnable implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(LoopRunnable.class);

    private boolean isRunning = true;
	/**
     * Loop Runable Exit method<br/>
     * 
     * @version crossdomain 0.5 2016-3-18
     */
    final public void exit() {
        isRunning = false;
    }

		/**
     * Loop Runable run method<br/>
     * 
     * @version crossdomain 0.5 2016-3-18
     */
    @Override
    final public void run() {
        while(isRunning) {
            try {
                loopRun();
            } catch(Exception e) {
                log.error("[LoopRunnable.run]", e);
            }
        }
        log.debug("exit thread:{}", Thread.currentThread().getName());
    }

    protected abstract void loopRun();
}
