package soya.framework.util.convert;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class URIConverter implements Converter<URI> {

    @Override
    public URI convert(Class<URI> type, Object value) throws ConvertException {
        if (value instanceof URL) {
            try {
                return ((URL) value).toURI();
            } catch (URISyntaxException e) {
                throw new ConvertException(type, value, e);
            }
        } else if (value instanceof File) {
            return ((File) value).toURI();

        } else {
            try {
                return URI.create(String.valueOf(value));

            } catch (IllegalArgumentException e) {
                throw new ConvertException(type, value, e);
            }

        }
    }
}
