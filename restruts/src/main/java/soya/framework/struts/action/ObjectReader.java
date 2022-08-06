package soya.framework.struts.action;

import java.io.IOException;
import java.io.InputStream;

public interface ObjectReader {
    <T> T read(InputStream inputStream, String contentType, Class<T> type) throws IOException;
}
