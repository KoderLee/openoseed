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
package org.openo.crossdomain.servicemgr.util.json;

import java.io.IOException;

import net.sf.json.JSON;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.type.TypeReference;

/**
 * Tools for operating json content.<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
public class JsonUtil {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {

        MAPPER.setDeserializationConfig(MAPPER.getDeserializationConfig().without(
                org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES));
        //
        MAPPER.setSerializationInclusion(Inclusion.NON_NULL);
    }

    /**
     * Convert Json to some type.<br/>
     *
     * @param jsonstr json content
     * @param type model type
     * @return model data
     * @throws IOException
     * @since crossdomain 0.5
     */
    public static <T> T unMarshal(String jsonstr, Class<T> type) throws IOException {
        return MAPPER.readValue(jsonstr, type);
    }

    /**
     * Convert Json to some type.<br/>
     *
     * @param jsonstr json content
     * @param type model type
     * @return model data
     * @throws IOException
     * @since crossdomain 0.5
     */
    public static <T> T unMarshal(String jsonstr, TypeReference<T> type) throws IOException {
        return MAPPER.readValue(jsonstr, type);
    }

    /**
     * Convert object to json String.<br/>
     *
     * @param srcObj service object
     * @return json string
     * @throws IOException
     * @since crossdomain 0.5
     */
    public static String marshal(Object srcObj) throws IOException {
        if(srcObj instanceof JSON) {
            return srcObj.toString();
        }
        return MAPPER.writeValueAsString(srcObj);
    }

    /**
     * Get instance MAPPER.<br/>
     *
     * @return MAPPER instance
     * @since crossdomain 0.5
     */
    public static ObjectMapper getMapper() {
        return MAPPER;
    }
}
