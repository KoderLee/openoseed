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
package org.openo.crossdomain.commonsvc.decomposer.logutil;

import org.openo.crossdomain.commonsvc.decomposer.constant.Constant;

import net.sf.json.JSONObject;

/**
 * Task header information
 * 
 * @since crossdomain 0.5
 */
public class TaskHeader {

	private String taskId;

	private String tenantId;

	private LogUser user;

	/**
	 * Constructor
	 * 
	 * @since crossdomain 0.5
	 */
	public TaskHeader() {
		super();
	}

	/**
	 * Constructor
	 * 
	 * @param attributeJson json object
	 * @since crossdomain 0.5
	 */
	public TaskHeader(final JSONObject attributeJson) {
		super();
		// TODO Auto-generated constructor stub
		String taskID = attributeJson.getString(Constant.TASK_ID);
		String tenantID = attributeJson.getString(Constant.TENANT_ID);
		String usrId = attributeJson.getString(Constant.USERID);
		String usrName = attributeJson.getString(Constant.USERNAME);
		String terminal = attributeJson.getString(Constant.TERMINAL);
		LogUser user = new LogUser(usrId, usrName, terminal);

		this.setTaskId(taskID);
		this.setTenantId(tenantID);
		this.setUser(user);
	}

	/**
	 * @return Returns the taskId.
	 */
	public String getTaskId() {
		return taskId;
	}

	/**
	 * @param taskId The taskId to set.
	 */
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	/**
	 * @return Returns the tenantId.
	 */
	public String getTenantId() {
		return tenantId;
	}

	/**
	 * @param tenantId The tenantId to set.
	 */
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	/**
	 * @return Returns the user.
	 */
	public LogUser getUser() {
		return user;
	}

	/**
	 * @param user The user to set.
	 */
	public void setUser(LogUser user) {
		this.user = user;
	}
}
