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

import org.openo.crossdomain.commonsvc.decomposer.model.ResMapping;
import org.openo.crossdomain.commonsvc.decomposer.model.Result;

import org.openo.commonservice.remoteservice.exception.ServiceException;

/**
 * The interface of dao layer for service resource.
 * 
 * @since crossdomain 0.5
 */
public interface IServiceResDao {

	/**
	 * insert resource
	 * 
	 * @param resMapping resource
	 * @return insert result
	 * @throws ServiceException if insert failed.
	 * @since crossdomain 0.5
	 */
	Result<Object> insert(ResMapping resMapping) throws ServiceException;

	/**
	 * delete resource
	 * 
	 * @param resMapping resource
	 * @return delete result
	 * @throws ServiceException if delete failed.
	 * @since crossdomain 0.5
	 */
	Result<Object> delete(ResMapping resMapping) throws ServiceException;

	/**
	 * update resource
	 * 
	 * @param resMapping resource
	 * @return update result
	 * @throws ServiceException if update failed.
	 * @since crossdomain 0.5
	 */
	Result<Object> update(ResMapping resMapping) throws ServiceException;

	/**
	 * Query service resources by service ID and tenant ID.<br>
	 * 
	 * @param serviceID Service ID.
	 * @param tenantID tenant ID.
	 * @return the result of resource list
	 * @throws ServiceException if query failed.
	 * @since crossdomain 0.5
	 */
	Result<List<ResMapping>> getResMappingBySvcID(String serviceID,
			String tenantID) throws ServiceException;

	/**
	 * insert resources
	 * 
	 * @param resMappings the resource list
	 * @return insert result
	 * @throws ServiceException if insert failed.
	 * @since crossdomain 0.5
	 */
	Result<Object> insert(List<ResMapping> resMappings) throws ServiceException;

	/**
	 * delete resources
	 * 
	 * @param resMappings the resource list
	 * @return delete result
	 * @throws ServiceException if delete failed.
	 * @since crossdomain 0.5
	 */
	Result<Object> delete(List<ResMapping> resMappings) throws ServiceException;

	/**
	 * Query service resources by resource ID and tenant ID.<br>
	 * 
	 * @param resID resource ID.
	 * @param tenantID tenant ID.
	 * @return the result of resource list
	 * @throws ServiceException if query failed.
	 * @since crossdomain 0.5
	 */
	Result<ResMapping> getResMappingByID(String resID, String tenantID)
			throws ServiceException;
}
