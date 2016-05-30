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

import java.util.List;

import org.openo.nfvo.vimadapter.util.Vim;

import org.openo.baseservice.remoteservice.exception.ServiceException;

/**
 * 
* The abstract class of vim database dao(data access operation)<br/>
* <p>
* </p>
* 
* @author
* @version NFVO 0.5 May 15, 2016
 */
public interface VimDao {

    int insertVim(Vim vim) throws ServiceException;

    int deleteVim(String vimDn) throws ServiceException;

    int updateVim(Vim vim) throws ServiceException;

    List<Vim> indexVims(int pageSize, int pageNo) throws ServiceException;

    Vim getVimById(String id);

    int getVimByUrl(String url);

    int getVimByName(String name);

    int getVimByUpdateName(String name, String vimId);
}
