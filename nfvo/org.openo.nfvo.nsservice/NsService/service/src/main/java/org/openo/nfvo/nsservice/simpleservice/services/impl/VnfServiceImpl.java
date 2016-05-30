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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.commons.io.IOUtils;
import org.openo.nfvo.nsservice.simpleservice.business.api.VnfBusiness;
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

public class VnfServiceImpl implements VnfService {

    private static final OssLog LOG = OssLogFactory.getLogger(VnfBusinessImpl.class);

    private NsServiceDao nsServiceDao;

    @Override
    public String doCreateVnf(HttpContext context) {
        JSONObject restJsonCreateVnf = new JSONObject();
        restJsonCreateVnf.put("retCode", Constant.REST_FAIL);
        HttpServletRequest reqCreateVnf = context.getHttpServletRequest();
        String resultCreateVnf = null;
        try {

            InputStream inputCreateVnf = reqCreateVnf.getInputStream();
            String dataCreateVnf = IOUtils.toString(inputCreateVnf);
            JSONObject jsonParamCreateVnf = JSONObject.fromObject(dataCreateVnf);
            String idCreateVnf = UUID.randomUUID().toString().replace("-", "");
            Object[] keyCreateVnf = jsonParamCreateVnf.getJSONObject("resources").keySet().toArray();
            String resourcesKey = (String)keyCreateVnf[0];
            if(!resourcesKey.isEmpty()) {
                jsonParamCreateVnf.getJSONObject("resources").getJSONObject(resourcesKey).put("id", idCreateVnf);
            } else {
                LOG.error("function=createVnf,msg=jsonParam:{}", jsonParamCreateVnf.toString());
            }
            LOG.error("function=createVnf,jsonParam:{}", jsonParamCreateVnf);
            NsService nsServiceCreateVnf = NsService.toEntity(jsonParamCreateVnf);
            nsServiceCreateVnf.setId(idCreateVnf);
            nsServiceDao.insertNsService(nsServiceCreateVnf);
            RestfulParametes restParametesCreateVnf = new RestfulParametes();
            restParametesCreateVnf.setRawData(jsonParamCreateVnf.toString());
            RestfulUtil.getRestfulParametesByToken(restParametesCreateVnf, context);
            resultCreateVnf =
                    RestfulUtil.getResponseContent("/rest/domed/v1/nfv/vnf", restParametesCreateVnf,
                            RestfulUtil.TYPE_ADD);
            LOG.error("function=createVnf demed_back,result:{}", resultCreateVnf);
        } catch(JSONException e) {
            restJsonCreateVnf.put("data", ResourceUtil.getMessage("org.openo.nfvo.domed.service.parse.jsonexception"));
            LOG.error("function=createVnf,msg=JSONException:{}", e);
            resultCreateVnf = restJsonCreateVnf.toString();
        } catch(IOException e) {
            restJsonCreateVnf.put("data", ResourceUtil.getMessage("org.openo.nfvo.domed.service.ioexception"));
            LOG.error("function=createVnf,msg=IOException:{}", e);
            resultCreateVnf = restJsonCreateVnf.toString();
        } catch(ServiceException e) {
            restJsonCreateVnf.put("data", "ServiceException:{}", e);
            LOG.error("function=createVnf,msg=IOException");
            resultCreateVnf = restJsonCreateVnf.toString();
        }
        LOG.error("function=createVnf,msg=restJson : " + restJsonCreateVnf.toString());
        return resultCreateVnf;
    }

    @Override
    public String doDelVnf(String resourceId, HttpContext context) {
        JSONObject restJsonDelVnf = new JSONObject();
        LOG.error("resourceId:{}", resourceId);
        restJsonDelVnf.put("retCode", Constant.REST_FAIL);

        NsService nsServiceDelVnf = nsServiceDao.getNsServiceByPrimaryKey(resourceId);
        nsServiceDelVnf.setAction("delete");
        RestfulParametes restParametesDelVnf = new RestfulParametes();
        String data = nsServiceDelVnf.toString().replace("create", "delete");

        LOG.error("nsServiceDelVnf data :{}", data);
        JSONObject jsonParamDelVnf = JSONObject.fromObject(data);
        Object[] key = jsonParamDelVnf.getJSONObject("resources").keySet().toArray();
        String resourcesKey = (String)key[0];
        String type = jsonParamDelVnf.getJSONObject("resources").getJSONObject(resourcesKey).getString("type");
        List<String> vnfNames = new ArrayList<String>();
        String serviceId = jsonParamDelVnf.getString("service_id");
        JSONObject vnfdJsonObject = null;
        String vnfName = null;
        JSONArray topologyTemplateJsonArray = null;
        JSONObject topologyTemplate = null;
        String fromliuqianResult = null;
        JSONArray fromliuqianDataArray = null;
        JSONArray vnfDataJsonArray = new JSONArray();
        if("NSD.node.Vnf".equals(type)) {
            JSONArray vnfdJsonArray =
                    jsonParamDelVnf.getJSONObject("resources").getJSONObject(resourcesKey).getJSONObject("response")
                            .getJSONArray("vnfd_data");
            for(Object object : vnfdJsonArray) {
                vnfdJsonObject = JSONObject.fromObject(object);
                topologyTemplateJsonArray = vnfdJsonObject.getJSONObject("plan").getJSONArray("topology_template");
                for(Object objectA : topologyTemplateJsonArray) {
                    topologyTemplate = JSONObject.fromObject(objectA);
                    vnfName = topologyTemplate.getString("vnf_name");
                    vnfNames.add(vnfName);
                }
            }
            for(String string : vnfNames) {
                RestfulParametes parametes = new RestfulParametes();
                RestfulUtil.getRestfulParametesByToken(parametes, context);
                fromliuqianResult =
                        RestfulUtil.getResponseContent("/rest/nsmanage/v1/vnf?projectId=" + serviceId + "&name="
                                + string, parametes, RestfulUtil.TYPE_GET);
                LOG.error("from liu qian URL :" + "/rest/nsmanage/v1/vnf?projectId=" + serviceId + "&name=" + string);
                LOG.error("from liu qian :{}", fromliuqianResult);
                fromliuqianDataArray = JSONObject.fromObject(fromliuqianResult).getJSONArray("data");
                vnfDataJsonArray.add(fromliuqianDataArray.get(0));
            }
            jsonParamDelVnf.getJSONObject("resources").getJSONObject(resourcesKey).getJSONObject("response")
                    .remove("vnfd_data");
            LOG.error("jsonParam 1 :{}", jsonParamDelVnf);
            jsonParamDelVnf.getJSONObject("resources").getJSONObject(resourcesKey).getJSONObject("response")
                    .put("vnf_data", vnfDataJsonArray);
            LOG.error("jsonParam 2:{}", jsonParamDelVnf);
        }
        restParametesDelVnf.setRawData(jsonParamDelVnf.toString());
        LOG.error("nsService: {}", jsonParamDelVnf.toString());
        RestfulUtil.getRestfulParametesByToken(restParametesDelVnf, context);
        String resultDelVnf =
                RestfulUtil.getResponseContent("/rest/domed/v1/nfv/vnf", restParametesDelVnf, RestfulUtil.TYPE_PUT);
        try {
            nsServiceDao.deleteNsService(resourceId);
        } catch(ServiceException e) {
            LOG.error("function=create,msg=IOException:{}", e);
            restJsonDelVnf.put("reason", "ServiceException");
            resultDelVnf = restJsonDelVnf.toString();
        }
        return resultDelVnf;
    }

    @Override
    public String doPutWithoutIdVnf(HttpContext context) {
        LOG.error("function=putWithoutVnf,msg= in putWithoutVnf");
        JSONObject restJsonPutWithoutVnf = new JSONObject();
        restJsonPutWithoutVnf.put("retCode", Constant.REST_FAIL);
        HttpServletRequest reqPutWithoutVnf = context.getHttpServletRequest();
        String dataPutWithoutVnf = null;
        String resultPutWithoutVnf = null;
        try {
            InputStream inputPutWithoutVnf = reqPutWithoutVnf.getInputStream();
            String idPutWithoutVnf = UUID.randomUUID().toString().replace("-", "");
            dataPutWithoutVnf = IOUtils.toString(inputPutWithoutVnf);
            LOG.error("function=putVnf,msg=data:{}", dataPutWithoutVnf);
            JSONObject jsonParamPutWithoutVnf = JSONObject.fromObject(dataPutWithoutVnf);
            Object[] key = jsonParamPutWithoutVnf.getJSONObject("resources").keySet().toArray();
            String resourcesKey = (String)key[0];
            if(!resourcesKey.isEmpty()) {
                jsonParamPutWithoutVnf.getJSONObject("resources").getJSONObject(resourcesKey)
                        .put("id", idPutWithoutVnf);
            } else {
                LOG.error("function=putVnf,msg=jsonParam:{}", jsonParamPutWithoutVnf.toString());
            }
            NsService nsServicePutWithoutVnf = NsService.toEntity(jsonParamPutWithoutVnf);
            nsServicePutWithoutVnf.setId(idPutWithoutVnf);
            nsServiceDao.updateNsService(nsServicePutWithoutVnf);
            RestfulParametes restParametesPutWithoutVnf = new RestfulParametes();
            restParametesPutWithoutVnf.setRawData(jsonParamPutWithoutVnf.toString());
            RestfulUtil.getRestfulParametesByToken(restParametesPutWithoutVnf, context);
            LOG.error("function=putVnf,msg=restParametes:{}", restParametesPutWithoutVnf.toString());
            resultPutWithoutVnf =
                    RestfulUtil.getResponseContent("/rest/domed/v1/nfv/vnf", restParametesPutWithoutVnf,
                            RestfulUtil.TYPE_PUT);
            LOG.error("function= putVnf result_from_domed,msg=result:{}", resultPutWithoutVnf);
        } catch(JSONException e) {
            restJsonPutWithoutVnf.put("data",
                    ResourceUtil.getMessage("org.openo.nfvo.domed.service.parse.jsonexception"));
            LOG.error("function=createVnf,msg=JSONException:{}", e);
            resultPutWithoutVnf = restJsonPutWithoutVnf.toString();
        } catch(IOException e) {
            restJsonPutWithoutVnf.put("data", ResourceUtil.getMessage("org.openo.nfvo.domed.service.ioexception"));
            LOG.error("function=createVnf,msg=IOException:{}", e);
            resultPutWithoutVnf = restJsonPutWithoutVnf.toString();
        } catch(ServiceException e) {
            restJsonPutWithoutVnf.put("data", "ServiceException:{}", e);
            LOG.error("function=createVnf,msg=IOException");
            resultPutWithoutVnf = restJsonPutWithoutVnf.toString();
        }
        LOG.error("function=createVnf,msg=restJson : " + restJsonPutWithoutVnf.toString());
        return resultPutWithoutVnf;
    }

    @Override
    public String doPutVnf(String resourceId, HttpContext context) {
        JSONObject restJsonPutVnf = new JSONObject();
        restJsonPutVnf.put("retCode", Constant.REST_FAIL);
        HttpServletRequest reqPutVnf = context.getHttpServletRequest();
        String dataPutVnf = null;
        String resultPutVnf = null;
        try {
            InputStream inputPutVnf = reqPutVnf.getInputStream();
            dataPutVnf = IOUtils.toString(inputPutVnf);
            JSONObject jsonParamPutVnf = JSONObject.fromObject(dataPutVnf);
            NsService nsServicePutVnf = NsService.toEntity(jsonParamPutVnf);
            nsServicePutVnf.setId(resourceId);
            nsServiceDao.updateNsService(nsServicePutVnf);
            RestfulParametes restParametesPutVnf = new RestfulParametes();
            restParametesPutVnf.setRawData(dataPutVnf);
            LOG.error("function=putVnf,msg=data:{}", dataPutVnf);
            RestfulUtil.getRestfulParametesByToken(restParametesPutVnf, context);
            LOG.error("function=putVnf,msg=restParametes:{}", restParametesPutVnf.toString());
            resultPutVnf =
                    RestfulUtil.getResponseContent("/rest/domed/v1/nfv/vnf", restParametesPutVnf, RestfulUtil.TYPE_PUT);
            LOG.error("function= putVnf result_from_domed,msg=result:{}", resultPutVnf);
        } catch(JSONException e) {

            restJsonPutVnf.put("data", ResourceUtil.getMessage("org.openo.nfvo.domed.service.parse.jsonexception"));
            LOG.error("function=createVnf,msg=JSONException:{}", e);
            resultPutVnf = restJsonPutVnf.toString();
        } catch(IOException e) {
            restJsonPutVnf.put("data", ResourceUtil.getMessage("org.openo.nfvo.domed.service.ioexception"));
            LOG.error("function=createVnf,msg=IOException:{}", e);
            resultPutVnf = restJsonPutVnf.toString();
        } catch(ServiceException e) {
            restJsonPutVnf.put("data", "ServiceException");
            LOG.error("function=createVnf,msg=IOException:{}", e);
            resultPutVnf = restJsonPutVnf.toString();
        }
        LOG.error("function=createVnf,msg=restJson : " + restJsonPutVnf.toString());
        return resultPutVnf;
    }

    public void setNsServiceDao(NsServiceDao nsServiceDao) {
        this.nsServiceDao = nsServiceDao;
    }

}
