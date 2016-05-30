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
package org.openo.crossdomain.commonsvc.jobscheduler.job.impl;

import java.util.Map;

import org.openo.crossdomain.commonsvc.decomposer.constant.Constant;
import org.openo.crossdomain.commonsvc.decomposer.constant.ErrorCode;
import org.openo.crossdomain.commonsvc.decomposer.dao.inf.IServiceOperateDao;
import org.openo.crossdomain.commonsvc.decomposer.logutil.TaskHeader;
import org.openo.crossdomain.commonsvc.decomposer.model.EnumProgress;
import org.openo.crossdomain.commonsvc.decomposer.model.Result;
import org.openo.crossdomain.commonsvc.decomposer.model.ServiceDecomposerTask;
import org.openo.crossdomain.commonsvc.decomposer.service.impl.ServiceOperateServiceImpl;
import org.openo.crossdomain.commonsvc.decomposer.util.DecomposerContextHelper;
import org.openo.crossdomain.commonsvc.decomposer.util.DecomposerUtil;
import org.openo.crossdomain.commonsvc.decomposer.util.ErrorCodeUtil;

import net.sf.json.JSONObject;

import org.openo.commonservice.log.OssLog;
import org.openo.commonservice.log.OssLogFactory;
import org.openo.commonservice.redis.oper.MapOper;
import org.openo.commonservice.remoteservice.exception.ServiceException;

/**
 * abstract decompose job
 * 
 * @since crossdomain 0.5
 */
public abstract class AbsDecomposeJob extends AbsPersistentJob {

	/**
	 * logger
	 */
	protected final OssLog logger = OssLogFactory
			.getLogger(AbsDecomposeJob.class);

	/**
	 * Constructor
	 * 
	 * @since crossdomain 0.5
	 */
	public AbsDecomposeJob() {
		super();
	}

	protected boolean checkJobRestart(ServiceOperateServiceImpl service,
			JSONObject attributeJson) {

		String taskID = attributeJson.getString(Constant.TASK_ID);
		String tenantID = attributeJson.getString(Constant.TENANT_ID);
		String storeDomain = DecomposerUtil.getTaskRedisStoreDomain(taskID);
		final MapOper<Object> taskMapOper = new MapOper<Object>(storeDomain,
				Constant.REDIS_DB);
		final Map<String, Object> taskStatusJsonMap = taskMapOper.getAll(
				Constant.TASK_ATTR, Object.class);

		if (taskStatusJsonMap == null || taskStatusJsonMap.isEmpty()
				|| (!taskStatusJsonMap.containsKey(Constant.STATUS))) {
			try {

				IServiceOperateDao srvOperDao = (IServiceOperateDao) DecomposerContextHelper
						.getInstance().getBean(
								Constant.SpringDefine.SRV_OPERATE_DAO);
				Result<ServiceDecomposerTask> taskResult = srvOperDao
						.getTaskByTaskID(taskID, tenantID);
				ErrorCodeUtil.checkResult(logger, taskResult,
						"Get Task Error! taskID:" + taskID,
						ErrorCode.SD_OPER_DB_ERROR);

				ServiceDecomposerTask task = taskResult.getData();
				task.setToken(attributeJson.getString(Constant.TOKEN));
				service.executeDecompose(task);
			} catch (ServiceException e) {
				TaskHeader taskHeader = new TaskHeader(attributeJson);
				writeErrorLogAndUpdateTask(taskHeader,
						EnumProgress.DECOMPOSE.getName(), "decompose failed",
						e.getMessage());
			}

			return true;
		}

		return false;
	}

}
