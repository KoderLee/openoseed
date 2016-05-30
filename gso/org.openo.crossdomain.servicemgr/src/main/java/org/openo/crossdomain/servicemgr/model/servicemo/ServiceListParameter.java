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
package org.openo.crossdomain.servicemgr.model.servicemo;

/**
 * Definition of service parameter which type is list.<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
public class ServiceListParameter {

    private String paramgroup_name;

    private String service_id;

    private String key_name;

    private String parameter_value;

    private String action;

    /**
     * @return Returns the key_name.
     */
    public String getKey_name() {
        return key_name;
    }

    /**
     * @param key_name The key_name to set.
     */
    public void setKey_name(String key_name) {
        this.key_name = key_name;
    }

    /**
     * @return Returns the paramgroup_name.
     */
    public String getParamgroup_name() {
        return paramgroup_name;
    }

    /**
     * @param paramgroup_name The paramgroup_name to set.
     */
    public void setParamgroup_name(String paramgroup_name) {
        this.paramgroup_name = paramgroup_name;
    }

    /**
     * @return Returns the service_id.
     */
    public String getService_id() {
        return service_id;
    }

    /**
     * @param service_id The service_id to set.
     */
    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    /**
     * @return Returns the parameter_value.
     */
    public String getParameter_value() {
        return parameter_value;
    }

    /**
     * @param parameter_value The parameter_value to set.
     */
    public void setParameter_value(String parameter_value) {
        this.parameter_value = parameter_value;
    }

    /**
     * @return Returns the action.
     */
    public String getAction() {
        return action;
    }

    /**
     * @param action The action to set.
     */
    public void setAction(String action) {
        this.action = action;
    }

    public static class ACTION_TYPE_NUM {

        public static final String ACTION_TYPE_CREATE = "create";

        public static final String ACTION_TYPE_UPDATE = "update";

        public static final String ACTION_TYPE_DELETE = "delete";
    }

}
