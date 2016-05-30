/*
 * Copyright (c) 2016, Huawei Technologies Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *  
 */
package org.openo.sdno.svc.vpn.l2vpn.common.util;

import org.apache.commons.lang.StringUtils;

import org.openo.sdno.remoteservice.exception.ExceptionArgs;
import org.openo.sdno.remoteservice.exception.ServiceException;
import org.openo.sdno.roa.common.HttpContext;
import org.openo.sdno.roa.util.restclient.RestfulResponse;
import org.openo.sdno.cbb.sdnwan.util.rest.RestRsp;
import org.openo.sdno.svc.vpn.l2vpn.common.constant.ErrorCode;

/**
 * Class to deal with service exception.<br/>
 * 
 * @author
 * @version SDNO 0.5 2016-3-17
 */
public class ServiceExceptionUtil {

    public static final String ADVICEARGS = "_adviceArgs";

    private ServiceExceptionUtil() {
    }

    /**
     * Get error detail by id to create a RestRsp class.<br/>
     * 
     * @param id exception id
     * @param detail detail of exception
     * @return RestRsp object which contain the exception detail
     * @since SDNO 0.5
     */
    public static <T> RestRsp<T> getOtherExpRsp(String id, String detail) {
        String errorCode = id;
        String errorInfo = getErrorMessage(id, id);
        String[] descArgs = {errorInfo};
        String[] reasonArgs = {errorInfo};
        String[] detailArgs = {detail, errorCode};
        String[] adviceArgs = {getI18Msg(errorCode + ADVICEARGS)};
        ExceptionArgs args = new ExceptionArgs(descArgs, reasonArgs, detailArgs, adviceArgs);
        RestRsp<T> rsp =
                new RestRsp<T>(isNumeric(errorCode) ? Integer.parseInt(id) : ErrorCode.ERROR_OTHER_HANDLE_FAILED,
                        errorInfo);
        rsp.setExceptionArg(args);
        return rsp;
    }

    /**
     * Throw another exception.<br/>
     * 
     * @param id exception id
     * @param detail detail of exception
     * @throws ServiceException with the input id and detail
     * @since SDNO 0.5
     */
    public static <T> void throwOtherExp(String id, String detail) throws ServiceException {
        RestRsp<T> rsp = getOtherExpRsp(id, detail);
        throwExpByRsp(rsp);
    }

    /**
     * Get a RestRsp class with the inner exception detail of adaptor layer.<br/>
     * 
     * @param response restful interface response class
     * @param msg the error message from restful interface
     * @return a RestRsp object which contain the exception detail
     * @since SDNO 0.5
     */
    public static <T> RestRsp<T> getAdaptorLayerInnerExpRsp(RestfulResponse response, String msg) {
        String errorCode = String.valueOf(ErrorCode.ERROR_ADAPTORLAYER_INNERERROR_FAILED);
        String errorInfo = getErrorMessage(ErrorCode.ERROR_ADAPTORLAYER_INNERERROR_FAILED, msg);
        String[] descArgs = {errorInfo};
        String[] reasonArgs = {msg};
        String[] detailArgs = {msg, errorCode};
        String[] adviceArgs = {getI18Msg(errorCode + ADVICEARGS)};
        ExceptionArgs args = new ExceptionArgs(descArgs, reasonArgs, detailArgs, adviceArgs);
        RestRsp<T> rsp = new RestRsp<T>(ErrorCode.ERROR_ADAPTORLAYER_INNERERROR_FAILED, errorInfo);
        rsp.setExceptionArg(args);
        return rsp;
    }

    /**
     * Get a RestRsp class with adaptor layer invoking exception.<br/>
     * 
     * @param id error id from restful response
     * @param detail error detail from restful response
     * @return a RestRsp object which contain the exception detail
     * @since SDNO 0.5
     */
    public static <T> RestRsp<T> getInvokeExpRsp(String id, String detail) {
        String errorCode = String.valueOf(ErrorCode.ERROR_INVOKE_ADAPTORLAYER_FAILED);
        String errorInfo = getErrorMessage(ErrorCode.ERROR_INVOKE_ADAPTORLAYER_FAILED, id);
        String[] descArgs = {errorInfo};
        String[] reasonArgs = {errorInfo};
        String[] detailArgs = {detail, errorCode};
        String[] adviceArgs = {getI18Msg(errorCode + ADVICEARGS)};
        ExceptionArgs args = new ExceptionArgs(descArgs, reasonArgs, detailArgs, adviceArgs);
        RestRsp<T> rsp = new RestRsp<T>(ErrorCode.ERROR_INVOKE_ADAPTORLAYER_FAILED, errorInfo);
        rsp.setExceptionArg(args);
        return rsp;
    }

    /**
     * Get a RestRsp class with translation exception.<br/>
     * 
     * @param id error id from restful response
     * @param detail error detail from restful response
     * @return a RestRsp object which contain the exception detail
     * @throws ServiceException with the input id and detail
     * @since SDNO 0.5
     */
    public static <T> RestRsp<T> getTranslateExpRsp(String id, String detail) {
        String errorCode = String.valueOf(ErrorCode.ERROR_TRANSLATE_DATA_FAILED);
        String errorInfo = getErrorMessage(ErrorCode.ERROR_TRANSLATE_DATA_FAILED, id);
        String[] descArgs = {errorInfo};
        String[] reasonArgs = {errorInfo};
        String[] detailArgs = {detail, errorCode};
        String[] adviceArgs = {getI18Msg(errorCode + ADVICEARGS)};
        ExceptionArgs args = new ExceptionArgs(descArgs, reasonArgs, detailArgs, adviceArgs);
        RestRsp<T> rsp = new RestRsp<T>(ErrorCode.ERROR_TRANSLATE_DATA_FAILED, errorInfo);
        rsp.setExceptionArg(args);
        return rsp;
    }

    /**
     * Creat a RestRsp class by exception detail.<br/>
     * 
     * @param exp input exception
     * @return a RestRsp object which contain the exception detail
     * @since SDNO 0.5
     */
    public static <T> RestRsp<T> getFailRspByDetailExp(ServiceException exp) {
        if(exp == null) {
            return new RestRsp<T>();
        }
        String errorInfo = "";
        ExceptionArgs args = exp.getExceptionArgs();
        if(args != null && args.getDescArgs() != null) {
            String[] descArgs = args.getDescArgs();
            if(descArgs.length != 0) {
                errorInfo = descArgs[0];
            }
        }
        RestRsp<T> rsp =
                new RestRsp<T>(isNumeric(exp.getId()) ? Integer.parseInt(exp.getId())
                        : ErrorCode.ERROR_OTHER_HANDLE_FAILED, errorInfo);
        rsp.setExceptionArg(exp.getExceptionArgs());
        return rsp;
    }

    /**
     * Throw a new exception by RestRsp.<br/>
     * 
     * @param rsp input RestRsp object
     * @throws ServiceException created by a RestRsp object
     * @since SDNO 0.5
     */
    public static <T> void throwExpByRsp(RestRsp<T> rsp) throws ServiceException {
        ServiceException exp = new ServiceException(String.valueOf(rsp.getResult()), rsp.getMessage());
        exp.setExceptionArgs(rsp.getExceptionArg());
        throw exp;
    }

    /**
     * Get an unsupported RestRsp class.<br/>
     * 
     * @param msg error message
     * @return a RestRsp object which contain the exception detail
     * @since SDNO 0.5
     */
    public static <T> RestRsp<T> getUnsupportedRsp(String msg) {
        String errorCode = String.valueOf(ErrorCode.ERROR_UNSUPPORTED_FAILED);
        String errorInfo = getErrorMessage(ErrorCode.ERROR_UNSUPPORTED_FAILED, msg);
        String[] descArgs = {errorInfo};
        String[] reasonArgs = {msg};
        String[] detailArgs = {msg, errorCode};
        String[] adviceArgs = {getI18Msg(errorCode + ADVICEARGS)};
        ExceptionArgs args = new ExceptionArgs(descArgs, reasonArgs, detailArgs, adviceArgs);
        RestRsp<T> rsp = new RestRsp<T>(ErrorCode.ERROR_UNSUPPORTED_FAILED, errorInfo);
        rsp.setExceptionArg(args);
        return rsp;
    }

    /**
     * Throw an unsupported exception.<br/>
     * 
     * @param msg error message
     * @throws ServiceException created by the input message
     * @since SDNO 0.5
     */
    public static <T> void throwUnsupportedExp(String msg) throws ServiceException {
        RestRsp<T> rsp = getUnsupportedRsp(msg);
        throwExpByRsp(rsp);
    }

    /**
     * Get a RestRsp with bad request exception.<br/>
     * 
     * @param msg error message
     * @return a RestRsp object which contain the exception detail
     * @since SDNO 0.5
     */
    public static <T> RestRsp<T> getBadRequestRsp(String msg) {
        final String errorCode = String.valueOf(ErrorCode.ERROR_INVALID_PARAMETER_FAILED);
        final String errorInfo = getErrorMessage(ErrorCode.ERROR_INVALID_PARAMETER_FAILED, msg);
        final String[] descArgs = {errorInfo};
        final String[] reasonArgs = {msg};
        final String[] detailArgs = {msg};
        final ExceptionArgs args = new ExceptionArgs(descArgs, reasonArgs, detailArgs, null);
        final RestRsp<T> rsp = new RestRsp<T>(ErrorCode.ERROR_INVALID_PARAMETER_FAILED, errorCode);
        rsp.setExceptionArg(args);
        return rsp;
    }

    /**
     * Throw a bad request exception.<br/>
     * 
     * @param msg error message
     * @throws ServiceException created by the input error message
     * @since SDNO 0.5
     */
    public static <T> void throwBadRequestExp(String msg) throws ServiceException {
        RestRsp<T> rsp = getBadRequestRsp(msg);
        throwExpByRsp(rsp);
    }

    /**
     * Throw a exception due to service deleted.<br/>
     * 
     * @throws ServiceException caused by delete service failure
     * @since SDNO 0.5
     */
    public static <T> void throwDelServiceCheckTpExp() throws ServiceException {
        String errorCode = String.valueOf(ErrorCode.ERROR_DELSERVICE_ACTIVE_FAILED);
        String errorInfo = getErrorMessage(ErrorCode.ERROR_DELSERVICE_ACTIVE_FAILED, "");
        String[] descArgs = {errorInfo};
        String[] reasonArgs = {errorInfo};
        String[] detailArgs = {errorInfo, errorCode};
        String[] adviceArgs = {getI18Msg(errorCode + ADVICEARGS)};
        ExceptionArgs args = new ExceptionArgs(descArgs, reasonArgs, detailArgs, adviceArgs);
        RestRsp<T> rsp = new RestRsp<T>(ErrorCode.ERROR_DELSERVICE_ACTIVE_FAILED, errorInfo);
        rsp.setExceptionArg(args);
        throwExpByRsp(rsp);
    }

    /**
     * Throw a exception due to service already exist.<br/>
     * 
     * @throws ServiceException cause by trying to create an existing service
     * @since SDNO 0.5
     */
    public static <T> void throwCreateServiceCheckExistExp() throws ServiceException {
        String errorCode = String.valueOf(ErrorCode.ERROR_CREATE_ISEXIST_FAILED);
        String errorInfo = getErrorMessage(ErrorCode.ERROR_CREATE_ISEXIST_FAILED, "");
        String[] descArgs = {errorInfo};
        String[] reasonArgs = {errorInfo};
        String[] detailArgs = {errorInfo, errorCode};
        String[] adviceArgs = {getI18Msg(errorCode + ADVICEARGS)};
        ExceptionArgs args = new ExceptionArgs(descArgs, reasonArgs, detailArgs, adviceArgs);
        RestRsp<T> rsp = new RestRsp<T>(ErrorCode.ERROR_CREATE_ISEXIST_FAILED, errorInfo);
        rsp.setExceptionArg(args);
        throwExpByRsp(rsp);
    }

    /**
     * The http response is not ok and without exception information.<br/>
     * 
     * @param rsp input RestRsp object
     * @since SDNO 0.5
     */
    public static <T> void checkFailRsp(RestRsp<T> rsp) {
        if(rsp != null && rsp.getResult() != ErrorCode.ERROR_HTTP_STATUS_OK_TETURN && rsp.getExceptionArg() == null) {
            String errorCode = String.valueOf(rsp.getResult());
            String errorInfo = getErrorMessage(errorCode, "");
            String[] descArgs = {errorInfo};
            String[] reasonArgs = {rsp.getMessage()};
            String[] detailArgs = {rsp.getMessage(), errorCode};
            String[] adviceArgs = {getI18Msg(errorCode + ADVICEARGS)};
            ExceptionArgs args = new ExceptionArgs(descArgs, reasonArgs, detailArgs, adviceArgs);
            rsp.setExceptionArg(args);
        }
    }

    /**
     * Set http response code according to RestRsp.<br/>
     * 
     * @param rsp input RestRsp object
     * @since SDNO 0.5
     */
    public static void setHttpRspStatus(RestRsp<?> rsp, HttpContext httpContext) {
        if(rsp != null && rsp.getResult() != ErrorCode.RESULT_SUCCESS) {
            if(rsp.getResult() >= ErrorCode.ERROR_OTHER_HANDLE_FAILED
                    && rsp.getResult() <= ErrorCode.ERROR_TRANSLATE_DATA_FAILED) {
                httpContext.setResponseStatus(ErrorCode.ERROR_HTTP_STATUS_INTERNAL_ERROR);
            } else {
                httpContext.setResponseStatus(ErrorCode.ERROR_HTTP_STATUS_BAD_REQUEST);
            }
        }
    }

    /**
     * Return a RestRsp instance with http response ok.<br/>
     * 
     * @param data input data
     * @return a RestRsp object which contain the success code
     * @since SDNO 0.5
     */
    public static <T> RestRsp<T> getSuccesRestRsp(T data) {
        return new RestRsp<T>(ErrorCode.RESULT_SUCCESS, data);

    }

    /**
     * Get a RestRsp from an old one.<br/>
     * 
     * @param origin old RestRsp
     * @param data input data object
     * @return a RestRsp object which contain the exception detail
     * @since SDNO 0.5
     */
    public static <T1, T2> RestRsp<T1> convertRestRsp(RestRsp<T2> origin, T1 data) {
        RestRsp<T1> rsp = new RestRsp<T1>(origin.getResult(), origin.getMessage(), data);
        rsp.setExceptionArg(origin.getExceptionArg());
        return rsp;
    }

    /**
     * Get error message by return code.<br/>
     * 
     * @param retCode error code
     * @param errorMsg error message
     * @return error detail get from the input error code or message
     * @since SDNO 0.5
     */
    private static String getErrorMessage(int retCode, String errorMsg) {
        ErrorResMgr mng = ErrorResMgr.getInstance();
        String errorInfo = mng.getErrorMessage(String.valueOf(retCode));
        if(StringUtils.isEmpty(errorInfo) && StringUtils.isNotEmpty(errorMsg)) {
            errorInfo = errorMsg;
        }
        return errorInfo;
    }

    /**
     * Get error message by return code.<br/>
     * 
     * @param retCode error code
     * @param errorMsg error message
     * @return error detail get from the input error code or message
     * @since SDNO 0.5
     */
    private static String getErrorMessage(String retCode, String errorMsg) {
        ErrorResMgr mng = ErrorResMgr.getInstance();
        String errorInfo = mng.getErrorMessage(retCode);
        if(StringUtils.isEmpty(errorInfo) && StringUtils.isNotEmpty(errorMsg)) {
            errorInfo = errorMsg;
        }
        return errorInfo;
    }

    private static String getI18Msg(String propertyName) {
        ErrorResMgr mng = ErrorResMgr.getInstance();
        String reStr = mng.getErrorMessage(propertyName);
        if(StringUtils.isNotEmpty(propertyName) && propertyName.equals(reStr)) {
            return "";
        }
        return reStr;

    }

    private static boolean isNumeric(String str) {
        if(StringUtils.isEmpty(str)) {
            return false;
        }
        for(int i = str.length(); --i >= 0;) {
            if(!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
