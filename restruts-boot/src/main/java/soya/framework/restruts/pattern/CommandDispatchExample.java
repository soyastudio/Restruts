package soya.framework.restruts.pattern;

import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionProperty;
import soya.framework.action.MediaType;
import soya.framework.action.dispatch.ActionPropertyAssignment;
import soya.framework.action.dispatch.EvaluationMethod;
import soya.framework.action.dispatch.CommandDispatchAction;
import soya.framework.action.dispatch.CommandDispatchPattern;

@ActionDefinition(domain = "pattern",
        name = "command-dispatch-example",
        path = "/pattern/dispatch/command-dispatch-example",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN,
        displayName = "EDM Table Mapping",
        description = "EDM Table Mapping.")
@CommandDispatchPattern(
        commandType = MethodDispatchExample.class,
        methodName = "call",
        propertyAssignments = {
                @ActionPropertyAssignment(name = "msg", assignmentMethod = EvaluationMethod.PARAMETER, expression = "message")
        })
public class CommandDispatchExample extends CommandDispatchAction<String> {

    @ActionProperty(
            description = "Execution method. The method must take no arguments.",
            parameterType = ActionProperty.PropertyType.PAYLOAD,
            required = true,
            option = "m")
    private String message;
}
