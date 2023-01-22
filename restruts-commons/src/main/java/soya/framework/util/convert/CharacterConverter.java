package soya.framework.util.convert;

public class CharacterConverter implements Converter<Character> {

    @Override
    public Character convert(Class<Character> type, Object value) throws ConvertException {
        return type.cast(new Character(value.toString().charAt(0)));
    }
}
