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
package org.openo.crossdomain.commonsvc.decomposer.dao.mapper;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.exceptions.PersistenceException;
import org.openo.crossdomain.commonsvc.decomposer.model.ServiceDecomposerMapping;

/**
 * The interface of mybatis Mapper for service register rules.
 * 
 * @since crossdomain 0.5
 */
public interface ServiceDecomposerMappingMapper {

	/**
	 * insert decomposition rule
	 * 
	 * @param sdMapping decomposition rule
	 * @return result
	 * @throws PersistenceException if insert failed
	 * @since crossdomain 0.5
	 */
	int insertSrvDecomposer(ServiceDecomposerMapping sdMapping)
			throws PersistenceException;

	/**
	 * delete decomposition rule
	 * 
	 * @param sdMapping decomposition rule
	 * @return result
	 * @throws PersistenceException if delete failed
	 * @since crossdomain 0.5
	 */
	int deleteSrvDecomposer(ServiceDecomposerMapping sdMapping)
			throws PersistenceException;

	/**
	 * update decomposition rule
	 * 
	 * @param sdMapping decomposition rule
	 * @return result
	 * @throws PersistenceException if update failed
	 * @since crossdomain 0.5
	 */
	int updateSrvDecomposer(ServiceDecomposerMapping sdMapping)
			throws PersistenceException;

	/**
	 * Query decomposition rules by name and type.<br>
	 * 
	 * @param typeName name
	 * @param regType type
	 * @return decomposition rule list
	 * @throws PersistenceException if query failed
	 * @since crossdomain 0.5
	 */
	@Deprecated
	List<ServiceDecomposerMapping> getSrvDecomposerByType(
			@Param("typeName") String typeName, @Param("regType") String regType)
			throws PersistenceException;

	/**
	 * Query decomposition rules by name and type.<br>
	 * 
	 * @param typeName name
	 * @param regType type
	 * @return decomposition rule list
	 * @throws PersistenceException if query failed
	 * @since crossdomain 0.5
	 */
	List<ServiceDecomposerMapping> getSrvDecomposer(
			@Param("typeName") String typeName, @Param("regType") String regType)
			throws PersistenceException;
}
