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

import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.EntityEnclosingMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.lang.StringUtils;

import org.openo.baseservice.i18n.ResourceUtil;
import org.openo.baseservice.log.OssLog;
import org.openo.baseservice.log.OssLogFactory;

import org.openo.nfvo.vimadapter.util.constant.Constant;

/**
 * 
* Used for http request of rest call, such as post/put/get/delete<br/>
* <p>
* </p>
* 
* @author
* @version NFVO 0.5 May 15, 2016
 */
public final class HttpRequests {
    private static final OssLog LOG = OssLogFactory
            .getLogger(HttpRequests.class);

    private static final MultiThreadedHttpConnectionManager HTTPCLIENTMANAGER;

    static {
        HTTPCLIENTMANAGER = new MultiThreadedHttpConnectionManager();
        HTTPCLIENTMANAGER.getParams().setStaleCheckingEnabled(true);
        HTTPCLIENTMANAGER.getParams().setMaxTotalConnections(20);
        HTTPCLIENTMANAGER.getParams().setDefaultMaxConnectionsPerHost(100);
    }

    private HttpRequests() {

    }

    public static class Builder {
        private final List<Header> headers = new ArrayList<Header>();

        private String paramsJson;

        private HttpClient client;

        private HttpMethod httpMethod;

        private String url;

        private String encoding;

        private String authenticateMode;

        public Builder(String authenticateMode) {
            this.authenticateMode = authenticateMode;
            client = new HttpClient(HTTPCLIENTMANAGER);
            client.getHttpConnectionManager().getParams()
                    .setConnectionTimeout(60000);
            client.getHttpConnectionManager().getParams().setSoTimeout(60000);
            encoding = Constant.ENCODEING;
        }

        public Builder addHeader(String name, String value) {
            headers.add(new Header(name, value));
            return this;
        }

        public Builder addHeaders(Header header, Header... headers) {
            if (header != null) {
                this.headers.add(header);
            }
            if (headers != null && headers.length > 0) {
                for (Header h : headers) {
                    this.headers.add(h);
                }
            }
            return this;
        }

        public Builder addHeaders(List<Header> headers) {
            if (headers != null && !headers.isEmpty()) {
                this.headers.addAll(headers);
            }
            return this;
        }

        public Builder setUrl(String url) throws LoginException {
            String urlTmp = url;
            if (StringUtils.isEmpty(url)) {
                throw new LoginException(
                        ResourceUtil
                                .getMessage("org.openo.nfvo.vimadapter.openstack.check.httprequest.url"));
            } else {
                if (url.contains("/identity-admin/v3/services")) {
                    urlTmp = url.replaceAll("/identity-admin/v3/services",
                            "/identity/v3/services");
                }
                if (url.contains("/identity-admin/v3/endpoints")) {
                    urlTmp = url.replaceAll("/identity-admin/v3/endpoints",
                            "/identity/v3/endpoints");
                }
            }

            this.url = urlTmp;

            Protocol.registerProtocol("https", new Protocol("https",
                    SslProtocolSocketFactory.getInstance()
                            .get(authenticateMode), 443));

            return this;
        }

        /**
         * POST method.
         */
        public Builder post() {
            this.httpMethod = new PostMethod(url);
            return this;
        }

        /**
         * GET method.
         */
        public Builder get() {
            this.httpMethod = new GetMethod(url);
            return this;
        }

        /**
         * PUT method.
         */
        public Builder put() {
            this.httpMethod = new PutMethod(url);
            return this;
        }

        /**
         * DELETE method.
         */
        public Builder delete() {
            this.httpMethod = new DeleteMethod(url);
            return this;
        }

        public Builder setParams(String json) {
            this.paramsJson = json;
            return this;
        }

        public Builder setEncoding(String encode) {
            this.encoding = encode;
            return this;
        }

        public String request() {
            String result = null;
            try {
                result = executeMethod().getResponseBodyAsString();
            } catch (SSLHandshakeException e) {
                LOG.error(
                        String.format(
                                "function=request, msg=http request url: %s, SSLHandshake Fail : ",
                                url), e);
                try {
                    LOG.error("function=request, msg=SSLHandshake Fail, start refresh certificate ...");
                    SslProtocolSocketFactory socketFactory = SslProtocolSocketFactory
                            .getInstance();
                    socketFactory.refresh(authenticateMode);
                    Protocol.registerProtocol("https",
                            new Protocol("https", SslProtocolSocketFactory
                                    .getInstance().get(authenticateMode), 443));
                    LOG.error("function=request, msg=SSLHandshake Fail, certificate refresh successful .");

                    result = executeMethod().getResponseBodyAsString();
                } catch (IOException ioe) {
                    LOG.error(
                            String.format(
                                    "function=request, msg=http request url: %s, error: ",
                                    url), ioe);
                } catch (LoginException ose) {
                    LOG.error(
                            String.format(
                                    "function=request, msg=http request url: %s, error: ",
                                    url), ose);
                }
            } catch (IOException e) {
                LOG.error(String.format(
                        "function=request, msg=http request url: %s, error: ",
                        url), e);
            } catch (LoginException e) {
                LOG.error(String.format(
                        "function=request, msg=http request url: %s, error: ",
                        url), e);
            } finally {
                httpMethod.releaseConnection();
            }
            return result;
        }

        public HttpMethod execute() throws LoginException, IOException {
            try {
                executeMethod();
            } catch (SSLHandshakeException e) {
                LOG.error(
                        String.format(
                                "function=execute, msg=http request url: %s, SSLHandshake Fail : ",
                                url), e);
                LOG.error("function=execute, SSLHandshake Fail, start refresh certificate ...");
                SslProtocolSocketFactory socketFactory = SslProtocolSocketFactory
                        .getInstance();
                socketFactory.refresh(authenticateMode);
                Protocol.registerProtocol("https",
                        new Protocol("https", SslProtocolSocketFactory
                                .getInstance().get(authenticateMode), 443));
                LOG.error("function=execute, SSLHandshake Fail, certificate refresh successful .");

                executeMethod();
            }
            return httpMethod;
        }

        private HttpMethod executeMethod() throws LoginException, IOException {
            if (httpMethod == null) {
                httpMethod = new GetMethod(url);
            }

            handleParams();

            client.executeMethod(httpMethod);

            return httpMethod;
        }

        private void handleParams() throws UnsupportedEncodingException {
            if (paramsJson != null && !paramsJson.isEmpty()) {
                StringRequestEntity stringRequestEntity = new StringRequestEntity(
                        paramsJson, "application/json", encoding);
                String contentLengthString = String.valueOf(stringRequestEntity
                        .getContentLength());

                if (httpMethod instanceof PostMethod
                        || httpMethod instanceof PutMethod) {
                    ((EntityEnclosingMethod) httpMethod)
                            .setRequestEntity(stringRequestEntity);
                    ((EntityEnclosingMethod) httpMethod).addRequestHeader(
                            "Content-Length", contentLengthString);
                } else {
                    httpMethod.setQueryString(paramsJson);
                }
                addHeader("Content-Type",
                        String.format("application/json;charset=%s", encoding));
            }

            for (Header header : headers) {
                httpMethod.addRequestHeader(header);
            }
        }
    }
}