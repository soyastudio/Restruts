package soya.framework.restruts.action;

import java.io.IOException;
import java.io.OutputStream;

public interface ObjectWriter {
    void write(Object object, String contentType, OutputStream outputStream) throws IOException;
}
