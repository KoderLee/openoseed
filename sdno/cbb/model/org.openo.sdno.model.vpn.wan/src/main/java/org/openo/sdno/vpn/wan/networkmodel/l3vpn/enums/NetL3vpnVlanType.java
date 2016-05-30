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

public enum NetL3vpnVlanType {
    DOT1Q(0, "dot1q"), VLANTYPE(1, "vlantype"), QINQ(2, "qinq"), UNTAGGED(3, "untagged");

    private int value;

    private String name;

    private NetL3vpnVlanType(int value, String name) {
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
        if(NetL3vpnVlanType.DOT1Q.getName().equals(nameStr)) {
            return NetL3vpnVlanType.DOT1Q.getValue();
        }

        if(NetL3vpnVlanType.VLANTYPE.getName().equals(nameStr)) {
            return NetL3vpnVlanType.VLANTYPE.getValue();
        }

        if(NetL3vpnVlanType.QINQ.getName().equals(nameStr)) {
            return NetL3vpnVlanType.QINQ.getValue();
        }

        if(NetL3vpnVlanType.UNTAGGED.getName().equals(nameStr)) {
            return NetL3vpnVlanType.UNTAGGED.getValue();
        }

        return -1;
    }
}
