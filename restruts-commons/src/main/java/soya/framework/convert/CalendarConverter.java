package soya.framework.convert;

import java.util.Calendar;

public class CalendarConverter implements Converter<Calendar> {
    private final DateTimeConfiguration configuration;

    public CalendarConverter() {
        this(new DateTimeConfiguration());
    }

    public CalendarConverter(DateTimeConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public Calendar convert(Class<Calendar> type, Object value) throws ConvertException {
        return null;
    }
}
