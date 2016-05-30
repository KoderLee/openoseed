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

package org.openo.nfvo.nsservice.utils;

import org.openo.baseservice.log.OssLog;
import org.openo.baseservice.log.OssLogFactory;
import org.openo.baseservice.roa.util.restclient.RestfulAsyncCallback;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;

/**
 * 
* Override the callback function, used for long time rest callback<br/>
* <p>
* </p>
* 
* @author
* @version NFVO 0.5 May 15, 2016
 */
public class AsyncCallback implements RestfulAsyncCallback {

    private static final OssLog LOG = OssLogFactory.getLog(AsyncCallback.class);

    @Override
    public void callback(RestfulResponse response) {
        LOG.warn("function=callback, msg=status={}, content={}.", response.getStatus(), response.getResponseContent());
    }

    @Override
    public void handleExcepion(Throwable e) {
        LOG.error("function=callback, msg= e is {}.", e);
    }
}
