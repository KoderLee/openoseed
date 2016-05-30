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
package org.openo.crossdomain.commonsvc.decomposer.dao.inf;

import java.util.List;

import org.openo.crossdomain.commonsvc.decomposer.model.Result;
import org.openo.crossdomain.commonsvc.decomposer.model.ServiceDecomposerMapping;

import org.openo.commonservice.remoteservice.exception.ServiceException;

/**
 * The interface of dao layer for decomposer rules register flow.
 * 
 * @since crossdomain 0.5
 */
public interface IServiceDecomposerDao {

	/**
	 * register decomposition rules
	 * 
	 * @param sdMappingLst the rules list.
	 * @return result
	 * @throws ServiceException if register rules failed.
	 * @since crossdomain 0.5
	 */
	Result<String> regService(List<ServiceDecomposerMapping> sdMappingLst)
			throws ServiceException;

	/**
	 * Unregister decomposer rules.<br>
	 * 
	 * @param sdMappingLst the rules list.
	 * @return unregister result
	 * @throws ServiceException if unregister rules failed.
	 * @since crossdomain 0.5
	 */
	Result<String> deleteService(List<ServiceDecomposerMapping> sdMappingLst)
			throws ServiceException;

	/**
	 * Query decomposer rule.<br>
	 * 
	 * @param svcType the register service name.
	 * @param regtype the register service type, only support service/resource.
	 * @param version the register service version.
	 * @return result if query rules successed.
	 * @throws ServiceException if query rules failed.
	 * @since crossdomain 0.5
	 */
	Result<ServiceDecomposerMapping> getServiceByType(String svcType,
			String regType, String version) throws ServiceException;

	/**
	 * Query decomposer rules.<br>
	 * 
	 * @param svcType the register service name.
	 * @param regtype the register service type, only support service/resource.
	 * @param version the register service version.
	 * @return result if query rules successed.
	 * @throws ServiceException if query rules failed.
	 * @since crossdomain 0.5
	 */
	Result<List<ServiceDecomposerMapping>> getServicesByType(String svcType,
			String regType, String version) throws ServiceException;

	/**
	 * Query decomposer rules.
	 * 
	 * @param svcType the register service name.
	 * @param regtype the register service type, only support service/resource.
	 * @return result if query rules successed.
	 * @throws ServiceException if query rules failed.
	 * @since crossdomain 0.5
	 */
	Result<List<ServiceDecomposerMapping>> getServices(String svcType,
			String regType) throws ServiceException;
}
