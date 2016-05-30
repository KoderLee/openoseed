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

package org.openo.nfvo.nsservice.simpleservice.register;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openo.nfvo.nsservice.api.IregisterIrManager;
import org.openo.nfvo.nsservice.api.RestfulUtil;
import org.openo.nfvo.nsservice.simpleservice.constant.Constant;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.openo.baseservice.log.OssLog;
import org.openo.baseservice.log.OssLogFactory;

/**
 * 
* The implement class that define the parameter used to register NFV Service for CrossDomain Module <br/>
* <p>
* </p>
* 
* @author
* @version NFVO 0.5 May 15, 2016
 */
public class AdapterRegisterIrManager implements IregisterIrManager {

    private static final OssLog LOG = OssLogFactory.getLogger(AdapterRegisterIrManager.class);

    private static final String REG_SERVICE_URL = "/rest/executor/v1/rules/register";

    private static final String TYPENAME_VNF = "NSD.node.VNF";

    private static final String TYPENAME_NETWORK = "NSD.net.NP";

    private static final String TYPENAME_NS = "NSD.node.NS";

    private static final String VERSION = "v1";

    private static final String URIPREFIX_VNF = "nfv/v1/vnf";

    private static final String URIPREFIX_NS = "nfv/v1/ns";

    private static final String URIPREFIX_NETWORK = "nfv/v1/network";

    private static final String OPERTYPE_VNF = "create|delete|update";

    private static final String OPERTYPE_NETWORK = "create|delete|update";

    private static final String OPERTYPE_NS = "create|delete|update";

    @Override
    public int regService() {
        int result = Constant.REST_FAIL;
        try {

            LOG.info("AdapterRegisterIrManager function=regService ...");

            Map<String, String> vnfRuleMap = new HashMap<String, String>(6);
            vnfRuleMap.put("typeName", TYPENAME_VNF);
            vnfRuleMap.put("version", VERSION);
            vnfRuleMap.put("oper_type", OPERTYPE_VNF);
            vnfRuleMap.put("uri_prefix", URIPREFIX_VNF);

            Map<String, String> netRuleMap = new HashMap<String, String>(6);
            netRuleMap.put("typeName", TYPENAME_NETWORK);
            netRuleMap.put("version", VERSION);
            netRuleMap.put("oper_type", OPERTYPE_NETWORK);
            netRuleMap.put("uri_prefix", URIPREFIX_NETWORK);

            Map<String, String> nsRuleMap = new HashMap<String, String>(6);
            nsRuleMap.put("typeName", TYPENAME_NS);
            nsRuleMap.put("version", VERSION);
            nsRuleMap.put("oper_type", OPERTYPE_NS);
            nsRuleMap.put("uri_prefix", URIPREFIX_NS);

            List<Map<String, String>> ruleFormations = new ArrayList<Map<String, String>>(10);
            ruleFormations.add(vnfRuleMap);
            ruleFormations.add(netRuleMap);
            ruleFormations.add(nsRuleMap);
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("rules", ruleFormations);

            LOG.info("register service msg=" + jsonParam.toString());
            JSONObject respJson = RestfulUtil.sendReqToApp(REG_SERVICE_URL, Constant.PUT, jsonParam, null);
            LOG.info("register service retmsg=" + respJson.toString());
            if(Constant.SUCCESS.equalsIgnoreCase(respJson.getString("result"))) {
                LOG.info("register service success");
                result = Constant.REST_SUCCESS;
            } else {
                LOG.info("register service failed");
                result = Constant.REST_FAIL;
            }
            LOG.info("register service success ...");
        } catch(JSONException e) {
            LOG.error("function=regService,msg=JSONException:{}", e);
            result = Constant.REST_FAIL;
        }
        return result;
    }
}
