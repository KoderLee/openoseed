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

import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.util.StringUtils;

import org.openo.commonservice.log.OssLog;
import org.openo.commonservice.log.OssLogFactory;

/**
 * Defines the logic to validate a given constraint CheckAttr for a given
 * object.
 * 
 * @since crossdomain 0.5
 */
public class CheckAttrValidator implements
		ConstraintValidator<CheckAttr, Object> {

	/**
	 * validate attribute
	 */
	private CheckAttr checkAttr;

	/**
	 * logger
	 */
	private final static OssLog LOGGER = OssLogFactory
			.getLogger(ModelChecker.class);

	/**
	 * error code
	 */
	public final static int ERROR_CODE_CHECK = 400;

	/**
	 * initialize method
	 * 
	 * @param constraintAnnotation validate attribute
	 * @since crossdomain 0.5
	 */
	@Override
	public void initialize(final CheckAttr constraintAnnotation) {
		this.checkAttr = constraintAnnotation;
	}

	/**
	 * validate attribute is valid or not
	 * 
	 * @param value value
	 * @param context Validator Context
	 * @return valid or not
	 * @since crossdomain 0.5
	 */
	@Override
	public boolean isValid(final Object value,
			final ConstraintValidatorContext context) {

		if (checkAttr.required()) {
			if (isInvalidValue(value)) {
				return false;
			}
		} else {

			if (value == null) {
				return true;
			}
			if (value instanceof String && value.toString().equals("")) {
				return true;
			}
		}

		List<Checker> checkerLst = getCheckerLst();
		for (Checker checker : checkerLst) {
			CheckResult checkResult = checker.check(checkAttr, value);
			if (checkResult != null) {
				return false;
			}
		}
		return true;
	}

	/**
	 * get the checker list
	 * 
	 * @return check list
	 * @since crossdomain 0.5
	 */
	public static List<Checker> getCheckerLst() {
		List<Checker> checkerLst = new ArrayList<Checker>(4);
		checkerLst.add(getEnumChecker());
		checkerLst.add(getIntChecker());
		checkerLst.add(getStringChecker());
		checkerLst.add(getDateChecker());

		return checkerLst;
	}

	private static Checker getStringChecker() {
		return new Checker() {

			@Override
			public CheckResult check(final CheckAttr checkAttr,
					final Object value) {

				if ((checkAttr.type().equals(CheckType.String))
						&& !isInvalidValue(value)) {
					String str = (String) value;
					if (checkAttr.min() > 0 && str.length() < checkAttr.min()) {

						return new CheckResult(
								ERROR_CODE_CHECK,
								checkAttr.message()
										+ "(The value must be greater than the minimum:"
										+ checkAttr.min() + ")");
					}
					if (checkAttr.max() > 0 && str.length() > checkAttr.max()) {

						return new CheckResult(
								ERROR_CODE_CHECK,
								checkAttr.message()
										+ "(The value must not exceed the maximum length:"
										+ checkAttr.max() + ")");
					}
					if (!checkRule(checkAttr.rule(), value.toString())) {

						return new CheckResult(ERROR_CODE_CHECK,
								checkAttr.message()
										+ "(The value does not match rule: "
										+ checkAttr.rule() + ")");
					}
				}

				return null;
			}
		};
	}

	private static boolean checkRule(final String rule, final String value) {
		if (!StringUtils.hasLength(rule)) {

			return true;
		}
		return value.matches(rule);
	}

	private static Checker getEnumChecker() {
		return new Checker() {

			@Override
			public CheckResult check(final CheckAttr checkAttr,
					final Object value) {

				if (checkAttr.type().equals(CheckType.Enum)) {

					Set<String> enumValueSet = new HashSet<String>();
					try {
						enumValueSet = getEnumValueSet(checkAttr);
					} catch (Exception e) {
						LOGGER.error(
								"Enum type check error! value is:"
										+ value.toString(), e);
					}
					if (!enumValueSet.contains(value.toString())) {

						return new CheckResult(ERROR_CODE_CHECK,
								checkAttr.message()
										+ "(Not in the valid range:"
										+ enumValueSet.toString() + ")");
					}
				}

				return null;
			}
		};
	}

	private static Checker getIntChecker() {
		return new Checker() {

			@Override
			public CheckResult check(final CheckAttr checkAttr,
					final Object value) {

				if ((checkAttr.type().equals(CheckType.Int))
						&& !isInvalidValue(value)) {
					int intValue = (Integer) value;
					if (checkAttr.min() > 0 && intValue < checkAttr.min()) {

						return new CheckResult(
								ERROR_CODE_CHECK,
								checkAttr.message()
										+ "(The value must be greater than the minimum:"
										+ checkAttr.min() + ")");
					}
					if (checkAttr.max() > 0 && intValue > checkAttr.max()) {

						return new CheckResult(
								ERROR_CODE_CHECK,
								checkAttr.message()
										+ "(The value must not exceed the maximum length:"
										+ checkAttr.max() + ")");
					}
				}

				return null;
			}
		};

	}

	private static Checker getDateChecker() {
		return new Checker() {

			@Override
			public CheckResult check(final CheckAttr checkAttr,
					final Object value) {
				if ((checkAttr.type().equals(CheckType.Date))
						&& !isInvalidValue(value)) {
					String date = (String) value;

					String strPatten = "\\d{4}-\\d{2}-\\d{2} (20|21|22|23|[0-1][0-9]):[0-5][0-9]:[0-5][0-9]$";
					Pattern myPattern = Pattern.compile(strPatten);
					Matcher myMatch = myPattern.matcher(date);
					if (!myMatch.matches()) {
						return new CheckResult(ERROR_CODE_CHECK,
								checkAttr.message());
					}

					SimpleDateFormat myFormat = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					try {
						Boolean isValid = myFormat.format(myFormat.parse(date))
								.equals(date);
						if (!isValid) {
							return new CheckResult(
									ERROR_CODE_CHECK,
									checkAttr.message()
											+ "(It is not a valid time format,format:yyyy-MM-dd HH:mm:ss)");
						}

					} catch (ParseException e) {
						return new CheckResult(ERROR_CODE_CHECK,
								checkAttr.message());
					}
				}

				return null;
			}
		};
	}

	@SuppressWarnings("rawtypes")
	private static Set<String> getEnumValueSet(final CheckAttr checkAttr)
			throws Exception {
		Set<String> valueSet = new HashSet<String>();
		Class<?> cls = checkAttr.enumCls();
		if (!checkAttr.enumCls().equals(Class.class)) {
			Method method = cls.getMethod("values");
			Enum inter[] = (Enum[]) method.invoke(null);
			for (Enum enumMessage : inter) {
				Method met = enumMessage.getClass().getMethod("getName");
				String name = (String) met.invoke(enumMessage);
				valueSet.add(name);
			}
		} else {
			String[] valueScop = checkAttr.valueScop();
			for (String value : valueScop) {
				valueSet.add(value);
			}
		}

		return valueSet;
	}

	/**
	 * validate value is valid or not
	 * 
	 * @param value validate value
	 * @return valid or not
	 * @since crossdomain 0.5
	 */
	public static boolean isInvalidValue(final Object value) {
		if (value == null) {
			return true;
		}
		if (value instanceof String) {
			return value.toString().equals("");
		}
		if (value instanceof Integer) {
			return ((Integer) value) <= 0;
		}

		return true;
	}

	/**
	 * the interface of checker
	 * 
	 * @since crossdomain 0.5
	 */
	public interface Checker {

		CheckResult check(CheckAttr checkAttr, Object value);
	}
}
