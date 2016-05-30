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
* The network operation on openstack, create/get/delete network<br/>
* <p>
* </p>
* 
* @author
* @version NFVO 0.5 May 15, 2016
 */
public class OpenstackNetwork {
    private static final OssLog LOG = OssLogFactory
            .getLogger(OpenstackNetwork.class);

    private ConnectInfo conInfo;

    private OpenstackConnection con = null;

    public OpenstackNetwork(Map<String, String> conInfoMap) {
        conInfo = new ConnectInfo(conInfoMap);
    }

    public JSONObject createNetwork(JSONObject network) {
        HttpMethod httpMethod = null;
        JSONObject resultObj = new JSONObject();
        try {
            con = ConnectionMgr.getConnectionMgr().getConnection(conInfo);
            LOG.warn("function=createNetwork: url->"
                    + con.getServiceUrl(Constant.ServiceName.NEUTRON));

            String reqBody = getNetworkBody(network);

            httpMethod = new HttpRequests.Builder(conInfo.getAuthenticateMode())
                    .addHeader(Constant.HEADER_AUTH_TOKEN,
                            con.getDomainTokens())
                    .setUrl(String.format(UrlConstant.POST_VNETWORK,
                            con.getServiceUrl(Constant.ServiceName.NEUTRON)))
                    .setParams(reqBody).post().execute();

            String resultCreate = ResultUtil.getResponseBody(httpMethod
                    .getResponseBody());

            if (httpMethod.getStatusCode() == HttpStatus.SC_OK
                    || httpMethod.getStatusCode() == HttpStatus.SC_CREATED) {
                LOG.warn("function=createNetwork, msg= status={}, result={}.",
                        httpMethod.getStatusCode(), resultCreate);
                JSONObject ceateResultObj = JSONObject.fromObject(resultCreate);
                JSONObject subresultObj = ceateResultObj
                        .getJSONObject("network");
                resultObj.put("retCode", Constant.REST_SUCCESS);
                resultObj.put("backendId", subresultObj.getString("id"));
                return resultObj;
            } else if (httpMethod.getStatusCode() == HttpStatus.SC_CONFLICT) {
                return getNetworkFromFs(network);
            } else {
                LOG.error(
                        "function=createNetwork, msg=OpenStack return fail,status={}.",
                        httpMethod.getStatusCode());
                resultObj.put("reason",
                        "Create network OpenStack return fail.");
            }
        } catch (IOException e) {
            LOG.error("function=createNetwork, msg=IOException occurs. e={}",
                    e);
            resultObj.put("reason", "IOException");
        } catch (LoginException e) {
            LOG.error(
                    "function=createNetwork, msg=OpenStackLoginException occurs. e={}",
                    e);
            resultObj.put("reason", "OpenStackLoginException");
        } finally {
            if (httpMethod != null) {
                httpMethod.releaseConnection();
            }
        }
        resultObj.put("retCode", Constant.REST_FAIL);
        return resultObj;
    }

    private JSONObject getNetworkFromFs(JSONObject network) {
        HttpMethod httpMethod = null;

        JSONObject resultObj = new JSONObject();

        String netType = network.getString("type");

        try {
            if (("vlan").equals(netType)) {
                httpMethod = new HttpRequests.Builder(
                        conInfo.getAuthenticateMode())
                        .addHeader(Constant.HEADER_AUTH_TOKEN,
                                con.getDomainTokens())
                        .setUrl(String.format(
                                UrlConstant.GET_VNETWORK_FORM_FS_VLAN,
                                con.getServiceUrl(Constant.ServiceName.NEUTRON),
                                network.getString("segmentation"),
                                network.getString("physicalNet"))).get()
                        .execute();

                String resultVlanGet = ResultUtil.getResponseBody(httpMethod
                        .getResponseBody());

                LOG.warn(
                        "function=createNetwork conflict, msg -> status={}, resultGet={}.",
                        httpMethod.getStatusCode(), resultVlanGet);

                if (httpMethod.getStatusCode() == HttpStatus.SC_OK
                        || httpMethod.getStatusCode() == HttpStatus.SC_CREATED) {
                    JSONObject getVlanResultObj = JSONObject
                            .fromObject(resultVlanGet);
                    JSONArray networkVlanArray = getVlanResultObj
                            .getJSONArray("networks");
                    if (!networkVlanArray.isEmpty()) {
                        resultObj.put("retCode", Constant.REST_SUCCESS);
                        resultObj.put("backendId", networkVlanArray
                                .getJSONObject(0).getString("id"));
                    }
                }
            } else if (("flat").equals(netType)) {
                httpMethod = new HttpRequests.Builder(
                        conInfo.getAuthenticateMode())
                        .addHeader(Constant.HEADER_AUTH_TOKEN,
                                con.getDomainTokens())
                        .setUrl(String.format(
                                UrlConstant.GET_VNETWORK_FORM_FS_FLAT,
                                con.getServiceUrl(Constant.ServiceName.NEUTRON),
                                network.getString("physicalNet"))).get()
                        .execute();

                String resultFlatGet = ResultUtil.getResponseBody(httpMethod
                        .getResponseBody());
                LOG.warn(
                        "function=createNetwork conflict, msg -> status={}, resultGet={}.",
                        httpMethod.getStatusCode(), resultFlatGet);

                if (httpMethod.getStatusCode() == HttpStatus.SC_OK
                        || httpMethod.getStatusCode() == HttpStatus.SC_CREATED) {
                    JSONObject getFlatResultObj = JSONObject
                            .fromObject(resultFlatGet);
                    JSONArray networkFlatArray = getFlatResultObj
                            .getJSONArray("networks");
                    if (!networkFlatArray.isEmpty()) {
                        resultObj.put("retCode", Constant.REST_SUCCESS);
                        resultObj.put("backendId", networkFlatArray
                                .getJSONObject(0).getString("id"));
                    }
                }

            }
        } catch (LoginException e) {
            LOG.error(
                    "function=createNetwork, msg=occurs a OpenStackException. e={}",
                    e);
            resultObj.put("reason", "OpenStackException");
            resultObj.put("retCode", Constant.REST_FAIL);
        } catch (IOException e) {
            LOG.error("function=createNetwork, msg=IOException occurs e={}",
                    e);
            resultObj.put("reason", "IOException");
            resultObj.put("retCode", Constant.REST_FAIL);
        } finally {
            if (httpMethod != null) {
                httpMethod.releaseConnection();
            }
        }
        return resultObj;

    }

    public int removeNetwork(JSONObject network) {
        HttpMethod httpMethod = null;
        try {
            con = ConnectionMgr.getConnectionMgr().getConnection(conInfo);

            httpMethod = new HttpRequests.Builder(conInfo.getAuthenticateMode())
                    .addHeader(Constant.HEADER_AUTH_TOKEN,
                            con.getDomainTokens())
                    .setUrl(String.format(UrlConstant.DEL_VNETWORK,
                            con.getServiceUrl(Constant.ServiceName.NEUTRON),
                            network.getString("backendId"))).delete().execute();

            String result = ResultUtil.getResponseBody(httpMethod
                    .getResponseBody());

            if (httpMethod.getStatusCode() == HttpStatus.SC_OK
                    || httpMethod.getStatusCode() == HttpStatus.SC_NO_CONTENT
                    || httpMethod.getStatusCode() == HttpStatus.SC_CONFLICT
                    || httpMethod.getStatusCode() == HttpStatus.SC_NOT_FOUND) {
                LOG.warn(
                        "function=removeNetwork, msg -> status={}, result={}.",
                        httpMethod.getStatusCode(), result);
                return httpMethod.getStatusCode();
            } else {
                LOG.error(
                        "function=removeNetwork fail,msg -> status={}, result={}.",
                        httpMethod.getStatusCode(), result);
                return httpMethod.getStatusCode();
            }
        } catch (IOException e) {
            LOG.error("function=removeNetwork, msg=IOException occurs e={}",
                    e);
         } catch (LoginException e) {
            LOG.error(
                    "function=removeNetwork, msg=LoginOpenStackException occurs. e={}",
                    e);
        } finally {
            if (httpMethod != null) {
                httpMethod.releaseConnection();
            }
        }
        return Constant.INTERNAL_EXCEPTION;
    }

    private String getNetworkBody(JSONObject network) {
        JSONObject reqBody = new JSONObject();
        
        String networkType = network.getString("type");

        reqBody.put("provider:physical_network",
                network.getString("physicalNet"));
        reqBody.put("tenant_id", network.getString("vimVendorId"));
        reqBody.put("name", network.getString("name"));

        reqBody.put("provider:network_type", networkType);
        reqBody.put("shared", network.getString("isPublic"));
        if (("vlan").equals(networkType)) {
            reqBody.put("provider:segmentation_id",
                    network.getString("segmentation"));
        }

        JSONObject mesbody = new JSONObject();
        mesbody.put("network", reqBody);

        return mesbody.toString();
    }
}