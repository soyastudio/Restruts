package soya.framework.action.mvc;

import soya.framework.action.Action;

import javax.servlet.http.HttpServletRequest;

public abstract class MvcAction<T> extends Action<T> {

    private HttpServletRequest request;

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }
}
