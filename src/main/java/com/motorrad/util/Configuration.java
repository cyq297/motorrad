package com.motorrad.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Properties;

public class Configuration {
    public static enum Key {
        httpPort("motorrad.http.port"),
        contextPath("motorrad.http.context.path");

        private String propertyName;

        Key(String propertyName) {
            this.propertyName = propertyName;
        }

        private int getIntValue(Properties properties) {
            return Integer.parseInt(properties.getProperty(propertyName));
        }

        private String getStringValue(Properties properties) {
            return properties.getProperty(propertyName);
        }

        private Boolean getBooleanValue(Properties properties) {
            return Boolean.parseBoolean(properties.getProperty(propertyName));
        }

        private String getPropertyName() {
            return propertyName;
        }
    }

    private Properties properties;

    public Configuration(Map<Key, String> parameters) {
        this.properties = new Properties();
        for (Key key : parameters.keySet()) {
            String value = parameters.get(key);
            properties.put(key.getPropertyName(), value);
        }
    }

    public Configuration(InputStream in) throws IOException {
        properties = new Properties();
        properties.load(new BufferedReader(new InputStreamReader(in)));
    }

    public String getStringVString(Key key) {
        return key.getStringValue(properties);
    }

    public int getIntValue(Key key) {
        return key.getIntValue(properties);
    }

    public Boolean getBooleanValue(Key key) {
        return key.getBooleanValue(properties);
    }
}
