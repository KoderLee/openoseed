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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.openo.nfvo.vimadapter.util.constant.Constant;

/**
 * 
* Define the basic vim info for NFVO connect the vim<br/>
* <p>
* </p>
* 
* @author
* @version NFVO 0.5 May 15, 2016
 */
public class Vim {

    private String id;

    private String name;

    private String type;

    private String version;

    private String userName;

    private String pwd;

    private String url;

    private String extraInfo;

    private String status;

    private Date createAt;

    private Date updateAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getUrl() {
        return url;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = (Date) createAt.clone();
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = (Date) updateAt.clone();
    }

    public Vim() {
        //constructor
    }

    public Vim(String id, JSONObject vimJsonObject, String status) {
        this.id = id;
        this.name = vimJsonObject.getString("name");
        this.type = vimJsonObject.getString("type");
        this.version = vimJsonObject.getString("version");
        this.userName = vimJsonObject.getString("userName");
        this.pwd = vimJsonObject.getString("pwd");
        this.url = vimJsonObject.getString("url");
        this.extraInfo = vimJsonObject.getJSONObject("extraInfo").toString();
        this.status = status;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this,
                ToStringStyle.SHORT_PREFIX_STYLE);

    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Vim)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Vim other = (Vim) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }

    public Map<String, String> generateConMap(JSONObject paramJson) {
        Map<String, String> conMap = new HashMap<String, String>(
                Constant.DEFAULT_COLLECTION_SIZE);
        conMap.put("url", url);
        conMap.put("userName", userName);
        conMap.put("userPwd", CryptUtil.deCrypt(pwd));
        conMap.put("extraInfo", extraInfo);
        conMap.put("vimId", id);
        conMap.put("vimName", name);
        conMap.put("queryId",
                (paramJson == null || paramJson.get("id") == null) ? null
                        : paramJson.getString("id"));
        return conMap;
    }

    public Map<String, String> genVimResultMap() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
        Map<String, String> resultMap = new HashMap<String, String>(
                Constant.DEFAULT_COLLECTION_SIZE);
        resultMap.put("id", id);
        resultMap.put("name", name);
        resultMap.put("userName", userName);
        resultMap.put("type", type);
        resultMap.put("version", version);
        resultMap.put("url", url);
        resultMap.put("status", status);
        resultMap.put("extraInfo", extraInfo);
        resultMap.put("createAt",
                createAt == null ? "" : dateFormat.format(createAt));
        resultMap.put("updateAt",
                updateAt == null ? "" : dateFormat.format(updateAt));
        return resultMap;
    }

    public void setVimBean(Vim argVim) {
        if (argVim != null) {
            if (argVim.getName() != null) {
                name = argVim.getName();
            }
            if (argVim.getUserName() != null) {
                userName = argVim.getUserName();
            }
            if (argVim.getPwd() != null) {
                pwd = CryptUtil.enCrypt(argVim.getPwd());
            }
            if (argVim.getExtraInfo() != null) {
                extraInfo = VcenterUtil.setVcenterPwd(argVim.getExtraInfo(),
                        extraInfo);
            }
        }
    }

    public void setModVim(JSONObject vimJsonObject, String id) {
        if (vimJsonObject.containsKey("name")) {
            this.name = vimJsonObject.getString("name");
        }
        if (vimJsonObject.containsKey("userName")) {
            this.userName = vimJsonObject.getString("userName");
        }
        if (vimJsonObject.containsKey("pwd")) {
            this.pwd = vimJsonObject.getString("pwd");
        }
        if (vimJsonObject.containsKey("extraInfo")) {
            this.extraInfo = vimJsonObject.getJSONObject("extraInfo")
                    .toString();
        }
        this.id = id;
    }
}
