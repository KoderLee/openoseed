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

import org.openo.sdno.vpn.wan.common.CommonName;

/**
 * Utility Class for Enumeration.<br/>
 * 
 * @author
 * @version SDNO 0.5 16-Mar-2016
 */
public class EnumUtil {

    private EnumUtil() {
    }

    /**
     * <br/>
     * <p>
     * Returns the enum constant of the specified enum type with the specified name.<br/>
     * The name must match exactly an identifier used to declare an enum constant in this type.
     * </p>
     * 
     * @param clazz the Class object to be checked
     * @param commonName The String to compare the Class against
     * @return Enum constant of the specified Enum type
     * @since SDNO 0.5
     */
    public static <T extends Enum<T>> T valueOf(Class<T> clazz, String commonName) {
        if(commonName == null) {
            return null;
        }

        if(CommonName.class.isAssignableFrom(clazz)) {
            return valueOf(clazz.getEnumConstants(), commonName);
        } else {
            return Enum.valueOf(clazz, commonName);
        }
    }

    private static <T extends Enum<T>> T valueOf(T[] ts, String commonName) {
        if((null == ts) || (ts.length == 0)) {
            return null;
        }

        for(T t : ts) {
            if(commonName.equals(((CommonName)t).getCommonName())) {
                return t;
            }
        }

        return null;
    }
}
