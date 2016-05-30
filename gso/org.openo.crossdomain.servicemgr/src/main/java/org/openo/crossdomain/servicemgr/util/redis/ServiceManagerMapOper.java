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
package org.openo.crossdomain.servicemgr.util.redis;

import org.openo.baseservice.redis.oper.MapOper;
import org.openo.baseservice.redis.serializer.BeanSerializer;

/**
 *  Service Manager mapping operation.<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
public class ServiceManagerMapOper<T> extends MapOper<T> {

	/**
	 * Constructor.<br/>
	 * @param storeDomain store domain
	 * @param redisDb redis database
	 */
    public ServiceManagerMapOper(String storeDomain, String redisDb) {
        super(storeDomain, redisDb);
    }

    /**
     * Put data into nginx.<br/>
     *
     * @param redisKey redis key
     * @param mapKey mapping key
     * @param value service data
     * @since crossdomain 0.5
     */
    public void putnx(String redisKey, String mapKey, T value) {
        String storeKey = getStoreKey(redisKey);
        BeanSerializer serializer = null;
        serializer = getSerializer(value.getClass());
        redis.hsetnx(storeKey, mapKey, serializer.stringLize(value));
    }
}
