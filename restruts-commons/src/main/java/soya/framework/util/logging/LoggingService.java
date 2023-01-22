package soya.framework.util.logging;

import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class LoggingService {

    private static LoggingService instance;

    protected LoggingService() {
        instance = this;
    }

    public static LoggingService getInstance() {
        if (instance == null) {
            new DefaultLoggingService();
        }
        return instance;
    }

    public abstract void log(String message);

    public abstract void log(Throwable throwable);

    public abstract void logEndTime(long startTime, String message);

    public abstract void logEndNanoTime(long startTime, String message);

    public abstract void log(String name, String message);

    public abstract void log(Class<?> caller, String message);

    public abstract void log(String name, Throwable throwable);

    public abstract void log(Class<?> caller, Throwable throwable);

    // ---------- TRACE
    public void trace(String name, String message) {
        log(Level.FINER, name, message);
    }

    public void trace(Class<?> caller, String message) {
        log(Level.FINER, caller, message);
    }

    public void trace(String name, Throwable e) {
        log(Level.FINER, name, e);
    }

    public void trace(Class<?> caller, Throwable e) {
        log(Level.FINER, caller, e);
    }

    // ---------- DEBUG
    public void debug(String name, String message) {
        log(Level.FINE, name, message);
    }

    public void debug(Class<?> caller, String message) {
        log(Level.FINE, caller, message);
    }

    public void debug(String name, Throwable e) {
        log(Level.FINE, name, e);
    }

    public void debug(Class<?> caller, Throwable e) {
        log(Level.FINE, caller, e);
    }

    // ---------- INFO
    public void info(String name, String message) {
        log(Level.INFO, name, message);
    }

    public void info(Class<?> caller, String message) {
        log(Level.INFO, caller, message);
    }

    public void info(String name, Throwable e) {
        log(Level.INFO, name, e);
    }

    public void info(Class<?> caller, Throwable e) {
        log(Level.INFO, caller, e);
    }

    // ---------- WARN
    public void warn(String name, String message) {
        log(Level.WARNING, name, message);
    }

    public void warn(Class<?> caller, String message) {
        log(Level.WARNING, caller, message);
    }

    public void warn(String name, Throwable e) {
        log(Level.WARNING, name, e);
    }

    public void warn(Class<?> caller, Throwable e) {
        log(Level.WARNING, caller, e);
    }

    // ---------- ERROR
    public void error(String name, String message) {
        log(Level.SEVERE, name, message);
    }

    public void error(Class<?> caller, String message) {
        log(Level.SEVERE, caller, message);
    }

    public void error(String name, Throwable e) {
        log(Level.SEVERE, name, e);
    }

    public void error(Class<?> caller, Throwable e) {
        log(Level.SEVERE, caller, e);
    }

    // ================================================
    public abstract void log(Level level, String name, String message);

    public abstract void log(Level level, Class<?> caller, String message);

    public abstract void log(Level level, String name, Throwable throwable);

    public abstract void log(Level level, Class<?> caller, Throwable throwable);

    static class DefaultLoggingService extends LoggingService {

        protected DefaultLoggingService() {
            super();
        }

        @Override
        public void log(String message) {
            System.out.println(message);
        }

        @Override
        public void log(Throwable throwable) {
            throwable.printStackTrace();
        }

        @Override
        public void logEndTime(long startTime, String message) {
            info("TIME TRACKING", message + " in " + (System.currentTimeMillis() - startTime) + "ms");
        }

        @Override
        public void logEndNanoTime(long startTime, String message) {
            info("TIME TRACKING", message + " in " + (System.nanoTime() - startTime) + "ns");
        }

        @Override
        public void log(String name, String message) {
            Logger.getLogger(name).info(message);
        }

        @Override
        public void log(Class<?> caller, String message) {
            Logger.getLogger(caller.getName()).info(message);
        }

        @Override
        public void log(String name, Throwable throwable) {
            Logger.getLogger(name).severe(throwable.getMessage());
        }

        @Override
        public void log(Class<?> caller, Throwable throwable) {
            Logger.getLogger(caller.getName()).severe(throwable.getMessage());
        }

        @Override
        public void log(Level level, String name, String message) {
            Logger.getLogger(name).log(level, message);
        }

        @Override
        public void log(Level level, Class<?> caller, String message) {
            Logger.getLogger(caller.getName()).log(level, message);
        }

        @Override
        public void log(Level level, String name, Throwable throwable) {
            Logger.getLogger(name).log(level, throwable.getMessage());
        }

        @Override
        public void log(Level level, Class<?> caller, Throwable throwable) {
            Logger.getLogger(caller.getName()).log(level, throwable.getMessage());
        }


    }
}
