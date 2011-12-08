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

import com.google.inject.Singleton;
import com.motorrad.util.Log;
import com.sun.jersey.api.view.Viewable;
import com.sun.jersey.spi.template.ViewProcessor;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import javax.inject.Inject;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

@Provider
@Singleton
public class FreemarkerViewProcessor implements ViewProcessor<Template> {
    private final Configuration cfg = new Configuration();

    @Inject
    public FreemarkerViewProcessor() {
        cfg.setLocalizedLookup(false);
        cfg.setTemplateUpdateDelay(0);
        cfg.setClassForTemplateLoading(this.getClass(), "/templates");
        cfg.setObjectWrapper(new DefaultObjectWrapper());
    }

    public Template resolve(final String path) {
        try {
            return cfg.getTemplate(path);
        } catch (Exception e) {
            Log.error(this, e);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public void writeTo(final Template template, final Viewable viewable, final OutputStream out) throws IOException {
        try {
            out.flush();
            template.process(viewable.getModel(), new OutputStreamWriter(out));
        } catch (final TemplateException e) {
            throw new IOException(e);
        }
    }
}
