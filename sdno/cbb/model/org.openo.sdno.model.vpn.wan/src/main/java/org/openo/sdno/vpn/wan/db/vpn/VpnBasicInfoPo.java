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
import org.openo.sdno.vpn.wan.servicemodel.common.Scene;
import org.openo.sdno.vpn.wan.servicemodel.vpn.VpnBasicInfo;

@MOResType(infoModelName = "wan_vpnbasicinfo")
public class VpnBasicInfoPo implements PoModel<VpnBasicInfo> {

    private String uuid;

    private String topology;

    private String serviceType;

    private String technology;

    private String adminStatus;

    private String scene;

    @NONInvField
    private List<NvString> addtionalInfo;

    @Override
    public String getUuid() {
        return uuid;
    }

    @Override
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTopology() {
        return topology;
    }

    public void setTopology(String topology) {
        this.topology = topology;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getTechnology() {
        return technology;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }

    public String getAdminStatus() {
        return adminStatus;
    }

    public void setAdminStatus(String adminStatus) {
        this.adminStatus = adminStatus;
    }

    public List<NvString> getAddtionalInfo() {
        return addtionalInfo;
    }

    public void setAddtionalInfo(List<NvString> addtionalInfo) {
        this.addtionalInfo = addtionalInfo;
    }

    public String getScene() {
        return scene;
    }

    public void setScene(final String scene) {
        this.scene = scene;
    }

    @Override
    public VpnBasicInfo toSvcModel() {
        final VpnBasicInfo svcModel = new VpnBasicInfo();
        FieldConvertUtil.setA2B(this, svcModel);
        svcModel.setCn3DciScene(Scene.DCI.getCommonName().equals(scene));
        return svcModel;
    }

    @Override
    public void fromSvcModel(final VpnBasicInfo svcModel) {
        FieldConvertUtil.setA2B(svcModel, this);
        if(svcModel.isCn3DciScene()) {
            scene = Scene.DCI.getCommonName();
        }
    }
}
