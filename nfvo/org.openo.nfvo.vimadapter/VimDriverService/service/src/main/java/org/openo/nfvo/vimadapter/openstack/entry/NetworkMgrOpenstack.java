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

import java.util.Map;

import org.openo.nfvo.vimadapter.util.InterfaceNetworkMgr;
import org.openo.nfvo.vimadapter.openstack.networkmgr.OpenstackNetwork;
import org.openo.nfvo.vimadapter.openstack.networkmgr.OpenstackSubNet;

import net.sf.json.JSONObject;

import org.openo.baseservice.log.OssLog;
import org.openo.baseservice.log.OssLogFactory;

/**
 * 
* The network manager on openstack, include create/remove network and sub network<br/>
* <p>
* </p>
* 
* @author
* @version NFVO 0.5 May 15, 2016
 */
public class NetworkMgrOpenstack implements InterfaceNetworkMgr {
    private static final OssLog LOG = OssLogFactory
            .getLogger(NetworkMgrOpenstack.class);

    @Override
    public JSONObject create(JSONObject network, Map<String, String> conInfoMap) {
        LOG.warn("funtion=create, msg=create new one.");
        OpenstackNetwork fsNetMgr = new OpenstackNetwork(conInfoMap);
        return fsNetMgr.createNetwork(network);
    }

    @Override
    public int remove(JSONObject network, Map<String, String> conInfoMap) {
        LOG.warn("function=remove, msg=remove one network.");
        OpenstackNetwork fsNetMgr = new OpenstackNetwork(conInfoMap);
        return fsNetMgr.removeNetwork(network);
    }

    @Override
    public JSONObject createSubNet(JSONObject network,
            Map<String, String> conInfoMap) {
        LOG.warn("funtion=createSubNet, msg=create new one.");
        OpenstackSubNet fsSubnetMgr = new OpenstackSubNet(conInfoMap);
        return fsSubnetMgr.createSubNet(network);
    }

    @Override
    public int deleteSubNet(String id, Map<String, String> conInfoMap) {
        LOG.warn("funtion=deleteSubNet, msg=del one.");
        OpenstackSubNet fsSubnetMgr = new OpenstackSubNet(conInfoMap);
        return fsSubnetMgr.deleteSubNet(id);
    }

}
