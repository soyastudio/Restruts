package soya.framework.convert;

public class BooleanConverter implements Converter<Boolean> {
    protected String[] trueStrings = {"true", "yes", "y", "on", "1"};
    protected String[] falseStrings = {"false", "no", "n", "off", "0"};

    public BooleanConverter() {
        super();
    }

    public BooleanConverter(String[] trueStrings, String[] falseStrings) {
        this.trueStrings = trueStrings;
        this.falseStrings = falseStrings;
    }

    @Override
    public Boolean convert(Class<Boolean> type, Object value) throws ConvertException {
        final String stringValue = value.toString().toLowerCase();
        for (String trueString : trueStrings) {
            if (trueString.equals(stringValue)) {
                return type.cast(Boolean.TRUE);
            }
        }

        for (String falseString : falseStrings) {
            if (falseString.equals(stringValue)) {
                return type.cast(Boolean.FALSE);
            }
        }

        throw new ConvertException(type, value);
    }
}
