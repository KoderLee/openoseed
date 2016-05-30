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
package org.openo.crossdomain.commonsvc.decomposer.model;

import org.openo.crossdomain.commonsvc.decomposer.check.CheckAttr;
import org.openo.crossdomain.commonsvc.decomposer.check.CheckType;
import org.openo.crossdomain.commonsvc.decomposer.constant.Constant;
import org.springframework.util.StringUtils;

import net.sf.json.JSONObject;

/**
 * service resource information
 * 
 * @since crossdomain 0.5
 */
public class ResMapping {

	@CheckAttr(type = CheckType.String, required = true, max = 64)
	private String tenantID;

	@CheckAttr(type = CheckType.String, required = true, max = 64)
	private String serviceID;

	@CheckAttr(type = CheckType.String, required = true, max = 64)
	private String resourceID;

	@CheckAttr(type = CheckType.String, required = true, max = 64)
	private String resourceLabel;

	@CheckAttr(type = CheckType.String, required = true, max = 32)
	private String type;

	private String version;

	private String resourceType;

	private String depends;

	private String acvtiveMode;

	private String result;

	private String resultReason;

	public ResMapping() {
		super();
	}

	public ResMapping(String tenantID, String serviceID, String resourceID,
			String resourceLabel, String type, String version,
			String resourceType, String depends, String acvtiveMode) {
		this.tenantID = tenantID;
		this.serviceID = serviceID;
		this.resourceID = resourceID;
		this.resourceLabel = resourceLabel;
		this.type = type;
		this.version = version;
		this.resourceType = resourceType;
		this.depends = depends;
		this.acvtiveMode = acvtiveMode;
	}

	/**
	 * @return Returns the serviceID.
	 */
	public String getServiceID() {
		return serviceID;
	}

	/**
	 * @param serviceID The serviceID to set.
	 */
	public void setServiceID(String serviceID) {
		this.serviceID = serviceID;
	}

	/**
	 * @return Returns the resourceID.
	 */
	public String getResourceID() {
		return resourceID;
	}

	/**
	 * @param resourceID The resourceID to set.
	 */
	public void setResourceID(String resourceID) {
		this.resourceID = resourceID;
	}

	/**
	 * @return Returns the resourceLabel.
	 */
	public String getResourceLabel() {
		return resourceLabel;
	}

	/**
	 * @param resourceLabel The resourceLabel to set.
	 */
	public void setResourceLabel(String resourceLabel) {
		this.resourceLabel = resourceLabel;
	}

	/**
	 * @return Returns the type.
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type The type to set.
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return Returns the version.
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version The version to set.
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @return Returns the resourceType.
	 */
	public String getResourceType() {
		return resourceType;
	}

	/**
	 * @param resourceType The resourceType to set.
	 */
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	/**
	 * @return Returns the depends.
	 */
	public String getDepends() {
		return depends;
	}

	/**
	 * @param depends The depends to set.
	 */
	public void setDepends(String depends) {
		this.depends = depends;
	}

	/**
	 * @return Returns the acvtiveMode.
	 */
	public String getAcvtiveMode() {
		return acvtiveMode;
	}

	/**
	 * @param acvtiveMode The acvtiveMode to set.
	 */
	public void setAcvtiveMode(String acvtiveMode) {
		this.acvtiveMode = acvtiveMode;
	}

	/**
	 * @return Returns the result.
	 */
	public String getResult() {
		return result;
	}

	/**
	 * @param result The result to set.
	 */
	public void setResult(String result) {
		this.result = result;
	}

	/**
	 * @return Returns the resultReason.
	 */
	public String getResultReason() {
		return resultReason;
	}

	/**
	 * @param resultReason The resultReason to set.
	 */
	public void setResultReason(String resultReason) {
		this.resultReason = resultReason;
	}

	/**
	 * @return Returns the tenantID.
	 */
	public String getTenantID() {
		return tenantID;
	}

	/**
	 * @param tenantID The tenantID to set.
	 */
	public void setTenantID(String tenantID) {
		this.tenantID = tenantID;
	}

	public JSONObject toJson(final String taskStatus) {
		JSONObject oneArrayElement = new JSONObject();

		converNotNullJsonObj(oneArrayElement, Constant.STATUS, taskStatus);
		oneArrayElement.put(Constant.ID, getResourceID());
		oneArrayElement.put(Constant.TYPE, getResourceType());

		JSONObject resultJsonObject = new JSONObject();
		resultJsonObject.put(Constant.TaskCommon.CODE, getResult());
		resultJsonObject.put(Constant.TaskCommon.REASON, getResultReason());
		oneArrayElement.put(Constant.RESULT, resultJsonObject);

		converNotNullJsonObj(oneArrayElement, Constant.ACTIVE_STATUS,
				getAcvtiveMode());
		converNotNullJsonObj(oneArrayElement, Constant.VERSION, getVersion());
		converNotNullJsonObj(oneArrayElement, Constant.DEPENDS, getDepends());
		return oneArrayElement;
	}

	private static void converNotNullJsonObj(final JSONObject json,
			final String key, final Object value) {
		if (StringUtils.hasLength(key)) {
			if ((value != null) && StringUtils.hasLength(value.toString())) {
				json.put(key, value);
			}
		}
	}

	public JSONObject toDesignerJson() {
		JSONObject jsonRes = new JSONObject();
		jsonRes.put(Constant.ID, getResourceID());
		jsonRes.put(Constant.TYPE, getResourceType());
		jsonRes.put(Constant.DEPENDS, getDepends());
		return jsonRes;
	}
}
