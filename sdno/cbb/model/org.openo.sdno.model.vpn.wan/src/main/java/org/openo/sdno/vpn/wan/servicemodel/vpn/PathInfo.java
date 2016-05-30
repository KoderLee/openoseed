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
package org.openo.sdno.vpn.wan.servicemodel.vpn;

import java.util.List;

import org.openo.sdno.vpn.wan.paradesc.ContainerSizeDesc;
import org.openo.sdno.vpn.wan.paradesc.EnumDesc;
import org.openo.sdno.vpn.wan.paradesc.StringDesc;
import org.openo.sdno.vpn.wan.servicemodel.AbstractSvcModel;
import org.openo.sdno.vpn.wan.servicemodel.common.BooleanType;
import org.openo.sdno.vpn.wan.servicemodel.common.NvString;
import org.openo.sdno.vpn.wan.servicemodel.common.PathRole;
import org.openo.sdno.vpn.wan.servicemodel.tp.Tp;

public class PathInfo extends AbstractSvcModel {

    @StringDesc(maxLen = 36)
    private String uuid;

    @StringDesc(pattern = "[TFtf]")
    private String bFullPath;

    @EnumDesc(BooleanType.class)
    private String bSequenceStrict;

    @EnumDesc(PathRole.class)
    private String pathRole;

    @ContainerSizeDesc(maxSize = 1000)
    private List<Tp> includeTpList;

    @ContainerSizeDesc(maxSize = 1000)
    private List<NvString> addtionalInfo;

    public String getbFullPath() {
        return bFullPath;
    }

    public void setbFullPath(String bFullPath) {
        this.bFullPath = bFullPath;
    }

    public String getbSequenceStrict() {
        return bSequenceStrict;
    }

    public void setbSequenceStrict(String bSequenceStrict) {
        this.bSequenceStrict = bSequenceStrict;
    }

    public String getPathRole() {
        return pathRole;
    }

    public void setPathRole(String pathRole) {
        this.pathRole = pathRole;
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

    public List<Tp> getIncludeTpList() {
        return includeTpList;
    }

    public void setIncludeTpList(List<Tp> includeTpList) {
        this.includeTpList = includeTpList;
    }
}
