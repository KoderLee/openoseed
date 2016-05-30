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
package org.openo.sdno.cbb.sdnwan.util;

import java.util.List;

import com.puer.framework.container.util.UUIDUtils;

/**
 * Utility Class for UUID.<br/>
 * 
 * @author
 * @version SDNO 0.5 17-Mar-2016
 */
public class UuidUtil {

    private static final int UUID_MAX_LIMIT = 36;

    private UuidUtil() {
    }

    /**
     * Wrapper over the puer framework to create a UUID<br/>
     * 
     * @return UUID from the puer framework
     * @since SDNO 0.5
     */
    public static String createUuid() {
        return UUIDUtils.createUuid();
    }

    /**
     * Checks the validity of the input UUID.<br/>
     * 
     * @param uuid to be examined
     * @return true if the UUID is valid
     * @since SDNO 0.5
     */
    public static boolean validate(String uuid) {
        return (uuid != null) && (!uuid.isEmpty()) && (uuid.length() <= UUID_MAX_LIMIT);
    }

    /**
     * Checks the validity of the input UUID list.<br/>
     * 
     * @param uuidList list of UUIDs to be examined
     * @return true if all of the UUIDs are valid
     * @since SDNO 0.5
     */
    public static boolean validate(List<String> uuidList) {
        if(uuidList != null) {
            for(String uuid : uuidList) {
                if(!validate(uuid)) {
                    return false;
                }
            }
        }

        return true;
    }
}
