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
        final Services services = new DevServices(CONFIGURATION);

        final WebApp webApp = new WebApp(services);
        final TFTPServer tftpServer = new TFTPServer(new File("/tmp"), TFTPServer.ServerMode.GET_ONLY);


        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                Log.info(this, "Shutting down server...");
                try {
                    webApp.stop();
                    webApp.destroy();
                    services.getPersistenceService().close();
                    tftpServer.shutdown();
                } catch (Exception e) {
                    Log.error(this, "Error while shutting down server");
                }
                Log.info(this, "k thx bye.");
            }
        });
        webApp.start();
    }
}
