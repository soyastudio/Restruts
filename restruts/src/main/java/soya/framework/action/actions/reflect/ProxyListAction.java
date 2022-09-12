package soya.framework.action.actions.reflect;

import soya.framework.action.Action;
import soya.framework.action.ActionContext;
import soya.framework.action.MediaType;
import soya.framework.action.OperationMapping;
import soya.framework.action.patterns.ActionProxyFactory;

@OperationMapping(domain = "about",
        name = "proxy-interfaces",
        path = "/about/proxies",
        method = OperationMapping.HttpMethod.GET,
        produces = MediaType.APPLICATION_JSON,
        displayName = "Proxy Interfaces",
        description = "List proxy interfaces.")
public class ProxyListAction extends Action<String[]> {

    @Override
    public String[] execute() throws Exception {
        return ActionContext.getInstance().getService(ActionProxyFactory.class).proxyInterfaces();
    }
}
