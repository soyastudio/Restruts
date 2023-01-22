package soya.framework.util.logging;

public abstract class LoggingDelegateService extends LoggingService {

    @Override
    public void log(String message) {
        // do nothing
    }

    @Override
    public void log(Throwable throwable) {
        // do nothing
    }

    @Override
    public void log(String name, String message) {
        // do nothing
    }

    @Override
    public void log(Class<?> caller, String message) {
        // do nothing
    }

    @Override
    public void log(String name, Throwable throwable) {
        // do nothing
    }

    @Override
    public void log(Class<?> caller, Throwable throwable) {
        // do nothing
    }

    @Override
    public void logEndTime(long startTime, String message) {
        // do nothing
    }

    @Override
    public void logEndNanoTime(long startTime, String message) {
        // do nothing
    }

}
