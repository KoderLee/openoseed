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

/**
 * Decomposition rule registration information
 * 
 * @since crossdomain 0.5
 */
public class ServiceDecomposerMapping {

	@CheckAttr(type = CheckType.String, required = true, max = 64)
	private String typeName;

	@CheckAttr(type = CheckType.Enum, required = true, valueScop = { "service",
			"resource" })
	private String regType;

	@CheckAttr(type = CheckType.String, required = true, max = 64)
	private String operType;

	@CheckAttr(type = CheckType.String, required = true, max = 32, rule = Constant.VERSION_RULS)
	private String version;

	@CheckAttr(type = CheckType.String, required = false, max = 64)
	private String uriprefix;

	/**
	 * Constructor
	 * 
	 * @since crossdomain 0.5
	 */
	public ServiceDecomposerMapping() {
	}

	/**
	 * Constructor
	 * 
	 * @param sdMapping ServiceDecomposerMapping
	 * @since crossdomain 0.5
	 */
	public ServiceDecomposerMapping(final ServiceDecomposerMapping sdMapping) {
		this.typeName = sdMapping.getTypeName();
		this.regType = sdMapping.getRegType();
		this.operType = sdMapping.getOperType();
		this.version = sdMapping.getVersion();
		this.uriprefix = sdMapping.getUriprefix();
	}

	public String getRegType() {
		return regType;
	}

	public String getVersion() {
		return version;
	}

	public void setTypeName(final String typeName) {
		this.typeName = typeName;
	}

	public void setUriprefix(final String uriprefix) {
		this.uriprefix = uriprefix;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setVersion(final String version) {
		this.version = version;
	}

	public String getUriprefix() {
		return uriprefix;
	}

	public void setRegType(final String regType) {
		this.regType = regType;
	}

	public String getOperType() {
		return operType;
	}

	public void setOperType(String operType) {
		this.operType = operType;
	}
}
