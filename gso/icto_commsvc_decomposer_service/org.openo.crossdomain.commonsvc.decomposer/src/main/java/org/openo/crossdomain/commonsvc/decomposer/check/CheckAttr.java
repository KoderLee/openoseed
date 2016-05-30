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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 *  Indicates that the attribute of the validata element .
 * @since   crossdomain 0.5
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
@Constraint(validatedBy = CheckAttrValidator.class)
public @interface CheckAttr {

    /**
     * The message of verify failed.
     */
    String message() default "Verify failed";
    
    /**
     * Validate group.
     */
    Class<?>[] groups() default {};

    /**
     * Payload type that can be attached to a given
     * constraint declaration.
     * Payloads are typically used to carry on metadata information
     * consumed by a validation client.
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * Describe the type of  attribute.
     */
    CheckType type() default CheckType.String;
 
    /**
     * Describe whether the attribute be necessary.
     */
    boolean required() default false;

    /**
     * The min value.
     */
    int min() default 0;

    /**
     * The max value.
     */
    int max() default 0;

    /**
     * The rule of Validate.
     */
    String rule() default "";

    /**
     * Describe that the attribute can be create/Retrieve/Update/Delete
     */
    CRUDType[] crud() default {CRUDType.C, CRUDType.R, CRUDType.U};
 
    /**
     * The class of enum type.
     */
    Class<?> enumCls() default Class.class;

    /**
     * value scope.
     */
    String[] valueScop() default {};

    /**
     * User define checker.
     */
    Class<?> userDefineChecker() default Class.class;

}
