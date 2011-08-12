package com.motorrad.webapp.http;

import com.motorrad.util.Configuration.Key;
import com.motorrad.util.Log;
import com.motorrad.webapp.service.Services;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.server.session.AbstractSessionManager;
import org.eclipse.jetty.server.session.HashSessionManager;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.SessionCookieConfig;
import java.util.concurrent.TimeUnit;


public class WebApp {
    private Services services;
    private static final String RESOURCE_BASE = "web";

    public WebApp(Services services) {
        this.services = services;
    }

    public void start() throws Exception {
        final Server server = new Server();
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

        Dispatcher dispatcher = new Dispatcher(ActionRegistry.defaultRegistry(), services);
        context.addServlet(new ServletHolder(dispatcher), "/");

        server.setHandler(context);

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                Log.info(this, "Shutting down http server...");
                try {
                    server.stop();
                    server.destroy();
                } catch (Exception e) {
                    Log.error(this, "Error while shutting down http server");
                }
                Log.info(this, "k thx bye.");
            }
        });

        server.start();
        server.join();

    }
}
