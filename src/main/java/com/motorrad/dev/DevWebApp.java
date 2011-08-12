package com.motorrad.dev;

import com.motorrad.util.Configuration;
import com.motorrad.webapp.http.WebApp;
import com.motorrad.webapp.service.Services;

import java.util.HashMap;
import java.util.Map;

public class DevWebApp {
    public static final Configuration CONFIGURATION;

    static {
        Map<Configuration.Key, String> map = new HashMap<Configuration.Key, String>();
        map.put(Configuration.Key.httpPort, "8080");
        map.put(Configuration.Key.contextPath, "/");
        CONFIGURATION = new Configuration(map);
    }

    public static void main(String[] args) throws Exception {
        System.setProperty("log4j.configuration", "log4jdev-console.properties");
        Services services = new DevServices(CONFIGURATION);
        WebApp webApp = new WebApp(services);
        webApp.start();
    }
}
