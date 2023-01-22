package soya.framework.util.convert;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class URLConverter implements Converter<URL> {
    @Override
    public URL convert(Class<URL> type, Object value) throws ConvertException {
        try {
            if (value instanceof File) {
                return ((File) value).toURI().toURL();

            } else if (value instanceof URI) {
                return ((URI) value).toURL();

            } else {
                return new URL(String.valueOf(value));
            }
        } catch (MalformedURLException e) {
            throw new ConvertException(type, value, e);
        }
    }
}
