package soya.framework.lang;

public class FunctionProcessorException extends RuntimeException {
    public FunctionProcessorException() {
    }

    public FunctionProcessorException(String message) {
        super(message);
    }

    public FunctionProcessorException(String message, Throwable cause) {
        super(message, cause);
    }

    public FunctionProcessorException(Throwable cause) {
        super(cause);
    }
}
