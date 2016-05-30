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

package org.openo.nfvo.vimadapter.util;

import java.io.File;

import org.openo.baseservice.config.Config;
import org.openo.baseservice.config.ConfigBuilder;
import org.apache.commons.io.FileUtils;

/**
 * 
* Config the iaas layer certificate parameter<br/>
* <p>
* </p>
* 
* @author
* @version NFVO 0.5 May 15, 2016
 */
public final class IaaScertParamsConfig {
    private static final String CONFIG_FILE = System.getProperty("user.dir")
            + File.separator + "etc" + File.separator + "certificate"
            + File.separator + "ssl" + File.separator + "IaaS" + File.separator
            + "IaaSCertParamsConfig.xml";

    private static final String CERT_KEY_STORE_NAME = "keyStoreName";

    private static final String CERT_KEY_STORE_P_W_D = "keyStorePwd";

    private static final String CERT_KEY_STORE_FORMAT = "keyStoreFormat";

    private static final String CERT_TRUST_STORE_NAME = "trustStoreName";

    private static final String CERT_TRUST_STORE_P_W_D = "trustStorePwd";

    private static final String CERT_TRUST_STORE_FORMAT = "trustStoreFormat";

    private String keyStore;

    private String keyStorePwd;

    private String keyStoreFormat;

    private String trustStore;

    private String trustStorePwd;

    private String trustStoreFormat;

    public String gotKeyStore() {
        return keyStore;
    }

    public String gotKeyStorePwd() {
        return keyStorePwd;
    }

    public String gotKeyStoreFormat() {
        return keyStoreFormat;
    }

    public String gotTrustStore() {
        return trustStore;
    }

    public String gotTrustStorePwd() {
        return trustStorePwd;
    }

    public String gotTrustStoreFormat() {
        return trustStoreFormat;
    }

    private static IaaScertParamsConfig instance = null;

    private Config baseConfig = ConfigBuilder.getConfig(FileUtils
            .getFile(CONFIG_FILE));

    public static synchronized IaaScertParamsConfig getInstance() {
        if (null == instance) {
            instance = new IaaSCertParamsConfig();
        }
        return instance;
    }

    private IaaScertParamsConfig() {
        if (baseConfig != null) {
            StringBuilder basePath = new StringBuilder(
                    System.getProperty("user.dir"));

            basePath.append(File.separator);
            basePath.append("etc");
            basePath.append(File.separator);
            basePath.append("certificate");
            basePath.append(File.separator);
            basePath.append("ssl");
            basePath.append(File.separator);
            basePath.append("IaaS");
            basePath.append(File.separator);

            StringBuilder tsb = new StringBuilder(basePath);
            tsb.append("trust");
            tsb.append(File.separator);
            tsb.append(baseConfig.getString(CERT_TRUST_STORE_NAME));
            trustStore = tsb.toString();
            trustStorePwd = baseConfig.getString(CERT_TRUST_STORE_P_W_D);
            trustStoreFormat = baseConfig.getString(CERT_TRUST_STORE_FORMAT);

            StringBuilder ksb = new StringBuilder(basePath);
            ksb.append("keystore");
            ksb.append(File.separator);
            ksb.append(baseConfig.getString(CERT_KEY_STORE_NAME));
            keyStore = ksb.toString();
            keyStorePwd = baseConfig.getString(CERT_KEY_STORE_P_W_D);
            keyStoreFormat = baseConfig.getString(CERT_KEY_STORE_FORMAT);
        }
    }
}