package com.motorrad.dev;

import com.motorrad.tftp.TFTPServer;
import com.motorrad.util.Configuration;
import com.motorrad.util.Log;
import com.motorrad.webapp.http.WebApp;
import com.motorrad.webapp.service.Services;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class DevApp {
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

        final WebApp webApp = new WebApp(services);
        final TFTPServer tftpServer = new TFTPServer(new File("/tmp"), TFTPServer.ServerMode.GET_ONLY);


        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                Log.info(this, "Shutting down server...");
                try {
                    webApp.stop();
                    webApp.destroy();
                    tftpServer.shutdown();
                } catch (Exception e) {
                    Log.error(this, "Error while shutting http server");
                }
                Log.info(this, "k thx bye.");
            }
        });
        webApp.start();
    }
}
