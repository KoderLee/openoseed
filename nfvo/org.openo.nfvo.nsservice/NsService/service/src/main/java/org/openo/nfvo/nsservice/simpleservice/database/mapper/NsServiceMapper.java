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

package org.openo.nfvo.nsservice.simpleservice.database.mapper;

import java.util.List;
import java.util.Map;

import org.openo.nfvo.nsservice.simpleservice.entity.NsService;

/**
 * 
* The mapper(refer to DataBase) of NsService class<br/>
* <p>
* </p>
* 
* @author
* @version NFVO 0.5 May 15, 2016
 */
public interface NsServiceMapper {


    /**
     * This method corresponds to the database table nsservice.
     *
     * @param id : Database id
     * @return : 0|1
     */
    int deleteById(String id);


    /**
     * This method corresponds to the database table nsservice.
     *
     * @param record : The entity of the instance
     * @return : 0|1
     */
    int insert(NsService record);

    /**
     * This method corresponds to the database table nsservice.
     *
     * @param record : The entity of the instance
     * @return : 0|1
     */
    int insertSelective(NsService record);


    /**
     * This method corresponds to the database table nsservice.
     *
     * @param id : Database id
     * @return : The entity of the instance
     */
    NsService selectByPrimaryKey(String id);

    List<NsService> selectByServiceId(Map<String, Object> condition);

    /**
     * This method corresponds to the database table nsservice.
     *
     * @param record : The entity of the instance
     * @return : 0|1
     */
    int updateByPrimaryKeySelective(NsService record);

    /**
     * This method corresponds to the database table nsservice.
     *
     * @param record : The entity of the instance
     * @return : 0|1
     */
    int updateByPrimaryKeyWithBlobs(NsService record);

    /**
     * This method corresponds to the database table nsservice.
     *
     * @param record : The entity of the instance
     * @return : 0|1
     */
    int updateByPrimaryKey(NsService record);
}
