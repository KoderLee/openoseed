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

import org.openo.commonservice.log.OssLog;
import org.openo.commonservice.log.OssLogFactory;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

/**
 * ConstraintValidator implementation(CheckAttr)<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-18
 */
public class CheckAttrValidator implements ConstraintValidator<CheckAttr, Object> {

    public final static int ERROR_CODE_CHECK = 400;

    private final static OssLog logger = OssLogFactory.getLogger(ModelChecker.class);

    private CheckAttr checkAttr;

    @Override
    public void initialize(CheckAttr constraintAnnotation) {
        this.checkAttr = constraintAnnotation;
    }

	/**
     * Check Attribute Validation<br/>
     *
     * @param value Object to be checked
     * @param context ConstraintValidatorContext
     * @return the result of validation
     * @since crossdomain 0.5
     */
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        if(checkAttr.required()) {
            if(isInvalidValue(value)) {
                return false;
            }
        } else {
            if(!isInvalidValue(value)) {
                return false;
            }
        }

        List<Checker> checkerLst = getCheckerLst();
        for(Checker checker : checkerLst) {
            CheckResult checkResult = checker.check(checkAttr, value);
            if(checkResult != null) {
                return false;
            }
        }

        return true;
    }

	/**
     * Get Checker List<br/>
     * @return the checker list
     * @since crossdomain 0.5
     */	
    public List<Checker> getCheckerLst() {
        List<Checker> checkerLst = new ArrayList<Checker>(4);
        checkerLst.add(getStringChecker());

        return checkerLst;
    }

    private Checker getStringChecker() {
        return new Checker() {

            public CheckResult check(CheckAttr checkAttr, Object value) {

                if(checkAttr.type().equals(CheckType.String) && !isInvalidValue(value)) {
                    String str = (String)value;
                    if(!StringUtils.hasLength(str)) {
                        return null;
                    }

                    if(checkAttr.min() > 0 && str.length() < checkAttr.min()) {

                        return new CheckResult(ERROR_CODE_CHECK, checkAttr.message()
                                + "(The value must be greater than the minimum:" + checkAttr.min() + ")");
                    }
                    if(checkAttr.max() > 0 && str.length() > checkAttr.max()) {

                        return new CheckResult(ERROR_CODE_CHECK, checkAttr.message()
                                + "(The value must not exceed the maximum length:" + checkAttr.max() + ")");
                    }

                }
                return null;
            }
        };
    }

    /**
     * Check Attribute Value validation<br/>
     *
     * @param value Object to be checked
     * @return the result of validation
     * @since crossdomain 0.5
     */
    public boolean isInvalidValue(Object value) {
        if(value == null) {
            return !checkAttr.allowNull();
        }
        if(value instanceof String) {
            return value.equals("") && !checkAttr.allowNull();
        }
        return true;
    }

    /**
     * Checker API<br/>
     * @since crossdomain 0.5
     */
    public interface Checker {

        CheckResult check(CheckAttr checkAttr, Object value);
    }

}
