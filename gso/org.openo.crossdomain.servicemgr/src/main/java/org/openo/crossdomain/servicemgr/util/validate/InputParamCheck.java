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
package org.openo.crossdomain.servicemgr.util.validate;

import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import org.openo.baseservice.remoteservice.exception.ExceptionArgs;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.crossdomain.commsvc.validator.InputParaValidator;
import org.openo.crossdomain.commsvc.validator.adt.RuleErrorInfo;

/**
 * Check input parameter.<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
public class InputParamCheck {

	/**
	 * Check parameters that are input.<br/>
	 *
	 * @param validData data that need to be check
	 * @throws ServiceException
	 * @since crossdomain 0.5
	 */
    public static void inputParamsCheck(Object validData) throws ServiceException {
        InputParaValidator ipv = new InputParaValidator();
        List<RuleErrorInfo> errList = ipv.validate(validData);
        if(errList != null && errList.size() > 0) {
            RuleErrorInfo info = errList.get(0);
            List<String> errParam = info.getLstErrParam();
            ExceptionArgs args = new ExceptionArgs();
            args.setDescArgs(new String[] {info.getParamName()});

            if(null != errParam) {
                args.setDetailArgs(errParam.toArray(new String[errParam.size()]));
            }
            throw new ServiceException(info.getErrorMsgKey(), HttpStatus.BAD_REQUEST_400, args);
        }
    }
}
