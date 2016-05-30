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
package org.openo.crossdomain.commonsvc.executor.activator;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import org.openo.commonservice.log.OssLog;
import org.openo.commonservice.log.OssLogFactory;

/**
 * Activator API<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-18
 */
public class Activator implements BundleActivator {

    private static OssLog log = OssLogFactory.getLogger(Activator.class);

	/**
     * start the bundle<br/>
     *
     * @param context BundleContext
     * @throws Exception when fail to start the bundle
     * @since crossdomain 0.5
     */
    @Override
    public void start(BundleContext context) throws Exception {
        log.info("Service Excutor starting.");
        log.info("Service Excutor start succeeded.");
    }
	
	/**
     * stop the bundle<br/>
     *
     * @param context BundleContext
     * @throws Exception when fail to stop the bundle
     * @since crossdomain 0.5
     */
    @Override
    public void stop(BundleContext context) throws Exception {
        log.info("Service Excutor stopping");
        log.info("Service Excutor stop succeeded.");
    }
}
