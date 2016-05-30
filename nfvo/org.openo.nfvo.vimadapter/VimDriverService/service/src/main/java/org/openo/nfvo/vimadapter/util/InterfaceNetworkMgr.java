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

package org.openo.nfvo.vimadapter.util;

import java.util.Map;

import net.sf.json.JSONObject;

/**
 * 
* Supply the basic network operation .<br/>
* <p>
* </p>
* 
* @author
* @version NFVO 0.5 May 15, 2016
 */
public interface InterfaceNetworkMgr {
    JSONObject create(JSONObject network, Map<String, String> conInfoMap);

    int remove(JSONObject network, Map<String, String> conInfoMap);

    JSONObject createSubNet(JSONObject network, Map<String, String> conInfoMap);

    int deleteSubNet(String id, Map<String, String> conInfoMap);

}
