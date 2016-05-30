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
package org.openo.crossdomain.commonsvc.executor.service.impl;

import org.openo.commonservice.biz.trail.AuditItem;
import org.openo.commonservice.log.OssLog;
import org.openo.commonservice.log.OssLogFactory;
import org.openo.commonservice.remoteservice.exception.ServiceException;
import org.openo.commonservice.roa.common.HttpContext;
import org.openo.commonservice.roa.common.RequestInputStream;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang.StringUtils;
import org.openo.crossdomain.commonsvc.executor.common.check.ModelChecker;
import org.openo.crossdomain.commonsvc.executor.common.constant.Constants;
import org.openo.crossdomain.commonsvc.executor.common.constant.ErrorMessage;
import org.openo.crossdomain.commonsvc.executor.common.constant.RequestJsonConstants;
import org.openo.crossdomain.commonsvc.executor.common.util.LogUtil;
import org.openo.crossdomain.commonsvc.executor.common.util.ServiceExceptionUtil;
import org.openo.crossdomain.commonsvc.executor.dao.inf.IServiceJobDao;
import org.openo.crossdomain.commonsvc.executor.dao.inf.IServiceResDao;
import org.openo.crossdomain.commonsvc.executor.model.Resource;
import org.openo.crossdomain.commonsvc.executor.model.ServiceJob;
import org.openo.crossdomain.commonsvc.executor.model.db.Result;
import org.openo.crossdomain.commonsvc.executor.model.util.Model2JsonUtil;
import org.openo.crossdomain.commonsvc.executor.service.Manager;
import org.openo.crossdomain.commonsvc.executor.service.Receiver;
import org.openo.crossdomain.commonsvc.executor.service.Restorer;
import org.openo.crossdomain.commonsvc.executor.service.inf.IServiceExecutorService;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

/**
 * Service Executor Service class<br/>
 *
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
@Service(value = "serviceExecutorService")
public class ServiceExecutorService implements IServiceExecutorService {

    /**
     * log util
     */
    private static final OssLog log = OssLogFactory.getLogger(ServiceExecutorService.class);

    @javax.annotation.Resource
    private IServiceJobDao srvExectuorJobDao; // DAO api

    @javax.annotation.Resource
    private IServiceResDao srvExectuorResourceDao; // DAO api

    @javax.annotation.Resource
    private Restorer restorer;

    @javax.annotation.Resource
    private Receiver receiver;

    /**
     * Execute Job Service<br/>
     *
     * @param input Request Input Stream
     * @param context HttpContext
     * @return an url using for query job detail (with key of "location")
     * @throws ServiceException when fail to execute job
     * @since crossdomain 0.5
     */
    @Override
    public String executeJob(RequestInputStream input, HttpContext context) throws ServiceException {
        String inputJsonStr = input.getBodyStr();
        if(!StringUtils.isNotEmpty(inputJsonStr)) {
            String msg = String.format(ErrorMessage.OBJECT_NULL_MSG, Constants.REQUEST_BODY);
            ServiceExceptionUtil.throwBadRequestException(msg);
        }

        ServiceJob job = ServiceJob.toServiceJob(inputJsonStr);
        if(job == null) {
            ServiceExceptionUtil.throwErrorException(ErrorMessage.SERVICE_JOB_INVALID_MSG);
        }

        // basic information check, include job and service
        try {
            BasicCheckerForJob.validate(job);
        } catch(ServiceException e) {
            ServiceExceptionUtil.throwBadRequestException(e.getMessage());
        }

        // get tenant id from HttpServletRequest header
        String tenantId = context.getHttpServletRequest().getHeader(Constants.HttpContext.X_TENANT_ID);
        if(tenantId == null) {
            tenantId = Constants.NULL_STR;
        }
        job.setTenantId(tenantId);

        // insert the basic information of log into database, which can be used for asynchronous
        // query
        AuditItem logItem = LogUtil.getAuditItem(context, job.getService().getServiceId(), AuditItem.LogSeverity.RISK);
        job.setAuditBasicInfoByAuditItem(logItem);

        Result result = receiver.pushJob(job);
        if(!result.isSuccess()) {
            ServiceExceptionUtil.throwErrorException(result.getResultReason());
        }

        context.setResponseStatus(HttpStatus.SC_ACCEPTED);

        String queueUrl = null;
        if(!StringUtils.isEmpty(job.getJobId())) {
            queueUrl = Constants.REST_PRE + "/" + Constants.URIPATH_SERVICE + "/" + job.getJobId();
        } else {
            ServiceExceptionUtil.throwErrorException(ErrorMessage.GENERATE_JOBID_FAIL_MSG);
        }

        // record the operation log
        LogUtil.writeStartCreateJobLog(logItem, job);

        return Model2JsonUtil.value2Json(Constants.LOCATION_KEY, queueUrl);
    }

    /**
     * <br/>
     *
     * @param tenantId Tenant id
     * @param jobId UUID of the job
     * @return job detail in form of ServiceJob model
     * @throws ServiceException when fail to get the job detail
     * @since crossdomain 0.5
     */
    @Override
    public ServiceJob getJobDetail(String tenantId, String jobId) throws ServiceException {
        if(!StringUtils.isNotEmpty(jobId)) {
            String msg = String.format(ErrorMessage.OBJECT_NULL_MSG, RequestJsonConstants.ServiceJob.jobId);
            ServiceExceptionUtil.throwBadRequestException(msg);
        }

        ServiceJob job = receiver.getServiceJob(jobId);
        if(job != null) {
            return job;
        }

        job = Manager.getInstance().get(tenantId, jobId);
        if(job != null) {
            return job;
        }

        try {
            job = srvExectuorJobDao.getJob(jobId);
            if(job != null) {
                List<Resource> resourceList = srvExectuorResourceDao.getAllResourcesOfJob(jobId);

                // replace the Resource in job with the result after the job is completed
                for(Resource resource : resourceList) {
                    job.getService().getResources().put(resource.getKey(), resource);
                }

                // restore the date to Redis
                restorer.restoreJob(job);
            } else {
                log.info("job not in db: {}", jobId);
            }
        } catch(SQLException e) {
            String msg = String.format("db error: %s", e.getMessage());
            ServiceExceptionUtil.throwErrorException(msg);
        }

        return job;
    }

    /**
     * Get Job List<br/>
     *
     * @param serviceId UUID of ServiceInfo
     * @param pageIndex page number
     * @param pageCapacity Maximum number of items can be displayed in one page
     * @return job details in form of ServiceJob List
     * @throws ServiceException when fail to get job list
     * @since crossdomain 0.5
     */
    @Override
    public List<ServiceJob> getJobList(String serviceId, String pageIndex, String pageCapacity) throws ServiceException {
        checkServiceId(serviceId, pageIndex, pageCapacity);
        int pageIndexInt = -1;
        int pageCapacityInt = -1;
        int startIndex = -1;
        if(StringUtils.isNotEmpty(pageIndex) && StringUtils.isNotEmpty(pageCapacity)) {
            // pageIndex and pageCapacity must be numbers
            if(!StringUtils.isNumeric(pageIndex) || !StringUtils.isNumeric(pageCapacity)) {
                ServiceExceptionUtil.throwBadRequestException(ErrorMessage.URI_INVALID_MSG);
            }

            pageIndexInt = Integer.parseInt(pageIndex);
            pageCapacityInt = Integer.parseInt(pageCapacity);
            // pageIndex and pageCapacity should start at 1
            if((pageIndexInt < 1) || (pageCapacityInt < 1)) {
                ServiceExceptionUtil.throwBadRequestException(ErrorMessage.URI_INVALID_MSG);
            }
            startIndex = pageCapacityInt * (pageIndexInt - 1);
        } else if((StringUtils.isNotEmpty(pageIndex) && StringUtils.isEmpty(pageCapacity))
                || (StringUtils.isEmpty(pageIndex) && StringUtils.isNotEmpty(pageCapacity))) {
            ServiceExceptionUtil.throwBadRequestException(ErrorMessage.URI_INVALID_MSG);
        }

        List<ServiceJob> jobList = null;
        try {
            jobList = srvExectuorJobDao.getJobByServiceID(serviceId, startIndex, pageCapacityInt);
            for(ServiceJob serviceJob : jobList) {
                serviceJob.setService(null);
            }
        } catch(SQLException e) {
            String msg = String.format("db error: %s", e.getMessage());
            ServiceExceptionUtil.throwErrorException(msg);
        }

        return jobList;
    }

    /**
     * Check Input Parameters<br/>
     *
     * @param serviceId UUID of ServiceInfo
     * @param pageIndex pageNumber
     * @param pageCapacity Maximum number of items can be displayed in one page
     * @throws ServiceException when input parameters is invalid
     * @since crossdomain 0.5
     */
    private void checkServiceId(String serviceId, String pageIndex, String pageCapacity) throws ServiceException {
        // check input serviceId
        if(StringUtils.isEmpty(serviceId)) {
            // if serviceId is empty, pageIndex and pageCapacity must not be empty
            if((StringUtils.isEmpty(pageIndex) || StringUtils.isEmpty(pageCapacity))) {
                String msg = String.format(ErrorMessage.OBJECT_NULL_MSG, RequestJsonConstants.Service.serviceId);
                ServiceExceptionUtil.throwBadRequestException(msg);
            }
        } else {
            // the length of serviceId must be in [1,32]
            if(serviceId.length() > ModelChecker.UUID_MAX_LENGTH) {
                String msg =
                        String.format(ErrorMessage.SERVICE_FIELD_INVALID_MSG, RequestJsonConstants.Service.serviceId);
                ServiceExceptionUtil.throwBadRequestException(msg);
            }
        }
    }
}
