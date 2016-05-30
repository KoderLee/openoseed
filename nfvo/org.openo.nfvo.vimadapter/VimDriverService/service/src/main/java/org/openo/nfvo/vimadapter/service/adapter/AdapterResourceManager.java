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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.openo.baseservice.i18n.ResourceUtil;
import org.openo.baseservice.log.OssLog;
import org.openo.baseservice.log.OssLogFactory;
import org.openo.baseservice.remoteservice.exception.ServiceException;

import net.sf.json.JSONObject;

import org.omg.CORBA.IntHolder;
import org.openo.nfvo.vimadapter.util.InterfaceResourceManager;
import org.openo.nfvo.vimadapter.util.InterfaceResourceMgr;
import org.openo.nfvo.vimadapter.util.Vim;
import org.openo.nfvo.vimadapter.util.VimOpResult;
import org.openo.nfvo.vimadapter.util.constant.Constant;
import org.openo.nfvo.vimadapter.util.dao.VimDao;
import org.openo.nfvo.vimadapter.openstack.entry.ResourceMgrOpenstack;

/**
 * 
* Used for adapte different VIM, supply same rest call for upper layer to get resource<br/>
* <p>
* </p>
* 
* @author
* @version NFVO 0.5 May 15, 2016
 */
public class AdapterResourceManager implements InterfaceResourceManager {
    private final static OssLog LOG = OssLogFactory
            .getLogger(AdapterResourceManager.class);

    private VimDao vimDao = null;

    public void setVimDao(VimDao vimDao) {
        this.vimDao = vimDao;
    }

    public synchronized static InterfaceResourceMgr createResourceMgr(String type) {
        InterfaceResourceMgr resourceMgr = null;
        if (Constant.OPENSTACK.equalsIgnoreCase(type)) {
            resourceMgr = new ResourceMgrOpenstack();
        } else {
            LOG.error("function=createResourceMgr, msg=Invalid VIM type");
        }

        return resourceMgr;
    }

    @Override
    public VimOpResult getRps(JSONObject paramJson) {
        return getInvokeResult("getRps", paramJson);
    }

    @Override
    public VimOpResult getVendors(JSONObject paramJson) {
        return getInvokeResult("getVendors", paramJson);
    }

    @Override
    public VimOpResult getVms(JSONObject paramJson) {
        return getInvokeResult("getVms", paramJson);
    }

    @Override
    public VimOpResult getNetworks(JSONObject paramJson) {
        return getInvokeResult("getNetworks", paramJson);
    }

    public List<Vim> getVimList(int pageSize, int pageNo) {
        try {
            return vimDao.indexVims(pageSize, pageNo);
        } catch (ServiceException e) {
            LOG.error("function=\"getVims\", msg=\"find vims error.\" e={}",
                    e);
        }
        return Collections.emptyList();
    }

    public VimOpResult getInvokeResult(String methodName, JSONObject paramJson) {
        LOG.info(
                "function=getInvokeResult, msg={} is invoked, and paramJson={}",
                methodName, paramJson);
        Objects.requireNonNull(methodName);

        VimOpResult vimOpResult = new VimOpResult();
        if (null == paramJson.get("vim") && paramJson.get("id") != null) {
            LOG.error("function=getInvokeResult, msg=get vim fail, vimId is null.");
            vimOpResult.setOperateStatus(VimOpResult.TaskStatus.FAIL);
            vimOpResult.setErrorMessage(ResourceUtil
                    .getMessage("org.openo.nfvo.vimadapter.service.null.id"));
            return vimOpResult;
        }
        List<Vim> vimList = new ArrayList<Vim>(Constant.DEFAULT_COLLECTION_SIZE);
        if (null == paramJson.get("vim")) {
            vimList = getVimList(0, 0);
        } else {
            String vimDn = paramJson.getString("vim");
            if (null != (vimDao.getVimById(vimDn))) {
                vimList.add(vimDao.getVimById(vimDn));
            } else {
                LOG.error(
                        "function=getInvokeResult, msg=get vim fail, vimId={}",
                        vimDn);
                vimOpResult.setOperateStatus(VimOpResult.TaskStatus.FAIL);
                vimOpResult
                        .setErrorMessage(ResourceUtil
                                .getMessage("org.openo.nfvo.vimadapter.service.get.no.vim"));
                return vimOpResult;
            }
        }
        IntHolder holder = new IntHolder();
        for (Vim vim : vimList) {
            Object resource = getResource(methodName, paramJson, vim, holder);
            if (resource != null) {
                if (resource instanceof List) {
                    vimOpResult.gotResult().addAll((List<?>) resource);
                } else {
                    vimOpResult.gotResult().add(resource);
                }
            }
        }
        if (holder.value == 0 || holder.value != vimList.size()) {
            vimOpResult.setOperateStatus(VimOpResult.TaskStatus.SUCCESS);
            vimOpResult
                    .setErrorMessage(ResourceUtil
                            .getMessage("org.openo.nfvo.vimadapter.service.query.succ"));
            LOG.info("function=getInvokeResult, msg=query success from server.");
            return vimOpResult;
        }

        LOG.error("function=getInvokeResult, msg=query failed from server.");
        vimOpResult.setOperateStatus(VimOpResult.TaskStatus.FAIL);
        vimOpResult.setErrorMessage(ResourceUtil
                .getMessage("org.openo.nfvo.vimadapter.service.query.fail"));
        return vimOpResult;
    }

    private Object getResource(String methodName, JSONObject paramJson,
            Vim vim, IntHolder holder) {
        IResourceMgr mgr = createResourceMgr(vim.getType());
        if (mgr != null) {
            try {
                Method method = IResourceMgr.class.getMethod(methodName,
                        Map.class, JSONObject.class);
                if (!method.isAccessible()) {
                    method.setAccessible(true);
                }
                Object obj = method.invoke(mgr, vim.generateConMap(paramJson),
                        paramJson);
                if (null == obj) {
                    holder.value += 1;
                }
                return obj;
            } catch (ReflectiveOperationException e) {
                LOG.error(
                        "function=getInvokeResult, msg=ReflectiveOperationException occurs, e={}",
                        e);
            }
        } else {
            LOG.error(
                    "function=getInvokeResult, msg=the vim type is not supported, vimDn={}, type={}",
                    vim.getId(), vim.getType());
        }
        holder.value += 1;
        return null;
    }
}
