package com.motorrad.webapp.http.views;

import com.motorrad.webapp.http.toolkit.Resource;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Map;

public class FourOFourPage implements Resource{
    @Override
    public String getContentType() {
        return "text/html";
    }

    @Override
    public int getHttpStatus() {
        return HttpServletResponse.SC_OK;
    }

    @Override
    public void renderWithoutClosing(OutputStream out) throws IOException {
        PrintWriter pw = new PrintWriter(out);
        pw.append("404");

        pw.flush();

    }

    @Override
    public Map<String, String> extraHeaders() {
        return Collections.emptyMap();
    }
}
