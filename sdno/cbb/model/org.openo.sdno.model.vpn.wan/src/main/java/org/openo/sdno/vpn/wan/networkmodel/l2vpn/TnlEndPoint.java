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
package org.openo.sdno.vpn.wan.networkmodel.l2vpn;

public class TnlEndPoint {

    private String id;

    private String ipAddress;

    private String adminIp;

    private String host;

    private String type;

    private int vniMappingType;

    private int userLearningType;

    private TnlConnections tnlConnections;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getAdminIp() {
        return adminIp;
    }

    public void setAdminIp(String adminIp) {
        this.adminIp = adminIp;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getVniMappingType() {
        return vniMappingType;
    }

    public void setVniMappingType(int vniMappingType) {
        this.vniMappingType = vniMappingType;
    }

    public int getUserLearningType() {
        return userLearningType;
    }

    public void setUserLearningType(int userLearningType) {
        this.userLearningType = userLearningType;
    }

    public TnlConnections getTnlConnections() {
        return tnlConnections;
    }

    public void setTnlConnections(TnlConnections tnlConnections) {
        this.tnlConnections = tnlConnections;
    }
}
