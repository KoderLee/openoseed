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
package org.openo.crossdomain.commonsvc.executor.service;

import org.openo.crossdomain.commonsvc.executor.common.LoopRunnable;
import org.openo.crossdomain.commonsvc.executor.common.enums.ExecutionStatus;
import org.openo.crossdomain.commonsvc.executor.common.exception.PreprocessException;
import org.openo.crossdomain.commonsvc.executor.common.util.CommonUtil;
import org.openo.crossdomain.commonsvc.executor.dao.inf.IServiceJobDao;
import org.openo.crossdomain.commonsvc.executor.model.ServiceJob;
import org.openo.crossdomain.commonsvc.executor.model.db.Result;
import org.openo.crossdomain.commonsvc.executor.service.inf.IPreprocessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;

@Component(value = "preparer")
public class Preparer extends LoopRunnable {

    private static final Logger log = LoggerFactory.getLogger(Preparer.class);

    private static Preparer instance;

    private List<IPreprocessor<ServiceJob>> preprocessors = new ArrayList<IPreprocessor<ServiceJob>>();

    @Resource
    private IServiceJobDao srvExectuorJobDao;

    @Resource
    private Receiver receiver;

    private Preparer() {

    }

    /**
     * Get the Preparer Instance<br/>
     * @return Preparer to be processed
     * @version crossdomain 0.5 2016-3-18
     */	
    public static synchronized Preparer getInstance() {
        if(instance == null) {
            instance = new Preparer();
        }
        return instance;
    }

    private void prepareJob() {

        ServiceJob svcjob = receiver.popJob();
        if(svcjob == null) {
            log.debug("pop job failed.");
            return;
        }

        Manager.getInstance().put(svcjob);

        try {
            for(IPreprocessor<ServiceJob> preprocessor : preprocessors) {
                preprocessor.preprocess(svcjob);
            }
        } catch(PreprocessException e) {
            String msg = String.format("prepareJob fails: %s", e.getMessage());
            log.error(msg);
            Result rst = new Result(Result.FAIL, msg, svcjob.getService().getServiceName());

            updateSeviceJob(rst, svcjob);
            return;
        }

        updateSeviceJob(null, svcjob);

        Dispatcher.getInstance().executeJob(svcjob);
    }

    @Override
    protected void loopRun() {
        prepareJob();
    }

    public List<IPreprocessor<ServiceJob>> getPreprocessors() {
        return preprocessors;
    }

    public void setPreprocessors(List<IPreprocessor<ServiceJob>> preprocessors) {
        this.preprocessors = preprocessors;
    }

    private void updateSeviceJob(Result result, ServiceJob svcJob) {
        if(result != null && !result.isSuccess()) {
            svcJob.setStatus(ExecutionStatus.COMPLETED);
        } else {
            svcJob.setStatus(ExecutionStatus.EXECUTING);
        }

        svcJob.setResult(result);

        if(svcJob.getStatus() == ExecutionStatus.COMPLETED) {
            svcJob.setCompletedTime(CommonUtil.getTimeInMillis());
        }

        Manager.getInstance().put(svcJob);

        srvExectuorJobDao.update(svcJob);
    }
}
