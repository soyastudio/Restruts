package soya.framework.restruts.action.servlet.io;

import soya.framework.restruts.action.servlet.ServletStreamHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JsonStreamHandler implements ServletStreamHandler {

    @Override
    public <T> T read(HttpServletRequest request, Class<T> type) throws IOException {
        return null;
    }

    @Override
    public void write(Object object, HttpServletRequest request, HttpServletResponse response) throws IOException {

    }
}
