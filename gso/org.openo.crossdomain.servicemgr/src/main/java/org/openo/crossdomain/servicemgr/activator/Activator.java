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
package org.openo.crossdomain.servicemgr.activator;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.openo.crossdomain.servicemgr.restrepository.impl.RegComponent;
import org.openo.crossdomain.servicemgr.roa.ServiceException;
import org.openo.crossdomain.servicemgr.util.service.ServiceOperationTimerUtil;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;

import org.openo.baseservice.biz.trail.AuditLog;
import org.openo.baseservice.roa.ROA;
import org.openo.baseservice.roa.annotation.Path;
import org.openo.baseservice.roa.exception.ROAException;
import org.openo.baseservice.util.AnnotationUtils;
import org.openo.crossdomain.commsvc.authorization.filter.AuthFilter;

/**
 * An interface that may be implemented when a bundle is started or stopped.<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
public class Activator implements BundleActivator, DestructionAwareBeanPostProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(Activator.class);

    private final ServiceOperationTimerUtil serviceOperationTimerUtil = new ServiceOperationTimerUtil();

    /**
     * Start bundle.<br/>
     *
     * @param context bundle context
     * @throws Exception It happens when initializing.
     * @since crossdomain 0.5
     */
    @Override
    public void start(BundleContext context) throws Exception {

        serviceOperationTimerUtil.startTimer();

        ScheduledExecutorService regComponenThread = new ScheduledThreadPoolExecutor(1);
        regComponenThread.schedule(new RegComponent(regComponenThread), 2, TimeUnit.MILLISECONDS);

        AuditLog.init("ServiceManager");
    }

    /**
     * Stop bundle.<br/>
     * 
     * @param context bundle context
     * @throws Exception stop exception
     * @since crossdomain 0.5
     */
    @Override
    public void stop(BundleContext context) throws Exception {

        serviceOperationTimerUtil.stopTimer();
    }

    /**
     * Post process after initialization.<br/>
     *
     * @param bean bean object
     * @param name the name of bean
     * @return bean object
     * @throws BeansException It happens when adding restful service.
     * @since crossdomain 0.5
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String name) throws BeansException {
        if(isROAService(bean)) {
            try {
                ROA.addRestfulService(bean);
                LOGGER.warn("register roa service {}", bean);

                ROA.addFilter("/rest", new AuthFilter(), "/*", 1);
                LOGGER.info("ServiceManager register autofilter finish!");
            } catch(ROAException e) {
                LOGGER.error("register roa service {} fail", bean, e);
            }
        }
        return bean;
    }

    /**
     * Post process before initialization.<br/>
     *
     * @param bean bean object
     * @param name bean name
     * @return bean object
     * @throws BeansException
     * @since crossdomain 0.5
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String name) throws BeansException {
        return bean;
    }

    /**
     * Post process before destruction.<br/>
     *
     * @param bean bean object
     * @param name bean name
     * @throws BeansException It happens when removing restful service.
     * @since crossdomain 0.5
     */
    @Override
    public void postProcessBeforeDestruction(Object bean, String name) throws BeansException {
        if(isROAService(bean)) {
            try {
                ROA.removeRestfulService(bean);
                LOGGER.warn("unRegister roa service {}", bean);
            } catch(ROAException e) {
                LOGGER.error("unRegister roa service {} fail", bean, e);
            }
        }

    }

    /**
     * Judge whether it is ROA service.<br/>
     *
     * @param bean bean object
     * @return true when the path of ROA service exists.
     * @since crossdomain 0.5
     */
    private boolean isROAService(Object bean) {
        Path path = AnnotationUtils.findAnnotation(bean.getClass(), Path.class);
        return path != null;
    }
}
