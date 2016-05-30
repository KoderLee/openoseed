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
package org.openo.sdno.vpn.wan.servicemodel.composedvpn;

public class TePathQueryVo {

    private String vpnId;

    private String vpnType;

    private String srcNeId;

    private String destNeId;

    private String srcAcId;

    private String destAcId;

    public TePathQueryVo() {
        // Constructed function.
    }

    public TePathQueryVo(String vpnId, String vpnType, String srcNeId, String destNeId, String srcAcId, String destAcId) {
        this.vpnId = vpnId;
        this.vpnType = vpnType;
        this.srcNeId = srcNeId;
        this.destNeId = destNeId;
        this.srcAcId = srcAcId;
        this.destAcId = destAcId;
    }

    public final String getVpnId() {
        return vpnId;
    }

    public final void setVpnId(String vpnId) {
        this.vpnId = vpnId;
    }

    public final String getVpnType() {
        return vpnType;
    }

    public final void setVpnType(String vpnType) {
        this.vpnType = vpnType;
    }

    public final String getSrcNeId() {
        return srcNeId;
    }

    public final void setSrcNeId(String srcNeId) {
        this.srcNeId = srcNeId;
    }

    public final String getDestNeId() {
        return destNeId;
    }

    public final void setDestNeId(String destNeId) {
        this.destNeId = destNeId;
    }

    public final String getSrcAcId() {
        return srcAcId;
    }

    public final void setSrcAcId(String srcAcId) {
        this.srcAcId = srcAcId;
    }

    public final String getDestAcId() {
        return destAcId;
    }

    public final void setDestAcId(String destAcId) {
        this.destAcId = destAcId;
    }

    @Override
    public String toString() {
        return "TePathQueryVo [vpnId=" + vpnId + ", vpnType=" + vpnType + ", srcNeId=" + srcNeId + ", destNeId="
                + destNeId + ", srcAcId=" + srcAcId + ", destAcId=" + destAcId + "]";
    }

    public String getQueryUrlPath() {

        return "?" + "vpnId=" + vpnId + "&srcNeId=" + srcNeId + "&destNeId=" + destNeId + "&srcAcId=" + srcAcId
                + "&destAcId=" + destAcId;
    }
}
