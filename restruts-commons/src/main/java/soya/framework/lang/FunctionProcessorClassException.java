package soya.framework.lang;

public class FunctionProcessorClassException extends FunctionProcessorException {

    public FunctionProcessorClassException(Class<? extends FunctionProcessor> filterClass, String message) {
        super("Illegal function processor class '" + filterClass.getName() + "': " + message);
    }
}
