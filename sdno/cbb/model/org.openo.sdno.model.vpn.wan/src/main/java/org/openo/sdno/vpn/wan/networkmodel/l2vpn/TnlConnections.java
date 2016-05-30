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
package org.openo.sdno.vpn.wan.networkmodel.l2vpn;

import java.util.List;

public class TnlConnections {

    private List<TnlConnection> tnlConnection;

    private PortBinding portBinding;

    public List<TnlConnection> getTnlConnection() {
        return tnlConnection;
    }

    public void setTnlConnection(List<TnlConnection> tnlConnection) {
        this.tnlConnection = tnlConnection;
    }

    public PortBinding getPortBinding() {
        return portBinding;
    }

    public void setPortBinding(PortBinding portBinding) {
        this.portBinding = portBinding;
    }

}
