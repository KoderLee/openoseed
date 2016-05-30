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
package org.openo.sdno.vpn.wan.servicemodel.tp;

import org.openo.sdno.vpn.wan.paradesc.EnumDesc;
import org.openo.sdno.vpn.wan.paradesc.StringDesc;
import org.openo.sdno.vpn.wan.servicemodel.AbstractSvcModel;
import org.openo.sdno.vpn.wan.servicemodel.common.LayerRate;

public class TpTypeSpec extends AbstractSvcModel {

    @StringDesc(maxLen = 36)
    private String uuid;

    @EnumDesc(LayerRate.class)
    private String layerRate;

    @StringDesc(maxLen = 200)
    private String addressPoolName;

    private EthernetTpSpec ethernetTpSpec;

    private IpTpSpec ipTpSpec;

    public EthernetTpSpec getEthernetTpSpec() {
        return ethernetTpSpec;
    }

    public void setEthernetTpSpec(EthernetTpSpec ethernetTpSpec) {
        this.ethernetTpSpec = ethernetTpSpec;
    }

    public IpTpSpec getIpTpSpec() {
        return ipTpSpec;
    }

    public void setIpTpSpec(IpTpSpec ipTpSpec) {
        this.ipTpSpec = ipTpSpec;
    }

    @Override
    public String getUuid() {
        return uuid;
    }

    @Override
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getLayerRate() {
        return layerRate;
    }

    public void setLayerRate(String layerRate) {
        this.layerRate = layerRate;
    }

    public String getAddressPoolName() {
        return addressPoolName;
    }

    public void setAddressPoolName(String addressPoolName) {
        this.addressPoolName = addressPoolName;
    }

}
