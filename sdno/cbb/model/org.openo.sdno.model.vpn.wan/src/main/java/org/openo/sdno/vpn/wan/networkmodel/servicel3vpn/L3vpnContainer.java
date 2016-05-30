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
package org.openo.sdno.vpn.wan.networkmodel.servicel3vpn;

import java.util.HashMap;
import java.util.Map;

import org.openo.sdno.vpn.wan.networkmodel.NetModel;

public class L3vpnContainer implements NetModel {

    private L3vpn l3vpn;

    private Map<String, Object> paramMap = new HashMap<String, Object>();

    public L3vpn getL3vpn() {
        return l3vpn;
    }

    public void setL3vpn(final L3vpn l3vpn) {
        this.l3vpn = l3vpn;
    }

    public Map<String, Object> getParamMap() {
        return paramMap;
    }

    public void setParamMap(final Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }
}
