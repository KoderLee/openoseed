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
package org.openo.crossdomain.commonsvc.decomposer.check;

/**
 * the structure of validate result .
 * 
 * @since crossdomain 0.5
 */
public class CheckResult {

	private int result;

	private String message;

	/**
	 * constructor
	 * 
	 * @param result result
	 * @param message message
	 * @since crossdomain 0.5
	 */
	public CheckResult(final int result, final String message) {
		this.result = result;
		this.message = message;
	}

	public int getResult() {
		return result;
	}

	public void setResult(final int result) {
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(final String message) {
		this.message = message;
	}

	public String getErrorInfo() {
		return "Data check failed. field=" + message;
	}
}
