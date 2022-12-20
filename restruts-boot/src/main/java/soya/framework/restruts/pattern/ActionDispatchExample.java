package soya.framework.restruts.pattern;

import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionParameterType;
import soya.framework.action.ActionProperty;
import soya.framework.action.MediaType;
import soya.framework.action.dispatch.ActionDispatchAction;
import soya.framework.action.dispatch.ActionDispatchPattern;

@ActionDefinition(
        domain = "pattern",
        name = "action-dispatch-example",
        path = "/pattern/dispatch/action-dispatch-example",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN,
        displayName = "EDM Table Mapping",
        description = "EDM Table Mapping."
)
@ActionDispatchPattern(
        uri = "albertsons://base64-encode#base64decode()"
)
public class ActionDispatchExample extends ActionDispatchAction {

    @ActionProperty(
            parameterType = ActionParameterType.PAYLOAD,
            required = true,
            option = "m",
            description = "Execution method. The method must take no arguments."
    )
    private String message;
}
