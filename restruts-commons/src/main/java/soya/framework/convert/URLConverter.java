package soya.framework.convert;

import java.net.MalformedURLException;
import java.net.URL;

public class URLConverter implements Converter<URL>{
    @Override
    public URL convert(Class<URL> type, Object value) throws ConvertException {
        try {
            return new URL(String.valueOf(value));
        } catch (MalformedURLException e) {
            throw new ConvertException(type, value);
        }
    }
}
