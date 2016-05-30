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
package org.openo.crossdomain.servicemgr.util.authorization;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.openo.crossdomain.servicemgr.util.system.SysEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Authentication configuration.<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
public class AuthConfigInfo {

    private static final Logger logger = LoggerFactory.getLogger(AuthConfigInfo.class);

    private static final String AUTH_CONFIG_FILE = "identity.properties";

    private static AuthConfigInfo authConfig = new AuthConfigInfo();

    /**
     * Get authentication configuration instance.<br/>
     *
     * @return authentication configuration instance.
     * @since crossdomain 0.5
     */
    public static AuthConfigInfo getInstance() {
        return authConfig;
    }

    private String accessName;

    private String identity;

    private AuthConfigInfo() {
        Properties prop = new Properties();
        InputStream in = null;

        try {
            in =
                    new FileInputStream(SysEnvironment.getEtcPath() + File.separator + "tconf" + File.separator
                            + AUTH_CONFIG_FILE);
            prop.load(in);
        } catch(IOException e) {
            logger.error("loadAuthConfig can't find config file");
        } finally {
            IOUtils.closeQuietly(in);
        }

        accessName = prop.getProperty("name");
        identity = prop.getProperty("value");
    }

    public String getAccessName() {
        return accessName;
    }

    public String getIdentity() {
        return identity;
    }

}
