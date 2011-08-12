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
