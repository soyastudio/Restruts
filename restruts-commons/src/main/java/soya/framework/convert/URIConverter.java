package soya.framework.convert;

import java.net.URI;

public class URIConverter implements Converter<URI> {
    @Override
    public URI convert(Class<URI> type, Object value) throws ConvertException {
        return URI.create(String.valueOf(value));
    }
}
