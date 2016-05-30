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

/**
 * <br/>
 * <p>
 * Utility Class to check the HTTP response status
 * </p>
 * 
 * @author
 * @version SDNO 0.5 16-Mar-2016
 */
public final class HttpStatusUtil {

    private static final int HTTP_SUCCESS_STATUS_CODE = 200;

    private HttpStatusUtil() {
    }

    /**
     * <br/>
     * <p>
     * Checks the HTTP response if is success based on the status input.
     * </p>
     * 
     * @param status Status code returned from the HTTP request
     * @return true if the status value is success
     * @since SDNO 0.5
     */
    public static boolean isSuccess(int status) {
        return (status / HTTP_SUCCESS_STATUS_CODE) == 1;
    }
}
