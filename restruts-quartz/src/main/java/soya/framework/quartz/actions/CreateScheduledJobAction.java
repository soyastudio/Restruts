package soya.framework.quartz.actions;


import soya.framework.action.Action;

public class CreateScheduledJobAction extends Action<String> {

    private String jobId;

    private String triggerId;

    private String cronExpression;

    private String ActionDispatch;

    @Override
    public String execute() throws Exception {
        return null;
    }
}
