package soya.framework.util.logging;

import java.util.logging.Level;

public class Log4JDelegateService extends LoggingDelegateService {


    public Log4JDelegateService() {
        super();
    }

    @Override
    public void log(Level level, String name, String message) {

    }

    @Override
    public void log(Level level, Class<?> caller, String message) {

    }

    @Override
    public void log(Level level, String name, Throwable throwable) {

    }

    @Override
    public void log(Level level, Class<?> caller, Throwable throwable) {

    }
}
