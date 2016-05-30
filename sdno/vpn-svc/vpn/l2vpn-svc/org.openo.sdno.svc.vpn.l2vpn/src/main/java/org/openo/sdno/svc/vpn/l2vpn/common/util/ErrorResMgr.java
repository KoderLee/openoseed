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
package org.openo.sdno.svc.vpn.l2vpn.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import com.puer.framework.container.util.SysEnvironment;

/**
 * Class to deal with error message.<br/>
 * 
 * @author
 * @version SDNO 0.5 2016-3-17
 */
public class ErrorResMgr {

    private static ErrorResMgr instance = null;

    private Map<String, Properties> errorProperties = new Hashtable<String, Properties>();

    private static final String LANGUAGE = SysEnvironment.getLocale().getLanguage() + "_"
            + SysEnvironment.getLocale().getCountry();

    /**
     * Constructor.
     * 
     * @since SDNO 0.5
     */
    public ErrorResMgr() {
        String filePath =
                (new StringBuilder(getAppPath())).append(File.separator).append("res").append(File.separator)
                        .append("errorcode").append(File.separator).toString();

        errorProperties.put("en_US", readPropertiesFile(filePath + "errorcode_en_US.properties"));

        errorProperties.put("zh_CN", readPropertiesFile(filePath + "errorcode_zh_CN.properties"));
    }

    /**
     * Get singleton instance.<br/>
     * 
     * @return singleton instance of the class
     * @since SDNO 0.5
     */
    public static synchronized ErrorResMgr getInstance() {
        if(instance == null) {
            instance = new ErrorResMgr();
        }
        return instance;
    }

    /**
     * Get the description of associated error key.<br/>
     * 
     * @param key Key of error
     * @return Description of error
     * @since SDNO 0.5
     */
    public String getErrorMessage(String key) {
        return getErrorMessage(LANGUAGE, key);
    }

    private String getErrorMessage(String language, String key) {
        if(StringUtils.isEmpty(language)) {
            return key;
        }
        Properties pro = errorProperties.get(language);
        if(pro != null) {
            return (String)pro.get(key);
        }
        return key;
    }

    private String getAppPath() {
        String root =
                SysEnvironment.getAppRoot() + File.separator + "module" + File.separator
                        + "org.openo.sdno.svc.vpn.l2vpn";
        return root;
    }

    private Properties readPropertiesFile(String filename) {
        Properties properties = new Properties();
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(filename);
            properties.load(inputStream);
        } catch(IOException e) {
        } finally {
            if(inputStream != null) {
                try {
                    inputStream.close();
                } catch(IOException e) {
                }
            }
        }
        return properties;
    }
}
