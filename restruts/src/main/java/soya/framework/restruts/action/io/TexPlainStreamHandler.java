package soya.framework.restruts.action.io;

import soya.framework.restruts.action.ServletStreamHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TexPlainStreamHandler implements ServletStreamHandler {
    @Override
    public <T> T read(HttpServletRequest request, Class<T> type) throws IOException {

        return null;
    }

    @Override
    public void write(Object object, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (object != null) {
            response.getOutputStream().write(object.toString().getBytes(StandardCharsets.UTF_8));
        }
    }
}
