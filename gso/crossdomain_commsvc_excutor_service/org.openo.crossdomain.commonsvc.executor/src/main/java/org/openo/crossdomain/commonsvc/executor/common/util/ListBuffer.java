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
package org.openo.crossdomain.commonsvc.executor.common.util;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ListBuffer<T> {

    private final List<T> buffer = new LinkedList<>();

    private long count;

    private final Lock lock = new ReentrantLock();

    private final Condition notEmpty = lock.newCondition();

    public void deposit(T data) throws InterruptedException {
        lock.lock();

        try {
            buffer.add(data);
            count++;

            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    public T fetch() throws InterruptedException {
        lock.lock();

        try {
            while(count == 0) {
                notEmpty.await();
            }

            T result = buffer.remove(0);
            count--;

            return result;
        } finally {
            lock.unlock();
        }
    }

    public long size() {
        lock.lock();

        try {
            return count;
        } finally {
            lock.unlock();
        }
    }
}
