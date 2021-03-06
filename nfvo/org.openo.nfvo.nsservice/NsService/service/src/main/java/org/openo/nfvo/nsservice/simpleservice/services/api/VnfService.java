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

package org.openo.nfvo.nsservice.simpleservice.services.api;

import org.openo.baseservice.roa.common.HttpContext;

/**
 * 
* The abstract class of vnf service, the operation of vnf<br/>
* <p>
* </p>
* 
* @author
* @version NFVO 0.5 May 15, 2016
 */
public interface VnfService {

    String doCreateVnf(HttpContext context);

    String doDelVnf(String resourceId, HttpContext context);

    String doPutWithoutIdVnf(HttpContext context);

    String doPutVnf(String resourceId, HttpContext context);

}
