package soya.framework.util.convert;

public class BooleanConverter implements Converter<Boolean> {
    protected String[] trueStrings = {"TRUE", "T", "YES", "Y", "ON", "1"};
    protected String[] falseStrings = {"FALSE", "F", "NO", "N", "OFF", "0"};

    public BooleanConverter() {
        super();
    }

    public BooleanConverter(String[] trueStrings, String[] falseStrings) {
        this.trueStrings = trueStrings;
        this.falseStrings = falseStrings;
    }

    @Override
    public Boolean convert(Class<Boolean> type, Object value) throws ConvertException {

        final String stringValue = value.toString().toUpperCase();
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
