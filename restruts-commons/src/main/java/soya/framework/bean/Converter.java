package soya.framework.bean;

public interface Converter {
    <T> T convert(Class<T> type, Object value);
}
