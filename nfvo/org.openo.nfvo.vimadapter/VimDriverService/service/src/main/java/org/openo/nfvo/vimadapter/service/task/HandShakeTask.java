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

package org.openo.nfvo.vimadapter.service.task;

import org.openo.nfvo.vimadapter.util.BaseTimeJob;
import org.openo.nfvo.vimadapter.service.adapter.AdapterVimManager;

import org.openo.baseservice.log.OssLog;
import org.openo.baseservice.log.OssLogFactory;

/**
 * 
* Used for 3rd system establish connection by corn job<br/>
* <p>
* </p>
* 
* @author
* @version NFVO 0.5 May 15, 2016
 */
public class HandShakeTask extends BaseTimeJob {

    private final static OssLog LOG = OssLogFactory
            .getLogger(HandShakeTask.class);

    private static final int PERIOD = 10;

    private static final int ONE_SECOND = 1;

    private AdapterVimManager adapter;

    private boolean flag = true;

    public void setAdapter(AdapterVimManager adapter) {
        this.adapter = adapter;
    }

    public HandShakeTask() {
        super();
        setPeriod(PERIOD * ONE_SECOND);
        setInitialDelay(ONE_SECOND);
    }

    @Override
    public void run() {
        try {
            LOG.debug("function=handShake, msg=Vim handShake, flag=" + flag);
            adapter.checkVimStatus(flag);
            flag = false;

            return;
        } catch (Exception e) {
            LOG.error("function=handShake, msg=exception info is " + e);
        }
    }

    public void init() {
        start();

    }

    public void fini() {
        stop();
    }
}
