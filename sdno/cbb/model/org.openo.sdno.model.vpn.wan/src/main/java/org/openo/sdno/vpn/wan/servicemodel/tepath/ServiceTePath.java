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

public class ServiceTePath {

    private String neId;

    private String neName;

    private String ingressTpId;

    private String egressTpId;

    private String ingressTpName;

    private String egressTpName;

    public String getNeId() {
        return neId;
    }

    public void setNeId(String neId) {
        this.neId = neId;
    }

    public String getNeName() {
        return neName;
    }

    public void setNeName(String neName) {
        this.neName = neName;
    }

    public String getIngressTpName() {
        return ingressTpName;
    }

    public void setIngressTpName(String ingressTpName) {
        this.ingressTpName = ingressTpName;
    }

    public String getEgressTpName() {
        return egressTpName;
    }

    public void setEgressTpName(String egressTpName) {
        this.egressTpName = egressTpName;
    }

    public String getIngressTpId() {
        return ingressTpId;
    }

    public void setIngressTpId(String ingressTpId) {
        this.ingressTpId = ingressTpId;
    }

    public String getEgressTpId() {
        return egressTpId;
    }

    public void setEgressTpId(String egressTpId) {
        this.egressTpId = egressTpId;
    }

}
