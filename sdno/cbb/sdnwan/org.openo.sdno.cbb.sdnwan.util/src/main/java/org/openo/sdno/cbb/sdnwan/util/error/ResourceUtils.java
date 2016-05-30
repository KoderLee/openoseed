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
package org.openo.sdno.cbb.sdnwan.util.error;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.openo.sdno.deploy.util.DefaultEnvUtil;
import com.puer.framework.container.util.SysEnvironment;

/**
 * Resource util.<br/>
 * 
 * @author
 * @version SDNO 0.5 17-Mar-2016
 */
public class ResourceUtils {


    private ResourceUtils() {
    }

    /**
     * Get the application's absolute path.<br/>
     * 
     * @return Application path
     * @since SDNO 0.5
     */
    public static String getAppPath() {
        final String root =
                SysEnvironment.getAppRoot() + File.separator + "module" + File.separator + "org.openo.sdno."
                        + DefaultEnvUtil.getAppName();

        return root;
    }

    /**
     * Get the Properties details read from the input .properties file.<br/>
     * 
     * @param filename input .properties file
     * @return Properties details
     * @since SDNO 0.5
     */
    public static Properties readPropertiesFile(final String filename) {
        final Properties properties = new Properties();
        InputStream inputStream = null;

        try {
            inputStream = new FileInputStream(filename);
            properties.load(inputStream);
        } catch(final IOException e) {
        } finally {
            if(inputStream != null) {
                try {
                    inputStream.close();
                } catch(final IOException e) {
                }
            }
        }

        return properties;
    }
}
