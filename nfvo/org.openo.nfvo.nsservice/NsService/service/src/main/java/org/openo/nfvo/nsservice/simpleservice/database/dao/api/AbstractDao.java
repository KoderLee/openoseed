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

package org.openo.nfvo.nsservice.simpleservice.database.dao.api;

import org.openo.nfvo.nsservice.simpleservice.constant.Constant;

import org.openo.baseservice.mybatis.session.MapperManager;

/**
 * 
* Absract class for dao(data access operation)<br/>
* <p>
* </p>
* 
* @author
* @version NFVO 0.5 May 15, 2016
 */
public abstract class AbstractDao {

    protected MapperManager mapperManager;

    /**
     * Get Mybatis Mapper.
     *
     * @param type : The class of the instance
     * @param <T> : The type of the instance
     * @return : The instance
     */
    public <T> T getMapperManager(Class<T> type) {
        return mapperManager.getMapper(type, Constant.DO_DB);
    }

    public MapperManager getMapperManager() {
        return mapperManager;
    }

    public void setMapperManager(MapperManager mapperManager) {
        this.mapperManager = mapperManager;
    }
}
