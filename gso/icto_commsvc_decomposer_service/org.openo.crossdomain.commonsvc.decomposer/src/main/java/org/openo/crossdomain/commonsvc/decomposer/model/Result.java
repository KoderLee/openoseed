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

import org.openo.crossdomain.commonsvc.decomposer.constant.EnumResult;

/**
 * Result information
 * 
 * @param <T>
 * @since crossdomain 0.5
 */
public class Result<T> {

	private String retCode;

	private int statusCode;

	private String reason;

	private T data;

	public Result() {
		super();
	}

	/**
	 * Constructor
	 * 
	 * @param retCode
	 * @since crossdomain 0.5
	 */
	public Result(String retCode) {
		super();
		this.retCode = retCode;
	}

	/**
	 * Constructor
	 * 
	 * @param retCode
	 * @param reason
	 * @since crossdomain 0.5
	 */
	public Result(String retCode, String reason) {
		super();
		this.retCode = retCode;
		this.reason = reason;
	}

	/**
	 * Constructor
	 * 
	 * @param retCode
	 * @param reason
	 * @param data
	 * @since crossdomain 0.5
	 */
	public Result(String retCode, String reason, T data) {
		super();
		this.retCode = retCode;
		this.reason = reason;
		this.data = data;
	}

	/**
	 * @return Returns the retCode.
	 */
	public String getRetCode() {
		return retCode;
	}

	/**
	 * @param retCode The retCode to set.
	 */
	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}

	/**
	 * @return Returns the reason.
	 */
	public String getReason() {
		return reason;
	}

	/**
	 * @param reason The reason to set.
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}

	/**
	 * @return Returns the data.
	 */
	public T getData() {
		return data;
	}

	/**
	 * @param data The data to set.
	 */
	public void setData(T data) {
		this.data = data;
	}

	/**
	 * check result is success or not
	 * 
	 * @return success or not
	 * @since crossdomain 0.5
	 */
	public boolean checkSuccess() {
		return retCode.equals(EnumResult.SUCCESS.getName());
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
}
