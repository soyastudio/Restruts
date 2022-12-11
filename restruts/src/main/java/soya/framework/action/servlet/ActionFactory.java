package soya.framework.action.servlet;

import soya.framework.action.ActionCallable;
import soya.framework.action.ActionName;

import javax.servlet.http.HttpServletRequest;

public interface ActionFactory {
    boolean contains(ActionName actionName);

    ActionCallable create(ActionMapping mapping, HttpServletRequest request);
}
