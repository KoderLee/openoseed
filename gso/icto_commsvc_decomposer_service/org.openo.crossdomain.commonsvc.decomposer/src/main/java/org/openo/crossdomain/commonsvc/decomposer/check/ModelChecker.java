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
package org.openo.crossdomain.commonsvc.decomposer.check;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.httpclient.HttpStatus;
import org.openo.crossdomain.commonsvc.decomposer.constant.ErrorCode;

import org.openo.commonservice.log.OssLog;
import org.openo.commonservice.log.OssLogFactory;
import org.openo.commonservice.remoteservice.exception.ServiceException;

/**
 * Model checker
 * 
 * @since crossdomain 0.5
 */
public class ModelChecker {

	/**
	 * logger
	 */
	private final static OssLog LOGGER = OssLogFactory
			.getLogger(ModelChecker.class);

	/**
	 * error code 400
	 */
	public final static int ERROR_CODE_CHECK = 400;

	/**
	 * pass code 200
	 */
	public final static int PASS_CODE_CHECK = 200;

	/**
	 * check action
	 * 
	 * @param oldObj old object
	 * @param newObj new object
	 * @return validate result
	 * @since crossdomain 0.5
	 */
	public static CheckResult check(final Object oldObj, final Object newObj) {
		Field[] declaredFields = newObj.getClass().getDeclaredFields();
		try {
			for (Field field : declaredFields) {
				CheckAttr checkAttr = field.getAnnotation(CheckAttr.class);
				if (checkAttr == null) {
					LOGGER.error(field.getName() + " getAnnotation Null!");
					continue;
				}

				field.setAccessible(true);
				Object value = field.get(newObj);

				if (checkAttr.required()
						&& CheckAttrValidator.isInvalidValue(value)) {

					return new CheckResult(ERROR_CODE_CHECK,
							checkAttr.message());
				}

				List<CheckAttrValidator.Checker> checkerLst = CheckAttrValidator
						.getCheckerLst();
				for (CheckAttrValidator.Checker checker : checkerLst) {
					CheckResult checkResult = checker.check(checkAttr, value);
					if (checkResult != null) {
						return checkResult;
					}
				}

				Object oldValue = (oldObj != null) ? field.get(oldObj) : null;

				CheckResult checkResult = checkCRUD(checkAttr, oldValue, value,
						oldObj == null);
				if (checkResult != null) {
					return checkResult;
				}
			}
		} catch (Exception e) {
			LOGGER.error("check", e);
			return new CheckResult(ERROR_CODE_CHECK, e.getMessage());
		}

		return null;
	}

	private static CheckResult checkCRUD(final CheckAttr checkAttr,
			final Object oldValue, final Object newValue, final boolean isNew) {
		CRUDType[] crud = checkAttr.crud();

		if (isNew && !isSupportCRUD(crud, CRUDType.C)
				&& !CheckAttrValidator.isInvalidValue(newValue)) {
			return new CheckResult(ERROR_CODE_CHECK, checkAttr.message());
		}
		if (!isNew && !isSupportCRUD(crud, CRUDType.U)) {
			if ((oldValue == null) && (newValue != null)) {
				return new CheckResult(ERROR_CODE_CHECK, checkAttr.message());
			}
			if ((oldValue != null) && (oldValue != newValue)) {
				return new CheckResult(ERROR_CODE_CHECK, checkAttr.message());
			}
		}

		return null;
	}

	private static boolean isSupportCRUD(final CRUDType[] crud,
			final CRUDType currentCRUD) {
		for (CRUDType crudType : crud) {
			if (crudType == currentCRUD) {
				return true;
			}
		}
		return false;
	}

	/**
	 * validate model
	 * 
	 * @param obj validate object
	 * @return If the check fails, an error message is returned, otherwise it
	 *         returns an empty string
	 * @throws ServiceException
	 * @since crossdomain 0.5
	 */
	public static String validateModel(final Object obj)
			throws ServiceException {
		if (obj == null) {
			return "";
		}
		return validate(obj);
	}

	/**
	 * validate model
	 * 
	 * @param objList validate the list of object
	 * @return If the check fails, an error message is returned, otherwise it
	 *         returns an empty string
	 * @throws ServiceException
	 * @since crossdomain 0.5
	 */
	@SuppressWarnings("rawtypes")
	public static String validateModel(final List objList)
			throws ServiceException {
		if (CollectionUtils.isEmpty(objList)) {
			return "";
		}

		StringBuffer buf = new StringBuffer();
		for (Object oneObj : objList) {
			buf.append(validate(oneObj));
		}

		return buf.toString();
	}

	private static String validate(final Object obj) throws ServiceException {
		String result = "";
		result = getValidator(obj);
		if (!result.isEmpty()) {
			LOGGER.error("Validation Error ! " + result);
			throw new ServiceException(ErrorCode.SD_PARAMETER_VALIDATE_ERROR,
					HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
		result = validateSubModel(obj);
		if (!result.isEmpty()) {
			LOGGER.error("Validation Error ! " + result);
			throw new ServiceException(ErrorCode.SD_PARAMETER_VALIDATE_ERROR,
					HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
		return result;
	}

	private static String validateSubModel(final Object obj)
			throws ServiceException {
		Field[] declaredFields = obj.getClass().getDeclaredFields();
		try {
			for (Field field : declaredFields) {

				if (!(field.isAnnotationPresent(CheckSubObj.class))) {
					continue;
				}

				// CheckSubObj checkSubOjb =
				// field.getAnnotation(CheckSubObj.class);
				// if (checkSubOjb == null)
				// {
				// continue;
				// }
				field.setAccessible(true);
				Object value = field.get(obj);
				validateModel(value);

				// if (!result.isEmpty())
				// {
				// return result;
				// }
			}
		} catch (Exception e) {
			LOGGER.error("validateSubModel", e);
			return e.toString();
		}
		return "";
	}

	private static String getValidator(final Object obj) {
		StringBuffer buffer = new StringBuffer();
		Validator validator = Validation.buildDefaultValidatorFactory()
				.getValidator();

		Set<ConstraintViolation<Object>> constraintViolations = validator
				.validate(obj);
		Iterator<ConstraintViolation<Object>> iter = constraintViolations
				.iterator();
		ConstraintViolation<Object> objIterator = null;
		while (iter.hasNext()) {
			objIterator = iter.next();
			buffer.append(objIterator.getPropertyPath().toString()).append(" ")
					.append(objIterator.getMessage()).append(", ");
		}

		return buffer.toString();
	}
}
