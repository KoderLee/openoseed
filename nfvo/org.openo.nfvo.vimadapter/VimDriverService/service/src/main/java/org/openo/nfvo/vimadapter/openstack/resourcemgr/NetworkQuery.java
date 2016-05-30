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

package org.openo.nfvo.vimadapter.openstack.resourcemgr;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openo.baseservice.log.OssLog;
import org.openo.baseservice.log.OssLogFactory;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.openo.nfvo.vimadapter.util.constant.Constant;
import org.openo.nfvo.vimadapter.util.constant.UrlConstant;
import org.openo.nfvo.vimadapter.util.http.HttpRequests;
import org.openo.nfvo.vimadapter.util.http.LoginException;
import org.openo.nfvo.vimadapter.openstack.api.ConnectInfo;
import org.openo.nfvo.vimadapter.openstack.connectmgr.ConnectionMgr;
import org.openo.nfvo.vimadapter.openstack.connectmgr.OpenstackConnection;

/**
 * 
* Query the network info from VIM<br/>
* <p>
* </p>
* 
* @author
* @version NFVO 0.5 May 15, 2016
 */
public class NetworkQuery {
    private static final OssLog LOG = OssLogFactory
            .getLogger(NetworkQuery.class);

    private ConnectInfo connect;

    private String queryId;

    private String url;

    private String vimId;

    private String vimName;

    public NetworkQuery(Map<String, String> conMap) {
        connect = new ConnectInfo(conMap);
        this.queryId = conMap.get("queryId");
        vimId = conMap.get("vimId");
        vimName = conMap.get("vimName");

        init();
    }

    public void init() {
        if (null != queryId && !"".equals(queryId.trim())) {
            url = UrlConstant.GET_NETWORK + '/' + queryId;
            return;
        }

        url = UrlConstant.GET_NETWORK;
    }

    /**
     * Get the networks from vim<br/>
     * 
     * @return the network data to be queried
     * @since NFVO 0.5
     */
    public List<JSONObject> getNetworks() {
        OpenstackConnection con = null;
        try {
            con = ConnectionMgr.getConnectionMgr().getConnection(connect);

            LOG.debug("function=getNetworks->url :"
                    + String.format(url,
                            con.getServiceUrl(Constant.ServiceName.NEUTRON)));

            String result = new HttpRequests.Builder(
                    connect.getAuthenticateMode())
                    .addHeader(Constant.HEADER_AUTH_TOKEN,
                            con.getDomainTokens())
                    .setUrl(String.format(url,
                            con.getServiceUrl(Constant.ServiceName.NEUTRON)))
                    .get().request();

            LOG.warn("function=getNetworks->result:" + result);
            JSONObject jsonObject = JSONObject.fromObject(result);

            if (jsonObject.containsKey(Constant.WRAP_NETWORKS)
                    || jsonObject.containsKey(Constant.WRAP_NETWORK)) {
                return getNetworksMap(jsonObject);
            }

        } catch (LoginException e) {
            LOG.error(
                    "function=\"getNetworks\", msg=LoginException occurs. e={}",
                    e);
        }
        return null;
    }

    private List<JSONObject> getNetworksMap(JSONObject jsonObject) {
        List<JSONObject> mapList = new ArrayList<JSONObject>(
                Constant.DEFAULT_COLLECTION_SIZE);
        if (!url.equals(UrlConstant.GET_NETWORK)) {
            mapList.add(addNetworksMap(jsonObject
                    .getJSONObject(Constant.WRAP_NETWORK)));
            return mapList;
        }

        JSONArray networkJsonObj = jsonObject
                .getJSONArray(Constant.WRAP_NETWORKS);
        int netSize = networkJsonObj.size();

        for (int i = 0; i < netSize; i++) {
            mapList.add(addNetworksMap(networkJsonObj.getJSONObject(i)));
        }
        return mapList;
    }

    /**
     * Get the networks from vim<br/>
     * 
     * @param obj
     * the network info 
     * @return the completed network map .
     * @since NFVO 0.5
     */
    private JSONObject addNetworksMap(JSONObject obj) {
        JSONObject netWorkMap = new JSONObject();
        netWorkMap.put("vimId", vimId);
        netWorkMap.put("vimName", vimName);
        netWorkMap.put("id", obj.getString("id"));
        netWorkMap.put("shared", obj.getString("shared"));
        netWorkMap.put("subnets", obj.getJSONArray("subnets"));
        netWorkMap.put("name", obj.getString("name"));
        netWorkMap.put("tenantId", obj.getString("tenant_id"));
        netWorkMap.put("status", obj.getString("status"));

        JSONObject providerMap = new JSONObject();

        providerMap.put("networkType", obj.getString("provider:network_type"));
        providerMap.put("physicalNetwork",
                obj.getString("provider:physical_network"));
        providerMap.put("segmentationId",
                obj.getString("provider:segmentation_id"));
        netWorkMap.put("provider", providerMap);

        JSONObject routerMap = new JSONObject();
        routerMap.put("external", obj.getString("router:external"));
        netWorkMap.put("router", routerMap);

        return netWorkMap;
    }
}