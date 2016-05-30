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
package org.openo.sdno.svc.vpn.l3vpn.services.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.openo.sdno.remoteservice.exception.ServiceException;
import org.openo.sdno.cbb.sdnwan.dao.TpDao;
import org.openo.sdno.cbb.sdnwan.dao.VpnDao;
import org.openo.sdno.svc.vpn.l3vpn.services.inf.VpnTpService;
import org.openo.sdno.vpn.wan.servicemodel.tp.Tp;
import com.puer.framework.container.util.UUIDUtils;

public class DefaultVpnTpService implements VpnTpService {

    @Autowired
    private TpDao tpDao;

    @Autowired
    private VpnDao vpnDao;

    /**
     * Create terminate point.
     * 
     * @since SDNO 0.5
     */
    @Override
    public Tp createTp(Tp tp) throws ServiceException {
        tp.setParentTp(tp.getId());
        tp.setId(UUIDUtils.createBase64Uuid());
        tpDao.addMos(Collections.singletonList(tp));
        return tp;
    }

    /**
     * Delete terminate point.
     * 
     * @since SDNO 0.5
     */
    @Override
    public Tp deleteTp(String id) throws ServiceException {
        final Tp tp = tpDao.getMoById(id);
        final boolean rst = tpDao.deleteTp(id);
        if(rst) {
            return tp;
        } else {
            throw new ServiceException("delete tp failed");
        }
    }

    /**
     * Get sites.
     * 
     * @since SDNO 0.5
     */
    @Override
    public List<Tp> getSites() throws ServiceException {
        return tpDao.getAllMo();
    }

    /**
     * Get site.
     * 
     * @since SDNO 0.5
     */
    @Override
    public Tp getSite(String id) throws ServiceException {
        return tpDao.getMoById(id);
    }

}
