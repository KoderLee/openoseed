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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openo.sdno.vpn.wan.servicemodel.vpn.MultiAsVpn;

public class MultiVpnPathfindVo {

    private MultiAsVpn multiAsVpn = null;

    private Map<String, List<TpVo>> tpMap = new HashMap<String, List<TpVo>>();

    public final MultiAsVpn getMultiAsVpn() {
        return multiAsVpn;
    }

    public final void setMultiAsVpn(MultiAsVpn multiAsVpn) {
        this.multiAsVpn = multiAsVpn;
    }

    public final Map<String, List<TpVo>> getTpMap() {
        return tpMap;
    }

    public final void setTpMap(Map<String, List<TpVo>> tpMap) {
        this.tpMap = tpMap;
    }

    @Override
    public String toString() {
        return "MultiVpnPathfindVo [multiAsVpn=" + multiAsVpn + ", tpMap=" + tpMap + "]";
    }
}
