package soya.framework.action.dispatch;

import soya.framework.action.Action;
import soya.framework.action.ActionDefinition;
import soya.framework.action.MediaType;

@ActionDefinition(domain = "dispatch",
        name = "generator-dispatch-class-template",
        path = "/dispatch/generator/class-template",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.APPLICATION_JSON,
        displayName = "Dispatch Class Template",
        description = "Print as markdown format.")
public class DispatchClassTemplateAction extends Action<DispatchClassTemplate> {

    @Override
    public DispatchClassTemplate execute() throws Exception {
        return new DispatchClassTemplate();
    }

}
