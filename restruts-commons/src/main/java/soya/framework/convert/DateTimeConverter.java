package soya.framework.convert;

import java.util.Date;

public class DateTimeConverter<T extends Date> implements Converter<T> {

    private final DateTimeConfiguration configuration;

    public DateTimeConverter() {
        this(new DateTimeConfiguration());
    }

    public DateTimeConverter(DateTimeConfiguration configuration) {
        this.configuration = configuration;

    }


    @Override
    public T convert(Class<T> type, Object value) throws ConvertException {
        return null;
    }
}
