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
import org.openo.nfvo.nsservice.simpleservice.services.api.NsService;
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

public class NsServiceImpl implements NsService {

    private static final OssLog LOG = OssLogFactory.getLogger(NsBusinessImpl.class);

    private NsServiceDao nsServiceDao;

    public String doCreateNs(HttpContext context) {
        JSONObject restJsonCreateNs = new JSONObject();
        restJsonCreateNs.put("retCode", Constant.REST_FAIL);
        HttpServletRequest reqCreateNs = context.getHttpServletRequest();
        String dataCreateNs = null;
        String resultCreateNs = null;
        try {
            InputStream inputCreateNs = reqCreateNs.getInputStream();
            dataCreateNs = IOUtils.toString(inputCreateNs);
            JSONObject jsonParamCreateNs = JSONObject.fromObject(dataCreateNs);
            String idCreateNs = UUID.randomUUID().toString().replace("-", "");
            Object[] keyCreateNs = jsonParamCreateNs.getJSONObject("resources").keySet().toArray();
            String resourcesKey = (String)keyCreateNs[0];
            if(!resourcesKey.isEmpty()) {
                jsonParamCreateNs.getJSONObject("resources").getJSONObject(resourcesKey).put("id", idCreateNs);
            } else {
                LOG.error("function=createVnf,msg=jsonParam:{}", jsonParamCreateNs.toString());
            }
            LOG.error("function=createVnf,jsonParam:{}", jsonParamCreateNs);
            NsService nsServiceCreateNs = NsService.toEntity(jsonParamCreateNs);
            nsServiceCreateNs.setId(idCreateNs);
            nsServiceDao.insertNsService(nsServiceCreateNs);
            RestfulParametes restParametesCreateNs = new RestfulParametes();
            restParametesCreateNs.setRawData(jsonParamCreateNs.toString());
            RestfulUtil.getRestfulParametesByToken(restParametesCreateNs, context);
            resultCreateNs =
                    RestfulUtil
                            .getResponseContent("/rest/domed/v1/nfv/ns", restParametesCreateNs, RestfulUtil.TYPE_ADD);
            LOG.error("function=createNs demed_back,result:{}", resultCreateNs);
        } catch(JSONException e) {
            LOG.error("function=create,msg=JSONException:{}", e);
            restJsonCreateNs.put("reason", ResourceUtil.getMessage("org.openo.nfvo.domed.service.parse.jsonexception"));
            resultCreateNs = restJsonCreateNs.toString();
        } catch(IOException e) {
            LOG.error("function=create,msg=IOException:{}", e);
            restJsonCreateNs.put("reason", ResourceUtil.getMessage("org.openo.nfvo.domed.service.ioexception"));
            resultCreateNs = restJsonCreateNs.toString();
        } catch(ServiceException e) {
            LOG.error("function=create,msg=IOException:{}", e);
            restJsonCreateNs.put("reason", "ServiceException");
            resultCreateNs = restJsonCreateNs.toString();
        }
        return resultCreateNs;
    }

    @Override
    public String doDelNs(String resourceId, HttpContext context) {
        JSONObject restJsonDelNs = new JSONObject();
        LOG.error("resourceId:{}", resourceId);
        restJsonDelNs.put("retCode", Constant.REST_FAIL);
        NsService nsServiceDelNs = nsServiceDao.getNsServiceByPrimaryKey(resourceId);
        nsServiceDelNs.setAction("delete");
        RestfulParametes restParametesDelNs = new RestfulParametes();

        String delNsService = nsServiceDelNs.toString().replace("create", "delete");
        restParametesDelNs.setRawData(delNsService);
        LOG.error("nsService: {}", delNsService);
        RestfulUtil.getRestfulParametesByToken(restParametesDelNs, context);
        String resultDelNs =
                RestfulUtil.getResponseContent("/rest/domed/v1/nfv/ns", restParametesDelNs, RestfulUtil.TYPE_PUT);
        try {
            nsServiceDao.deleteNsService(resourceId);
        } catch(ServiceException e) {
            LOG.error("function=create,msg=IOException:{}", e);
            restJsonDelNs.put("reason", "ServiceException");
            resultDelNs = restJsonDelNs.toString();
        }
        return resultDelNs;
    }

    @Override
    public String doPutWithoutIdNs(HttpContext context) {
        LOG.error("function=putWithoutNs,msg= in putWithoutNs");
        JSONObject restJsonPutWithoutNs = new JSONObject();
        restJsonPutWithoutNs.put("retCode", Constant.REST_FAIL);
        HttpServletRequest reqPutWithoutNs = context.getHttpServletRequest();
        String resultPutWithoutNs = null;
        try {
            InputStream inputPutWithoutNs = reqPutWithoutNs.getInputStream();
            String dataPutWithoutNs = IOUtils.toString(inputPutWithoutNs);
            LOG.error("function=putNs,msg=data:{}", dataPutWithoutNs);
            String idPutWithoutNs = UUID.randomUUID().toString().replace("-", "");
            JSONObject jsonParamPutWithoutNs = JSONObject.fromObject(dataPutWithoutNs);
            Object[] keyPutWithoutNs = jsonParamPutWithoutNs.getJSONObject("resources").keySet().toArray();
            String resourcesKey = (String)keyPutWithoutNs[0];
            if(!resourcesKey.isEmpty()) {
                jsonParamPutWithoutNs.getJSONObject("resources").getJSONObject(resourcesKey).put("id", idPutWithoutNs);
            } else {
                LOG.error("function=putVnf,msg=jsonParam:{}", jsonParamPutWithoutNs.toString());
            }
            NsService nsServicePutWithoutNs = NsService.toEntity(jsonParamPutWithoutNs);
            nsServicePutWithoutNs.setId(idPutWithoutNs);
            nsServiceDao.updateNsService(nsServicePutWithoutNs);
            RestfulParametes restParametesPutWithoutNs = new RestfulParametes();
            restParametesPutWithoutNs.setRawData(jsonParamPutWithoutNs.toString());
            RestfulUtil.getRestfulParametesByToken(restParametesPutWithoutNs, context);
            LOG.error("function=putNs,msg=restParametes:{}", restParametesPutWithoutNs.toString());
            resultPutWithoutNs =
                    RestfulUtil.getResponseContent("/rest/domed/v1/nfv/ns", restParametesPutWithoutNs,
                            RestfulUtil.TYPE_PUT);
            LOG.error("function= putNs result_from_domed,msg=result:{}", resultPutWithoutNs);
        } catch(JSONException e) {
            LOG.error("function=create,msg=JSONException:{}", e);
            restJsonPutWithoutNs.put("reason",
                    ResourceUtil.getMessage("org.openo.nfvo.domed.service.parse.jsonexception"));
            resultPutWithoutNs = restJsonPutWithoutNs.toString();
        } catch(IOException e) {
            LOG.error("function=create,msg=IOException:{}", e);
            restJsonPutWithoutNs.put("reason", ResourceUtil.getMessage("org.openo.nfvo.domed.service.ioexception"));
            resultPutWithoutNs = restJsonPutWithoutNs.toString();
        } catch(ServiceException e) {
            LOG.error("function=create,msg=IOException:{}", e);
            restJsonPutWithoutNs.put("reason", "ServiceException");
            resultPutWithoutNs = restJsonPutWithoutNs.toString();
        }
        return resultPutWithoutNs;
    }

    @Override
    public String doPutNs(String resourceId, HttpContext context) {
        JSONObject restJsonPutNs = new JSONObject();
        restJsonPutNs.put("retCode", Constant.REST_FAIL);
        HttpServletRequest reqPutNs = context.getHttpServletRequest();
        String resultPutNs = null;
        try {
            InputStream inputPutNs = reqPutNs.getInputStream();
            String dataPutNs = IOUtils.toString(inputPutNs);
            JSONObject jsonParamPutNs = JSONObject.fromObject(dataPutNs);
            NsService nsServicePutNs = NsService.toEntity(jsonParamPutNs);
            nsServicePutNs.setId(resourceId);
            nsServiceDao.updateNsService(nsServicePutNs);
            RestfulParametes restParametesPutNs = new RestfulParametes();
            restParametesPutNs.setRawData(dataPutNs);
            LOG.error("function=putNs,msg=data:{}", dataPutNs);
            RestfulUtil.getRestfulParametesByToken(restParametesPutNs, context);
            LOG.error("function=putNs,msg=restParametes:{}", restParametesPutNs.toString());
            resultPutNs =
                    RestfulUtil.getResponseContent("/rest/domed/v1/nfv/ns", restParametesPutNs, RestfulUtil.TYPE_PUT);
            LOG.error("function= putNs result_from_domed,msg=result:{}", resultPutNs);
        } catch(JSONException e) {
            LOG.error("function=create,msg=JSONException:{}", e);
            restJsonPutNs.put("reason", ResourceUtil.getMessage("org.openo.nfvo.domed.service.parse.jsonexception"));
            resultPutNs = restJsonPutNs.toString();
        } catch(IOException e) {
            LOG.error("function=create,msg=IOException:{}", e);
            restJsonPutNs.put("reason", ResourceUtil.getMessage("org.openo.nfvo.domed.service.ioexception"));
            resultPutNs = restJsonPutNs.toString();
        } catch(ServiceException e) {
            LOG.error("function=create,msg=IOException");
            restJsonPutNs.put("reason", "ServiceException:{}", e);
            resultPutNs = restJsonPutNs.toString();
        }
        return resultPutNs;
    }

    public void setNsServiceDao(NsServiceDao nsServiceDao) {
        this.nsServiceDao = nsServiceDao;
    }

}
