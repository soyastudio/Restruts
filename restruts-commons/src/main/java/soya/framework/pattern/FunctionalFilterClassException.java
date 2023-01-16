package soya.framework.pattern;

public class FunctionalFilterClassException extends FunctionalFilterException {

    public FunctionalFilterClassException(Class<? extends FunctionalFilter> filterClass, String message) {
        super("Illegal function processor class '" + filterClass.getName() + "': " + message);
    }
}
