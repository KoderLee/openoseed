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

package org.openo.nfvo.vimadapter.util.constant;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 
* Constant class<br/>
* <p>
* </p>
* 
* @author
* @version NFVO 0.5 May 15, 2016
 */
public interface Constant {
    String VIM_DB = "vimdb";
    String PARAM_MODULE = "VIMDriverService";
    String VCLOUD = "vcloud";
    String VCENTER = "vcenter";
    String OPENSTACK = "openstack";

    String POST = "POST";
    String PUT = "PUT";
    String DEL = "DEL";
    String GET = "GET";
    int ERROR_CODE = -1;
    String DELETE = "DELETE";

    String ASYNCPOST = "asyncPost";
    String ASYNCGET = "asyncGet";
    String ASYNCPUT = "asyncPut";
    String ASYNCDELETE = "asyncDelete";

    String HANDSHAKE = "handShake";
    String FIRST_HANDSHAKE = "first_handShake";

    String ENCODEING = "utf-8";
    String CONTENT_TYPE = "Content-type";
    String APPLICATION = "application/json";
    String HEADER_SUBJECT_TOKEN = "X-Subject-Token";
    String HEADER_AUTH_TOKEN = "X-Auth-Token";

    int DEFAULT_COLLECTION_SIZE = 10;

    int MIN_VLANID = 0;
    int MAX_VLANID = 4094;

    int CREATENETTIMES = 10;
    int QUERYNETINTERVAL = 2000;
    int CPUMHZ = 2600;
    int STARTVOLUME = 0;
    int WAITFORTASK_RATE = 5;
    int OPENSTACK_NOVAURL_MIN_LENTH = 3;
    int TIME_EXCEPT_VALUE = 0;
    int WAITFORTASK_TIMEOUT = 1000 * 60 * 60 * 2;

    int VCLOUD_NOT_NEED_SUBNET_INDEX = -1;
    int VCLOUD_DEFAULT_SUBNET_INDEX = 100;
    int VCLOUD_SUBNET_ROUND = 256;

    int REST_SUCCESS = 1;
    int REST_PART_SUCCESS = 0;
    int REST_FAIL = -1;

    String TIMOUTMINUTE = "1440";

    int RELOGIN_BEFORE_TIME = 60;

    long TIME_INTERNAL = 60000;

    int HTTP_OK = 200;
    int HTTP_CREATED = 201;
    int HTTP_ACCEPTED = 202;
    int HTTP_NOCONTENT = 204;
    int HTTP_BAD_REQUEST = 400;
    int HTTP_UNAUTHORIZED = 401;
    int HTTP_NOTFOUND = 404;
    int HTTP_CONFLICT = 409;
    int HTTP_INNERERROR = 500;
    int INTERNAL_EXCEPTION = 600;
    int TOKEN_HEAD_NULL = 601;
    int TOKEN_USER_NULL = 602;
    int SERVICE_URL_ERROR = 603;
    int ACCESS_OBJ_NULL = 604;
    int CONNECT_NOT_FOUND = 605;
    int VCENTER_PARA_ERROR = 606;
    int TYPE_PARA_ERROR = 607;
    int CONNECT_FAIL = 608;
    int CONNECT_NULL = 609;
    int CONNECT_TMOUT = 700;

    double USAGE_RATE = 100.0;

    int DEFLAUT_SECURE_PORT = 443;

    String WRAP_ERROR = "error";

    String ACTIVE = "active";
    String INACTIVE = "inactive";

    String DELETE_FAIL_NETWORK = "delete";

    String MATAIN_CONFLICT_NETWORK = "matain";
    String WRAP_USER = "user";
    String WRAP_TOKEN = "token";
    String WRAP_ACCESS = "access";
    String ISSUED_AT = "issued_at";

    String WRAP_TENANT = "tenant";
    String WRAP_TENANTS = "tenants";

    String WRAP_METADATA = "metadata";
    String WRAP_ENDPOINTS = "endpoints";

    String WRAP_SERVERS = "servers";
    String WRAP_SERVER = "server";
    String WRAP_SERVICES = "services";

    String WRAP_PROJECT = "project";

    String WRAP_HOST = "host";
    String WRAP_HOSTS = "hosts";

    String WRAP_NETWORKS = "networks";
    String WRAP_NETWORK = "network";

    String WRAP_HYPERVISOR_STATS = "hypervisor_statistics";

    String WRAP_PROVIDER = "provider";

    String WRAP_VOLUME_IMAGE_METADATA = "volume_image_metadata";
    String WRAP_RESOURCE = "resource";
    String WRAP_ATTACH = "volumeAttachment";

    String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    List<String> VIMTYPELIST = Collections.unmodifiableList(Arrays.asList(
            OPENSTACK, VCLOUD));

    List<String> AUTHLIST = Collections.unmodifiableList(Arrays.asList(
            AuthenticationMode.ANONYMOUS, AuthenticationMode.CERTIFICATE));

    List<String> TENANTS_NAME_LIST = Collections.unmodifiableList(Arrays
            .asList("elb", "router", "service"));

    List<String> VCENTER_USAGE = Collections.unmodifiableList(Arrays
            .asList(new String[] { "cpu.usage.average.rate",
                    "mem.usage.average.absolute", "disk.usage.average.rate" }));

    String ID = "id";
    String FIELD_NULL = "null";

    String SERVICE_CATALOG = "serviceCatalog";
    String REGION = "region";
    String PUBLICURL = "publicURL";
    String SERVICENAME = "name";

    String VCLOUD_VDC_TAG = "urn:vcloud:vdc:";
    String VCLOUD_ORG_TAG = "urn:vcloud:org:";
    String VCLOUD_NETWORK_TAG = "urn:vcloud:network:";

    int VCLOUD_CREATE_SUCC = 1;

    int MIN_URL_LENGTH = 7;
    int MAX_VIM_NAME_LENGTH = 64;
    int MIN_VIM_NAME_LENGTH = 1;
    int MAX_URL_LENGTH = 256;
    int MAX_SAMPLE_NUM = 1;
    int INTERVAL_SECOND = 20;

    String RESOURCE_PATH = "";

    String DEFAULT_SERVICEINSTANCE_VALUE = "ServiceInstance";

    String ADD_VIM_EVENT = "org.openo.nfvo.vim.add";
    String UPDATE_VIM_EVENT = "org.openo.nfvo.vim.update";
    String STATUS_CHANGE_VIM_EVENT = "org.openo.nfvo.vim.status";

    public interface ServiceName {

        String GLANCE = "glance";

        String NEUTRON = "neutron";

        String NOVA = "nova";

        String KEYSTONE = "keystone";

        String CEILOMETER = "ceilometer";

        String CINDER = "cinder";

        String CPS = "cps";
    }

    public interface VmShare {
        String CPU = "cpu";

        String DISK = "disk";

        String MEM = "mem";
    }

    public interface AuthenticationMode {

        String ANONYMOUS = "Anonymous";

        String CERTIFICATE = "Certificate";
    }

    public interface VmStatus {
        String ERROR = "ERROR";

        String INITIALIZED = "INITIALIZED";

        String ACTIVE = "ACTIVE";

        String UNKNOWN = "UNKNOWN";

        String SHUTOFF = "SHUTOFF";

        String BUILD = "BUILD";
    }
}
