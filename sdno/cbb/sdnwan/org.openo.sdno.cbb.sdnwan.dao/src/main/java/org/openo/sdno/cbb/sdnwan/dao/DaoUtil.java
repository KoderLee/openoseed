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

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import org.openo.sdno.remoteservice.exception.ServiceException;
import org.openo.sdno.inventory.sdk.model.annotation.MOResType;
import org.openo.sdno.vpn.wan.db.PoModel;
import org.openo.sdno.vpn.wan.servicemodel.SvcModel;
import com.puer.framework.container.util.UUIDUtils;

public final class DaoUtil {

    private static final int MAX_LIST_SIZE = 500;

    private DaoUtil() {

    }

    /**
     * Get table name.
     * 
     * @since SDNO 0.5
     */
    public static String getTableName(Class<?> clazz) {
        final MOResType moResType = clazz.getAnnotation(MOResType.class);
        if(moResType != null) {
            return moResType.infoModelName();
        }
        return null;
    }

    /**
     * Batch mo convert.
     * 
     * @since SDNO 0.5
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <T extends PoModel> List<T> batchMoConvert(List<? extends SvcModel> mos, Class<T> poClass) {
        final List<T> pos = new ArrayList<T>();
        if(mos == null) {
            return pos;
        }
        try {
            for(final SvcModel mo : mos) {
                final T po = poClass.newInstance();
                po.fromSvcModel(mo);
                pos.add(po);
            }
        } catch(final InstantiationException e) {
            return null;
        } catch(final IllegalAccessException e) {
            return null;
        }
        return pos;
    }

    /**
     * Batch po convert.
     * 
     * @since SDNO 0.5
     */
    public static <T extends SvcModel> List<T> batchPoConvert(List<? extends PoModel<T>> pos, Class<T> moClass) {
        final List<T> mos = new ArrayList<T>();
        if(pos == null) {
            return mos;
        }
        for(final PoModel<T> po : pos) {
            final T mo = po.toSvcModel();
            mos.add(mo);
        }
        return mos;
    }

    /**
     * Fill uuid for pos.
     * 
     * @since SDNO 0.5
     */
    @SuppressWarnings("rawtypes")
    public static void fillUuid(List<? extends PoModel> pos, List<String> uuids) {
        if(CollectionUtils.isEmpty(pos) || CollectionUtils.isEmpty(uuids)) {
            return;
        }
        if(pos.size() != uuids.size()) {
            throw new IllegalArgumentException("parametets size is not euqal");
        }
        for(int i = 0; i < pos.size(); i++) {
            final PoModel po = pos.get(i);
            po.setUuid(uuids.get(i));
        }
    }

    /**
     * Set value of field.
     * 
     * @since SDNO 0.5
     */
    public static void setField(Object targetObj, String fieldName, Object val) throws ServiceException {
        if(targetObj == null || fieldName == null) {
            throw new IllegalArgumentException("targetObj or fieldName is null");
        }
        try {
            final Class<?> clazz = targetObj.getClass();
            final Field field = getField(clazz, fieldName);
            if(field == null) {
                throw new ServiceException("Invalid fieldName");
            }
            if(val != null && field.getType() != val.getClass()) {
                throw new ServiceException("Mismatch field and value type");
            }
            final Method setMethod = findSetMethod(targetObj.getClass(), field);
            if(setMethod == null) {
                throw new ServiceException("Illegal field Name");
            }
            setMethod.invoke(targetObj, val);
        } catch(final NoSuchFieldException e) {
            throw new ServiceException(e);
        } catch(final SecurityException e) {
            throw new ServiceException(e);
        } catch(final IllegalAccessException e) {
            throw new ServiceException(e);
        } catch(final IllegalArgumentException e) {
            throw new ServiceException(e);
        } catch(final InvocationTargetException e) {
            throw new ServiceException(e);
        }
    }

    private static Method findSetMethod(Class<?> clazz, Field field) {
        final String methodsName = "set" + field.getName();
        final Method[] methods = getMethods(clazz);
        for(final Method method : methods) {
            if(!method.getName().equalsIgnoreCase(methodsName)) {
                continue;
            }
            if(method.getReturnType() != Void.TYPE) {
                continue;
            }
            if(method.getParameterTypes().length != 1) {
                continue;
            }
            if(method.getParameterTypes()[0] != field.getType()) {
                continue;
            }
            return method;
        }
        return null;
    }

    private static Method findGetMethod(Class<?> clazz, Field field) {
        final String methodsName = "get" + field.getName();
        final Method[] methods = getMethods(clazz);
        for(final Method method : methods) {
            if(!method.getName().equalsIgnoreCase(methodsName)) {
                continue;
            }
            if(method.getParameterTypes().length != 0) {
                continue;
            }
            if(method.getReturnType() != field.getType()) {
                continue;
            }
            return method;
        }
        return null;
    }

    private static Field getField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        Field[] fields = clazz.getDeclaredFields();
        for(Field field : fields) {
            if(field.getName().equals(fieldName)) {
                return field;
            }
        }
        Class<?> superClazz = clazz.getSuperclass();
        while(superClazz != null) {
            fields = clazz.getDeclaredFields();
            for(Field field : fields) {
                if(field.getName().equals(fieldName)) {
                    return field;
                }
            }
            superClazz = superClazz.getSuperclass();
        }
        return null;
    }

    private static Method[] getMethods(Class<?> clazz) {
        final List<Method> methods = new ArrayList<Method>();
        Class<?> superClazz = clazz.getSuperclass();
        while(superClazz != null) {
            methods.addAll(Arrays.asList(superClazz.getDeclaredMethods()));
            superClazz = superClazz.getSuperclass();
        }
        methods.addAll(Arrays.asList(clazz.getDeclaredMethods()));
        return methods.toArray(new Method[methods.size()]);
    }

    /**
     * Update slave mo.
     * 
     * @since SDNO 0.5
     */
    public static <M extends SvcModel, S extends SvcModel, P extends PoModel<M>, T extends PoModel<S>> boolean
            updateSlaveMo(M mastMo, S slaveMo, DafaultDao<P, M> mastDao, DafaultDao<T, S> slaveDao, String slaveIdName)
                    throws ServiceException {
        T oldSlavePo = null;
        final P mastPo = mastDao.selectById(mastMo.getUuid());
        if(null == mastPo) {
            throw new ServiceException("Invalid mo uuid");
        }
        final String slaveUuid = (String)getFieldVal(mastPo, slaveIdName);
        if(StringUtils.hasLength(slaveUuid)) {
            oldSlavePo = slaveDao.selectById(slaveUuid);
        }

        if(slaveMo == null) {
            if(oldSlavePo != null) {
                final S oldSlaveMo = slaveDao.getMoById(oldSlavePo.getUuid());
                return slaveDao.delMos(Collections.singletonList(oldSlaveMo));
            }

            return true;
        }

        if(oldSlavePo == null) {
            resetUuid(slaveMo);
            slaveDao.addMos(Collections.singletonList(slaveMo));
            return true;
        }

        slaveMo.setUuid(oldSlavePo.getUuid());
        return slaveDao.updateMos(Collections.singletonList(slaveMo));
    }

    private static Object getFieldVal(Object target, String fieldName) throws ServiceException {
        final Class<?> clazz = target.getClass();
        try {
            final Method getMethod = findGetMethod(clazz, clazz.getDeclaredField(fieldName));
            if(null == getMethod) {
                throw new ServiceException("unsupported field" + fieldName);
            }
            return getMethod.invoke(target, new Object[] {});
        } catch(final Exception ex) {
            throw new ServiceException("500", ex);
        }
    }

    /**
     * Fill uuid.
     * 
     * @since SDNO 0.5
     */
    public static List<String> setUuidIfEmpty(List<? extends SvcModel> mos) {
        if(mos == null) {
            return new ArrayList<String>();
        }
        final List<String> uuids = new ArrayList<String>(mos.size());
        for(final SvcModel mo : mos) {
            if(mo == null) {
                throw new IllegalArgumentException("there is null element in list");
            }
            if(StringUtils.hasLength(mo.getUuid())) {
                uuids.add(mo.getUuid());
                continue;
            }
            final String uuid = UUIDUtils.createBase64Uuid();
            uuids.add(uuid);
            mo.setUuid(uuid);
        }
        return uuids;
    }

    /**
     * Reset uuid.
     * 
     * @since SDNO 0.5
     */
    public static List<String> resetUuids(List<? extends SvcModel> mos) {
        if(mos == null) {
            return new ArrayList<String>();
        }
        final List<String> uuids = new ArrayList<String>(mos.size());
        for(final SvcModel mo : mos) {
            if(mo == null) {
                throw new IllegalArgumentException("there is null element in list");
            }
            final String uuid = UUIDUtils.createBase64Uuid();
            uuids.add(uuid);
            mo.setUuid(uuid);
        }
        return uuids;
    }

    /**
     * Reset uuid.
     * 
     * @since SDNO 0.5
     */
    public static String resetUuid(SvcModel mo) {
        if(mo == null) {
            return null;
        }
        final String uuid = UUIDUtils.createBase64Uuid();
        mo.setUuid(uuid);
        return uuid;
    }

    /**
     * Get uuid list.
     * 
     * @since SDNO 0.5
     */
    public static List<String> getUuids(List<? extends SvcModel> mos) {
        final List<String> uuids = new ArrayList<String>();
        if(CollectionUtils.isEmpty(mos)) {
            return uuids;
        }
        for(final SvcModel mo : mos) {
            uuids.add(mo == null ? null : mo.getUuid());
        }
        return uuids;
    }

    /**
     * Set foreign key.
     * 
     * @since SDNO 0.5
     */
    public static void setForeignKey(List<? extends SvcModel> mos, String fKeyName, String fKeyVal) {
        if(CollectionUtils.isEmpty(mos)) {
            return;
        }

        if(StringUtils.hasLength(fKeyName) && StringUtils.hasLength(fKeyVal)) {
            for(final SvcModel mo : mos) {
                mo.setValue4Po(fKeyName, fKeyVal);
            }
        } else {
            throw new IllegalArgumentException("empty foreign key name or value");
        }
    }

    /**
     * Add collections.
     * 
     * @since SDNO 0.5
     */
    public static <T> void addCollections(Collection<T> fromClctn, Collection<T> toClctn) {
        if(fromClctn != null && toClctn != null) {
            toClctn.addAll(fromClctn);
        }
    }
}
