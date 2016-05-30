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

package org.openo.nfvo.vimadapter.util.http;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.security.GeneralSecurityException;

import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.protocol.ControllerThreadSocketFactory;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;

import org.openo.baseservice.i18n.ResourceUtil;
import org.openo.baseservice.log.OssLog;
import org.openo.baseservice.log.OssLogFactory;

import org.openo.nfvo.vimadapter.util.AbstractSslContext;

/**
 * 
* Implement the AbstractSSLContext for ssl socket certificate<br/>
* <p>
* </p>
* 
* @author
* @version NFVO 0.5 May 15, 2016
 */
public class SslCertificateSocket extends AbstractSSLContext implements
        ProtocolSocketFactory {
    private static final OssLog LOGGER = OssLogFactory
            .getLog(SslCertificateSocket.class);

    private SSLSocketFactory sslSocketFactory = null;

    public void init() throws LoginException {
        try {
            this.sslSocketFactory = getCertificateSSLContext()
                    .getSocketFactory();
        } catch (GeneralSecurityException e) {
            LOGGER.error("got Certificate SSLContext exception ", e);
            throw new LoginException(e.getMessage());
        } catch (IOException e) {
            LOGGER.error("got Certificate SSLContext exception ", e);
            throw new LoginException(e.getMessage());
        }
    }

    private void handleSocket(Socket socket) {
        if (socket instanceof SSLSocket) {
            SSLSocket sslSocket = (SSLSocket) socket;
            sslSocket.setEnabledProtocols(getSslProtocolsCertificate());

            String[] supported = sslSocket.getSupportedCipherSuites();
            String[] eanbled = getCipherSuitesCertificate();
            sslSocket.setEnabledCipherSuites(getEnabledCiphers(supported,
                    eanbled));
            sslSocket.setNeedClientAuth(true);
        }
    }

    private static String[] getEnabledCiphers(String[] supported,
            String[] enabled) {
        String[] result = new String[enabled.length];
        int count = 0;
        for (String e : enabled) {
            for (String s : supported) {
                if (e.equals(s)) {
                    result[count++] = e;
                    break;
                }
            }
        }

        if (count == 0) {
            throw new IllegalArgumentException("no enabled cipher suits.");
        }

        String[] r = new String[count];
        System.arraycopy(result, 0, r, 0, count);
        return r;
    }

    @Override
    public Socket createSocket(String host, int port) throws IOException {
        Socket socket = this.sslSocketFactory.createSocket(host, port);
        handleSocket(socket);
        return socket;
    }

    @Override
    public Socket createSocket(String host, int port, InetAddress clientHost,
            int clientPort) throws IOException {
        Socket socket = sslSocketFactory.createSocket(host, port, clientHost,
                clientPort);
        handleSocket(socket);
        return socket;
    }

    @Override
    public Socket createSocket(String host, int port, InetAddress localAddress,
            int localPort, HttpConnectionParams params) throws IOException {
        if (null == params) {
            throw new IOException(
                    ResourceUtil
                            .getMessage("vnfm.adapter.openstack.check.createsocket.params"));
        }

        int timeout = params.getConnectionTimeout();
        Socket socket;
        if (timeout == 0) {
            socket = sslSocketFactory.createSocket(host, port, localAddress,
                    localPort);
        } else {
            socket = ControllerThreadSocketFactory.createSocket(this, host,
                    port, localAddress, localPort, timeout);
        }
        handleSocket(socket);
        return socket;
    }
}