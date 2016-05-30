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

import org.openo.crossdomain.commonsvc.executor.common.constant.Constants;

import org.openo.commonservice.log.OssLog;
import org.openo.commonservice.log.OssLogFactory;
import org.openo.commonservice.redis.oper.AbstractOper;
import org.openo.commonservice.redis.serializer.BeanSerializer;

public class RedisSingleVariable<T> extends AbstractOper<T> {

    private static final OssLog log = OssLogFactory.getLogger(RedisSingleVariable.class);

    public RedisSingleVariable() {
        super(Constants.BUNDLE_NAME, Constants.RDB_NAME);
    }

    /**
     * Set key to hold string value if key does not exist<br>
     *
     * @param key
     * @param value
     * @return true if the key was set;false if the key was not set
     */
    public boolean setValueIfNotExist(String key, T value) {
        String storeKey = getStoreKey(key);
        BeanSerializer serializer = this.getSerializer(value.getClass());

        Long ret = this.redis.setnx(storeKey, serializer.stringLize(value));
        return (ret == Constants.NUM_ONE);
    }

    public long deleteVariable(String key) {
        String storeKey = getStoreKey(key);
        return this.redis.del(storeKey);
    }

    /**
     * Atomically sets key to value and returns the old value stored at key
     *
     * @param key redisKey
     * @return the old value stored at key, or nil when key did not exist
     */
    public T getSet(Class<T> type, String key, T value) {
        String storeKey = getStoreKey(key);
        BeanSerializer serializer = this.getSerializer(value.getClass());
        String storeStr = this.redis.getSet(storeKey, serializer.stringLize(value));
        if(storeStr == null) {
            return null;
        }

        return serializer.fromString(storeStr, type);
    }

    public T getValue(Class<T> type, String key) {
        String storeKey = getStoreKey(key);
        BeanSerializer serializer = this.getSerializer(type);
        String storeStr = this.redis.get(storeKey);
        if(storeStr == null) {
            return null;
        }

        return serializer.fromString(storeStr, type);
    }

    public boolean setValue(String key, T value) {
        String storeKey = getStoreKey(key);

        boolean ret = true;
        BeanSerializer serializer = this.getSerializer(value.getClass());
        if(!"OK".equals(this.redis.set(storeKey, serializer.stringLize(value)))) {
            log.error("setValueGlobal fails: {}", storeKey);
            ret = false;
        }

        return ret;
    }
}
