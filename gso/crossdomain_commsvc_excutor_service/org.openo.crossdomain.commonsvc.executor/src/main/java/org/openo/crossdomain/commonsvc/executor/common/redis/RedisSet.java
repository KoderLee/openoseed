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
import org.openo.commonservice.redis.oper.AbstractOper;

import java.util.Set;

import org.openo.crossdomain.commonsvc.executor.common.constant.Constants;

public class RedisSet<T> extends AbstractOper<T> {

    private static final String SEPARATOR = ":";

    private static final OssLog log = OssLogFactory.getLogger(RedisSingleVariable.class);

    private String name;

    public RedisSet(String name) {
        super(Constants.BUNDLE_NAME, Constants.RDB_NAME);
        this.name = name;
    }

    public RedisSet(String name, String instanceId) {
        super((instanceId + SEPARATOR + Constants.BUNDLE_NAME), Constants.RDB_NAME);
        this.name = name;
    }

    public boolean addMember(String value) {
        String storeKey = getStoreKey(name);
        Long addNum = this.redis.sadd(storeKey, value);
        return addNum > Constants.NULL_ID;
    }

    public void deleteMember(String member) {
        String storeKey = getStoreKey(name);
        this.redis.srem(storeKey, new String[] {member});
    }

    public Set<String> getMember() {
        String storeKey = getStoreKey(name);
        return this.redis.smembers(storeKey);
    }

    public void clear() {
        clear(name);
    }

    public boolean exists()

    {
        return exists(name);
    }

}
