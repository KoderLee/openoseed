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
import java.util.Locale;

import org.openo.commonservice.deploy.util.DefaultEnvUtil;

/**
 * system environment util class
 * 
 * @since crossdomain 0.5
 */
public class SysEnvironment {

	public static final String getHome() {
		return DefaultEnvUtil.getOssRoot();
	}

	/**
	 * get ect path
	 * 
	 * @return ect path
	 * @since crossdomain 0.5
	 */
	public static final String getEtcPath() {
		return DefaultEnvUtil.getAppRoot() + File.separator + "etc"
				+ File.separator + "tconf";
	}

	/**
	 * get app root path
	 * 
	 * @return app root path
	 * @since crossdomain 0.5
	 */
	public static final String getAppRoot() {
		return DefaultEnvUtil.getAppRoot();
	}

	/**
	 * get app root path by app name
	 * 
	 * @param appName app name
	 * @return app root path
	 * @since crossdomain 0.5
	 */
	public static final String getAppPath(String appName) {
		return getAppRoot() + File.separator + "module" + File.separator
				+ appName;
	}

	/**
	 * get current language
	 * 
	 * @return current language
	 * @since crossdomain 0.5
	 */
	public static final String getLanguage() {
		return DefaultEnvUtil.getOssLang();
	}

	/**
	 * get current locale
	 * 
	 * @return current locale
	 * @since crossdomain 0.5
	 */
	public static final Locale getLocale() {
		String language = getLanguage();
		String[] str = language.split("_");
		if ((str == null) || (str.length != 2)) {
			throw new IllegalArgumentException("Invalid language: " + language);
		}
		return new Locale(str[0], str[1]);
	}
}
