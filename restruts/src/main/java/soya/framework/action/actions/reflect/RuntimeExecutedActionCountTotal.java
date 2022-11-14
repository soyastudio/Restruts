package soya.framework.action.actions.reflect;

import soya.framework.action.Action;
import soya.framework.action.ActionClass;
import soya.framework.action.ActionDefinition;
import soya.framework.action.MediaType;

@ActionDefinition(
        domain = "reflect",
        name = "total-executed-action-count",
        path = "/runtime/executed-action-count-total",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.APPLICATION_JSON,
        displayName = "Threads",
        description = "List runtime threads."
)
public class RuntimeExecutedActionCountTotal extends Action<Long> {

    @Override
    public Long execute() throws Exception {
        return ActionClass.totalExecutedActionCount();
    }
}
