package soya.framework.util;

import soya.framework.util.convert.ConvertService;

public final class ConvertUtils {
    private ConvertUtils() {
    }

    public static <T> T convert(final Object value, final Class<T> type) {
        return ConvertService.convert(value, type);
    }
}
