package com.motorrad.webapp.http;

import com.motorrad.util.Bag;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

public class RequestBagger {
    public Bag<String, String> bagParameters(HttpServletRequest request) {
        Bag<String, String> bag = new Bag<String, String>();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String parameterName = parameterNames.nextElement();
            String[] values = request.getParameterValues(parameterName);
            for (String value : values) {
                bag.put(parameterName, value);
            }
        }
        return bag;
    }
}
