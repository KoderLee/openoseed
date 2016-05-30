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
package org.openo.sdno.svc.vpn.l2vpn;

import org.openo.sdno.roa.ROA;
import org.openo.sdno.paraverify.ParaVerifyFilter;
import com.puer.framework.base.service.IRoaModule;

/**
 * Rest module registration class of L2VPN service.<br/>
 * 
 * @author
 * @version SDNO 0.5 2016-3-16
 */
public class L2VpnSvcRoaModule extends IRoaModule {


    @Override
    protected void destroy() {

        /* init() and destroy() methods for spring framework */
    }

    @Override
    protected void init() {
        // Parameter verification is divided into 3 levels:
        // 1. Use cbb-utils's ParaVerifyFilter, registered in L2VpnSvcRoaModule.
        // Mainly solve the problem like non numeric characters injected into
        // the plastic variables leading to error.
        // 2. Use the annotations in the business model and the API in the
        // L2vpnSvc module to manual check in IResource.
        // 3. Business logic manual verification. Manual verification is
        // performed in the IL2vpnBussiValidate.
        ROA.addFilter("/rest", new ParaVerifyFilter("org.openo.sdno.svc.vpn.l2vpn"), "/svc/l2vpn/*", 1);
    }
}
