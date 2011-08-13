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
