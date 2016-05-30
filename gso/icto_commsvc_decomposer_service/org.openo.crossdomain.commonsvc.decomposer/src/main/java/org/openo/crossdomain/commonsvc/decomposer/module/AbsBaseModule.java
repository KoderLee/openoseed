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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * abstract base Module, provider module start/stop service
 * 
 * @since crossdomain 0.5
 */
public abstract class AbsBaseModule {

	/**
	 * logger
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(AbsBaseModule.class);

	/**
	 * service start action
	 * 
	 * @since crossdomain 0.5
	 */
	public final void start() {
		try {
			doStart();
			init();
		} catch (Exception e) {
			logger.error("module start error", e);
		}
	}

	/**
	 * service stop action
	 * 
	 * @since crossdomain 0.5
	 */
	public final void stop() {
		try {
			doStop();
			destroy();
		} catch (Exception e) {
			logger.error("module start stop", e);
		}
	}

	/**
	 * do start action
	 * 
	 * @since crossdomain 0.5
	 */
	protected void doStart() {
	}

	/**
	 * do stop action
	 * 
	 * @since crossdomain 0.5
	 */
	protected void doStop() {
	}

	/**
	 * initialize method
	 * 
	 * @since crossdomain 0.5
	 */
	protected abstract void init();

	/**
	 * destroy method
	 * 
	 * @since crossdomain 0.5
	 */
	protected abstract void destroy();

}
