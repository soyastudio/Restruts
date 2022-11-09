package soya.framework.action.actions.reflect;

import soya.framework.action.Action;
import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionProperty;
import soya.framework.action.MediaType;

import java.util.Date;

@ActionDefinition(domain = "reflect",
        name = "current-timestamp",
        path = "/runtime/current-timestamp",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.TEXT_PLAIN,
        displayName = "Echo",
        description = "Print input message directly.")
public class CurrentTimestampAction extends Action<Long> {

    @Override
    public Long execute() throws Exception {
        return System.currentTimeMillis();
    }
}
