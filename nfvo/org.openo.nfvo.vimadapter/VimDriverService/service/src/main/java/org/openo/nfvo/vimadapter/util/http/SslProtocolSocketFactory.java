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

import java.util.Hashtable;
import java.util.Map;

import org.openo.baseservice.i18n.ResourceUtil;
import org.openo.baseservice.log.OssLog;
import org.openo.baseservice.log.OssLogFactory;

import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.openo.nfvo.vimadapter.util.constant.Constant;

public class SslProtocolSocketFactory {
    private static final OssLog LOG = OssLogFactory
            .getLogger(SslProtocolSocketFactory.class);

    private static final Map<String, ProtocolSocketFactory> SOCKMAP = new Hashtable<String, ProtocolSocketFactory>(
            Constant.DEFAULT_COLLECTION_SIZE);

    private static SslProtocolSocketFactory singleinstance = null;

    public static synchronized SslProtocolSocketFactory getInstance() {
        if (singleinstance == null) {
            singleinstance = new SslProtocolSocketFactory();
        }
        return singleinstance;
    }

    public synchronized ProtocolSocketFactory get(String authenticateMode)
            throws LoginException {
        if (SOCKMAP.get(authenticateMode) == null) {
            if (Constant.AuthenticationMode.ANONYMOUS.equals(authenticateMode)) {
                SslAnonymousSocket anonymous = new SslAnonymousSocket();
                anonymous.init();
                SOCKMAP.put(Constant.AuthenticationMode.ANONYMOUS, anonymous);
            } else if (Constant.AuthenticationMode.CERTIFICATE
                    .equals(authenticateMode)) {
                SslCertificateSocket certificate = new SslCertificateSocket();
                certificate.init();
                SOCKMAP.put(Constant.AuthenticationMode.CERTIFICATE,
                        certificate);
            } else {
                LOG.error(
                        "funtion=get, msg=ProtocolSocketFactory Unknown AuthenticateMode={}",
                        authenticateMode);
                throw new LoginException(
                        String.format(
                                ResourceUtil
                                        .getMessage("org.openo.nfvo.vimadapter.unknown.authenticate.mode.format"),
                                authenticateMode));
            }
        }

        return SOCKMAP.get(authenticateMode);
    }

    public synchronized void refresh(String autherMode) throws LoginException {
        if (Constant.AuthenticationMode.ANONYMOUS.equals(autherMode)) {
            SslAnonymousSocket anonymous = new SslAnonymousSocket();
            anonymous.init();
            SOCKMAP.put(Constant.AuthenticationMode.ANONYMOUS, anonymous);
        } else if (Constant.AuthenticationMode.CERTIFICATE.equals(autherMode)) {
            SslCertificateSocket certificate = new SslCertificateSocket();
            certificate.init();
            SOCKMAP.put(Constant.AuthenticationMode.CERTIFICATE, certificate);
        }
    }
}