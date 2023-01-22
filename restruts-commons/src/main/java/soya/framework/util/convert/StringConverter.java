package soya.framework.util.convert;

public class StringConverter implements Converter<String> {

    @Override
    public String convert(Class<String> type, Object value) throws ConvertException {
        return value.toString();
    }
}
