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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * authority configuration
 * 
 * @since crossdomain 0.5
 */
public class AuthConfigInfo {

	private static final Logger logger = LoggerFactory
			.getLogger(AuthConfigInfo.class);

	private static final String AUTH_CONFIG_FILE = "identity.properties";

	private static AuthConfigInfo authConfig = new AuthConfigInfo();

	/**
	 * get instance
	 * 
	 * @return instance
	 * @since crossdomain 0.5
	 */
	public static AuthConfigInfo getInstance() {
		return authConfig;
	}

	private String userName;

	private String encryptedPW;

	private AuthConfigInfo() {
		Properties prop = new Properties();

		try (InputStream in = new FileInputStream(SysEnvironment.getEtcPath()
				+ File.separator + AUTH_CONFIG_FILE)) {
			prop.load(in);
		} catch (IOException e) {
			logger.error("loadAuthConfig can't find config file!");
		}

		userName = prop.getProperty("name");
		encryptedPW = prop.getProperty("value");
	}

	public String getUserName() {
		return userName;
	}

	public String getEncryptedPW() {
		return encryptedPW;
	}

}
