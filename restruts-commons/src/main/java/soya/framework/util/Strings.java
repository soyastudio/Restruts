package soya.framework.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class Strings {
    private static final Gson GSON = new Gson();
    private static final Gson PRETTY_GSON = new GsonBuilder().setPrettyPrinting().create();

    // ---------- Empty
    public static String requireNonEmpty(String str) {
        if (str != null && !str.isEmpty()) {
            return str;
        }
        throw new NullPointerException();
    }

    public static String requireNonEmpty(String str, Supplier<String> supplier) {
        if (str != null && !str.isEmpty()) {
            return str;
        }

        return supplier.get();
    }

    // ---------- toString
    public static String toString(String[] arr, String splitter) {
        StringBuilder builder = new StringBuilder();
        IntStream.range(0, arr.length).forEach(i -> {
            if (i > 0) {
                builder.append(splitter);
            }
            builder.append(arr[i]);
        });
        return builder.toString();
    }

    public static String toString(Object obj) {
        return "???";
    }

    public static String toJson(Object obj) {
        return GSON.toJson(obj);
    }

    public static String toPrettyJson(Object obj) {
        return PRETTY_GSON.toJson(obj);
    }

    // ---------- toType
    public static Date toDate(String str, DateFormat format) throws ParseException {
        return format.parse(str);
    }

    public static URI toURI(String str) {
        return URI.create(str);
    }

    public static URL toURL(String str) throws MalformedURLException {
        return new URL(str);
    }

    public static <T> T toType(String str, Class<T> type) {
        if (str == null) {
            return null;
        }
        return GSON.fromJson(str, type);
    }

    public static <T> T toType(String str, Class<T> type, Supplier<T> defaultValueSupplier) {
        if (str == null) {
            return defaultValueSupplier.get();
        }
        return GSON.fromJson(str, type);
    }

    // ---------- Array
    public static String[] trim(String[] arr) {
        if (arr == null) {
            return null;
        }
        IntStream.range(0, arr.length).forEach(i -> arr[i] = arr[i].trim());
        return arr;
    }

    // ---------- Format
    public static String format(String template, Object... args) {
        Marker marker;

        Logger logger = LoggerFactory.getLogger("xxx");
        System.out.println(logger.getClass().getName());
        logger.info(template, "A", "B");

        return String.format(template, args);
    }
}
