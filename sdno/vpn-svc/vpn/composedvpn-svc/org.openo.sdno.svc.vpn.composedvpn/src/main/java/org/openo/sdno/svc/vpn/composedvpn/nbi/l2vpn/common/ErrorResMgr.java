/*
 * Copyright (c) 2016, Huawei Technologies Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *  
 */
package org.openo.sdno.svc.vpn.composedvpn.nbi.l2vpn.common;

import java.io.File;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import com.puer.framework.container.util.SysEnvironment;

/**
 * Error resource manager.<br/>
 * 
 * @author
 * @version SDNO 0.5 16-Mar-2016
 */
public class ErrorResMgr {

    private static ErrorResMgr instance = null;

    private final Map<String, Properties> errorProperties = new Hashtable<String, Properties>();

    private static final String LANGUAGE = SysEnvironment.getLocale().getLanguage() + "_"
            + SysEnvironment.getLocale().getCountry();

    /**
     * Constructor.
     * 
     * @since SDNO 0.5
     */
    private ErrorResMgr() {
        String filePath =
                (new StringBuilder(ResourceUtils.getAppPath())).append(File.separator).append("res")
                        .append(File.separator).append("errorcode").append(File.separator).toString();

        errorProperties.put("en_US", ResourceUtils.readPropertiesFile(filePath + "errorcode_en_US.properties"));

        errorProperties.put("zh_CN", ResourceUtils.readPropertiesFile(filePath + "errorcode_zh_CN.properties"));
    }

    /**
     * Get error resource manager instance.
     * 
     * @since SDNO 0.5
     */
    public static synchronized ErrorResMgr getInstance() {
        if(instance == null) {
            instance = new ErrorResMgr();
        }

        return instance;
    }

    /**
     * Get error message.
     * 
     * @since SDNO 0.5
     */
    public String getErrorMessage(String key) {
        return getErrorMessage(LANGUAGE, key);
    }

    /**
     * Get error message.
     * 
     * @since SDNO 0.5
     */
    public String getErrorMessage(String language, String key) {

        if(StringUtils.isEmpty(language)) {
            return key;
        }

        Properties pro = errorProperties.get(language);
        if(pro != null) {
            return (String)pro.get(key);
        }

        return key;
    }
}
