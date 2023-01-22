package soya.framework.util.convert;

public interface Converter<T> {
    T convert(Class<T> type, Object value) throws ConvertException;
}
