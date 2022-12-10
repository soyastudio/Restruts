package soya.framework.quartz.actions;


import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionProperty;
import soya.framework.action.MediaType;
import soya.framework.action.ParameterType;

@ActionDefinition(
        domain = "quartz-scheduler",
        name = "scheduled-dispatch-job-details",
        path = "/scheduled-dispatch-job",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.TEXT_PLAIN
)
public class GetScheduledDispatchJobDetails extends QuartzSchedulerAction<String> {

    @ActionProperty(parameterType = ParameterType.HEADER_PARAM, required = true, option = "j")
    private String jobId;

    @Override
    public String execute() throws Exception {
        return schedulerManager.getDispatchJobDetails(jobId);
    }
}
