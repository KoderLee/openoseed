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

public class Nve {

    private String nveId;

    private String ipAddress;

    private String adminIp;

    private NveBinding nveBinding;

    private NveAttchs nveAttchs;

    public Nve() {
        nveBinding = new NveBinding();
        nveAttchs = new NveAttchs();
    }

    public String getNveId() {
        return nveId;
    }

    public void setNveId(String nveId) {
        this.nveId = nveId;
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

    public NveBinding getNveBinding() {
        return nveBinding;
    }

    public void setNveBinding(NveBinding nveBinding) {
        this.nveBinding = nveBinding;
    }

    public NveAttchs getNveAttchs() {
        return nveAttchs;
    }

    public void setNveAttchs(NveAttchs nveAttchs) {
        this.nveAttchs = nveAttchs;
    }
}
