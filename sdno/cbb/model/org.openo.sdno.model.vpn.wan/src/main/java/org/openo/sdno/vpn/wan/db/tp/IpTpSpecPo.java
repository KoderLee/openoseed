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

import java.util.List;

import org.openo.sdno.inventory.sdk.model.annotation.MOResType;
import org.openo.sdno.inventory.sdk.model.annotation.NONInvField;
import org.openo.sdno.vpn.wan.db.FieldConvertUtil;
import org.openo.sdno.vpn.wan.db.PoModel;
import org.openo.sdno.vpn.wan.servicemodel.common.NvString;
import org.openo.sdno.vpn.wan.servicemodel.tp.IpTpSpec;

@MOResType(infoModelName = "wan_iptpspec")
public class IpTpSpecPo implements PoModel<IpTpSpec> {

    private String uuid;

    private String masterIp;

    @NONInvField
    private List<NvString> addtionalInfo;

    public String getMasterIp() {
        return masterIp;
    }

    public void setMasterIp(String masterIp) {
        this.masterIp = masterIp;
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

    /**
     * Convert to service model.
     * 
     * @since SDNO 0.5
     */
    @Override
    public IpTpSpec toSvcModel() {
        final IpTpSpec svcModel = new IpTpSpec();
        FieldConvertUtil.setA2B(this, svcModel);
        return svcModel;
    }

    /**
     * Convert to po from service model.
     * 
     * @since SDNO 0.5
     */
    @Override
    public void fromSvcModel(IpTpSpec svcModel) {
        FieldConvertUtil.setA2B(svcModel, this);
    }
}
