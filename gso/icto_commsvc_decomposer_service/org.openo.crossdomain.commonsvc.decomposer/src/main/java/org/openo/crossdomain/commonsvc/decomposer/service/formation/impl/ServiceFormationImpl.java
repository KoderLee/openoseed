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
package org.openo.crossdomain.commonsvc.decomposer.service.formation.impl;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.openo.crossdomain.commonsvc.decomposer.constant.Constant;
import org.openo.crossdomain.commonsvc.decomposer.constant.ConstantURL;
import org.openo.crossdomain.commonsvc.decomposer.constant.ErrorCode;
import org.openo.crossdomain.commonsvc.decomposer.dao.inf.IServiceResDao;
import org.openo.crossdomain.commonsvc.decomposer.model.RegType;
import org.openo.crossdomain.commonsvc.decomposer.model.ResMapping;
import org.openo.crossdomain.commonsvc.decomposer.model.Result;
import org.openo.crossdomain.commonsvc.decomposer.model.ServiceDecomposerMapping;
import org.openo.crossdomain.commonsvc.decomposer.model.ServiceDecomposerTask;
import org.openo.crossdomain.commonsvc.decomposer.rest.util.RestfulMethod;
import org.openo.crossdomain.commonsvc.decomposer.rest.util.RestfulProxy;
import org.openo.crossdomain.commonsvc.decomposer.service.formation.inf.IServiceFormation;
import org.openo.crossdomain.commonsvc.decomposer.service.inf.IServiceDecomposerService;
import org.openo.crossdomain.commonsvc.decomposer.util.DecomposerContextHelper;
import org.openo.crossdomain.commonsvc.decomposer.util.ErrorCodeUtil;
import org.openo.crossdomain.commonsvc.decomposer.util.JsonConvertUtils;
import org.springframework.stereotype.Component;

import org.openo.commonservice.log.OssLog;
import org.openo.commonservice.log.OssLogFactory;
import org.openo.commonservice.remoteservice.exception.ServiceException;

/**
 * the implement for service design
 * 
 * @since crossdomain 0.5
 */
@Component(value = Constant.SpringDefine.SRV_FORMATION)
public class ServiceFormationImpl implements IServiceFormation {

	/**
	 * logger
	 */
	private final OssLog logger = OssLogFactory
			.getLogger(ServiceFormationImpl.class);

	/**
	 * @see IServiceFormation#serviceFormation(ServiceDecomposerTask)
	 */
	@Override
	public String serviceFormation(final ServiceDecomposerTask task)
			throws ServiceException {

		String srvBody = task.getServiceContent();

		String serviceType = JSONObject.fromObject(srvBody)
				.getJSONObject(Constant.SERVICE).getString(Constant.TYPE);

		IServiceDecomposerService sdService = (IServiceDecomposerService) DecomposerContextHelper
				.getInstance().getBean(
						Constant.SpringDefine.SRV_DECOMPOSER_SERVICE);
		final Result<ServiceDecomposerMapping> rstSDMap = sdService
				.getServiceByType(serviceType, RegType.SERVICE.getName(), null);
		ErrorCodeUtil.checkResult(logger, rstSDMap, "getServiceByType failed!",
				ErrorCode.SD_OPER_DB_ERROR);

		ErrorCodeUtil.checkObject(logger, rstSDMap.getData(),
				"Can not found registered service. serviceType is "
						+ serviceType, ErrorCode.SD_BAD_PARAM);
		// if (null == rstSDMap.getData())
		// {
		// logger.error("Can not found registered service. serviceType is " +
		// serviceType);

		// }

		String action = task.getOperType();
		if (rstSDMap.getData().getOperType().contains(action)) {

			final String url = MessageFormat.format(
					ConstantURL.SERVICE_DECOMPOSE, rstSDMap.getData()
							.getUriprefix(), action, task.getServiceID());

			Map<String, String> mapHeader = new HashMap<String, String>();
			mapHeader.put(Constant.Security.X_AUTH_TOKEN, task.getToken());
			mapHeader.put(Constant.Security.X_CLIENT_ADDR,
					task.getUser() != null ? task.getUser().getTerminal()
							: Constant.BLANK_STRING);
			final Result<String> rst = RestfulProxy.restSend(RestfulMethod.PUT,
					url, srvBody, mapHeader);
			ErrorCodeUtil.checkResult(logger, rst, action
					+ " Service Formation failed!",
					ErrorCode.SD_TASK_EXECUTE_FAIL);

			return rst.getData();
		} else {

			IServiceResDao srvResDao = (IServiceResDao) DecomposerContextHelper
					.getInstance().getBean("srvResDao");
			Result<List<ResMapping>> resMappingsResult = srvResDao
					.getResMappingBySvcID(task.getServiceID(),
							task.getTenantID());
			ErrorCodeUtil.checkResult(logger, resMappingsResult,
					"Get Resource failed!", ErrorCode.SD_OPER_DB_ERROR);

			return JsonConvertUtils.covMode2SvcDesign(task,
					resMappingsResult.getData());
		}
	}
}
