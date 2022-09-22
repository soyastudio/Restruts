package soya.framework.action.dispatch;

import soya.framework.action.Action;
import soya.framework.action.ActionContext;
import soya.framework.action.MediaType;
import soya.framework.action.ActionDefinition;
import soya.framework.action.dispatch.ActionProxyFactory;

@ActionDefinition(domain = "dispatch",
        name = "proxy-interfaces",
        path = "/dispatch/proxy/interfaces",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.APPLICATION_JSON,
        displayName = "Proxy Interfaces",
        description = "List proxy interfaces.")
public class ProxyInterfacesAction extends Action<String[]> {

    @Override
    public String[] execute() throws Exception {
        return ActionContext.getInstance().getService(ActionProxyFactory.class).proxyInterfaces();
    }
}