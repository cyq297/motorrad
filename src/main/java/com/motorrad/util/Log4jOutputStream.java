package com.motorrad.util;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.OutputStream;

public class Log4jOutputStream extends OutputStream {
    private Logger log;
    private StringBuilder sb;

    public Log4jOutputStream(Class loggedClass) {
        log = Logger.getLogger(loggedClass);
        sb = new StringBuilder();
    }

    @Override
    public void write(int b) throws IOException {
        if (b == '\n') {
            log.info(sb.toString());
            sb = new StringBuilder();
        } else {
            sb.append((char) b);
        }
    }
}
