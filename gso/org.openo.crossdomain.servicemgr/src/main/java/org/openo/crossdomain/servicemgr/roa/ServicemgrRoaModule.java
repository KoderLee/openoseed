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
package org.openo.crossdomain.servicemgr.roa;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.openo.crossdomain.servicemgr.constant.Constant;
import org.openo.crossdomain.servicemgr.constant.HttpConstant;
import org.openo.crossdomain.servicemgr.exception.HttpCode;
import org.openo.crossdomain.servicemgr.model.roamo.BaseServiceModelRspInfo;
import org.openo.crossdomain.servicemgr.model.roamo.OperationConstant;
import org.openo.crossdomain.servicemgr.model.roamo.ServiceModelConstant;
import org.openo.crossdomain.servicemgr.model.roamo.ServiceModelInfo;
import org.openo.crossdomain.servicemgr.model.roamo.ServiceModelRspInfo;
import org.openo.crossdomain.servicemgr.model.servicemo.ServiceModel;
import org.openo.crossdomain.servicemgr.model.servicemo.ServiceOperation;
import org.openo.crossdomain.servicemgr.service.inf.IServiceManager;
import org.openo.crossdomain.servicemgr.service.inf.IServiceOperationManager;
import org.openo.crossdomain.servicemgr.util.audit.OperationLog;
import org.openo.crossdomain.servicemgr.util.authorization.TokenUtil;
import org.openo.crossdomain.servicemgr.util.uuid.UUIDUtils;
import org.openo.crossdomain.servicemgr.util.validate.InputParamCheck;
import org.openo.crossdomain.servicemgr.util.validate.ValidateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import org.openo.baseservice.biz.trail.AuditItem.AuditResult;
import org.openo.baseservice.biz.trail.AuditItem.LogSeverity;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.annotation.Consumes;
import org.openo.baseservice.roa.annotation.DEL;
import org.openo.baseservice.roa.annotation.GET;
import org.openo.baseservice.roa.annotation.POST;
import org.openo.baseservice.roa.annotation.PUT;
import org.openo.baseservice.roa.annotation.Path;
import org.openo.baseservice.roa.annotation.PathParam;
import org.openo.baseservice.roa.annotation.Produces;
import org.openo.baseservice.roa.annotation.QueryParam;
import org.openo.baseservice.roa.annotation.Target;
import org.openo.baseservice.roa.common.HttpContext;

/**
 * Rest service entry<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
@Path("servicemanager/v1")
@Target("servicemanager")
public class ServicemgrRoaModule {

    /**
     * log
     */
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * servicemanager
     */
    private IServiceManager servicemanager;

    /**
     * operation log service
     */
    private IServiceOperationManager operationManager;

    /**
     * Query service information<br/>
     * 
     * @param service_id service ID
     * @param context http context
     * @return service object
     * @throws ServiceException customize exception
     * @since crossdomain 0.5
     */
    @GET
    @Produces("application/json")
    @Consumes("application/json")
    @Path("/services/{service_id}")
    public Object getNetworkServiceByServiceId(@PathParam("service_id") String service_id, HttpContext context)
            throws ServiceException {
        ValidateUtil.assertNotEmpty(service_id, ServiceModelConstant.VALIDATE_FIELDS_NUM.ID);
        String tenantID = TokenUtil.getTenantIDByRequest(context);
        ValidateUtil.assertNotEmpty(tenantID, ServiceModelConstant.VALIDATE_FIELDS_NUM.TENANT_ID);

        logger.info("Get getNetworkService by serviceId {}.", service_id);
        ServiceModel serviceModel = servicemanager.getServiceModelByServiceID(tenantID, service_id);
        Map<String, Object> svcModelResult = new HashMap<String, Object>();
        ServiceModelRspInfo svcModelRsp = new ServiceModelRspInfo();
        if(null == serviceModel) {
            context.setResponseStatus(HttpCode.NOT_FOUND);
            return null;
        } else {
            svcModelRsp = serviceModel.toServiceModelRspInfo();
        }
        svcModelResult.put(ServiceModelConstant.RESPONSE_KEY_NUM.SERVICE_KEY, svcModelRsp);

        return svcModelResult;
    }

    /**
     * Query the service information of tenant<br/>
     * 
     * @param name service name, extend query parameter
     * @param description service description, extend query parameter
     * @param activeStatus service status, extend query parameter
     * @param serviceType extend query parameter
     * @param context http context
     * @return service object
     * @throws ServiceException customize exception
     * @since crossdomain 0.5
     */
    @GET
    @Produces("application/json")
    @Consumes("application/json")
    @Path("/services")
    public Object getNetworkServiceByTenantId(@QueryParam("name") String name,
            @QueryParam("description") String description, @QueryParam("activeStatus") String activeStatus,
            @QueryParam("serviceType") String serviceType, HttpContext context) throws ServiceException {
        String tenantID = TokenUtil.getTenantIDByRequest(context);
        ValidateUtil.assertNotEmpty(tenantID, ServiceModelConstant.VALIDATE_FIELDS_NUM.TENANT_ID);

        logger.info("Get getNetworkService by tenantId.");

        Map<String, List<BaseServiceModelRspInfo>> getSvcModelListResult =
                new HashMap<String, List<BaseServiceModelRspInfo>>();

        List<ServiceModel> serviceModelList = servicemanager.getServiceModelByTenantID(tenantID, null);

        List<BaseServiceModelRspInfo> svcModelRspList = new LinkedList<BaseServiceModelRspInfo>();
        if(!CollectionUtils.isEmpty(serviceModelList)) {
            for(ServiceModel svcModel : serviceModelList) {
                svcModelRspList.add(svcModel.toBaseServiceModelRspInfo());
            }
        } else {
            context.setResponseStatus(HttpCode.NOT_FOUND);
            return null;
        }
        getSvcModelListResult.put(ServiceModelConstant.RESPONSE_KEY_NUM.SERVICES_KEY, svcModelRspList);

        return getSvcModelListResult;
    }

    /**
     * Create service<br/>
     * 
     * @param serviceModelInfo service information for network layer
     * @param context http context
     * @return service object
     * @throws ServiceException customize exception
     * @since crossdomain 0.5
     */
    @POST
    @Produces("application/json")
    @Consumes("application/json")
    @Path("/services")
    public Object createNetworkService(ServiceModelInfo serviceModelInfo, HttpContext context) throws ServiceException {
        String serviceId = null;
        ServiceModel serviceModelRsp = new ServiceModel();
        try {
            serviceId = UUIDUtils.createBase64Uuid();
            serviceModelRsp.setUser(context.getHttpServletRequest());
            OperationLog.writeLogString(OperationLog.OPER_TYPE.CREATE_TASK_OPERATION, serviceId, AuditResult.SUCCESS,
                    serviceModelRsp, LogSeverity.WARNING);

            logger.info("ServiceManager create NetWorkService.");

            ValidateUtil.assertNotNull(serviceModelInfo, ServiceModelConstant.VALIDATE_FIELDS_NUM.SERVICEMODLEINFO);
            ValidateUtil.assertNotNull(serviceModelInfo.getService(),
                    ServiceModelConstant.VALIDATE_FIELDS_NUM.SERVICEINFO);

            InputParamCheck.inputParamsCheck(serviceModelInfo);
            InputParamCheck.inputParamsCheck(serviceModelInfo.getService());

            String svcName = serviceModelInfo.getService().getName();
            ValidateUtil.assertNotEmpty(svcName, ServiceModelConstant.VALIDATE_FIELDS_NUM.NAME);

            String svcPkgId = serviceModelInfo.getService().getService_definition_id();
            ValidateUtil.assertNotEmpty(svcPkgId, ServiceModelConstant.VALIDATE_FIELDS_NUM.SERVICE_PACKAGE_ID);

            String templateId = serviceModelInfo.getService().getTemplate_id();
            ValidateUtil.assertNotEmpty(templateId, ServiceModelConstant.VALIDATE_FIELDS_NUM.TEMPLATE_ID);

            String projectID = TokenUtil.getProjectIDByToken(context.getHttpServletRequest().getQueryString());
            ValidateUtil.assertNotEmpty(projectID, ServiceModelConstant.VALIDATE_FIELDS_NUM.PROJECT_ID);

            String tenantID = TokenUtil.getTenantIDByRequest(context);
            ValidateUtil.assertNotEmpty(tenantID, ServiceModelConstant.VALIDATE_FIELDS_NUM.TENANT_ID);

            ServiceModel serviceModelReq = ServiceModel.toServiceModel(serviceModelInfo, serviceId);
            ValidateUtil.assertNotNull(serviceModelReq, ServiceModelConstant.VALIDATE_FIELDS_NUM.SERVICE_MODEL);
            serviceModelReq.setTenant_id(tenantID);
            serviceModelReq.setProject_id(projectID);
            
            String userName = context.getHttpServletRequest().getHeader(Constant.USER_NAME);
            serviceModelRsp =
                    servicemanager.createServiceModel(serviceModelReq, userName, serviceModelInfo,
                            context.getHttpServletRequest());

            setResponseHeader(context, HttpCode.RESPOND_ACCEPTED, serviceModelRsp);
            Map<String, String> result = new HashMap<String, String>();
            result.put(ServiceModelConstant.RESPONSE_KEY_NUM.SERVICE_ID, serviceModelRsp.getService_id());
            result.put(ServiceModelConstant.RESPONSE_KEY_NUM.OPERATION_ID, serviceModelRsp.getOperation_id());

            Map<String, Object> reponseBody = new HashMap<String, Object>();
            reponseBody.put(ServiceModelConstant.RESPONSE_KEY_NUM.SERVICE_KEY, result);

            return reponseBody;
        } catch(ServiceException e) {
            OperationLog.writeLogString(OperationLog.OPER_TYPE.CREATE_TASK_OPERATION, serviceId, AuditResult.FAILURE,
                    serviceModelRsp, LogSeverity.WARNING);
            throw e;
        }
    }

    /**
     * Modify service<br/>
     * 
     * @param service_id service ID
     * @param serviceModelInfo service information for network layer
     * @param context http context
     * @return template object
     * @throws ServiceException customize exception
     * @since crossdomain 0.5
     */
    @PUT
    @Produces("application/json")
    @Consumes("application/json")
    @Path("/services/{service_id}")
    public Object updateNetworkServiceByServicID(@PathParam("service_id") String service_id,
            ServiceModelInfo serviceModelInfo, HttpContext context) throws ServiceException {
        ServiceModel serviceModel = new ServiceModel();
        try {
            serviceModel.setUser(context.getHttpServletRequest());
            OperationLog.writeLogString(OperationLog.OPER_TYPE.UPDATE_TASK_OPERATION, service_id, AuditResult.SUCCESS,
                    serviceModel, LogSeverity.WARNING);

            logger.info("ServiceManager update NetWorkService by serviceID {}.", service_id);
            ValidateUtil.assertNotEmpty(service_id, ServiceModelConstant.VALIDATE_FIELDS_NUM.ID);
            ValidateUtil.assertNotNull(serviceModelInfo, ServiceModelConstant.VALIDATE_FIELDS_NUM.SERVICEMODLEINFO);
            ValidateUtil.assertNotNull(serviceModelInfo.getService(),
                    ServiceModelConstant.VALIDATE_FIELDS_NUM.SERVICEINFO);

            InputParamCheck.inputParamsCheck(serviceModelInfo);
            InputParamCheck.inputParamsCheck(serviceModelInfo.getService());

            String tenantID = TokenUtil.getTenantIDByRequest(context);
            ValidateUtil.assertNotEmpty(tenantID, ServiceModelConstant.VALIDATE_FIELDS_NUM.TENANT_ID);

            Map<String, String> reponseBody = new HashMap<String, String>();

            String userName = context.getHttpServletRequest().getHeader(Constant.USER_NAME);
            serviceModel =
                    servicemanager.updateServiceModel(tenantID, service_id, userName, serviceModelInfo,
                            context.getHttpServletRequest());

            if(null != serviceModel) {
                setResponseHeader(context, HttpCode.RESPOND_ACCEPTED, serviceModel);
                reponseBody.put(ServiceModelConstant.RESPONSE_KEY_NUM.OPERATION_ID, serviceModel.getOperation_id());
            }
            return reponseBody;
        } catch(ServiceException e) {
            OperationLog.writeLogString(OperationLog.OPER_TYPE.UPDATE_TASK_OPERATION, service_id, AuditResult.FAILURE,
                    serviceModel, LogSeverity.WARNING);
            throw e;
        }
    }

    /**
     * Delete service<br/>
     * 
     * @param service_id service ID
     * @param context http context
     * @return response code
     * @throws ServiceException customize exception
     * @since crossdomain 0.5
     */
    @DEL
    @Produces("application/json")
    @Consumes("application/json")
    @Path("/services/{service_id}")
    public Object delNetworkServiceByServiceID(@PathParam("service_id") String service_id, HttpContext context)
            throws ServiceException {
        ServiceModel serviceModel = new ServiceModel();
        try {
            serviceModel.setUser(context.getHttpServletRequest());
            OperationLog.writeLogString(OperationLog.OPER_TYPE.DELETE_TASK_OPERATION, service_id, AuditResult.FAILURE,
                    serviceModel, LogSeverity.RISK);

            ValidateUtil.assertNotEmpty(service_id, ServiceModelConstant.VALIDATE_FIELDS_NUM.ID);
            String tenantID = TokenUtil.getTenantIDByRequest(context);
            ValidateUtil.assertNotEmpty(tenantID, ServiceModelConstant.VALIDATE_FIELDS_NUM.TENANT_ID);

            logger.info("ServiceManager delete NetWorkService by serviceID {}.", service_id);

            Map<String, String> reponseBody = new HashMap<String, String>();

            String userName = context.getHttpServletRequest().getHeader(Constant.USER_NAME);
            serviceModel =
                    servicemanager.delServiceModel(tenantID, service_id, userName, context.getHttpServletRequest());

            if(null != serviceModel) {
                if(serviceModel.isSend()) {
                    setResponseHeader(context, HttpCode.RESPOND_ACCEPTED, serviceModel);
                } else {
                    setResponseHeader(context, HttpCode.RESPOND_OK, serviceModel);
                }

                reponseBody.put(ServiceModelConstant.RESPONSE_KEY_NUM.OPERATION_ID, serviceModel.getOperation_id());
            }
            return reponseBody;
        } catch(ServiceException e) {
            OperationLog.writeLogString(OperationLog.OPER_TYPE.DELETE_TASK_OPERATION, service_id, AuditResult.FAILURE,
                    serviceModel, LogSeverity.RISK);
            throw e;
        }
    }

    /**
     * Inactive service<br/>
     * 
     * @param service_id service id
     * @param serviceModelInfo service information for network layer
     * @param context http context
     * @return response code
     * @throws ServiceException customize exception
     * @since crossdomain 0.5
     */
    @POST
    @Produces("application/json")
    @Consumes("application/json")
    @Path("/services/{service_id}/deactivation")
    public Object deactivServiceModelByServiceID(@PathParam("service_id") String service_id,
            ServiceModelInfo serviceModelInfo, HttpContext context) throws ServiceException {
        ServiceModel serviceModel = new ServiceModel();
        try {
            serviceModel.setUser(context.getHttpServletRequest());
            OperationLog.writeLogString(OperationLog.OPER_TYPE.DEACTIVE_TASK_OPERATION, service_id,
                    AuditResult.SUCCESS, serviceModel, LogSeverity.WARNING);

            InputParamCheck.inputParamsCheck(serviceModelInfo);
            ValidateUtil.assertNotEmpty(service_id, ServiceModelConstant.VALIDATE_FIELDS_NUM.ID);
            String tenantID = TokenUtil.getTenantIDByRequest(context);
            ValidateUtil.assertNotEmpty(tenantID, ServiceModelConstant.VALIDATE_FIELDS_NUM.TENANT_ID);

            logger.info("ServiceManager deactive NetWorkService by serviceID {}.", service_id);

            Map<String, String> reponseBody = new HashMap<String, String>();

            String userName = context.getHttpServletRequest().getHeader(Constant.USER_NAME);

            serviceModel =
                    servicemanager.deactivServiceModel(tenantID, service_id, userName, context.getHttpServletRequest());

            if(null != serviceModel) {
                setResponseHeader(context, HttpCode.RESPOND_ACCEPTED, serviceModel);
                reponseBody.put(ServiceModelConstant.RESPONSE_KEY_NUM.OPERATION_ID, serviceModel.getOperation_id());
            }
            return reponseBody;
        } catch(ServiceException e) {
            OperationLog.writeLogString(OperationLog.OPER_TYPE.DEACTIVE_TASK_OPERATION, service_id,
                    AuditResult.FAILURE, serviceModel, LogSeverity.WARNING);
            throw e;
        }
    }

    /**
     * Active service<br/>
     * 
     * @param service_id service id
     * @param serviceModelInfo service information for network layer
     * @param context http context
     * @return response code
     * @throws ServiceException customize exception
     * @since crossdomain 0.5
     */
    @POST
    @Produces("application/json")
    @Consumes("application/json")
    @Path("/services/{service_id}/activation")
    public Object activServiceModelByServiceID(@PathParam("service_id") String service_id,
            ServiceModelInfo serviceModelInfo, HttpContext context) throws ServiceException {
        ServiceModel serviceModel = new ServiceModel();
        try {
            serviceModel.setUser(context.getHttpServletRequest());
            OperationLog.writeLogString(OperationLog.OPER_TYPE.ACTIVE_TASK_OPERATION, service_id, AuditResult.SUCCESS,
                    serviceModel, LogSeverity.WARNING);

            InputParamCheck.inputParamsCheck(serviceModelInfo);
            ValidateUtil.assertNotEmpty(service_id, ServiceModelConstant.VALIDATE_FIELDS_NUM.ID);

            String tenantID = TokenUtil.getTenantIDByRequest(context);
            ValidateUtil.assertNotEmpty(tenantID, ServiceModelConstant.VALIDATE_FIELDS_NUM.TENANT_ID);

            logger.info("ServiceManager active NetWorkService by serviceID {}.", service_id);

            Map<String, String> reponseBody = new HashMap<String, String>();

            String userName = context.getHttpServletRequest().getHeader(Constant.USER_NAME);

            serviceModel =
                    servicemanager.activServiceModel(tenantID, service_id, userName, context.getHttpServletRequest());

            if(null != serviceModel) {
                setResponseHeader(context, HttpCode.RESPOND_ACCEPTED, serviceModel);
                reponseBody.put(ServiceModelConstant.RESPONSE_KEY_NUM.OPERATION_ID, serviceModel.getOperation_id());
            }
            return reponseBody;
        } catch(ServiceException e) {
            OperationLog.writeLogString(OperationLog.OPER_TYPE.ACTIVE_TASK_OPERATION, service_id, AuditResult.FAILURE,
                    serviceModel, LogSeverity.WARNING);
            throw e;
        }
    }

    /**
     * Query the log of service operation<br/>
     * 
     * @param service_id service id
     * @param context http context
     * @return operations
     * @throws ServiceException customize exception
     * @since crossdomain 0.5
     */
    @GET
    @Produces("application/json")
    @Consumes("application/json")
    @Path("/services/{service_id}/operations")
    public Object getOperationsByServiceID(@PathParam("service_id") String service_id, HttpContext context)
            throws ServiceException {
        ValidateUtil.assertNotEmpty(service_id, ServiceModelConstant.VALIDATE_FIELDS_NUM.ID);

        logger.info("ServiceManager get operations by serviceID {}.", service_id);

        Map<String, Object> operationsResult = new HashMap<String, Object>();
        List<ServiceOperation> operations = operationManager.getServiceOperationsByServiceID(service_id);
        operationsResult.put(OperationConstant.RESPONSE_KEY_NUM.OPERATIONS_KEY, operations);

        return operationsResult;
    }

    /**
     * Query the result and process of service operation<br/>
     * 
     * @param service_id service ID
     * @param operation_id operation ID
     * @param context http context
     * @return operations
     * @throws ServiceException customize exception
     * @since crossdomain 0.5
     */
    @GET
    @Produces("application/json")
    @Consumes("application/json")
    @Path("/services/{service_id}/operations/{operation_id}")
    public Object getOperationsByOperFlowID(@PathParam("service_id") String service_id,
            @PathParam("operation_id") String operation_id, HttpContext context) throws ServiceException {
        ValidateUtil.assertNotEmpty(service_id, ServiceModelConstant.VALIDATE_FIELDS_NUM.ID);
        ValidateUtil.assertNotEmpty(operation_id, OperationConstant.VALIDATE_FIELDS_NUM.OPERATION_ID);

        logger.info("ServiceManager get operations by operationID {}.", operation_id);

        Map<String, Object> operationResult = new HashMap<String, Object>();
        ServiceOperation operation = operationManager.getServiceOperationByID(service_id, operation_id);
        operationResult.put(OperationConstant.RESPONSE_KEY_NUM.OPERATION_KEY, operation);

        return operationResult;
    }

    /**
     * Query the basic information of service resource<br/>
     * 
     * @param service_id service ID
     * @param context http context
     * @return resource
     * @throws ServiceException customize exception
     * @since crossdomain 0.5
     */
    @GET
    @Produces("application/json")
    @Consumes("application/json")
    @Path("/services/{service_id}/resources")
    public Object getResourceByServiceID(@PathParam("service_id") String service_id, HttpContext context)
            throws ServiceException {
        ValidateUtil.assertNotEmpty(service_id, ServiceModelConstant.VALIDATE_FIELDS_NUM.ID);
        logger.info("ServiceManager get resource by serviceID {}.", service_id);

        return servicemanager.getResourceByServiceID(service_id, context.getHttpServletRequest());
    }

    /**
     * Set the location attribution for server response's header<br/>
     * 
     * @param context http context
     * @param httpCode http code
     * @param serviceModel the model of service
     * @since crossdomain 0.5
     */
    private void setResponseHeader(HttpContext context, int httpCode, ServiceModel serviceModel) {
        // /rest/servicemanager/v1/services/{service_id}/operations/{operation_id}
        String rspHeaderLocation =
                new StringBuilder(HttpConstant.RESPONSEHEADER_LOCATION_PREFIX)
                        .append(HttpConstant.RESPONSEHEADER_LOCATION_SERVICES).append(Constant.FORWARD_SLASH)
                        .append(serviceModel.getService_id()).append(Constant.FORWARD_SLASH)
                        .append(HttpConstant.RESPONSEHEADER_LOCATION_OPERATIONS).append(Constant.FORWARD_SLASH)
                        .append(serviceModel.getOperation_id()).toString();

        context.setResponseHeader(HttpConstant.RESPONSEHEADER_LOCATION, rspHeaderLocation);
        context.setResponseStatus(httpCode);
    }

    /**
     * @param servicemanager The servicemanager to set.
     */
    public void setServicemanager(IServiceManager servicemanager) {
        this.servicemanager = servicemanager;
    }

    /**
     * @param operationManager The operationManager to set.
     */
    public void setOperationManager(IServiceOperationManager operationManager) {
        this.operationManager = operationManager;
    }
}
