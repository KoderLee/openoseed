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

import org.openo.sdno.remoteservice.exception.ServiceException;
import org.openo.sdno.inventory.sdk.model.NetworkElementMO;

public interface INetworkElementService {

    /**
     * Query AS network element by AS name.
     * 
     * @since SDNO 0.5
     */
    List<NetworkElementMO> queryAsbrNeByAsName(String asName) throws ServiceException;

    /**
     * Get network element by id.
     * 
     * @since SDNO 0.5
     */
    NetworkElementMO getById(String uuid) throws ServiceException;

    /**
     * Get network element by name.
     * 
     * @since SDNO 0.5
     */
    List<NetworkElementMO> getByName(String neName) throws ServiceException;

    /**
     * Get network element by site id.
     * 
     * @since SDNO 0.5
     */
    NetworkElementMO getBySiteId(String siteUuid) throws ServiceException;
}
