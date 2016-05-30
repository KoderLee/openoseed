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
package org.openo.crossdomain.commonsvc.executor.common.enums;

import org.openo.commonservice.log.OssLog;
import org.openo.commonservice.log.OssLogFactory;

import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import org.openo.crossdomain.commonsvc.executor.common.constant.RequestJsonConstants;
import org.openo.crossdomain.commonsvc.executor.model.util.Json2ModelUtil;

public enum ActionType {
    CREATE("create"), MODIFY("modify"), DELETE("delete"), ACTIVE("activate"), DEACTIVE("deactivate"), UPDATE("update"), DRYRUN_CREATE(
            "dryrun_create"), DRYRUN_MODIFY("dryun_modify"), DRYRUN_DEL("dryrun_del");

    private static OssLog log = OssLogFactory.getLogger(ActionType.class);

    private String value;

    ActionType(String value) {
        this.value = value;
    }

    public static ActionType enumValueOf(String value) {
        Map<String, ActionType> actionTypeMap = new HashMap<>();
        actionTypeMap.put(ActionType.CREATE.toString(), ActionType.CREATE);
        actionTypeMap.put(ActionType.MODIFY.toString(), ActionType.MODIFY);
        actionTypeMap.put(ActionType.DELETE.toString(), ActionType.DELETE);
        actionTypeMap.put(ActionType.ACTIVE.toString(), ActionType.ACTIVE);
        actionTypeMap.put(ActionType.DEACTIVE.toString(), ActionType.DEACTIVE);
        actionTypeMap.put(ActionType.UPDATE.toString(), ActionType.UPDATE);
        actionTypeMap.put(ActionType.DRYRUN_CREATE.toString(), ActionType.DRYRUN_CREATE);
        actionTypeMap.put(ActionType.DRYRUN_MODIFY.toString(), ActionType.DRYRUN_MODIFY);
        actionTypeMap.put(ActionType.DRYRUN_DEL.toString(), ActionType.DRYRUN_DEL);

        ActionType actionType = actionTypeMap.get(value);

        if(actionType == null) {
            log.error("action Type invalid: {}", value);
        }

        return actionType;
    }

    public static ActionType toAction(JSONObject jsonObject) {
        return ActionType.enumValueOf(Json2ModelUtil.getString(jsonObject, RequestJsonConstants.action).toLowerCase());
    }

    @Override
    public String toString() {
        return value;
    }

}
