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

import org.openo.baseservice.log.OssLog;
import org.openo.baseservice.log.OssLogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 
* used for spring configration<br/>
* <p>
* </p>
* 
* @author
* @version NFVO 0.5 May 15, 2016
 */
public class SpringContextHolder implements ApplicationContextAware {

    private static final Logger LOG = LoggerFactory.getLogger(SpringContextHolder.class);

    private static ApplicationContext appContext;

    public void setApplicationContext(ApplicationContext applicationContext) {
        setAppContext(applicationContext);
    }

    public static ApplicationContext getApplicationContext() {
        checkApplicationContext();
        return appContext;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getEntity(String name) {
        checkApplicationContext();
        return (T)appContext.getBean(name);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getEntity(Class<T> requiredType) {
        checkApplicationContext();
        return (T)appContext.getBeansOfType(requiredType);
    }

    public static void cleanApplicationContext() {
        appContext = null;
    }

    private static void checkApplicationContext() {
        if(appContext == null) {
            LOG.error("spring appContext do not insert.");
            throw new IllegalStateException("spring appContext is null.");
        }
    }

    private static void setAppContext(ApplicationContext applicationContext) {
        SpringContextHolder.appContext = applicationContext;
    }
}
