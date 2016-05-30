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
package org.openo.crossdomain.commonsvc.decomposer.rest.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpStatus;
import org.openo.crossdomain.commonsvc.decomposer.constant.ErrorCode;
import org.openo.crossdomain.commonsvc.decomposer.model.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.openo.commonservice.remoteservice.exception.ServiceException;
import org.openo.commonservice.roa.util.restclient.RestfulOptions;
import org.openo.commonservice.roa.util.restclient.RestfulParametes;
import org.openo.commonservice.roa.util.restclient.RestfulResponse;

/**
 * Restful proxy
 * 
 * @since crossdomain 0.5
 */
public class RestfulProxy {

	private static final Logger logger = LoggerFactory
			.getLogger(RestfulProxy.class);

	public static boolean isSuccessErrorCode(int errorCode) {
		if (errorCode < HttpStatus.SC_OK) {
			return false;
		}

		if (errorCode > HttpStatus.SC_MULTI_STATUS) {
			return false;
		}

		return true;
	}

	/**
	 * send a REST request
	 * 
	 * @param action action
	 * @param uri URI
	 * @param jsonData json string
	 * @param mapHeader header
	 * @return send result
	 * @since crossdomain 0.5
	 */
	public static Result<String> restSend(final RestfulMethod action,
			final String uri, final String jsonData,
			final Map<String, String> mapHeader) {
		RestfulParametes restParametes = new RestfulParametes();

		Map<String, String> header = new HashMap<String, String>(mapHeader);
		// restParametes.putHttpContextHeader("Content-Type",
		// "application/json;charset=UTF-8");
		header.put("Content-Type", "application/json;charset=UTF-8");
		header.put("Accept", "application/json");
		restParametes.setHeaderMap(header);
		restParametes.setRawData(jsonData);

		Result<String> rsp = restSend(action, uri, restParametes);

		return rsp;
	}

	/**
	 * send a REST request
	 * 
	 * @param action action
	 * @param uri URI
	 * @param restParametes parameter
	 * @return send result
	 * @since crossdomain 0.5
	 */
	public static Result<String> restSend(final RestfulMethod action,
			final String uri, final RestfulParametes restParametes) {
		String retCode = ErrorCode.SUCCESS;

		Result<String> rsp = new Result<String>();
		RestfulResponse response = null;
		try {
			response = send(action, uri, restParametes);
		} catch (ServiceException e) {
			retCode = ErrorCode.FAIL;
		} catch (Exception e) {
			retCode = ErrorCode.FAIL;
		}

		if (response != null) {
			if (isSuccessErrorCode(response.getStatus())) {
				rsp.setData(response.getResponseContent());
			} else {
				retCode = ErrorCode.FAIL;
				logger.error(uri + ":ErrorCode:" + response.getStatus());
			}
			rsp.setStatusCode(response.getStatus());
		} else {
			retCode = ErrorCode.FAIL;
		}

		rsp.setRetCode(retCode);

		return rsp;
	}

	/**
	 * send a REST request
	 * 
	 * @param action action
	 * @param uri URI
	 * @param jsonData json string
	 * @param timeOut time out
	 * @return send result
	 * @since crossdomain 0.5
	 */
	public static Result<RestfulResponse> restSend(final RestfulMethod action,
			final String uri, final String jsonData, final int timeOut) {
		RestfulParametes restParametes = new RestfulParametes();

		restParametes.putHttpContextHeader("Content-Type",
				"application/json;charset=UTF-8");
		restParametes.setRawData(jsonData);

		RestfulOptions option = new RestfulOptions();
		option.setRestTimeout(timeOut);

		Result<RestfulResponse> rsp = restSend(action, uri, restParametes,
				option);

		return rsp;
	}

	/**
	 * send a REST request
	 * 
	 * @param action action
	 * @param uri URI
	 * @param restParametes parameter
	 * @param restOptions options
	 * @return send result
	 * @since crossdomain 0.5
	 */
	public static Result<RestfulResponse> restSend(final RestfulMethod action,
			final String uri, final RestfulParametes restParametes,
			final RestfulOptions restOptions) {
		String retCode = ErrorCode.SUCCESS;

		Result<RestfulResponse> rsp = new Result<RestfulResponse>();
		RestfulResponse response = null;
		try {
			response = send(action, uri, restParametes, restOptions);
		} catch (ServiceException e) {
			retCode = ErrorCode.FAIL;
		}

		if (response != null) {
			if (isSuccessErrorCode(response.getStatus())) {
				rsp.setData(response);
			} else {
				retCode = ErrorCode.FAIL;
				logger.error(uri + ":ErrorCode:" + response.getStatus());
			}
			rsp.setStatusCode(response.getStatus());
		} else {
			retCode = ErrorCode.FAIL;
		}

		rsp.setRetCode(retCode);

		return rsp;
	}

	private static RestfulResponse send(final RestfulMethod action,
			final String uri, final RestfulParametes restParametes)
			throws ServiceException {
		logger.warn("ServiceDecomposer::Send: " + action.name() + " " + uri);
		// if (action != RestfulMethod.GET)
		// {
		// logger.warn("ServiceDecomposer::Send Data: " +
		// restParametes.getRawData());
		// }
		RestfulResponse response = action.method(uri, restParametes);

		logger.warn("ServiceDecomposer::Receive Status: "
				+ response.getStatus());
		// logger.warn("ServiceDecomposer::Receive Data: " +
		// response.getResponseContent());

		return response;
	}

	private static RestfulResponse send(final RestfulMethod action,
			final String uri, final RestfulParametes restParametes,
			final RestfulOptions restOptions) throws ServiceException {
		logger.warn("ServiceDecomposer::Send Url: " + action.name() + " " + uri);

		RestfulResponse response = action.method(uri, restParametes,
				restOptions);

		logger.warn("ServiceDecomposer::Receive Status: "
				+ response.getStatus());
		// logger.warn("ServiceDecomposer::Receive Data: " +
		// response.getResponseContent());

		return response;
	}
}
