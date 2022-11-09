package soya.framework.quartz.actions;

import soya.framework.action.ActionDefinition;
import soya.framework.action.MediaType;

@ActionDefinition(
        domain = "quartz-scheduler",
        name = "scheduler-info",
        path = "/scheduler",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.TEXT_PLAIN
)
public class SchedulerInfoAction extends QuartzSchedulerAction<String> {

    @Override
    public String execute() throws Exception {
        return schedulerManager.schedulerMetaData().getSummary();
    }
}
