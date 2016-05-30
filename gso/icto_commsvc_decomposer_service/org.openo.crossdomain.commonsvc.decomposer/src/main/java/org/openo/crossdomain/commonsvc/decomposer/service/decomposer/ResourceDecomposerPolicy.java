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
package org.openo.crossdomain.commonsvc.decomposer.service.decomposer;

import java.util.Map;

import org.openo.crossdomain.commonsvc.decomposer.constant.Constant;
import org.openo.crossdomain.commonsvc.decomposer.model.RegType;
import org.openo.crossdomain.commonsvc.decomposer.model.Result;
import org.openo.crossdomain.commonsvc.decomposer.model.ServiceDecomposerMapping;
import org.openo.crossdomain.commonsvc.decomposer.rest.util.RestfulMethod;
import org.openo.crossdomain.commonsvc.decomposer.rest.util.RestfulProxy;
import org.openo.crossdomain.commonsvc.decomposer.service.inf.IServiceDecomposerService;
import org.openo.crossdomain.commonsvc.decomposer.util.DecomposerContextHelper;
import org.openo.crossdomain.commonsvc.decomposer.util.DecomposerUtil;

import net.sf.json.JSONObject;

import org.openo.commonservice.log.OssLog;
import org.openo.commonservice.log.OssLogFactory;
import org.openo.commonservice.remoteservice.exception.ServiceException;

/**
 * resource decomposition policy
 * 
 * @since crossdomain 0.5
 */
public class ResourceDecomposerPolicy // extends AbstractDecomposerPolicy
{

	/**
	 * logger
	 */
	private static final OssLog logger = OssLogFactory
			.getLogger(ResourceDecomposerPolicy.class);

	/**
	 * resource design and return the result of designed resource
	 * 
	 * @param tenantID tenant ID
	 * @param serviceBaseJsonObj service base json object
	 * @param inResJsonObject input resource
	 * @param versionDomain version and domain string
	 * @param mapHeader Header map
	 * @return the result of designed resource
	 * @throws ServiceException
	 * @since crossdomain 0.5
	 */
	public static Result<String> queryDecResource(final String tenantID,
			final JSONObject serviceBaseJsonObj,
			final JSONObject inResJsonObject, final String versionDomain,
			final Map<String, String> mapHeader) throws ServiceException {
		final String serviceID = serviceBaseJsonObj
				.getString(Constant.SERVICEID);

		final JSONObject decJsonObjReq = constructDecomposerJsonReq(
				serviceBaseJsonObj, inResJsonObject);

		return queryDecResult(tenantID, decJsonObjReq, serviceID,
				versionDomain, mapHeader);
	}

	/**
	 * whether to end the design, if resource is atomic resources ,will be end
	 * of design
	 * 
	 * @param inResJsonObject input resource
	 * @param outResJsonObject output resource
	 * @return whether to end the design
	 * @throws ServiceException
	 * @since crossdomain 0.5
	 */
	public static boolean isEnd(final JSONObject inResJsonObject,
			final JSONObject outResJsonObject) throws ServiceException {

		if (inResJsonObject == null) {
			logger.info("inResJsonObject is null.");
			final String type = outResJsonObject.getString(Constant.TYPE);

			IServiceDecomposerService sdService = (IServiceDecomposerService) DecomposerContextHelper
					.getInstance().getBean(
							Constant.SpringDefine.SRV_DECOMPOSER_SERVICE);
			final Result<ServiceDecomposerMapping> sdMappingResult = sdService
					.getServiceByType(type, RegType.RESOURCE.getName(), null);
			if (sdMappingResult.checkSuccess()) {
				return (sdMappingResult.getData() == null);
			} else {
				return true;
			}
		}

		final String outResID = outResJsonObject.getString(Constant.ID);
		final String outResIDType = outResJsonObject.getString(Constant.TYPE);

		final String inResID = inResJsonObject.getString(Constant.ID);
		final String inResIDType = inResJsonObject.getString(Constant.TYPE);

		logger.info("inResIDType is " + inResIDType + " outResIDType is "
				+ outResIDType);
		if (inResID.equals(outResID) && inResIDType.equals(outResIDType)) {
			return true;
		}

		return false;
	}

	private static Result<String> queryDecResult(final String tenantID,
			final JSONObject inputJsonObj, final String serviceID,
			final String versionDomain, final Map<String, String> mapHeader) {
		logger.debug("QueryDecResult() is input: " + inputJsonObj.toString());

		String url = DecomposerUtil.constructURL(versionDomain, tenantID,
				serviceID);
		logger.debug("QueryDecResult() is input: " + url);

		Result<String> strRst = RestfulProxy.restSend(RestfulMethod.POST, url,
				inputJsonObj.toString(), mapHeader);

		return strRst;
	}

	/**
	 * construct json object for resource design
	 * 
	 * @param serviceJsonObj service json object
	 * @param resJsonObject resource json object
	 * @return json object for resource design
	 * @since crossdomain 0.5
	 */
	public static JSONObject constructDecomposerJsonReq(
			final JSONObject serviceJsonObj, final JSONObject resJsonObject) {

		JSONObject decJsonReq = JSONObject
				.fromObject(serviceJsonObj.toString());
		// JSONObject resJsonObj = new JSONObject();
		// resJsonObj.accumulate(reReskey, resJsonObject);
		decJsonReq.put(Constant.RESOURCES, resJsonObject);

		return decJsonReq;
	}
}
