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
package org.openo.sdno.svc.vpn.composedvpn.nbi.resource.inf;

import java.util.List;
import java.util.Map;

import org.openo.sdno.remoteservice.exception.ServiceException;
import org.openo.sdno.cbb.sdnwan.util.rest.RestRsp;
import org.openo.sdno.inventory.sdk.model.TerminationPointMO;
import org.openo.sdno.result.Result;
import org.openo.sdno.vpn.wan.servicemodel.tp.Tp;
import org.openo.sdno.vpn.wan.servicemodel.vpn.Vpn;
import com.puer.framework.base.service.IService;

public interface ITerminationPointService extends IService {

    /**
     * Get terminate point map.
     * 
     * @since SDNO 0.5
     */
    Map<String, List<Tp>> getTpAsMap(List<Vpn> vpnList);

    /**
     * Query terminate point.
     * 
     * @since SDNO 0.5
     */
    Result<TerminationPointMO> query(String uuid) throws ServiceException;

    /**
     * Query terminate point by network element id.
     * 
     * @since SDNO 0.5
     */
    RestRsp<List<TerminationPointMO>> queryByNeid(String neUuid) throws ServiceException;
}
