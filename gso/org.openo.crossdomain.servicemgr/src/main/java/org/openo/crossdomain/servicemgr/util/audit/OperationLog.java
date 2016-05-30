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
package org.openo.crossdomain.servicemgr.util.audit;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.openo.crossdomain.servicemgr.model.servicemo.ServiceModel;
import org.openo.crossdomain.servicemgr.util.json.JsonUtil;
import org.openo.crossdomain.servicemgr.util.system.SysEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.openo.baseservice.biz.trail.AuditItem;
import org.openo.baseservice.biz.trail.AuditItem.AuditResult;
import org.openo.baseservice.biz.trail.AuditItem.LogEventType;
import org.openo.baseservice.biz.trail.AuditItem.LogSeverity;
import org.openo.baseservice.biz.trail.AuditLog;
import org.openo.baseservice.i18n.ResourceUtil;

/**
 * Operation log.<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
public class OperationLog {

    private final static Logger logger = LoggerFactory.getLogger(OperationLog.class);

    private final static String SERVICEID = "service_id";

    private final static String SERVICENAME = "name";

    /**
     * Write log.<br/>
     *
     * @param operType the type of operation
     * @param operObjectId the object ID of operation
     * @param operResult the result of operation
     * @param serviceModel service data
     * @param loglevel the level of log
     * @since crossdomain 0.5
     */
    public static final void writeLogString(String operType, String operObjectId, AuditResult operResult,
            ServiceModel serviceModel, LogSeverity loglevel) {
        String userId = serviceModel.getAuditUserID();
        String userName = serviceModel.getAuditUserName();

        Locale locale = SysEnvironment.getLocale();        

        Map<String, String> operObjMap = new HashMap<String, String>();
        operObjMap.put(SERVICEID, operObjectId);
        operObjMap.put(SERVICENAME, serviceModel.getName());

        AuditItem item = new AuditItem();
        item.setUser(userId, userName, null, null);
        item.level = loglevel;
        
        String operName = ResourceUtil.getMessage(operType, locale);
        item.operation = operName;
        item.terminal = serviceModel.getAuditTerminal();
        item.eventType = LogEventType.OPERATION.toString();
        item.result = operResult;
        try {
            item.targetObj = JsonUtil.marshal(operObjMap);

            item.detail = JsonUtil.marshal(serviceModel);
        } catch(IOException e) {
            logger.info("OperationLog >> writeLogString >> OperationDetail can't parse to json");
        }

        AuditLog.record(item);
    }

    /**
     * Definition for the type of operation.<br/>
     * 
     * @author
     * @version crossdomain 0.5 2016-3-19
     */
    public static class OPER_TYPE {

        public static final String CREATE_TASK_OPERATION = "servicemgr.audit.create_task_operation";

        public static final String UPDATE_TASK_OPERATION = "servicemgr.audit.update_task_operation";

        public static final String DELETE_TASK_OPERATION = "servicemgr.audit.delete_task_operation";

        public static final String ACTIVE_TASK_OPERATION = "servicemgr.audit.active_task_operation";

        public static final String DEACTIVE_TASK_OPERATION = "servicemgr.audit.deactive_task_operation";

    }

    /**
     * Definition for the detail of operation.<br/>
     * 
     * @author
     * @version crossdomain 0.5 2016-3-19
     */
    public static class OPER_DETAIL {

        public static final String OPER_FAIL = "servicemgr.audit.oper_fail";

        public static final String CREATE_TASK_OPERATION_DETAIL = "servicemgr.audit.create_task_operation.detail";

        public static final String UPDATE_TASK_OPERATION_DETAIL = "servicemgr.audit.update_task_operation.detail";

        public static final String DELETE_TASK_OPERATION_DETAIL = "servicemgr.audit.delete_task_operation.detail";

        public static final String ACTIVE_TASK_OPERATION_DETAIL = "servicemgr.audit.active_task_operation.detail";

        public static final String DEACTIVE_TASK_OPERATION_DETAIL = "servicemgr.audit.deactive_task_operation.detail";

    }
}
