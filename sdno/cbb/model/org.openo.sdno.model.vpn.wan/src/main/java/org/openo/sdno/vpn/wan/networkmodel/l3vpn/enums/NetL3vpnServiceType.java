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

public enum NetL3vpnServiceType {
    NULL(0, "null"), FULLMESH(1, "fullmesh"), HUB_SPOKE(2, "hub-spoke");

    private int value;

    private String name;

    private NetL3vpnServiceType(int value, String name) {
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
        if(NetL3vpnServiceType.NULL.getName().equals(nameStr)) {
            return NetL3vpnServiceType.NULL.getValue();
        }

        if(NetL3vpnServiceType.FULLMESH.getName().equals(nameStr)) {
            return NetL3vpnServiceType.FULLMESH.getValue();
        }

        if(NetL3vpnServiceType.HUB_SPOKE.getName().equals(nameStr)) {
            return NetL3vpnServiceType.HUB_SPOKE.getValue();
        }

        return -1;
    }

}
