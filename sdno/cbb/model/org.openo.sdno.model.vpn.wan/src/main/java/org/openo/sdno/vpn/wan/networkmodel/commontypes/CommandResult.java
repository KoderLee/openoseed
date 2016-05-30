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
package org.openo.sdno.vpn.wan.networkmodel.commontypes;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import org.codehaus.jackson.annotate.JsonProperty;

@XmlAccessorType(XmlAccessType.FIELD)
public class CommandResult {

    @XmlElement(name = "result")
    @JsonProperty("result")
    private int result;

    @XmlElementWrapper(name = "success-resources")
    @XmlElement(name = "success-resource")
    @JsonProperty("success-resources")
    private List<SuccessResource> successResources;

    @XmlElementWrapper(name = "failed-resources")
    @XmlElement(name = "failed-resource")
    @JsonProperty("failed-resources")
    private List<FailedResource> failedResources;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public List<SuccessResource> getSuccessResources() {
        return successResources;
    }

    public void setSuccessResources(List<SuccessResource> successResources) {
        this.successResources = successResources;
    }

    public List<FailedResource> getFailedResources() {
        return failedResources;
    }

    public void setFailedResources(List<FailedResource> failedResources) {
        this.failedResources = failedResources;
    }

}
