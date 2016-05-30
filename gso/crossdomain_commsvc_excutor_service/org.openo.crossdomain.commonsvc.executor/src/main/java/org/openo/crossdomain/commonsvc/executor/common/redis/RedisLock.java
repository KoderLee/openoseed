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
import org.openo.crossdomain.commonsvc.executor.common.util.CommonUtil;
import org.openo.crossdomain.commonsvc.executor.common.util.ExecutorContextHelper;
import org.openo.crossdomain.commonsvc.executor.common.util.SchedulerProxy;
import org.openo.crossdomain.commonsvc.executor.common.util.ThreadUtil;

import org.openo.commonservice.log.OssLog;
import org.openo.commonservice.log.OssLogFactory;

public class RedisLock {

    private static final OssLog log = OssLogFactory.getLogger(RedisQueue.class);

    private static final int TIMEOUT = 30000;

    private static final int WAITTIME = 10;

    private RedisSingleVariable<LockData> redisStore = new RedisSingleVariable<>();

    private String name;

    private String instanceId;

    public RedisLock(String name) {
        this.name = name;
    }

    public void lock() {
        boolean ownLock = false;
        while(!ownLock) {
            LockData data = new LockData();
            data.setExpireAt(CommonUtil.getTimeInMillis() + TIMEOUT);
            data.setInstanceId(getInstanceId());
            ownLock = redisStore.setValueIfNotExist(name, data);

            if(!ownLock && tryLock()) {
                ownLock = true;
            }

            if(!ownLock) {
                ThreadUtil.sleep(WAITTIME);
            }
        }
    }

    private boolean tryLock() {
        boolean ownLock = false;
        LockData data = redisStore.getValue(LockData.class, name);
        if(data != null) {
            if(CommonUtil.getTimeInMillis() < data.getExpireAt()) {
                return ownLock;
            }

            LockData newData = new LockData();
            newData.setExpireAt(CommonUtil.getTimeInMillis() + TIMEOUT);
            newData.setInstanceId(getInstanceId());

            LockData oldData = redisStore.getSet(LockData.class, name, newData);
            if(oldData != null) {
                if(CommonUtil.getTimeInMillis() > oldData.getExpireAt()) {
                    ownLock = true;
                }
            } else {
                log.info("redisStore.getSet return null");
            }
        } else {
            log.info("redisStore.getValue return null");
        }

        return ownLock;
    }

    public void unlock() {
        LockData data = redisStore.getValue(LockData.class, name);
        if(data != null && data.getInstanceId().equals(this.getInstanceId())
                && CommonUtil.getTimeInMillis() < data.getExpireAt()) {
            redisStore.deleteVariable(name);
        }
    }

    private String getInstanceId() {
        if(instanceId == null) {
            SchedulerProxy schedulerProxy =
                    (SchedulerProxy)ExecutorContextHelper.getInstance().getBean(Constants.SpringDefine.SCHEDULERPROXY);
            instanceId = schedulerProxy.getInstanceId();
        }
        return instanceId;
    }

    static private class LockData {

        String instanceId;

        Long expireAt;

        public Long getExpireAt() {
            return expireAt;
        }

        public void setExpireAt(Long expireAt) {
            this.expireAt = expireAt;
        }

        public String getInstanceId() {
            return instanceId;
        }

        public void setInstanceId(String instanceId) {
            this.instanceId = instanceId;
        }
    }
}
