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
package org.openo.crossdomain.commonsvc.decomposer.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang.StringUtils;
import org.openo.crossdomain.commonsvc.decomposer.check.CheckResult;
import org.openo.crossdomain.commonsvc.decomposer.constant.Constant;
import org.openo.crossdomain.commonsvc.decomposer.constant.ErrorCode;
import org.openo.crossdomain.commonsvc.decomposer.constant.Constant.OperType;
import org.openo.crossdomain.commonsvc.decomposer.dao.inf.IServiceOperateDao;
import org.openo.crossdomain.commonsvc.decomposer.dao.inf.IServiceResDao;
import org.openo.crossdomain.commonsvc.decomposer.logutil.LogUser;
import org.openo.crossdomain.commonsvc.decomposer.logutil.ServicedecomposerAuditLog;
import org.openo.crossdomain.commonsvc.decomposer.logutil.TaskHeader;
import org.openo.crossdomain.commonsvc.decomposer.model.EnumJobType;
import org.openo.crossdomain.commonsvc.decomposer.model.EnumOperType;
import org.openo.crossdomain.commonsvc.decomposer.model.EnumProgress;
import org.openo.crossdomain.commonsvc.decomposer.model.RegType;
import org.openo.crossdomain.commonsvc.decomposer.model.ResMapping;
import org.openo.crossdomain.commonsvc.decomposer.model.Result;
import org.openo.crossdomain.commonsvc.decomposer.model.ServiceDecomposerMapping;
import org.openo.crossdomain.commonsvc.decomposer.model.ServiceDecomposerTask;
import org.openo.crossdomain.commonsvc.decomposer.model.TaskLog;
import org.openo.crossdomain.commonsvc.decomposer.rest.util.TokenUtil;
import org.openo.crossdomain.commonsvc.decomposer.service.decomposer.ResourceDecomposerPolicy;
import org.openo.crossdomain.commonsvc.decomposer.service.formation.inf.IServiceFormation;
import org.openo.crossdomain.commonsvc.decomposer.service.inf.IGetResProvider;
import org.openo.crossdomain.commonsvc.decomposer.service.inf.IServiceDecomposerService;
import org.openo.crossdomain.commonsvc.decomposer.service.inf.IServiceOperateService;
import org.openo.crossdomain.commonsvc.decomposer.service.inf.IServiceOperateTask;
import org.openo.crossdomain.commonsvc.decomposer.service.inf.ITaskLogService;
import org.openo.crossdomain.commonsvc.decomposer.util.DecomposerContextHelper;
import org.openo.crossdomain.commonsvc.decomposer.util.DecomposerUtil;
import org.openo.crossdomain.commonsvc.decomposer.util.ErrorCodeUtil;
import org.openo.crossdomain.commonsvc.decomposer.util.JsonConvertUtils;
import org.openo.crossdomain.commonsvc.decomposer.util.JsonUtils;
import org.openo.crossdomain.commonsvc.decomposer.util.UUIDUtils;
import org.springframework.stereotype.Service;

import org.openo.commonservice.log.OssLog;
import org.openo.commonservice.log.OssLogFactory;
import org.openo.commonservice.redis.oper.MapOper;
import org.openo.commonservice.remoteservice.exception.ServiceException;
import org.openo.commonservice.roa.common.HttpContext;
import com.huawei.icto.commsvc.jobscheduler.core.inf.IScheduler;
import com.huawei.icto.commsvc.jobscheduler.model.JobBean;

/**
 * The implement of logic layer for service decomposer flow.
 * 
 * @since crossdomain 0.5
 */
@Service(value = Constant.SpringDefine.SRV_OPERATE_SERVICE)
public class ServiceOperateServiceImpl implements IServiceOperateService {

	/**
	 * logger
	 */
	private final OssLog logger = OssLogFactory
			.getLogger(ServiceOperateServiceImpl.class);

	/**
	 * @see IServiceOperateService#queryService(org.openo.crossdomain.commonsvc.decomposer.service.inf.HttpContext,
	 *      String)
	 */
	@Override
	public Result<String> queryService(final HttpContext context,
			final String service_id) throws ServiceException {
		IServiceResDao srvResDao = (IServiceResDao) DecomposerContextHelper
				.getInstance().getBean("srvResDao");
		final Result<List<ResMapping>> resMappingsResult = srvResDao
				.getResMappingBySvcID(service_id, TokenUtil
						.getStringFromHeader(context,
								Constant.Security.X_TENANT_ID));
		ErrorCodeUtil.checkResult(logger, resMappingsResult,
				"Get Resource failed! ServiceID: " + service_id,
				ErrorCode.SD_OPER_DB_ERROR);

		final JSONObject resJsonObj = JsonConvertUtils.convertResFromDb(
				resMappingsResult.getData(), "");

		final JSONObject serviceObj = new JSONObject();
		serviceObj.put(Constant.SERVICEID, service_id);
		serviceObj.put(Constant.RESOURCES, resJsonObj);

		JSONObject returnJsonObject = new JSONObject();
		returnJsonObject.put(Constant.SERVICE, serviceObj);

		return new Result<String>(ErrorCode.SUCCESS, "",
				returnJsonObject.toString());
	}

	/**
	 * @see IServiceOperateService#queryExecuteProcess(org.openo.crossdomain.commonsvc.decomposer.service.inf.HttpContext,
	 *      String)
	 */
	@Override
	public Result<String> queryExecuteProcess(final HttpContext context,
			final String taskID) throws ServiceException {

		IServiceOperateDao srvOperateDao = (IServiceOperateDao) DecomposerContextHelper
				.getInstance().getBean(Constant.SpringDefine.SRV_OPERATE_DAO);
		final Result<ServiceDecomposerTask> decomposerTaskResult = srvOperateDao
				.getTaskByTaskID(taskID, TokenUtil.getStringFromHeader(context,
						Constant.Security.X_TENANT_ID));
		ErrorCodeUtil.checkResult(logger, decomposerTaskResult,
				"Get Task failed! taskID: " + taskID,
				ErrorCode.SD_OPER_DB_ERROR);

		final ServiceDecomposerTask decomposerTask = decomposerTaskResult
				.getData();

		ErrorCodeUtil.checkObject(logger, decomposerTask,
				"[queryExecuteProcess] task is null! taskID: " + taskID,
				ErrorCode.SD_BAD_PARAM);
		decomposerTask.setToken(TokenUtil.getStringFromHeader(context,
				Constant.Security.X_AUTH_TOKEN));

		final JSONObject returnTaskObj = decomposerTask
				.assembleTaskCommonJson();
		JSONObject returnSrvObj = JsonConvertUtils
				.assembleServiceCommonJson(decomposerTask);

		IGetResProvider resProvider;

		if (EnumProgress.EXECUTE.getName().equals(decomposerTask.getProgress())
				&& StringUtils.isNotEmpty(decomposerTask.getSeJobID())) {
			resProvider = new GetResProviderSEImpl(decomposerTask.getSeJobID(),
					decomposerTask.getToken(), decomposerTask.getServiceID(),
					decomposerTask.getTenantID());
		} else {
			resProvider = new GetResProviderDbImpl(
					decomposerTask.getServiceID(),
					decomposerTask.getTenantID(), decomposerTask.getProgress());
		}

		// @formatter:off
		/*
		 * { "task" : { "task_id" : "5a895ad6e2474ea083659f3718fb7e8a",
		 * "tenant_id" : "", "opertype" : "create", "progress" : "execute",
		 * "result" : "Execute Start", "result_reason" : "", "service" : { "id"
		 * : "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA1", "name" : "test1", "resources" :
		 * [...] } } }
		 */
		// @formatter:on

		final JSONObject returnJsonObj = new JSONObject();

		returnSrvObj.put(Constant.RESOURCES, resProvider.getResInfo());
		returnTaskObj.put(Constant.SERVICE, returnSrvObj);
		returnJsonObj.put(Constant.TaskCommon.TASK, returnTaskObj);

		final Result<String> result = new Result<String>();
		result.setData(returnJsonObj.toString());
		result.setRetCode(ErrorCode.SUCCESS);

		return result;
	}

	/**
	 * @see IServiceOperateService#queryTask(org.openo.crossdomain.commonsvc.decomposer.service.inf.HttpContext,
	 *      String)
	 */
	@Override
	public Result<String> queryTask(final HttpContext context,
			final String serviceID) throws ServiceException {

		IServiceOperateDao srvOperateDao = (IServiceOperateDao) DecomposerContextHelper
				.getInstance().getBean(Constant.SpringDefine.SRV_OPERATE_DAO);
		Result<List<ServiceDecomposerTask>> sdTaskLstResult = srvOperateDao
				.getSDTaskBySvcID(serviceID, TokenUtil.getStringFromHeader(
						context, Constant.Security.X_TENANT_ID));

		JSONArray jsonSDArray = JSONArray.fromObject(sdTaskLstResult.getData());
		JSONObject jsonRules = new JSONObject();
		jsonRules.put(Constant.TaskCommon.TASKS, jsonSDArray);

		return new Result<String>(ErrorCode.SUCCESS, "", jsonRules.toString());
	}

	/**
	 * @see IServiceOperateService#queryTaskLog(org.openo.crossdomain.commonsvc.decomposer.service.inf.HttpContext,
	 *      String)
	 */
	@Override
	public Result<String> queryTaskLog(final HttpContext context,
			final String taskID) throws ServiceException {
		ITaskLogService taskLogService = (ITaskLogService) DecomposerContextHelper
				.getInstance().getBean("taskLogService");
		Result<List<TaskLog>> taskLogLstResult = taskLogService.getTaskLog(
				context, taskID);
		ErrorCodeUtil
				.checkResult(logger, taskLogLstResult,
						"Get TaskLog failed! taskID: " + taskID,
						ErrorCode.SD_BAD_PARAM);

		JSONArray jsonLogsArray = JSONArray.fromObject(taskLogLstResult
				.getData());
		JSONObject jsonLogs = new JSONObject();
		jsonLogs.put(Constant.TaskCommon.TASK_ID, taskID);
		jsonLogs.put(Constant.TaskCommon.LOGS, jsonLogsArray);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(Constant.TaskCommon.TASK, jsonLogs);

		return new Result<String>(ErrorCode.SUCCESS, "", jsonObject.toString());
	}

	/**
	 * @see IServiceOperateService#executeTask(org.openo.crossdomain.commonsvc.decomposer.service.inf.HttpContext,
	 *      String, String)
	 */
	@Override
	public Result<String> executeTask(final HttpContext context,
			final String srvBody, final String action) throws ServiceException {

		JSONObject bodyJsonObject = JSONObject.fromObject(srvBody);
		JSONObject srvJsonObject = bodyJsonObject
				.getJSONObject(Constant.SERVICE);
		srvJsonObject.put(Constant.ACTION, action);

		ServiceDecomposerTask task = JsonConvertUtils
				.covJson2SvcTask(bodyJsonObject);
		task.setTenantID(TokenUtil.getStringFromHeader(context,
				Constant.Security.X_TENANT_ID));
		task.setUser(new LogUser(context));
		task.setToken(TokenUtil.getStringFromHeader(context,
				Constant.Security.X_AUTH_TOKEN));

		ServicedecomposerAuditLog.addAuditLogBegin(task);

		TokenUtil.checkAccessible(logger, task);

		IServiceOperateDao srvOperateDao = (IServiceOperateDao) DecomposerContextHelper
				.getInstance().getBean(Constant.SpringDefine.SRV_OPERATE_DAO);
		Result<Object> insertResult = srvOperateDao.insert(task);
		ErrorCodeUtil.checkResult(logger, insertResult,
				"Insert Task failed! TaskID: " + task.getTaskID(),
				ErrorCode.SD_OPER_DB_ERROR);

		IServiceOperateTask srvOperateTask = (IServiceOperateTask) DecomposerContextHelper
				.getInstance().getBean(Constant.SpringDefine.SRV_OPERATE_TASK);
		srvOperateTask.updateTaskStatusAndWriteLog(task,
				EnumProgress.DECOMPOSE.getName(), "decompose start", "");
		try {

			executeDecompose(task);
		} catch (ServiceException serviceException) {
			srvOperateTask.updateTaskStatusAndWriteLog(task,
					EnumProgress.COMPLETE.getName(), ErrorCode.FAIL,
					serviceException.getMessage());
			DecomposerUtil.clearTaskRedis(task.getTaskID());
			ServicedecomposerAuditLog.addAuditLogEnd(task);

			throw serviceException;
		}

		context.setResponseStatus(HttpStatus.SC_ACCEPTED);

		return new Result<String>(ErrorCode.SUCCESS, "",
				"/decomposer/v1/tasks/" + task.getTaskID());
	}

	/**
	 * execute decompose action
	 * 
	 * @param task task
	 * @throws ServiceException if decompose failed
	 * @since crossdomain 0.5
	 */
	public void executeDecompose(final ServiceDecomposerTask task)
			throws ServiceException {

		final MapOper<JSONObject> taskMapOper = new MapOper<JSONObject>(
				DecomposerUtil.getTaskRedisStoreDomain(task.getTaskID()),
				Constant.REDIS_DB);
		JSONObject statusJsonObject = new JSONObject();
		statusJsonObject.put(Constant.STATUS, EnumProgress.DECOMPOSE);
		taskMapOper.put(Constant.TASK_ATTR, Constant.STATUS, statusJsonObject);

		IServiceFormation srvFormation = (IServiceFormation) DecomposerContextHelper
				.getInstance().getBean(Constant.SpringDefine.SRV_FORMATION);
		String rspFormation = srvFormation.serviceFormation(task);
		ErrorCodeUtil.hasLength(logger, rspFormation,
				"Service Formation failed!", ErrorCode.SD_TASK_EXECUTE_FAIL);

		CheckResult checkResult = JsonUtils.checkJsonObj(rspFormation,
				Constant.SERVICE);
		ErrorCodeUtil.checkObjectForNull(logger, checkResult,
				"Service Formation Check failed!",
				ErrorCode.SD_TASK_EXECUTE_FAIL);

		addSerialDecomposerJob(task, rspFormation);
	}

	/**
	 * decompose resource
	 * 
	 * @param resBody
	 * @param attributeJson
	 * @return
	 * @throws ServiceException
	 * @since crossdomain 0.5
	 */
	public Result<String> decomposerResource(final JSONObject resBody,
			final JSONObject attributeJson) throws ServiceException {
		String tenantID = attributeJson.getString(Constant.TENANT_ID);
		String srvBaseString = attributeJson.getString(Constant.SRV_BASE);
		JSONObject srvBaseJsonObj = JSONObject.fromObject(srvBaseString);
		String operType = attributeJson.getString(Constant.ACTION);
		srvBaseJsonObj.put(Constant.ACTION, operType);
		String domain = attributeJson.getString(Constant.VERSION_DOMAIN);
		Map<String, String> mapHeader = new HashMap<String, String>();
		mapHeader.put(Constant.Security.X_AUTH_TOKEN,
				attributeJson.getString(Constant.TOKEN));
		mapHeader.put(Constant.Security.X_CLIENT_ADDR,
				attributeJson.getString(Constant.TERMINAL));

		Result<String> rst = ResourceDecomposerPolicy.queryDecResource(
				tenantID, srvBaseJsonObj, resBody, domain, mapHeader);

		return rst;
	}

	/**
	 * process result of decomposed resource
	 * 
	 * @param result decomposed result
	 * @param resContext resource context
	 * @param attributeJson attribute json object
	 * @return result
	 * @throws ServiceException
	 * @since crossdomain 0.5
	 */
	public Result<String> processSerialDecomposerResourceResult(
			final Result<String> result, final String resContext,
			final JSONObject attributeJson) throws ServiceException {
		// String serviceID = attributeJson.getString(Constant.SERVICEID);

		/**
		 * { "service_id": "aQ74FLneQZO6Wzxlvuvxrg", "name": "test1",
		 * "description": "Huawei VPN (Beijing To Shenzhen)", "action":
		 * "modify", "resources": { "resource_2": { "name": "sap2", "id":
		 * "<resource_2>", "type": "SAP", "action": "delete", "depends": "" } }
		 * }
		 */

		final String taskID = attributeJson.getString(Constant.TASK_ID);
		String storeDomain = DecomposerUtil.getTaskRedisStoreDomain(taskID);
		final MapOper<JSONObject> taskMapOper = new MapOper<JSONObject>(
				storeDomain, Constant.REDIS_DB);

		final JSONObject domainJsonObject = JSONObject.fromObject(resContext);
		final JSONObject decomposerResourcJsonObject = domainJsonObject
				.getJSONObject(Constant.DEC_RESOURCES);
		final String data = result.getData();
		final JSONObject resultObj = JSONObject.fromObject(data);
		final JSONObject rtResjson = resultObj
				.getJSONObject(Constant.RESOURCES);
		final Iterator<?> itRtResJson = rtResjson.keys();

		while (itRtResJson.hasNext()) {
			final String reReskey = (String) itRtResJson.next();
			final JSONObject rstResJsonObject = rtResjson
					.getJSONObject(reReskey);

			final JSONObject matchInResJsonObject = getMatchInResJsonObject(
					decomposerResourcJsonObject, reReskey);

			if (ResourceDecomposerPolicy.isEnd(matchInResJsonObject,
					rstResJsonObject)) {
				logger.info("isEnd() is return true. reReskey" + reReskey);
				// addResJsonObj(serviceBaseJsonObj, reReskey,
				// rstResJsonObject);

				taskMapOper.put(Constant.DECOMPOSED_RES, reReskey,
						rstResJsonObject);
			} else {

				final String resType = rstResJsonObject
						.getString(Constant.TYPE);

				final ServiceDecomposerMapping rstSDMap = getDecomposerMapping(
						resType, RegType.RESOURCE.getName());
				JsonConvertUtils.assembleRes(domainJsonObject, rstSDMap,
						reReskey, rstResJsonObject);
			}
		}

		if (domainJsonObject.isEmpty()) {
			logger.info("No resources need to decompose!");
			processAfterDecomposed(attributeJson, taskID);
		} else {
			logger.info("decompose again");
			final JobBean job = createJob(domainJsonObject, attributeJson,
					EnumJobType.DECOMPOSE);
			IScheduler scheduler = (IScheduler) DecomposerContextHelper
					.getInstance().getBean("scheduler");
			scheduler.addNewJob(job);
		}

		return new Result<String>(ErrorCode.SUCCESS);
	}

	/**
	 * action of after decomposed
	 * 
	 * @param attributeJson attribute json object
	 * @param taskID task ID
	 * @throws ServiceException
	 * @since crossdomain 0.5
	 */
	private void processAfterDecomposed(final JSONObject attributeJson,
			final String taskID) throws ServiceException {

		String storeDomain = DecomposerUtil.getTaskRedisStoreDomain(taskID);
		final MapOper<JSONObject> taskMapOper = new MapOper<JSONObject>(
				storeDomain, Constant.REDIS_DB);
		final Map<String, JSONObject> resourceJsonMap = taskMapOper.getAll(
				Constant.DECOMPOSED_RES, JSONObject.class);

		final JSONObject resJsonObject = new JSONObject();
		if (resourceJsonMap != null) {
			for (final Entry<String, JSONObject> resJsonEntry : resourceJsonMap
					.entrySet()) {
				resJsonObject.accumulate(resJsonEntry.getKey(),
						resJsonEntry.getValue());
			}
		}

		final JSONObject serBaseJsonObj = attributeJson
				.getJSONObject(Constant.SRV_BASE);
		final String tenantID = attributeJson.getString(Constant.TENANT_ID);
		serBaseJsonObj.put(Constant.RESOURCES, resJsonObject);

		if (!OperType.CREATE_DEFINE.equals(attributeJson
				.getString(Constant.ACTION))) {
			attachResID(serBaseJsonObj, tenantID);
		}

		List<ResMapping> resMappings = JsonConvertUtils.covJson2ResMapping(
				tenantID, serBaseJsonObj, EnumOperType.CREATE.getName());
		IServiceResDao srvResDao = (IServiceResDao) DecomposerContextHelper
				.getInstance().getBean("srvResDao");
		Result<Object> insertResResult = srvResDao.insert(resMappings);
		ErrorCodeUtil.checkResult(logger, insertResResult,
				"Insert Resources failed! ResCount: " + resMappings.size(),
				ErrorCode.SD_OPER_DB_ERROR);

		IServiceOperateTask srvOperateTask = (IServiceOperateTask) DecomposerContextHelper
				.getInstance().getBean(Constant.SpringDefine.SRV_OPERATE_TASK);
		final ServiceDecomposerTask task = srvOperateTask.getTask(taskID,
				tenantID);
		final String seJsonBody = JsonConvertUtils.covJson2SvcEx(
				serBaseJsonObj.toString(), task.getSeJobID());

		final JSONObject jsonObject = JSONObject.fromObject(seJsonBody);

		final JobBean job = createJob(jsonObject, attributeJson,
				EnumJobType.EXECUTE);
		IScheduler scheduler = (IScheduler) DecomposerContextHelper
				.getInstance().getBean("scheduler");
		scheduler.addNewJob(job);

		srvOperateTask.updateTaskStatusAndWriteLog(task,
				EnumProgress.EXECUTE.getName(), "execute start", "");
	}

	private void attachResID(final JSONObject srvDataJsonObject,
			final String tenantID) throws ServiceException {
		String serviceID = srvDataJsonObject.getString(Constant.SERVICEID);
		IServiceResDao srvResDao = (IServiceResDao) DecomposerContextHelper
				.getInstance().getBean("srvResDao");
		Result<List<ResMapping>> queryResResult = srvResDao
				.getResMappingBySvcID(serviceID, tenantID);
		ErrorCodeUtil.checkResult(logger, queryResResult,
				"Get Resource failed! ServiceID: " + serviceID,
				ErrorCode.SD_OPER_DB_ERROR);

		Map<String, ResMapping> resMappingMap = DecomposerUtil
				.resMapping2Map(queryResResult.getData());

		JSONObject jsonRess = srvDataJsonObject
				.getJSONObject(Constant.RESOURCES);
		Iterator<?> keys = jsonRess.keys();

		while (keys.hasNext()) {
			String resourceLabel = keys.next().toString();
			JSONObject jsonRes = jsonRess.getJSONObject(resourceLabel);
			ResMapping resMapping = resMappingMap.get(resourceLabel);
			if (resMapping != null) {
				jsonRes.put(Constant.ID, resMapping.getResourceID());
			}
		}
	}

	private static JSONObject getMatchInResJsonObject(
			final JSONObject inResJsonObject, final String reReskey) {
		JSONObject matchInResJsonObject = null;
		if (inResJsonObject.containsKey(reReskey)) {
			matchInResJsonObject = inResJsonObject.getJSONObject(reReskey);
		}

		return matchInResJsonObject;
	}

	private JobBean createJob(final JSONObject contextJsonObject,
			final JSONObject attributeJson, final EnumJobType jobType)
			throws ServiceException {

		final JobBean job = new JobBean();

		job.setId(UUIDUtils.createUuid());
		job.setType(jobType.getName());
		job.setContext(DecomposerUtil.encrypt(contextJsonObject.toString()));
		job.setAttribute(attributeJson.toString());
		job.setVersion(0);

		return job;
	}

	private void addSerialDecomposerJob(final ServiceDecomposerTask task,
			final String inJsonString) throws ServiceException {
		final JSONObject inJsonObject = JSONObject.fromObject(inJsonString);
		final JSONObject srvJsonObject = inJsonObject
				.getJSONObject(Constant.SERVICE);
		final JSONObject inResjsonObj = srvJsonObject
				.getJSONObject(Constant.RESOURCES);

		if (inResjsonObj.isEmpty()) {
			logger.warn("Resource is empty after decompose! Nothing to execute!");

			IServiceOperateTask srvOperateTask = (IServiceOperateTask) DecomposerContextHelper
					.getInstance().getBean(
							Constant.SpringDefine.SRV_OPERATE_TASK);
			srvOperateTask.updateTaskStatusAndWriteLog(task,
					EnumProgress.COMPLETE.getName(), ErrorCode.SUCCESS,
					"execute complete");

			ServicedecomposerAuditLog.addAuditLogEnd(task);
			return;
		}

		final Iterator<?> it = inResjsonObj.keys();
		final JSONObject domainJsonObject = new JSONObject();

		String storeDomain = DecomposerUtil.getTaskRedisStoreDomain(task
				.getTaskID());
		final MapOper<JSONObject> taskMapOper = new MapOper<JSONObject>(
				storeDomain, Constant.REDIS_DB);

		while (it.hasNext()) {
			final String resKey = (String) it.next();
			final JSONObject inResJsonObject = inResjsonObj
					.getJSONObject(resKey);
			final String resType = inResJsonObject.getString(Constant.TYPE);

			final ServiceDecomposerMapping rstSDMap = getDecomposerMapping(
					resType, RegType.RESOURCE.getName());
			if (rstSDMap == null) {
				logger.warn("can not get the registered service! type:"
						+ resType);

				taskMapOper.put(Constant.DECOMPOSED_RES, resKey,
						inResJsonObject);
			} else {
				JsonConvertUtils.assembleRes(domainJsonObject, rstSDMap,
						resKey, inResJsonObject);
			}
		}

		if (domainJsonObject.isEmpty()) {

			processAfterDecomposed(task.toJobAttr(), task.getTaskID());
		} else {

			final JobBean job = createJob(domainJsonObject, task.toJobAttr(),
					EnumJobType.DECOMPOSE);
			IScheduler scheduler = (IScheduler) DecomposerContextHelper
					.getInstance().getBean("scheduler");
			scheduler.addNewJob(job);
		}
	}

	/**
	 * process execute result
	 * 
	 * @param result execute result
	 * @param context context
	 * @param attributeJsonObject attribute Json Object
	 * @throws ServiceException
	 * @since crossdomain 0.5
	 */
	public void processExecuteResult(final Result<String> result,
			final String context, final JSONObject attributeJsonObject)
			throws ServiceException {
		final String data = result.getData();
		ErrorCodeUtil.hasLength(logger, data, "Service Exectue error!",
				ErrorCode.SD_TASK_EXECUTE_FAIL);

		JSONObject dataObject = JSONObject.fromObject(data);
		JSONObject srvJobObject = dataObject
				.getJSONObject(Constant.SERVICE_JOB);
		JSONObject srvBodyJsonObject = srvJobObject
				.getJSONObject(Constant.SERVICE);
		final String serviceID = attributeJsonObject
				.getString(Constant.SERVICEID);
		final String tenantID = attributeJsonObject
				.getString(Constant.TENANT_ID);
		final JSONObject resourceJson = srvBodyJsonObject
				.getJSONObject(Constant.RESOURCES);

		IServiceResDao srvResDao = (IServiceResDao) DecomposerContextHelper
				.getInstance().getBean("srvResDao");
		Result<List<ResMapping>> queryResResult = srvResDao
				.getResMappingBySvcID(serviceID, tenantID);
		ErrorCodeUtil.checkResult(logger, queryResResult,
				"Get Resource failed! ServiceID: " + serviceID,
				ErrorCode.SD_OPER_DB_ERROR);

		List<ResMapping> updateResMappings = new ArrayList<ResMapping>();
		List<ResMapping> deleteResMappings = new ArrayList<ResMapping>();
		assembleRes(queryResResult.getData(), resourceJson, updateResMappings,
				deleteResMappings);

		updateRes(updateResMappings);

		Result<Object> delResResult = srvResDao.delete(deleteResMappings);
		ErrorCodeUtil.checkResult(logger, delResResult,
				"Delete Resource failed! Count: " + deleteResMappings.size(),
				ErrorCode.SD_OPER_DB_ERROR);

		IServiceOperateTask srvOperateTask = (IServiceOperateTask) DecomposerContextHelper
				.getInstance().getBean(Constant.SpringDefine.SRV_OPERATE_TASK);

		TaskHeader taskHeader = new TaskHeader(attributeJsonObject);
		srvOperateTask.updateTaskStatusAndWriteLog(taskHeader,
				EnumProgress.COMPLETE.getName(), ErrorCode.SUCCESS,
				"execute complete");
	}

	private void assembleRes(final List<ResMapping> resMappings,
			final JSONObject resourceJson,
			final List<ResMapping> updateResMappings,
			final List<ResMapping> deleteResMappings) {

		Map<String, ResMapping> resMap = DecomposerUtil
				.resMapping2Map(resMappings);

		Iterator<?> keys = resourceJson.keys();
		while (keys.hasNext()) {
			String resLabel = (String) keys.next();
			ResMapping resMapping = resMap.get(resLabel);
			if (resMapping != null) {
				JSONObject resJsonObject = resourceJson.getJSONObject(resLabel);
				final String action = JsonUtils.getString(resJsonObject,
						Constant.ACTION);
				final JSONObject resultJsonObject = JsonUtils.getJsonObject(
						resJsonObject, Constant.RESULT);

				// final JSONObject resultJsonObject =
				// resJsonObject.getJSONObject(Constant.RESULT);
				String result = "";
				String resultReason = "";
				if (resultJsonObject == null) {
					result = ErrorCode.SUCCESS;
					resultReason = "";
				} else {
					result = JsonUtils.getString(resultJsonObject,
							Constant.CODE);
					resultReason = JsonUtils.getString(resultJsonObject,
							Constant.REASON);
				}

				if (EnumOperType.DELETE.getName().equals(action)
						&& ErrorCode.SUCCESS.equalsIgnoreCase(result)) {
					deleteResMappings.add(resMapping);
				} else {

					resMapping.setResourceID(JsonUtils.getString(resJsonObject,
							Constant.ID));
					resMapping.setAcvtiveMode(getActionStatus(action, result));
					resMapping.setResult(result);
					resMapping.setResultReason(resultReason);

					updateResMappings.add(resMapping);
				}
			}
		}
	}

	private String getActionStatus(final String action, String result) {
		if (StringUtils.isEmpty(action) || StringUtils.isEmpty(result)
				|| (!ErrorCode.SUCCESS.equalsIgnoreCase(result))) {
			return null;
		}

		if (action.equals(OperType.ACTIVATE_DEFINE)) {
			return Constant.ACTIVE;
		} else if (action.equals(OperType.DEACTIVATE_DEFINE)) {
			return Constant.DEACTIVE;
		} else {
			return null;
		}
	}

	private ServiceDecomposerMapping getDecomposerMapping(final String resType,
			final String regType) throws ServiceException {
		IServiceDecomposerService sdService = (IServiceDecomposerService) DecomposerContextHelper
				.getInstance().getBean(
						Constant.SpringDefine.SRV_DECOMPOSER_SERVICE);
		final Result<ServiceDecomposerMapping> result = sdService
				.getServiceByType(resType, regType, null);
		ErrorCodeUtil.checkResult(logger, result,
				"getServiceByType failed! resType is: " + resType,
				ErrorCode.SD_OPER_DB_ERROR);

		return result.getData();
	}

	private void updateRes(final List<ResMapping> resMappings)
			throws ServiceException {
		IServiceResDao srvResDao = (IServiceResDao) DecomposerContextHelper
				.getInstance().getBean("srvResDao");

		for (ResMapping resMapping : resMappings) {
			Result<Object> updateResResult = srvResDao.update(resMapping);
			ErrorCodeUtil.checkResult(
					logger,
					updateResResult,
					"Update Resource failed! ResID: "
							+ resMapping.getResourceID(),
					ErrorCode.SD_OPER_DB_ERROR);
		}
	}

}
