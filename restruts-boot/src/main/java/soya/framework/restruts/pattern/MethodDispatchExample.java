package soya.framework.restruts.pattern;

import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionProperty;
import soya.framework.action.MediaType;
import soya.framework.action.dispatch.EvaluationMethod;
import soya.framework.action.dispatch.MethodDispatchAction;
import soya.framework.action.dispatch.MethodDispatchPattern;
import soya.framework.action.dispatch.MethodParameterAssignment;

@ActionDefinition(domain = "pattern",
        name = "method-dispatch-example",
        path = "/pattern/dispatch/method-dispatch-example",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN,
        displayName = "EDM Table Mapping",
        description = "EDM Table Mapping.")
@MethodDispatchPattern(type = Workshop.class,
        methodName = "base64Encode",
        parameterAssignments = {
                @MethodParameterAssignment(type = String.class,
                        assignmentMethod = EvaluationMethod.PARAMETER,
                        expression = "msg")
        }
)
public class MethodDispatchExample extends MethodDispatchAction<String> {

    @ActionProperty(
            description = "Execution method. The method must take no arguments.",
            parameterType = ActionProperty.PropertyType.PAYLOAD,
            required = true,
            option = "m")
    private String msg;
}
