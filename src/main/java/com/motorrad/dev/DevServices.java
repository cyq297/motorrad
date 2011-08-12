package com.motorrad.dev;

import com.motorrad.util.Configuration;
import com.motorrad.webapp.service.Services;

public class DevServices  implements Services{
    private Configuration configuration;

    public DevServices(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }
}
