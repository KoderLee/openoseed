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

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import org.openo.sdno.remoteservice.exception.ServiceException;
import org.openo.sdno.vpn.wan.db.PoModel;
import org.openo.sdno.vpn.wan.servicemodel.SvcModel;

public abstract class DafaultDao<P extends PoModel<M>, M extends SvcModel> extends AbstractDao<P, M> {

    /**
     * Get all mos.
     * @since SDNO 0.5
     */
    public List<M> getAllMo() throws ServiceException {
        final List<P> pos = selectAll();
        if(null == pos) {
            return null;
        }
        return assembleMo(pos);
    }

    /**
     * Get mo by uuid.
     * 
     * @since SDNO 0.5
     */
    public M getMoById(String uuid) throws ServiceException {
        final P po = selectById(uuid);
        if(po == null) {
            return null;
        }
        final List<M> mos = assembleMo(Collections.singletonList(po));
        if(!CollectionUtils.isEmpty(mos)) {
            return mos.get(0);
        }
        return null;
    }

    /**
     * Batch selection, get mo list by uuid list.
     * 
     * @since SDNO 0.5
     */
    public List<M> getMoByIds(List<String> uuids) throws ServiceException {
        final List<P> pos = selectByIds(uuids);
        return assembleMo(pos);
    }

    /**
     * Get mo list by filter condition.
     * 
     * @since SDNO 0.5
     */
    public List<M> getMoByCondition(String fieldName, Object fieldVal, boolean inCondition) throws ServiceException {
        final List<P> pos = selectByCondition(fieldName, fieldVal, inCondition);
        return assembleMo(pos);
    }

    /**
     * Get mo list by filter conditions.
     * 
     * @since SDNO 0.5
     */
    public List<M> getMoByConditions(String fieldName, List<?> fieldVals) throws ServiceException {
        final List<P> pos = selectByConditions(fieldName, fieldVals);
        return assembleMo(pos);
    }

    /**
     * Update mos.
     * 
     * @since SDNO 0.5
     */
    public boolean handleModifyMosWithFkey(List<M> mos, String fieldName, String fieldVal) throws ServiceException {
        List<M> tmpMos = DaoUtil.safeList(mos);
        List<M> oldMos = getMoByConditions(fieldName, Collections.singletonList(fieldVal));
        oldMos = DaoUtil.safeList(oldMos);
        if(tmpMos.isEmpty() && oldMos.isEmpty()) {
            return true;
        }
        final Map<String, M> newMoMap = new HashMap<String, M>();
        final Map<String, M> oldMoMap = new HashMap<String, M>();
        final List<M> addMos = new ArrayList<M>();
        final List<M> updateMos = new ArrayList<M>();
        final List<M> deleteMos = new ArrayList<M>();

        for(final M mo : tmpMos) {
            if(StringUtils.hasLength(mo.getUuid())) {
                newMoMap.put(mo.getUuid(), mo);
            } else {
                DaoUtil.resetUuid(mo);
                addMos.add(mo);
            }
        }

        for(final M mo : oldMos) {
            oldMoMap.put(mo.getUuid(), mo);
        }

        for(final String uuid : newMoMap.keySet()) {
            final M mo = newMoMap.get(uuid);
            if(oldMoMap.containsKey(uuid)) {
                updateMos.add(mo);
                oldMoMap.remove(uuid);
            } else {
                DaoUtil.resetUuid(mo);
                addMos.add(mo);
            }
        }
        deleteMos.addAll(oldMoMap.values());

        for(final M mo : addMos) {
            mo.setValue4Po(fieldName, fieldVal);
        }
        for(final M mo : updateMos) {
            mo.setValue4Po(fieldName, fieldVal);
        }

        addMos(addMos);
        boolean result = updateMos(updateMos);
        result &= delMos(deleteMos);
        return result;
    }

    /**
     * Assemble mo.
     * 
     * @since SDNO 0.5
     */
    public abstract List<M> assembleMo(List<P> pos) throws ServiceException;
}
