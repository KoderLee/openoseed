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

package org.openo.nfvo.nsservice.simpleservice.register;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.openo.nfvo.nsservice.simpleservice.constant.Constant;

import org.openo.baseservice.i18n.ResourceUtil;
import org.openo.baseservice.log.OssLog;
import org.openo.baseservice.log.OssLogFactory;
import org.openo.nfvo.common.auditlog.AuditLog;
import org.openo.nfvo.common.auditlog.model.LogSeverity;
import org.openo.nfvo.common.auditlog.model.Result;

/**
 * 
* Override the runnable class, used for create a thread that register NFV service to CrossDomain Module<br/>
* <p>
* </p>
* 
* @author
* @version NFVO 0.5 May 15, 2016
 */
public class RegisterService implements Runnable {

    private static final OssLog LOG = OssLogFactory.getLogger(RegisterService.class);

    private int sleepTime = 30000;

    private String regSuccess = "org.openo.nfvo.domed.nfvodo.timetask.register.success";

    private String regFail = "org.openo.nfvo.domed.nfvodo.timetask.register.fail";

    /**
    * Constructor<br/>
    * <p>
    * </p>
    *
    * @since NFVO 0.5
    */
    public RegisterService() {
        // Do nothing
    }

    public RegisterService(int sleepTime) {
        this.sleepTime = sleepTime;
    }

    @Override
    public void run() {
        LOG.error("RegisterService function=run ...");
        AdapterRegisterIrManager registerService = new AdapterRegisterIrManager();
        int result = Constant.REST_FAIL;
        while(true) {
            try {
                result = registerService.regService();
                LOG.warn("RegisterService function=run ...");

                AuditLog.logInstance(Constant.PARAM_MODULE)
                        .addDetail(ResourceUtil.getMessage(result == Constant.REST_SUCCESS ? regSucess : regFail))
                        .addLevel(LogSeverity.MINOR)
                        .addOperation(ResourceUtil.getMessage("org.openo.nfvo.domed.nfvodo.timetask.register"))
                        .addResult(result == Constant.REST_SUCCESS ? Result.SUCCESSFUL : Result.FAILURE)
                        .addTargetObj(Constant.PARAM_MODULE).generateSystemLog();

                if(result == Constant.REST_SUCCESS) {
                    LOG.warn("RegisterService function=stop ...");
                    return;
                }
                TimeUnit.MILLISECONDS.sleep(sleepTime);
            } catch(InterruptedException e) {
                LOG.error("Thread Interrupted", e);
            }
        }
    }

    public void startThread() {
        ExecutorService es = Executors.newSingleThreadExecutor();
        es.submit(new RegisterService());
        es.shutdown();
    }
}
