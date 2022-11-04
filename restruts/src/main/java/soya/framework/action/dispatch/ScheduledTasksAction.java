package soya.framework.action.dispatch;

import soya.framework.action.Action;
import soya.framework.action.ActionDefinition;
import soya.framework.action.MediaType;

@ActionDefinition(domain = "dispatch",
        name = "scheduled-tasks",
        path = "/scheduled-tasks",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.APPLICATION_JSON,
        displayName = "Scheduled Tasks",
        description = "Scheduled task names.")
public class ScheduledTasksAction extends Action<String[]> {

    @Override
    public String[] execute() throws Exception {
        return DispatchScheduler.getInstance().taskNames();
    }
}
