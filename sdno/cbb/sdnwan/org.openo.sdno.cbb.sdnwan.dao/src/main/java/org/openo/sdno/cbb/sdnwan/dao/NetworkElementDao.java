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
package org.openo.sdno.cbb.sdnwan.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import org.openo.sdno.remoteservice.exception.ServiceException;
import org.openo.sdno.inventory.sdk.inf.IControllerService;
import org.openo.sdno.inventory.sdk.inf.IInvDAO;
import org.openo.sdno.inventory.sdk.model.ControllerMO;
import org.openo.sdno.inventory.sdk.model.NetworkElementMO;
import org.openo.sdno.inventory.sdk.model.QueryParams;
import org.openo.sdno.result.Result;
import com.puer.framework.base.util.JsonUtil;

@Repository("networkElementDao")
public class NetworkElementDao {


    @Autowired
    private IInvDAO<NetworkElementMO> invDao;

    @Autowired
    private IControllerService controllerService;

    public Map<String, String> queryNeUuidToIpMap(Set<String> neuuidList) {
        String filter = null;
        final Map<String, Object> ff = new HashMap<String, Object>();
        if(!neuuidList.isEmpty()) {
            ff.put("uuid", neuuidList);
            filter = JsonUtil.toJson(ff);
        } else {
            filter = "";
        }

        final Map<String, String> neUuidToIpMap = new HashMap<String, String>();
        final List<NetworkElementMO> neList = getNetworkElement(filter);
        for(final NetworkElementMO tempNe : neList) {
            neUuidToIpMap.put(tempNe.getUuid(), tempNe.getIp());
        }

        return neUuidToIpMap;
    }

    /**
     * Query uuid.
     * 
     * @since SDNO 0.5
     */
    public Map<String, String> queryNeUuidToNameMap(final Set<String> neuuidList) {
        String filter = null;
        final Map<String, Object> filterMap = new HashMap<String, Object>();
        if(neuuidList.isEmpty()) {
            filter = "";
        } else {
            filterMap.put("uuid", neuuidList);
            filter = JsonUtil.toJson(filterMap);
        }

        final Map<String, String> neUuidToNameMap = new HashMap<String, String>();
        final List<NetworkElementMO> neList = getNetworkElement(filter);
        for(final NetworkElementMO tempNe : neList) {
            neUuidToNameMap.put(tempNe.getUuid(), tempNe.getName());
        }

        return neUuidToNameMap;
    }

    /**
     * Get network element.
     * 
     * @since SDNO 0.5
     */
    public List<NetworkElementMO> getNetworkElement(String filter) {
        try {
            final Result<List<NetworkElementMO>> result =
                    invDao.query(NetworkElementMO.class, new QueryParams(filter, "", "", ""));

            if(result.isFailed()) {
                return new ArrayList<NetworkElementMO>();
            }
            return result.getResultObj();
        } catch(ServiceException e) {
            return new ArrayList<NetworkElementMO>();
        }
    }

    /**
     * Get network element by uuid.
     * 
     * @since SDNO 0.5
     */
    public List<NetworkElementMO> getNetworkElementById(String uuid) {
        try {
            final Result<NetworkElementMO> result = invDao.query(uuid, NetworkElementMO.class);

            if(result.isFailed()) {
                return new ArrayList<NetworkElementMO>();
            }

            final List<NetworkElementMO> neList = new ArrayList<NetworkElementMO>();
            neList.add(result.getResultObj());
            return neList;
        } catch(final ServiceException e) {
            return new ArrayList<NetworkElementMO>();
        }
    }

    /**
     * Get controller by ne uuid.
     * 
     * @since SDNO 0.5
     */
    public ControllerMO getControllerFromNe(String neuuid) {
        if(!StringUtils.hasText(neuuid)) {
            return null;
        }
        try {
            final Result<ControllerMO> result = controllerService.getControllerForNE(neuuid);

            if(result.isFailed()) {
                return null;
            }

            return result.getResultObj();
        } catch(final ServiceException e) {
            return null;
        }
    }

    /**
     * Get controller by ne uuid.
     * 
     * @since SDNO 0.5
     */
    public String getCtrlUuidFromNe(String neuuid) {
        ControllerMO controller = getControllerFromNe(neuuid);
        return controller == null ? null : controller.getUuid();
    }
}
