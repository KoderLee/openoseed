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

package org.openo.nfvo.nsservice.simpleservice.services.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.commons.io.IOUtils;
import org.openo.nfvo.nsservice.simpleservice.services.api.NetworkService;
import org.openo.nfvo.nsservice.simpleservice.constant.Constant;
import org.openo.nfvo.nsservice.simpleservice.database.dao.api.NsServiceDao;
import org.openo.nfvo.nsservice.simpleservice.entity.NsService;

import org.openo.baseservice.i18n.ResourceUtil;
import org.openo.baseservice.log.OssLog;
import org.openo.baseservice.log.OssLogFactory;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.common.HttpContext;
import org.openo.baseservice.roa.util.restclient.RestfulParametes;
import org.openo.nfvo.common.rpc.rest.RestfulUtil;

/**
 * 
* The implement entity of NetworkService class<br/>
* <p>
* </p>
* 
* @author
* @version NFVO 0.5 May 15, 2016
 */
public class NetworkServiceImpl implements NetworkService {

    private static final OssLog LOG = OssLogFactory.getLogger(NetworkBusinessImpl.class);

    private NsServiceDao nsServiceDao;

    /**
     * Create a virtual network<br/>
     * @param context the info to create virtual network that required
     * @return success or not to create a network
     * @since   NFVO 0.5
     */
    @Override
    public String doCreateNetwork(HttpContext context) {
        JSONObject createNetworkRestJson = new JSONObject();
        HttpServletRequest createNetworkReq = context.getHttpServletRequest();
        String createNetworkData = null;
        String createNetworkResult = null;
        try {

            InputStream createNetworkInput = createNetworkReq.getInputStream();
            createNetworkData = IOUtils.toString(createNetworkInput);
            JSONObject createNetworkJsonParam = JSONObject.fromObject(createNetworkData);
            String idNetwork = UUID.randomUUID().toString().replace('-', "");
            Object[] keyCreateNetwork = createNetworkJsonParam.getJSONObject("resources").keySet().toArray();
            String resourcesKeyCreateNetwork = (String)keyCreateNetwork[0];
            if(!resourcesKeyCreateNetwork.isEmpty()) {
                createNetworkJsonParam.getJSONObject("resources").getJSONObject(resourcesKeyCreateNetwork)
                        .put("id", idNetwork);
            } else {
                LOG.error("function=createVnf,msg=jsonParam:{}", createNetworkJsonParam.toString());
            }
            LOG.error("function=createVnf,jsonParam:{}", createNetworkJsonParam);
            NsService nsServiceCreateNetwork = NsService.toEntity(createNetworkJsonParam);
            nsServiceCreateNetwork.setId(idNetwork);
            nsServiceDao.insertNsService(nsServiceCreateNetwork);
            RestfulParametes restParametesCreateNetwork = new RestfulParametes();
            restParametesCreateNetwork.setRawData(createNetworkJsonParam.toString());
            RestfulUtil.getRestfulParametesByToken(restParametesCreateNetwork, context);
            createNetworkResult =
                    RestfulUtil.getResponseContent("/rest/domed/v1/nfv/network", restParametesCreateNetwork,
                            RestfulUtil.TYPE_ADD);
            LOG.error("function=createNetwork demed_back,result:{}", createNetworkResult);
        } catch(JSONException e) {
            LOG.error("function=createNetwork,msg=JSONException:{}", e);
            createNetworkRestJson.put("retCode", "failed");
            createNetworkRestJson.put("reason",
                    ResourceUtil.getMessage("org.openo.nfvo.domed.service.parse.jsonexception"));
            createNetworkResult = createNetworkRestJson.toString();
        } catch(IOException e) {
            LOG.error("function=createNetwork,msg=IOException:{}", e);
            createNetworkRestJson.put("retCode", "failed");
            createNetworkRestJson.put("reason", ResourceUtil.getMessage("org.openo.nfvo.domed.service.ioexception"));
            createNetworkResult = createNetworkRestJson.toString();
        } catch(ServiceException e) {
            LOG.error("function=createNetwork,msg=ServiceException:{}", e);
            createNetworkRestJson.put("retCode", "failed");
            createNetworkRestJson.put("reason", ResourceUtil.getMessage("org.openo.nfvo.domed.service.ioexception"));
            createNetworkResult = createNetworkRestJson.toString();
        }
        return createNetworkResult;
    }

    @Override
    public String doDelNetwork(String resourceId, HttpContext context) {
        JSONObject restJsonDelNetwork = new JSONObject();
        LOG.error("resourceId:{}", resourceId);
        restJsonDelNetwork.put("retCode", Constant.REST_FAIL);
        NsService nsServiceDelNetwork = nsServiceDao.getNsServiceByPrimaryKey(resourceId);
        nsServiceDelNetwork.setAction("delete");
        RestfulParametes restParametesDelNetwork = new RestfulParametes();

        String delNsService = nsServiceDelNetwork.toString().replace("create", "delete");
        restParametesDelNetwork.setRawData(delNsService);
        LOG.error("nsService: {}", delNsService);
        RestfulUtil.getRestfulParametesByToken(restParametesDelNetwork, context);
        String resultDelNetwork =
                RestfulUtil.getResponseContent("/rest/domed/v1/nfv/network", restParametesDelNetwork,
                        RestfulUtil.TYPE_PUT);
        try {
            nsServiceDao.deleteNsService(resourceId);
        } catch(ServiceException e) {
            LOG.error("function=create,msg=IOException:{}", e);
            restJsonDelNetwork.put("reason", "ServiceException");
            resultDelNetwork = restJsonDelNetwork.toString();
        }
        return resultDelNetwork;
    }

    @Override
    public String doPutWithoutIdNetwork(HttpContext context) {
        LOG.error("function=putWithoutNetwork,msg= in putWithoutNetwork");
        JSONObject restJsonPutWithoutNetwork = new JSONObject();
        HttpServletRequest reqPutWithoutNetwork = context.getHttpServletRequest();
        String dataPutWithoutNetwork = null;
        String resultPutWithoutNetwork = null;
        try {
            InputStream inputPutWithoutNetwork = reqPutWithoutNetwork.getInputStream();
            dataPutWithoutNetwork = IOUtils.toString(inputPutWithoutNetwork);
            String idPutWithoutNetwork = UUID.randomUUID().toString().replace("-", "");
            LOG.error("function=putNetwork,msg=data:{}", dataPutWithoutNetwork);
            JSONObject jsonParamPutWithoutNetwork = JSONObject.fromObject(dataPutWithoutNetwork);
            Object[] keyPutWithoutNetwork = jsonParamPutWithoutNetwork.getJSONObject("resources").keySet().toArray();
            String resourcesKey = (String)keyPutWithoutNetwork[0];
            if(!resourcesKey.isEmpty()) {
                jsonParamPutWithoutNetwork.getJSONObject("resources").getJSONObject(resourcesKey)
                        .put("id", idPutWithoutNetwork);
            } else {
                LOG.error("function=putVnf,msg=jsonParam:{}", jsonParamPutWithoutNetwork.toString());
            }
            NsService nsService = NsService.toEntity(jsonParamPutWithoutNetwork);
            nsService.setId(idPutWithoutNetwork);
            nsServiceDao.updateNsService(nsService);
            RestfulParametes restParametes = new RestfulParametes();
            restParametes.setRawData(jsonParamPutWithoutNetwork.toString());
            RestfulUtil.getRestfulParametesByToken(restParametes, context);
            LOG.error("function=putNetwork,msg=restParametes:{}", restParametes.toString());
            resultPutWithoutNetwork =
                    RestfulUtil.getResponseContent("/rest/domed/v1/nfv/network", restParametes, RestfulUtil.TYPE_PUT);
            LOG.error("function= putNetwork result_from_domed,msg=result:{}", resultPutWithoutNetwork);
        } catch(JSONException e) {
            LOG.error("function=createNetwork,msg=JSONException:{}", e);
            restJsonPutWithoutNetwork.put("retCode", "failed");
            restJsonPutWithoutNetwork.put("reason",
                    ResourceUtil.getMessage("org.openo.nfvo.domed.service.parse.jsonexception"));
            resultPutWithoutNetwork = restJsonPutWithoutNetwork.toString();
        } catch(IOException e) {
            LOG.error("function=createNetwork,msg=IOException:{}", e);
            restJsonPutWithoutNetwork.put("retCode", "failed");
            restJsonPutWithoutNetwork.put("reason",
                    ResourceUtil.getMessage("org.openo.nfvo.domed.service.ioexception"));
            resultPutWithoutNetwork = restJsonPutWithoutNetwork.toString();
        } catch(ServiceException e) {
            LOG.error("function=createNetwork,msg=ServiceException:{}", e);
            restJsonPutWithoutNetwork.put("retCode", "failed");
            restJsonPutWithoutNetwork.put("reason",
                    ResourceUtil.getMessage("org.openo.nfvo.domed.service.ioexception"));
            resultPutWithoutNetwork = restJsonPutWithoutNetwork.toString();
        }
        return resultPutWithoutNetwork;
    }

    @Override
    public String doPutNetwork(String resourceId, HttpContext context) {
        JSONObject restJsonPutNetwork = new JSONObject();
        HttpServletRequest reqPutNetwork = context.getHttpServletRequest();
        String dataPutNetwork = null;
        String resultPutNetwork = null;
        try {
            InputStream inputPutNetwork = reqPutNetwork.getInputStream();
            dataPutNetwork = IOUtils.toString(inputPutNetwork);
            JSONObject jsonParamPutNetwork = JSONObject.fromObject(dataPutNetwork);
            NsService nsServicePutNetwork = NsService.toEntity(jsonParamPutNetwork);
            nsServicePutNetwork.setId(resourceId);
            nsServiceDao.updateNsService(nsServicePutNetwork);
            RestfulParametes restParametesPutNetwork = new RestfulParametes();
            restParametesPutNetwork.setRawData(dataPutNetwork);
            LOG.error("function=putNetwork,msg=data:{}", dataPutNetwork);
            RestfulUtil.getRestfulParametesByToken(restParametesPutNetwork, context);
            LOG.error("function=putNetwork,msg=restParametes:{}", restParametesPutNetwork.toString());
            resultPutNetwork =
                    RestfulUtil.getResponseContent("/rest/domed/v1/nfv/network", restParametesPutNetwork,
                            RestfulUtil.TYPE_PUT);
            LOG.error("function= putNetwork result_from_domed,msg=result:{}", resultPutNetwork);
        } catch(JSONException e) {
            LOG.error("function=createNetwork,msg=JSONException:{}", e);
            restJsonPutNetwork.put("retCode", "failed");
            restJsonPutNetwork.put("reason",
                    ResourceUtil.getMessage("org.openo.nfvo.domed.service.parse.jsonexception"));
            resultPutNetwork = restJsonPutNetwork.toString();
        } catch(IOException e) {
            LOG.error("function=createNetwork,msg=IOException:{}", e);
            restJsonPutNetwork.put("retCode", "failed");
            restJsonPutNetwork.put("reason", ResourceUtil.getMessage("org.openo.nfvo.domed.service.ioexception"));
            resultPutNetwork = restJsonPutNetwork.toString();
        } catch(ServiceException e) {
            LOG.error("function=createNetwork,msg=ServiceException:{}", e);
            restJsonPutNetwork.put("retCode", "failed");
            restJsonPutNetwork.put("reason", ResourceUtil.getMessage("org.openo.nfvo.domed.service.ioexception"));
            resultPutNetwork = restJsonPutNetwork.toString();
        }
        return resultPutNetwork;
    }

    public void setNsServiceDao(NsServiceDao nsServiceDao) {
        this.nsServiceDao = nsServiceDao;
    }

}
