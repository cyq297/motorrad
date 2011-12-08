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

package com.motorrad.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

public class Log {
    private static boolean showFreeMemory = true;
    public static final DecimalFormat MEMORY_FORMAT = new DecimalFormat("0.0M");
    private static Map<String, Logger> loggers = new HashMap<String, Logger>();

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
            loggers.put(sourceName(source), LoggerFactory.getLogger(sourceName(source)));
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