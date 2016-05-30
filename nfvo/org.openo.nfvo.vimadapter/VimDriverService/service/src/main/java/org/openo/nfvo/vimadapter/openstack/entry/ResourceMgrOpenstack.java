/*******************************************************************************
 * Copyright (c) 2016, Huawei Technologies Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

package org.openo.nfvo.vimadapter.openstack.entry;

import java.util.List;
import java.util.Map;

import org.openo.nfvo.vimadapter.util.InterfaceResourceMgr;
import org.openo.nfvo.vimadapter.openstack.resourcemgr.NetworkQuery;
import org.openo.nfvo.vimadapter.openstack.resourcemgr.RpQuery;
import org.openo.nfvo.vimadapter.openstack.resourcemgr.VendorQuery;
import org.openo.nfvo.vimadapter.openstack.resourcemgr.VmQuery;

import net.sf.json.JSONObject;

/**
 * 
* The reource manager on openstack, include get resource poll, vendor(tenants), vm, network<br/>
* <p>
* </p>
* 
* @author
* @version NFVO 0.5 May 15, 2016
 */
public class ResourceMgrOpenstack implements InterfaceResourceMgr {
    @Override
    public List<JSONObject> getRps(Map<String, String> conMap, JSONObject param) {
        RpQuery query = new RpQuery(conMap);
        return query.getRps();
    }

    @Override
    public List<JSONObject> getVendors(Map<String, String> conMap,
            JSONObject param) {
        VendorQuery vendorQuery = new VendorQuery(conMap);
        return vendorQuery.getVendors();
    }

    @Override
    public List<JSONObject> getVms(Map<String, String> conMap, JSONObject param) {
        VmQuery vQuery = new VmQuery(conMap);
        return vQuery.getVms();
    }

    @Override
    public List<JSONObject> getNetworks(Map<String, String> conMap,
            JSONObject param) {
        NetworkQuery query = new NetworkQuery(conMap);
        return query.getNetworks();
    }
}
