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

package org.openo.nfvo.vimadapter.util;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;

import org.openo.baseservice.i18n.ResourceUtil;
import org.openo.baseservice.log.OssLog;
import org.openo.baseservice.log.OssLogFactory;
import org.openo.baseservice.ssl.SSLOption;
import org.apache.commons.io.FileUtils;

/**
 * 
* the abstract class for ssl basic operation<br/>
* <p>
* </p>
* 
* @author
* @version NFVO 0.5 May 15, 2016
 */
public abstract class AbstractSslContext {

    private AbstractSslContext(){
        //Constructor
    }

    private static final OssLog LOGGER = OssLogFactory
            .getLog(AbstractSslContext.class);

    private static final String APP_HOME = System.getProperty("user.dir");

    private static final String FILE_SERP = File.separator;
    private static final String ANO_SSL_SOCKET_NAME = "ssl.vim.cer.properties";

    private static final String CER_SSL_SOCKET_NAME = "ssl.vim.cer.properties";

    public static SSLOption getSslOption(String confName) {
        String confPath = new StringBuffer(APP_HOME).append(FILE_SERP)
                .append("etc/certificate/ssl").append(FILE_SERP)
                .append(confName).toString();
        LOGGER.info("VIM SSL conf path is: " + confPath);
        try {
            SSLOption option = SSLOption.build(confPath);
            return option;
        } catch (IllegalArgumentException e) {
            LOGGER.error("Error when get an vim SSL option", e);
        }
        return null;

    }

    public static String[] getArrayFromStrings(String input) {
        String localStr;
        if (input == null || "".equals(input.trim())) {
            return new String[0];
        }
        localStr = input.replaceAll("\\s*", "");
        String[] infoGet = localStr.split(",|;");
        return infoGet;
    }

    public static String[] getCipherSuitesAnonymous() {
        SSLOption option = getSSLOption(ANO_SSL_SOCKET_NAME);
        if (option != null) {
            String[] resultArray = getArrayFromStrings(option.getCiphers());
            return resultArray;
        }
        return new String[0];
    }

    public static String[] getCipherSuitesCertificate() {
        SSLOption option = getSSLOption(CER_SSL_SOCKET_NAME);
        if (option != null) {
            String[] resultArray = getArrayFromStrings(option.getCiphers());
            return resultArray;
        }
        return new String[0];
    }

    public static String[] getSslProtocolsAnonymous() {
        SSLOption option = getSSLOption(ANO_SSL_SOCKET_NAME);
        if (option != null) {
            String[] resultArray = getArrayFromStrings(option.getProtocols());
            return resultArray;
        }
        return new String[0];
    }

    public static String[] getSslProtocolsCertificate() {
        SSLOption option = getSSLOption(CER_SSL_SOCKET_NAME);
        if (option != null) {
            String[] resultArray = getArrayFromStrings(option.getProtocols());
            return resultArray;
        }
        return new String[0];
    }

    private static SSLContext getSslContext() throws NoSuchAlgorithmException {
        return SSLContext.getInstance("TLSv1.2");
    }

    protected static SSLContext getAnonymousSslContext()
            throws GeneralSecurityException {
        SSLContext sslContext = getSSLContext();
        TrustManager[] trustAllCerts = new TrustManager[] { new TrustAllTrustManager() };
        sslContext.init(null, trustAllCerts, null);
        return sslContext;
    }

    protected static SSLContext getCertificateSslContext()
            throws GeneralSecurityException, IOException {
        FileInputStream keyStoreStream = null;
        FileInputStream trustStoreStream = null;
        try {
            IaaSCertParamsConfig config = IaaSCertParamsConfig.getInstance();

            String keyStore = config.gotKeyStore();
            String keyStorePwd = config.gotKeyStorePwd();
            String keyStoreFormat = config.gotKeyStoreFormat();

            String trustStore = config.gotTrustStore();
            String trustStorePwd = config.gotTrustStorePwd();
            String trustStoreFormat = config.gotTrustStoreFormat();
            if (checkParams(keyStore, keyStorePwd, keyStoreFormat, trustStore,
                    trustStorePwd, trustStoreFormat)) {
                throw new IOException(
                        ResourceUtil
                                .getMessage("vnfm.adapter.check.certificate.property"));
            }

            String keyPwd = keyStorePwd.isEmpty() ? "" : String
                    .valueOf(CryptUtil.deCrypt(keyStorePwd));
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            KeyStore kks = KeyStore.getInstance(keyStoreFormat);
            keyStoreStream = new FileInputStream(FileUtils.getFile(keyStore));
            kks.load(keyStoreStream, keyPwd.toCharArray());
            kmf.init(kks, keyPwd.toCharArray());
            TrustManagerFactory tmf = TrustManagerFactory
                    .getInstance("SunX509");
            KeyStore tks = KeyStore.getInstance(trustStoreFormat);
            trustStoreStream = new FileInputStream(
                    FileUtils.getFile(trustStore));
            String trustPwd = trustStorePwd.isEmpty() ? "" : String
                    .valueOf(CryptUtil.deCrypt(trustStorePwd));
            tks.load(trustStoreStream, trustPwd.toCharArray());
            tmf.init(tks);
            SSLContext sslContext = getSSLContext();
            sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
            return sslContext;
        } catch (FileNotFoundException e) {
            LOGGER.error("got Certificate SSLContext error, No certificate is found: ", e);
            throw new FileNotFoundException(
                    ResourceUtil
                            .getMessage("vnfm.adapter.certificate.no.found"));
        } catch (GeneralSecurityException e) {
            LOGGER.error("got Certificate SSLContext error ", e);
            throw e;
        } catch (IOException e) {
            LOGGER.error("got Certificate SSLContext error ", e);
            throw e;
        } finally {
            closeStream(keyStoreStream);
            closeStream(trustStoreStream);
        }
    }

    private static void closeStream(FileInputStream inputStream) {
        try {
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (IOException e) {
            LOGGER.error("FileInputStream close error ", e);
        }
    }

    private static boolean checkParams(String keyStore, String keyStorePwd,
            String keyStoreFormat, String trustStore, String trustStorePwd,
            String trustStoreFormat) {
        return keyStore == null || keyStorePwd == null
                || keyStoreFormat == null || trustStore == null
                || trustStorePwd == null || trustStoreFormat == null;
    }

    private static class TrustAllTrustManager implements
            javax.net.ssl.X509TrustManager {

        @Override
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            java.security.cert.X509Certificate[] cetificates = new java.security.cert.X509Certificate[0];
            return cetificates;
        }

        @Override
        public void checkServerTrusted(
                java.security.cert.X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
            // only check servers
        }

        @Override
        public void checkClientTrusted(
                java.security.cert.X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
         // only check client
        }
    }
}
