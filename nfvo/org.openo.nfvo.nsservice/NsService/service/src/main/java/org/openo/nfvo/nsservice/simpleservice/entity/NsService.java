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

package org.openo.nfvo.nsservice.simpleservice.entity;

import org.apache.commons.lang3.StringUtils;
import org.openo.nfvo.nsservice.api.CheckEntity;
import org.openo.nfvo.nsservice.api.EncryptionUtil;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.nfvo.common.utils.JsonUtil;

import net.sf.json.JSONObject;

/**
 * 
* The class of NsService, include basic info about a network service<br/>
* <p>
* </p>
* 
* @author
* @version NFVO 0.5 May 15, 2016
 */
public class NsService {

    /**
     * This field corresponds to the database column nsservice.ID
     */
    private String id;

    /**
     * This field corresponds to the database column nsservice.SERVICE_ID
     */
    private String serviceId;

    /**
     * This field corresponds to the database column nsservice.NAME
     */
    private String name;

    /**
     * This field corresponds to the database column nsservice.ACTION
     */
    private String action;

    /**
     * This field corresponds to the database column nsservice.RESOURCES
     */
    private String resources;

    /**
     * return the value of nsservice.ID
     */
    public String getId() {
        return id;
    }

    /**
     * This method sets the value of the database column nsservice.ID.
     * 
     * @param id the value for nsservice.ID
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * return the value of nsservice.SERVICE_ID
     */
    public String getServiceId() {
        return serviceId;
    }

    /**
     * This method sets the value of the database column nsservice.SERVICE_ID.
     * 
     * @param serviceId the value for nsservice.SERVICE_ID
     */
    public void setServiceId(String serviceId) {
        this.serviceId = serviceId == null ? null : serviceId.trim();
    }

    /**
     * return the value of nsservice.NAME
     */
    public String getName() {
        return name;
    }

    /**
     * This method sets the value of the database column nsservice.NAME.
     * 
     * @param name the value for nsservice.NAME
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * return the value of nsservice.ACTION
     */
    public String getAction() {
        return action;
    }

    /**
     * This method sets the value of the database column nsservice.ACTION.
     * 
     * @param action the value for nsservice.ACTION
     */
    public void setAction(String action) {
        this.action = action == null ? null : action.trim();
    }

    /**
     * return the value of nsservice.RESOURCES
     */
    public String getResources() {
        return resources;
    }

    /**
     * This method sets the value of the database column nsservice.RESOURCES.
     * 
     * @param resources the value for nsservice.RESOURCES
     */
    public void setResources(String resources) {
        this.resources = resources == null ? null : resources.trim();
    }

    public static NsService toEntity(JSONObject jsonObject) throws ServiceException {
        NsService entity = new NsService();
        entity.setId(CheckEntity.checkId(JsonUtil.getJsonFieldStr(jsonObject, "id"), "NsService_Id"));
        entity.setName(CheckEntity.checkAll(JsonUtil.getJsonFieldStr(jsonObject, "name"), "NsService_Name"));
        entity.setServiceId(CheckEntity.checkId(JsonUtil.getJsonFieldStr(jsonObject, "service_id"),
                "NsService_ServiceId"));
        entity.setResources(EncryptionUtil.enCrypt(JsonUtil.getJsonFieldStr(jsonObject, "resources")));
        entity.setAction(CheckEntity.checkAll(JsonUtil.getJsonFieldStr(jsonObject, "action"), "NsService_Id"));
        return entity;
    }

    @Override
    public String toString() {
        JSONObject nsObj = new JSONObject();
        nsObj.put("name", StringUtils.trimToEmpty(this.getName()));
        nsObj.put("service_id", StringUtils.trimToEmpty(this.getServiceId()));
        nsObj.put("action", StringUtils.trimToEmpty(this.getAction()));
        nsObj.put("resources", EncryptionUtil.deCrypt(StringUtils.trimToEmpty(this.getResources())));
        return nsObj.toString();
    }

}
