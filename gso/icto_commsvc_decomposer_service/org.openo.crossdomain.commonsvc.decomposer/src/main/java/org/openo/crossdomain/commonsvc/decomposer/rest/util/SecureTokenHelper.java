/*******************************************************************************
 * Copyright (c) 2016, Huawei Technologies Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package org.openo.crossdomain.commonsvc.decomposer.rest.util;

import java.util.logging.Logger;

import org.openo.crossdomain.commonsvc.decomposer.constant.Constant;
import org.openo.crossdomain.commonsvc.decomposer.constant.ErrorCode;
import org.openo.crossdomain.commonsvc.decomposer.model.Result;

/**
 * Secure Token Helper Class <br/>
 * <p>
 * 
 * @author
 * @version crossdomain 0.5
 */
public class SecureTokenHelper {

	/**
	 * the rest URI to get token
	 */
	private static final String URL_TOKEN = "/v3/auth/tokens";

	/**
	 * logger for the SecureTokenHelper class<br>
	 */
	private static Logger logger = LoggerFactory
			.getLogger(SecureTokenHelper.class);

	/**
	 * data body to get token
	 */
	private static String TOKEN_POST_BODY = "{\"auth\": {\"identity\": {\"methods\": [\"password\"],\"password\": {\"user\": {\"name\": \"%s\",\"password\": \"%s\",\"domain\": {\"name\": \"%s\"}}}},\"scope\": {\"project\": {\"name\": \"%s\"}}}}";

	/**
	 * get auth token <br/>
	 * 
	 * @param userName user name
	 * @param passwd pwd
	 * @return token string
	 * @since crossdomain 0.5
	 */
	public static String getAuthToken(String userName, String passwd) {
		Result<RestfulResponse> rsp = new Result<RestfulResponse>();
		try {
			char pd[] = CipherManager.getInstance().decrypt(
					passwd.toCharArray(), KeyType.SHARE, "common_shared");

			if (pd == null) {
				logger.error("SecureTokenHelper.getAuthToken>>pd is null");
				return "";
			}

			// requestBody.replace(passwd, new String(pd));
			String requestBody = String.format(TOKEN_POST_BODY, userName,
					new String(pd), "op_service", "op_service");
			clearSensitivityMem(pd);

			rsp = RestfulProxy.restSend(RestfulMethod.POST, URL_TOKEN,
					requestBody, 300000);

			if (!ErrorCode.SUCCESS.equalsIgnoreCase(rsp.getRetCode())) {
				logger.error(
						"SecureTokenHelper.getAuthToken>>http post error, errorCode:",
						rsp.getRetCode());
				return "";
			}
		} catch (CipherException e) {
			logger.error("SecureTokenHelper.getAuthToken>>dcrpt throw an CipherException");
			return "";
		} catch (Exception e) {
			logger.error("SecureTokenHelper.getAuthToken>>send post request throw an exception");
			return "";
		}

		return rsp.getData()
				.getRespHeaderStr(Constant.Security.X_SUBJECT_TOKEN);
	}

	/**
	 * clear Sensitivity Memory <br/>
	 * 
	 * @param content content char array
	 * @since crossdomain 0.5
	 */
	public static void clearSensitivityMem(char[] content) {
		if (content != null && content.length > 0) {
			for (int i = 0; i < content.length; i++) {
				content[i] = '\000';
			}
		}
	}

	/**
	 * get decomposer auth token <br/>
	 * 
	 * @return token
	 * @since crossdomain 0.5
	 */
	public static String getDecomposerAuthToken() {
		return getAuthToken(AuthConfigInfo.getInstance().getUserName(),
				AuthConfigInfo.getInstance().getEncryptedPW());
	}

}
