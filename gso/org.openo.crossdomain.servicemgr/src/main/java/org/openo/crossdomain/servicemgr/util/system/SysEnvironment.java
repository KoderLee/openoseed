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
package org.openo.crossdomain.servicemgr.util.system;

import java.io.File;
import java.util.Locale;

import org.openo.baseservice.deploy.util.DefaultEnvUtil;

/**
 * Get system environment.<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
public class SysEnvironment {

    public static final String getHome() {
        return DefaultEnvUtil.getOssRoot();
    }

    public static final String getEtcPath() {
        return DefaultEnvUtil.getAppRoot() + File.separator + "etc";
    }

    public static final String getAppRoot() {
        return DefaultEnvUtil.getAppRoot();
    }

    public static final String getAppPath(String appName) {
        return getAppRoot() + File.separator + "module" + File.separator + appName;
    }

    public static final String getConfPath(String appName) {
        return getAppPath(appName) + File.separator + "config";
    }

    public static final String getVarPath() {
        return DefaultEnvUtil.getAppShareDir();
    }

    public static final String getLanguage() {
        return DefaultEnvUtil.getOssLang();
    }

    public static final Locale getLocale() {
        String language = getLanguage();
        String[] str = language.split("_");
        if((str == null) || (str.length != 2)) {
            throw new IllegalArgumentException("Invalid language: " + language);
        }
        return new Locale(str[0], str[1]);
    }
}
