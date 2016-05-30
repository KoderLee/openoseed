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

import org.openo.sdno.inventory.sdk.model.SiteMO;

public class SiteVo {

    private String uuid;

    private String name;

    private String description;

    private String location;

    private String cpeName;

    private String cpeIp;

    private String cpeId;

    private String type;

    private String asName;

    private String asUuid;

    public final String getUuid() {
        return uuid;
    }

    public final void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public final String getName() {
        return name;
    }

    public final void setName(String name) {
        this.name = name;
    }

    public final String getDescription() {
        return description;
    }

    public final void setDescription(String description) {
        this.description = description;
    }

    public final String getLocation() {
        return location;
    }

    public final void setLocation(String location) {
        this.location = location;
    }

    public final String getCpeName() {
        return cpeName;
    }

    public final void setCpeName(String cpeName) {
        this.cpeName = cpeName;
    }

    public final String getCpeIp() {
        return cpeIp;
    }

    public final void setCpeIp(String cpeIp) {
        this.cpeIp = cpeIp;
    }

    public final String getCpeId() {
        return cpeId;
    }

    public final void setCpeId(String cpeId) {
        this.cpeId = cpeId;
    }

    public final String getAsName() {
        return asName;
    }

    public final void setAsName(String asName) {
        this.asName = asName;
    }

    public final String getAsUuid() {
        return asUuid;
    }

    public final void setAsUuid(String asUuid) {
        this.asUuid = asUuid;
    }

    public final String getType() {
        return type;
    }

    public final void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "SiteVo [uuid=" + uuid + ", name=" + name +", description="
                + description + ", location=" + location + ", cpeName=" + cpeName + ", cpeIp=" + cpeIp + ", cpeId="
                + cpeId + ", type=" + type + ", asName=" + asName + ", asUuid=" + asUuid + "]";
    }

    public static SiteVo moToVo(SiteMO siteMo) {
        SiteVo siteVo = new SiteVo();
        siteVo.setUuid(siteMo.getUuid());
        siteVo.setLocation(siteMo.getLocation());
        siteVo.setDescription(siteMo.getDescription());
        siteVo.setName(siteMo.getName());
        siteVo.setType(siteMo.getType());
        return siteVo;
    }

}
