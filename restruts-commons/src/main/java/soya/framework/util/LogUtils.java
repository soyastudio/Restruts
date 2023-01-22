package soya.framework.util;

import soya.framework.util.logging.LoggingService;

public final class LogUtils {
    private static LoggingService loggingService;

    static {
        loggingService = LoggingService.getInstance();
    }

    private LogUtils() {
    }

    // ---------- Direct Print
    public static void log(String message) {
        loggingService.log(message);
    }

    public static void log(Throwable throwable) {
        loggingService.log(throwable);
    }

    // ---------- Print with Location Information
    public static void log(String name, String message) {
        loggingService.log(name, message);
    }

    public static void log(Class<?> caller, String message) {
        loggingService.log(caller, message);
    }

    public static void log(String name, Throwable e) {
        loggingService.log(name, e);
    }

    public static void log(Class<?> caller, Throwable e) {
        loggingService.log(caller, e);
    }

    // ---------- Log start
    public static long logStartTime() {
        return System.currentTimeMillis();
    }

    public static void logEndTime(long startTime, String message) {
        loggingService.logEndTime(startTime, message);
    }

    public static long logStartNanoTime() {
        return System.nanoTime();
    }

    public static void logEndNanoTime(long startTime, String message) {
        loggingService.logEndNanoTime(startTime, message);
    }

    // ---------- TRACE
    public static void trace(String name, String message) {
        loggingService.trace(name, message);
    }

    public static void trace(Class<?> caller, String message) {
        loggingService.trace(caller, message);
    }

    public static void trace(String name, Throwable e) {
        loggingService.trace(name, e);
    }

    public static void trace(Class<?> caller, Throwable e) {
        loggingService.trace(caller, e);
    }

    // ---------- DEBUG
    public static void debug(String name, String message) {
        loggingService.debug(name, message);
    }

    public static void debug(Class<?> caller, String message) {
        loggingService.debug(caller, message);
    }

    public static void debug(String name, Throwable e) {
        loggingService.debug(name, e);
    }

    public static void debug(Class<?> caller, Throwable e) {
        loggingService.debug(caller, e);
    }

    // ---------- INFO
    public static void info(String name, String message) {
        loggingService.info(name, message);
    }

    public static void info(Class<?> caller, String message) {
        loggingService.info(caller, message);
    }

    public static void info(String name, Throwable e) {
        loggingService.info(name, e);
    }

    public static void info(Class<?> caller, Throwable e) {
        loggingService.info(caller, e);
    }

    // ---------- WARN
    public static void warn(String name, String message) {
        loggingService.warn(name, message);
    }

    public static void warn(Class<?> caller, String message) {
        loggingService.warn(caller, message);
    }

    public static void warn(String name, Throwable e) {
        loggingService.warn(name, e);
    }

    public static void warn(Class<?> caller, Throwable e) {
        loggingService.warn(caller, e);
    }

    // ---------- ERROR
    public static void error(String name, String message) {
        loggingService.error(name, message);
    }

    public static void error(Class<?> caller, String message) {
        loggingService.error(caller, message);
    }

    public static void error(String name, Throwable e) {
        loggingService.error(name, e);
    }

    public static void error(Class<?> caller, Throwable e) {
        loggingService.error(caller, e);
    }

    public static class TimeTracker {
        private long startTime;
        private long endTime;

        public TimeTracker() {
            startTime = System.currentTimeMillis();
        }
    }


    public static void main(String[] args) {
        log("xyz", new RuntimeException(new Exception("ERROR Message...")));
    }
}
