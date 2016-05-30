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
package org.openo.sdno.vpn.wan.servicemodel.composedvpn;

public class NetworkElement extends BaseTopoVo {

    private String nodeClass = "NetworkElement";

    private String productType;

    private String asName;

    private String icon = "router";

    private String role = "";

    private String x = "100";

    private String y = "100";

    public String getNodeClass() {
        return nodeClass;
    }

    public void setNodeClass(String nodeClass) {
        this.nodeClass = nodeClass;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public final String getAsName() {
        return asName;
    }

    public final void setAsName(String asName) {
        this.asName = asName;
    }

    public final String getRole() {
        return role;
    }

    public final void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "NetworkElement [nodeClass=" + nodeClass + ", productType=" + productType + ", asName=" + asName
                + ", icon=" + icon + ", role=" + role + ", x=" + x + ", y=" + y + "]";
    }
}
