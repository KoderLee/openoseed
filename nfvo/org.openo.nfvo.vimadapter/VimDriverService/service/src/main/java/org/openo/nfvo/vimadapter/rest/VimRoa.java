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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.commons.io.IOUtils;
import org.openo.nfvo.vimadapter.util.Vim;
import org.openo.nfvo.vimadapter.util.VimOpResult;
import org.openo.nfvo.vimadapter.util.VimOpResult.TaskStatus;
import org.openo.nfvo.vimadapter.util.constant.Constant;
import org.openo.nfvo.vimadapter.service.adapter.AdapterVimManager;

import org.openo.baseservice.i18n.ResourceUtil;
import org.openo.baseservice.log.OssLog;
import org.openo.baseservice.log.OssLogFactory;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.annotation.DEL;
import org.openo.baseservice.roa.annotation.GET;
import org.openo.baseservice.roa.annotation.POST;
import org.openo.baseservice.roa.annotation.PUT;
import org.openo.baseservice.roa.annotation.Path;
import org.openo.baseservice.roa.annotation.PathParam;
import org.openo.baseservice.roa.annotation.Target;
import org.openo.baseservice.roa.common.HttpContext;
import org.openo.nfvo.common.auditlog.AuditLog;
import org.openo.nfvo.common.auditlog.model.LogSeverity;
import org.openo.nfvo.common.auditlog.model.Result;

/**
 * 
* Used for get vims info, send request to VIM.<br/>
* <p>
* </p>
* 
* @author
* @version NFVO 0.5 May 15, 2016
 */
@Path("/vimadapter/v1/vims")
@Target("vim-adapter")
public class VimRoa {

    private final static OssLog LOG = OssLogFactory.getLogger(VimRoa.class);

    private JSONObject restJson = new JSONObject();

    private AdapterVimManager adapter;

    public void setAdapter(AdapterVimManager adapter) {
        this.adapter = adapter;
    }

    /**
     * Add VIM and save VIM info into NFVO, the rest URI:/vimadapter/v1/vims<br/>
     * 
     * @param context: the necessary info for add a VIM 
     * @return The info about VIM that had been added.
     * @throws ServiceException common exception.
     * @since  NFVO 0.5 
     */
    @POST
    public String addVim(HttpContext context) throws ServiceException {
        HttpServletRequest req = context.getHttpServletRequest();
        String data = null;
        try {
            InputStream input = req.getInputStream();
            data = IOUtils.toString(input);

            JSONObject subJsonObject = JSONObject.fromObject(data);
            JSONObject vimJsonObject = subJsonObject.getJSONObject("VIM");
            if(vimJsonObject == null || vimJsonObject.isEmpty()) {
                restJson.put("retCode", Constant.REST_FAIL);
                restJson.put("data", ResourceUtil.getMessage("org.openo.nfvo.vimadapter.service.param.insufficient"));
                return restJson.toString();
            }

            String id = UUID.randomUUID().toString();
            Vim vim = new Vim(id, vimJsonObject, Constant.ACTIVE);
            VimOpResult vimOpResult = adapter.addVim(vim, context);

            if(vimOpResult.gotOperateStatus() == TaskStatus.SUCCESS) {
                restJson.put("data", vimOpResult.gotResult().get(0));
                restJson.put("retCode", Constant.REST_SUCCESS);
                restJson.getJSONObject("data").put("extraInfo",
                        JSONObject.fromObject(restJson.getJSONObject("data").get("extraInfo")));
            } else {
                restJson.put("retCode", Constant.REST_FAIL);
                restJson.put("data", vimOpResult.gotErrorMessage());
            }
        } catch(JSONException e) {
            LOG.error("function=addVim,msg=JSONException" + e);
            restJson.put("retCode", Constant.REST_FAIL);
            restJson.put("data", ResourceUtil.getMessage("org.openo.nfvo.vimadapter.service.parsejson.exception"));
        } catch(IOException e) {
            restJson.put("retCode", Constant.REST_FAIL);
            LOG.error("function=addVim,msg=IOException" + e);
            restJson.put("data", ResourceUtil.getMessage("org.openo.nfvo.vimadapter.service.parsejson.exception"));
        }

        if(String.valueOf(Constant.REST_FAIL).equals(restJson.getString("retCode"))) {
            AuditLog.logInstance(Constant.PARAM_MODULE)
                    .addOperation(ResourceUtil.getMessage("org.openo.nfvo.vimadapter.vim.log.add"))
                    .addLevel(LogSeverity.MINOR)
                    .addDetail(ResourceUtil.getMessage("org.openo.nfvo.vimadapter.vim.log.add.fail"))
                    .addResult(Result.FAILURE).addTargetObj(Constant.PARAM_MODULE).generateOperationLog(context);
        } else {
            AuditLog.logInstance(Constant.PARAM_MODULE)
                    .addOperation(ResourceUtil.getMessage("org.openo.nfvo.vimadapter.vim.log.add"))
                    .addLevel(LogSeverity.WARNING)
                    .addDetail(ResourceUtil.getMessage("org.openo.nfvo.vimadapter.vim.log.add.success"))
                    .addResult(Result.SUCCESSFUL).addTargetObj(Constant.PARAM_MODULE).generateOperationLog(context);
        }

        return restJson.toString();

    }

    /**
     * Remova a VIM from NFVO system, the rest URI: /vimadapter/v1/vims/{vimid}.<br/>
     * 
     * @param context the http context
     * @param vimId the id of VIM to be deleted
     * @return success or not about deleting the specific VIM
     * @throws ServiceException common exception.
     * @since  NFVO 0.5 
     */
    @DEL
    @Path("/{vimId}")
    public String delVim(HttpContext context, @PathParam("vimId") String vimId) throws ServiceException {
        if(null == vimId || "".equals(vimId)) {
            restJson.put("retCode", Constant.REST_FAIL);
            restJson.put("data", ResourceUtil.getMessage("org.openo.nfvo.vimadapter.service.null.id"));
            return restJson.toString();
        }

        VimOpResult vimOpResult = adapter.deleteVim(vimId, context);

        if(vimOpResult.gotOperateStatus() == TaskStatus.SUCCESS) {
            restJson.put("retCode", Constant.REST_SUCCESS);
            restJson.put("data", ResourceUtil.getMessage("org.openo.nfvo.vimadapter.service.del.vim.succ"));
        } else {
            restJson.put("retCode", Constant.REST_FAIL);
            restJson.put("data", vimOpResult.gotErrorMessage());
        }

        if(String.valueOf(Constant.REST_FAIL).equals(restJson.getString("retCode"))) {
            AuditLog.logInstance(Constant.PARAM_MODULE)
                    .addOperation(ResourceUtil.getMessage("org.openo.nfvo.vimadapter.vim.log.del"))
                    .addLevel(LogSeverity.MINOR)
                    .addDetail(ResourceUtil.getMessage("org.openo.nfvo.vimadapter.vim.log.del.fail"))
                    .addResult(Result.FAILURE).addTargetObj(Constant.PARAM_MODULE).generateOperationLog(context);
        } else {
            AuditLog.logInstance(Constant.PARAM_MODULE)
                    .addOperation(ResourceUtil.getMessage("org.openo.nfvo.vimadapter.vim.log.del"))
                    .addLevel(LogSeverity.WARNING)
                    .addDetail(ResourceUtil.getMessage("org.openo.nfvo.vimadapter.vim.log.del.success"))
                    .addResult(Result.SUCCESSFUL).addTargetObj(Constant.PARAM_MODULE).generateOperationLog(context);
        }

        return restJson.toString();
    }

    /**
     * Batched delete VIM from NFVO system the rest URI: /vimadapter/v1/vims/<br/>
     * 
     * @param context the http request.
     * @return success or not about batched delete VIMs.
     * @throws ServiceException common exception.
     * @since  NFVO 0.5 
     */
    @DEL
    public String delVims(HttpContext context) throws ServiceException {
        HttpServletRequest req = context.getHttpServletRequest();
        String data = null;
        try {
            InputStream input = req.getInputStream();
            data = IOUtils.toString(input);
            JSONArray vimList = JSONArray.fromObject(data);

            if(null == vimList || vimList.isEmpty()) {
                restJson.put("retCode", Constant.REST_FAIL);
                restJson.put("data", ResourceUtil.getMessage("org.openo.nfvo.vimadapter.service.batch.del.null.id"));
                return restJson.toString();
            }
            List<String> vimIdList = new ArrayList<String>();
            int vmSize = vimList.size();

            for(int i = 0; i < vmSize; i++) {
                vimIdList.add(vimList.getString(i));
            }

            VimOpResult vimOpResult = adapter.deleteVims(vimIdList, context);

            if(vimOpResult.gotOperateStatus() == TaskStatus.SUCCESS) {
                restJson.put("retCode", Constant.REST_SUCCESS);
                restJson.put("data", ResourceUtil.getMessage("org.openo.nfvo.vimadapter.service.batch.del.succ"));
            } else if(vimOpResult.gotOperateStatus() == TaskStatus.PART_SUCCESS) {
                JSONArray failList = new JSONArray();
                for(Object vimId : vimOpResult.gotResult()) {
                    failList.add((String)vimId);
                }
                restJson.put("data", ResourceUtil.getMessage("org.openo.nfvo.vimadapter.service.batch.del.partly")
                        + failList);
                restJson.put("retCode", Constant.REST_PART_SUCCESS);
            } else {
                restJson.put("retCode", Constant.REST_FAIL);
                restJson.put("data", ResourceUtil.getMessage("org.openo.nfvo.vimadapter.service.batch.del.fail"));
            }

        } catch(IOException e) {
            LOG.warn("delVims:IOException" + e);
            restJson.put("retCode", Constant.REST_FAIL);
            restJson.put("data", ResourceUtil.getMessage("org.openo.nfvo.vimadapter.service.delete.io.exception"));
        } catch(JSONException e) {
            LOG.warn("delVims:JSONException" + e);
            restJson.put("retCode", Constant.REST_FAIL);
            restJson.put("data", ResourceUtil.getMessage("org.openo.nfvo.vimadapter.service.parsejson.exception"));
        }

        if(String.valueOf(Constant.REST_FAIL).equals(restJson.getString("retCode"))) {
            AuditLog.logInstance(Constant.PARAM_MODULE)

            .addOperation(ResourceUtil.getMessage("org.openo.nfvo.vimadapter.vim.log.del"))
                    .addLevel(LogSeverity.MINOR)
                    .addDetail(ResourceUtil.getMessage("org.openo.nfvo.vimadapter.vim.log.del.fail"))
                    .addResult(Result.FAILURE).addTargetObj(Constant.PARAM_MODULE).generateOperationLog(context);
        } else if(String.valueOf(Constant.REST_PART_SUCCESS).equals(restJson.getString("retCode"))) {
            AuditLog.logInstance(Constant.PARAM_MODULE)
                    .addOperation(ResourceUtil.getMessage("org.openo.nfvo.vimadapter.vim.log.del"))
                    .addLevel(LogSeverity.WARNING)
                    .addDetail(ResourceUtil.getMessage("org.openo.nfvo.vimadapter.vim.log.del.partsuccess"))
                    .addResult(Result.PARTIAL_SUCCESS).addTargetObj(Constant.PARAM_MODULE)
                    .generateOperationLog(context);
        } else {
            AuditLog.logInstance(Constant.PARAM_MODULE)
                    .addOperation(ResourceUtil.getMessage("org.openo.nfvo.vimadapter.vim.log.del"))
                    .addLevel(LogSeverity.WARNING)
                    .addDetail(ResourceUtil.getMessage("org.openo.nfvo.vimadapter.vim.log.del.success"))
                    .addResult(Result.SUCCESSFUL).addTargetObj(Constant.PARAM_MODULE).generateOperationLog(context);
        }
        return restJson.toString();
    }

    /**
     * Query specific(if vimId isn't null)/all VIM info , the rest URI: /vimadapter/v1/vims/<br/>
     * 
     * @param context the http context.
     * @param vimId the id of VIM to be queried.
     * @return the info of VIM to be queried.
     * @throws ServiceException common exception.
     * @since  NFVO 0.5 
     */
    @GET
    @Path("/{vimId}")
    public String getVim(HttpContext context, @PathParam("vimId") String vimId) throws ServiceException {
        VimOpResult vimOpResult = adapter.getVim(vimId);

        if(vimOpResult.gotOperateStatus() == TaskStatus.SUCCESS) {
            restJson.put("retCode", Constant.REST_SUCCESS);
            restJson.put("data", vimOpResult.gotResult().get(0));
            restJson.getJSONObject("data").put("extraInfo",
                    JSONObject.fromObject(restJson.getJSONObject("data").get("extraInfo")));
        } else {
            restJson.put("data", vimOpResult.gotErrorMessage());
            restJson.put("retCode", Constant.REST_FAIL);
        }
        if(String.valueOf(Constant.REST_FAIL).equals(restJson.getString("retCode"))) {
            AuditLog.logInstance(Constant.PARAM_MODULE)
                    .addOperation(ResourceUtil.getMessage("org.openo.nfvo.vimadapter.vim.log.select"))
                    .addLevel(LogSeverity.MINOR)
                    .addDetail(ResourceUtil.getMessage("org.openo.nfvo.vimadapter.vim.log.select.fail"))
                    .addResult(Result.FAILURE).addTargetObj(Constant.PARAM_MODULE).generateOperationLog(context);
        } else {
            AuditLog.logInstance(Constant.PARAM_MODULE)
                    .addOperation(ResourceUtil.getMessage("org.openo.nfvo.vimadapter.vim.log.select"))
                    .addLevel(LogSeverity.WARNING)
                    .addDetail(ResourceUtil.getMessage("org.openo.nfvo.vimadapter.vim.log.select.success"))
                    .addResult(Result.SUCCESSFUL).addTargetObj(Constant.PARAM_MODULE).generateOperationLog(context);
        }
        return restJson.toString();

    }

    /**
     * List all VIM info in NFVO system.<br/>
     * 
     * @param context the http context.
     * @return all VIM info in NFVO system.
     * @throws ServiceException common exception.
     * @since  NFVO 0.5 
     */
    @GET
    public String indexVims(HttpContext context) throws ServiceException {
        VimOpResult vimOpResult = adapter.indexVims(0, 0);

        if(vimOpResult.gotOperateStatus() == TaskStatus.SUCCESS) {
            restJson.put("retCode", Constant.REST_SUCCESS);
            restJson.put("data", vimOpResult.gotResult());
            JSONArray array = restJson.getJSONArray("data");
            int arraySize = array.size();
            for(int i = 0; i < arraySize; i++) {
                array.getJSONObject(i).put("extraInfo", JSONObject.fromObject(array.getJSONObject(i).get("extraInfo")));
            }
        } else {
            restJson.put("retCode", Constant.REST_FAIL);
            restJson.put("data", vimOpResult.gotErrorMessage());
        }

        if(String.valueOf(Constant.REST_FAIL).equals(restJson.getString("retCode"))) {
            AuditLog.logInstance(Constant.PARAM_MODULE)
                    .addOperation(ResourceUtil.getMessage("org.openo.nfvo.vimadapter.vim.log.select"))
                    .addLevel(LogSeverity.MINOR)

                    .addDetail(ResourceUtil.getMessage("org.openo.nfvo.vimadapter.vim.log.select.fail"))
                    .addResult(Result.FAILURE).addTargetObj(Constant.PARAM_MODULE).generateOperationLog(context);
        } else {
            AuditLog.logInstance(Constant.PARAM_MODULE)
                    .addOperation(ResourceUtil.getMessage("org.openo.nfvo.vimadapter.vim.log.select"))
                    .addLevel(LogSeverity.WARNING)
                    .addDetail(ResourceUtil.getMessage("org.openo.nfvo.vimadapter.vim.log.select.success"))
                    .addResult(Result.SUCCESSFUL).addTargetObj(Constant.PARAM_MODULE).generateOperationLog(context);
        }
        return restJson.toString();

    }

    /**
     * Update the specific VIM info in NFVO system.<br/>
     * 
     * @param vimId id of VIM to be updated.
     * @param context the updating info for the specific VIM.
     * @return success or not that update the VIM.
     * @throws ServiceException common exception.
     * @since  NFVO 0.5 
     */
    @PUT
    @Path("/{vimId}")
    public String modVim(@PathParam("vimId") String vimId, HttpContext context) throws ServiceException {
        HttpServletRequest req = context.getHttpServletRequest();
        String data = null;
        try {
            InputStream input = req.getInputStream();
            data = IOUtils.toString(input);
            JSONObject subJsonObject = JSONObject.fromObject(data);
            JSONObject vimJsonObject = subJsonObject.getJSONObject("VIM");
            if(vimJsonObject.isEmpty()) {
                restJson.put("data", ResourceUtil.getMessage("org.openo.nfvo.vimadapter.service.param.insufficient"));
                restJson.put("retCode", Constant.REST_FAIL);

                AuditLog.logInstance(Constant.PARAM_MODULE)
                        .addOperation(ResourceUtil.getMessage("org.openo.nfvo.vimadapter.vim.log.update"))
                        .addLevel(LogSeverity.MINOR)
                        .addDetail(ResourceUtil.getMessage("org.openo.nfvo.vimadapter.vim.log.update.fail"))
                        .addResult(Result.FAILURE).addTargetObj(Constant.PARAM_MODULE).generateOperationLog(context);

                return restJson.toString();
            }
            Vim vim = new Vim();
            vim.setModVim(vimJsonObject, vimId);

            VimOpResult result = adapter.updateVim(vim, context);
            if(result.gotOperateStatus() == TaskStatus.SUCCESS) {
                AuditLog.logInstance(Constant.PARAM_MODULE)
                        .addOperation(ResourceUtil.getMessage("org.openo.nfvo.vimadapter.vim.log.update"))
                        .addLevel(LogSeverity.WARNING)
                        .addDetail(ResourceUtil.getMessage("org.openo.nfvo.vimadapter.vim.log.update.success"))
                        .addResult(Result.SUCCESSFUL).addTargetObj(Constant.PARAM_MODULE).generateOperationLog(context);

                return getVim(context, vimId);
            } else {
                restJson.put("retCode", Constant.REST_FAIL);
                restJson.put("data", result.gotErrorMessage());
            }
        } catch(JSONException e) {
            restJson.put("retCode", Constant.REST_FAIL);
            LOG.error("JSON parse Exception: {}", e);
            restJson.put("data", ResourceUtil.getMessage("org.openo.nfvo.vimadapter.service.parsejson.exception"));
        } catch(IOException e) {
            LOG.error("IO Exception: {}", e);
            restJson.put("retCode", Constant.REST_FAIL);
            restJson.put("data", ResourceUtil.getMessage("org.openo.nfvo.vimadapter.service.io.exception"));
        }

        if(String.valueOf(Constant.REST_FAIL).equals(restJson.getString("retCode"))) {
            AuditLog.logInstance(Constant.PARAM_MODULE)
                    .addOperation(ResourceUtil.getMessage("org.openo.nfvo.vimadapter.vim.log.update"))
                    .addLevel(LogSeverity.MINOR)
                    .addDetail(ResourceUtil.getMessage("org.openo.nfvo.vimadapter.vim.log.update.fail"))
                    .addResult(Result.FAILURE).addTargetObj(Constant.PARAM_MODULE).generateOperationLog(context);
        } else {
            AuditLog.logInstance(Constant.PARAM_MODULE)
                    .addOperation(ResourceUtil.getMessage("org.openo.nfvo.vimadapter.vim.log.update"))
                    .addLevel(LogSeverity.WARNING)
                    .addDetail(ResourceUtil.getMessage("org.openo.nfvo.vimadapter.vim.log.update.success"))
                    .addResult(Result.SUCCESSFUL).addTargetObj(Constant.PARAM_MODULE).generateOperationLog(context);
        }
        return restJson.toString();
    }
}
