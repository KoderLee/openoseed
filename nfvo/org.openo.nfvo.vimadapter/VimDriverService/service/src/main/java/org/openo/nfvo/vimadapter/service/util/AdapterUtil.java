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

package org.openo.nfvo.vimadapter.service.util;

import org.apache.commons.lang3.StringUtils;
import org.openo.nfvo.vimadapter.util.NumberUtil;
import org.openo.nfvo.vimadapter.util.constant.Constant;

import net.sf.json.JSONObject;

import org.openo.baseservice.log.OssLog;
import org.openo.baseservice.log.OssLogFactory;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;

/**
 * 
* used for validate the parameter of creating network<br/>
* <p>
* </p>
* 
* @author
* @version NFVO 0.5 May 15, 2016
 */
public final class AdapterUtil {
    private static final OssLog LOG = OssLogFactory.getLog(AdapterUtil.class);

    private AdapterUtil() {

    }

    public static boolean getResponseFromResmgr(RestfulResponse dbResponse) {
        if (dbResponse != null && dbResponse.getStatus() == Constant.HTTP_OK) {
            JSONObject rpJson = JSONObject.fromObject(dbResponse
                    .getResponseContent());
            return rpJson.getInt("retCode") == Constant.REST_SUCCESS;
        }
        return false;
    }

    /**
     * Check the parameter of creating a network<br/>
     * 
     * @param network: the network info to be added
     * @return the parameter valid or not.
     * @since  NFVO 0.5 
     */
    public static boolean checkAddNetworkData(JSONObject network) {
        String name = network.getString("name");
        String id = network.getString("id");
        String type = network.getString("type");
        String physicalnet = network.getString("physicalNet");
        String rpid = network.getString("rpId");
        String segmentation = network.getString("segmentation");
        String projectId = network.getString("projectId");

        if (!checkBasicInfo(id, name, type, physicalnet, rpid, projectId)) {
            LOG.error("function=checkBasicInfo.msg=The basic info is invalid.");
            return false;
        }
        if (!checkVlanId(type, segmentation)) {
            LOG.error("function=checkVlanId.msg=The segmentation is invalid.");
            return false;
        }

        return true;
    }

    public static boolean checkBasicInfo(String id, String name, String type,
            String physicalNet, String rpId, String projectId) {
        if (StringUtils.isEmpty(id) || StringUtils.isEmpty(name)
                || StringUtils.isEmpty(type)
                || StringUtils.isEmpty(physicalNet)
                || StringUtils.isEmpty(rpId) || StringUtils.isEmpty(projectId)) {
            LOG.error(
                    "function=checkBasicInfo.msg=ID,missing required info.id={},name={},type={},physicalNet={},rpId={}",
                    id, name, type, physicalNet, rpId);
            return false;
        }
        return true;
    }

    public static boolean checkVlanId(String type, String segmentation) {
        boolean flag = true;
        if (("vlan").equals(type)) {
            if (StringUtils.isEmpty(segmentation)) {
                flag = false;
            }
            if (!NumberUtil.isNumeric(segmentation)) {
                flag = false;
            }
            int vlanid = Integer.parseInt(segmentation);
            if ((vlanid < Constant.MIN_VLANID)
                    || (vlanid > Constant.MAX_VLANID)) {
                flag = false;
            }
        }
        return flag;
    }

}
