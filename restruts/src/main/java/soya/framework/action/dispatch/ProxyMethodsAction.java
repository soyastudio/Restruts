package soya.framework.action.dispatch;

import soya.framework.action.Action;
import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionProperty;
import soya.framework.action.MediaType;

import java.lang.reflect.Method;

@ActionDefinition(domain = "dispatch",
        name = "proxy-methods",
        path = "/dispatch/proxy/methods",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.APPLICATION_JSON,
        displayName = "Proxy Methods",
        description = "List proxy interfaces.")
public class ProxyMethodsAction extends Action<String[]> {

    @ActionProperty(parameterType = ActionProperty.PropertyType.HEADER_PARAM, required = true)
    private String proxyInterface;

    @Override
    public String[] execute() throws Exception {
        Method[] methods = Class.forName(proxyInterface).getDeclaredMethods();
        String[] signatures = new String[methods.length];
        for(int i = 0; i < methods.length; i ++) {
            signatures[i] = methods[i].toString();
        }

        return signatures;
    }
}
