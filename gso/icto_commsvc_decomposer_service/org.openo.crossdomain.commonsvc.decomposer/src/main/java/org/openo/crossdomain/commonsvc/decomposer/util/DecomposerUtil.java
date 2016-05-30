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

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.openo.crossdomain.commonsvc.decomposer.constant.Constant;
import org.openo.crossdomain.commonsvc.decomposer.constant.ConstantURL;
import org.openo.crossdomain.commonsvc.decomposer.constant.ErrorCode;
import org.openo.crossdomain.commonsvc.decomposer.model.ResMapping;
import org.openo.crossdomain.commonsvc.decomposer.model.ServiceDecomposerMapping;

import org.openo.commonservice.encrypt.cbb.CipherException;
import org.openo.commonservice.encrypt.cbb.CipherManager;
import org.openo.commonservice.encrypt.cbb.KeyType;
import org.openo.commonservice.log.OssLog;
import org.openo.commonservice.log.OssLogFactory;
import org.openo.commonservice.redis.oper.MapOper;
import org.openo.commonservice.remoteservice.exception.ServiceException;

/**
 * Decompose util class, provider common method
 * 
 * @since crossdomain 0.5
 */
public class DecomposerUtil {

	/**
	 * Logger
	 */
	private static final OssLog logger = OssLogFactory
			.getLogger(DecomposerUtil.class);

	/**
	 * get the service key of redis
	 * 
	 * @param typeName name
	 * @param regType register type
	 * @return
	 * @since crossdomain 0.5
	 */
	public static String getServiceKeyForRedis(final String typeName,
			final String regType) {
		return (typeName + ":" + regType);
	}

	/**
	 * To determine whether it is empty list
	 * 
	 * @param aList list
	 * @return false: list is empty ,true: list is not empty
	 * @since crossdomain 0.5
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isListHasRealLength(final List aList) {
		if (CollectionUtils.isEmpty(aList)
				|| ((aList.size() == 1) && (aList.get(0) == null))) {
			return false;
		}

		return true;
	}

	/**
	 * construct URL
	 * 
	 * @param versionDomain version and domain string
	 * @param tenantID tenant ID
	 * @param serviceID service
	 * @return formatted URL
	 * @since crossdomain 0.5
	 */
	public static String constructURL(final String versionDomain,
			final String tenantID, final String serviceID) {
		return MessageFormat.format(ConstantURL.RESOURCE_DESIGN, versionDomain,
				serviceID);

	}

	/**
	 * check URI prefix
	 * 
	 * @param mapping decomposition rules list
	 * @throws ServiceException if uri prefix is empty
	 * @since crossdomain 0.5
	 */
	public static void checkUriPrefix(
			final List<ServiceDecomposerMapping> mapping)
			throws ServiceException {
		for (ServiceDecomposerMapping map : mapping) {
			if (StringUtils.isNotEmpty(map.getUriprefix())) {
				continue;
			} else {
				throw new ServiceException(ErrorCode.SD_BAD_PARAM,
						"Url_prefix is empty. type is " + map.getTypeName());
			}
		}

	}

	/**
	 * get redis key of task
	 * 
	 * @param taskID task ID
	 * @return the redis key of task
	 * @since crossdomain 0.5
	 */
	public static String getTaskRedisStoreDomain(final String taskID) {
		return "decomposer:" + taskID;
	}

	/**
	 * convert ResMapping to map
	 * 
	 * @param resMappings ResMapping list
	 * @return the map of ResMappings
	 * @since crossdomain 0.5
	 */
	public static Map<String, ResMapping> resMapping2Map(
			final List<ResMapping> resMappings) {

		Map<String, ResMapping> resMap = new HashMap<String, ResMapping>();
		for (ResMapping resMapping : resMappings) {
			resMap.put(resMapping.getResourceLabel(), resMapping);
		}
		return resMap;
	}

	/**
	 * clear redis of task by task ID
	 * 
	 * @param taskID task ID
	 * @since crossdomain 0.5
	 */
	public static void clearTaskRedis(String taskID) {
		final MapOper<JSONObject> taskMapOper = new MapOper<JSONObject>(
				DecomposerUtil.getTaskRedisStoreDomain(taskID),
				Constant.REDIS_DB);
		taskMapOper.clear(Constant.TASK_ATTR);
		taskMapOper.clear(Constant.DECOMPOSED_RES);
	}

	/**
	 * encrypt
	 * 
	 * @param cipher cipher string
	 * @return encrypted string
	 * @throws ServiceException when CipherException occured
	 * @since crossdomain 0.5
	 */
	public static String encrypt(String cipher) throws ServiceException {
		if (StringUtils.isEmpty(cipher)) {
			logger.error("encrypt cipher is empty!");
			return Constant.BLANK_STRING;
		}

		try {
			long start = System.currentTimeMillis();
			logger.warn("encrypt start:" + start);

			char[] encrypted = CipherManager.getInstance().encrypt(
					cipher.toCharArray(), KeyType.SHARE, "common_shared");

			long end = System.currentTimeMillis();
			logger.warn("encrypt end:" + end);
			logger.warn("encrypt time:" + (end - start));

			if (encrypted == null) {
				logger.error("The encrypted cipher is null!");
				return Constant.BLANK_STRING;
			}

			return new String(encrypted);
		} catch (CipherException e) {
			logger.error(e.toString());
			throw new ServiceException(ErrorCode.SD_DATA_ENCRYPT_FAIL,
					e.toString());
		}
	}

	/**
	 * decrypt
	 * 
	 * @param cipher cipher string
	 * @return decrypted string
	 * @throws ServiceException when CipherException occured
	 * @since crossdomain 0.5
	 */
	public static String decrypt(String cipher) throws ServiceException {
		if (StringUtils.isEmpty(cipher)) {
			logger.error("decrypt cipher is empty!");
			return Constant.BLANK_STRING;
		}

		try {
			long start = System.currentTimeMillis();
			logger.warn("decrypt start:" + start);

			char[] decrypted = CipherManager.getInstance().decrypt(
					cipher.toCharArray(), KeyType.SHARE, "common_shared");

			long end = System.currentTimeMillis();
			logger.warn("decrypt end:" + end);
			logger.warn("decrypt time:" + (end - start));

			if (decrypted == null) {
				logger.error("The decrypted cipher is null!");
				return Constant.BLANK_STRING;
			}

			return new String(decrypted);
		} catch (CipherException e) {
			logger.error(e.toString());
			throw new ServiceException(ErrorCode.SD_DATA_DECRYPT_FAIL,
					e.toString());
		}
	}

	/**
	 * convert \r\n
	 * 
	 * @param input convert string
	 * @return converted string
	 * @since crossdomain 0.5
	 */
	public static String escapeLinefeed(String input) {
		if (input == null) {
			return input;
		}

		String output = input.replace("\r", "\\r").replace("\n", "\\n");
		return output;
	}
}
