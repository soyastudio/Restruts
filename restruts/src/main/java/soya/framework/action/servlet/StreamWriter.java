package soya.framework.action.servlet;

import java.io.IOException;
import java.io.OutputStream;

public interface StreamWriter {
    void write(Object object, OutputStream out) throws IOException;
}
