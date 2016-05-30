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
package org.openo.sdno.vpn.wan.db.vpn;

import java.util.List;

import org.openo.sdno.inventory.sdk.model.annotation.MOResType;
import org.openo.sdno.inventory.sdk.model.annotation.NONInvField;
import org.openo.sdno.vpn.wan.db.FieldConvertUtil;
import org.openo.sdno.vpn.wan.db.PoModel;
import org.openo.sdno.vpn.wan.servicemodel.common.NvString;
import org.openo.sdno.vpn.wan.servicemodel.vpn.SegmentVpn;

@MOResType(infoModelName = "wan_segmentvpn")
public class SegmentVpnPo implements PoModel<SegmentVpn> {

    private String uuid;

    private String vpnType;

    private String vpnInfoId;

    private String multiAsVpnId;

    @NONInvField
    private List<NvString> addtionalInfo;

    public String getVpnType() {
        return vpnType;
    }

    public void setVpnType(String vpnType) {
        this.vpnType = vpnType;
    }

    public List<NvString> getAddtionalInfo() {
        return addtionalInfo;
    }

    public void setAddtionalInfo(List<NvString> addtionalInfo) {
        this.addtionalInfo = addtionalInfo;
    }

    @Override
    public String getUuid() {
        return uuid;
    }

    @Override
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getVpnInfoId() {
        return vpnInfoId;
    }

    public void setVpnInfoId(String vpnInfoId) {
        this.vpnInfoId = vpnInfoId;
    }

    public String getMultiAsVpnId() {
        return multiAsVpnId;
    }

    public void setMultiAsVpnId(String multiAsVpnId) {
        this.multiAsVpnId = multiAsVpnId;
    }

    @Override
    public SegmentVpn toSvcModel() {
        final SegmentVpn svcModel = new SegmentVpn();
        FieldConvertUtil.setA2B(this, svcModel);
        return svcModel;
    }

    @Override
    public void fromSvcModel(SegmentVpn svcModel) {
        FieldConvertUtil.setA2B(svcModel, this);
    }
}
