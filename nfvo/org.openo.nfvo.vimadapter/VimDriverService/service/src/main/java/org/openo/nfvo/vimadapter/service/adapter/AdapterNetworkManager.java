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

import org.openo.nfvo.vimadapter.util.InterfaceNetworkManager;
import org.openo.nfvo.vimadapter.util.InterfaceNetworkMgr;
import org.openo.nfvo.vimadapter.util.RestfulUtil;
import org.openo.nfvo.vimadapter.util.Vim;
import org.openo.nfvo.vimadapter.util.VimOpResult;
import org.openo.nfvo.vimadapter.util.constant.Constant;
import org.openo.nfvo.vimadapter.util.constant.UrlConstant;
import org.openo.nfvo.vimadapter.util.dao.VimDao;
import org.openo.nfvo.vimadapter.openstack.entry.NetworkMgrOpenstack;
import org.openo.nfvo.vimadapter.service.util.AdapterUtil;

import org.openo.baseservice.roa.common.HttpContext;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.openo.baseservice.i18n.ResourceUtil;
import org.openo.baseservice.log.OssLog;
import org.openo.baseservice.log.OssLogFactory;
import org.openo.baseservice.roa.util.restclient.RestfulParametes;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;

/**
 * 
* Used for adapte different VIM, supply same rest call for upper layer to create/update/delete network<br/>
* <p>
* </p>
* 
* @author
* @version NFVO 0.5 May 15, 2016
 */
public class AdapterNetworkManager implements InterfaceNetworkManager {
    private final static OssLog LOG = OssLogFactory
            .getLogger(AdapterNetworkManager.class);

    private VimDao vimDao;

    private Map<String, String> conInfoMap;

    public void setVimDao(VimDao vimDao) {
        this.vimDao = vimDao;
    }

    private InterfaceNetworkMgr createNetworkMgr(String type) {
        InterfaceNetworkMgr networkMgr = null;
        if (Constant.OPENSTACK.equalsIgnoreCase(type)) {
            networkMgr = new NetworkMgrOpenstack();
        } else {
            LOG.error("funtion=createNetworkMgr, msg=invalid VIM type!");
        }
        return networkMgr;
    }

    @Override
    public VimOpResult addNetwork(JSONObject network, HttpContext context) {
        LOG.warn(
                "function=addNetwork, msg=NetworkManager enter to add a network, networkId={}.",
                network.getString("id"));
        VimOpResult result = new VimOpResult();

        if (network.get("vimId") == null) {
            LOG.error("function=addNetwork, msg=NetworkManager add network fail, vimId is none.");
            result.setOperateStatus(VimOpResult.TaskStatus.FAIL);
            result.setErrorMessage(ResourceUtil
                    .getMessage("org.openo.nfvo.vimadapter.service.param.error"));
            return result;
        }

        String vimId = network.getString("vimId");

        Vim vim = vimDao.getVimById(vimId);
        if (vim == null) {
            LOG.error("function=addNetwork,msg=vim not exists.");
            result.setOperateStatus(VimOpResult.TaskStatus.FAIL);
            result.setErrorMessage(ResourceUtil
                    .getMessage("org.openo.nfvo.vimadapter.service.add.network.fail.no.vim"));
            return result;
        }
        conInfoMap = vim.generateConMap(null);

        if((Constant.OPENSTACK.equalsIgnoreCase(vim.getType()))
                && (!AdapterSubnetworkManager.getConnectionMgr(vim.getType()).
                        checkSubNetInfo(network, vim.getType()))) {

            LOG.error("function=checkSubNetValid,subnet information is invalid");
            result.setOperateStatus(VimOpResult.TaskStatus.FAIL);
            result.setErrorMessage(ResourceUtil.getMessage("org.openo.nfvo.vimadapter.service.subnet.param.error"));
            return result;
        }

        if (AdapterUtil.checkAddNetworkData(network)) {
            return checkAddNetwork(vim, network, conInfoMap, context);
        }
        LOG.error("function=addNetwork, msg=NetworkManager add network fail, param error");
        result.setOperateStatus(VimOpResult.TaskStatus.FAIL);
        result.setErrorMessage(ResourceUtil
                .getMessage("org.openo.nfvo.vimadapter.service.param.error"));
        return result;
    }
    
    private VimOpResult checkAddNetwork(Vim vim, JSONObject network, Map<String, 
            String> conInfoMap, HttpContext context) {
        VimOpResult result = new VimOpResult();
        INetworkMgr networkMgr = createNetworkMgr(vim.getType());

        if (networkMgr == null) {
            LOG.error("function=addNetwork,getType is invalid:"
                    + vim.getType());
            result.setOperateStatus(VimOpResult.TaskStatus.FAIL);
            result.setErrorMessage(ResourceUtil
                    .getMessage("org.openo.nfvo.vimadapter.service.subnet.param.error"));
            return result;
        }

        JSONObject netObj = networkMgr.create(network, conInfoMap);

        if (validNetObj(netObj)) {
            LOG.error("function=addNetwork,msg= add network fail, CMS return none");
            result.setOperateStatus(VimOpResult.TaskStatus.FAIL);
            result.setErrorMessage(ResourceUtil
                    .getMessage("org.openo.nfvo.vimadapter.service.add.network.fail"));
            return result;
        }

        if (netObj.getInt("retCode") == Constant.REST_SUCCESS) {
            network.put("backendId", netObj.getString("backendId"));
            network.put("subnetId", "");

            if(validAddNetworkVimType(vim, network, context, conInfoMap)) {
                LOG.error("function=addNetwork,msg=subnetwork add fail,check subnet info.");
                result.setOperateStatus(VimOpResult.TaskStatus.FAIL);
                result.setErrorMessage(ResourceUtil
                        .getMessage("org.openo.nfvo.vimadapter.service.add.subnet.fail"));
                return result;
            }

            netObj.put("subnetId", network.getString("subnetId"));
            return sendMsgToAddNetDataBase(network, vim, context);
        }
        LOG.error(
                "function=addNetwork, msg=NetworkManager add network fail, connect fail, reason={}",
                netObj.getString("reason"));
        result.setOperateStatus(VimOpResult.TaskStatus.FAIL);
        result.setErrorMessage(ResourceUtil
                .getMessage("org.openo.nfvo.vimadapter.service.add.network.fail"));
        return result;
    
    }
    
    private boolean validAddNetworkVimType(Vim vim, JSONObject network, HttpContext context,
            Map<String, String> conInfoMap) {
       return (Constant.OPENSTACK.equalsIgnoreCase(vim.getType()))
        && (!AdapterSubnetworkManager
                .getConnectionMgr(Constant.OPENSTACK).getSubnetId(network,
                conInfoMap, Constant.OPENSTACK, context));
    }
    
    private boolean validNetObj(JSONObject netObj) {
        return netObj == null || netObj.isEmpty();
    }

    private VimOpResult sendMsgToAddNetDataBase(JSONObject network, Vim vim,
            HttpContext context) {
        VimOpResult result = new VimOpResult();

        RestfulParametes params = new RestfulParametes();
        params.setRawData(network.toString());
        RestfulResponse resmgrResponse = RestfulUtil.getResponseResult(
                UrlConstant.RESMGR_VNETWORK, params, RestfulUtil.TYPE_ADD,
                context);

        if (AdapterUtil.getResponseFromResmgr(resmgrResponse)) {
            LOG.warn("function=addNetwork info to resmgr db success");
            result.setOperateStatus(VimOpResult.TaskStatus.SUCCESS);
            result.addResult(network);
            return result;
        } else {
            INetworkMgr networkMgr = createNetworkMgr(vim.getType());

            if (networkMgr == null) {
                LOG.error("function=addNetwork,getType is invalid:"
                        + vim.getType());
                result.setOperateStatus(VimOpResult.TaskStatus.FAIL);
                result.setErrorMessage(ResourceUtil
                        .getMessage("org.openo.nfvo.vimadapter.service.subnet.param.error"));
                return result;
            }
            int ret = networkMgr.remove(network, conInfoMap);
            LOG.error(
                    "function=addNetwork, msg=NetworkManager add network to resmgr fail,rmv network retcode={}",
                    ret);
            result.setOperateStatus(VimOpResult.TaskStatus.FAIL);
            result.setErrorMessage(ResourceUtil
                    .getMessage("org.openo.nfvo.vimadapter.service.add.network.to.db.fail"));
            return result;
        }
    }

    @Override
    public VimOpResult deleteNetwork(String networkDn, HttpContext context) {
        LOG.warn(
                "function=deletenetwork, msg=NetworkManager enter to delete a network, networkId={}.",
                networkDn);
        VimOpResult result = new VimOpResult();

        RestfulParametes networkId = new RestfulParametes();
        networkId.put("id", networkDn);
        RestfulResponse restfulResponse = RestfulUtil.getResponseResult(
                UrlConstant.RESMGR_VNETWORK, networkId, RestfulUtil.TYPE_GET,
                context);

        if (AdapterUtil.getResponseFromResmgr(restfulResponse)) {
            JSONObject networkJson = getNetworkInfo(restfulResponse);

            if (networkJson.isEmpty()) {
                LOG.error(
                        "function=deletenetwork, msg=Delete Network fail: Network not exists, networkId={}",
                        networkDn);
                result.setOperateStatus(VimOpResult.TaskStatus.FAIL);
                result.setErrorMessage(ResourceUtil
                        .getMessage("org.openo.nfvo.vimadapter.service.del.no.network"));
                return result;
            }

            Vim vim = vimDao.getVimById(networkJson.getString("vimId"));

            if (vim == null) {
                LOG.error("function=deleteNetwork,msg=vim not exists.");
                result.setOperateStatus(VimOpResult.TaskStatus.FAIL);
                result.setErrorMessage(ResourceUtil
                        .getMessage("org.openo.nfvo.vimadapter.service.del.network.fail.no.vim"));
                return result;
            }

            conInfoMap = vim.generateConMap(null);

            JSONObject checkVappResult = checkNetworkUsedByApp(networkJson,
                    context);

            if (checkVappResult.getInt("status") == Constant.REST_SUCCESS) {
                return deleteNetworkSuccess(vim, networkId, conInfoMap, context, checkVappResult);
            } else {
                LOG.error("function=deletenetwork, msg=check network is used by vapp fail.");
                result.setOperateStatus(VimOpResult.TaskStatus.FAIL);
                result.setErrorMessage(ResourceUtil
                        .getMessage("org.openo.nfvo.vimadapter.service.del.network.fail"));
                return result;
            }
        } else {
            LOG.error(
                    "function=deletenetwork, msg=get network from resmgr fail, status={}.",
                    restfulResponse.getStatus());
            result.setOperateStatus(VimOpResult.TaskStatus.FAIL);
            result.setErrorMessage(ResourceUtil
                    .getMessage("org.openo.nfvo.vimadapter.service.del.network.fail"));
        }
        return result;
    }
    
    private VimOpResult deleteNetworkSuccess(Vim vim, RestfulParametes networkId, Map<String, 
            String> conInfoMap, HttpContext context, JSONObject checkVappResult) {
        VimOpResult result = new VimOpResult();
        if(Constant.OPENSTACK.equals(vim.getType())) {
            AdapterSubnetworkManager.getConnectionMgr(vim.getType()).deleteSubnet(networkId, conInfoMap,
                    context);
        }

        if(checkVappResult.getBoolean("isUsed")) {
            LOG.warn("network is uesd by other vapp, delete the network info from DB only.");
        } else {
            INetworkMgr networkMgr = createNetworkMgr(vim.getType());

            if(networkMgr == null) {
                LOG.error("function=deleteNetwork,getType is invalid:" + vim.getType());
                result.setOperateStatus(VimOpResult.TaskStatus.FAIL);
                result.setErrorMessage(ResourceUtil
                        .getMessage("org.openo.nfvo.vimadapter.service.subnet.param.error"));
                return result;
            }

            int retCode = networkMgr.remove(networkJson, conInfoMap);

            if(!(retCode == Constant.HTTP_OK || retCode == Constant.HTTP_NOCONTENT
                    || retCode == Constant.HTTP_CONFLICT)) {
                LOG.error("function=deletenetwork, msg=deleteNetwork fail.");
                result.setOperateStatus(VimOpResult.TaskStatus.FAIL);
                result.setErrorMessage(ResourceUtil
                        .getMessage("org.openo.nfvo.vimadapter.service.del.network.fail"));
                return result;
            }
        }
        return sendMsgToDelNetDataBase(networkId, context);
    }

    private JSONObject getNetworkInfo(RestfulResponse restfulResponse) {
        JSONObject resultJson = new JSONObject();
        JSONArray dataList = JSONObject.fromObject(
                restfulResponse.getResponseContent()).getJSONArray("data");
        if (!dataList.isEmpty()) {
            JSONObject networkJson = dataList.getJSONObject(0);
            if (!networkJson.isEmpty()) {
                resultJson = networkJson;
            }
        }

        LOG.error("function=getNetworkInfo, msg=get Network info: resmgr return wrong data");
        return resultJson;
    }

    private JSONObject checkNetworkUsedByApp(JSONObject networkJson,
            HttpContext context) {
        JSONObject resultJson = new JSONObject();
        RestfulParametes params = new RestfulParametes();
        params.put("backendId", networkJson.getString("backendId"));
        RestfulResponse sameBackendidResponse = RestfulUtil.getResponseResult(
                UrlConstant.RESMGR_VNETWORK, params, RestfulUtil.TYPE_GET,
                context);

        if (AdapterUtil.getResponseFromResmgr(sameBackendidResponse)) {
            JSONArray sameBackendidList = JSONObject.fromObject(
                    sameBackendidResponse.getResponseContent()).getJSONArray(
                    "data");

            if (sameBackendidList.size() > 1) {
                resultJson.put("status", Constant.REST_SUCCESS);
                resultJson.put("isUsed", true);
            } else {
                resultJson.put("status", Constant.REST_SUCCESS);
                resultJson.put("isUsed", false);
            }
        } else {
            resultJson.put("status", Constant.REST_FAIL);
        }
        return resultJson;
    }

    private VimOpResult sendMsgToDelNetDataBase(RestfulParametes params,
            HttpContext context) {
        VimOpResult result = new VimOpResult();

        RestfulResponse delDbResponse = RestfulUtil.getResponseResult(
                UrlConstant.RESMGR_VNETWORK, params, RestfulUtil.TYPE_DEL,
                context);

        if (AdapterUtil.getResponseFromResmgr(delDbResponse)) {
            LOG.warn("delete the network info from resmgr db success");
            result.setOperateStatus(VimOpResult.TaskStatus.SUCCESS);
            result.addResult(ResourceUtil
                    .getMessage("org.openo.nfvo.vimadapter.service.del.network.succ"));
        } else {
            LOG.error("delete the network info from db fail.status={}",
                    delDbResponse.getStatus());
            result.setOperateStatus(VimOpResult.TaskStatus.FAIL);
            result.setErrorMessage(ResourceUtil
                    .getMessage("org.openo.nfvo.vimadapter.service.del.network.from.db.fail"));
        }
        return result;
    }

}
