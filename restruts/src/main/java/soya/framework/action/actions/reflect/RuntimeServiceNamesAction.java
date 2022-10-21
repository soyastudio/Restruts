package soya.framework.action.actions.reflect;

import soya.framework.action.Action;
import soya.framework.action.ActionContext;
import soya.framework.action.ActionDefinition;
import soya.framework.action.MediaType;

@ActionDefinition(domain = "reflect",
        name = "runtime-service-names",
        path = "/runtime-service-names",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.APPLICATION_JSON,
        displayName = "Service Names",
        description = "Print runtime service names.")
public class RuntimeServiceNamesAction extends Action<String[]> {

    @Override
    public String[] execute() throws Exception {
        return ActionContext.getInstance().serviceNames();
    }
}
