package com.motorrad.webapp.http;

import com.motorrad.webapp.http.views.FourOFourPage;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;


public class StaticFileServlet extends HttpServlet {
    public static final Map<String, String> MIME_TYPES;

    static {
        String[][] EXTENSIONS = new String[][]{
                {"*.gif", "image/gif"},
                {"*.pdf", "application/pdf"},
                {"*.doc", "application/vnd.ms-word"},
                {"*.jpg", "image/jpg"},
                {"*.png", "image/png"},
                {"*.css", "text/css"},
                {"*.js", "text/javascript"},
                {"*.txt", "text/plain"},
                {"*.ico", "image/x-icon"},
                {"*.vcf", "text/x-vcard"},
                {"*.xml", "text/xml"},
                {"*.swf", "application/x-shockwave-flash"}

        };

        MIME_TYPES = new HashMap<String, String>();
        for (String[] mapping : EXTENSIONS) {
            MIME_TYPES.put(mapping[0], mapping[1]);
        }
    }

    private String resourceBase;

    public StaticFileServlet(String resourceBase) {
        this.resourceBase = resourceBase;
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String mimeType = MIME_TYPES.get(getExtension(request.getRequestURI()));
        File resourceFile = new File(resourceBase + request.getRequestURI());

        if (!resourceFile.exists()) {
            ServletOutputStream outputStream = response.getOutputStream();
            new FourOFourPage().renderWithoutClosing(outputStream);
            outputStream.close();
            return;
        }

        InputStream in = new FileInputStream(resourceFile);
        response.setContentType(mimeType);
        OutputStream out = response.getOutputStream();
        IOUtils.copy(in, out);

        out.flush();
        out.close();
    }

    private String getExtension(String resource) {
        int offset = resource.lastIndexOf('.');
        return "*" + resource.substring(offset);
    }

}