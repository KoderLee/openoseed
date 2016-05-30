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

package org.openo.nfvo.vimadapter.service.database.dao;

import java.util.List;

import org.openo.nfvo.vimadapter.util.Vim;
import org.openo.nfvo.vimadapter.util.dao.AbstractDao;
import org.openo.nfvo.vimadapter.util.dao.VimDao;
import org.openo.nfvo.vimadapter.service.database.mapper.VimMapper;

import org.openo.baseservice.remoteservice.exception.ServiceException;

/**
 * 
* The operation of vim database<br/>
* <p>
* </p>
* 
* @author
* @version NFVO 0.5 May 15, 2016
 */
public class VimDaoImpl extends AbstractDao implements VimDao {

    public int insertVim(Vim vim) throws ServiceException {
        return getMapperManager(VimMapper.class).insertVim(vim);
    }

    public int deleteVim(String vimDn) throws ServiceException {
        return getMapperManager(VimMapper.class).deleteVim(vimDn);
    }

    public int updateVim(Vim vim) throws ServiceException {
        return getMapperManager(VimMapper.class).updateVim(vim);
    }

    public List<Vim> indexVims(int pageSize, int pageNo)
            throws ServiceException {
        VimMapper vimMapper = getMapperManager(VimMapper.class);
        int offset = (pageNo - 1) * pageSize;
        List<Vim> vimList = vimMapper.indexVims(offset, pageSize);
        return vimList;
    }

    @Override
    public Vim getVimById(String id) {
        return getMapperManager(VimMapper.class).getVimById(id);
    }

    @Override
    public int getVimByUrl(String url) {
        return getMapperManager(VimMapper.class).getVimByUrl(url);
    }

    @Override
    public int getVimByName(String name) {
        return getMapperManager(VimMapper.class).getVimByName(name);
    }

    @Override
    public int getVimByUpdateName(String name, String vimId) {
        return getMapperManager(VimMapper.class)
                .getVimByUpdateName(name, vimId);
    }

}
