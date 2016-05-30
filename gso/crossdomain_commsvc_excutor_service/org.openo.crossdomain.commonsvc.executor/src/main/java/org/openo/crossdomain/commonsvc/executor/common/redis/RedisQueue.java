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

import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.openo.crossdomain.commonsvc.executor.common.constant.Constants;

import org.openo.commonservice.log.OssLog;
import org.openo.commonservice.log.OssLogFactory;

public class RedisQueue<T> {

    private static final OssLog log = OssLogFactory.getLogger(RedisQueue.class);

    private final ReentrantLock lock;

    private final Condition notEmpty;

    private final Condition notFull;

    private QueueOper<T> queueOper = new QueueOper<T>(Constants.BUNDLE_NAME, Constants.RDB_NAME);

    private String name;

    private int length;

    public RedisQueue(String name, int length) {
        this.name = name;
        this.length = length;
        lock = new ReentrantLock(true);
        notEmpty = lock.newCondition();
        notFull = lock.newCondition();
    }

    public void push(T value) {
        push(value, false);
    }

    public void push(T value, boolean force) {
        log.debug("RedisQueue push start");

        try {
            lock.lockInterruptibly();

            if(!force) {

                while(queueOper.length(name) >= length) {
                    notFull.await();
                }
            }

            queueOper.push(name, value);

            notEmpty.signal();
        } catch(InterruptedException e) {
            log.error("InterruptedException", e);
        } finally {
            log.debug("RedisQueue push end");

            lock.unlock();
        }
    }

    public T pop(Class<T> type) {
        log.debug("RedisQueue pop start");

        try {

            lock.lockInterruptibly();

            while(queueOper.length(name) <= 0) {
                notEmpty.await();
            }

            T obj = queueOper.pop(name, type);

            notFull.signal();
            return obj;
        } catch(InterruptedException e) {
            log.error("InterruptedException", e);
            return null;
        } finally {
            log.debug("RedisQueue pop end");

            lock.unlock();
        }

    }

    public List<T> get(Class<T> type) {
        List<T> list = null;
        try {
            lock.lockInterruptibly();
            list = queueOper.getAll(name, type);
        } catch(InterruptedException e) {
            log.error("InterruptedException", e);
        } finally {

            lock.unlock();
        }

        return list;
    }

    public void clear() {
        try {

            lock.lockInterruptibly();
            queueOper.clear(name);
        } catch(InterruptedException e) {
            log.error("InterruptedException", e);
        } finally {
            lock.unlock();
        }
    }
}
