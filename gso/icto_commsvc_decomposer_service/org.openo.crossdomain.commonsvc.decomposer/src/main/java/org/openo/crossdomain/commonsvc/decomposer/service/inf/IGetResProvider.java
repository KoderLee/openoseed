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
package org.openo.crossdomain.commonsvc.decomposer.service.inf;

import org.openo.crossdomain.commonsvc.decomposer.service.impl.ServiceOperateServiceImpl;

/**
 * The interface for provide resource data
 * @since   crossdomain 0.5
 */
public interface IGetResProvider {

    public final OssLog logger = OssLogFactory.getLogger(ServiceOperateServiceImpl.class);

    /**
     * Get resource data.<br>
     *
     * @throws throw ServiceException if get data failed.
     *             example:
     *             "resources" : [{
     *             "vCPE_A" : {
     *             "id" : "ABCDGEF1",
     *             "type" : "ICTO::NS::VNFTemplateNode",
     *             "result" : "xx",
     *             "result_reason" : "xx",
     *             "activestatus" : "activestatus",
     *             "version" : "version"
     *             },
     *             "vCPE_Z" : {
     *             "id" : "ABCDGEF2",
     *             "type" : "ICTO::NS::VNFTemplateNode",
     *             "result" : "xx",
     *             "result_reason" : "xx",
     *             "activestatus" : "activestatus",
     *             "version" : "version"
     *             }
     *             }
     *             ]
     * @since   crossdomain 0.5
     */
    JSONObject getResInfo() throws ServiceException;
}
