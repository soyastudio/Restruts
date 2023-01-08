package soya.framework.action.dispatch;

public class EvaluatorException extends Exception {
    public EvaluatorException(String message) {
        super(message);
    }

    public EvaluatorException(String message, Throwable cause) {
        super(message, cause);
    }

    public EvaluatorException(Throwable cause) {
        super(cause);
    }
}
