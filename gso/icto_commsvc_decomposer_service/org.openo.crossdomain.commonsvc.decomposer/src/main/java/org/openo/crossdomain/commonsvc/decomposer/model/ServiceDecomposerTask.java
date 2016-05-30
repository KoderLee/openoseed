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

import java.util.HashMap;
import java.util.Map;

import org.openo.crossdomain.commonsvc.decomposer.constant.Constant;
import org.openo.crossdomain.commonsvc.decomposer.logutil.LogUser;

import net.sf.json.JSONObject;

/**
 * service decomposer task
 * 
 * @since crossdomain 0.5
 */
public class ServiceDecomposerTask {

	/**
	 * TaskID
	 */
	private String taskID;

	private String serviceID;

	private String tenantID;

	private String token;

	private String srvVersion;

	private String operType;

	private String seJobID;

	private String serviceContent;

	private String progress;

	private String result;

	private String resultReason;

	private LogUser user;

	public String getProgress() {
		return progress;
	}

	public void setSeJobID(final String seJobID) {
		this.seJobID = seJobID;
	}

	public void setTenantID(final String tenantID) {
		this.tenantID = tenantID;
	}

	/**
	 * @return Returns the token.
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @param token The token to set.
	 */
	public void setToken(String token) {
		this.token = token;
	}

	public String getSeJobID() {
		return seJobID;
	}

	public void setServiceContent(final String serviceContent) {
		this.serviceContent = serviceContent;
	}

	public void setServiceID(final String serviceID) {
		this.serviceID = serviceID;
	}

	public String getServiceContent() {
		return serviceContent;
	}

	public void setSrvVersion(final String srvVersion) {
		this.srvVersion = srvVersion;
	}

	public String getServiceID() {
		return serviceID;
	}

	public String getSrvVersion() {
		return srvVersion;
	}

	public String getTenantID() {
		return tenantID;
	}

	public void setOperType(final String operType) {
		this.operType = operType;
	}

	public void setProgress(final String progress) {
		this.progress = progress;
	}

	public String getOperType() {
		return operType;
	}

	public String getTaskID() {
		return taskID;
	}

	public void setTaskID(final String taskID) {
		this.taskID = taskID;
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
	public void setResult(final String result) {
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
	public void setResultReason(final String resultReason) {
		this.resultReason = resultReason;
	}

	/**
	 * @return Returns the terminal.
	 */
	public LogUser getUser() {
		return user;
	}

	/**
	 * @param terminal The terminal to set.
	 */
	public void setUser(LogUser user) {
		this.user = user;
	}

	/**
	 * convert to job attribute
	 * 
	 * @return
	 * @since crossdomain 0.5
	 */
	public JSONObject toJobAttr() {
		final Map<String, Object> resAttributeMap = new HashMap<String, Object>();
		resAttributeMap.put(Constant.SRV_BASE, toDesignerJson());
		resAttributeMap.put(Constant.TASK_ID, getTaskID());
		resAttributeMap.put(Constant.SERVICEID, getServiceID());
		resAttributeMap.put(Constant.TENANT_ID, getTenantID());
		resAttributeMap.put(Constant.ACTION, getOperType());
		resAttributeMap.put(Constant.TOKEN, getToken());

		resAttributeMap.put(Constant.USERID, getUser().getUserId());
		resAttributeMap.put(Constant.USERNAME, getUser().getUserName());
		resAttributeMap.put(Constant.TERMINAL, getUser().getTerminal());

		return JSONObject.fromObject(resAttributeMap);
	}

	/**
	 * initialize task
	 * 
	 * @param svcBody service json object
	 * @since crossdomain 0.5
	 */
	public void initalTask(final JSONObject svcBody) {
		JSONObject jsonSvcBody = JSONObject.fromObject(svcBody);
		JSONObject jsonSvc = jsonSvcBody.getJSONObject(Constant.SERVICE);
		setServiceID(jsonSvc.getString(Constant.SERVICEID));
		setSrvVersion("v1");
		setOperType(jsonSvc.getString(Constant.ACTION));

		setServiceContent(svcBody.toString());
		setProgress(EnumProgress.READY.getName());
	}

	/**
	 * convert to the format for designer
	 * 
	 * @return json object for designer
	 * @since crossdomain 0.5
	 */
	public JSONObject toDesignerJson() {
		String sdTaskBody = getServiceContent();
		JSONObject jsonSDTask = JSONObject.fromObject(sdTaskBody);

		JSONObject srvBodyDesign = new JSONObject();
		srvBodyDesign.put(Constant.SERVICEID, getServiceID());
		srvBodyDesign.put(
				Constant.NAME,
				jsonSDTask.getJSONObject(Constant.SERVICE).getString(
						Constant.NAME));
		srvBodyDesign.put(
				Constant.DESCRIPTION,
				jsonSDTask.getJSONObject(Constant.SERVICE).getString(
						Constant.DESCRIPTION));
		srvBodyDesign.put(Constant.ACTION, getOperType());

		return srvBodyDesign;
	}

	/**
	 * assemble task attribute
	 * 
	 * @return task json object
	 * @since crossdomain 0.5
	 */
	public JSONObject assembleTaskCommonJson() {
		JSONObject taskCommonJson = new JSONObject();
		taskCommonJson.put(Constant.TaskCommon.TASK_ID, getTaskID());
		taskCommonJson.put(Constant.TaskCommon.TENANT_ID, getTenantID());
		taskCommonJson.put(Constant.TaskCommon.OPERTYPE, getOperType());
		taskCommonJson.put(Constant.TaskCommon.PROGRESS, getProgress());
		taskCommonJson.put(Constant.TaskCommon.RESULT, getResult());
		taskCommonJson
				.put(Constant.TaskCommon.RESULT_REASON, getResultReason());

		return taskCommonJson;
	}

	/**
	 * deep copy task
	 * 
	 * @return the task objects being copied
	 * @since crossdomain 0.5
	 */
	public ServiceDecomposerTask deepCopy() {
		ServiceDecomposerTask newTask = new ServiceDecomposerTask();
		newTask.setOperType(getOperType());
		newTask.setProgress(getProgress());
		newTask.setResult(getResult());
		newTask.setResultReason(getResultReason());
		newTask.setSeJobID(getSeJobID());
		newTask.setServiceContent(getServiceContent());
		newTask.setServiceID(getServiceID());
		newTask.setSrvVersion(getSrvVersion());
		newTask.setTaskID(getTaskID());
		newTask.setTenantID(getTenantID());
		newTask.setToken(getToken());
		if (null != user) {
			newTask.user = new LogUser();
			newTask.user.setUserId(user.getUserId());
			newTask.user.setUserName(user.getUserName());
			newTask.user.setTerminal(user.getTerminal());
		}
		return newTask;
	}
}
