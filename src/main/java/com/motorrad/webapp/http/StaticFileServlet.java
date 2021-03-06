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

import org.apache.commons.io.IOUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

        InputStream in = this.getClass().getResourceAsStream(resourceBase + request.getRequestURI());

        if (in == null) {
            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.close();
            return;
        }

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