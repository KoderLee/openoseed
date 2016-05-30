/*
 * Copyright (c) 2016, Huawei Technologies Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *  
 */
package org.openo.sdno.vpn.wan.servicemodel.composedvpn;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.puer.framework.container.util.UUIDUtils;

public class ConnectionLink extends BaseTopoVo {

    private String fromId;

    private String fromNeName;

    private String fromTpId = "";

    private String fromTpName = "";

    private String toId;

    private String toNeName;

    private String toTpId = "";

    private String toTpName = "";

    private String linkType;

    private String customData;

    public ConnectionLink(String fromId, String fromNeName, String toId, String toNeName) {
        super();
        this.id = UUIDUtils.createUuid();
        this.fromId = fromId;
        this.fromNeName = fromNeName;
        this.toId = toId;
        this.toNeName = toNeName;
    }

    public ConnectionLink() {
        super();
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }

    public String getLinkType() {
        return linkType;
    }

    public void setLinkType(String linkType) {
        this.linkType = linkType;
    }

    public String getCustomData() {
        return customData;
    }

    public void setCustomData(String customData) {
        this.customData = customData;
    }

    public final String getFromNeName() {
        return fromNeName;
    }

    public final void setFromNeName(String fromNeName) {
        this.fromNeName = fromNeName;
    }

    public final String getToNeName() {
        return toNeName;
    }

    public final void setToNeName(String toNeName) {
        this.toNeName = toNeName;
    }

    public final String getFromTpId() {
        return fromTpId;
    }

    public final void setFromTpId(String fromTpId) {
        this.fromTpId = fromTpId;
    }

    public final String getFromTpName() {
        return fromTpName;
    }

    public final void setFromTpName(String fromTpName) {
        this.fromTpName = fromTpName;
    }

    public final String getToTpId() {
        return toTpId;
    }

    public final void setToTpId(String toTpId) {
        this.toTpId = toTpId;
    }

    public final String getToTpName() {
        return toTpName;
    }

    public final void setToTpName(String toTpName) {
        this.toTpName = toTpName;
    }

    @JsonIgnore
    public String findLinkId(List<ConnectionLink> linkList) {
        for(ConnectionLink link : linkList) {
            if(this.equals(link)) {
                return link.id;
            }
        }
        return null;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((fromId == null) ? 0 : fromId.hashCode());
        result = (prime * result) + ((fromNeName == null) ? 0 : fromNeName.hashCode());
        result = (prime * result) + ((fromTpId == null) ? 0 : fromTpId.hashCode());
        result = (prime * result) + ((toId == null) ? 0 : toId.hashCode());
        result = (prime * result) + ((toNeName == null) ? 0 : toNeName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }

        if(obj == null) {
            return false;
        }

        if(getClass() != obj.getClass()) {
            return false;
        }
        ConnectionLink other = (ConnectionLink)obj;

        return checkFieldEquals(toNeName, other.toNeName) && checkFieldEquals(toId, other.toId)
                && checkFieldEquals(fromTpId, other.fromTpId) && checkFieldEquals(fromNeName, other.fromNeName)
                && checkFieldEquals(fromId, other.fromId);
    }

    private boolean checkFieldEquals(String thisFieldValue, String otherFieldValue) {

        if(thisFieldValue == null) {
            return otherFieldValue == null;
        } else {
            return thisFieldValue.equals(otherFieldValue);
        }
    }

}
