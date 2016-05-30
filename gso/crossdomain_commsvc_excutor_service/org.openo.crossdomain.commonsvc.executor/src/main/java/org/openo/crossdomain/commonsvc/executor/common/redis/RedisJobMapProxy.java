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
import org.openo.crossdomain.commonsvc.executor.model.ServiceJob;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

@Component(value = "redisJobMapProxy")
public class RedisJobMapProxy {

    private static final int CAPACITY = 100;

    private static final int REMOVE_5PERCENT = 5;

    private static final int REMOVE_100PERCENT = 100;

    private static RedisJobMapProxy instance;

    private final ReentrantLock lock = new ReentrantLock();

    private int capacity = CAPACITY;

    private Map<String, RedisMap<ServiceJob>> redisOperMap = new HashMap<>();

    public synchronized static RedisJobMapProxy getInstance() {
        if(instance == null) {
            instance = new RedisJobMapProxy();
        }
        return instance;
    }

    public RedisMap<ServiceJob> getRedisOper(String tenantId) {
        RedisMap<ServiceJob> redisOper;
        lock.lock(); // block until condition holds
        try {
            redisOper = redisOperMap.get(tenantId);
            if(redisOper == null) {
                redisOper = putRedisOper(tenantId);
            } else {
                redisOper.setFreshAt(System.currentTimeMillis());
            }
        } finally {
            lock.unlock();
        }

        return redisOper;
    }

    private RedisMap<ServiceJob> putRedisOper(String tenantId) {
        if(redisOperMap.size() >= capacity) {
            removeOldEntry();
        }

        RedisMap<ServiceJob> jobMap = new RedisMap<>(Constants.EXECUTED_JOB_RD, tenantId);
        redisOperMap.put(tenantId, jobMap);

        return jobMap;
    }

    private void removeOldEntry() {
        Map<String, Long> tenantIdMap = new HashMap<>();
        for(Map.Entry<String, RedisMap<ServiceJob>> entry : redisOperMap.entrySet()) {
            tenantIdMap.put(entry.getKey(), entry.getValue().getFreshAt());
        }

        List<Map.Entry<String, Long>> list = new LinkedList<>(tenantIdMap.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Long>>() {

            public int compare(Map.Entry<String, Long> o1, Map.Entry<String, Long> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        int removeSize = capacity * REMOVE_5PERCENT / REMOVE_100PERCENT;
        int i = Constants.NULL_ID;
        for(Map.Entry<String, Long> entry : list) {
            if(i >= removeSize) {
                break;
            }

            redisOperMap.remove(entry.getKey());
            ++i;
        }
    }
}
