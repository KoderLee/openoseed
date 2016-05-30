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

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

public class TopoView {

    private String id;

    private String name;

    private List<NetworkElement> networkElements = new ArrayList<NetworkElement>();

    private List<ConnectionLink> connections = new ArrayList<ConnectionLink>();

    private List<?> alarms;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<NetworkElement> getNetworkElements() {
        return networkElements;
    }

    public void setNetworkElements(List<NetworkElement> networkElements) {
        this.networkElements = networkElements;
    }

    public List<ConnectionLink> getConnections() {
        return connections;
    }

    public void setConnections(List<ConnectionLink> connections) {
        this.connections = connections;
    }

    public List<?> getAlarms() {
        return alarms;
    }

    public void setAlarms(List<?> alarms) {
        this.alarms = alarms;
    }

    public void addLinkIfNotExist(ConnectionLink connection) {
        if(null == connection.findLinkId(this.getConnections())) {
            this.getConnections().add(connection);
        }
    }

    public boolean isNetworkElementExist(String uuid) {
        for(NetworkElement ne : this.networkElements) {
            if(uuid.equals(ne.getId())) {
                return true;
            }
        }
        return false;
    }

    @JsonIgnore
    public List<NetworkElement> getNetworkElementsByIcon(String iconName) {
        List<NetworkElement> result = new ArrayList<NetworkElement>();
        for(NetworkElement ne : this.networkElements) {
            if(ne.getIcon().equals(TopoConstant.SITE_ICON)) {
                result.add(ne);
            }
        }
        return result;
    }

    @JsonIgnore
    public List<ConnectionLink> getLinksBySrcNeId(String srcNeId) {
        List<ConnectionLink> linkList = new ArrayList<ConnectionLink>();
        for(ConnectionLink link : this.getConnections()) {
            if(link.getFromId().equals(srcNeId)) {
                linkList.add(link);
            }
        }
        return linkList;
    }

}
