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

package org.openo.nfvo.vimadapter.util.dao;

import org.openo.nfvo.vimadapter.util.constant.Constant;

import org.openo.baseservice.mybatis.session.MapperManager;

/**
 * database abstract class to get the MapperManager.
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
        return mapperManager.getMapper(type, Constant.VIM_DB);
    }

    public MapperManager getMapperManager() {
        return mapperManager;
    }

    public void setMapperManager(MapperManager mapperManager) {
        this.mapperManager = mapperManager;
    }
}
