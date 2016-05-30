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
package org.openo.sdno.vpn.wan.networkmodel.l3vpn.enums;

public enum NetL3vpnAfType {
    UNASSIGNED(0, "unassigned"), IPV4UNI(1, "ipv4uni"), IPV6UNI(2, "ipv6uni");

    private int value;

    private String name;

    private NetL3vpnAfType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static int getIntValueByName(String nameStr) {
        if(NetL3vpnAfType.UNASSIGNED.getName().equals(nameStr)) {
            return NetL3vpnAfType.UNASSIGNED.getValue();
        }

        if(NetL3vpnAfType.IPV4UNI.getName().equals(nameStr)) {
            return NetL3vpnAfType.IPV4UNI.getValue();
        }

        if(NetL3vpnAfType.IPV6UNI.getName().equals(nameStr)) {
            return NetL3vpnAfType.IPV6UNI.getValue();
        }

        return -1;
    }
}
