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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.openo.sdno.inventory.sdk.model.LinkMO;
import org.openo.sdno.vpn.wan.servicemodel.vpn.MultiAsVpn;
import org.openo.sdno.vpn.wan.servicemodel.vpn.Vpn;

public class VpnDetail {

    private TopoView topoView;

    private MultiAsVpn multiAsVpn = null;

    private List<LinkMO> multiAsAternativeLinks = new ArrayList<LinkMO>();

    private List<Vpn> singleAsVpnList = new ArrayList<Vpn>();

    private Map<String, List<String>> vpnPathMap = new HashMap<String, List<String>>();

    private Map<String, List<String>> linkPathMap = new HashMap<String, List<String>>();

    public TopoView getTopoView() {
        return topoView;
    }

    public void setTopoView(TopoView topoView) {
        this.topoView = topoView;
    }

    public final MultiAsVpn getMultiAsVpn() {
        return multiAsVpn;
    }

    public final void setMultiAsVpn(MultiAsVpn multiAsVpn) {
        this.multiAsVpn = multiAsVpn;
    }

    public final Map<String, List<String>> getVpnPathMap() {
        return vpnPathMap;
    }

    public final void setVpnPathMap(Map<String, List<String>> vpnPathMap) {
        this.vpnPathMap = vpnPathMap;
    }

    public Map<String, List<String>> getLinkPathMap() {
        return linkPathMap;
    }

    public void setLinkPathMap(Map<String, List<String>> linkPathMap) {
        this.linkPathMap = linkPathMap;
    }

    public final List<LinkMO> getMultiAsAternativeLinks() {
        return multiAsAternativeLinks;
    }

    public final void setMultiAsAternativeLinks(List<LinkMO> multiAsAternativeLinks) {
        this.multiAsAternativeLinks = multiAsAternativeLinks;
    }

    public final List<Vpn> getSingleAsVpnList() {
        return singleAsVpnList;
    }

    public final void setSingleAsVpnList(List<Vpn> singleAsVpnList) {
        this.singleAsVpnList = singleAsVpnList;
    }

    public final List<VpnLinkPathVo> getVpnLinkPathMap() {
        List<VpnLinkPathVo> vpnLinkPathMap = new ArrayList<VpnLinkPathVo>();
        Iterator<String> vpnNameIter = linkPathMap.keySet().iterator();

        while(vpnNameIter.hasNext()) {
            String vpnName = vpnNameIter.next();
            VpnLinkPathVo vpnLinkPathVo = new VpnLinkPathVo();
            vpnLinkPathVo.setName(vpnName);
            vpnLinkPathVo.setVpnLinkList(linkPathMap.get(vpnName));
            vpnLinkPathMap.add(vpnLinkPathVo);
        }
        return vpnLinkPathMap;
    }

    public void addLinkPath(String vpnName, String linkId) {
        if(!linkPathMap.containsKey(vpnName) || null == linkPathMap.get(vpnName)) {
            linkPathMap.put(vpnName, new ArrayList<String>());
        }
        linkPathMap.get(vpnName).add(linkId);
    }

    @Override
    public String toString() {
        return "VpnDetail [topoView=" + topoView + ", multiAsVpn=" + multiAsVpn + ", multiAsAternativeLinks="
                + multiAsAternativeLinks + ", singleAsVpnList=" + singleAsVpnList + ", vpnPathMap=" + vpnPathMap
                + ", tempPathMap=" + linkPathMap + "]";
    }
}
