package com.motorrad.webapp.http.toolkit;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public interface Resource {
    public String getContentType();

    public int getHttpStatus();

    public void renderWithoutClosing(OutputStream out) throws IOException;

    public Map<String, String> extraHeaders();
}
