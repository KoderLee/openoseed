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
package org.openo.crossdomain.commonsvc.decomposer.module;

import org.openo.crossdomain.commonsvc.decomposer.constant.EnumResult;
import org.openo.crossdomain.commonsvc.decomposer.service.inf.IService;

import net.sf.json.JSONObject;

/**
 * abstract resource information
 * 
 * @param <T>
 * @since crossdomain 0.5
 */
public abstract class AbsResource<T extends IService> {

	protected T service;

	protected final JSONObject jsonObjSucess;

	/**
	 * Constructor
	 * 
	 * @since crossdomain 0.5
	 */
	public AbsResource() {
		jsonObjSucess = new JSONObject();
		jsonObjSucess.put("result", EnumResult.SUCCESS.getName());
	}

	public void setService(final T service) {
		this.service = service;
	}

	/**
	 * get resource URI
	 * 
	 * @return
	 * @since crossdomain 0.5
	 */
	public abstract String getResUri();
}
