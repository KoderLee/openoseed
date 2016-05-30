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

package org.openo.nfvo.vimadapter.service.database.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.openo.nfvo.vimadapter.util.Vim;

/**
 * 
* The mapper between mybatis database and VIM Database Dao(Data access Operation)<br/>
* <p>
* </p>
* 
* @author
* @version NFVO 0.5 May 15, 2016
 */
public interface VimMapper {

    int insertVim(Vim vim);

    int updateVim(Vim vim);

    int deleteVim(String vimDn);

    List<Vim> indexVims(@Param("offset") int offset,
            @Param("pageSize") int pageSize);

    int getCountVims();

    Vim getVimById(String id);

    int getVimByName(String name);

    int getVimByUrl(String url);

    int getVimByUpdateName(@Param("name") String name, @Param("id") String vimId);

}
