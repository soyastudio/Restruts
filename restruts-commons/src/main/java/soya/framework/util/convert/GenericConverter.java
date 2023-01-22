package soya.framework.util.convert;

import com.google.gson.Gson;

public class GenericConverter<T> implements Converter<T> {
    private static final Gson GSON = new Gson();

    @Override
    public T convert(Class<T> type, Object value) throws ConvertException {
        return GSON.fromJson(GSON.toJsonTree(value), type);
    }
}
