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
package org.openo.crossdomain.servicemgr.job.impl;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.io.IOUtils;
import org.openo.crossdomain.servicemgr.constant.Constant;
import org.openo.crossdomain.servicemgr.dao.inf.IServiceModelDao;
import org.openo.crossdomain.servicemgr.dao.inf.IServiceParameterDao;
import org.openo.crossdomain.servicemgr.exception.HttpCode;
import org.openo.crossdomain.servicemgr.model.servicemo.ServiceModel;
import org.openo.crossdomain.servicemgr.restrepository.ICatalogProxy;
import org.openo.crossdomain.servicemgr.service.impl.job.JobFactory.InitJobBean;
import org.openo.crossdomain.servicemgr.service.inf.IServiceOperationManager;
import org.openo.crossdomain.servicemgr.util.authorization.AuthConfigInfo;
import org.openo.crossdomain.servicemgr.util.authorization.SecureTokenHelper;
import org.openo.crossdomain.servicemgr.util.http.HttpClientUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.openo.baseservice.encrypt.cbb.CipherException;
import org.openo.baseservice.encrypt.cbb.CipherManager;
import org.openo.baseservice.encrypt.cbb.KeyType;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.crossdomain.commsvc.common.util.jsonutil.JsonUtil;
import org.openo.crossdomain.commsvc.jobscheduler.dao.inf.IJobDao;
import org.openo.crossdomain.commsvc.jobscheduler.job.StepPolicy;

/**
 * Query progress job.<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
public abstract class QueryProgressJob extends AbsPersistentJob {

    public static final Logger logger = LoggerFactory.getLogger(QueryProgressJob.class);

    private static final String SVCDECOMPOSER_TASK_REST_URL_PREFIX = "/rest";

    private final static String ICTO_POLL_TIME_CFG_FILE_PATH = "./etc/ICTO_POLL_TIME_CFG.property";

    private static final int POLL_TIME = getPollTime();

    private static final int DEFAULT_POLL_TIME = 15;

    public final static String COMPLETED = "completed";

    public final static String SUCCESS = "success";

    public final static String FAILED = "failed";

    public final static int RETRY_NUM = 3;

    public IJobDao jobDao;

    public IServiceModelDao serviceModelDao;

    public IServiceParameterDao serviceParameterDao;

    public ICatalogProxy catalogProxy;

    public IServiceOperationManager operationManager;

    private static void close(final Closeable toClosed) {
        if(toClosed != null) {
            IOUtils.closeQuietly(toClosed);
        }
    }

    private static int getPollTime() {
        File file = new File(ICTO_POLL_TIME_CFG_FILE_PATH);
        InputStream ins = null;
        try {
            if(file.exists()) {
                ins = new FileInputStream(file);

                Properties properties = new Properties();
                properties.load(ins);

                final String time = properties.getProperty("ICTO_POLL_TIME");
                if(time != null) {
                    return Integer.parseInt(time);
                }
            }
        } catch(IOException e) {
            logger.error("exception", e);
        } finally {
            if(ins != null) {
                close(ins);
            }
        }

        return DEFAULT_POLL_TIME;
    }

    /**
     * Start operation policy.<br/>
     *
     * @return delete policy
     * @since crossdomain 0.5
     */
    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public StepPolicy run() {
        logger.info("job.run() start! The job is QueryProgressJob.");
        ServiceModel serviceModel = new ServiceModel();
        try {
            String context = jobBean.getContext();
            try {
                char ct[] = CipherManager.getInstance().decrypt(context.toCharArray(), KeyType.SHARE, "common_shared");
                if(ct == null) {
                    return StepPolicy.createDeletedPolicy();
                }

                InitJobBean initJobBean = JsonUtil.unMarshal(new String(ct), InitJobBean.class);
                serviceModel = initJobBean.getServiceModel();
            } catch(CipherException e) {
                logger.error("CipherManager decrypt failed!");

                handleFailed(serviceModel, null);
                return StepPolicy.createDeletedPolicy();
            }

            String attribute = jobBean.getAttribute();
            final Map<String, Object> attributeJson = JsonUtil.unMarshal(attribute, Map.class);

            Object url = attributeJson.get(Constant.LOCATION);
            if(null == url) {
                logger.error("getUrl failed! Url is null");

                handleFailed(serviceModel, null);
                return StepPolicy.createDeletedPolicy();
            }
            logger.info("getUrl success! Url {}", url);

            String queryUrl = SVCDECOMPOSER_TASK_REST_URL_PREFIX + url.toString();

            RestfulResponse response = getProgressFromDecomposer(queryUrl, serviceModel);
            if(null == response || (!HttpCode.isSucess(response.getStatus()))) {
                logger.error("getProgress failed! response Status :" + (null == response ? null : response.getStatus()));

                handleFailed(serviceModel, null);
                return StepPolicy.createDeletedPolicy();
            }

            final Map<String, Object> jsonObject = JsonUtil.unMarshal(response.getResponseContent(), Map.class);

            Object obj = jsonObject.get(Constant.TASK);
            if(!(obj instanceof Map)) {
                logger.error("QueryProgressJob failed! status {} responseContent : {}", response.getStatus(),
                        response.getResponseContent());
                handleFailed(serviceModel, null);
                return StepPolicy.createDeletedPolicy();
            }

            Object process = ((Map)obj).get(Constant.PROGRESS);
            Object result = ((Map)obj).get(Constant.RESULT);

            if(null != process && null != result && QueryProgressJob.COMPLETED.equals(process.toString())) {

                if(QueryProgressJob.SUCCESS.equals(result)) {
                    logger.info("QueryProgressJob success!");
                    handleComplete(serviceModel);
                    return StepPolicy.createDeletedPolicy();
                }
                logger.error("QueryProgressJob failed! the result is {}", result);
                handleFailed(serviceModel, ((Map)obj).get(Constant.REASON).toString());
                return StepPolicy.createDeletedPolicy();
            }

            logger.info("QueryProgressJob not complete! createRescheduleLaterPolicy");
            return StepPolicy.createRescheduleLaterPolicy(POLL_TIME);
        } catch(ServiceException e) {
            logger.error("unMarshal failed!");
        }
        handleFailed(serviceModel, null);
        return StepPolicy.createDeletedPolicy();
    }

    private RestfulResponse getProgressFromDecomposer(String queryUrl, ServiceModel serviceModel) {
        RestfulResponse response = null;
        int retryNum = 0;
        while(RETRY_NUM - retryNum >= 0) {

            try {
                response = getProgress(queryUrl, serviceModel);
                if(HttpStatus.SC_SERVICE_UNAVAILABLE != response.getStatus()) {
                    break;
                }
                retryNum++;
                logger.error("QueryProgressJob failed! query number :" + retryNum);
            } catch(ServiceException e) {
                retryNum++;
                logger.error("QueryProgressJob failed! query number :" + retryNum);
            }
        }
        return response;
    }

    private RestfulResponse getProgress(String url, ServiceModel serviceModel) throws ServiceException {
        Map<String, String> headerMap = new HashMap<String, String>();

        String token =
                SecureTokenHelper.getAssumeToken(AuthConfigInfo.getInstance().getAccessName(), AuthConfigInfo
                        .getInstance().getIdentity(), serviceModel.getTenant_id(), serviceModel.getProject_id());
        headerMap.put("X-Auth-Token", token);

        return HttpClientUtil.get(url, headerMap);
    }

    /**
     * Handle for completing to operate service successfully.<br/>
     *
     * @param serviceModel service data
     * @since crossdomain 0.5
     */
    abstract void handleComplete(ServiceModel serviceModel);

    /**
     * Handle for failing to operate service.<br/>
     *
     * @param serviceModel service data.
     * @param reason the reason of failure
     * @since crossdomain 0.5
     */
    abstract void handleFailed(ServiceModel serviceModel, String reason);
}
