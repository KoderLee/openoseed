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
package org.openo.crossdomain.commonsvc.decomposer.rest.util;

import org.openo.commonservice.remoteservice.exception.ServiceException;
import org.openo.commonservice.roa.util.restclient.RestfulFactory;
import org.openo.commonservice.roa.util.restclient.RestfulOptions;
import org.openo.commonservice.roa.util.restclient.RestfulParametes;
import org.openo.commonservice.roa.util.restclient.RestfulResponse;

/**
 * Restful Method
 * 
 * @since crossdomain 0.5
 */
public enum RestfulMethod {
	GET {

		@Override
		public RestfulResponse method(String uri, RestfulParametes restParametes)
				throws ServiceException {
			return RestfulFactory.getRestInstance().get(uri, restParametes);
		}

		@Override
		public RestfulResponse method(String uri,
				RestfulParametes restParametes, RestfulOptions restOptions)
				throws ServiceException {
			return RestfulFactory.getRestInstance().get(uri, restParametes,
					restOptions);
		}
	},

	POST {

		@Override
		public RestfulResponse method(String uri, RestfulParametes restParametes)
				throws ServiceException {
			return RestfulFactory.getRestInstance().post(uri, restParametes);
		}

		@Override
		public RestfulResponse method(String uri,
				RestfulParametes restParametes, RestfulOptions restOptions)
				throws ServiceException {
			return RestfulFactory.getRestInstance().post(uri, restParametes,
					restOptions);
		}
	},

	PUT {

		@Override
		public RestfulResponse method(String uri, RestfulParametes restParametes)
				throws ServiceException {
			return RestfulFactory.getRestInstance().put(uri, restParametes);
		}

		@Override
		public RestfulResponse method(String uri,
				RestfulParametes restParametes, RestfulOptions restOptions)
				throws ServiceException {
			return RestfulFactory.getRestInstance().put(uri, restParametes,
					restOptions);
		}
	},

	DELETE {

		@Override
		public RestfulResponse method(String uri, RestfulParametes restParametes)
				throws ServiceException {
			return RestfulFactory.getRestInstance().delete(uri, restParametes);
		}

		@Override
		public RestfulResponse method(String uri,
				RestfulParametes restParametes, RestfulOptions restOptions)
				throws ServiceException {
			return RestfulFactory.getRestInstance().delete(uri, restParametes,
					restOptions);
		}
	};

	public abstract RestfulResponse method(String uri,
			RestfulParametes restParametes) throws ServiceException;

	public abstract RestfulResponse method(String uri,
			RestfulParametes restParametes, RestfulOptions restOptions)
			throws ServiceException;
}
