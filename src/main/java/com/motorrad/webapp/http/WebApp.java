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

import com.google.inject.servlet.GuiceFilter;
import com.motorrad.util.Configuration.Key;
import com.motorrad.webapp.service.Services;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.server.session.AbstractSessionManager;
import org.eclipse.jetty.server.session.HashSessionManager;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;


public class WebApp {
    private Services services;
    private static final String RESOURCE_BASE = "web";
    private static final Server server = new Server();

    public WebApp(Services services) {
        this.services = services;
    }

    public void start() throws Exception {
        Connector connector = new SelectChannelConnector();
        connector.setPort(services.getConfiguration().getIntValue(Key.httpPort));
        connector.setHost("0.0.0.0");

        server.addConnector(connector);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath(services.getConfiguration().getStringVString(Key.contextPath));
        context.setClassLoader(Thread.currentThread().getContextClassLoader());

        AbstractSessionManager sessionManager = new HashSessionManager();

        SessionHandler sessionHandler = new SessionHandler(sessionManager);
        sessionHandler.setHandler(context);

        ServletHolder servletHolder = new ServletHolder(new StaticFileServlet(RESOURCE_BASE));

        for (String extension : StaticFileServlet.MIME_TYPES.keySet()) {
            context.addServlet(servletHolder, extension);
        }

        context.addEventListener(new KickstartServletConfig(ActionRegistry.defaultRegistry(), services));
        context.addFilter(GuiceFilter.class, "/ks/*", null);

        context.addEventListener(new ApiServletConfig(ActionRegistry.defaultRegistry(), services));
        context.addFilter(GuiceFilter.class, "/api/*", null);

        Dispatcher dispatcher = new Dispatcher(ActionRegistry.defaultRegistry(), services);
        context.addServlet(new ServletHolder(dispatcher), "/");

        server.setHandler(context);

        server.start();
        server.join();

    }

    public void stop() throws Exception {
        server.stop();
    }

    public void destroy() throws Exception {
        server.destroy();
    }

}
