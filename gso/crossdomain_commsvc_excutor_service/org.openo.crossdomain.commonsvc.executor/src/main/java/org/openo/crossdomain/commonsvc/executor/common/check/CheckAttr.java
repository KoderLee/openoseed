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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Check Attribute API<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-18
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
@Constraint(validatedBy = CheckAttrValidator.class)
public @interface CheckAttr {

    String message() default "Verify failed";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    CheckType type() default CheckType.String;

    boolean required() default false;

    boolean allowNull() default false;

    int min() default 0;

    int max() default 0;

    CRUDType[] crud() default {CRUDType.C, CRUDType.R, CRUDType.U};

    Class enumCls() default Class.class;

    String[] valueScop() default {};

    Class userDefineChecker() default Class.class;

}
