package soya.framework.util.convert;

import java.math.BigDecimal;
import java.math.BigInteger;

public class NumberConverter<T extends Number> implements Converter<T> {
    public NumberConverter() {
        super();
    }

    @Override
    public T convert(Class<T> type, Object value) throws ConvertException {

        String strValue = String.valueOf(value);
        if (Integer.class.equals(type)) {
            return (T) Integer.valueOf(strValue);

        } else if (Long.class.equals(type)) {
            return (T) Long.valueOf(strValue);

        } else if (Short.class.equals(type)) {
            return (T) Short.valueOf(strValue);

        } else if (Float.class.equals(type)) {
            return (T) Float.valueOf(strValue);

        } else if (Double.class.equals(type)) {
            return (T) Double.valueOf(strValue);

        } else if (Byte.class.equals(type)) {
            return (T) Byte.valueOf(strValue);

        } else if (BigInteger.class.equals(type)) {
            return (T) BigInteger.valueOf(Long.valueOf(strValue));

        } else if (BigDecimal.class.equals(type)) {
            return (T) BigDecimal.valueOf(Double.valueOf(strValue));

        }

        throw new ConvertException(type, value);
    }
}
