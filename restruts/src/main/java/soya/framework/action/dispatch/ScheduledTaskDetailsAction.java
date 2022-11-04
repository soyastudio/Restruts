package soya.framework.action.dispatch;

import soya.framework.action.Action;
import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionProperty;
import soya.framework.action.MediaType;

@ActionDefinition(domain = "dispatch",
        name = "scheduled-task-details",
        path = "/scheduled-task-details",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.APPLICATION_JSON,
        displayName = "Scheduled Task Details",
        description = "Scheduled Task Details.")
public class ScheduledTaskDetailsAction extends Action<String> {

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
    public String execute() throws Exception {
        return DispatchScheduler.getInstance().getTaskDetails(task);
    }
}
