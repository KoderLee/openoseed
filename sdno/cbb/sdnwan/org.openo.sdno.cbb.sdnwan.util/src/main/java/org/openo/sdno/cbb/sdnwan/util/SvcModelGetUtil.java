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
package org.openo.sdno.cbb.sdnwan.util;

import java.util.ArrayList;
import java.util.List;

import org.openo.sdno.remoteservice.exception.ServiceException;
import org.openo.sdno.vpn.wan.servicemodel.tp.Tp;
import org.openo.sdno.vpn.wan.servicemodel.vpn.SegmentVpn;
import org.openo.sdno.vpn.wan.servicemodel.vpn.Vpn;

/**
 * <br/>
 * <p>
 * Utility Class to validate the VPN input and get its specific attribute value
 * </p>
 * 
 * @author
 * @version SDNO 0.5 17-Mar-2016
 */
public class SvcModelGetUtil {

    private SvcModelGetUtil() {
    }

    /**
     * <br/>
     * <p>
     * Checks the validity of the input and Gets the service Type of Vpn Info object
     * </p>
     * 
     * @param segVpn The VPN object whose status is to be return
     * @return the service Type of Vpn Info object
     * @throws ServiceException throws exception if the input or the content of the input is null
     * @since SDNO 0.5
     */
    public static String getVpnServiceType(SegmentVpn segVpn) throws ServiceException {
        if(segVpn != null) {
            Vpn vpnInfo = segVpn.getVpnInfo();

            if((vpnInfo != null) && (vpnInfo.getVpnBasicInfo() != null)) {
                return vpnInfo.getVpnBasicInfo().getServiceType();
            } else {
                throw new ServiceException("Vpn service Type not fpund, vpnInfo is null.");
            }
        } else {
            throw new ServiceException("Vpn service Type not fpund, SegmentVpn is null.");
        }
    }

    /**
     * <br/>
     * <p>
     * Checks the validity of the input VPN and Gets the list of access points
     * </p>
     * 
     * @param segVpn the VPN object to be examined
     * @return list of access points
     * @since SDNO 0.5
     */
    public static List<Tp> getAccessPointList(SegmentVpn segVpn) {
        if(segVpn != null) {
            Vpn vpnInfo = segVpn.getVpnInfo();

            if((vpnInfo != null) && (vpnInfo.getVpnBasicInfo() != null)) {
                return vpnInfo.getVpnBasicInfo().getAccessPointList();
            } else {
                return new ArrayList<Tp>(0);
            }
        } else {
            return new ArrayList<Tp>(0);
        }
    }
}
