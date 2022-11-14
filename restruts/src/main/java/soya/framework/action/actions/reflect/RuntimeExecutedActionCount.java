package soya.framework.action.actions.reflect;

import soya.framework.action.*;

import java.net.URI;

@ActionDefinition(
        domain = "reflect",
        name = "executed-action-count",
        path = "/runtime/executed-action-count",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.APPLICATION_JSON,
        displayName = "Threads",
        description = "List runtime threads."
)
public class RuntimeExecutedActionCount extends Action<Long> {

    @ActionProperty(
            parameterType = ActionProperty.PropertyType.HEADER_PARAM,
            required = true,
            option = "a",
            description = "Prefix for filtering."

    )
    private String actionName;

    @Override
    public Long execute() throws Exception {
        return ActionClass.getExecutedActionCount(ActionName.fromURI(URI.create(actionName)));
    }
}
