package soya.framework.restruts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ExceptionHandler<T extends Throwable> {
    void onException(T t, HttpServletRequest request, HttpServletResponse response);
}
