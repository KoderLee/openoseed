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

import org.openo.crossdomain.commonsvc.decomposer.check.CRUDType;
import org.openo.crossdomain.commonsvc.decomposer.check.CheckAttr;
import org.openo.crossdomain.commonsvc.decomposer.check.CheckType;

/**
 * resource action information
 * 
 * @since crossdomain 0.5
 */
public class ResActionInfo {

	@CheckAttr(type = CheckType.String, required = true, min = 0, max = 32, crud = {
			CRUDType.C, CRUDType.R })
	private String serviceID;

	@CheckAttr(type = CheckType.String, required = true, min = 0, max = 32, crud = {
			CRUDType.C, CRUDType.R })
	private String jobID;

	private String resourceLabel;

	private String resourceContent;

	private String operationType;

	private String progress;

	public void setResourceContent(String resourceContent) {
		this.resourceContent = resourceContent;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}

	public void setProgress(String progress) {
		this.progress = progress;
	}

	public String getResourceContent() {
		return resourceContent;
	}

	public void setResourceLabel(String resourceLabel) {
		this.resourceLabel = resourceLabel;
	}

	public void setServiceID(String serviceID) {
		this.serviceID = serviceID;
	}

	public String getProgress() {
		return progress;
	}

	public String getServiceID() {
		return serviceID;
	}

	public String getResourceLabel() {
		return resourceLabel;
	}

	public String getJobID() {
		return jobID;
	}

	public void setJobID(String jobID) {
		this.jobID = jobID;
	}

	public String getOperationType() {
		return operationType;
	}
}
