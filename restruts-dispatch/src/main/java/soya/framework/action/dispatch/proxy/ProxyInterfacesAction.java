package soya.framework.action.dispatch.proxy;

import soya.framework.action.Action;
import soya.framework.action.ActionContext;
import soya.framework.action.ActionDefinition;
import soya.framework.action.MediaType;

@ActionDefinition(domain = "dispatch",
        name = "proxy-interfaces",
        path = "/proxy/interfaces",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.APPLICATION_JSON,
        displayName = "Generic Action Dispatch",
        description = "Generic action dispatch action.")
public class ProxyInterfacesAction extends Action<String[]> {
    @Override
    public String[] execute() throws Exception {
        return ActionContext.getInstance().getService(ActionProxyFactory.class).proxyInterfaces();
    }
}
