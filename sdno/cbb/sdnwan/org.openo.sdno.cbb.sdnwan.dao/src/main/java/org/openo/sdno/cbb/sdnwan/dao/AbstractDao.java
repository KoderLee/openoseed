/*
 * Copyright (c) 2016, Huawei Technologies Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *  
 */
package org.openo.sdno.cbb.sdnwan.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import org.openo.sdno.remoteservice.exception.ServiceException;
import org.openo.sdno.exception.DBErrorServiceException;
import org.openo.sdno.inventory.sdk.inf.IInvDAO;
import org.openo.sdno.inventory.sdk.model.QueryParams;
import org.openo.sdno.result.Result;
import org.openo.sdno.vpn.wan.db.PoModel;
import org.openo.sdno.vpn.wan.servicemodel.SvcModel;
import com.puer.framework.base.util.JsonUtil;

public abstract class AbstractDao<P extends PoModel<M>, M extends SvcModel> implements Dao<P, M> {


    @Autowired
    protected IInvDAO<P> invDao;

    /**
     * Add mo list.
     * @since SDNO 0.5
     */
    public abstract void addMos(List<M> mos) throws ServiceException;

    /**
     * Delete mo list.
     * @since SDNO 0.5
     */
    public abstract boolean delMos(List<M> mos) throws ServiceException;

    /**
     * Update mo elements in parameter list.
     * @since SDNO 0.5
     */
    public abstract boolean updateMos(List<M> mos) throws ServiceException;

    protected List<P> selectAll() throws ServiceException {
        final List<P> condition = Collections.emptyList();
        final Result<List<P>> rst = invDao.query(condition, getPoClass());
        if(rst.isSucess()) {
            if(rst.getResultObj() == null) {
                return Collections.emptyList();
            }
            return rst.getResultObj();
        }
        throw new ServiceException("selectAll failed");
    }

    protected P selectById(String uuid) throws ServiceException {
        if(!StringUtils.hasLength(uuid)) {
            return null;
        }
        Result<P> rst;
        try {
            rst = invDao.query(uuid, getPoClass());
            if(rst.isSucess()) {
                return rst.getResultObj();
            }
        } catch(final DBErrorServiceException e) {
        }
        return null;
    }

    protected List<P> selectByCondition(String fieldName, Object fieldVal, boolean inCondition) throws ServiceException {
        if(inCondition) {
            return selectByConditions(fieldName, Collections.singletonList(fieldVal));
        } else {
            final Map<String, String> attr = new HashMap<String, String>();
            attr.put(fieldName, String.valueOf(fieldVal));
            final Result<List<P>> rst =
                    invDao.query(getPoClass(), new QueryParams(JsonUtil.toJson(attr), "all", null, null));
            if(rst.isSucess()) {
                return rst.getResultObj();
            }
            throw new ServiceException("selectByCondition failed");
        }
    }

    protected List<P> selectByConditions(String fieldName, List<?> fieldVals) throws ServiceException {
        if(CollectionUtils.isEmpty(fieldVals)) {
            return Collections.emptyList();
        }
        List<P> rst = new ArrayList<P>(fieldVals.size());

        List<?> groupedIds = DaoUtil.splitList(fieldVals);
        for(Object elementUuids : groupedIds) {
            rst.addAll(selectByFiniteConditions(fieldName, (List<?>)elementUuids));
        }
        return rst;
    }

    private List<P> selectByFiniteConditions(String fieldName, List<?> fieldVals) throws ServiceException {
        if(CollectionUtils.isEmpty(fieldVals)) {
            return Collections.emptyList();
        }
        final List<String> strFildVals = new ArrayList<String>(fieldVals.size());
        for(final Object obj : fieldVals) {
            strFildVals.add(String.valueOf(obj));
        }
        final Map<String, List<String>> attr = new HashMap<String, List<String>>();
        attr.put(fieldName, strFildVals);
        final Result<List<P>> rst =
                invDao.query(getPoClass(), new QueryParams(JsonUtil.toJson(attr), "all", null, null));
        if(rst.isSucess()) {
            if(rst.getResultObj() == null) {
                return Collections.emptyList();
            }
            return rst.getResultObj();
        }
        throw new ServiceException("selectByCondition failed");
    }

    protected List<P> selectByIds(List<String> uuids) throws ServiceException {
        if(CollectionUtils.isEmpty(uuids)) {
            return Collections.emptyList();
        }
        List<P> rst = new ArrayList<P>(uuids.size());

        List<List<String>> groupedIds = DaoUtil.splitList(uuids);
        for(List<String> elementUuids : groupedIds) {
            rst.addAll(selectByFiniteIds(elementUuids));
        }
        return rst;
    }

    private List<P> selectByFiniteIds(List<String> uuids) throws ServiceException {
        if(CollectionUtils.isEmpty(uuids)) {
            return Collections.emptyList();
        }
        final Map<String, String[]> attr = new HashMap<String, String[]>();
        attr.put("uuid", uuids.toArray(new String[uuids.size()]));
        final Result<List<P>> rst =
                invDao.query(getPoClass(), new QueryParams(JsonUtil.toJson(attr), "all", null, null));
        if(rst.isSucess()) {
            if(rst.getResultObj() == null) {
                return Collections.emptyList();
            }
            return rst.getResultObj();
        }
        throw new ServiceException("selectByFiniteIds failed");
    }

    protected List<String> insertWithFkey(List<P> pos, String fieldName, String fieldVal) throws ServiceException {
        if(CollectionUtils.isEmpty(pos)) {
            return null;
        }
        for(final P po : pos) {
            DaoUtil.setField(po, fieldName, fieldVal);
        }
        return insert(pos);
    }

    protected List<String> insert(List<P> pos) throws ServiceException {
        if(CollectionUtils.isEmpty(pos)) {
            return null;
        }
        final Result<List<String>> rst = invDao.add(pos, getPoClass());
        if(rst.isSucess()) {
            DaoUtil.fillUuid(pos, rst.getResultObj());
            return rst.getResultObj();
        }
        return null;
    }

    protected boolean update(List<P> pos) throws ServiceException {
        if(CollectionUtils.isEmpty(pos)) {
            return true;
        }
        final Result<?> rst = invDao.update(pos, getPoClass());
        if(rst.isSucess()) {
            return true;
        }
        return false;
    }

    protected boolean delete(List<P> pos) throws ServiceException {
        if(CollectionUtils.isEmpty(pos)) {
            return true;
        }
        final List<String> uuids = new ArrayList<String>();
        for(final P po : pos) {
            uuids.add(po.getUuid());
        }
        final Result<?> rst = invDao.batchDelete(uuids, getPoClass());
        if(rst.isSucess()) {
            return true;
        }
        return false;
    }

    protected boolean deleteByCondition(String fieldName, Object fieldVal, boolean inCondition) throws ServiceException {
        final List<P> pos = selectByCondition(fieldName, fieldVal, inCondition);
        if(CollectionUtils.isEmpty(pos)) {
            return true;
        }
        return delete(pos);
    }

    protected boolean deleteByConditions(String fieldName, List<?> fieldVals) throws ServiceException {
        final List<P> pos = selectByConditions(fieldName, fieldVals);
        if(CollectionUtils.isEmpty(pos)) {
            return true;
        }
        return delete(pos);
    }

    protected abstract Class<P> getPoClass();
}
