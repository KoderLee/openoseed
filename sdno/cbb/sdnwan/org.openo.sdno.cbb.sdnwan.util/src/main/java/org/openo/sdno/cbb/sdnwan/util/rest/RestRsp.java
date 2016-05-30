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
package org.openo.sdno.cbb.sdnwan.util.rest;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnore;

import org.openo.sdno.remoteservice.exception.ExceptionArgs;

/**
 * RESTFul response.<br/>
 * 
 * @author
 * @version SDNO 0.5 17-Mar-2016
 */
@SuppressWarnings("serial")
public class RestRsp<T> implements Serializable {

    protected int result;

    protected String message;

    protected T data;

    protected ExceptionArgs exceptionArg;

    /**
     * Constructor.
     * 
     * @version SDNO 0.5 17-Mar-2016
     */
    public RestRsp() {
        super();
    }

    /**
     * Constructor.
     * 
     * @version SDNO 0.5 17-Mar-2016
     */
    public RestRsp(int result) {
        super();
        this.result = result;
    }

    /**
     * Constructor.
     * 
     * @version SDNO 0.5 17-Mar-2016
     */
    public RestRsp(int result, String message) {
        super();
        this.result = result;
        this.message = message;
    }

    /**
     * Constructor.
     * 
     * @version SDNO 0.5 17-Mar-2016
     */
    public RestRsp(int result, T data) {
        super();
        this.result = result;
        this.data = data;
    }

    /**
     * Constructor.
     * 
     * @version SDNO 0.5 17-Mar-2016
     */
    public RestRsp(int result, String message, T data) {
        super();
        this.result = result;
        this.message = message;
        this.data = data;
    }
    
    /**
     * Constructor.
     * 
     * @version SDNO 0.5 17-Mar-2016
     */
    public RestRsp(int result, String message, T data, ExceptionArgs exceptionArg) {
        super();
        this.result = result;
        this.message = message;
        this.data = data;
        this.exceptionArg = exceptionArg;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @JsonIgnore
    public boolean isValid() {
        return this.result == 0 && null != this.data;
    }

    public ExceptionArgs getExceptionArg() {
        return exceptionArg;
    }

    public void setExceptionArg(ExceptionArgs exceptionArg) {
        this.exceptionArg = exceptionArg;
    }
}
