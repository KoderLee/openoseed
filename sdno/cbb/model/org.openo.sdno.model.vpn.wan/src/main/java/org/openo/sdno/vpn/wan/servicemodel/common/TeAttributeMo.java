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
package org.openo.sdno.vpn.wan.servicemodel.common;

import org.openo.sdno.inventory.sdk.model.BaseMO;
import org.openo.sdno.inventory.sdk.model.annotation.MOResType;

@MOResType(infoModelName = "wan_te_attr")
public class TeAttributeMo extends BaseMO {

    private String linkId;

    private String nodeId;

    private String linkDirection;

    private String latency;

    private String teMetric;

    private String adminGroup;

    private String bc0;

    private String threholdBandwidth;

    private String maxResBandwidth;

    private String totalReservedBandwidth;

    private String totalAvailableBandwidth;

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getLinkId() {
        return linkId;
    }

    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }

    public String getLinkDirection() {
        return linkDirection;
    }

    public void setLinkDirection(String linkDirection) {
        this.linkDirection = linkDirection;
    }

    public String getLatency() {
        return latency;
    }

    public void setLatency(String latency) {
        this.latency = latency;
    }

    public String getTeMetric() {
        return teMetric;
    }

    public void setTeMetric(String teMetric) {
        this.teMetric = teMetric;
    }

    public String getAdminGroup() {
        return adminGroup;
    }

    public void setAdminGroup(String adminGroup) {
        this.adminGroup = adminGroup;
    }

    public String getBc0() {
        return bc0;
    }

    public void setBc0(String bc0) {
        this.bc0 = bc0;
    }

    public String getThreholdBandwidth() {
        return threholdBandwidth;
    }

    public void setThreholdBandwidth(String threholdBandwidth) {
        this.threholdBandwidth = threholdBandwidth;
    }

    public String getMaxResBandwidth() {
        return maxResBandwidth;
    }

    public void setMaxResBandwidth(String maxResBandwidth) {
        this.maxResBandwidth = maxResBandwidth;
    }

    public String getTotalReservedBandwidth() {
        return totalReservedBandwidth;
    }

    public void setTotalReservedBandwidth(String totalReservedBandwidth) {
        this.totalReservedBandwidth = totalReservedBandwidth;
    }

    public String getTotalAvailableBandwidth() {
        return totalAvailableBandwidth;
    }

    public void setTotalAvailableBandwidth(String totalAvailableBandwidth) {
        this.totalAvailableBandwidth = totalAvailableBandwidth;
    }
}
