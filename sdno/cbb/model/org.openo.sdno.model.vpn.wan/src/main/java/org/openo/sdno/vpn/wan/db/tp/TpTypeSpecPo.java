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
package org.openo.sdno.vpn.wan.db.tp;

import org.openo.sdno.inventory.sdk.model.annotation.MOResType;
import org.openo.sdno.vpn.wan.db.FieldConvertUtil;
import org.openo.sdno.vpn.wan.db.PoModel;
import org.openo.sdno.vpn.wan.servicemodel.tp.TpTypeSpec;

@MOResType(infoModelName = "wan_tptypespec")
public class TpTypeSpecPo implements PoModel<TpTypeSpec> {

    private String uuid;

    private String layerRate;

    private String addressPoolName;

    private String ethernetTpSpecId;

    private String ipTpSpecId;

    private String vxlanTpSpecId;

    private String tpId;

    @Override
    public String getUuid() {
        return uuid;
    }

    @Override
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getEthernetTpSpecId() {
        return ethernetTpSpecId;
    }

    public void setEthernetTpSpecId(String ethernetTpSpecId) {
        this.ethernetTpSpecId = ethernetTpSpecId;
    }

    public String getIpTpSpecId() {
        return ipTpSpecId;
    }

    public void setIpTpSpecId(String ipTpSpecId) {
        this.ipTpSpecId = ipTpSpecId;
    }

    public String getVxlanTpSpecId() {
        return vxlanTpSpecId;
    }

    public void setVxlanTpSpecId(String vxlanTpSpecId) {
        this.vxlanTpSpecId = vxlanTpSpecId;
    }

    public String getTpId() {
        return tpId;
    }

    public void setTpId(String tpId) {
        this.tpId = tpId;
    }

    @Override
    public TpTypeSpec toSvcModel() {
        final TpTypeSpec svcModel = new TpTypeSpec();
        FieldConvertUtil.setA2B(this, svcModel);
        return svcModel;
    }

    @Override
    public void fromSvcModel(TpTypeSpec svcModel) {
        FieldConvertUtil.setA2B(svcModel, this);
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
