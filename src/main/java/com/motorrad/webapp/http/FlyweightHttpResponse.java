package com.motorrad.webapp.http;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

public class FlyweightHttpResponse {
    private HttpServletResponse response;

    public FlyweightHttpResponse(HttpServletResponse response) {
        this.response = response;
    }

    public void setContentType(String contentType) {
        response.setContentType(contentType);
    }

    public OutputStream getOutputStream() throws IOException {
        return response.getOutputStream();
    }

    public void setStatusCode(int statusCode) {
        response.setStatus(statusCode);
    }

    public void setHeader(String key, String value) {
        response.setHeader(key, value);
    }
}