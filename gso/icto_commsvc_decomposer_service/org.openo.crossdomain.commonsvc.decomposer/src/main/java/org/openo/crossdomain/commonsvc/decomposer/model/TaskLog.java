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
 * Task log information
 * 
 * @since crossdomain 0.5
 */
public class TaskLog {

	private String taskID;

	private int sequenceID;

	private long timestap;;

	private String description;

	private String result;

	private String resultReason;

	public TaskLog() {
		this("");
	}

	public TaskLog(String taskID) {
		super();
		this.taskID = taskID;
		this.timestap = System.currentTimeMillis();
	}

	/**
	 * @return Returns the taskID.
	 */
	public String getTaskID() {
		return taskID;
	}

	/**
	 * @return Returns the sequenceID.
	 */
	public int getSequenceID() {
		return sequenceID;
	}

	/**
	 * @param description The description to set.
	 */
	public void setSequenceID(int sequenceID) {
		this.sequenceID = sequenceID;
	}

	/**
	 * @return Returns the timestap.
	 */
	public long getTimestap() {
		return timestap;
	}

	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
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

}
