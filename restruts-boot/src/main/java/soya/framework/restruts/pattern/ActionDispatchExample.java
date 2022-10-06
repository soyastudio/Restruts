package soya.framework.restruts.pattern;

import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionProperty;
import soya.framework.action.MediaType;
import soya.framework.action.dispatch.ActionDispatchAction;
import soya.framework.action.dispatch.ActionDispatchPattern;
import soya.framework.action.dispatch.ActionPropertyAssignment;
import soya.framework.action.dispatch.AssignmentMethod;

@ActionDefinition(domain = "pattern",
        name = "action-dispatch-example",
        path = "/pattern/dispatch/action-dispatch-example",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN,
        displayName = "EDM Table Mapping",
        description = "EDM Table Mapping.")
@ActionDispatchPattern(uri = "albertsons://base64-encode",
        propertyAssignments = {
                @ActionPropertyAssignment(name = "message", assignmentMethod = AssignmentMethod.PARAMETER, expression = "msg")
        })
public class ActionDispatchExample extends ActionDispatchAction<String> {

        @ActionProperty(
                description = "Execution method. The method must take no arguments.",
                parameterType = ActionProperty.PropertyType.PAYLOAD,
                required = true,
                option = "m")
        private String msg;
}
