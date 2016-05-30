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
package org.openo.sdno.cbb.sdnwan.util.accessor;

import java.util.List;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import org.openo.sdno.vpn.wan.servicemodel.tp.Tp;

/**
 * Terminate point accessor.
 * 
 * @author
 * @version SDNO 0.5 16-Mar-2016
 */
public final class TpModelAccessor {

    private TpModelAccessor() {
    }

    /**
     * Get ne uuid by terminate point.
     * 
     * @param tps the access point list sent as input
     * @return gets the NeID of the TPs
     * @since SDNO 0.5
     */
    public static String getNeId(List<Tp> tps) {
        if(CollectionUtils.isEmpty(tps)) {
            return null;
        }

        for(Tp tp : tps) {
            if(StringUtils.hasText(tp.getNeId())) {
                return tp.getNeId();
            }
        }

        return null;
    }
}
