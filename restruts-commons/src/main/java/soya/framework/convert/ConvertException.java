package soya.framework.convert;

public class ConvertException extends RuntimeException {

    public ConvertException(final Class<?> type, final Object value) {
        super("Can't convert value '" + value + "' to type " + type);
    }

    public ConvertException(final Class<?> type, final Object value, Throwable e) {
        super("Can't convert value '" + value + "' to type: " + type, e);
    }

    public ConvertException(String message) {
        super(message);
    }
}
