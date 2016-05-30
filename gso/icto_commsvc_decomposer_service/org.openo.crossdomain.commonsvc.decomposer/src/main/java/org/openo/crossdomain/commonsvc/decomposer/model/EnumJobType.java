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

/**
 * The enumeration of Job type
 * 
 * @since crossdomain 0.5
 */
public enum EnumJobType {

	DECOMPOSE("decompose", 0), DECOMPOSE_WAITREPLAY("Decompose_WaitReplay", 1),
	// DECOMPOSE_GROUP_MONITOR("Decompose_Group_Monitor", 2),
	EXECUTE("execute", 3), EXECUTE_WAITREPLAY("Execute_WaitReplay", 4);

	private String name;

	private int index;

	/**
	 * Constructor
	 * 
	 * @param name name
	 * @param index index
	 * @since crossdomain 0.5
	 */
	EnumJobType(String name, int index) {
		this.name = name;
		this.index = index;
	}

	/**
	 * Gets index.
	 * 
	 * @return Value of index.
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Gets name.
	 * 
	 * @return Value of name.
	 */
	public String getName() {
		return name;
	}

}