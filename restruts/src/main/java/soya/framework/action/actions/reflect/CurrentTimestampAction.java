package soya.framework.action.actions.reflect;

import soya.framework.action.Action;
import soya.framework.action.ActionDefinition;
import soya.framework.action.MediaType;

@ActionDefinition(
        domain = "reflect",
        name = "current-timestamp",
        path = "/runtime/current-timestamp",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.TEXT_PLAIN,
        displayName = "Current Timestamp",
        description = "Print current timestamp as long."
)
public class CurrentTimestampAction extends Action<Long> {

    @Override
    public Long execute() throws Exception {
        return System.currentTimeMillis();
    }
}
