package com.motorrad.webapp.http;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.motorrad.webapp.http.actions.ks.Kickstart;
import com.motorrad.webapp.service.Services;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class KickstartServletConfig extends GuiceServletContextListener {
    private ActionRegistry actionRegistry;
    private Services services;

    public KickstartServletConfig(ActionRegistry actionRegistry, Services services) {
        this.actionRegistry = actionRegistry;
        this.services = services;
    }

    @Override
    protected Injector getInjector() {
        return Guice.createInjector(new JerseyServletModule() {
            @Override
            protected void configureServlets() {
                bind(Kickstart.class);
                bind(Services.class).toInstance(services);
                bind(ActionRegistry.class).toInstance(actionRegistry);
                serve("/ks/*").with(GuiceContainer.class);
            }
        });
    }

}
