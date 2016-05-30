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

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openo.nfvo.vimadapter.util.CryptUtil;
import org.openo.nfvo.vimadapter.util.InterfaceConnectMgr;
import org.openo.nfvo.vimadapter.util.InterfaceVimManager;
import org.openo.nfvo.vimadapter.util.RestfulUtil;
import org.openo.nfvo.vimadapter.util.StringUtil;
import org.openo.nfvo.vimadapter.util.VcenterUtil;
import org.openo.nfvo.vimadapter.util.Vim;
import org.openo.nfvo.vimadapter.util.VimOpResult;
import org.openo.nfvo.vimadapter.util.VimOpResult.TaskStatus;
import org.openo.nfvo.vimadapter.util.constant.Constant;
import org.openo.nfvo.vimadapter.util.constant.UrlConstant;
import org.openo.nfvo.vimadapter.util.dao.VimDao;
import org.openo.nfvo.vimadapter.openstack.entry.ConnectMgrOpenstack;
import org.openo.nfvo.vimadapter.service.util.AdapterUtil;
import org.openo.nfvo.vimadapter.service.util.EventUtil;

import org.openo.nfvo.common.auditlog.AuditLog;
import org.openo.nfvo.common.auditlog.model.LogSeverity;
import org.openo.nfvo.common.auditlog.model.Result;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.openo.baseservice.i18n.ResourceUtil;
import org.openo.baseservice.log.OssLog;
import org.openo.baseservice.log.OssLogFactory;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.common.HttpContext;
import org.openo.baseservice.roa.util.restclient.RestfulParametes;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;

/**
 * 
* Used for adapte different VIM(FS/OS/VMWARE), supply same rest call for upper layer to create/update/delete vim<br/>
* <p>
* </p>
* 
* @author
* @version NFVO 0.5 May 15, 2016
 */
public class AdapterVimManager implements InterfaceVimManager {
    private final static OssLog LOG = OssLogFactory
            .getLogger(AdapterVimManager.class);

    private VimDao vimDao;

    private InterfaceConnectMgr createConnectMgr(String type) {
        InterfaceConnectMgr connectMgr = null;
        if (Constant.OPENSTACK.equalsIgnoreCase(type)) {
            connectMgr = new ConnectMgrOpenstack();
        } else {
            LOG.error("funtion=createConnectMgr, msg=Invalid VIM type!");
        }

        return connectMgr;
    }

    public void setVimDao(VimDao vimDao) {
        this.vimDao = vimDao;
    }

    @Override
    public VimOpResult addVim(Vim vim, HttpContext context) {
        LOG.info("function=addVim, msg=VimManager enter to add a vim, vim="
                + vim.getId());
        VimOpResult result = new VimOpResult();

        try {
            if (checkUrl(result, vim) && validateVimData(result, vim)) {
                int retCode = createConnectMgr(vim.getType()).connect(vim,
                        Constant.POST);
                if (retCode == Constant.HTTP_OK
                        || retCode == Constant.HTTP_CREATED) {
                    vim.setCreateAt(new Date());
                    vim.setUpdateAt(new Date());
                    vim.setPwd(CryptUtil.enCrypt(vim.getPwd()));
                    vim.setExtraInfo(VcenterUtil.cryptVcenterPwd(
                            vim.getExtraInfo(), true));
                    int affectRows = vimDao.insertVim(vim);
                    if (affectRows > 0) {
                        result.setOperateStatus(VimOpResult.TaskStatus.SUCCESS);

                        vim.setExtraInfo(VcenterUtil.hideVcenterPwd(vim
                                .getExtraInfo()));
                        result.addResult(vim.genVimResultMap());
                        EventUtil.sentEvtByRest(
                                String.format(UrlConstant.REST_EVENT_ADD,
                                        vim.getId()), Constant.ASYNCPOST,
                                new JSONObject(), context);
                        LOG.warn(
                                "function=addVim, msg=VimManager enter addVim >>>  sentEvtByRest, vimId is {}.",
                                vim.getId());
                        return result;
                    }

                    LOG.error("function=addVim, msg=add vim fail,database error");
                    result.setOperateStatus(VimOpResult.TaskStatus.FAIL);
                    result.setErrorMessage(ResourceUtil
                            .getMessage("org.openo.nfvo.vimadapter.service.db.error.add"));
                    return result;
                }

                if (retCode == Constant.CONNECT_TMOUT) {
                    LOG.error("function=addVim, msg=add vim fail, connect time out");
                    result.setOperateStatus(VimOpResult.TaskStatus.FAIL);
                    result.setErrorMessage(ResourceUtil
                            .getMessage("org.openo.nfvo.vimadapter.vim.connect.timeout"));
                    return result;
                }

                LOG.error("function=addVim, msg=VimManager add vim fail, connect fail retcode="
                        + retCode);
                result.setOperateStatus(VimOpResult.TaskStatus.FAIL);
                result.setErrorMessage(ResourceUtil
                        .getMessage("org.openo.nfvo.vimadapter.service.add.vim.fail"));
                return result;
            }

            LOG.error("function=addVim, msg=VimManager add vim fail, param error");
            return result;
        } catch (ServiceException e) {
            LOG.error("function=addVim, msg=VimManager add vim exception" + e);
            result.setOperateStatus(VimOpResult.TaskStatus.FAIL);
            result.setErrorMessage(ResourceUtil
                    .getMessage("org.openo.nfvo.vimadapter.service.add.exception"));
        }

        return result;

    }
    
    private boolean validSuccessStatus(int retCode) {
        return retCode == Constant.HTTP_OK|| retCode == Constant.HTTP_CREATED;
    }

    @Override
    public VimOpResult updateVim(Vim argVim, HttpContext context) {
        VimOpResult result = new VimOpResult();

        try {
            Vim vim = vimDao.getVimById(argVim.getId());
            if (vim == null) {
                LOG.error("function=updateVim, msg=update Vim: Vim not exists");
                result.setOperateStatus(VimOpResult.TaskStatus.FAIL);
                result.setErrorMessage(ResourceUtil
                        .getMessage("org.openo.nfvo.vimadapter.service.update.no.vim"));
                return result;
            }

            if (!(checkVcenterInfo(
                    JSONObject.fromObject(argVim.getExtraInfo()), vim.getType()))) {
                LOG.error("function=updateVim, msg=update Vim: vCenter info is invalid");
                result.setOperateStatus(VimOpResult.TaskStatus.FAIL);
                result.setErrorMessage(ResourceUtil
                        .getMessage("org.openo.nfvo.vimadapter.service.param.vcenter.error"));
                return result;
            }

            boolean isValid = validateUpdateData(argVim, result);
            if (isValid) {
                vim.setVimBean(argVim);
                vim.setPwd(CryptUtil.deCrypt(vim.getPwd()));
                vim.setExtraInfo(VcenterUtil.cryptVcenterPwd(
                        vim.getExtraInfo(), false));
                int retCode = createConnectMgr(vim.getType()).connect(vim,
                        Constant.PUT);

                if (validSuccessStatus(retCode)) {
                    vim.setPwd(CryptUtil.enCrypt(vim.getPwd()));
                    vim.setExtraInfo(VcenterUtil.cryptVcenterPwd(
                            vim.getExtraInfo(), true));
                    vim.setUpdateAt(new Date());
                    vim.setStatus(Constant.ACTIVE);

                    int affectRows = vimDao.updateVim(vim);
                    if (affectRows > 0) {
                        result.setOperateStatus(VimOpResult.TaskStatus.SUCCESS);
                        result.setErrorMessage(ResourceUtil
                                .getMessage("org.openo.nfvo.vimadapter.service.update.vim.succ"));

                        vim.setExtraInfo(VcenterUtil.hideVcenterPwd(vim
                                .getExtraInfo()));
                        result.addResult(vim.genVimResultMap());

                        EventUtil.sentEvtByRest(String.format(
                                UrlConstant.REST_EVENT_UPDATE, vim.getId()),
                                Constant.ASYNCPUT, new JSONObject(), context);
                        LOG.warn(
                                "function=updateVim, msg=VimManager enter updateVim >>>  sentEvtByRest, vimId is {}.",
                                vim.getId());
                        return result;
                    }

                    LOG.error("function=updateVim, msg=update fail,update database error");
                    result.setOperateStatus(VimOpResult.TaskStatus.FAIL);
                    result.setErrorMessage(ResourceUtil
                            .getMessage("org.openo.nfvo.vimadapter.service.update.db.error"));
                    return result;
                }

                LOG.error("function=updateVim, msg=update fail, ret=" + retCode);
                result.setOperateStatus(VimOpResult.TaskStatus.FAIL);
                result.setErrorMessage(ResourceUtil
                        .getMessage("org.openo.nfvo.vimadapter.service.update.vim.fail"));
                return result;
            }

            LOG.error("function=updateVim, msg=update failed, vim=" + vim);
            return result;
        } catch (ServiceException e) {
            LOG.error("function=updateVim, msg=VimManager update vim fail" + e);
            result.setOperateStatus(VimOpResult.TaskStatus.FAIL);
            result.setErrorMessage(ResourceUtil
                    .getMessage("org.openo.nfvo.vimadapter.service.update.vim.exception"));
        }

        return result;

    }

    @Override
    public VimOpResult deleteVim(String vimDn, HttpContext context) {
        LOG.info("function=deleteVim, msg=VimManager enter to delete a vim, vimDn="
                + vimDn);

        VimOpResult result = new VimOpResult();
        Vim vim = vimDao.getVimById(vimDn);
        if (vim == null) {
            LOG.error("function=deleteVim, msg=Delete Vim: Vim not exists, vimDn="
                    + vimDn);
            result.setOperateStatus(VimOpResult.TaskStatus.FAIL);
            result.setErrorMessage(ResourceUtil
                    .getMessage("org.openo.nfvo.vimadapter.service.del.no.vim"));
            return result;
        }
        boolean flag = delVimRes(vim.getId(), context);
        if (!flag) {
            LOG.error("function=deleteVim, msg=Vim is used by site, vimDn="
                    + vimDn);
            result.setOperateStatus(VimOpResult.TaskStatus.FAIL);
            result.setErrorMessage(ResourceUtil
                    .getMessage("org.openo.nfvo.vimadapter.service.del.used.vim"));
            return result;
        }

        try {
            vim.setPwd(CryptUtil.deCrypt(vim.getPwd()));
            vim.setExtraInfo(VcenterUtil.cryptVcenterPwd(vim.getExtraInfo(),
                    false));

            createConnectMgr(vim.getType()).connect(vim, Constant.DEL);
            vimDao.deleteVim(vimDn);
            result.setOperateStatus(VimOpResult.TaskStatus.SUCCESS);
            result.setErrorMessage(ResourceUtil
                    .getMessage("org.openo.nfvo.vimadapter.service.del.vim.succ"));
        } catch (ServiceException e) {
            LOG.error("function=deleteVim, msgVimManager delete vim fail" + e);
            result.setOperateStatus(VimOpResult.TaskStatus.FAIL);
            result.setErrorMessage(ResourceUtil
                    .getMessage("org.openo.nfvo.vimadapter.service.del.vim.exception"));
        }

        return result;

    }

    @Override
    public VimOpResult getVim(String vimDn) {
        VimOpResult result = new VimOpResult();

        Vim vim = vimDao.getVimById(vimDn);

        if (null == vim) {
            LOG.error("function=getVim, msg=Get Vim: Vim not exists, vimDn="
                    + vimDn);
            result.setOperateStatus(VimOpResult.TaskStatus.FAIL);
            result.setErrorMessage(ResourceUtil
                    .getMessage("org.openo.nfvo.vimadapter.service.get.no.vim"));
            return result;
        }

        result.setOperateStatus(VimOpResult.TaskStatus.SUCCESS);
        vim.setExtraInfo(VcenterUtil.hideVcenterPwd(vim.getExtraInfo()));
        result.addResult(vim.genVimResultMap());

        return result;

    }

    @Override
    public VimOpResult indexVims(int pageSize, int pageNo) {
        VimOpResult result = new VimOpResult();
        try {
            List<Vim> vimList = vimDao.indexVims(pageSize, pageNo);
            for (Vim vim : vimList) {
                vim.setExtraInfo(VcenterUtil.hideVcenterPwd(vim.getExtraInfo()));
                result.addResult(vim.genVimResultMap());
            }
            result.setOperateStatus(TaskStatus.SUCCESS);
        } catch (ServiceException e) {
            LOG.error("function=indexVims, msg=VimManager index vim fail" + e);
            result.setOperateStatus(TaskStatus.FAIL);
            result.setErrorMessage(ResourceUtil
                    .getMessage("org.openo.nfvo.vimadapter.service.get.vim.fail"));
        }

        return result;

    }

    @Override
    public void checkVimStatus(boolean flag) {
        try {
            List<Vim> vimList = vimDao.indexVims(0, 0);
            for (Vim vim : vimList) {
                handShakeByVim(vim, flag);
            }
        } catch (ServiceException e) {
            LOG.error("function=checkVimStatus, msg=ServiceException, Info="
                    + e);
        }
    }

    private void handShakeByVim(Vim vim, boolean flag) throws ServiceException {
        LOG.debug("function=handShakeByVim, msg=enter");

        vim.setPwd(CryptUtil.deCrypt(vim.getPwd()));
        vim.setExtraInfo(VcenterUtil.cryptVcenterPwd(vim.getExtraInfo(), false));

        if (flag) {
            createConnectMgr(vim.getType()).connect(vim,
                    Constant.FIRST_HANDSHAKE);
        }

        int status = createConnectMgr(vim.getType()).connect(vim,
                Constant.HANDSHAKE);

        if (status != Constant.HTTP_OK) {
            int ret = createConnectMgr(vim.getType()).connect(vim,
                    Constant.FIRST_HANDSHAKE);
            if (ret == Constant.HTTP_OK) {
                status = createConnectMgr(vim.getType()).connect(vim,
                        Constant.HANDSHAKE);
            }
        }

        if (status == Constant.HTTP_OK) {
            if (Constant.INACTIVE.equalsIgnoreCase(vim.getStatus())) {
                LOG.debug("function=handShakeByVim, msg=set status active");
                vim.setPwd(CryptUtil.enCrypt(vim.getPwd()));
                vim.setExtraInfo(VcenterUtil.cryptVcenterPwd(
                        vim.getExtraInfo(), true));
                vim.setStatus(Constant.ACTIVE);
                vim.setUpdateAt(new Date());
                vimDao.updateVim(vim);

                JSONObject paramJson = new JSONObject();
                paramJson.put("vimId", vim.getId());
                paramJson.put("status", vim.getStatus());
                EventUtil.sentEvtByRest(UrlConstant.REST_EVENT_STATUS,
                        Constant.ASYNCPUT, paramJson, null);
                LOG.warn(
                        "function=handShakeByVim, msg=VimManager enter handShakeByVim >>>  sentEvtByRest, json is {}.",
                        paramJson);

                AuditLog.logInstance(Constant.PARAM_MODULE)
                        .addDetail(
                                "HandShakeTask success . updatedo status ACTIVE ")
                        .addLevel(LogSeverity.WARNING)
                        .addOperation("HandShakeTask")
                        .addResult(Result.SUCCESSFUL)
                        .addTargetObj(Constant.PARAM_MODULE)
                        .generateSystemLog();

            }
        } else {
            if (Constant.ACTIVE.equalsIgnoreCase(vim.getStatus())) {
                LOG.debug("function=handShakeByVim, msg=set status inactive");
                vim.setPwd(CryptUtil.enCrypt(vim.getPwd()));
                vim.setExtraInfo(VcenterUtil.cryptVcenterPwd(
                        vim.getExtraInfo(), true));
                vim.setStatus(Constant.INACTIVE);
                vim.setUpdateAt(new Date());
                vimDao.updateVim(vim);
                JSONObject paramJson = new JSONObject();
                paramJson.put("vimId", vim.getId());
                paramJson.put("status", vim.getStatus());
                EventUtil.sentEvtByRest(UrlConstant.REST_EVENT_STATUS,
                        Constant.ASYNCPUT, paramJson, null);
                LOG.warn(
                        "function=handShakeByVim, msg=VimManager enter handShakeByVim >>>  sentEvtByRest, json is {}.",
                        paramJson);

                AuditLog.logInstance(Constant.PARAM_MODULE)
                        .addDetail(
                                "HandShakeTask fail . updatedo status INACTIVE ")
                        .addLevel(LogSeverity.MINOR)
                        .addOperation("HandShakeTask")
                        .addResult(Result.FAILURE)
                        .addTargetObj(Constant.PARAM_MODULE)
                        .generateSystemLog();
            }
        }
    }

    @Override
    public VimOpResult deleteVims(List<String> vimIdList, HttpContext context) {
        LOG.info("function=deleteVims, msg=VimManager enter to delete vims, vimDn="
                + vimIdList);
        VimOpResult result = new VimOpResult();
        List<String> delFailList = new ArrayList<String>(
                Constant.DEFAULT_COLLECTION_SIZE);
        Vim vim = null;
        String vimId = null;

        try {
            boolean flag = false;

            for (String vimDn : vimIdList) {
                vimId = vimDn;
                vim = vimDao.getVimById(vimDn);
                if (vim == null) {
                    LOG.warn("function=deleteVims, msg=Delete Vim: Vim not exists, vimDn="
                            + vimDn);
                    delFailList.add(vimDn);
                    continue;
                }

                flag = delVimRes(vim.getId(), context);
                if (!flag) {
                    LOG.warn("function=deleteVims, msg=Vim is used by site, vimDn="
                            + vimDn);
                    delFailList.add(vimDn);
                    continue;
                }

                createConnectMgr(vim.getType()).connect(vim, Constant.DEL);
                vimDao.deleteVim(vimDn);
            }

        } catch (ServiceException e) {
            LOG.error("function=deleteVim, msgVimManager delete vim fail" + e);
            result.setOperateStatus(VimOpResult.TaskStatus.PART_SUCCESS);
            delFailList.add(vimId);
        }

        if (delFailList.isEmpty()) {
            result.setOperateStatus(VimOpResult.TaskStatus.SUCCESS);
        } else if (delFailList.size() == vimIdList.size()) {
            result.setOperateStatus(VimOpResult.TaskStatus.FAIL);
        } else {
            result.setOperateStatus(VimOpResult.TaskStatus.PART_SUCCESS);
            result.addResult(delFailList);
        }

        return result;
    }

    private boolean validateVimData(VimOpResult result, Vim vim) {
        String name = vim.getName();
        String type = vim.getType();
        List<Vim> vims;
        try {
            vims = Collections.unmodifiableList(vimDao.indexVims(0, 0));
        } catch (ServiceException e) {
            LOG.error(
                    "function=validateVimData, msg=serviceException occurs, e = {}.",
                    e);
            return false;
        }
        if (!Constant.VIMTYPELIST.contains(type)) {
            LOG.warn("function=validateVimData.msg=The type is unvalidate, type:"
                    + type);
            result.setOperateStatus(VimOpResult.TaskStatus.FAIL);
            result.setErrorMessage(ResourceUtil
                    .getMessage("org.openo.nfvo.vimadapter.service.param.type"));
            return false;
        }
        if (!checkVimName(name, vims, result)) {
            LOG.warn("function=validateVimData.msg=The name is unvalidate, name:"
                    + name);
            return false;
        }
        String url = vim.getUrl();
        String extraInfo = vim.getExtraInfo();
        
        if (!checkVimUrl(url, type, extraInfo, vims, result)) {
            LOG.warn("function=validateVimData.msg=The url is unvalidate, url:"
                    + url);
            return false;
        }

        JSONObject extraInfoJsonObject = JSONObject.fromObject(extraInfo);
        String authenticMode = extraInfoJsonObject.getString("authenticMode");

        if (!Constant.AUTHLIST.contains(authenticMode)) {
            LOG.error("function=validateVimData, msg=The extraInfo is invalid");
            result.setOperateStatus(VimOpResult.TaskStatus.FAIL);
            result.setErrorMessage(ResourceUtil
                    .getMessage("org.openo.nfvo.vimadapter.service.param.error.extraInfo"));
            return false;
        }

        if (!checkVcenterInfo(extraInfoJsonObject, type)) {
            LOG.error("function=validateVimData, msg=The vcenter info is invalid");
            result.setOperateStatus(VimOpResult.TaskStatus.FAIL);
            result.setErrorMessage(ResourceUtil
                    .getMessage("org.openo.nfvo.vimadapter.service.param.vcenter.error"));
            return false;
        }

        return true;
    }
    
    private boolean checkUrl(VimOpResult result, Vim vim) {
        String type = vim.getType();
        String url = vim.getUrl();
        
        if (validLen(url)) {
            LOG.error("function=checkVimUrl, msg=The url is unvalidate, url="
                    + url);
            result.setOperateStatus(VimOpResult.TaskStatus.FAIL);
            result.setErrorMessage(ResourceUtil
                    .getMessage("org.openo.nfvo.vimadapter.service.param.error.URL.format"));
            return false;
        }
        if (Constant.OPENSTACK.equals(type)) {
            String regex = "http[s]?://.*";
            Pattern pattern = Pattern.compile(regex);
            Matcher mat = pattern.matcher(url);
            if (!mat.matches()) {
                result.setOperateStatus(VimOpResult.TaskStatus.FAIL);
                result.setErrorMessage(ResourceUtil
                        .getMessage("org.openo.nfvo.vimadapter.service.param.error.URL.format"));
                return false;
            }
        }
        
        return true;
    }

    private boolean checkVcenterInfo(JSONObject extraInfoJsonObject, String type) {
        if (Constant.VCLOUD.equals(type)
                && extraInfoJsonObject.containsKey("vcenterInfo")) {
            JSONArray vcList = extraInfoJsonObject.getJSONArray("vcenterInfo");
            int vcSize = vcList.size();
            HashSet<String> set = new HashSet<String>(
                    Constant.DEFAULT_COLLECTION_SIZE);

            for (int i = 0; i < vcSize; i++) {
                JSONObject vcenterInfo = vcList.getJSONObject(i);
                String name = vcenterInfo.getString("name");
                Pattern p = Pattern.compile("^[a-z0-9A-Z_]{1,64}$");
                Matcher m = p.matcher(name);
                if (!m.matches()) {
                    LOG.error("function=checkVcenterName, msg=vCenter name is invalid, name="
                            + name);
                    return false;
                }
                set.add(vcenterInfo.getString("url").replace("https://", "")
                        .split(":")[0]);
            }

            if (vcSize != set.size()) {
                LOG.error("function=checkVcenterName, msg=vCenter is duplicate");
                return false;
            }
        }

        return true;
    }

    private boolean checkVimName(String name, List<Vim> vims, VimOpResult result) {
        Pattern p = Pattern.compile("[a-z0-9A-Z_]+");
        Matcher m = p.matcher(name);
        if (!m.matches()) {
            LOG.error("function=checkVimName, msg=The name contains special character , name="
                    + name);
            result.setOperateStatus(VimOpResult.TaskStatus.FAIL);
            result.setErrorMessage(ResourceUtil
                    .getMessage("org.openo.nfvo.vimadapter.service.param.error.name"));
            return false;
        }
        if (name.length() < Constant.MIN_VIM_NAME_LENGTH
                || name.length() > Constant.MAX_VIM_NAME_LENGTH) {
            LOG.error("function=checkVimName, msg=The name is unvalidate, name="
                    + name);
            result.setOperateStatus(VimOpResult.TaskStatus.FAIL);
            result.setErrorMessage(ResourceUtil
                    .getMessage("org.openo.nfvo.vimadapter.service.param.error.name"));
            return false;
        }
        for (Vim vim : vims) {
            if (name.equals(vim.getName())) {
                LOG.error("function=checkVimName, msg=The name has exist, name="
                        + name);
                result.setOperateStatus(VimOpResult.TaskStatus.FAIL);
                result.setErrorMessage(ResourceUtil
                        .getMessage("org.openo.nfvo.vimadapter.service.param.error.name.exist"));
                return false;
            }
        }
        return true;
    }

    private boolean checkVimUpdateName(String name, String vimId) {
        if (name.length() < Constant.MIN_VIM_NAME_LENGTH
                || name.length() > Constant.MAX_VIM_NAME_LENGTH) {
            LOG.error("function=checkVimUpdateName, msg=The name is unvalidate, name="
                    + name);
            return false;
        }
        int countUpdateName = vimDao.getVimByUpdateName(name, vimId);
        if (countUpdateName != 0) {
            LOG.error("function=checkVimUpdateName, msg=The name has been used, name="
                    + name);
            return false;
        }
        return true;
    }
    
    private boolean validLen(String url) {
        return url.length() < Constant.MIN_URL_LENGTH
                || url.length() > Constant.MAX_URL_LENGTH;
    }
    
    private boolean validType(String url, JSONObject extraInfoJsonObject, String extraInfo) {
        return !extraInfoJsonObject.containsKey("orgName")
                || !extraInfoJsonObject.containsKey("authenticMode")
                || !extraInfoJsonObject.containsKey("vcenterInfo")
                || !checkUrlFormat(url, extraInfo);
    }
    
    private boolean validDomin(String type, JSONObject extraInfoJsonObject) {
       return (Constant.OPENSTACK.equals(type)) 
        && (!extraInfoJsonObject.containsKey("domain")
                ||!extraInfoJsonObject.containsKey("authenticMode"));
    }

    private boolean checkVimUrl(String url, String type, String extraInfo,
            List<Vim> vims, VimOpResult result) {
        JSONObject extraInfoJsonObject = JSONObject.fromObject(extraInfo);
 
        if (Constant.VCLOUD.equals(type)
            &&  validType(url, extraInfoJsonObject, extraInfo)) {
                LOG.error(
                        "function=checkVimUrl, msg=The url is not ip, url={}, extraInfo={}",
                        url, extraInfo);
                result.setOperateStatus(VimOpResult.TaskStatus.FAIL);
                result.setErrorMessage(ResourceUtil
                        .getMessage("org.openo.nfvo.vimadapter.service.param.error.URL.format"));
                return false;
        } 
        
        if (validDomin(url, extraInfoJsonObject)) {
                result.setOperateStatus(VimOpResult.TaskStatus.FAIL);
                result.setErrorMessage(ResourceUtil
                        .getMessage("org.openo.nfvo.vimadapter.service.param.error.extraInfo"));
                return false;
        }

        for (Vim vim : vims) {
            try {
                if (StringUtil.isSameUrlIgnorePort(url, vim.getUrl(), type,
                        vim.getType())) {
                    LOG.error(
                            "function=checkVimUrl, msg=The url has exist, param url is {}",
                            url);
                    result.setOperateStatus(VimOpResult.TaskStatus.FAIL);
                    result.setErrorMessage(ResourceUtil
                            .getMessage("org.openo.nfvo.vimadapter.service.param.error.URL.exist"));
                    return false;
                }
            } catch (MalformedURLException e) {
                LOG.error(
                        "function=checkVimUrl, msg=param or url from db isn't legal format. e= {}",
                        e);
                result.setOperateStatus(VimOpResult.TaskStatus.FAIL);
                result.setErrorMessage(ResourceUtil
                        .getMessage("org.openo.nfvo.vimadapter.service.param.error.URL.format"));
                return false;
            }
        }
        return true;
    }

    private static boolean checkUrlFormat(String url, String extraInfo) {
        if (!VcenterUtil.checkVcloudUrlFormat(url)) {
            LOG.error("function=checkUrlFormat, msg=The VcloudUrl is not ip, url="
                    + url);
            return false;
        }

        if (!VcenterUtil.checkVcenterUrlFormat(extraInfo)) {
            LOG.error("function=checkUrlFormat, msg=The VcenterUrl is not ip, extraInfo="
                    + extraInfo);
            return false;
        }
        return true;
    }

    private boolean validateUpdateData(Vim argVim, VimOpResult result) {
        boolean isEnableName = true;
        boolean isEnableUserName = true;
        if (null != argVim && null != argVim.getName()) {
            Pattern p = Pattern.compile("[a-z0-9A-Z_]+");
            Matcher m = p.matcher(argVim.getName());
            if (!m.matches()) {
                result.setOperateStatus(VimOpResult.TaskStatus.FAIL);
                result.setErrorMessage(ResourceUtil
                        .getMessage("org.openo.nfvo.vimadapter.service.param.error.name"));
                return false;
            }
            isEnableName = checkVimUpdateName(argVim.getName(), argVim.getId());
        }
        if (null != argVim && null != argVim.getUserName()) {
            Pattern p = Pattern.compile("[a-z0-9A-Z_]+");
            Matcher m = p.matcher(argVim.getUserName());
            isEnableUserName = m.matches();
            if (!m.matches()) {
                result.setOperateStatus(VimOpResult.TaskStatus.FAIL);
                result.setErrorMessage(ResourceUtil
                        .getMessage("org.openo.nfvo.vimadapter.service.param.error.username"));
            }
        }
        boolean isPass = isEnableName && isEnableUserName;

        if (isPass) {
            LOG.info("function=validateUpdateData, msg=update validate is pass");
        } else {
            LOG.error("function=validateUpdateData, msg=update validate failed");
        }
        return isPass;
    }

    private boolean delVimRes(String vimId, HttpContext context) {
        RestfulParametes params = new RestfulParametes();
        params.put("vimId", vimId);
        RestfulResponse resmgrResponse = RestfulUtil.getResponseResult(
                UrlConstant.DEL_VIM_RES, params, RestfulUtil.TYPE_DEL, context);

        return AdapterUtil.getResponseFromResmgr(resmgrResponse);
    }
}
