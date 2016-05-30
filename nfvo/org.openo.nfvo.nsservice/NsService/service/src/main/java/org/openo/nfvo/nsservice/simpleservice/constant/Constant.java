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

package org.openo.nfvo.nsservice.simpleservice.constant;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 
* Const parameter for NsSevice module<br/>
* <p>
* </p>
* 
* @author
* @version NFVO 0.5 May 15, 2016
 */
public class Constant {

    private Constant() {
        //Construtor
    }

    public static final String PARAM_MODULE = "NsService";

    public static final String DO_DB = "nsdb";

    public static final String NFVODO = "NFVO";

    public static final int ERROR_CODE = -1;

    public static final int DEFAULT_PAGE_SIZE = 20;

    public static final int DEFAULT_COLLECTION_SIZE = 10;

    public static final String POST = "post";

    public static final String PUT = "put";

    public static final String DELETE = "delete";

    public static final String GET = "get";

    public static final String ADD = "add";

    public static final String ASYNCPOST = "asyncPost";

    public static final String ASYNCGET = "asyncGet";

    public static final String ASYNCPUT = "asyncPut";

    public static final String ASYNCDELETE = "asyncDelete";

    public static final String HANDSHAKE = "handShake";

    public static final int TIME_EXCEPT_VALUE = 0;

    public static final int TIMOUTMINUTE = 1440;

    public static final int HTTP_OK = 200;

    public static final int HTTP_CREATED = 201;

    public static final int HTTP_ACCEPTED = 202;

    public static final int HTTP_NOCONTENT = 204;

    public static final int HTTP_BAD_REQUEST = 400;

    public static final int HTTP_UNAUTHORIZED = 401;

    public static final int HTTP_FORBIDDEN = 403;

    public static final int HTTP_NOTFOUND = 404;

    public static final int HTTP_CONFLICT = 409;

    public static final int HTTP_INNERERROR = 500;

    public static final int INTERNAL_EXCEPTION = 600;

    public static final int TOKEN_HEAD_NULL = 601;

    public static final int TOKEN_USER_NULL = 602;

    public static final int SERVICE_URL_ERROR = 603;

    public static final int ACCESS_OBJ_NULL = 604;

    public static final int CONNECT_NOT_FOUND = 605;

    public static final int VCENTER_PARA_ERROR = 606;

    public static final int TYPE_PARA_ERROR = 607;

    public static final int CONNECT_FAIL = 608;

    public static final String WRAP_ERROR = "error";

    public static final String ACTIVE = "active";

    public static final String INACTIVE = "inactive";

    public static final String SUCCESS = "SUCCESS";

    public static final String FAILE = "FAILE";

    public static final String HTTPS = "https";

    public static final String HTTP = "http";

    public static final String COOKIE = "Cookie";

    public static final String BSPSESSION = "bspsession=";

    public static final String DATA = "data";

    public static final String EMPTY_STRING = "";

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final int MIN_URL_LENGTH = 7;

    public static final int MAX_DO_NAME_LENGTH = 64;

    public static final int MIN_DO_NAME_LENGTH = 1;

    public static final int MAX_URL_LENGTH = 256;

    public static final int MAX_SAMPLE_NUM = 1;

    public static final int MIN_USERNAME_LENGTH = 6;

    public static final int MAX_USERNAME_LENGTH = 64;

    public static final int REST_SUCCESS = 1;

    public static final int REST_PART_SUCCESS = 0;

    public static final int REST_FAIL = -1;

    public static final List<String> DOTYPELIST = Collections.unmodifiableList(Arrays.asList(NFVODO));

    public static final String CONTENT_TYPE = "Content-type";

    public static final String X_AUTH_TOKEN = "X-Auth-Token";

    public static final String APPLICATION = "application/json";

    public static final String ADD_DO_EVENT = "org.openo.nfvo.do.add";

    public static final String DEL_DO_EVENT = "org.openo.nfvo.do.del";

    public static final String STATUS_CHANGE_DO_EVENT = "org.openo.nfvo.do.status";

    public static final String ENCODEING = "utf-8";

    public static final interface AuthenticationMode {

        String ANONYMOUS = "Anonymous";

        String CERTIFICATE = "Certificate";
    }

    public static final String GET_TOKENS_V1 = "{\"grantType\": \"password\", \"userName\": \"%s\",\"value\": \"%s\"}";

    public static final String REST_3RD_CONNECTION = "/rest/plat/smapp/v1/oauth/token";

    public static final String REST_3RD_DISCONNECT = "/rest/plat/smapp/v1/sessions?roarand=%s";

    public static final String GET_SITES = "/rest/v1/resmanage/sites";

    public static final String GET_TENANTSITES = "/rest/v1/resmanage/tenantsites";

    public static final String GET_RESPOOL = "/rest/v1/resmanage/respool";

    public static final String GET_MONITOR = "/rest/do_monitor/v1/monitor";

    public static final String URL_FOR_HANDSHAKE = "/rest/sodriver/v1/sos";

    // notify resource
    public static final String REST_EVENT_ADD = "/rest/soresmanage/v1/resoperate";

    // notify resource
    public static final String REST_EVENT_DELETE = "/rest/soresmanage/v1/resoperate?doId=%s";

    // notify resource
    public static final String REST_EVENT_PUT = "/rest/soresmanage/v1/sodores";

    public static final String GET_TOKENS_DO = "{\"userName\": \"%s\",\"pwd\": \"%s\"}";

    public static final String REST_DO_AUTH = "/rest/sodriver/v1/auth/tokens";
}
