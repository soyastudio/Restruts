package soya.framework.action.servlet;

import java.io.IOException;
import java.io.InputStream;

public interface StreamReader {
    <T> T read(InputStream inputStream, Class<T> type) throws IOException;
}
