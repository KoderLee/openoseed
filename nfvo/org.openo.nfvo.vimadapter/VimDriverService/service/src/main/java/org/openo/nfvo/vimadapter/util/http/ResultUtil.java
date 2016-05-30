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

package org.openo.nfvo.vimadapter.util.http;

import java.io.UnsupportedEncodingException;
import java.util.List;

import net.sf.json.JSONArray;

import org.openo.baseservice.log.OssLog;
import org.openo.baseservice.log.OssLogFactory;

import org.openo.nfvo.vimadapter.util.JsonUtil;
import org.openo.nfvo.vimadapter.util.constant.Constant;

/**
 * 
* User for format the result<br/>
* <p>
* </p>
* 
* @author
* @version NFVO 0.5 May 15, 2016
 */
public final class ResultUtil {
    private static final OssLog LOG = OssLogFactory.getLogger(ResultUtil.class);

    private ResultUtil() {

    }

    /**
     * Convert result to list
     */
    public static <T> List<T> result2List(String shareName, String result,
            Class<T> pojoClass) {
        if (!judgeShareResult(shareName, result)) {
            return null;
        }

        JSONArray jsonArray = JSONArray.fromObject(result);
        if (jsonArray.isEmpty()) {
            LOG.error("get " + shareName + " share isEmpty .");
            return null;
        } else {
            return JsonUtil.jsonToList(result, pojoClass);
        }
    }

    private static boolean judgeShareResult(String shareName, String result) {
        if (null == result) {
            LOG.error("QueryUsageTask | get {} share result is null.",
                    shareName);
            return false;
        } else if (result.isEmpty()) {
            LOG.error("QueryUsageTask | get {} share result is empty.",
                    shareName);
            return false;
        }

        if (!(result.charAt(0) == '[')) {
            LOG.error("get " + shareName
                    + " share response body={}, format error.", result);
            return false;
        }
        return true;
    }

    public static String getResponseBody(byte[] responseBody)
            throws UnsupportedEncodingException {
        String result = null;
        if (responseBody != null && responseBody.length > 0) {
            result = new String(responseBody, Constant.ENCODEING);
        }
        return result;
    }
}