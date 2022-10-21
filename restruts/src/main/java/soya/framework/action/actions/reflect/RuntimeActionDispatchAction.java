package soya.framework.action.actions.reflect;

import soya.framework.action.*;

import java.util.ArrayList;
import java.util.List;

@ActionDefinition(domain = "reflect",
        name = "runtime-action-execution",
        path = "/runtime-action-execution",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.APPLICATION_JSON,
        displayName = "Runtime Action Execution",
        description = "Runtime Action Execution.")
public class RuntimeActionDispatchAction extends Action<String> {

    @ActionProperty(
            parameterType = ActionProperty.PropertyType.PAYLOAD,
            required = true,
            option = "c",
            description = "Action execution command in yaml, commandline or uri format."

    )
    private String commandline;

    @Override
    public String execute() throws Exception {

        List<String> list = new ArrayList<>();
        Object result = ActionExecutor.executor(list.toArray(new String[list.size()])).execute();

        return result.toString();

    }
}
