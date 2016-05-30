/*
 * Copyright (c) 2016, Huawei Technologies Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *  
 */
package org.openo.sdno.vpn.wan.servicemodel.tepath;

import java.util.List;

public class TePath {

    private String pathRole;

    private String pathStatus;

    private List<ServiceTePath> pathList;

    public List<ServiceTePath> getPathList() {
        return pathList;
    }

    public void setPathList(List<ServiceTePath> pathList) {
        this.pathList = pathList;
    }

    public String getPathRole() {
        return pathRole;
    }

    public void setPathRole(String pathRole) {
        this.pathRole = pathRole;
    }

    public String getPathStatus() {
        return pathStatus;
    }

    public void setPathStatus(String pathStatus) {
        this.pathStatus = pathStatus;
    }

}
