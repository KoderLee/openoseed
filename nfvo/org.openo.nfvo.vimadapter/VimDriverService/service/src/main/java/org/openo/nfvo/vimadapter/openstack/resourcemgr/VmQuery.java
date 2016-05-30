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
* Query the vms info from VIM<br/>
* <p>
* </p>
* 
* @author
* @version NFVO 0.5 May 15, 2016
 */
public class VmQuery {
    private static final OssLog LOG = OssLogFactory.getLogger(VmQuery.class);

    private ConnectInfo connect;

    private String queryId;

    private String vimId;

    private String vimName;

    private String url;

    public VmQuery(Map<String, String> conMap) {
        queryId = conMap.get("queryId");
        init();
        connect = new ConnectInfo(conMap);
        vimId = conMap.get("vimId");
        vimName = conMap.get("vimName");
    }

    public void init() {
        if (null != queryId && !"".equals(queryId.trim())) {
            url = UrlConstant.GET_VM.substring(0,
                    UrlConstant.GET_VM.lastIndexOf("/") + 1)
                    + queryId;
            return;
        }

        url = UrlConstant.GET_VM;
    }

    /**
     * Get the vendors from vim<br/>
     * 
     * @return the vms data to be queried
     * @since NFVO 0.5
     */
    public List<JSONObject> getVms() {
        try {
            LOG.error("queryId=" + queryId + ",url=" + url);
            OpenstackConnection con = ConnectionMgr.getConnectionMgr()
                    .getConnection(connect);
            LOG.debug("function=getVms,msg="
                    + con.getServiceUrl(Constant.ServiceName.NOVA));

            String result = new HttpRequests.Builder(
                    connect.getAuthenticateMode())
                    .addHeader(Constant.HEADER_AUTH_TOKEN,
                            con.getDomainTokens())
                    .setUrl(String.format(url,
                            con.getServiceUrl(Constant.ServiceName.NOVA),
                            con.getProjectId())).get().request();

            LOG.warn("function=getVms,msg=fs result: " + result);
            JSONObject vmsJsonObj = JSONObject.fromObject(result);
            if (vmsJsonObj.containsKey(Constant.WRAP_SERVERS)
                    || vmsJsonObj.containsKey(Constant.WRAP_SERVER)) {
                return getVmsMap(vmsJsonObj);
            }
        } catch (LoginException e) {
            LOG.error("function=getVms, msg=get from fs OpenStackLoginException, exception="
                    + e);
        }

        return null;
    }

    private List<JSONObject> getVmsMap(JSONObject vmsJsonObj) {
        List<JSONObject> list = new ArrayList<JSONObject>(
                Constant.DEFAULT_COLLECTION_SIZE);
        JSONArray array = new JSONArray();
        if (vmsJsonObj.containsKey(Constant.WRAP_SERVERS)) {
            array = vmsJsonObj.getJSONArray(Constant.WRAP_SERVERS);
        }
        if (vmsJsonObj.containsKey(Constant.WRAP_SERVER)) {
            array.add(vmsJsonObj.getJSONObject(Constant.WRAP_SERVER));
        }
        int arraySize = array.size();
        JSONObject json = null;
        JSONObject vmJson = null;

        for (int i = 0; i < arraySize; i++) {
            json = array.getJSONObject(i);
            vmJson = new JSONObject();
            vmJson.put("id", json.getString("id"));
            vmJson.put("status", (json.getString("status")).toLowerCase());
            vmJson.put("hostId", json.getString("hostId"));
            vmJson.put("name", json.getString("name"));
            vmJson.put("tenantId", json.getString("tenant_id"));
            vmJson.put("image", json.getJSONObject("image").getString("id"));
            vmJson.put("metadata", json.getJSONObject("metadata").toString());
            vmJson.put("vimId", vimId);
            vmJson.put("vimName", vimName);
            list.add(vmJson);
        }
        return list;
    }
}
