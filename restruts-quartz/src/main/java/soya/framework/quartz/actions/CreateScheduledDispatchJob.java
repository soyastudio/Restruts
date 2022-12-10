package soya.framework.quartz.actions;


import soya.framework.action.*;

@ActionDefinition(
        domain = "quartz-scheduler",
        name = "create-scheduled-dispatch-job",
        path = "/scheduled-dispatch-job",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN
)
public class CreateScheduledDispatchJob extends QuartzSchedulerAction<String> {

    @ActionProperty(parameterType = ParameterType.HEADER_PARAM, required = true, option = "j")
    private String jobId;

    @ActionProperty(parameterType = ParameterType.HEADER_PARAM, required = true, option = "t")
    private String triggerId;

    @ActionProperty(parameterType = ParameterType.HEADER_PARAM, required = true, option = "x")
    private String cronExpression;

    @ActionProperty(parameterType = ParameterType.HEADER_PARAM, required = true, option = "a")
    private String actionDispatch;

    @Override
    public String execute() throws Exception {
        return schedulerManager.createScheduledJob(jobId, triggerId, cronExpression, actionDispatch).toString();
    }
}
