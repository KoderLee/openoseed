/*******************************************************************************
 * Copyright (c) 2016, Huawei Technologies Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

package org.openo.nfvo.vimadapter.util.constant;

/**
 * 
* The url constant<br/>
* <p>
* </p>
* 
* @author
* @version NFVO 0.5 May 15, 2016
 */
public static class UrlConstant {

    private UrlConstant(){
        //Constructor
    }

    public static final String GET_NETWORK_8ID_NEUTRON = "%s/v2.0/networks/%s";

    public static final String GET_NETWORK_BY_VLAN_PHY_NEUTRON =
            "%s/v2.0/networks?provider:segmentation_id=%s&provider:physical_network=%s";

    public static final String GET_VNETWORK_BY_VLAN_PHY_NEUTRON =
            "%s/v1/vnetworks?provider:segmentation_id=%s&provider:physical_network=%s";

    public static final String GET_NETWORK_BY_TYPE_PHY_NEUTRON =
            "%s/v2.0/networks?provider:network_type=%s&provider:physical_network=%s";

    public static final String GET_VNETWORK_BY_TYPE_PHY_NEUTRON =
            "%s/v1/vnetworks?provider:network_type=%s&provider:physical_network=%s";

    public static final String GET_SUBNET_NEUTRON = "%s/v2.0/subnets";

    public static final String GET_SUBNET_8ID_NEUTRON = "%s/v2.0/subnets/%s";

    public static final String GET_PHYNET_NEUTRON = "%s/v2.0/physicalnetworks";

    public static final String POST_AUTH_TOKENS_V3 = "%s/v3/auth/tokens?nocatalog";

    public static final String POST_AUTH_TOKENS_V2 = "%s/v2.0/tokens";

    public static final String GET_SERVICES_V3 = "%s/v3/services";

    public static final String GET_RP = "%s/v2/%s/os-hypervisors/statistics";

    public static final String GET_VM = "%s/v2/%s/servers/detail";

    public static final String GET_NETWORK = "%s/v2.0/networks";

    public static final String GET_HOSTS = "%s/cps/v1/hosts";

    public static final String GET_PROVIDERNET = "%s/cps/v1/providers";

    public static final String GET_HOST = "%s/v2/%s/os-hosts/%s";

    public static final String GET_VENDOR = "%s-admin/v2.0/tenants";

    public static final String GET_PORT = "%s/v2.0/ports";

    public static final String GET_VM_CPU_USAGE =
            "%s/v2/meters/cpu_util.inband?q.field=start&q.value=%s&q.field=end&q.value=%s";

    public static final String GET_SINGLE_VM_CPU_USAGE =
            "%s/v2/meters/cpu_util.inband?q.field=start&q.value=%s&q.field=end&q.value=%s&q.field=resource_id&q.value=";

    public static final String GET_VM_OUT_CPU_USAGE =
            "%s/v2/meters/cpu_util?q.field=start&q.value=%s&q.field=end&q.value=%s";

    public static final String GET_SINGLE_VM_OUT_CPU_USAGE =
            "%s/v2/meters/cpu_util?q.field=start&q.value=%s&q.field=end&q.value=%s&q.field=resource_id&q.value=";

    public static final String GET_VM_MEM_USAGE =
            "%s/v2/meters/mem_util?q.field=start&q.value=%s&q.field=end&q.value=%s";

    public static final String GET_SINGLE_VM_MEM_USAGE =
            "%s/v2/meters/mem_util?q.field=start&q.value=%s&q.field=end&q.value=%s&q.field=resource_id&q.value=";

    public static final String GET_VM_DISK_USAGE =
            "%s/v2/meters/disk_util.inband?q.field=start&q.value=%s&q.field=end&q.value=%s";

    public static final String GET_SINGLE_VM_DISK_USAGE =
            "%s/v2/meters/disk_util.inband?q.field=start&q.value=%s&q.field=end&q.value=%s&q.field=resource_id&q.value=";

    public static final String GET_HOST_CPU_USAGE =
            "%s/v2/meters/host.cpu.util?q.field=start&q.value=%s&q.field=end&q.value=%s";

    public static final String GET_SINGLE_HOST_CPU_USAGE =
            "%s/v2/meters/host.cpu.util?q.field=start&q.value=%s&q.field=end&q.value=%s&q.field=resource_id&q.value=";

    public static final String GET_HOST_MEM_USAGE =
            "%s/v2/meters/host.memory.util?q.field=start&q.value=%s&q.field=end&q.value=%s";

    public static final String GET_SINGLE_HOST_MEM_USAGE =
            "%s/v2/meters/host.memory.util?q.field=start&q.value=%s&q.field=end&q.value=%s&q.field=resource_id&q.value=";

    public static final String GET_HOST_DISK_USAGE =
            "%s/v2/meters/host.disk.util?q.field=start&q.value=%s&q.field=end&q.value=%s";

    public static final String GET_SINGLE_HOST_DISK_USAGE =
            "%s/v2/meters/host.disk.util?q.field=start&q.value=%s&q.field=end&q.value=%s&q.field=resource_id&q.value=";

    public static final String POST_VNETWORK = "%s/v2.0/networks";

    public static final String POST_VSUBNETWORK = "%s/v2.0/subnets";

    public static final String DEL_VNETWORK = "%s/v2.0/networks/%s";

    public static final String DEL_VSUBNETWORK = "%s/v2.0/subnets/%s";

    public static final String RESMGR_VNETWORK = "/rest/v1/resmanage/virtualnetworks";

    public static final String GET_VNETWORK_FORM_FS_VLAN =
            "%s/v2.0/networks?provider:segmentation_id=%s&provider:physical_network=%s";

    public static final String RESMGR_VSUBNETWORK = "/rest/v1/resmanage/subnetworks";

    public static final String DEL_VIM_RES = "/rest/v1/resmanage/main";

    public static final String GET_VNETWORK_FORM_FS_FLAT =
            "%s/v2.0/networks?provider:network_type=flat&provider:physical_network=%s";

    public static final String REST_SERVICE_CHAINS = "/rest/v2.0/service_chains";

    public static final String REST_SERVICE_CHAIN = "/rest/v2.0/service_chains/%s";

    public static final String REST_EVENT_ADD = "/rest/v1/resmanage/main/resmgr/vims?vimId=%s";

    public static final String REST_EVENT_UPDATE = "/rest/v1/resmanage/main/updateres?vimId=%s";

    public static final String REST_EVENT_STATUS = "/rest/v1/resmanage/modresstatus";

    public static final String REST_UPDATE_INSTANCE = "%s/v2.0/service_function_instances/%s";

    public static final String REST_GET_INSTANCE = "%s/v2.0/service_function_instances/%s";

    public static final String REST_GET_ALL_INSTANCE = "%s/v2.0/service_function_instances";

    public static final String REST_AND_INSTANCE = "%s/v2.0/service_function_instances";

    public static final String REST_DEL_INSTANCE = "%s/v2.0/service_function_instances/%s";

    public static final String REST_PUT_GROUP = "%s/v2.0/service_function_groups/%s";

    public static final String REST_GET_GROUP = "%s/v2.0/service_function_groups/%s";

    public static final String REST_GET_ALL_GROUP = "%s/v2.0/service_function_groups";

    public static final String CREATE_GROUP = "%s/v2.0/service_function_groups";

    public static final String DEL_GROUP = "%s/v2.0/service_function_groups/%s";

    public static final String REST_GET_PAIR = "%s/v2.0/service_chain_pairs/%s";

    public static final String REST_GET_ALL_PAIR = "%s/v2.0/service_chain_pairs";

    public static final String REST_AND_PAIR = "%s/v2.0/service_chain_pairs";

    public static final String REST_DEL_PAIR = "%s/v2.0/service_chain_pairs/%s";

    public static final String REST_ADD_CLASSIFIER = "%s/v2.0/service_traffic_classifiers";

    public static final String REST_DEL_CLASSIFIER = "%s/v2.0/service_traffic_classifiers/%s";

    public static final String REST_PUT_CLASSIFIER = "%s/v2.0/service_traffic_classifiers/%s";

    public static final String REST_GET_CLASSIFIER = "%s/v2.0/service_traffic_classifiers/%s";

    public static final String REST_GET_ALL_CLASSIFIER = "%s/v2.0/service_traffic_classifiers";

    public static final String REST_PUT_CHAIN = "%s/v2.0/service_chains/%s";

    public static final String REST_GET_CHAIN = "%s/v2.0/service_chains/%s";

    public static final String REST_GET_CHAINS = "%s/v2.0/service_chains";

    public static final String GET_RACKS = "%s/v1/racks?nfvoid=%s&vimid=%s";

    public static final String GET_RACK = "%s/v1/racks/%s";

    public static final String GET_SERVERS = "%s/v1/hardware-servers?nfvoid=%s&vimid=%s";

    public static final String GET_SERVER = "%s/v1/hardware-servers/%s";

    public static final String GET_HRVMS = "%s/v1/vms?nfvoid=%s&status=%s&flavor=%s&vimid=%s";

    public static final String GET_HRVM = "%s/v1/vms/%s";

    public static final String GET_VIMS = "%s/v1/vims?vimid=%s&nfvoid=%s";

    public static final String GET_CHASSISES = "%s/v1/chassises?nfvoid=%s&vimid=%s";

    public static final String GET_CHASSIS = "%s/v1/chassises/%s";

    public static final String GET_IMAGES = "%s/v1/images?nfvoid=%s&vimid=%s";

    public static final String GET_IMAGE = "%s/v1/images/%s";

    public static final String GET_STORAGES = "%s/v1/storages?nfvoid=%s&vimid=%s";

    public static final String GET_STORAGE = "%s/v1/storages/%s";

    public static final String DEL_IMAGE = "%s/v1/images/%s";

    public static final String POST_IMAGE = "%s/v1/images";

    public static final String PUT_IMAGE = "%s/v1/images/%s/file";

    public static final String POST_JOBS = "%s/v1/pm/jobs";

    public static final String DEL_JOBS = "%s/v1/pm/jobs/%s";

    public static final String GET_JOBS = "%s/v1/pm/jobs?nfvoid=%s&vimid=%s";

    public static final String NOTIFIC_JOBS = "/rest/do_monitor/v1/monitor";

    public static final String GET_VOLUMES = "%s/v1/volumes?nfvoid=%s&zone=%s&volumeType=%s&vimid=%s";

    public static final String GET_VOLUME = "%s/v1/volumes/%s";

    public static final String POST_CMCCNETWORK = "%s/v1/networks";

    public static final String GET_CMCCNETWORK = "%s/v1/networks/%s";

    public static final String PUT_CMCCNETWORK = "%s/v1/networks/%s";

    public static final String GET_CMCCNETWORKS = "%s/v1/networks?nfvoid=%s&vimid=%s";

    public static final String DEL_CMCCNETWORK = "%s/v1/networks/%s";

    public static final String GET_CMCCSUBNETWORK = "%s/v1/subnets/%s";

    public static final String GET_CMCCSUBNETWORKS = "%s/v1/subnets?nfvoid=%s&vimid=%s";

    public static final String POST_CMCCSUBNETWORK = "%s/v1/subnets";

    public static final String DEL_CMCCSUBNETWORK = "%s/v1/subnets/%s";
}
