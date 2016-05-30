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
package org.openo.sdno.vpn.wan.servicemodel;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;

public abstract class AbstractSvcModel implements SvcModel {

    @JsonIgnore
    private final Map<String, Object> valuel4PoMap = new HashMap<String, Object>();

    @Override
    public void setValue4Po(String poFieldName, Object val) {
        valuel4PoMap.put(poFieldName, val);
    }

    public Map<String, Object> getValuel4PoMap() {
        return valuel4PoMap;
    }
}
