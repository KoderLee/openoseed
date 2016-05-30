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

import org.openo.commonservice.biz.trail.AuditItem;
import org.openo.commonservice.roa.common.HttpContext;

/**
 * User define for Log
 * 
 * @since crossdomain 0.5
 */
public class LogUser {

	private String userId;

	private String userName;

	private String terminal;

	/**
	 * Constructor
	 * 
	 * @since crossdomain 0.5
	 */
	public LogUser() {
		super();
	}

	/**
	 * Constructor
	 * 
	 * @param userId user ID
	 * @param userName user name
	 * @param terminal terminal
	 * @since crossdomain 0.5
	 */
	public LogUser(String userId, String userName, String terminal) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.terminal = terminal;
	}

	/**
	 * Constructor, construct this by context info
	 * 
	 * @param context context
	 * @since crossdomain 0.5
	 */
	public LogUser(HttpContext context) {
		if (context == null || context.getHttpServletRequest() == null) {
			return;
		}

		userId = context.getHttpServletRequest().getHeader(
				Constant.Security.X_USER_ID);

		userName = context.getHttpServletRequest().getHeader(
				Constant.Security.X_USER_NAME);

		terminal = context.getHttpServletRequest().getHeader(
				Constant.Security.X_CLIENT_ADDR);
		// terminal = context.getHttpServletRequest().getRemoteAddr();
	}

	/**
	 * @param userId The userId to set.
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @param userName The userName to set.
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @param terminal The terminal to set.
	 */
	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}

	public void SetAuditLogUser(AuditItem item, String domainId) {
		item.setUser(userId, userName, domainId, "");
		item.terminal = terminal;
	}

	/**
	 * @return Returns the userId.
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @return Returns the userName.
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @return Returns the terminal.
	 */
	public String getTerminal() {
		return terminal;
	}

}
