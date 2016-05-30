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

import java.util.ArrayList;
import java.util.List;

/**
 * 
* Supply the operation result entity for other service use<br/>
* <p>
* </p>
* 
* @author
* @version NFVO 0.5 May 15, 2016
 */
public class DoOpResult {

    public enum TaskStatus {
        INIT, SUCCESS, FAIL, PART_SUCCESS, RUNNING, TIMEOUT
    }

    private String errorMessage;

    private TaskStatus operateStatus;

    private List<Object> results = new ArrayList<Object>(10);

    private int errorCode;

    private String taskId = null;

    public DoOpResult() {
        operateStatus = TaskStatus.INIT;
        errorMessage = "";
        errorCode = 0;
    }

    public DoOpResult(TaskStatus operateStatus, String errorMessage) {
        this.operateStatus = operateStatus;
        this.errorMessage = errorMessage;
        this.errorCode = 0;
    }

    public void setOperateStatus(TaskStatus operateStatus) {
        this.operateStatus = operateStatus;
    }

    public TaskStatus getOperateStatus() {
        return operateStatus;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public List<Object> getResult() {
        return results;
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

    @SuppressWarnings("unchecked")
    public void addResult(Object result) {
        if(result instanceof List<?>) {
            this.results.addAll((List<Object>)result);
        } else {
            this.results.add(result);
        }
    }
    
    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public boolean isTaskSuccess() {
        return operateStatus.equals(TaskStatus.SUCCESS);
    }

    public boolean isTimeout() {
        return operateStatus.equals(TaskStatus.TIMEOUT);
    }

    public boolean isTaskFailed() {
        return operateStatus.equals(TaskStatus.FAIL);
    }

    public boolean isFinished() {
        return (operateStatus != TaskStatus.SUCCESS) && (operateStatus != TaskStatus.INIT);
    }
}
