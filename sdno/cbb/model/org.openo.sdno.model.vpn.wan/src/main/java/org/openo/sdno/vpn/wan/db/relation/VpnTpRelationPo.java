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
package org.openo.sdno.vpn.wan.db.relation;

import org.openo.sdno.inventory.sdk.model.annotation.MOResType;
import org.openo.sdno.vpn.wan.db.FieldConvertUtil;
import org.openo.sdno.vpn.wan.db.PoModel;
import org.openo.sdno.vpn.wan.servicemodel.relation.VpnTpRelation;

@MOResType(infoModelName = "wan_vpntprelation")
public class VpnTpRelationPo implements PoModel<VpnTpRelation> {

    private String uuid;

    private String vpnBasicInfoId;

    private String tpId;

    /**
     * Get terminate point id.
     * 
     * @since SDNO 0.5
     */
    public String getTpId() {
        return tpId;
    }

    public void setTpId(String tpId) {
        this.tpId = tpId;
    }

    @Override
    public String getUuid() {
        return uuid;
    }

    @Override
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public VpnTpRelation toSvcModel() {
        final VpnTpRelation svcModel = new VpnTpRelation();
        FieldConvertUtil.setA2B(this, svcModel);
        return svcModel;
    }

    @Override
    public void fromSvcModel(VpnTpRelation svcModel) {
        FieldConvertUtil.setA2B(svcModel, this);
    }

    public String getVpnBasicInfoId() {
        return vpnBasicInfoId;
    }

    public void setVpnBasicInfoId(String vpnBasicInfoId) {
        this.vpnBasicInfoId = vpnBasicInfoId;
    }
}
