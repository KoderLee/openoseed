/*******************************************************************************
 * Copyright (c) 2016, Huawei Technologies Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

package org.openo.nfvo.vimadapter.openstack.entry;

import org.openo.nfvo.vimadapter.util.InterfaceConnectMgr;
import org.openo.nfvo.vimadapter.util.Vim;
import org.openo.nfvo.vimadapter.util.constant.Constant;
import org.openo.nfvo.vimadapter.util.http.LoginException;
import org.openo.nfvo.vimadapter.openstack.connectmgr.ConnectionMgr;

import org.openo.baseservice.log.OssLog;
import org.openo.baseservice.log.OssLogFactory;

/**
 * 
* The conncetion manager between openstack and NFVO<br/>
* <p>
* </p>
* 
* @author
* @version NFVO 0.5 May 15, 2016
 */
public class ConnectMgrOpenstack implements InterfaceConnectMgr {

    private static final OssLog LOG = OssLogFactory
            .getLogger(ConnectMgrOpenstack.class);

    @Override
    public int connect(Vim vim, String type) {
        try {
            ConnectionMgr connectionMgr = ConnectionMgr.getConnectionMgr();

            if (connectionMgr != null) {
                if (Constant.POST.equals(type)) {
                    return connectionMgr.addConnection(vim);
                } else if (Constant.PUT.equals(type)) {
                    return connectionMgr.updateConnection(vim);
                } else if (Constant.DEL.equals(type)) {
                    connectionMgr.disConnect(vim);
                    return Constant.HTTP_OK;
                } else if (Constant.HANDSHAKE.equals(type)) {
                    return connectionMgr.isConnected(vim);
                } else if (type.equals(Constant.FIRST_HANDSHAKE)) {
                    return connectionMgr.addConnection(vim);
                } else {
                    return Constant.TYPE_PARA_ERROR;
                }
            }
        } catch (LoginException e) {
            LOG.error("funtion=connect, msg=connect OpenStackLoginException, Exceptioninfo"
                    + e);
        }
        return Constant.INTERNAL_EXCEPTION;
    }
}
