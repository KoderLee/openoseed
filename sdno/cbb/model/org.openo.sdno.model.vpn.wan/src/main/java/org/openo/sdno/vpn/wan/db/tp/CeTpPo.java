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
import org.openo.sdno.vpn.wan.servicemodel.tp.CeTp;

@MOResType(infoModelName = "wan_cetp")
public class CeTpPo implements PoModel<CeTp> {

    private String uuid;

    /**
     * The customer site.
     */
    private String siteName;

    /**
     * CE device name.
     */
    private String ceName;

    private String ceIfmasterIp;

    private String location;

    @NONInvField
    private List<NvString> addtionalInfo;

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getCeName() {
        return ceName;
    }

    public void setCeName(String ceName) {
        this.ceName = ceName;
    }

    public String getCeIfmasterIp() {
        return ceIfmasterIp;
    }

    public void setCeIfmasterIp(String ceIfmasterIp) {
        this.ceIfmasterIp = ceIfmasterIp;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    @Override
    public CeTp toSvcModel() {
        final CeTp svcModel = new CeTp();
        FieldConvertUtil.setA2B(this, svcModel);
        return svcModel;
    }

    @Override
    public void fromSvcModel(CeTp svcModel) {
        FieldConvertUtil.setA2B(svcModel, this);
    }

}
