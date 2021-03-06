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

package org.openo.nfvo.nsservice.simpleservice.database.dao.api;

import java.util.List;
import java.util.Map;

import org.openo.nfvo.nsservice.simpleservice.entity.NsService;

import org.openo.baseservice.remoteservice.exception.ServiceException;

/**
 * 
* Abstract class for NsService dao<br/>
* <p>
* </p>
* 
* @author
* @version NFVO 0.5 May 15, 2016
 */
public interface NsServiceDao {

    int insertNsService(NsService nsBean) throws ServiceException;

    int deleteNsService(String id) throws ServiceException;

    int updateNsService(NsService nsBean) throws ServiceException;

    List<NsService> getNsServiceByServiceId(Map<String, Object> condition);

    NsService getNsServiceByPrimaryKey(String id);

}
