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

import org.apache.commons.lang3.StringUtils;

/**
 * Error code util.<br/>
 * 
 * @author
 * @version SDNO 0.5 17-Mar-2016
 */
public class ErrorCodeUtils {

    private static final String POINT = ".";

    private ErrorCodeUtils() {
    }

    /**
     * Returns a unique error code for the given appName, source and error by joining them.<br/>
     * 
     * @param appName given appName to be appended
     * @param source source details to be appended
     * @param error error to be appended
     * @return error code
     * @since SDNO 0.5
     */
    public static final String getErrorCode(final String appName, final String source, final String error) {
        return StringUtils.join(appName, POINT, source, POINT, error);
    }
}
