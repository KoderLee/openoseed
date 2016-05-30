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

package org.openo.nfvo.vimadapter.openstack.networkmgr;

import java.io.IOException;
import java.util.Map;

import org.openo.baseservice.log.OssLog;
import org.openo.baseservice.log.OssLogFactory;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.openo.nfvo.vimadapter.util.StringUtil;
import org.openo.nfvo.vimadapter.util.constant.Constant;
import org.openo.nfvo.vimadapter.util.constant.UrlConstant;
import org.openo.nfvo.vimadapter.util.http.HttpRequests;
import org.openo.nfvo.vimadapter.util.http.LoginException;
import org.openo.nfvo.vimadapter.util.http.ResultUtil;
import org.openo.nfvo.vimadapter.openstack.api.ConnectInfo;
import org.openo.nfvo.vimadapter.openstack.connectmgr.ConnectionMgr;
import org.openo.nfvo.vimadapter.openstack.connectmgr.OpenstackConnection;

/**
 * 
* The sub network operation on openstack, create/get/delete network<br/>
* <p>
* </p>
* 
* @author
* @version NFVO 0.5 May 15, 2016
 */
public class OpenstackSubNet {
    private static final OssLog LOG = OssLogFactory
            .getLogger(OpenstackSubNet.class);

    private ConnectInfo conInfo;

    private OpenstackConnection con;

    public OpenstackSubNet(Map<String, String> conInfoMap) {
        conInfo = new ConnectInfo(conInfoMap);
    }

    public JSONObject createSubNet(JSONObject network) {
        HttpMethod httpMethod = null;
        JSONObject resultObj = new JSONObject();

        try {
            con = ConnectionMgr.getConnectionMgr().getConnection(conInfo);
            LOG.debug("function=createSubNet: url->"
                    + con.getServiceUrl(Constant.ServiceName.NEUTRON));

            String reqBody = setSubNetBody(network);

            httpMethod = new HttpRequests.Builder(conInfo.getAuthenticateMode())
                    .addHeader(Constant.HEADER_AUTH_TOKEN,
                            con.getDomainTokens())
                    .setUrl(String.format(UrlConstant.POST_VSUBNETWORK,
                            con.getServiceUrl(Constant.ServiceName.NEUTRON)))
                    .setParams(reqBody).post().execute();

            String result = ResultUtil.getResponseBody(httpMethod
                    .getResponseBody());
            LOG.warn("function=createSubNet, msg -> status={}, result={}.",
                    httpMethod.getStatusCode(), result);

            if (httpMethod.getStatusCode() == HttpStatus.SC_OK
                    || httpMethod.getStatusCode() == HttpStatus.SC_CREATED) {
                JSONObject ceateResultObj = JSONObject.fromObject(result);
                resultObj.put("retCode", Constant.REST_SUCCESS);
                resultObj.put("subnet", getSubNetBody(ceateResultObj, network));
                return resultObj;
            } else {
                LOG.error(
                        "function=createSubNet, msg=createSubNet fail, retcode={}",
                        httpMethod.getStatusCode());
                return getSubnetNum(network);
            }
        } catch (LoginException e) {
            LOG.error(
                    "function=createSubNet, msg=OpenStackLoginException occurs. e={}",
                    e);
            resultObj.put("reason", "OpenStackLoginException");
        } catch (IOException e) {
            LOG.error("function=createSubNet, msg=IOException occurs e={}",
                    e);
            resultObj.put("reason", "IOException");
        } finally {
            if (httpMethod != null) {
                httpMethod.releaseConnection();
            }
        }
        resultObj.put("retCode", Constant.REST_FAIL);
        return resultObj;
    }

    private JSONObject getSubnetNum(JSONObject network) {
        HttpMethod httpMethod = null;
        JSONObject resultObj = new JSONObject();

        try {
            httpMethod = new HttpRequests.Builder(conInfo.getAuthenticateMode())
                    .addHeader(Constant.HEADER_AUTH_TOKEN,
                            con.getDomainTokens())
                    .setUrl(String.format(UrlConstant.GET_NETWORK_8ID_NEUTRON,
                            con.getServiceUrl(Constant.ServiceName.NEUTRON),
                            network.getString("backendId"))).get().execute();

            if (httpMethod.getStatusCode() == HttpStatus.SC_OK) {
                String result = ResultUtil.getResponseBody(httpMethod
                        .getResponseBody());

                JSONObject resObj = JSONObject.fromObject(result);
                JSONObject networkJson = resObj.getJSONObject("network");

                JSONArray subnetJson = networkJson.getJSONArray("subnets");
                if (subnetJson.isEmpty()) {
                    resultObj
                            .put("networkStatue", Constant.DELETE_FAIL_NETWORK);
                    resultObj.put("retCode", Constant.REST_FAIL);
                    return resultObj;
                }
            }
            resultObj.put("networkStatue", Constant.MATAIN_CONFLICT_NETWORK);
            resultObj.put("reason", "Create subnet OpenStack return fail.");

        } catch (LoginException e) {
            LOG.error(
                    "function=getSubnetNum, msg=occurs a OpenStackException. e={}",
                    e);
            resultObj.put("networkStatue", Constant.DELETE_FAIL_NETWORK);
            resultObj.put("reason", "OpenStackException");
        } catch (IOException e) {
            LOG.error("function=getSubnetNum, msg=IOException occurs e={}",
                    e);
            resultObj.put("networkStatue", Constant.DELETE_FAIL_NETWORK);
            resultObj.put("reason", "IOException");
        } finally {
            if (httpMethod != null) {
                httpMethod.releaseConnection();
            }
        }
        resultObj.put("retCode", Constant.REST_FAIL);
        return resultObj;

    }

    public int deleteSubNet(String id) {
        HttpMethod httpMethod = null;
        try {
            con = ConnectionMgr.getConnectionMgr().getConnection(conInfo);

            httpMethod = new HttpRequests.Builder(conInfo.getAuthenticateMode())
                    .addHeader(Constant.HEADER_AUTH_TOKEN,
                            con.getDomainTokens())
                    .setUrl(String.format(UrlConstant.DEL_VSUBNETWORK,
                            con.getServiceUrl(Constant.ServiceName.NEUTRON), id))
                    .delete().execute();

            String result = ResultUtil.getResponseBody(httpMethod
                    .getResponseBody());
            LOG.error("function=removeNetwork, msg -> status={}, result={}.",
                    httpMethod.getStatusCode(), result);
            return httpMethod.getStatusCode();
        } catch (LoginException e) {
            LOG.error(
                    "function=deleteSubNet, msg=OpenStackLoginException occurs. e={}",
                    e);
        } catch (IOException e) {
            LOG.error("function=deleteSubNet, msg=IOException occurs e={}",
                    e);
        } finally {
            if (httpMethod != null) {
                httpMethod.releaseConnection();
            }
        }

        return Constant.INTERNAL_EXCEPTION;
    }

    private String getSubNetBody(JSONObject ceateResultObj, JSONObject network) {
        JSONObject subObj = ceateResultObj.getJSONObject("subnet");
        JSONObject reqBody = new JSONObject();
        reqBody.put("name", subObj.get("name"));
        reqBody.put("allocPools", subObj.get("allocation_pools"));
        reqBody.put("hostRoutes", subObj.get("host_routes"));
        reqBody.put("cidr", subObj.get("cidr"));

        reqBody.put("ipVersion", subObj.get("ip_version"));
        reqBody.put("gatewayIp", subObj.get("gateway_ip"));

        reqBody.put("backendId", subObj.get("id"));
        reqBody.put("dnsServer", subObj.get("dns_nameservers"));
        reqBody.put("enableDhcp", network.getString("enableDhcp"));
        reqBody.put("id", network.getString("id"));
        reqBody.put("vimVendorId", network.getString("vimVendorId"));
        reqBody.put("networkId", network.getString("backendId"));
        reqBody.put("rpId", network.getString("rpId"));
        reqBody.put("rpName", network.getString("rpName"));
        reqBody.put("vimId", network.getString("vimId"));

        return reqBody.toString();
    }

    private String setSubNetBody(JSONObject network) {
        JSONObject reqBody = new JSONObject();

        reqBody.put("name", network.getString("name") + "_subnet");
        reqBody.put("network_id", network.getString("backendId"));
        reqBody.put("tenant_id", network.getString("vimVendorId"));
        reqBody.put("ip_version", "4");
        reqBody.put("enable_dhcp", network.getString("enableDhcp"));
        reqBody.put("gateway_ip", network.getString("subGate"));

        String[] ipList = network.getString("subnets").split("-");
        JSONObject pool = new JSONObject();
        pool.put("start", ipList[0]);
        pool.put("end", ipList[1]);

        JSONArray pools = new JSONArray();
        pools.add(pool);
        reqBody.put("allocation_pools", pools);

        reqBody.put("cidr",
                StringUtil.getCidr(ipList[0], network.getString("subMask")));
        JSONObject mesBody = new JSONObject();
        mesBody.put("subnet", reqBody);
        return mesBody.toString();
    }
}
