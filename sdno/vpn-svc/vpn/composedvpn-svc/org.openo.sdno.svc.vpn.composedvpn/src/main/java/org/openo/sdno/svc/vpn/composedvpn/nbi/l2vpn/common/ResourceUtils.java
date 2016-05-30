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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.puer.framework.container.util.SysEnvironment;

public class ResourceUtils {

    private ResourceUtils() {

    }

    /**
     * Read property file.
     * 
     * @since SDNO 0.5
     */
    public static Properties readPropertiesFile(String filename) {
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
