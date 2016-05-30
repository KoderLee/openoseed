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
package org.openo.sdno.cbb.sdnwan.util.rest;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import org.openo.sdno.remoteservice.exception.ExceptionArgs;
import org.openo.sdno.remoteservice.exception.ServiceException;
import org.openo.sdno.roa.common.RequestInputStream;

public class RoaInputStreamParser {

    private RoaInputStreamParser() {
    }

    /**
     * Convert a JSON in an input stream to a Object.<br/>
     * 
     * @param input Stream input of the JSON string
     * @param clazz Class reference object for the Object translation
     * @return Object of clazz type
     * @throws ServiceException throws exception when the Stream input of the JSON is not properly
     *             formed
     * @since SDNO 0.5
     */
    public static <T> T fromJson(RequestInputStream input, Class<T> clazz) throws ServiceException {
        if((input == null) || (clazz == null)) {
            throw new IllegalArgumentException("input or clazz is null");
        }

        return formJsonStr(input.getBodyStr(), clazz);
    }

    private static <T> T formJsonStr(String jsonStr, Class<T> clazz) throws ServiceException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            return mapper.readValue(jsonStr, clazz);
        } catch(JsonParseException e) {
            ServiceException serviceException = new ServiceException(e);
            serviceException.setHttpCode(400);
            serviceException.setExceptionArgs(new ExceptionArgs(new String[] {e.getMessage()}, null, null, null));
            throw serviceException;
        } catch(JsonMappingException e) {
            ServiceException serviceException = new ServiceException(e);
            serviceException.setHttpCode(400);
            serviceException.setExceptionArgs(new ExceptionArgs(new String[] {e.getMessage()}, null, null, null));
            throw serviceException;
        } catch(IOException e) {
            ServiceException serviceException = new ServiceException(e);
            serviceException.setHttpCode(400);
            serviceException.setExceptionArgs(new ExceptionArgs(new String[] {e.getMessage()}, null, null, null));
            throw serviceException;
        }
    }
}
