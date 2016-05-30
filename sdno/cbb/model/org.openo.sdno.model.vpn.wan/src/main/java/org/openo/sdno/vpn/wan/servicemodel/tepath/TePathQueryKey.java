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

import java.util.HashMap;
import java.util.Map;

public class TePathQueryKey {

    private String vpnId;

    private String srcNeId;

    private String destNeId;

    private String srcAcId;

    private String destAcId;

    public TePathQueryKey(String vpnId, String srcNeId, String destNeId, String srcAcId, String destAcId) {
        this.vpnId = vpnId;
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

    public Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("vpnId", vpnId);
        params.put("srcNeId", srcNeId);
        params.put("destNeId", destNeId);
        params.put("srcAcId", srcAcId);
        params.put("destAcId", destAcId);
        return params;
    }
}
