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
package org.openo.sdno.vpn.wan.networkmodel.l3vpn;

import org.openo.sdno.vpn.wan.networkmodel.NetModel;
import org.openo.sdno.vpn.wan.networkmodel.l3vpn.enums.NetL3vpnProtocolType;
import org.openo.sdno.vpn.wan.networkmodel.l3vpn.enums.NetL3vpnStatus;
import org.openo.sdno.vpn.wan.networkmodel.l3vpn.enums.NetL3vpnVlanType;

public class AcIf implements NetModel {

    private String acIfName;

    private int acIfNumber;

    private String description;

    private String acIfAddr;

    private int acIfMaskLength;

    private String protocol = NetL3vpnProtocolType.UNASSIGNED.getName();

    private String adminStatus = NetL3vpnStatus.UP.getName();

    private String operStatus = NetL3vpnStatus.UP.getName();

    private String vlanType = NetL3vpnVlanType.DOT1Q.getName();

    private String sVlan;

    private String cVlan;

    private StaticRoutes staticRoutes;

    private BgpProtocols bgpProtocols;

    public String getAcIfName() {
        return acIfName;
    }

    public void setAcIfName(String acIfName) {
        this.acIfName = acIfName;
    }

    public int getAcIfNumber() {
        return acIfNumber;
    }

    public void setAcIfNumber(int acIfNumber) {
        this.acIfNumber = acIfNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAcIfAddr() {
        return acIfAddr;
    }

    public void setAcIfAddr(String acIfAddr) {
        this.acIfAddr = acIfAddr;
    }

    public int getAcIfMaskLength() {
        return acIfMaskLength;
    }

    public void setAcIfMaskLength(int acIfMaskLength) {
        this.acIfMaskLength = acIfMaskLength;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getAdminStatus() {
        return adminStatus;
    }

    public void setAdminStatus(String adminStatus) {
        this.adminStatus = adminStatus;
    }

    public String getOperStatus() {
        return operStatus;
    }

    public void setOperStatus(String operStatus) {
        this.operStatus = operStatus;
    }

    public String getVlanType() {
        return vlanType;
    }

    public void setVlanType(String vlanType) {
        this.vlanType = vlanType;
    }

    public StaticRoutes getStaticRoutes() {
        return staticRoutes;
    }

    public void setStaticRoutes(StaticRoutes staticRoutes) {
        this.staticRoutes = staticRoutes;
    }

    public BgpProtocols getBgpProtocols() {
        return bgpProtocols;
    }

    public void setBgpProtocols(BgpProtocols bgpProtocols) {
        this.bgpProtocols = bgpProtocols;
    }

    public String getsVlan() {
        return sVlan;
    }

    public void setsVlan(String sVlan) {
        this.sVlan = sVlan;
    }

    public String getcVlan() {
        return cVlan;
    }

    public void setcVlan(String cVlan) {
        this.cVlan = cVlan;
    }
}
