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
package org.openo.sdno.svc.vpn.l2vpn.service.inf;

import java.util.List;

import org.openo.sdno.remoteservice.exception.ServiceException;
import org.openo.sdno.cbb.sdnwan.util.rest.RestRsp;
import org.openo.sdno.vpn.wan.servicemodel.tepath.TePath;
import org.openo.sdno.vpn.wan.servicemodel.tepath.TePathQueryKey;
import com.puer.framework.base.service.IService;

/**
 * L2VPN TE interface.<br/>
 * 
 * @author
 * @version SDNO 0.5 2016-3-16
 */
public interface IL2vpnTeService extends IService {

    /**
     * Get L2VPN TE path.<br/>
     */
    RestRsp<List<TePath>> getL2vpnTePath(final TePathQueryKey queryKey) throws ServiceException;
}
