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

package org.openo.nfvo.vimadapter.util;

import java.util.ArrayList;
import java.util.List;

import org.openo.nfvo.vimadapter.util.constant.Constant;

/**
 * 
* Format the communication result between NFVO and VIM. <br/>
* <p>
* </p>
* 
* @author
* @version NFVO 0.5 May 15, 2016
 */
public class VimOpResult {
    private String errorMessage;

    public enum TaskStatus {
        INIT, SUCCESS, FAIL, PART_SUCCESS, RUNNING, TIMEOUT
    }

    private TaskStatus operateStatus;
    private List<Object> results = new ArrayList<Object>(
            Constant.DEFAULT_COLLECTION_SIZE);

    private int errorCode;

    public VimOpResult() {
        operateStatus = TaskStatus.INIT;
        errorMessage = "";
        errorCode = 0;
    }

    public VimOpResult(TaskStatus operateStatus, String errorMessage) {
        this.operateStatus = operateStatus;
        this.errorMessage = errorMessage;
        errorCode = 0;
    }

    public void setOperateStatus(TaskStatus operateStatus) {
        this.operateStatus = operateStatus;
    }

    public TaskStatus gotOperateStatus() {
        return operateStatus;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String gotErrorMessage() {
        return errorMessage;
    }

    public List<Object> gotResult() {
        return results;
    }

    @SuppressWarnings("unchecked")
    public void addResult(Object result) {
        if (result instanceof List<?>) {
            this.results.addAll((List<Object>) result);
        } else {
            this.results.add(result);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(50);
        sb.append(getClass().getName()).append("@[");
        sb.append("operateStatus=").append(operateStatus).append(", ");
        sb.append("errorCode=").append(errorCode).append(", ");
        sb.append("errorMessage=").append(errorMessage);

        return sb.toString();
    }

    public boolean isTaskSuccess() {
        return TaskStatus.SUCCESS.equals(operateStatus);
    }

    public boolean isTimeout() {
        return TaskStatus.TIMEOUT.equals(operateStatus);
    }

    public boolean isTaskFailed() {
        return TaskStatus.FAIL.equals(operateStatus);
    }

    public boolean isFinished() {
        return (!TaskStatus.SUCCESS.equals(operateStatus))
                && (TaskStatus.INIT.equals(operateStatus));
    }
}
