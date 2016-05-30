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

import java.util.ArrayList;
import java.util.List;

import org.openo.commonservice.redis.oper.AbstractOper;
import org.openo.commonservice.redis.serializer.BeanSerializer;

public class QueueOper<T> extends AbstractOper<T> {

    public QueueOper(String storeDomain, String redisDb) {
        super(storeDomain, redisDb);
    }

    public long length(String key) {
        String storeKey = getStoreKey(key);
        return redis.llen(storeKey);
    }

    public List<T> getAll(String key, Class<T> type) {
        String storeKey = getStoreKey(key);
        List<String> storeResult = redis.lrange(storeKey, 0, -1);
        if(storeResult == null) {
            return null;
        }

        BeanSerializer serializer = getSerializer(type);
        List<T> result = new ArrayList<T>();
        for(String item : storeResult) {
            result.add(serializer.fromString(item, type));
        }
        return result;
    }

    public void push(String key, T value) {
        String storeKey = getStoreKey(key);
        if(value == null) {
            return;
        }
        BeanSerializer serializer = getSerializer(value.getClass());
        redis.lpush(storeKey, serializer.stringLize(value));
    }

    public T pop(String key, Class<T> type) {
        String storeKey = getStoreKey(key);
        String storeResult = redis.rpop(storeKey);
        if(storeResult == null) {
            return null;
        }

        BeanSerializer serializer = getSerializer(type);
        return serializer.fromString(storeResult, type);
    }
}
