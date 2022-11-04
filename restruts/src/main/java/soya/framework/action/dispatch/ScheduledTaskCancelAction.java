package soya.framework.action.dispatch;

import soya.framework.action.Action;
import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionProperty;
import soya.framework.action.MediaType;

@ActionDefinition(domain = "dispatch",
        name = "scheduled-task-cancellation",
        path = "/scheduled-task-cancellation",
        method = ActionDefinition.HttpMethod.DELETE,
        produces = MediaType.APPLICATION_JSON,
        displayName = "Scheduled Task Cancellation",
        description = "Scheduled Task Cancellation.")
public class ScheduledTaskCancelAction extends Action<Boolean> {
    @ActionProperty(
            description = {
                    "Task name."
            },
            parameterType = ActionProperty.PropertyType.HEADER_PARAM,
            required = true,
            option = "n"
    )
    private String task;

    @Override
    public Boolean execute() throws Exception {
        return DispatchScheduler.getInstance().cancel(task);
    }
}
