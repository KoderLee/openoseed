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
import org.openo.sdno.vpn.wan.paradesc.EnumDesc;
import org.openo.sdno.vpn.wan.paradesc.StringDesc;
import org.openo.sdno.vpn.wan.servicemodel.AbstractSvcModel;
import org.openo.sdno.vpn.wan.servicemodel.common.EncapType;
import org.openo.sdno.vpn.wan.servicemodel.common.NvString;
import org.openo.sdno.vpn.wan.servicemodel.common.VlanActionType;

public class EthernetTpSpec extends AbstractSvcModel {

    @StringDesc(maxLen = 36)
    private String uuid;

    @EnumDesc(EncapType.class)
    private String encapType;

    @EnumDesc(EncapType.class)
    private String portAcceptFrameType;

    @EnumDesc(VlanActionType.class)
    private String vlanAction;

    @StringDesc(maxLen = 400)
    private String cvlanList;

    @StringDesc(maxLen = 400)
    private String svlanList;

    @StringDesc(maxLen = 400)
    private String mappingCvlanList;

    @StringDesc(maxLen = 400)
    private String mappingSvlanList;

    @ContainerSizeDesc(maxSize = 1000)
    private List<NvString> addtionalInfo;

    public String getEncapType() {
        return encapType;
    }

    public void setEncapType(String encapType) {
        this.encapType = encapType;
    }

    public String getPortAcceptFrameType() {
        return portAcceptFrameType;
    }

    public void setPortAcceptFrameType(String portAcceptFrameType) {
        this.portAcceptFrameType = portAcceptFrameType;
    }

    public String getVlanAction() {
        return vlanAction;
    }

    public void setVlanAction(String vlanAction) {
        this.vlanAction = vlanAction;
    }

    public String getCvlanList() {
        return cvlanList;
    }

    public void setCvlanList(String cvlanList) {
        this.cvlanList = cvlanList;
    }

    public String getSvlanList() {
        return svlanList;
    }

    public void setSvlanList(String svlanList) {
        this.svlanList = svlanList;
    }

    public String getMappingCvlanList() {
        return mappingCvlanList;
    }

    public void setMappingCvlanList(String mappingCvlanList) {
        this.mappingCvlanList = mappingCvlanList;
    }

    public String getMappingSvlanList() {
        return mappingSvlanList;
    }

    public void setMappingSvlanList(String mappingSvlanList) {
        this.mappingSvlanList = mappingSvlanList;
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
