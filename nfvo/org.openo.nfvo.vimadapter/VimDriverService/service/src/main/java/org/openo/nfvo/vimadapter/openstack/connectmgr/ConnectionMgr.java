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

package org.openo.nfvo.vimadapter.openstack.connectmgr;

import java.util.List;

import org.openo.baseservice.log.OssLog;
import org.openo.baseservice.log.OssLogFactory;
import org.openo.baseservice.redis.oper.MapOper;

import org.openo.nfvo.vimadapter.util.Vim;
import org.openo.nfvo.vimadapter.util.constant.Constant;
import org.openo.nfvo.vimadapter.util.http.LoginException;
import org.openo.nfvo.vimadapter.openstack.api.ConnectInfo;

/**
 * 
* The connection manager entity(establish/get/update/disable), used for other service<br/>
* <p>
* </p>
* 
* @author
* @version NFVO 0.5 May 15, 2016
 */
public final class ConnectionMgr {
    private static final OssLog LOG = OssLogFactory
            .getLogger(ConnectionMgr.class);

    private static ConnectionMgr conSingleton = null;

    private MapOper<OpenstackConnection> connectMap = new MapOper<OpenstackConnection>(
            "vimadapter:" + Constant.OPENSTACK, "vimcachedb");

    private ConnectionMgr() {
    }

    public synchronized static ConnectionMgr getConnectionMgr() {
        if (conSingleton == null) {
            conSingleton = new ConnectionMgr();
        }
        return conSingleton;
    }

    public synchronized int addConnection(Vim vim) throws LoginException {
        LOG.info("funtion=addConnection, msg=create a new connection.");

        ConnectInfo info = new ConnectInfo(vim);
        OpenstackConnection connect = new OpenstackConnection(info);
        int status = connect.connect();
        connectMap.put(info.getUrl(), info.getUrl(), connect);
        return status;
    }

    public synchronized int updateConnection(Vim vim) throws LoginException {
        LOG.info("funtion=updateConnection, msg=update connection.");

        int status = Constant.HTTP_INNERERROR;
        ConnectInfo info = new ConnectInfo(vim);
        if (!connectMap.exists(info.getUrl(), info.getUrl())) {
            LOG.warn("funtion=Connection, msg=not find connection.");
            status = Constant.CONNECT_NOT_FOUND;
            return status;
        }

        List<OpenstackConnection> connects = connectMap.get(info.getUrl(),
                OpenstackConnection.class, info.getUrl());
        OpenstackConnection connect = connects.get(0);
        connect.setConnInfo(info);
        status = connect.connect();
        connectMap.put(info.getUrl(), info.getUrl(), connect);
        return status;
    }

    public OpenstackConnection getConnection(ConnectInfo info)
            throws LoginException {
        OpenstackConnection connect = null;
        if (connectMap.exists(info.getUrl(), info.getUrl())) {
            List<OpenstackConnection> connects = connectMap.get(
                    info.getUrl(), OpenstackConnection.class, info.getUrl());
            connect = connects.get(0);
            if (connect.isNeedRenewInfo(info)
                    || (connect.isConnected() != Constant.HTTP_OK)) {
                connect.setConnInfo(info);
                connect.connect();
                connectMap.put(info.getUrl(), info.getUrl(), connect);
            }
        } else {
            LOG.info("funtion=getConnection, msg=Connection is not exist, new one .");
            connect = new OpenstackConnection(info);
            connect.connect();
            connectMap.put(info.getUrl(), info.getUrl(), connect);
        }
        return connect;
    }

    public synchronized void disConnect(Vim vim) {
        ConnectInfo info = new ConnectInfo(vim);
        List<OpenstackConnection> connects = connectMap.get(info.getUrl(),
                OpenstackConnection.class, info.getUrl());
        OpenstackConnection connect = connects.get(0);
        if (connect != null) {
            connect.disconnect();
            connectMap.remove(info.getUrl(), info.getUrl());
        }
    }

    public int isConnected(Vim vim) throws LoginException {
        ConnectInfo info = new ConnectInfo(vim);
        LOG.debug("funtion=isConnected, msg=isConnected, connection info:",
                info.toString());

        List<OpenstackConnection> connects = connectMap.get(info.getUrl(),
                OpenstackConnection.class, info.getUrl());
        OpenstackConnection connect = connects.get(0);
        if (connect != null) {
            return connect.isConnected();
        }

        LOG.warn(
                "funtion=isConnected, msg=isConnected is null, connection info:",
                info.toString());
        return Constant.CONNECT_FAIL;
    }
}
