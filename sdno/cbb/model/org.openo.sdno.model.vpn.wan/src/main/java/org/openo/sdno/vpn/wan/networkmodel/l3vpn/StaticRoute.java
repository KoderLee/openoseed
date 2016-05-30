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

public class StaticRoute {

    private String destination;

    private int maskLength;

    private String nextHopAddr;

    private int preference;

    public StaticRoute() {
        // Constructed function.
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getMaskLength() {
        return maskLength;
    }

    public void setMaskLength(int maskLength) {
        this.maskLength = maskLength;
    }

    public String getNextHopAddr() {
        return nextHopAddr;
    }

    public void setNextHopAddr(String nextHopAddr) {
        this.nextHopAddr = nextHopAddr;
    }

    public int getPreference() {
        return preference;
    }

    public void setPreference(int preference) {
        this.preference = preference;
    }
}
