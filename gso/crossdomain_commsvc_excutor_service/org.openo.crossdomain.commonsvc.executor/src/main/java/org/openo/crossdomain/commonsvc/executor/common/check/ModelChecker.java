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
package org.openo.crossdomain.commonsvc.executor.common.check;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.openo.crossdomain.commonsvc.executor.common.constant.Constants;

import org.openo.commonservice.log.OssLog;
import org.openo.commonservice.log.OssLogFactory;
import org.openo.commonservice.remoteservice.exception.ServiceException;

	/**
     * Check Model<br/>
	 * @author
     * @since crossdomain 0.5
     */
public final class ModelChecker {

    public final static int ERROR_CODE_CHECK = 400;

    public final static int UUID_MAX_LENGTH = 32;

    public final static int STRING_MAX_LENGTH = 512;

    private final static OssLog logger = OssLogFactory.getLogger(ModelChecker.class);

    /**
     * Check Model Validation<br/>
     *
     * @param obj Object to be validated
	 * @throws ServiceException when fail to validateModel
     * @since crossdomain 0.5
     */
    public static String validateModel(Object obj) throws ServiceException {
        if(obj == null) {
            return "";
        }

        String result = "";
        if(obj instanceof List<?>) {
            for(Object o : (List<?>)obj) {
                result = validate(o);
            }
        } else {
            result = validate(obj);
        }

        return result;

    }

    private static String validate(Object obj) throws ServiceException {
        String result;
        result = getValidator(obj);
        if(!result.isEmpty()) {
            logger.error("Validation Error ! " + result);
            throw new ServiceException("Validation Error");
        }
        return validateSubModel(obj);
    }
    /**
     * Check SubModel Validation<br/>
     *
     * @param obj Object to be validated
	 * @throws ServiceException when fail to validateModel
     * @since crossdomain 0.5
     */
    public static String validateSubModel(Object obj) throws ServiceException {
        String result = Constants.NULL_STR;
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        try {
            for(Field field : declaredFields) {
                if(!(field.isAnnotationPresent(CheckSubObj.class))) {
                    continue;
                }

                field.setAccessible(true);
                Object value = field.get(obj);
                result = validateModel(value);
            }
        } catch(ServiceException e) {
            throw e;
        } catch(Exception e) {
            logger.error("validateSubModel", e);
            throw new ServiceException(e.getMessage());
        }
        return result;
    }

    private static String getValidator(Object obj) {
        StringBuffer buffer = new StringBuffer();
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(obj);
        Iterator<ConstraintViolation<Object>> iter = constraintViolations.iterator();
        ConstraintViolation<Object> objIterator;
        while(iter.hasNext()) {
            objIterator = iter.next();
            buffer.append(objIterator.getPropertyPath().toString()).append(" ").append(objIterator.getMessage())
                    .append(", ");
        }

        return buffer.toString();
    }
}
