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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import org.codehaus.jackson.annotate.JsonProperty;

@XmlAccessorType(XmlAccessType.FIELD)
public class PortPerformance {

    @XmlElement(name = "service-id")
    @JsonProperty("service-id")
    private String serviceId;

    @XmlElement(name = "ac-id")
    @JsonProperty("ac-id")
    private String acId;

    @XmlElement(name = "input-time-stamp")
    @JsonProperty("input-time-stamp")
    private String inputTimeStamp;

    @XmlElement(name = "input-packets-number")
    @JsonProperty("input-packets-number")
    private long inputPacketsNumber;

    @XmlElement(name = "input-bits-number")
    @JsonProperty("input-bits-number")
    private long inputBitsNumber;

    @XmlElement(name = "input-peak-rate")
    @JsonProperty("input-peak-rate")
    private long inputPeakRate;

    @XmlElement(name = "input-avg-rate")
    @JsonProperty("input-avg-rate")
    private long inputAvgRate;

    @XmlElement(name = "output-time-stamp")
    @JsonProperty("output-time-stamp")
    private String outputTimeStamp;

    @XmlElement(name = "output-packets-number")
    @JsonProperty("output-packets-number")
    private long outputPacketsNumber;

    @XmlElement(name = "output-bits-number")
    @JsonProperty("output-bits-number")
    private long outputBitsNumber;

    @XmlElement(name = "output-peak-rate")
    @JsonProperty("output-peak-rate")
    private long outputPeakRate;

    @XmlElement(name = "output-avg-rate")
    @JsonProperty("output-avg-rate")
    private long outputAvgRate;

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getAcId() {
        return acId;
    }

    public void setAcId(String acId) {
        this.acId = acId;
    }

    public String getInputTimeStamp() {
        return inputTimeStamp;
    }

    public void setInputTimeStamp(String inputTimeStamp) {
        this.inputTimeStamp = inputTimeStamp;
    }

    public long getInputPacketsNumber() {
        return inputPacketsNumber;
    }

    public void setInputPacketsNumber(long inputPacketsNumber) {
        this.inputPacketsNumber = inputPacketsNumber;
    }

    public long getInputBitsNumber() {
        return inputBitsNumber;
    }

    public void setInputBitsNumber(long inputBitsNumber) {
        this.inputBitsNumber = inputBitsNumber;
    }

    public long getInputPeakRate() {
        return inputPeakRate;
    }

    public void setInputPeakRate(long inputPeakRate) {
        this.inputPeakRate = inputPeakRate;
    }

    public long getInputAvgRate() {
        return inputAvgRate;
    }

    public void setInputAvgRate(long inputAvgRate) {
        this.inputAvgRate = inputAvgRate;
    }

    public String getOutputTimeStamp() {
        return outputTimeStamp;
    }

    public void setOutputTimeStamp(String outputTimeStamp) {
        this.outputTimeStamp = outputTimeStamp;
    }

    public long getOutputPacketsNumber() {
        return outputPacketsNumber;
    }

    public void setOutputPacketsNumber(long outputPacketsNumber) {
        this.outputPacketsNumber = outputPacketsNumber;
    }

    public long getOutputBitsNumber() {
        return outputBitsNumber;
    }

    public void setOutputBitsNumber(long outputBitsNumber) {
        this.outputBitsNumber = outputBitsNumber;
    }

    public long getOutputPeakRate() {
        return outputPeakRate;
    }

    public void setOutputPeakRate(long outputPeakRate) {
        this.outputPeakRate = outputPeakRate;
    }

    public long getOutputAvgRate() {
        return outputAvgRate;
    }

    public void setOutputAvgRate(long outputAvgRate) {
        this.outputAvgRate = outputAvgRate;
    }

}
