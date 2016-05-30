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

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * ExecutorContextHelper
 * supply ExecutorBundle internal method for getting SpringBean<br/>
 *
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
public class ExecutorContextHelper implements ApplicationContextAware {

    /**
     * supply api for single instance
     */
    private static ExecutorContextHelper instance;

    private ApplicationContext appCtx;

    private ExecutorContextHelper() {
        instance = this;
    }

    public static ExecutorContextHelper getInstance() {
        return instance;
    }

    /**
     * SetApplicationContext <br/>
     *
     * @param arg0 ApplicationContext of Spring
     * @throws BeansException when fail to get bean
     * @since crossdomain 0.5
     */
    @Override
    public void setApplicationContext(ApplicationContext arg0) throws BeansException {
        appCtx = arg0;
    }

    /**
     * Get Bean Object<br/>
     *
     * @param beanName bean Name
     * @return correspond Object with input bean name
     * @since crossdomain 0.5
     */
    public Object getBean(String beanName) {
        return appCtx.getBean(beanName);
    }
}
