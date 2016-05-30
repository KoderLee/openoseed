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
package org.openo.sdno.svc.vpn.l2vpn.common.constant;

/**
 * Define normal L2VPN error code.<br/>
 * 
 * @author
 * @version SDNO 0.5 2016-3-17
 */
public class ErrorCode {

    public static final int ERROR_HTTP_STATUS_OK_TETURN = 200;

    public static final int ERROR_HTTP_STATUS_BAD_REQUEST = 400;

    public static final int ERROR_HTTP_STATUS_INTERNAL_ERROR = 500;

    public static final int RESULT_SUCCESS = 0;

    public static final int UUID_MAX_LENGTH = 36;

    public static final int MAX_INPUT = 255;

    public static final int MIN_INPUT = 10;

    public static final int ERRORCODE_L2VPNSVC_MIN = 10250001;

    public static final int ERRORCODE_L2VPNSVC_MAX = 10259999;

    public static final int ERROR_OTHER_HANDLE_FAILED = 10250001;

    public static final int ERROR_ADAPTORLAYER_INNERERROR_FAILED = 10250002;

    public static final int ERROR_INVOKE_ADAPTORLAYER_FAILED = 10250003;

    public static final int ERROR_TRANSLATE_DATA_FAILED = 10250004;

    public static final int ERROR_INVALID_PARAMETER_FAILED = 10250005;

    public static final int ERROR_DELSERVICE_ACTIVE_FAILED = 10250006;

    public static final int ERROR_CREATE_ISEXIST_FAILED = 10250007;

    public static final int ERROR_UNSUPPORTED_FAILED = 10250008;

    private ErrorCode() {
        /*
         * Default constructor. Make it private. Utility class constructor need
         * to be private
         */
    }

}
