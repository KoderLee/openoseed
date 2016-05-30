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
package org.openo.sdno.vpn.wan.networkmodel.commontypes;

public enum NotificationType {
    NE(0, "ne"), LINK(1, "link"), LTP(2, "ltp");

    private int value;

    private String name;

    private NotificationType(int value, String name) {
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
        if(NotificationType.NE.getName().equals(nameStr)) {
            return NotificationType.NE.getValue();
        }

        if(NotificationType.LINK.getName().equals(nameStr)) {
            return NotificationType.LINK.getValue();
        }

        if(NotificationType.LTP.getName().equals(nameStr)) {
            return NotificationType.LTP.getValue();
        }

        return -1;
    }

}
