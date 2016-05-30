/*******************************************************************************
 * Copyright (c) 2016, Huawei Technologies Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package org.openo.crossdomain.commonsvc.decomposer.service.inf;

import java.util.List;

import javax.xml.ws.spi.http.HttpContext;

import org.openo.crossdomain.commonsvc.decomposer.model.Result;
import org.openo.crossdomain.commonsvc.decomposer.model.ServiceDecomposerMapping;

/**
 * The interface of logic layer for decomposer rules register flow.
 * @since   crossdomain 0.5
 */
public interface IServiceDecomposerService extends IService {

    /**
     * Register decomposer rules.<br>
     *
     * @param HttpContext HTTP context.
     * @param sdMappingLst the rules list.
     * @return result if register rules successed.
     * @throws ServiceException if register rules failed.
     * @since   crossdomain 0.5
     */
    Result<String> regService(HttpContext context, List<ServiceDecomposerMapping> sdMappingLst) throws ServiceException;

    /**
     * Query decomposer rules.<br>
     *
     * @param HttpContext HTTP context.
     * @param typename the register object name.
     * @param regtype the register object type, only support service/resource.
     * @return result, return all records if input parameters are empty.
     * @throws ServiceException if query rules failed.
     * @since   crossdomain 0.5
     */
    Result<String> queryService(HttpContext context, String typename, String regtype) throws ServiceException;

    /**
     * Unregister decomposer rules.<br>
     *
     * @param HttpContext HTTP context.
     * @param sdMappingLst the rules list.
     * @return unregister result, if delete rules successed.
     * @throws ServiceException if delete rules failed.
     * @since   crossdomain 0.5
     */
    Result<String> deleteService(HttpContext context, List<ServiceDecomposerMapping> sdMappingLst)
            throws ServiceException;

    /**
     * Query decomposer rules.<br>
     *
     * @param svcType the register service name.
     * @param regtype the register service type, only support service/resource.
     * @param version the register service version.
     * @return result if query rules successed.
     * @throws ServiceException if query rules failed.
     * @since   crossdomain 0.5
     */
    Result<ServiceDecomposerMapping> getServiceByType(final String svcType, final String regType, final String version)
            throws ServiceException;
}
