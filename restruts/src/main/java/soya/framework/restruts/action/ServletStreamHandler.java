package soya.framework.restruts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface ServletStreamHandler {

    <T> T read(HttpServletRequest request, Class<T> type) throws IOException;

    void write(Object object, HttpServletRequest request, HttpServletResponse response) throws IOException;

}
