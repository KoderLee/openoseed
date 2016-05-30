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
import org.openo.sdno.vpn.wan.servicemodel.vpn.PathInfo;

@MOResType(infoModelName = "wan_pathinfo")
public class PathInfoPo implements PoModel<PathInfo> {

    private String uuid;

    private String isFullPath;

    private String isSequenceStrict;

    private String pathRole;

    private String vpnBasicInfoId;

    @NONInvField
    private List<NvString> addtionalInfo;

    @Override
    public PathInfo toSvcModel() {
        final PathInfo svcModel = new PathInfo();
        FieldConvertUtil.setA2B(this, svcModel);
        return svcModel;
    }

    @Override
    public void fromSvcModel(PathInfo svcModel) {
        FieldConvertUtil.setA2B(svcModel, this);
    }

    /**
     * @return Returns the uuid.
     */
    @Override
    public String getUuid() {
        return uuid;
    }

    /**
     * @param uuid The uuid to set.
     */
    @Override
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    /**
     * @return Returns the isFullPath.
     */
    public String getIsFullPath() {
        return isFullPath;
    }

    /**
     * @param isFullPath The isFullPath to set.
     */
    public void setIsFullPath(String isFullPath) {
        this.isFullPath = isFullPath;
    }

    /**
     * @return Returns the isSequenceStrict.
     */
    public String getIsSequenceStrict() {
        return isSequenceStrict;
    }

    /**
     * @param isSequenceStrict The isSequenceStrict to set.
     */
    public void setIsSequenceStrict(String isSequenceStrict) {
        this.isSequenceStrict = isSequenceStrict;
    }

    /**
     * @return Returns the pathRole.
     */
    public String getPathRole() {
        return pathRole;
    }

    /**
     * @param pathRole The pathRole to set.
     */
    public void setPathRole(String pathRole) {
        this.pathRole = pathRole;
    }

    /**
     * @return Returns the vpnBasicInfoId.
     */
    public String getVpnBasicInfoId() {
        return vpnBasicInfoId;
    }

    /**
     * @param vpnBasicInfoId The vpnBasicInfoId to set.
     */
    public void setVpnBasicInfoId(String vpnBasicInfoId) {
        this.vpnBasicInfoId = vpnBasicInfoId;
    }

    /**
     * @return Returns the addtionalInfo.
     */
    public List<NvString> getAddtionalInfo() {
        return addtionalInfo;
    }

    /**
     * @param addtionalInfo The addtionalInfo to set.
     */
    public void setAddtionalInfo(List<NvString> addtionalInfo) {
        this.addtionalInfo = addtionalInfo;
    }
}
