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

package org.openo.nfvo.vimadapter.rest;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.io.IOUtils;
import org.openo.nfvo.vimadapter.util.VimOpResult;
import org.openo.nfvo.vimadapter.util.VimOpResult.TaskStatus;
import org.openo.nfvo.vimadapter.util.constant.Constant;
import org.openo.nfvo.vimadapter.service.adapter.AdapterNetworkManager;

import org.openo.baseservice.i18n.ResourceUtil;
import org.openo.baseservice.log.OssLog;
import org.openo.baseservice.log.OssLogFactory;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.annotation.DEL;
import org.openo.baseservice.roa.annotation.POST;
import org.openo.baseservice.roa.annotation.Path;
import org.openo.baseservice.roa.annotation.PathParam;
import org.openo.baseservice.roa.annotation.Target;
import org.openo.baseservice.roa.common.HttpContext;

/**
 * 
* Used for create/update/delete virtual network, send request to VIM.<br/>
* <p>
* </p>
* 
* @author
* @version NFVO 0.5 May 15, 2016
 */
@Path("/vimadapter/v1/network/vnet")
@Target("vim-vnet")
public class NetworkRoa {
    private final static OssLog LOG = OssLogFactory.getLogger(NetworkRoa.class);

    private JSONObject restJson = new JSONObject();

    private AdapterNetworkManager adapter;

    public void setAdapter(AdapterNetworkManager adapter) {
        this.adapter = adapter;
    }

    @POST
    /**
     * create a virtual network in vim.<br/>
     * 
     * @param context The necessary info of create a network.
     * @return The info about virtual network that had been created.
     * @throws ServiceException common exception.
     * @since  NFVO 0.5
     */
    public String addNetwork(HttpContext context) throws ServiceException {
        HttpServletRequest req = context.getHttpServletRequest();
        String data = null;

        try {
            InputStream input = req.getInputStream();
            data = IOUtils.toString(input);

            JSONObject subJsonObject = JSONObject.fromObject(data);
            JSONObject networkJsonObject = subJsonObject
                    .getJSONObject("NETWORK");

            if (networkJsonObject == null || networkJsonObject.isEmpty()) {
                restJson.put("retCode", Constant.REST_FAIL);
                restJson.put(
                        "data",
                        ResourceUtil
                                .getMessage("org.openo.nfvo.vimadapter.service.param.insufficient"));
                return restJson.toString();
            }

            VimOpResult vimOpResult = adapter.addNetwork(networkJsonObject,
                    context);

            if (vimOpResult.gotOperateStatus() == TaskStatus.SUCCESS) {
                restJson.put("retCode", Constant.REST_SUCCESS);
                restJson.put("data", networkJsonObject);
                JSONObject resObj = (JSONObject) vimOpResult.gotResult().get(0);
                networkJsonObject.put("backendId",
                        resObj.getString("backendId"));
                networkJsonObject.put("subnetId", resObj.getString("subnetId"));
            } else {
                restJson.put("retCode", Constant.REST_FAIL);
                restJson.put("data", vimOpResult.gotErrorMessage());
            }
        } catch (IOException e) {
            restJson.put("retCode", Constant.REST_FAIL);
            LOG.error("function=addNetwork,msg=IOException" + e);
            restJson.put(
                    "data",
                    ResourceUtil
                            .getMessage("org.openo.nfvo.vimadapter.service.parsejson.exception"));
        }
        return restJson.toString();
    }

    @DEL
    @Path("/{networkId}")
    /**
     * delete a virtual network create by NFVO from vim.<br/>
     * 
     * @param context 
     *      httpContext
     * @param identifier of the network to be deleted.
     * @return Status of deleting network.
     * @throws ServiceException common exception of the service.
     * @since  NFVO 0.5
     */
    public String delNetwork(HttpContext context,
            @PathParam("networkId") String networkId) throws ServiceException {
        VimOpResult vimOpResult = adapter.deleteNetwork(networkId, context);

        if (vimOpResult.gotOperateStatus() == TaskStatus.SUCCESS) {
            restJson.put("retCode", Constant.REST_SUCCESS);
            restJson.put(
                    "data",
                    ResourceUtil
                            .getMessage("org.openo.nfvo.vimadapter.service.del.network.succ"));
        } else {
            restJson.put("retCode", Constant.REST_FAIL);
            restJson.put("data", vimOpResult.gotErrorMessage());
        }
        return restJson.toString();
    }
}
