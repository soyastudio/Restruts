package soya.framework.action.dispatch;

import soya.framework.action.*;

@ActionDefinition(domain = "dispatch",
        name = "scheduled-task-details",
        path = "/schedule/task-details",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.APPLICATION_JSON,
        displayName = "Scheduled Task Details",
        description = "Scheduled Task Details.")
public class ScheduledTaskDetailsAction extends Action<String> {

    @ActionProperty(
            description = {
                    "Task name."
            },
            parameterType = ActionParameterType.HEADER_PARAM,
            required = true,
            option = "n"
    )
    private String task;

    @Override
    public String execute() throws Exception {
        return DispatchScheduler.getInstance().getTaskDetails(task);
    }
}
