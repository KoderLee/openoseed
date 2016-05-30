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

package org.openo.nfvo.vimadapter.service.adapter;

import java.util.Map;

import org.openo.baseservice.roa.common.HttpContext;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.openo.nfvo.vimadapter.util.InterfaceNetworkMgr;
import org.openo.nfvo.vimadapter.util.RestfulUtil;
import org.openo.nfvo.vimadapter.util.StringUtil;
import org.openo.nfvo.vimadapter.util.constant.Constant;
import org.openo.nfvo.vimadapter.util.constant.UrlConstant;
import org.openo.nfvo.vimadapter.openstack.entry.NetworkMgrOpenstack;
import org.openo.nfvo.vimadapter.service.util.AdapterUtil;

import org.openo.baseservice.log.OssLog;
import org.openo.baseservice.log.OssLogFactory;
import org.openo.baseservice.roa.util.restclient.RestfulParametes;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;

/**
 * 
* Used for adapte different VIM, supply same rest call for upper layer to create/update/delete sub network<br/>
* <p>
* </p>
* 
* @author
* @version NFVO 0.5 May 15, 2016
 */
public final class AdapterSubnetworkManager {
    private InterfaceNetworkMgr networkMgr = null;
    private static AdapterSubnetworkManager conSingleton = null;
    private final static OssLog LOG = OssLogFactory
            .getLogger(AdapterSubnetworkManager.class);

    private AdapterSubnetworkManager(String type) {
        if (Constant.OPENSTACK.equalsIgnoreCase(type)) {
            networkMgr = new NetworkMgrOpenstack();
        }
    }

    public synchronized static AdapterSubnetworkManager getConnectionMgr(
            String type) {
        if (conSingleton == null) {
            conSingleton = new AdapterSubnetworkManager(type);
        }
        return conSingleton;
    }

    public boolean checkSubNetInfo(JSONObject network, String type) {
        if (Constant.OPENSTACK.equalsIgnoreCase(type)) {
            return checkSubNetValid(network);
        }

        return true;
    }
    
    private boolean validSubNetInfoIsNone(String subgate, String submask, String subnet) {
        return StringUtils.isEmpty(subgate) && StringUtils.isEmpty(submask) && StringUtils.isEmpty(subnet);
    }
    
    private boolean validSubNetNotNone(String subgate, String submask, String subnet) {
        return (!StringUtils.isEmpty(subgate)) && (!StringUtils.isEmpty(submask)) && (!StringUtils.isEmpty(subnet));
    }
    
    public boolean checkSubNetValid(JSONObject network) {
        String subgate = network.getString("subGate");
        String submask = network.getString("subMask");
        String subnet = network.getString("subnets");

        boolean subnetInfoIsNone = validSubNetInfoIsNone(subgate, submask, subnet);
        boolean subnetInfoNotNone = validSubNetNotNone(subgate, submask, subnet);;

        if (!(subnetInfoIsNone || subnetInfoNotNone))
        {
            LOG.error(
                            "function=checkSubNetValid.msg=sunet info error, subgate={}, submask={}, subnet={}", 
                            subgate, submask, subnet);
            return false;
        }
        if (subnetInfoIsNone)
        {
            return true;
        }

        String[] ipList = subnet.split("-");

        if (!validIp(ipList[0], ipList[1], subgate, submask)) {
            LOG.error(
                    "function=checkSubNetValid.msg=subnet is not ip, subgate={}, submask={}, subnet={}",
                    subgate, submask, subnet);
            return false;
        }

        long startIp = StringUtil.ipToLong(ipList[0]);
        long endIp = StringUtil.ipToLong(ipList[1]);

        if (startIp > endIp) {
            LOG.error(
                    "function=checkSubNetValid.msg=subnet startIp > endIp  startIp={}, endIp={}",
                    ipList[0], ipList[1]);
            return false;
        }

        long mask = StringUtil.ipToLong(submask);
        if (validMask(mask, startIp, endId)) {
            LOG.error(
                    "function=checkSubNetValid.msg=subnet, startIp={}, endIp={}",
                    ipList[0], ipList[1]);
            return false;
        }

        long gate = StringUtil.ipToLong(subgate);
        if (validGate(mask, startIp, endId, gate)) {
            LOG.error(
                    "function=checkSubNetValid.msg=subnet, sIp={}, eIp={}, subgate={}",
                    ipList[0], ipList[1], subgate);
            return false;
        }

        return true;
    }
    
    private boolean validIp(String ip1, String ip2, String subgate, String submask) {
        return StringUtil.isboolIp(ip1) && StringUtil.isboolIp(ip2)
                && StringUtil.isboolIp(subgate) && StringUtil.isboolIp(submask);
    }
    
    private boolean validGate(String mask, String startIp, String endId, String gate) {
        return ((gate & mask) != (startIp & mask))
                || ((gate & mask) != (endIp & mask));
    }
    
    private boolean validMask(String mask, String startIp, String endId) {
        return (startIp & mask) != (endIp & mask);
    }
    
    public boolean addSubnet(JSONObject network,
            Map<String, String> conInfoMap, HttpContext context) {
        JSONObject subnetObj = networkMgr.createSubNet(network, conInfoMap);

        if (subnetObj != null && !subnetObj.isEmpty()) {
            if (subnetObj.getInt("retCode") == Constant.REST_SUCCESS) {
                String subnetId = subnetObj.getJSONObject("subnet").getString(
                        "backendId");
                RestfulParametes para = new RestfulParametes();
                para.setRawData(subnetObj.getJSONObject("subnet").toString());

                RestfulResponse resmgrAddResponse = RestfulUtil
                        .getResponseResult(UrlConstant.RESMGR_VSUBNETWORK,
                                para, RestfulUtil.TYPE_ADD, context);

                if (AdapterUtil.getResponseFromResmgr(resmgrAddResponse)) {
                    network.put("subnetId", subnetId);
                    return true;
                } else {
                    int ret = networkMgr.deleteSubNet(subnetId, conInfoMap);
                    LOG.error(
                            "function=addSubnet, msg=add subnetwork db fail, add db fail, rmv fs subnet ret={}",
                            ret);
                }
            } else if (Constant.DELETE_FAIL_NETWORK.equalsIgnoreCase(subnetObj
                    .getString("networkStatue"))) {
                LOG.error("function=addSubnet, msg=add subnet fail,the network has no other subnet,delete network. ");
            }
        }
        return false;
    }

    public boolean deleteSubnet(RestfulParametes delNetworkId,
            Map<String, String> conInfoMap, HttpContext context) {
        RestfulResponse restfulResponse = RestfulUtil.getResponseResult(
                UrlConstant.RESMGR_VSUBNETWORK, delNetworkId,
                RestfulUtil.TYPE_GET, context);
        if (AdapterUtil.getResponseFromResmgr(restfulResponse)) {
            String subnetBackendId = getSubnetId(restfulResponse);

            if (null == subnetBackendId) {
                LOG.error("function=deleteSubnet, msg=Delete subNetwork backendId: resmgr return subnet not exists");
                return true;
            }

            int delSubnetResult = networkMgr.deleteSubNet(subnetBackendId,
                    conInfoMap);
            return handleDelSubnetResult(delSubnetResult, delNetworkId,
                    context);
        }
        return false;
    }

    private boolean handleDelSubnetResult(int retCode,
            RestfulParametes delNetworkId, HttpContext context) {
        boolean flag = false;

        if (retCode == Constant.HTTP_OK || retCode == Constant.HTTP_NOCONTENT) {
            LOG.warn("function=handleDelSubnetDBResult, msg=deletesubNetwork to CMS success");
            RestfulResponse delDbResponse = RestfulUtil.getResponseResult(
                    UrlConstant.RESMGR_VSUBNETWORK, delNetworkId,
                    RestfulUtil.TYPE_DEL, context);

            if (AdapterUtil.getResponseFromResmgr(delDbResponse)) {
                flag = true;
            }

            LOG.error("delete subnetwork info from db fail.status="
                    + delDbResponse.getStatus());
        }

        LOG.error(
                "funtion=handleDelSubnetDBResult, msg=send to fs https request fail, status={}",
                retCode);
        return flag;
    }

    private String getSubnetId(RestfulResponse restfulResponse) {
        JSONArray dataList = JSONObject.fromObject(
                restfulResponse.getResponseContent()).getJSONArray("data");

        return dataList.isEmpty() ? null : dataList.getJSONObject(0).getString(
                "backendId");
    }

    public boolean getSubnetId(JSONObject network,
            Map<String, String> conInfoMap, String type, HttpContext context) {
        boolean flag = true;

        if (StringUtils.isEmpty(network.getString("subGate"))
                && StringUtils.isEmpty(network.getString("subMask"))
                && StringUtils.isEmpty(network.getString("subnets"))) {
            LOG.error("function=getSubnetId,msg= subnet info all is none");
            network.put("subnetId", "");
        } else if (!addSubnet(network, conInfoMap, context)) {
            flag = false;
        }
        return flag;
    }
}
