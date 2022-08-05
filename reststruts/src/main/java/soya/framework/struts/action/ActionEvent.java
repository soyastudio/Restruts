package soya.framework.struts.action;

import javax.servlet.http.HttpServletRequest;
import java.util.EventObject;

public final class ActionEvent extends EventObject {
    private String method;
    private Object payload;

    public ActionEvent(HttpServletRequest request) {
        super(request);
        this.method = request.getMethod();
        request.getPathInfo();
    }

    public <T> T getPayload(Class<T> type) {
        return null;
    }
}
