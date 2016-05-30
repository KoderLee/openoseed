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

import org.openo.commonservice.log.OssLog;
import org.openo.commonservice.log.OssLogFactory;
import org.openo.commonservice.roa.ROA;
import org.openo.commonservice.roa.exception.DuplicateServiceException;
import org.openo.commonservice.roa.exception.IllegalServiceException;
import org.openo.commonservice.roa.exception.NotExistServiceException;

import java.util.ArrayList;
import java.util.List;

public abstract class IServiceModule extends IBaseModule {

    private static final OssLog logger = OssLogFactory.getLogger(IServiceModule.class);

    private List<IResource<?>> roaResList = new ArrayList<IResource<?>>();

    public final void setRoaResList(List<IResource<?>> roaResList) {
        this.roaResList = roaResList;
    }
	/**
     * start method<br/>
     * 
     * @version crossdomain 0.5 2016-3-18
     */
    @Override
    public void doStart() {
        registerRoaResource();
    }

	/**
     * Stop method<br/>
     * 
     * @version crossdomain 0.5 2016-3-18
     */
    @Override
    public void doStop() {
        unRegisterRoaResource();
    }

	
    /**
     * Register ROA Service API<br/>
     * 
     * @version crossdomain 0.5 2016-3-18
     */
    public final void registerRoaResource() {
        for(IResource<?> res : roaResList) {
            if(res != null) {
                res.getClass().getName();
                try {
                    ROA.addRestfulService(res);
                    logger.info("ROA.addRestfulService = {}", res.getResUri());
                } catch(IllegalServiceException | DuplicateServiceException e) {
                    logger.error("ROA.addRestfulService error:" + res.getClass().getName(), e);
                }
            }
        }
    }
	
    /**
     * Unregister ROA Service API<br/>
     * 
     * @version crossdomain 0.5 2016-3-18
     */
    public final void unRegisterRoaResource() {
        for(IResource<?> res : roaResList) {
            if(res != null) {
                try {
                    ROA.removeRestfulService(res);
                    logger.info("ROA.removeRestfulService = " + res.getResUri());
                } catch(IllegalServiceException | NotExistServiceException e) {
                    logger.error("ROA.removeRestfulService error:" + res.getClass().getName(), e);
                }
            }
        }
    }
}
