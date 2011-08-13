/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.motorrad.webapp.http;

import com.motorrad.util.Bag;

import javax.servlet.http.HttpServletRequest;


public class HttpContext {
    private Bag<String, String> parameters;
    private String sessionId;
    private HttpServletRequest originalRequest;

    public HttpContext(Bag<String, String> parameters, String sessionId, HttpServletRequest originalRequest) {
        this.parameters = parameters;
        this.sessionId = sessionId;
        this.originalRequest = originalRequest;
    }

    public Bag<String, String> getParameters() {
        return parameters;
    }

    public String getSessionId() {
        return sessionId;
    }

    public HttpServletRequest getOriginalRequest() {
        return originalRequest;
    }
}
