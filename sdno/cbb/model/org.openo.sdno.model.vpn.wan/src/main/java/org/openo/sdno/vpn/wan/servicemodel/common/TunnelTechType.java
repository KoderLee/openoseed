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
package org.openo.sdno.vpn.wan.servicemodel.common;

import org.openo.sdno.vpn.wan.common.CommonName;

public enum TunnelTechType implements CommonName {
    NOP("nop"), CR_LSP("cr-lsp"), LDP("ldp"), GRE("gre"), SR_TE("sr-te"), SEGMENT_ROUTING("segment-routing"), RSVP_TE(
            "rsvp-te"), AUTO_SELECT("auto-select");

    private String commonName;

    TunnelTechType(String commonName) {
        this.commonName = commonName;
    }

    @Override
    public String getCommonName() {
        return commonName;
    }
}
