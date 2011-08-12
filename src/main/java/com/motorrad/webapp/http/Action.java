package com.motorrad.webapp.http;

import com.motorrad.webapp.http.toolkit.Resource;

public interface Action {
    public Resource execute(HttpContext httpContext);
}
