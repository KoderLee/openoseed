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
package org.openo.crossdomain.commonsvc.decomposer.module;

import java.util.ArrayList;
import java.util.List;

import org.openo.crossdomain.commonsvc.decomposer.constant.ConstantURL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.openo.commonservice.roa.ROA;
import org.openo.commonservice.roa.exception.DuplicateServiceException;
import org.openo.commonservice.roa.exception.IllegalServiceException;
import org.openo.commonservice.roa.exception.NotExistServiceException;
import com.huawei.icto.commsvc.authorization.filter.AuthFilter;

/**
 * abstract service module
 * 
 * @since crossdomain 0.5
 */
public abstract class AbsServiceModule extends AbsBaseModule {

	/**
	 * logger
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(AbsServiceModule.class);

	/**
	 * ROA interface list
	 */
	private List<AbsResource<?>> roaResList = new ArrayList<AbsResource<?>>();

	public final void setRoaResList(final List<AbsResource<?>> roaResList) {
		this.roaResList = roaResList;
	}

	@Override
	protected void doStart() {
		super.doStart();
		registerRoaResource();
	}

	@Override
	protected void doStop() {
		super.doStop();
		unRegisterRoaResource();
	}

	/**
*
*/
	protected final void registerRoaResource() {
		for (AbsResource<?> res : roaResList) {
			if (res != null) {
				res.getClass().getName();
				try {
					ROA.addRestfulService(res);
					logger.info("ROA.addRestfulService = " + res.getResUri());
				} catch (IllegalServiceException e) {
					logger.error("ROA.addRestfulService illegal:"
							+ res.getClass().getName());
				} catch (DuplicateServiceException e) {
					logger.error("ROA.addRestfulService duplicate:"
							+ res.getClass().getName());
				}
			}
		}

		logger.info("decomposer init success!");
		ROA.addFilter("/rest", new AuthFilter(), ConstantURL.DECOMPOSER_PREFIX
				+ "/*", 1);
		logger.info("decomposer register autofilter finish!");
	}

	/**
*
*/
	protected final void unRegisterRoaResource() {
		for (AbsResource<?> res : roaResList) {
			if (res != null) {
				try {
					ROA.removeRestfulService(res);
					logger.info("ROA.removeRestfulService = " + res.getResUri());
				} catch (IllegalServiceException e) {
					logger.error("ROA.removeRestfulService illegal:"
							+ res.getClass().getName());
				} catch (NotExistServiceException e) {
					logger.error("ROA.removeRestfulService not exist:"
							+ res.getClass().getName());
				}
			}
		}
	}
}
