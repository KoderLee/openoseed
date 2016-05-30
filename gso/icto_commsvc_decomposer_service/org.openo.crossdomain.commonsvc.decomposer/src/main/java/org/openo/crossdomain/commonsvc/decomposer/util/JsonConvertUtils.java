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
package org.openo.crossdomain.commonsvc.decomposer.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.openo.crossdomain.commonsvc.decomposer.constant.Constant;
import org.openo.crossdomain.commonsvc.decomposer.model.EnumProgress;
import org.openo.crossdomain.commonsvc.decomposer.model.ResMapping;
import org.openo.crossdomain.commonsvc.decomposer.model.ServiceDecomposerMapping;
import org.openo.crossdomain.commonsvc.decomposer.model.ServiceDecomposerTask;

/**
 * json convert util class.
 * 
 * @since crossdomain 0.5
 */
public class JsonConvertUtils {

	/**
	 * convert ResMapping to json format
	 * 
	 * @param resMappingList ResMapping list
	 * @param taskStatus task status
	 * @return JSON format ResMapping
	 * @since crossdomain 0.5
	 */
	public static JSONObject convertResFromDb(
			final List<ResMapping> resMappingList, final String taskStatus) {

		final JSONObject resJsonObject = new JSONObject();

		if (CollectionUtils.isEmpty(resMappingList)) {
			return resJsonObject;
		}

		for (final ResMapping resMapping : resMappingList) {
			JSONObject oneArrayElement = resMapping.toJson(taskStatus);
			resJsonObject.put(resMapping.getResourceLabel(), oneArrayElement);
		}

		return resJsonObject;
	}

	// @formatter:off
	/*
	 * { "service_job" : { "job_id" : "ddd", "progess" : "execute", "created_at"
	 * : "", "completed_at" : "", "result" : "", "result_reason" : "", "service"
	 * : { "serviceId" : "services_2_1_1", "resource" : { "VCPE_A" : { "name" :
	 * "VCPE_A", "type" : "NFV.CPE", "..." : "...", "status" : "execute",
	 * "result" : { "code" : "SUCCESS", "reason" : "" } }, "VCPE_Z" : { "name" :
	 * "VCPE_Z", "type" : "NFV.CPE", "..." : "...", "status" : "execute",
	 * "result" : { "code" : "SUCCESS", "reason" : "" } } } } } }
	 */
	// @formatter:on

	/**
	 * convert resource to json format from Executor
	 * 
	 * @param jsonStringFromSE
	 * @return JSON format resources
	 * @since crossdomain 0.5
	 */
	public static JSONObject convertResFromSE(final String jsonStringFromSE) {

		final JSONObject resJsonObj = new JSONObject();

		if (StringUtils.isEmpty(jsonStringFromSE)) {
			return resJsonObj;
		}

		final JSONObject seJson = JSONObject.fromObject(jsonStringFromSE);

		final JSONObject resFromSeJson = seJson
				.getJSONObject(Constant.SERVICE_JOB)
				.getJSONObject(Constant.SERVICE)
				.getJSONObject(Constant.RESOURCES);

		return resFromSeJson;
	}

	/**
	 * convert to JSON format for service design
	 * 
	 * @param svcString string
	 * @param action action
	 * @return JSON format for service design
	 * @since crossdomain 0.5
	 */
	public static String covJson2SvcDesign(final String svcString,
			final String action) {
		final JSONObject jsonSvcBody = JSONObject.fromObject(svcString);
		final JSONObject jsonSvc = jsonSvcBody.getJSONObject(Constant.SERVICE);
		jsonSvc.put(jsonSvc, action);
		return jsonSvc.toString();
	}

	/**
	 * convert to decompose task.<br>
	 * 
	 * @param svcBody service json object.s
	 * @return decompose task
	 * @since crossdomain 0.5
	 */
	public static ServiceDecomposerTask covJson2SvcTask(final JSONObject svcBody) {
		ServiceDecomposerTask task = new ServiceDecomposerTask();
		task.setTaskID(UUIDUtils.createUuid());
		task.initalTask(svcBody);
		return task;
	}

	/**
	 * convert to the format for service design.<br>
	 * 
	 * @param task task
	 * @param resMappings ResMapping list
	 * @return the format for service design
	 * @since crossdomain 0.5
	 */
	public static String covMode2SvcDesign(final ServiceDecomposerTask task,
			final List<ResMapping> resMappings) {
		if ((null == task) || (null == resMappings)) {
			return "";
		}

		JSONObject srvBodyDesign = task.toDesignerJson();
		JSONObject jsonResources = new JSONObject();
		for (ResMapping res : resMappings) {
			JSONObject jsonRes = res.toDesignerJson();
			jsonRes.put(Constant.ACTION, task.getOperType());
			jsonResources.put(res.getResourceLabel(), jsonRes);
		}
		srvBodyDesign.put(Constant.RESOURCES, jsonResources);

		JSONObject jsonSvcDesign = new JSONObject();
		jsonSvcDesign.put(Constant.SERVICE, srvBodyDesign);

		return jsonSvcDesign.toString();
	}

	/**
	 * convert to ResMapping list.<br>
	 * 
	 * @param tenantID tenant ID
	 * @param jsonSvcBody service json object
	 * @param act action
	 * @return ResMapping list
	 * @since crossdomain 0.5
	 */
	public static List<ResMapping> covJson2ResMapping(final String tenantID,
			final JSONObject jsonSvcBody, final String... act) {
		List<String> actList = (act == null || act.length == 0) ? null : Arrays
				.asList(act);

		List<ResMapping> resMappings = new ArrayList<ResMapping>();
		if (jsonSvcBody.isEmpty()) {
			return resMappings;
		}
		String serviceID = jsonSvcBody.getString(Constant.SERVICEID);
		JSONObject jsonResources = jsonSvcBody
				.getJSONObject(Constant.RESOURCES);
		Iterator<?> keys = jsonResources.keys();
		while (keys.hasNext()) {
			String resourceLabel = keys.next().toString();
			JSONObject jsonRes = jsonResources.getJSONObject(resourceLabel);
			String resourceID = JsonUtils.getString(jsonRes, Constant.ID);
			String resourceType = JsonUtils.getString(jsonRes, Constant.TYPE);
			String depends = JsonUtils.getString(jsonRes, Constant.DEPENDS);
			String action = JsonUtils.getString(jsonRes, Constant.ACTION);

			if (actList != null && !actList.contains(action)) {
				continue;
			}
			ResMapping resMapping = new ResMapping(tenantID, serviceID,
					resourceID, resourceLabel, Constant.RESOURCE, "v1",
					resourceType, depends, Constant.DEACTIVE);

			JSONObject resultJsonObject = JsonUtils.getJsonObject(jsonRes,
					Constant.RESULT);
			if (resultJsonObject != null) {
				String code = JsonUtils.getString(resultJsonObject,
						Constant.CODE);
				String reason = JsonUtils.getString(resultJsonObject,
						Constant.REASON);
				resMapping.setResult(code);
				resMapping.setResultReason(reason);
			}

			resMappings.add(resMapping);
		}
		return resMappings;
	}

	/**
	 * convert to the format for executor
	 * 
	 * @param svcDesign service
	 * @param jobID job ID
	 * @return the format for executor
	 * @since crossdomain 0.5
	 */
	public static String covJson2SvcEx(final String svcDesign,
			final String jobID) {
		final JSONObject jsonSvcBody = JSONObject.fromObject(svcDesign);

		jsonSvcBody.put(Constant.ONFAILURE, "rollback");
		jsonSvcBody.put(Constant.IS_DRYRUN, "false");

		final JSONObject serviceJob = new JSONObject();
		// serviceJob.put(Constant.JOB_ID, jobID);
		serviceJob.put(Constant.SERVICE, jsonSvcBody);

		final JSONObject jsonSEObject = new JSONObject();
		jsonSEObject.put(Constant.SERVICE_JOB, serviceJob);

		return jsonSEObject.toString();
	}

	/**
	 * assemble service common attribute
	 * 
	 * @param decomposerTask task
	 * @return the service json object with basic attribute
	 * @since crossdomain 0.5
	 */
	public static JSONObject assembleServiceCommonJson(
			final ServiceDecomposerTask decomposerTask) {

		final String sdTaskBody = decomposerTask.getServiceContent();
		final JSONObject jsonSDTask = JSONObject.fromObject(sdTaskBody);

		JSONObject taskServiceJson = new JSONObject();
		taskServiceJson.put(Constant.ID, decomposerTask.getServiceID());
		taskServiceJson.put(
				Constant.NAME,
				jsonSDTask.getJSONObject(Constant.SERVICE).getString(
						Constant.NAME));

		return taskServiceJson;
	}

	/**
	 * assemble resource , Group according to the URI prefix and version of the
	 * resource.
	 * 
	 * @param domainJsonObject group json object
	 * @param rstSDMap ServiceDecomposerMapping
	 * @param resKey resource key
	 * @param resJsonObject resource json object
	 * @since crossdomain 0.5
	 */
	public static void assembleRes(final JSONObject domainJsonObject,
			final ServiceDecomposerMapping rstSDMap, final String resKey,
			final JSONObject resJsonObject) {
		if (rstSDMap == null) {
			return;
		}
		final String domainKey = rstSDMap.getUriprefix() + "/"
				+ rstSDMap.getVersion();

		JSONObject resourcesJsonObject;
		if (!domainJsonObject.containsKey(domainKey)) {
			resourcesJsonObject = new JSONObject();
			JSONObject aDomainJsonObject = new JSONObject();
			aDomainJsonObject
					.put(Constant.STATUS, EnumProgress.READY.getName());
			resourcesJsonObject.put(resKey, resJsonObject);
			aDomainJsonObject.put(Constant.RESOURCES, resourcesJsonObject);
			domainJsonObject.put(domainKey, aDomainJsonObject);
		} else {
			resourcesJsonObject = domainJsonObject.getJSONObject(domainKey)
					.getJSONObject(Constant.RESOURCES);
			resourcesJsonObject.put(resKey, resJsonObject);
		}
	}
}
