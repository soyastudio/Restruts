package soya.framework.action.dispatch;

import soya.framework.action.ActionDefinition;
import soya.framework.action.MediaType;

@ActionDefinition(domain = "dispatch",
        name = "generator-dispatch-action-class",
        path = "/dispatch/action/class",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN,
        displayName = "Dispatch Action Class Generator",
        description = "Print as markdown format.")
public class ActionDispatchClassGenerator extends DispatchClassGenerator {

}
