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
package org.openo.crossdomain.commonsvc.executor.model.db;

import org.openo.crossdomain.commonsvc.executor.common.check.CheckAttr;
import org.openo.crossdomain.commonsvc.executor.common.check.CheckType;
import org.openo.crossdomain.commonsvc.executor.common.constant.Constants;
import org.openo.crossdomain.commonsvc.executor.common.enums.ActionType;
import org.openo.crossdomain.commonsvc.executor.common.enums.ExecutionStatus;

public class ResourceCommon extends Result

{

    protected static final int KEY_LEN_MAX = 64;

    @CheckAttr(type = CheckType.String, required = true, min = 1, max = KEY_LEN_MAX)
    protected String key;

    protected String resContent;

    protected ActionType operType;

    protected ExecutionStatus status = ExecutionStatus.INITEXECUTE;

    @CheckAttr(type = CheckType.String, required = true, min = 1, max = Constants.TYPELEN_MAX)
    protected String type;

    protected String queryUrl;

    public ResourceCommon() {
        super(null, null, null);
    }

    /**
     * @return Returns the key.
     */
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    /**
     * @return Returns the resContent.
     */
    public String getResContent() {
        return resContent;
    }

    /**
     * @param resContent The resContent to set.
     */
    public void setResContent(String resContent) {
        this.resContent = resContent;
    }

    /**
     * @return Returns the operType.
     */
    public ActionType getOperType() {
        return operType;
    }

    /**
     * @param operType The operType to set.
     */
    public void setOperType(ActionType operType) {
        this.operType = operType;
    }

    /**
     * @return Returns the status.
     */
    public ExecutionStatus getStatus() {
        return status;
    }

    /**
     * @param status The status to set.
     */
    public void setStatus(ExecutionStatus status) {
        this.status = status;
    }

    /**
     * @return Returns the type.
     */
    public String getType() {
        return type;
    }

    /**
     * @param type The type to set.
     */
    public void setType(String type) {
        this.type = type;
    }

    public String getQueryUrl() {
        return queryUrl;
    }

    public void setQueryUrl(String queryUrl) {
        this.queryUrl = queryUrl;
    }
}
