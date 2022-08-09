package soya.framework.restruts.action;

import javax.servlet.http.HttpServletRequest;

public interface ActionMappings {

    Class<? extends Action> getActionType(HttpServletRequest request);
}
