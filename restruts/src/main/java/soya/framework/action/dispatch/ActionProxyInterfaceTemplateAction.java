package soya.framework.action.dispatch;

import soya.framework.action.ActionDefinition;
import soya.framework.action.MediaType;

@ActionDefinition(domain = "dispatch",
        name = "/generator-action-proxy-interface-template",
        path = "/dispatch/generator/action-proxy-interface",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.TEXT_PLAIN,
        displayName = "Pipeline Action Class Generator",
        description = "Print as markdown format.")
@ActionMapping(
        uri = "dispatch://resource",
        parameters = {
                @ActionParameter(name = "uri",
                        assignmentMethod = AssignmentMethod.VALUE,
                        expression = "classpath://template/ActionProxyInterface.json")
        }
)
public class ActionProxyInterfaceTemplateAction extends ActionDispatchAction<String> {

}
