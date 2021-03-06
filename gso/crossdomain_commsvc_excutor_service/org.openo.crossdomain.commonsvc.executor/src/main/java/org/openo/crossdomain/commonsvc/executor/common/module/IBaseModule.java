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
package org.openo.crossdomain.commonsvc.executor.common.module;

	/**
     * Abstract Base Module(load Service Executor)<br/>
     * 
     * @version crossdomain 0.5 2016-3-18
     */
public abstract class IBaseModule {

	/**
     * Start method<br/>
     * 
     * @version crossdomain 0.5 2016-3-18
     */
    public final void start() {
        doStart();
        init();
    }
	
    /**
     * Stop method<br/>
     * 
     * @version crossdomain 0.5 2016-3-18
     */

    public final void stop() {
        doStop();
        destroy();
    }

    protected void doStart() {

    }

    protected void doStop() {

    }

    protected abstract void init();

    protected abstract void destroy();

}
