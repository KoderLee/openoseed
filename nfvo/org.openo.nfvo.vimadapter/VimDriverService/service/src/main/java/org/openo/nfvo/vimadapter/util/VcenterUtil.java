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

package org.openo.nfvo.vimadapter.util;

import java.util.HashMap;
import java.util.Map;

import org.openo.nfvo.vimadapter.util.constant.Constant;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
* Used for vmware connection basic operation.<br/>
* <p>
* </p>
* 
* @author
* @version NFVO 0.5 May 15, 2016
 */
public final class VcenterUtil {
    private VcenterUtil() {

    }

    public static boolean checkVcloudUrlFormat(String url) {
        return StringUtil.isboolIp(url);
    }

    public static boolean checkVcenterUrlFormat(String extraInfo) {
        JSONObject extraInfoJsonObject = JSONObject.fromObject(extraInfo);
        JSONArray vcenterList = extraInfoJsonObject.getJSONArray("vcenterInfo");
        int vcSize = vcenterList.size();
        JSONObject vcenterObj = null;

        for (int i = 0; i < vcSize; i++) {
            vcenterObj = vcenterList.getJSONObject(i);

            if (!StringUtil.isboolIp(vcenterObj.getString("url"))) {
                return false;
            }
        }
        return true;
    }

    public static String cryptVcenterPwd(String extraInfo, boolean enctypt) {
        JSONObject extraObj = JSONObject.fromObject(extraInfo);

        if (extraObj.containsKey("vcenterInfo")) {
            JSONArray vcList = extraObj.getJSONArray("vcenterInfo");
            int vcSize = vcList.size();
            JSONObject vcenterObj = null;

            for (int i = 0; i < vcSize; i++) {
                vcenterObj = vcList.getJSONObject(i);

                if (enctypt) {
                    vcenterObj.put("pwd",
                            CryptUtil.enCrypt(vcenterObj.getString("pwd")));
                } else {
                    vcenterObj.put("pwd",
                            CryptUtil.deCrypt(vcenterObj.getString("pwd")));
                }
            }

            return extraObj.toString();
        }

        else {
            return extraInfo;
        }
    }

    public static String setVcenterPwd(String extraInfo, String extra) {
        JSONObject extraObj = JSONObject.fromObject(extraInfo);

        Map<String, String> vcUrlMap = null;

        if (extraObj.containsKey("vcenterInfo")) {
            JSONArray vcList = extraObj.getJSONArray("vcenterInfo");
            int vcSize = vcList.size();
            JSONObject vcenterObj = null;
            String pwdTmp = null;

            for (int i = 0; i < vcSize; i++) {
                vcenterObj = vcList.getJSONObject(i);

                pwdTmp = vcenterObj.getString("pwd");
                if (("").equals(pwdTmp)) {
                    if (vcUrlMap == null) {
                        vcUrlMap = getVcenterUrlMap(extra);
                    }

                    vcenterObj.put("pwd",
                            vcUrlMap.get(vcenterObj.getString("url")));
                } else if (pwdTmp != null && !("").equals(pwdTmp)) {
                    vcenterObj.put("pwd", CryptUtil.enCrypt(pwdTmp));
                }

            }

            return extraObj.toString();

        }

        else {
            return extraInfo;
        }
    }

    public static Map<String, String> getVcenterUrlMap(String extraInfo) {
        Map<String, String> vcUrlMap = new HashMap<String, String>(
                Constant.DEFAULT_COLLECTION_SIZE);
        JSONObject extraObj = JSONObject.fromObject(extraInfo);
        JSONObject vcenterObj = null;
        JSONArray vcList = extraObj.getJSONArray("vcenterInfo");
        int vcSize = vcList.size();

        for (int i = 0; i < vcSize; i++) {
            vcenterObj = vcList.getJSONObject(i);

            vcUrlMap.put(vcenterObj.getString("url"),
                    vcenterObj.getString("pwd"));
        }

        return vcUrlMap;

    }

    public static String hideVcenterPwd(String extraInfo) {
        JSONObject extraObj = JSONObject.fromObject(extraInfo);

        if (extraObj.containsKey("vcenterInfo")) {
            JSONArray vcList = extraObj.getJSONArray("vcenterInfo");
            JSONObject vcenterObj = null;
            int vcSize = vcList.size();

            for (int i = 0; i < vcSize; i++) {
                vcenterObj = vcList.getJSONObject(i);
                if (vcenterObj.containsKey("pwd")) {
                    vcenterObj.remove("pwd");
                }
            }

            return extraObj.toString();
        }

        else {
            return extraInfo;
        }
    }

}
