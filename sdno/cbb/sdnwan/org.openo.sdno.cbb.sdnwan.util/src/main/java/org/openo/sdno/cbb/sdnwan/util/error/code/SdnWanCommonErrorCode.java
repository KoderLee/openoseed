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
package org.openo.sdno.cbb.sdnwan.util.error.code;

import org.openo.sdno.cbb.sdnwan.util.error.ErrorCodeUtils;

/**
 * COMMON ERROR CODES for SDNWAN.<br/>
 * 
 * @author
 * @version SDNO 0.5 18-Mar-2016
 */
public class SdnWanCommonErrorCode {

    public static final String APP = "sdnwancommon";

    private static final String SRC_CONTAINER = "container";

    private static final String SRC_ENUM = "enum";

    private static final String SRC_FIELD = "field";

    private static final String SRC_STRING = "string";

    private static final String SRC_IP = "ip";

    private static final String SRC_MASK = "mask";

    /**
     * the value of {0}: {1} is out of range.
     */
    public static final String CHECKER_ENUM_OUT_OF_RANGE = ErrorCodeUtils.getErrorCode(APP, SRC_ENUM, "out_of_range");

    /**
     * The field {0} value should not be null.
     */
    public static final String CHECKER_FILED_IS_NULL = ErrorCodeUtils.getErrorCode(APP, SRC_FIELD, "field_is_null");

    /**
     * The value of {0} : {1}, is not a valid ip address without mask.
     */
    public static final String CHECKER_IP_INVALID = ErrorCodeUtils.getErrorCode(APP, SRC_IP, "ip_invalid");

    /**
     * The value of {0} : {1}, is not a valid ip address with mask.
     */
    public static final String CHECKER_CIDR_INVALID = ErrorCodeUtils.getErrorCode(APP, SRC_IP, "cidr_invalid");

    /**
     * Mask value:{0} is out of rang:0~32.
     */
    public static final String CHECKER_MASK_INVALID = ErrorCodeUtils.getErrorCode(APP, SRC_MASK, "mask_invalid");

    /**
     * the {0}'s size: {1} is out of range.
     */
    public static final String CHECKER_SIZE_OUT_OF_RANGE = ErrorCodeUtils.getErrorCode(APP, SRC_CONTAINER,
            "size_out_of_range");

    /**
     * {0} can not be blank.
     */
    public static final String CHECKER_STRING_IS_BLANK = ErrorCodeUtils
            .getErrorCode(APP, SRC_STRING, "string_is_blank");

    /**
     * {0} can not be empty.
     */
    public static final String CHECKER_STRING_IS_EMPTY = ErrorCodeUtils
            .getErrorCode(APP, SRC_STRING, "string_is_empty");

    /**
     * the value of {0}:{1}, it's length is out of range.
     */
    public static final String CHECKER_STRING_OVER_LENGTH = ErrorCodeUtils.getErrorCode(APP, SRC_STRING,
            "string_over_length");

    /**
     * The field {2} in {1} is unsupported for [0].
     */
    public static final String CHECKER_UNSUPPORT_FIELD_TYPE = ErrorCodeUtils.getErrorCode(APP, "scopechecker",
            "field_type_unsupport");

    /**
     * The VLAN scope {0} is not valid.
     */
    public static final String CHECKER_VLAN_SCOPE_INVALID = ErrorCodeUtils.getErrorCode(APP, "vlanscope",
            "vlanscope_invalid");

    /**
     * Resource list required to be locked is null or empty. 196001.
     */
    public static final String EMPTY_LOCK_RESOURCE = ErrorCodeUtils.getErrorCode(APP, "common", "empty_lock_resource");

    /**
     * Resource list required to be unlocked is null or empty. 196002
     */
    public static final String EMPTY_UNLOCK_RESOURCE = ErrorCodeUtils.getErrorCode(APP, "common",
            "empty_unlock_resource");

    /**
     * Lock resource failed. 196003
     */
    public static final String LOCK_RESOURCE_FAILED = ErrorCodeUtils
            .getErrorCode(APP, "common", "lock_resource_failed");

    /**
     * Lock resource failed in frame. 196004
     */
    public static final String LOCK_RESOURCE_INNER_FAILED = ErrorCodeUtils.getErrorCode(APP, "common",
            "lock_resource_inner_failed");

    public static final String NORMALIZE_REFLECT_FAILED = ErrorCodeUtils.getErrorCode(APP, "common",
            "normalize_reflect_failed");

    public static final int RESOURCE_NOT_EXIST = 10010013;

    /**
     * Unlock resource failed in frame.196005
     */
    public static final String UNLOCK_RESOURCE_INNER_FAILED = ErrorCodeUtils.getErrorCode(APP, "common",
            "unlock_resource_inner_failed");

    /**
     * UUID is invalid 196006.
     */
    public static final String UUID_INVALID = ErrorCodeUtils.getErrorCode(APP, "common", "uuid_invalid");

    /**
     * {0} is not a A type IP address.
     */
    public static final String CHECK_IP_NOT_A_CLASS = ErrorCodeUtils.getErrorCode(APP, SRC_IP, "ip_not_a_class");

    /**
     * {0} is not a B type IP address.
     */
    public static final String CHECK_IP_NOT_B_CLASS = ErrorCodeUtils.getErrorCode(APP, SRC_IP, "ip_not_b_class");

    /**
     * {0} is not a C type IP address.
     */
    public static final String CHECK_IP_NOT_C_CLASS = ErrorCodeUtils.getErrorCode(APP, SRC_IP, "ip_not_c_class");

    /**
     * {0} is not a D type IP address.
     */
    public static final String CHECK_IP_NOT_D_CLASS = ErrorCodeUtils.getErrorCode(APP, SRC_IP, "ip_not_d_class");

    /**
     * {0} is not a E type IP address.
     */
    public static final String CHECK_IP_NOT_E_CLASS = ErrorCodeUtils.getErrorCode(APP, SRC_IP, "ip_not_e_class");

    /**
     * {0} is not a ABC type IP address.
     */
    public static final String CHECK_IP_NOT_ABC_CLASS = ErrorCodeUtils.getErrorCode(APP, SRC_IP, "ip_not_abc_class");

    private SdnWanCommonErrorCode() {
    }
}
