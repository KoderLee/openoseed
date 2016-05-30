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
* Query the resource pool info from VIM<br/>
* <p>
* </p>
* 
* @author
* @version NFVO 0.5 May 15, 2016
 */
public class RpQuery {
    private static final OssLog LOG = OssLogFactory.getLogger(RpQuery.class);

    private ConnectInfo connect;

    private String vimId;

    private String vimName;

    public RpQuery(Map<String, String> conMap) {
        connect = new ConnectInfo(conMap);
        vimId = conMap.get("vimId");
        vimName = conMap.get("vimName");
    }

    /**
     * Get the rp from vim<br/>
     * 
     * @return the rp data to be queried
     * @since NFVO 0.5
     */
    public List<JSONObject> getRps() {
        try {
            OpenstackConnection con = ConnectionMgr.getConnectionMgr()
                    .getConnection(connect);
            LOG.debug("function=getRps: url->"
                    + con.getServiceUrl(Constant.ServiceName.NOVA));
            String rpStatus = getRpStatus(connect, con);

            String result = new HttpRequests.Builder(
                    connect.getAuthenticateMode())
                    .addHeader(Constant.HEADER_AUTH_TOKEN,
                            con.getDomainTokens())
                    .setUrl(String.format(UrlConstant.GET_RP,
                            con.getServiceUrl(Constant.ServiceName.NOVA),
                            con.getProjectId())).get().request();

            LOG.warn("function=getRps, result: " + result);
            JSONObject rpObj = JSONObject.fromObject(result);
            if (rpObj.containsKey(Constant.WRAP_HYPERVISOR_STATS)) {
                return getRpMap(rpObj, con, rpStatus);
            }
        } catch (LoginException e) {
            LOG.error("function=getRps, msg=LoginException, info:"
                    + e);
        }

        return null;
    }

    private List<JSONObject> getRpMap(JSONObject rpObj,
            OpenstackConnection con, String rpStatus) {
        JSONObject hypervisorObj = rpObj
                .getJSONObject(Constant.WRAP_HYPERVISOR_STATS);
        JSONObject totalJson = new JSONObject();
        totalJson.put("vcpus", hypervisorObj.getString("vcpus"));
        totalJson.put("cpumhz", String.valueOf(Integer.parseInt(hypervisorObj
                .getString("vcpus")) * Constant.CPUMHZ));
        totalJson.put("memory", hypervisorObj.getString("memory_mb"));
        totalJson.put("disk", hypervisorObj.getString("local_gb"));
        JSONObject usedJson = new JSONObject();
        usedJson.put("vcpus", hypervisorObj.getString("vcpus_used"));
        usedJson.put("cpumhz", String.valueOf(Integer.parseInt(hypervisorObj
                .getString("vcpus_used")) * Constant.CPUMHZ));
        usedJson.put("memory", hypervisorObj.getString("memory_mb_used"));
        usedJson.put("disk", hypervisorObj.getString("local_gb_used"));
        JSONObject resultJson = new JSONObject();
        resultJson.put("total", totalJson);
        resultJson.put("used", usedJson);

        String[] rpSplit = con.getServiceUrl(Constant.ServiceName.NOVA).split(
                "[.]");
        String rpName = null;
        if (rpSplit.length >= Constant.OPENSTACK_NOVAURL_MIN_LENTH) {
            rpName = rpSplit[1] + '.' + rpSplit[2];
        }

        else {
            LOG.error("function=getRpMap, msg=get openstack rp name failed.");
            return null;
        }

        resultJson.put("name", rpName);
        resultJson.put("id", vimId + '-' + rpName);
        resultJson.put("tenantName", "admin");
        resultJson.put("tenantId", con.getProjectId());
        resultJson.put("status", rpStatus);
        resultJson.put("vimId", vimId);
        resultJson.put("vimName", vimName);
        List<JSONObject> list = new ArrayList<JSONObject>(
                Constant.DEFAULT_COLLECTION_SIZE);
        list.add(resultJson);
        return list;
    }

    private String getRpStatus(ConnectInfo conInfo, OpenstackConnection con) {
        String rpStatus = Constant.INACTIVE;
        try {
            String hostResult = new HttpRequests.Builder(
                    conInfo.getAuthenticateMode())
                    .addHeader(Constant.HEADER_AUTH_TOKEN,
                            con.getDomainTokens())
                    .setUrl(String.format(UrlConstant.GET_HOSTS,
                            con.getServiceUrl(Constant.ServiceName.CPS))).get()
                    .request();

            JSONObject hostObj = JSONObject.fromObject(hostResult);

            if (hostObj.containsKey(Constant.WRAP_HOSTS)) {
                JSONArray array = hostObj.getJSONArray(Constant.WRAP_HOSTS);
                JSONObject jo = null;
                int arraySize = array.size();
                for (int i = 0; i < arraySize; i++) {
                    jo = array.getJSONObject(i);
                    if ("normal".equals(jo.getString("status"))) {
                        rpStatus = Constant.ACTIVE;
                        break;
                    }
                }
            }
        } catch (LoginException e) {
            LOG.error("function=getRps, msg=LoginException, info:"
                    + e);
        }
        return rpStatus;
    }
}
