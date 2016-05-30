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

import java.util.List;

import org.openo.sdno.vpn.wan.paradesc.ContainerSizeDesc;
import org.openo.sdno.vpn.wan.paradesc.IpDesc;
import org.openo.sdno.vpn.wan.paradesc.StringDesc;
import org.openo.sdno.vpn.wan.servicemodel.AbstractSvcModel;
import org.openo.sdno.vpn.wan.servicemodel.common.NvString;

public class CeTp extends AbstractSvcModel {

    @StringDesc(maxLen = 36)
    private String uuid;

    /**
     * The customer site.
     */
    @StringDesc(maxLen = 200)
    private String siteName;

    /**
     * CE device name.
     */
    @StringDesc(maxLen = 200)
    private String ceName;

    @IpDesc
    private String ceIfmasterIp;

    @StringDesc(maxLen = 200)
    private String location;

    @ContainerSizeDesc(maxSize = 1000)
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

}
