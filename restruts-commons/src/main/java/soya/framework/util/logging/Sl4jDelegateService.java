package soya.framework.util.logging;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;

public class Sl4jDelegateService extends LoggingDelegateService {
    private static final Class<?> LOGGER_FACTORY_CLASS;
    private static final Class<?> LOGGER_CLASS;

    private static final Method GET_LOGGER_FROM_NAME;
    private static final Method GET_LOGGER_FROM_CLASS;

    private static final Method TRACE;
    private static final Method DEBUG;
    private static final Method INFO;
    private static final Method WARN;
    private static final Method ERROR;

    static {
        try {
            LOGGER_FACTORY_CLASS = Class.forName("org.slf4j.LoggerFactory");
            LOGGER_CLASS = Class.forName("org.slf4j.Logger");

            GET_LOGGER_FROM_NAME = LOGGER_FACTORY_CLASS.getMethod("getLogger", new Class[]{String.class});
            GET_LOGGER_FROM_CLASS = LOGGER_FACTORY_CLASS.getMethod("getLogger", new Class[]{Class.class});

            TRACE = LOGGER_CLASS.getMethod("trace", new Class[] {String.class});
            DEBUG = LOGGER_CLASS.getMethod("debug", String.class);
            INFO = LOGGER_CLASS.getMethod("info", String.class);
            WARN = LOGGER_CLASS.getMethod("warn", String.class);
            ERROR = LOGGER_CLASS.getMethod("error", String.class);

        } catch (ClassNotFoundException | NoSuchMethodException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public Sl4jDelegateService() {
    }

    @Override
    public void log(Level level, String name, String message) {
        try {
            getMethod(level).invoke(getLogger(name), new Object[]{message});
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void log(Level level, Class<?> caller, String message) {
        try {
            getMethod(level).invoke(getLogger(caller), new Object[]{message});
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void log(Level level, String name, Throwable throwable) {
        try {
            getMethod(level).invoke(getLogger(name), new Object[]{throwable.getMessage()});
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void log(Level level, Class<?> caller, Throwable throwable) {
        try {
            getMethod(level).invoke(getLogger(caller), new Object[]{throwable.getMessage()});
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private Method getMethod(Level level) {
        if (Level.OFF.equals(level) || Level.SEVERE.equals(level)) {
            return ERROR;

        } else if (Level.WARNING.equals(level)) {
            return WARN;

        } else if (Level.CONFIG.equals(level) || Level.FINE.equals(level)) {
            return DEBUG;

        } else if (Level.FINER.equals(level) || Level.FINEST.equals(level)) {
            return TRACE;

        } else {
            return INFO;

        }
    }

    private Object getLogger(Class<?> cls) {
        try {
            return GET_LOGGER_FROM_CLASS.invoke(null, new Object[]{cls});
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private Object getLogger(String name) {
        try {
            return GET_LOGGER_FROM_NAME.invoke(null, new Object[]{name});
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
