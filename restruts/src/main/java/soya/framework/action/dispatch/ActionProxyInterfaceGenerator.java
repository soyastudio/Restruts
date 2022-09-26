package soya.framework.action.dispatch;

import soya.framework.action.ActionDefinition;
import soya.framework.action.MediaType;

@ActionDefinition(domain = "dispatch",
        name = "/generator-action-proxy-interface",
        path = "/dispatch/generator/action-proxy-interface",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN,
        displayName = "Pipeline Action Class Generator",
        description = "Print as markdown format.")
public class ActionProxyInterfaceGenerator extends DispatchClassGenerator {

    @Override
    public String execute() throws Exception {
        return null;
    }
}
