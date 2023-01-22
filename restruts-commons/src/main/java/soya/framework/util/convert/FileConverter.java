package soya.framework.util.convert;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

public class FileConverter implements Converter<File> {

    @Override
    public File convert(Class<File> type, Object value) throws ConvertException {
        URI uri = null;
        if(value instanceof URI) {
            uri = (URI) value;

        } else if(value instanceof URL) {
            URL url = (URL) value;
            try {
                uri = url.toURI();

            } catch (URISyntaxException e) {
                throw new ConvertException(type, value, e);
            }

        } else {
            uri = URI.create(String.valueOf(value));
        }

        try {
            return Paths.get(uri).toFile();

        } catch (Exception e) {
            throw new ConvertException(type, value, e);
        }
    }
}
