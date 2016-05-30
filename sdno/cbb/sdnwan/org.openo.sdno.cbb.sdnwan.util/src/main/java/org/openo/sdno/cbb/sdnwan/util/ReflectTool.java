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
package org.openo.sdno.cbb.sdnwan.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.BeanUtils;

import org.openo.sdno.remoteservice.exception.ServiceException;

/**
 * <br/>
 * <p>
 * Utility class over the java reflection to read values from Class without making its object
 * </p>
 * 
 * @author
 * @version SDNO 0.5 16-Mar-2016
 */
public class ReflectTool {

    private static Object[] EMPTY_ARR = new Object[] {};

    private ReflectTool() {

    }

    /**
     * <br/>
     * <p>
     * Returns an array of Field objects reflecting all the fields declared by the class.
     * </p>
     * 
     * @param clazz The Class object to be operated on
     * @return the array of Field objects representing all the declared fields of this class
     * @since SDNO 0.5
     */
    public static Field[] getAllFields(final Class<?> clazz) {
        final List<Field> fields = new ArrayList<Field>();
        Class<?> superClazz = clazz.getSuperclass();
        while(superClazz != null) {
            fields.addAll(Arrays.asList(superClazz.getDeclaredFields()));
            superClazz = superClazz.getSuperclass();
        }
        fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        return fields.toArray(new Field[fields.size()]);
    }

    /**
     * <br/>
     * <p>
     * Invokes the underlying method represented by the Method object on the specified target object
     * with the specified fieldName.
     * </p>
     * 
     * @param target the object who class type is used to get the property descriptor
     * @param fieldName the filed name used to get the property descriptor
     * @return the result of dispatching the underlying method
     * @throws ServiceException throws exception if the input is invalid
     * @since SDNO 0.5
     */
    public static Object readVal(final Object target, final String fieldName) throws ServiceException {
        final PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(target.getClass(), fieldName);
        if(pd == null) {
            return null;
        }

        final Method readMethod = pd.getReadMethod();
        if(readMethod == null) {
            throw new ServiceException(fieldName + " has no read method");
        }
        try {
            return readMethod.invoke(target, EMPTY_ARR);
        } catch(InvocationTargetException e) {
            return null;
        } catch(IllegalAccessException e) {
            return null;
        } catch(IllegalArgumentException e) {
            return null;
        }
    }
}
