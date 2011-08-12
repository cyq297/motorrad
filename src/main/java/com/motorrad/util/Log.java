package com.motorrad.util;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

public class Log {
    private static boolean showFreeMemory = true;
    public static final DecimalFormat MEMORY_FORMAT = new DecimalFormat("0.0M");
    private static Map<String, Logger> loggers = new HashMap<String, Logger>();

    public static void setDebug(boolean isDebug) {
        Logger rootLogger = Logger.getRootLogger();
        if (isDebug) {
            rootLogger.setLevel(Level.DEBUG);
        } else {
            rootLogger.setLevel(Level.INFO);
        }
    }

    private static String sourceName(Object source) {
        String sourceName;
        if (source == null) {
            sourceName = "static";
        } else if (source instanceof String) {
            sourceName = (String) source;
        } else if (source instanceof Class) {
            sourceName = ((Class) source).getName();
        } else {
            sourceName = source.getClass().getName();
        }
        return sourceName;
    }

    private static long getTotalMemory() {
        return Runtime.getRuntime().totalMemory();
    }

    public static long getUsedMemory() {
        long free = Runtime.getRuntime().freeMemory();
        long total = getTotalMemory();
        return total - free;
    }

    public static String formatAsMegabytes(NumberFormat format, long bytes) {
        return format.format(bytes / 1024f / 1024f);
    }

    public static String formatAsMegabytes(long bytes) {
        return formatAsMegabytes(MEMORY_FORMAT, bytes);
    }

    private static String withK(String message) {
        if (showFreeMemory) {
            return message + " ["
                    + formatAsMegabytes(getUsedMemory()) + " used of "
                    + formatAsMegabytes(getTotalMemory()) + "]";
        } else {
            return message;
        }
    }

    private static Logger lazyLoggerLoad(Object source) {
        if (loggers.get(sourceName(source)) == null) {
            loggers.put(sourceName(source), Logger.getLogger(sourceName(source)));
        }
        return loggers.get(sourceName(source));
    }

    public static void error(Object source, String message) {
        lazyLoggerLoad(source).error(message);
    }

    public static void error(Object source, String message, Throwable err) {
        lazyLoggerLoad(source).error(message, err);
    }

    public static void error(Object source) {
        error(source, "");
    }

    public static void error(Object source, Throwable err) {
        error(source, "", err);
    }

    public static void debug(Object source, String message) {
        lazyLoggerLoad(sourceName(source)).debug(withK(message));
    }

    public static void debug(Object source, String message, Throwable err) {
        lazyLoggerLoad(sourceName(source)).debug(withK(message), err);
    }

    public static void info(Object source, String message) {
        lazyLoggerLoad(sourceName(source)).info(withK(message));
    }

    public static void info(Object source, String message, Throwable err) {
        lazyLoggerLoad(sourceName(source)).info(withK(message), err);
    }

    public static void warn(Object source, String message) {
        lazyLoggerLoad(sourceName(source)).info(withK(message));
    }

    public static void warn(Object source, String message, Throwable err) {
        lazyLoggerLoad(sourceName(source)).info(withK(message), err);
    }
}