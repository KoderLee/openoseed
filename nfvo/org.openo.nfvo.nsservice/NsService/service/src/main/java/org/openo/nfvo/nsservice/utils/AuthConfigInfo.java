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

package org.openo.nfvo.nsservice.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.openo.baseservice.deploy.util.DefaultEnvUtil;

/**
 * 
* Keep the authentication configuration status, used for token create<br/>
* <p>
* </p>
* 
* @author
* @version NFVO 0.5 May 15, 2016
 */
public final class AuthConfigInfo {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthConfigInfo.class);

    private static final String AUTH_CONFIG_FILE = "identity.properties";

    private String userName;

    private String encryptedPw;

    private String domain;

    private static AuthConfigInfo authConfig = new AuthConfigInfo();

    public static AuthConfigInfo getInstance() {
        return authConfig;
    }

    private String getAuthConfigPath() {
        return DefaultEnvUtil.getAppRoot() + File.separator + "etc" + File.separator + AUTH_CONFIG_FILE;
    }

    private AuthConfigInfo() {
        Properties prop = new Properties();
        InputStream in = null;

        try {
            in = new FileInputStream(getAuthConfigPath());
            prop.load(in);
        } catch(IOException e) {
            LOGGER.error("loadAuthConfig can't find config file>>" + e);
        } finally {
            try {
                if(in != null) {
                    in.close();
                }
            } catch(IOException e) {
                LOGGER.error("release InputStream exception" + e);
            }
        }

        userName = prop.getProperty("name");
        encryptedPw = prop.getProperty("value");
        domain = prop.getProperty("domain");
    }

    public String gotUserName() {
        return userName;
    }

    public String gotEncryptedPw() {
        return encryptedPw;
    }

    public String gotDomain() {
        return domain;
    }

}
