package soya.framework.pattern;

public class FunctionalFilterException extends RuntimeException {
    public FunctionalFilterException() {
    }

    public FunctionalFilterException(String message) {
        super(message);
    }

    public FunctionalFilterException(String message, Throwable cause) {
        super(message, cause);
    }

    public FunctionalFilterException(Throwable cause) {
        super(cause);
    }
}
