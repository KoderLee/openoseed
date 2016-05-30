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
package org.openo.crossdomain.commonsvc.decomposer.constant;

/**
 * Constant definition of URL
 * 
 * @since crossdomain 0.5
 */
public class ConstantURL {

	// "{uri_prefix}/v1/services/{service_type}"
	// public static final String CREATE_SERVICE = "/rest/{0}/v1/services/{1}";

	// {uri_prefix}/v1/services/{service_type}/{service_id}
	// public static final String MODIFY_SERVICE =
	// "/rest/{0}/v1/services/{1}/{2}";

	public static final String SERVICE_DECOMPOSE = "/rest/{0}?action={1}&service_id={2}";

	public static final String RESOURCE_DESIGN = "/rest/{0}/services/{1}/resource";

	public static final String CREATE_SE_JOB = "/rest/executor/v1/jobs";

	// "executor/v1/jobs/{job_id}?action={action}"
	public static final String MODIFY_SE_JOB = "/rest/executor/v1/jobs/{0}?action={1}";

	// "executor/v1/jobs?serviceid={id}"
	public static final String QUERY_SE_JOB_LIST = "/rest/executor/v1/jobs?serviceid={0}";

	// "executor/v1/jobs/{job_id}"
	public static final String QUERY_SE_JOB_DETAIL = "/rest/executor/v1/jobs/{0}";

	public static final String DECOMPOSER_PREFIX = "/decomposer/v1";

	public static final String DECOMPOSER_RULES_PREFIX = DECOMPOSER_PREFIX
			+ "/rules";

	public static final String REGISTE = "/register";

	public static final String UNREGISTE = "/unregister";

	public static final String TASKS = "/tasks";

	public static final String QUERY_DECOMPOSE_TASK_DETAIL = TASKS
			+ "/{task_id}";

	public static final String QUERY_SERVICE_DETAIL = "/services/{service_id}/resources";

	public static final String QUERY_DECOMPOSE_TASK_LOG = QUERY_DECOMPOSE_TASK_DETAIL
			+ "/logs";
}
