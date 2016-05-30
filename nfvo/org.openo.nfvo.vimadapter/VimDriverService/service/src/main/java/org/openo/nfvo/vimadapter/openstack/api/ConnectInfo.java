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

package org.openo.nfvo.vimadapter.openstack.api;

import java.util.Map;

import net.sf.json.JSONObject;

import org.openo.nfvo.vimadapter.util.Vim;
import org.openo.nfvo.vimadapter.util.constant.Constant;

/**
 * 
* The entity of connection information for common use<br/>
* <p>
* </p>
* 
* @author
* @version NFVO 0.5 May 15, 2016
 */
public class ConnectInfo {
    private String url;

    private String domainName;

    private String userName;

    private String userPwd;

    private String authenticateMode;

    public ConnectInfo() {
        //constructor
    }

    public ConnectInfo(Map<String, String> conMap) {
        String extraInfo = conMap.get("extraInfo");
        JSONObject extraInfoJsonObject = JSONObject.fromObject(extraInfo);
        this.domainName = extraInfoJsonObject.get("domain") == null ? ""
                : extraInfoJsonObject.getString("domain");
        this.authenticateMode = extraInfoJsonObject.get("authenticMode") == null ? Constant.AuthenticationMode.ANONYMOUS
                : extraInfoJsonObject.getString("authenticMode");
        this.url = conMap.get("url") == null ? "" : conMap.get("url");
        this.userName = conMap.get("userName") == null ? "" : conMap
                .get("userName");
        this.userPwd = conMap.get("userPwd") == null ? "" : conMap
                .get("userPwd");
    }

    public ConnectInfo(Vim vim) {
        JSONObject extraInfoJsonObject = JSONObject.fromObject(vim
                .getExtraInfo());

        this.url = vim.getUrl() == null ? "" : vim.getUrl();
        this.domainName = extraInfoJsonObject.get("domain") == null ? ""
                : extraInfoJsonObject.getString("domain");
        this.userName = vim.getUserName() == null ? "" : vim.getUserName();
        this.userPwd = vim.getPwd() == null ? "" : vim.getPwd();
        this.authenticateMode = extraInfoJsonObject.get("authenticMode") == null ? Constant.AuthenticationMode.ANONYMOUS
                : extraInfoJsonObject.getString("authenticMode");
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((url == null) ? 0 : url.hashCode());
        return result;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public void setAuthenticateMode(String authenticateMode) {
        this.authenticateMode = authenticateMode;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof ConnectInfo)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ConnectInfo other = (ConnectInfo) obj;
        if (url == null) {
            if (other.url != null) {
                return false;
            }
        } else if (!url.equals(other.url)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ConnectInfo [AuthenticateMode: " + authenticateMode + ",url="
                + url + ", domainName=" + domainName + ", userName=" + userName
                + ']';
    }

    public ConnectInfo generateConByMap(Map<String, String> conMap) {
        return new ConnectInfo(conMap);
    }

    public String getUrl() {
        return url;
    }

    public String getDomainName() {
        return domainName;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public String getAuthenticateMode() {
        return authenticateMode;
    }

    public boolean isNeedRenewInfo(ConnectInfo info) {
        return !(url.equals(info.getUrl())
                && domainName.equals(info.getDomainName())
                && userName.equals(info.getUserName())
                && userPwd.equals(info.getUserPwd()) && authenticateMode
                    .equals(info.getAuthenticateMode()));
    }

}
