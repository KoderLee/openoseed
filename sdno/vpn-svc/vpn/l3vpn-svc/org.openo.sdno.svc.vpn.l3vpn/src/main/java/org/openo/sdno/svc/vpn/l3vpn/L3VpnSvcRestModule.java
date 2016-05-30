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
package org.openo.sdno.svc.vpn.l3vpn;

import org.openo.sdno.roa.ROA;
import org.openo.sdno.paraverify.ParaVerifyFilter;
import com.puer.framework.base.service.IRoaModule;

/**
 * Rest module registration class for L3vpn service.<br/>
 * 
 * @author
 * @version SDNO 0.5 2016-3-11
 */
public class L3VpnSvcRestModule extends IRoaModule {

    @Override
    protected void destroy() {

        /* init() and destroy() methods for spring framework */

    }

    @Override
    protected void init() {
        /* init() and destroy() methods for spring framework */
        
        ROA.addFilter("/rest", new ParaVerifyFilter("org.openo.sdno.svc.vpn.l3vpn"), "/svc/l3vpn/*", 1);
        ROA.addFilter("/rest", new ParaVerifyFilter("org.openo.sdno.svc.vpn.l3vpn"), "/svc/vpn/*", 1);
    }

}
