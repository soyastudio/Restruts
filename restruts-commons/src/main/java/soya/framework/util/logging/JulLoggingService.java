package soya.framework.util.logging;

import java.util.logging.Level;
import java.util.logging.Logger;

public class JulLoggingService extends LoggingDelegateService {
    private Level defaultLevel = Level.INFO;
    private Level defaultErrorLevel = Level.SEVERE;

    public JulLoggingService() {
    }

    public JulLoggingService(Level defaultLevel, Level defaultErrorLevel) {
        this.defaultLevel = defaultLevel;
        this.defaultErrorLevel = defaultErrorLevel;
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
