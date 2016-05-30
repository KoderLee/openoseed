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
* Query the vendors info from VIM<br/>
* <p>
* </p>
* 
* @author
* @version NFVO 0.5 May 15, 2016
 */
public class VendorQuery {
    private static final OssLog LOG = OssLogFactory
            .getLogger(VendorQuery.class);

    private ConnectInfo connect;

    private String vimId;

    private String vimName;

    public VendorQuery(Map<String, String> conMap) {
        connect = new ConnectInfo(conMap);
        vimId = conMap.get("vimId");
        vimName = conMap.get("vimName");
    }

    /**
     * Get the vendors from vim<br/>
     * 
     * @return the vendors data to be queried
     * @since NFVO 0.5
     */
    public List<JSONObject> getVendors() {
        String result = null;
        try {
            OpenstackConnection con = ConnectionMgr.getConnectionMgr()
                    .getConnection(connect);

            result = new HttpRequests.Builder(connect.getAuthenticateMode())
                    .addHeader(Constant.HEADER_AUTH_TOKEN,
                            con.getDomainTokens())
                    .setUrl(String.format(UrlConstant.GET_VENDOR,
                            con.getServiceUrl(Constant.ServiceName.KEYSTONE)))
                    .get().request();

            LOG.warn("function = getVendors result: " + result);
            JSONObject vendorObj = JSONObject.fromObject(result);

            if (vendorObj.containsKey(Constant.WRAP_TENANTS)) {
                return getVendorsMap(vendorObj);
            }
        } catch (LoginException e) {
            LOG.error("function=getVendors, msg=get from fs OpenStackLoginException,info:"
                    + e);
        }
        return null;
    }

    private List<JSONObject> getVendorsMap(JSONObject vendorObj) {
        JSONArray tenantsArray = vendorObj.getJSONArray(Constant.WRAP_TENANTS);
        List<JSONObject> list = new ArrayList<JSONObject>(
                Constant.DEFAULT_COLLECTION_SIZE);
        int tenantSize = tenantsArray.size();
        JSONObject tenantsObject = null;

        for (int index = 0; index < tenantSize; index++) {
            tenantsObject = tenantsArray.getJSONObject(index);
            if (!Constant.TENANTS_NAME_LIST.contains(tenantsObject
                    .getString("name"))) {
                JSONObject tenantsMap = new JSONObject();
                tenantsMap.put("description",
                        tenantsObject.getString("description"));
                tenantsMap.put("id", tenantsObject.getString("id"));
                tenantsMap.put("name", tenantsObject.getString("name"));
                String status = Boolean.parseBoolean(tenantsObject
                        .getString("enabled")) ? Constant.ACTIVE
                        : Constant.INACTIVE;
                tenantsMap.put("status", status);
                tenantsMap.put("vimId", vimId);
                tenantsMap.put("vimName", vimName);
                list.add(tenantsMap);
            }
        }
        return list;
    }
}
