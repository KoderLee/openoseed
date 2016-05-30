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
package org.openo.crossdomain.commonsvc.executor.common.redis;

import org.openo.commonservice.log.OssLog;
import org.openo.commonservice.log.OssLogFactory;
import org.openo.commonservice.redis.oper.MapOper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.openo.crossdomain.commonsvc.executor.common.constant.Constants;

public class RedisMap<T> {

    private static final String SEPARATOR = ":";

    private static final OssLog log = OssLogFactory.getLogger(RedisMap.class);

    private final ReadWriteLock lock;

    private MapOper<T> mapOper;

    private String name;

    private String tenantId;

    private long freshAt = System.currentTimeMillis();

    public RedisMap(String name, String tenantId) {
        this.name = name;
        lock = new ReentrantReadWriteLock(true);
        this.tenantId = tenantId;
    }

    public T get(Class<T> type, String key) {
        List<T> values = get(type, new String[] {key});
        if((values != null) && !values.isEmpty()) {
            return values.get(0);
        }

        return null;
    }

    public List<T> get(Class<T> type, String... fields) {
        try {

            lock.readLock().lockInterruptibly();

            return getMapOper().get(name, type, fields);
        } catch(InterruptedException e) {
            log.error("getAll Exception", e);
        } finally {

            lock.readLock().unlock();
        }

        return null;
    }

    public Map<String, T> getAll(Class<T> type) {
        Map<String, T> all = new HashMap<String, T>();
        try {

            lock.readLock().lockInterruptibly();

            Map<String, T> tmpMap = getMapOper().getAll(name, type);
            if(tmpMap != null) {
                all.putAll(tmpMap);
            }
        } catch(InterruptedException e) {
            log.error("getAll Exception", e);
        } finally {
            lock.readLock().unlock();
        }

        return all;
    }

    public void put(String key, T value) {
        try {
            lock.writeLock().lockInterruptibly();
            getMapOper().put(name, key, value);
        } catch(InterruptedException e) {
            log.error("InterruptedException", e);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void remove(String... keys) {
        try {

            lock.writeLock().lockInterruptibly();
            getMapOper().remove(name, keys);
        } catch(InterruptedException e) {
            log.error("InterruptedException", e);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public boolean containsKey(String key) {
        try {
            lock.readLock().lockInterruptibly();
            return getMapOper().exists(name, key);
        } catch(InterruptedException e) {
            log.error("InterruptedException", e);
        } finally {
            lock.readLock().unlock();
        }
        return false;
    }

    public void clear() {
        try {
            lock.readLock().lockInterruptibly();
            getMapOper().clear(name);
        } catch(InterruptedException e) {
            log.error("InterruptedException", e);
        } finally {
            lock.readLock().unlock();
        }
    }

    public Set<String> keys() {
        Set<String> keys = null;
        try {
            lock.readLock().lockInterruptibly();
            keys = getMapOper().keys(name);
        } catch(InterruptedException e) {
            log.error("keys Exception", e);
        } finally {
            lock.readLock().unlock();
        }

        return keys;
    }

    private MapOper<T> getMapOper() {
        if(mapOper == null) {
            mapOper = new MapOper<T>(getStoreDoamin(Constants.BUNDLE_NAME), Constants.RDB_NAME);
        }

        return mapOper;
    }

    private String getStoreDoamin(String name) {
        if(Constants.NULL_STR.equals(tenantId)) {
            return name;
        } else {
            return tenantId + SEPARATOR + name;
        }
    }

    public long getFreshAt() {
        return freshAt;
    }

    public void setFreshAt(long freshAt) {
        this.freshAt = freshAt;
    }
}
