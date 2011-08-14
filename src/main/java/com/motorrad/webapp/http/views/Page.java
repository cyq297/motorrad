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

package com.motorrad.webapp.http.views;

import com.motorrad.webapp.http.toolkit.Resource;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Page implements Resource {
    Template template;
    Map pageData = new HashMap();

    public Page(String template) throws IOException {
        Configuration cfg = new Configuration();
        URL url = getClass().getResource("/templates/");
        cfg.setDirectoryForTemplateLoading(new File(url.getFile()));
        cfg.setObjectWrapper(new DefaultObjectWrapper());
        this.template = cfg.getTemplate(template);
    }

    public Page(String template, Map pageData) throws IOException {
        Configuration cfg = new Configuration();
        URL url = getClass().getResource("/templates/");
        cfg.setDirectoryForTemplateLoading(new File(url.getFile()));
        cfg.setObjectWrapper(new DefaultObjectWrapper());
        this.template = cfg.getTemplate(template);
        this.pageData.putAll(pageData);
    }

    @Override
    public String getContentType() {
        return "text/html";
    }

    @Override
    public int getHttpStatus() {
        return HttpServletResponse.SC_OK;
    }

    @Override
    public void renderWithoutClosing(OutputStream out) throws IOException, TemplateException {
        PrintWriter pw = new PrintWriter(out);

        template.process(pageData, pw);

        pw.flush();

    }

    @Override
    public Map<String, String> extraHeaders() {
        return Collections.emptyMap();
    }
}
