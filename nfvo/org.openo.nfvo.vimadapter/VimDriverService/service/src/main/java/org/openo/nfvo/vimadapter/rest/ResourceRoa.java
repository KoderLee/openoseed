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

import java.lang.reflect.Method;

import net.sf.json.JSONObject;

import org.openo.baseservice.i18n.ResourceUtil;
import org.openo.baseservice.log.OssLog;
import org.openo.baseservice.log.OssLogFactory;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.annotation.GET;
import org.openo.baseservice.roa.annotation.Path;
import org.openo.baseservice.roa.annotation.QueryParam;
import org.openo.baseservice.roa.annotation.Target;
import org.openo.baseservice.roa.common.HttpContext;
import org.openo.nfvo.vimadapter.util.VimOpResult;
import org.openo.nfvo.vimadapter.util.constant.Constant;
import org.openo.nfvo.vimadapter.service.adapter.AdapterResourceManager;

/**
 * 
* Used for get vendors/rps/networks/vms, send request to VIM.<br/>
* <p>
* </p>
* 
* @author
* @version NFVO 0.5 May 15, 2016
 */
@Path("/vimadapter/v1/res")
@Target("vim-adapter-res")
public class ResourceRoa {
    private final static OssLog LOG = OssLogFactory
            .getLogger(ResourceRoa.class);

    private AdapterResourceManager adapter;

    public void setAdapter(AdapterResourceManager adapter) {
        this.adapter = adapter;
    }

    /**
     * Query resource pool info from VIM, the rest URI:/vimadapter/v1/re/rps<br/>
     * 
     * @return the resource pool info of VIM to be queried 
     * @throws ServiceException: common exception supplied by baseservice
     * @since  NFVO 0.5 
     */
    @GET
    @Path("/rps")
    public String getRp(HttpContext context, @QueryParam("vim") String vim)
            throws ServiceException {
        JSONObject paramJson = new JSONObject();
        paramJson.put("vim", vim);

        return getRoaProxyResults("getRps", paramJson);
    }

    /**
     * Query tenant info from VIM, the rest URI:/vimadapter/v1/re/tenant<br/>
     * 
     * @param context the context of http request
     * @return the tenant info about the VIM to be queried
     * @throws ServiceException: ServiceException common exception supplied by baseservice
     * @since  NFVO 0.5 
     */
    @GET
    @Path("/tenants")
    public String getVendor(HttpContext context, @QueryParam("vim") String vim)
            throws ServiceException {
        JSONObject paramJson = new JSONObject();
        paramJson.put("vim", vim);

        return getRoaProxyResults("getVendors", paramJson);
    }

    /**
     * Query one or all info from VIM, the rest URI:/vimadapter/v1/re/vms<br/>
     * 
     * @return the VM info about the VIM to be queried
     * @throws ServiceException: ServiceException common exception supplied by baseservice
     * @since  NFVO 0.5 
     */
    @GET
    @Path("/vms")
    public String getVm(HttpContext context, @QueryParam("vim") String vim,
            @QueryParam("vm") String id) throws ServiceException {
        JSONObject paramJson = new JSONObject();
        paramJson.put("id", id);
        paramJson.put("vim", vim);

        return getRoaProxyResults("getVms", paramJson);
    }

    /**
     * Query one or all network info from VIM, the rest URI:/vimadapter/v1/re/networks<br/>
     * 
     * @return the network info about the VIM to be queried
     * @throws ServiceException: ServiceException common exception supplied by baseservice
     * @since  NFVO 0.5 
     */
    @GET
    @Path("/networks")
    public String getNetwork(HttpContext context,
            @QueryParam("vim") String vim, @QueryParam("network") String id)
            throws ServiceException {
        JSONObject paramJson = new JSONObject();
        paramJson.put("id", id);
        paramJson.put("vim", vim);

        return getRoaProxyResults("getNetworks", paramJson);
    }

    private String getRoaProxyResults(String methodName, JSONObject paramJson) {
        LOG.info(
                "function=getRoaProxyResults, msg=enter function.methodName={}, paramJson={}",
                methodName, paramJson);
        JSONObject rtnJsonObj = new JSONObject();
        VimOpResult vimOpResult = new VimOpResult();
        try {
            Method method = adapter.getClass().getMethod(methodName,
                    JSONObject.class);
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }
            vimOpResult = (VimOpResult) method.invoke(adapter, paramJson);

            if (vimOpResult.gotOperateStatus() == VimOpResult.TaskStatus.SUCCESS) {
                rtnJsonObj.put("retCode", Constant.REST_SUCCESS);
                rtnJsonObj.put("data", vimOpResult.gotResult());
                return rtnJsonObj.toString();
            }

        } catch (NoSuchMethodException e) {
            LOG.error(
                    "function=getRoaProxyResults, msg=NoSuchMethodException occurs.e={}",
                    e);
        } catch (ReflectiveOperationException e) {
            LOG.error(
                    "function=getRoaProxyResults, msg=ReflectiveOperationException occurs.e={}",
                    e);
        }

        rtnJsonObj.put("retCode", Constant.REST_FAIL);

        if (("").equals(vimOpResult.gotErrorMessage())) {
            rtnJsonObj
                    .put("data",
                            ResourceUtil
                                    .getMessage("org.openo.nfvo.vimadapter.service.roa.query.fail"));
        } else {
            rtnJsonObj.put("data", vimOpResult.gotErrorMessage());
        }

        return rtnJsonObj.toString();
    }

}
