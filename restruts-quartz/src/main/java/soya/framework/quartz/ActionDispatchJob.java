package soya.framework.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import soya.framework.action.dispatch.ActionDispatch;

public final class ActionDispatchJob implements Job {
    public static final String ACTION_DISPATCH = "ACTION_DISPATCH";

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        ActionDispatch
                .fromURI(jobExecutionContext.getJobDetail().getJobDataMap().getString(ACTION_DISPATCH))
                .create(jobExecutionContext)
                .call();
    }
}
