package soya.framework.convert;

public class NullConverter<T> implements Converter<T> {

    @Override
    public T convert(Class<T> type, Object value) throws ConvertException {
        if (type.isPrimitive()) {
            throw new ConvertException("Cannot covert null to " + type.getName());

        } else {
            return null;
        }
    }
}
