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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.openo.baseservice.log.OssLog;
import org.openo.baseservice.log.OssLogFactory;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.openo.nfvo.vimadapter.util.TimeUtil;
import org.openo.nfvo.vimadapter.util.constant.Constant;
import org.openo.nfvo.vimadapter.util.constant.ParamConstants;
import org.openo.nfvo.vimadapter.util.constant.UrlConstant;
import org.openo.nfvo.vimadapter.util.http.HttpRequests;
import org.openo.nfvo.vimadapter.util.http.LoginException;
import org.openo.nfvo.vimadapter.util.http.ResultUtil;
import org.openo.nfvo.vimadapter.openstack.api.ConnectInfo;

/**
 * 
* The connection manager between openstack and NFVO, include the establish/get/update/disable operation<br/>
* <p>
* </p>
* 
* @author
* @version NFVO 0.5 May 15, 2016
 */
public class OpenstackConnection {
    private static final OssLog LOG = OssLogFactory
            .getLogger(OpenstackConnection.class);

    private ConnectInfo connInfo;

    private String domainTokens;

    private long detalTime;

    private String projectId;

    private Map<String, List<JSONObject>> urlMap = new HashMap<String, List<JSONObject>>(
            Constant.DEFAULT_COLLECTION_SIZE);

    public Map<String, List<JSONObject>> getUrlMap() {
        return urlMap;
    }

    public OpenstackConnection() {
        //constructor
    }

    public OpenstackConnection(ConnectInfo info) {
        this.connInfo = info;
    }

    public int connect() {
        HttpMethod httpMethod = null;
        try {
            httpMethod = new HttpRequests.Builder(
                    connInfo.getAuthenticateMode())
                    .setUrl(String.format(UrlConstant.POST_AUTH_TOKENS_V2,
                            connInfo.getUrl()))
                    .setParams(
                            String.format(ParamConstants.DOMAIN_TOKENS_V2,
                                    connInfo.getUserPwd(),
                                    connInfo.getUserName(),
                                    connInfo.getDomainName())).post().execute();

            String result = ResultUtil.getResponseBody(httpMethod
                    .getResponseBody());

            if (httpMethod.getStatusCode() == HttpStatus.SC_OK
                    || httpMethod.getStatusCode() == HttpStatus.SC_CREATED) {
                JSONObject accessObj = JSONObject.fromObject(result)
                        .getJSONObject(Constant.WRAP_ACCESS);

                if (!accessObj.containsKey(Constant.WRAP_TOKEN)) {
                    return Constant.ACCESS_OBJ_NULL;
                }

                JSONObject token = accessObj.getJSONObject(Constant.WRAP_TOKEN);
                this.domainTokens = token.getString(Constant.ID);
                this.projectId = token.getJSONObject(Constant.WRAP_TENANT)
                        .getString(Constant.ID);

                this.detalTime = sotDetalTime(accessObj);

                if (!setServiceUrl(accessObj)) {
                    return Constant.SERVICE_URL_ERROR;
                }
            }

            return httpMethod.getStatusCode();
        } catch (LoginException e) {
            LOG.error("function=connect, msg=connect OpenStackException, Exceptioninfo:"
                    + e);
        } catch (UnsupportedEncodingException e) {
            LOG.error("function=connect, msg=connect UnsupportedEncodingException, Exceptioninfo:"
                    + e);
        } catch (ConnectTimeoutException e) {
            LOG.error("function=connect, msg=connect ConnectTimeoutException, Exceptioninfo:"
                    + e);
            return Constant.CONNECT_TMOUT;
        } catch (IOException e) {
            LOG.error("function=connect, msg=connect IOException, Exceptioninfo:"
                    + e);
        } finally {
            if (httpMethod != null) {
                httpMethod.releaseConnection();
            }
        }
        return Constant.INTERNAL_EXCEPTION;
    }

    private boolean setServiceUrl(JSONObject accessObj) {
        if (accessObj.containsKey(Constant.SERVICE_CATALOG)) {
            JSONArray serviceCatalog = accessObj
                    .getJSONArray(Constant.SERVICE_CATALOG);
            LOG.debug("function=setServiceUrl, msg=serviceCatalog:"
                    + serviceCatalog);
            int scSize = serviceCatalog.size();
            JSONObject singleLog = null;
            List<JSONObject> urlst = null;

            for (int i = 0; i < scSize; i++) {
                singleLog = serviceCatalog.getJSONObject(i);
                urlst = getRegionUrlMap(singleLog
                        .getJSONArray(Constant.WRAP_ENDPOINTS));
                urlMap.put(singleLog.getString(Constant.SERVICENAME)
                        .toLowerCase(Locale.getDefault()), urlst);
            }

            LOG.warn("function=setServiceUrl, msg=urlMap:{}", urlMap);
            return true;
        }

        LOG.error("function=setServiceUrl, msg=get service catalog failed");
        return false;
    }

    private List<JSONObject> getRegionUrlMap(JSONArray endPoints) {
        List<JSONObject> urlst = new ArrayList<JSONObject>(
                Constant.DEFAULT_COLLECTION_SIZE);
        JSONObject regionUrlMap = new JSONObject();
        int epSize = endPoints.size();
        String url = null;
        String region = null;

        for (int i = 0; i < epSize; i++) {
            url = handleEndpointUrl(endPoints.getJSONObject(i).getString(
                    Constant.PUBLICURL));
            region = endPoints.getJSONObject(i).getString(Constant.REGION);
            regionUrlMap.put(region, url);
            urlst.add(regionUrlMap);
        }
        LOG.warn("function=getRegionUrlMap, msg=urlMap:{}", urlst);
        return urlst;
    }

    public void setDetalTime(long detalTime) {
        this.detalTime = detalTime;
    }

    private static String handleEndpointUrl(String url) {
        if (url == null || url.isEmpty()) {
            return null;
        }
        
        String localStr;
        

        localStr = url.trim();

        int index = localStr.lastIndexOf("/v");

        if (index == -1) {
            if (localStr.charAt(localStr.length() - 1) == '/') {

                localStr = localStr.substring(0, localStr.length() - 1);
            }
        } else {
            localStr = localStr.substring(0, index);
        }
        return localStr;
    }

    public int isConnected() {
        HttpMethod httpMethod = null;
        try {
            httpMethod = new HttpRequests.Builder(
                    connInfo.getAuthenticateMode())
                    .addHeader(Constant.HEADER_AUTH_TOKEN, domainTokens)
                    .setUrl(String.format(UrlConstant.GET_SERVICES_V3,
                            connInfo.getUrl())).get().execute();

            if (httpMethod.getStatusCode() == HttpStatus.SC_OK) {
                return HttpStatus.SC_OK;
            } else if (httpMethod.getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
                LOG.warn("function=isConnected, msg=domian tokens invalid, create one.");
                connect();
                return HttpStatus.SC_OK;
            }
        } catch (IOException e) {
            LOG.error("function=isConnected, msg=isConnected exception, exceptioninfo:"
                    + e);
        } catch (LoginException e) {
            LOG.error("function=isConnected, msg=isConnected OpenStackException, exceptioninfo:"
                    + e);
        } finally {
            if (httpMethod != null) {
                httpMethod.releaseConnection();
            }
        }
        return HttpStatus.SC_BAD_REQUEST;
    }

    public void disconnect() {
        try {
            if (domainTokens == null) {
                return;
            }

            deleteTokens(domainTokens);

        } catch (LoginException e) {
            LOG.error("function=isConnected, msg=disConnect OpenStackException, exceptioninfo:"
                    + e);
        } finally {
            domainTokens = null;
        }
    }

    private void deleteTokens(String tokens) throws LoginException {
        LOG.info("function=deleteTokens, msg=delete tokens ...");

        new HttpRequests.Builder(connInfo.getAuthenticateMode())
                .addHeader(Constant.HEADER_SUBJECT_TOKEN, tokens)
                .addHeader(Constant.HEADER_AUTH_TOKEN, tokens)
                .setUrl(String.format(UrlConstant.POST_AUTH_TOKENS_V3,
                        connInfo.getUrl())).delete().request();
    }

    public boolean isNeedRenewInfo(ConnectInfo info) {
        return connInfo.isNeedRenewInfo(info);
    }

    public void setConnInfo(ConnectInfo connInfo) {
        this.connInfo = connInfo;
    }

    public ConnectInfo getConnInfo() {
        return connInfo;
    }

    public String getDomainTokens() {
        return domainTokens;
    }

    public String getProjectId() {
        return projectId;
    }

    public long getDetalTime() {
        return detalTime;
    }

    public long sotDetalTime(JSONObject accessObj) {
        String issuedAt = accessObj.getJSONObject(Constant.WRAP_TOKEN)
                .getString(Constant.ISSUED_AT);
        long time = TimeUtil.getIntTime(issuedAt);
        return time == Constant.TIME_EXCEPT_VALUE ? Constant.TIME_EXCEPT_VALUE
                : (time - TimeUtil.getTime());
    }

    public String getServiceUrl(String serviceName) {
        return urlMap.get(serviceName.toLowerCase(Locale.getDefault())).get(0)
                .values().iterator().next().toString();
    }
}
