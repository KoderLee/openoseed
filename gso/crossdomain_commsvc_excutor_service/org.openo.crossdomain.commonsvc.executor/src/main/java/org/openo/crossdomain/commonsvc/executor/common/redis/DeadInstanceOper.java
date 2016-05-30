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
import org.openo.crossdomain.commonsvc.executor.common.util.ExecutorContextHelper;
import org.openo.crossdomain.commonsvc.executor.common.util.SchedulerProxy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component(value = "deadInstanceOper")
public class DeadInstanceOper {

    private static final String LOCK_NAME = "DeadInstanceOperLock";

    private RedisMap<String> redisMap = new RedisMap<>(Constants.DEAD_INSTANCE_RD, Constants.NULL_STR);

    private String instanceId;

	/**
	 *Delete Dead Instance
	 *@param deadInstance key of deadInstance
	 *@since crossdomain 0.5 2016-3-18
	 */
    public void deleteDeadInstance(String deadInstance) {
        redisMap.remove(deadInstance);
    }

	/**
	 *Adopt Dead Instance
	 *@param deadInstance key of deadInstance
	 *@since crossdomain 0.5 2016-3-18
	 */
    public boolean adoptInstance(String deadInstance) {
        removeUnused();

        RedisLock lock = new RedisLock(LOCK_NAME);
        try {
            lock.lock();
            if(redisMap.get(String.class, deadInstance) != null) {
                return false;
            }

            redisMap.put(deadInstance, getInstanceId());

            return true;
        } finally {
            lock.unlock();
        }
    }

	/**
	 *get Adopted Dead Instance
	 *@param deadInstance key of deadInstance
	 *@since crossdomain 0.5 2016-3-18
	 */
    public String getAdoptingInstance(String deadInstance) {
        return redisMap.get(String.class, deadInstance);
    }

    private void removeUnused() {
        Map<String, String> deadInstanceMap = redisMap.getAll(String.class);

        List<String> delList = new ArrayList<>();
        for(Map.Entry<String, String> entry : deadInstanceMap.entrySet()) {

            String liveInstance = deadInstanceMap.get(entry.getValue());
            if(liveInstance != null) {
                delList.add(entry.getKey());
            }

            if(entry.getValue().equals(getInstanceId())) {
                delList.add(entry.getKey());
            }
        }

        for(String deadInstant : delList) {
            redisMap.remove(deadInstant);
        }
    }

    private String getInstanceId() {
        if(instanceId == null) {
            SchedulerProxy proxy =
                    (SchedulerProxy)ExecutorContextHelper.getInstance().getBean(Constants.SpringDefine.SCHEDULERPROXY);
            instanceId = proxy.getInstanceId();
        }
        return instanceId;
    }
}
